package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Character card effect strategy interface.
 */
public interface EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Method prototypes

    /**
     * Prototype method for effect implementation.
     *
     * @param gameEngine game engine instance
     * @param username  player username
     */
    void useEffect(GameEngine gameEngine, String username);
    //endregion
}
