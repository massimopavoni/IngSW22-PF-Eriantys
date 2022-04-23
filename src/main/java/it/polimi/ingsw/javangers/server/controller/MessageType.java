package it.polimi.ingsw.javangers.server.controller;

/**
 * Enumeration of the different message types.
 */
public enum MessageType {
    /**
     * Creation of the game manager instance.
     */
    CREATE,
    /**
     * Addition of a player in the game.
     */
    PLAYER,
    /**
     * Initialization of the game.
     */
    START,
    /**
     * Execution of the action of the player.
     */
    ACTION,
    /**
     * Error occurred in the deserialization.
     */
    ERROR
}
