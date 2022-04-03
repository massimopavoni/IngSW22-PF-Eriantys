package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;

/**
 * Player action strategy interface.
 */
public interface ActionStrategy {
    /**
     * Prototype method actions implementation.
     *
     * @param gameState game state instance
     * @param username player username
     */
    void doAction(GameState gameState, String username);
}
