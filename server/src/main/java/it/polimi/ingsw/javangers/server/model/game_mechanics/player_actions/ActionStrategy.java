package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Player action strategy interface.
 */
public interface ActionStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Method prototypes

    /**
     * Prototype method for action implementation.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    void doAction(GameEngine gameEngine, String username);
    //endregion
}
