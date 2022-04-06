package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;

/**
 * Character card effect strategy interface.
 */
public interface EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Method prototypes

    /**
     * Prototype method for effect implementation.
     *
     * @param gameState game state instance
     * @param username  player username
     */
    void useEffect(GameState gameState, String username);
    //endregion
}
