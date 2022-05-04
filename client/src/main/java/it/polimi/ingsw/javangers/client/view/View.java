package it.polimi.ingsw.javangers.client.view;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;

import java.util.List;

/**
 * Class representing a client view.
 */
public abstract class View {
    /**
     * Regex for valid usernames.
     */
    protected static final String USERNAME_REGEX = "^\\w{4,32}$";
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
     * List of winners.
     */
    protected List<String> winners;
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
     * Constructor for view, initializing directives dispatcher and parser.
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
     * Get directives dispatcher instance.
     *
     * @return directives dispatcher
     */
    protected DirectivesDispatcher getDirectivesDispatcher() {
        return this.directivesDispatcher;
    }

    /**
     * Get directives parser instance.
     *
     * @return directives parser
     */
    protected DirectivesParser getDirectivesParser() {
        return this.directivesParser;
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
        if (messageContent.equals("FULL") && this.gameCreator) {
            this.startGame();
        } else {
            this.checkWaitForStart(messageContent);
        }
    }

    /**
     * Update from action outcomes.
     */
    protected void updateFromAction() {
        this.updateGame();
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
                    case START -> this.startShow();
                    case ACTION -> this.updateFromAction();
                    case ABORT -> {
                        this.checkEmptyContent(messageContent);
                        this.showAbort(messageContent);
                    }
                    case ERROR -> {
                        this.checkEmptyContent(messageContent);
                        this.showError(messageContent);
                    }
                }
                this.continueGame();
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
