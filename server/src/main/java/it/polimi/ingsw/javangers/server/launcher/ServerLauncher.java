package it.polimi.ingsw.javangers.server.launcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.controller.MessageHandler;
import it.polimi.ingsw.javangers.server.controller.network.ConnectionsPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for server bootstrap.
 */
public class ServerLauncher {
    /**
     * Server launcher logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ServerLauncher.class.getName());
    /**
     * Exception message for logging.
     */
    private static final String EXCEPTION_MESSAGE = "Logging exception:";
    /**
     * Server port argument.
     */
    private static final String PORT_ARG = "-port";
    /**
     * Server max connections' argument.
     */
    private static final String MAX_CONNECTIONS_ARG = "-maxconn";
    /**
     * Server polling interval argument.
     */
    private static final String POLLING_INTERVAL_ARG = "-polling";
    /**
     * Server resources locations argument.
     */
    private static final String RESOURCE_LOCATIONS_ARG = "-reslocations";
    /**
     * Default arguments for the server.
     */
    private static final Map<String, Integer> DEFAULT_SERVER_ARGS;
    /**
     * Default resource locations for server and game configuration.
     */
    private static final String DEFAULT_SERVER_RESOURCE_LOCATIONS = "/it/polimi/ingsw/javangers/server/launcher/server_resource_locations.json";
    /**
     * Connections pool singleton instance.
     */
    private static ConnectionsPool connectionsPool;

    // Static block for default server args map initialization.
    static {
        Map<String, Integer> defaultServerArgs = new HashMap<>();
        defaultServerArgs.put(PORT_ARG, 50666);
        defaultServerArgs.put(MAX_CONNECTIONS_ARG, 4);
        defaultServerArgs.put(POLLING_INTERVAL_ARG, 200);
        DEFAULT_SERVER_ARGS = Collections.unmodifiableMap(defaultServerArgs);
    }

    /**
     * Server bootstrap main method.
     *
     * @param args console arguments for default args override
     */
    public static void main(String[] args) {
        LOGGER.info("Server bootstrap - parsing arguments");
        List<String> availableArgs = new ArrayList<>(DEFAULT_SERVER_ARGS.keySet());
        Map<String, Integer> serverArgs = new HashMap<>(DEFAULT_SERVER_ARGS);
        String serverResourceLocations = "";
        for (String arg : args) {
            String[] argArgs = arg.split("=");
            if (availableArgs.remove(argArgs[0]))
                serverArgs.put(argArgs[0], Integer.parseInt(argArgs[1]));
            else if (argArgs[0].equals(RESOURCE_LOCATIONS_ARG))
                serverResourceLocations = argArgs[1];
        }
        if (serverResourceLocations.isEmpty())
            serverResourceLocations = DEFAULT_SERVER_RESOURCE_LOCATIONS;
        LOGGER.log(Level.INFO, "Server arguments\nPort: {0}\nMax connections: {1}" +
                        "\nResource locations: {3}\nPolling interval: {2}",
                new Object[]{serverArgs.get(PORT_ARG).toString(), serverArgs.get(MAX_CONNECTIONS_ARG).toString(),
                        serverArgs.get(POLLING_INTERVAL_ARG).toString(), serverResourceLocations});
        LOGGER.info("Server bootstrap - creating server launcher");
        ServerLauncher serverLauncher = new ServerLauncher();
        try {
            LOGGER.info("Starting connections pool");
            connectionsPool = ConnectionsPool.getInstance(serverArgs.get(PORT_ARG), serverArgs.get(MAX_CONNECTIONS_ARG));
            new Thread(connectionsPool).start();
            LOGGER.info("Server bootstrap - deserializing resource locations");
            ObjectMapper jsonMapper = new ObjectMapper();
            InputStream jsonInputStream = ServerLauncher.class.getResourceAsStream(serverResourceLocations);
            Map<String, String> serverResourceLocationsMap = jsonMapper
                    .readValue(jsonInputStream, new TypeReference<Map<String, String>>() {
                    });
            LOGGER.info("Server bootstrap - starting message handler");
            serverLauncher.startMessageHandler(serverResourceLocationsMap, serverArgs.get(POLLING_INTERVAL_ARG));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Server shutdown triggering graceful hooks");
                MessageHandler.closeMessageHandler();
                connectionsPool.closeServerSocket();
            }));
        } catch (ConnectionsPool.ConnectionsPoolException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ServerLauncherException(String.format("Error while getting connections pool (%s)", e.getMessage()), e));
            System.exit(1);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ServerLauncherException(String.format("Error while parsing server resource locations (%s)", e.getMessage()), e));
            System.exit(1);
        }
    }

    /**
     * Start message handler singleton inside main server thread.
     *
     * @param serverResourceLocations server resource locations map
     * @param pollingInterval         polling interval in milliseconds
     */
    private void startMessageHandler(Map<String, String> serverResourceLocations, int pollingInterval) {
        new Thread(MessageHandler.getInstance(connectionsPool, serverResourceLocations, pollingInterval)).start();
    }

    /**
     * Exception for errors within server launcher class.
     */
    public static class ServerLauncherException extends Exception {
        /**
         * ServerLauncherException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public ServerLauncherException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}