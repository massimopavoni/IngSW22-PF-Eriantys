package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

/**
 * Enumeration of the different endgame types.
 */
public enum Endgame {
    /**
     * No endgame, the game continues.
     */
    NONE,
    /**
     * Game ends when a player places all towers.
     */
    ALL_TOWERS,
    /**
     * Game ends when islands get merged into three or less.
     */
    FEW_ISLANDS,
    /**
     * Game ends when there are no more tokens in the bag.
     */
    EMPTY_BAG,
    /**
     * Game ends when a player has no more assistant cards.
     */
    NO_ASSISTANTS
}
