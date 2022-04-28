package it.polimi.ingsw.javangers.server.controller;

/**
 * Enumeration of the different message types.
 */
public enum MessageType {
    /**
     * Game creation.
     */
    CREATE,
    /**
     * Player addition to the game.
     */
    PLAYER,
    /**
     * Game initialization.
     */
    START,
    /**
     * Player action execution.
     */
    ACTION,
    /**
     * Game abort.
     */
    ABORT,
    /**
     * Error message.
     */
    ERROR
}
