package it.polimi.ingsw.javangers.server.launcher;


import it.polimi.ingsw.javangers.server.controller.network.ConnectionsPool;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.Deflater;

public class ServerLauncher {

    private static final Map<String, Integer> DEFAULT_SERVER_ARGS;
    static {
        Map<String, Integer> defaultServerArgs = new HashMap<>();
        defaultServerArgs.put("-p", 50666);
        defaultServerArgs.put("-mc", 4);
        defaultServerArgs.put("-pt", 500);
        DEFAULT_SERVER_ARGS = Collections.unmodifiableMap(defaultServerArgs);
    }

    private static ConnectionsPool connectionsPool;


    public ServerLauncher(int port, int maxConnections) {
        try {
            connectionsPool = ConnectionsPool.getInstance(port, maxConnections);
            new Thread(connectionsPool).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        List<String> availableArgs = new ArrayList<>(Arrays.asList("-p", "-mc", "-pt"));
        Map<String, Integer> serverArgs = new HashMap<>(DEFAULT_SERVER_ARGS);
        for (String arg : args) {
                String[] argArgs = arg.split("=");
                if (availableArgs.remove(argArgs[0]))
                    serverArgs.put(argArgs[0], Integer.parseInt(argArgs[1]));
        }


        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> connectionsPool.closeServerSocket())
        );
        ServerLauncher serverLauncher = new ServerLauncher(serverArgs.get("-p"), serverArgs.get("-mc"));
        serverLauncher.startServerLauncher(serverArgs.get("-pt"));

    }

    public void startServerLauncher(int pollingTime) {
        new Thread( MessageHandler.getInstance(connectionsPool, pollingTime)).start();
    }
}
