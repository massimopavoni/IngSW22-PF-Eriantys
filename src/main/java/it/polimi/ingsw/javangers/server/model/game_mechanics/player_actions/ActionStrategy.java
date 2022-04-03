package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;

/**
 * Player actions interface.
 */
public interface  ActionStrategy {
    /**
     * Action the player performs.
     *
     * @param gameState status of the game
     * @param username nickname of the player
     */
    void doAction(GameState gameState, String username);

}
