package it.polimi.ingsw.javangers.client.launcher;

import it.polimi.ingsw.javangers.client.controller.MessageHandler;
import it.polimi.ingsw.javangers.client.controller.NetworkManager;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.cli.CLI;
import it.polimi.ingsw.javangers.client.view.cli.CLIConstants;
import it.polimi.ingsw.javangers.client.view.gui.GUILauncherApplication;
import it.polimi.ingsw.javangers.client.view.gui.GUILauncherController;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class representing the launcher of the client.
 */
public class ClientLauncher {
    /**
     * Input scanner for cli launcher.
     */
    private static final Scanner input = new Scanner(System.in);
    /**
     * List of available view interfaces.
     */
    private static final List<String> availableInterfaces = Arrays.asList("cli", "gui");
    /**
     * Default server host address.
     */
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    /**
     * Default server listening port.
     */
    private static final int DEFAULT_SERVER_PORT = 50666;

    /**
     * Method used to prompt user for server address.
     *
     * @return server address string
     */
    private static String chooseServerAddress() {
        System.out.printf("> Please specify server address [default: %s127.0.0.1%s]: ", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
        String serverAddress = input.nextLine();
        return !serverAddress.isEmpty() ? serverAddress : DEFAULT_SERVER_ADDRESS;
    }

    /**
     * Method used to prompt user for server port.
     *
     * @return server port number
     */
    public static int chooseServerPort() {
        String serverPortString = "";
        int serverPort = -1;
        System.out.printf("> Please specify server port [default: %s50666%s]: ", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
        while (serverPort < 0 || serverPort > 65535) {
            try {
                serverPortString = input.nextLine();
                serverPort = !serverPortString.isEmpty() ? Integer.parseInt(serverPortString) : DEFAULT_SERVER_PORT;
                if (serverPort < 0 || serverPort > 65535) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.printf("> Invalid input, please specify server port [default: %s50666%s]: ", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
            }
        }
        return serverPort;
    }

    /**
     * Method used to prompt user for view interface.
     *
     * @return view interface string
     */
    public static String chooseGameplayInterface() {
        String gameplayInterface = "";
        System.out.printf("> Select preferred gameplay interface [%scli%s/%sgui%s]: ", CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
        while (gameplayInterface.isEmpty()) {
            gameplayInterface = input.nextLine().toLowerCase();
            if (!availableInterfaces.contains(gameplayInterface)) {
                System.out.printf("> Invalid input, please select preferred gameplay interface [%scli%s/%sgui%s]: ", CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
                gameplayInterface = "";
            }
        }
        return gameplayInterface;
    }

    /**
     * Main method used to launch client.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        String serverAddress;
        int serverPort;
        DirectivesParser directivesParser = null;
        NetworkManager networkManager = null;
        DirectivesDispatcher directivesDispatcher = null;
        System.out.printf("%sWelcome to Eriantys%s%n%n", CLIConstants.ANSI_BRIGHT_MAGENTA, CLIConstants.ANSI_RESET);
        while (directivesParser == null || directivesDispatcher == null) {
            serverAddress = chooseServerAddress();
            serverPort = chooseServerPort();
            try {
                directivesParser = DirectivesParser.getInstance();
                networkManager = NetworkManager.getInstance(serverAddress, serverPort, directivesParser);
                new Thread(networkManager).start();
                directivesDispatcher = DirectivesDispatcher.getInstance(MessageHandler.getInstance(networkManager));
            } catch (NetworkManager.NetworkManagerException e) {
                System.out.printf("%sNetwork error (%s)%s%n", CLIConstants.ANSI_BRIGHT_RED, e.getMessage(), CLIConstants.ANSI_RESET);
                System.out.printf("%sPlease try again%s%n", CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
            } catch (DirectivesParser.DirectivesParserException |
                     DirectivesDispatcher.DirectivesDispatcherException e) {
                throw new ClientLauncherException(String.format("Error while starting client (%s)", e.getMessage()), e);
            }
        }
        System.out.printf("%sConnected!%s%n", CLIConstants.ANSI_BRIGHT_GREEN, CLIConstants.ANSI_RESET);
        String gameplayInterface = chooseGameplayInterface();
        switch (gameplayInterface) {
            case "cli" -> {
                new CLI(directivesDispatcher, directivesParser);
                new Thread(() -> CLI.main(args)).start();
            }
            case "gui" -> {
                GUILauncherApplication.setDirectives(directivesDispatcher, directivesParser);
                new Thread(() -> GUILauncherApplication.main(args)).start();
            }
            default ->
                    throw new ClientLauncherException(String.format("Invalid gameplay interface (%s)", gameplayInterface));
        }
    }

    /**
     * Runtime exception for errors within client launcher class.
     */
    public static class ClientLauncherException extends RuntimeException {
        /**
         * ClientLauncherException constructor with message.
         *
         * @param message message to be shown
         */
        public ClientLauncherException(String message) {
            super(message);
        }

        /**
         * ClientLauncherException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public ClientLauncherException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
