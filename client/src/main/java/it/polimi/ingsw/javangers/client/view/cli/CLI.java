package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.MessageType;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Class representing the cli view.
 */
public class CLI extends View {
    /**
     * The scanner for user input.
     */
    private static final Scanner input = new Scanner(System.in);
    /**
     * Constant for list options.
     */
    private static final String LIST_OPTION = "- %s%s [%s]%s%n";
    /**
     * List of loading animation frames.
     */
    private static final List<String> LOADING_ANIMATION_FRAMES = List.of("▖", "▘", "▝", "▗");
    /**
     * Thread for loading animation.
     */
    private static Thread loadingThread;

    /**
     * Constructor for cli, initializing directives dispatcher and parser.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    public CLI(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        super(directivesDispatcher, directivesParser);
    }

    /**
     * Method for cli clear.
     */
    public static void clear() {
        try {
            String os = System.getProperty("os.name");
            ProcessBuilder pb;
            if (os.contains("Windows"))
                pb = new ProcessBuilder("cmd", "/c", "cls");
            else
                pb = new ProcessBuilder("clear");
            pb.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.printf("%sError while clearing console (%s)%s%n",
                    CLIConstants.ANSI_BRIGHT_RED, e.getMessage(), CLIConstants.ANSI_RESET);
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }

    /**
     * Stop loading thread animation.
     */
    private static void stopLoading() {
        if (CLI.loadingThread != null) {
            CLI.loadingThread.interrupt();
            CLI.loadingThread = null;
        }
    }

    /**
     * Make user choose the exact number of players for the game.
     */
    private void chooseExactPlayersNumber() {
        this.exactPlayersNumber = 0;
        String exactPlayersNumberString = "";
        System.out.printf("> Insert exact number of players for the game [%s2%s/%s3%s]: ",
                CLIConstants.ANSI_BRIGHT_CYAN, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
        while (this.exactPlayersNumber < MIN_PLAYERS_NUMBER || this.exactPlayersNumber > MAX_PLAYERS_NUMBER) {
            try {
                exactPlayersNumberString = CLI.input.nextLine().strip();
                this.exactPlayersNumber = Integer.parseInt(exactPlayersNumberString);
                if (exactPlayersNumber < MIN_PLAYERS_NUMBER || exactPlayersNumber > MAX_PLAYERS_NUMBER)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.printf("> Invalid input, insert exact number of players for the game [%s2%s/%s3%s]: ",
                        CLIConstants.ANSI_BRIGHT_CYAN, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
            }
        }
    }

    /**
     * Make user insert their username.
     */
    private void chooseUsername() {
        this.username = "";
        System.out.print("> Insert your username (min 4/max 32 characters, alphanumeric + underscores): ");
        while (this.username.isEmpty()) {
            this.username = CLI.input.nextLine().strip();
            if (!View.isValidUsername(this.username)) {
                this.username = "";
                System.out.print("> Invalid input, insert your username (min 4/max 32 characters, alphanumeric + underscores): ");
            }
        }
    }

    /**
     * Make user select expert mode.
     */
    private void chooseExpertMode() {
        System.out.printf("> Use expert mode? [%sY%s/%sn%s] ",
                CLIConstants.ANSI_BRIGHT_CYAN, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
        String expertModeString = CLI.input.nextLine().strip().toLowerCase();
        expertModeString = !expertModeString.isEmpty() ? expertModeString : "true";
        this.expertMode = Boolean.parseBoolean(expertModeString);
    }

    /**
     * Make user select their wizard type.
     */
    private void chooseWizardType() {
        this.wizardType = "";
        System.out.println("> Choose wizard type:");
        View.WIZARD_TYPES_MAPPINGS.forEach((key, value) -> System.out.printf(CLI.LIST_OPTION,
                CLIConstants.WIZARD_TYPES_CLI_COLORS.get(key), value, key, CLIConstants.ANSI_RESET));
        System.out.print("> ");
        while (this.wizardType.isEmpty()) {
            this.wizardType = CLI.input.nextLine().strip().toLowerCase();
            this.wizardType = View.AVAILABLE_WIZARD_TYPES.get(this.wizardType);
            if (this.wizardType == null) {
                this.wizardType = "";
                System.out.println("> Invalid input, choose wizard type:");
                View.WIZARD_TYPES_MAPPINGS.forEach((key, value) -> System.out.printf(CLI.LIST_OPTION,
                        CLIConstants.WIZARD_TYPES_CLI_COLORS.get(key), value, key, CLIConstants.ANSI_RESET));
                System.out.print("> ");
            }
        }
    }

    /**
     * Make user select their tower color.
     */
    private void chooseTowerColor() {
        this.towerColor = "";
        System.out.println("> Choose tower color:");
        View.TOWER_COLORS_MAPPINGS.forEach((key, value) -> System.out.printf(CLI.LIST_OPTION,
                CLIConstants.ANSI_BRIGHT_WHITE, value, key, CLIConstants.ANSI_RESET));
        System.out.print("> ");
        while (this.towerColor.isEmpty()) {
            this.towerColor = CLI.input.nextLine().strip().toLowerCase();
            this.towerColor = View.AVAILABLE_TOWER_COLORS.get(this.towerColor);
            if (this.towerColor == null) {
                this.towerColor = "";
                System.out.println("> Invalid input, choose tower color:");
                View.TOWER_COLORS_MAPPINGS.forEach((key, value) -> System.out.printf(CLI.LIST_OPTION,
                        CLIConstants.ANSI_BRIGHT_WHITE, value, key, CLIConstants.ANSI_RESET));
                System.out.print("> ");
            }
        }
    }

    /**
     * Main method for cli starting.
     *
     * @param args arguments
     */
    @Override
    public void main(String[] args) {
        CLI.clear();
        System.out.printf("%n%s%s%s%n%n", CLIConstants.ANSI_BRIGHT_MAGENTA, CLIConstants.ERIANTYS_TITLE, CLIConstants.ANSI_RESET);
        System.out.printf("> Do you want to create the game or join it? [%sc%s/%sj%s] ",
                CLIConstants.ANSI_BRIGHT_CYAN, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
        String choice = "";
        while (choice.isEmpty()) {
            choice = CLI.input.nextLine().strip().toLowerCase();
            switch (choice) {
                case "c" -> this.createGame();
                case "j" -> this.joinGame();
                default ->
                        System.out.printf("> Invalid input, do you want to create the game or join it? [%sc%s/%sj%s] ",
                                CLIConstants.ANSI_BRIGHT_CYAN, CLIConstants.ANSI_RESET, CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
            }
        }
    }

    /**
     * Create game method.
     */
    @Override
    protected void createGame() {
        this.chooseExactPlayersNumber();
        this.chooseUsername();
        this.chooseExpertMode();
        this.chooseWizardType();
        this.chooseTowerColor();
        this.directivesDispatcher.createGame(this.username, this.exactPlayersNumber, this.expertMode, this.wizardType, this.towerColor);
        this.previousMessageType = MessageType.CREATE;
    }

    /**
     * Join game method.
     */
    @Override
    protected void joinGame() {
        this.chooseUsername();
        this.chooseWizardType();
        this.chooseTowerColor();
        this.directivesDispatcher.addPlayer(this.username, this.wizardType, this.towerColor);
        this.previousMessageType = MessageType.PLAYER;
    }

    /**
     * Wait for start method.
     */
    @Override
    protected void waitForStart() {
        CLI.loadingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < 4; i++) {
                    System.out.printf("%sWait for game start %s%s\r",
                            CLIConstants.ANSI_BRIGHT_YELLOW, CLI.LOADING_ANIMATION_FRAMES.get(i), CLIConstants.ANSI_RESET);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        CLI.loadingThread.start();
    }

    /**
     * Start game method.
     */
    @Override
    protected void startGame() {
        this.directivesDispatcher.startGame(this.username);
        this.previousMessageType = MessageType.START;
    }

    /**
     * Start showing method.
     */
    @Override
    protected void startShow() {
        CLI.stopLoading();
        CLI.clear();
    }

    /**
     * Update game method.
     */
    @Override
    protected void updateGame() {
    }

    /**
     * Show abort method.
     *
     * @param message abort message
     */
    @Override
    protected void showAbort(String message) {
        CLI.stopLoading();
        System.out.printf("%sAbort: %s%nPress enter to continue.%s",
                CLIConstants.ANSI_BRIGHT_RED, message, CLIConstants.ANSI_RESET);
        CLI.input.nextLine();
    }

    /**
     * Show error method.
     *
     * @param message error message
     */
    @Override
    protected void showError(String message) {
        CLI.stopLoading();
        System.out.printf("%sError: %s%nPress enter to continue.%s",
                CLIConstants.ANSI_BRIGHT_RED, message, CLIConstants.ANSI_RESET);
        CLI.input.nextLine();
    }

    /**
     * Close game method.
     *
     * @param winners list of winners
     */
    @Override
    protected void closeGame(List<String> winners) {
        CLI.stopLoading();
        System.out.printf("%sGame is ended (%s)%nWinner%s: %s%nPress enter to continue.%s",
                CLIConstants.ANSI_BRIGHT_GREEN, this.directivesParser.getEndGame(), this.winners.size() == 1 ? "" : "s",
                String.join(", ", this.winners), CLIConstants.ANSI_RESET);
        CLI.input.nextLine();
    }

    /**
     * Enable actions method.
     */
    @Override
    protected void enableActions() {

    }

    /**
     * Wait turn method.
     */
    @Override
    protected void waitTurn() {
        CLI.loadingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                for (int i = 0; i < 4; i++) {
                    System.out.printf("%sWait for your turn %s%s\r",
                            CLIConstants.ANSI_BRIGHT_YELLOW, CLI.LOADING_ANIMATION_FRAMES.get(i), CLIConstants.ANSI_RESET);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        CLI.loadingThread.start();
    }

    /**
     * Return to main menu method.
     */
    @Override
    protected void returnToMainMenu() {
        this.main(null);
    }
}
