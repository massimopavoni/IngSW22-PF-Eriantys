package it.polimi.ingsw.javangers.client.view;

import it.polimi.ingsw.javangers.client.controller.MessageType;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing a client view.
 */
public abstract class View {
    /**
     * Regex for valid usernames.
     */
    protected static final String USERNAME_REGEX = "^\\w{4,32}$";
    /**
     * Minimum number of players.
     */
    protected static final int MIN_PLAYERS_NUMBER = 2;
    /**
     * Maximum number of players.
     */
    protected static final int MAX_PLAYERS_NUMBER = 3;
    /**
     * Map of available wizard types.
     */
    protected static final Map<String, String> AVAILABLE_WIZARD_TYPES = Map.of(
            "d", "DRUID",
            "k", "KING",
            "w", "WITCH",
            "s", "SENSEI");
    /**
     * Mappings for wizard types.
     */
    protected static final Map<String, String> WIZARD_TYPES_MAPPINGS = Map.of(
            "d", "Druid",
            "k", "King",
            "w", "Witch",
            "s", "Sensei");
    /**
     * Map of available tower colors.
     */
    protected static final Map<String, String> AVAILABLE_TOWER_COLORS = Map.of(
            "w", "WHITE",
            "b", "BLACK",
            "g", "GRAY");
    /**
     * Mappings for tower colors.
     */
    protected static final Map<String, String> TOWER_COLORS_MAPPINGS = Map.of(
            "w", "White",
            "b", "Black",
            "g", "Gray");
    /**
     * Map of available token colors.
     */
    protected static final Map<String, String> AVAILABLE_TOKEN_COLORS = Map.of(
            "y", "YELLOW_ELF",
            "b", "BLUE_UNICORN",
            "g", "GREEN_FROG",
            "r", "RED_DRAGON",
            "p", "PINK_FAIRY");
    /**
     * Mappings for token colors.
     */
    protected static final Map<String, String> TOKEN_COLORS_MAPPINGS = Map.of(
            "y", "Yellow Elf",
            "b", "Blue Unicorn",
            "g", "Green Frog",
            "r", "Red Dragon",
            "p", "Pink Fairy");
    /**
     * Mappings for possible endgames.
     */
    protected static final Map<String, String> POSSIBLE_ENDGAMES = Map.of(
            "ALL_TOWERS", "Player placed all of their towers",
            "FEW_ISLANDS", "Archipelago was reduced to too few islands",
            "EMPTY_BAG", "Students bag was emptied",
            "NO_ASSISTANTS", "No assistant cards left"
    );
    /**
     * Locking object for view update wait.
     */
    protected final Object updateLock = new Object();
    /**
     * Directives dispatcher instance for this view.
     */
    protected final DirectivesDispatcher directivesDispatcher;
    /**
     * Directives parser instance for this view.
     */
    protected final DirectivesParser directivesParser;
    /**
     * Flag for game creator.
     */
    protected boolean gameCreator = false;
    /**
     * Message type of previous directive.
     */
    protected MessageType previousMessageType;
    /**
     * List of winners.
     */
    protected List<String> winners = Collections.emptyList();
    /**
     * Chosen number of players for the game.
     */
    protected int exactPlayersNumber;
    /**
     * Chosen expert mode flag.
     */
    protected boolean expertMode;
    /**
     * Chosen player username.
     */
    protected String username;
    /**
     * Chosen player wizard type.
     */
    protected String wizardType;
    /**
     * Chosen player tower color
     */
    protected String towerColor;

    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected View(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        this.directivesDispatcher = directivesDispatcher;
        this.directivesParser = directivesParser;
        this.directivesParser.setView(this);
        this.startMainLoopThread();
    }

    /**
     * Verify if chosen username matches provided regex.
     *
     * @param username player username
     * @return true if username is valid, false otherwise
     */
    protected static boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    /**
     * Method called to unlock the view update wait object.
     */
    public void unlockUpdate() {
        synchronized (this.updateLock) {
            this.updateLock.notifyAll();
        }
    }

    /**
     * Method called to reset the view properties.
     */
    protected void reset() {
        this.gameCreator = false;
        this.previousMessageType = null;
        this.winners = Collections.emptyList();
        this.exactPlayersNumber = 0;
        this.expertMode = false;
        this.username = "";
        this.wizardType = "";
        this.towerColor = "";
    }

    /**
     * Method called to start the view.
     *
     * @param args arguments
     */
    public abstract void main(String[] args);

    /**
     * Method called to create the game.
     */
    protected abstract void createGame();

    /**
     * Method called to join the game.
     */
    protected abstract void joinGame();

    /**
     * Method called to wait for game start.
     */
    protected abstract void waitForStart();

    /**
     * Method called to start the game.
     */
    protected abstract void startGame();

    /**
     * Method called to start showing the game.
     */
    protected abstract void startShow();

    /**
     * Method called to update the view game information.
     */
    protected abstract void updateGame();

    /**
     * Method called to show abort.
     *
     * @param message abort message
     */
    protected abstract void showAbort(String message);

    /**
     * Method called to show an error.
     *
     * @param message error message
     */
    protected abstract void showError(String message);

    /**
     * Method called to show the game end.
     */
    protected abstract void closeGame(List<String> winners);

    /**
     * Method called to show actions options.
     */
    protected abstract void enableActions();

    /**
     * Method called to show waiting for turn.
     */
    protected abstract void waitTurn();

    /**
     * Method called to go back to starting choice of create or player.
     */
    protected abstract void returnToMainMenu();

    /**
     * Check if message content for create or player is valid, and wait for start.
     *
     * @param messageContent message content
     */
    protected void checkWaitForStart(String messageContent) {
        if (!messageContent.equals("OK"))
            throw new ViewException("Unexpected non-ok message content");
        this.waitForStart();
    }

    /**
     * Check if client has to start the game.
     *
     * @param messageContent message content
     */
    protected void checkStart(String messageContent) {
        if (messageContent.equals("FULL")) {
            if (this.gameCreator)
                this.startGame();
        } else if (this.previousMessageType != MessageType.START)
            this.checkWaitForStart(messageContent);
    }

    /**
     * Update winners after action.
     */
    protected void checkWinners() {
        try {
            this.winners = this.directivesParser.getWinners();
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new ViewException(String.format("Directives parser error (%s)", e.getMessage()), e);
        }
    }

    /**
     * Check if message content is empty.
     *
     * @param messageContent message content
     */
    protected void checkEmptyContent(String messageContent) {
        if (messageContent.isEmpty())
            throw new ViewException("Unexpected empty message content");
    }

    /**
     * Continue game if winners is empty.
     */
    protected void continueGame() {
        if (!this.winners.isEmpty()) {
            if (this.directivesParser.getCurrentPlayer().equals(this.username)) {
                this.enableActions();
            } else {
                this.waitTurn();
            }
        }
    }

    /**
     * Main loop thread method, for updating view according to received directive type.
     */
    protected void startMainLoopThread() {
        new Thread(() -> {
            while (this.winners.isEmpty()) {
                try {
                    synchronized (this.updateLock) {
                        this.updateLock.wait();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new ViewException(String.format("View update wait was interrupted (%s)", e.getMessage()), e);
                }
                String messageContent = this.directivesParser.getMessageContent();
                switch (this.directivesParser.getMessageType()) {
                    case CREATE -> {
                        this.checkWaitForStart(messageContent);
                        this.gameCreator = true;
                    }
                    case PLAYER -> this.checkStart(messageContent);
                    case START -> {
                        this.startShow();
                        this.continueGame();
                    }
                    case ACTION -> {
                        this.updateGame();
                        this.checkWinners();
                        this.continueGame();
                    }
                    case ABORT -> {
                        this.checkEmptyContent(messageContent);
                        this.showAbort(messageContent);
                        this.reset();
                        this.returnToMainMenu();
                    }
                    case ERROR -> {
                        this.checkEmptyContent(messageContent);
                        this.showError(messageContent);
                        if (this.previousMessageType == MessageType.CREATE || this.previousMessageType == MessageType.PLAYER) {
                            this.reset();
                            this.returnToMainMenu();
                        } else {
                            this.continueGame();
                        }
                    }
                }
            }
            this.closeGame(this.winners);
        }).start();
    }

    /**
     * Runtime exception for errors within any view class.
     */
    public static class ViewException extends RuntimeException {
        /**
         * ViewException constructor with message.
         *
         * @param message message to be shown
         */
        public ViewException(String message) {
            super(message);
        }

        /**
         * ViewException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public ViewException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
