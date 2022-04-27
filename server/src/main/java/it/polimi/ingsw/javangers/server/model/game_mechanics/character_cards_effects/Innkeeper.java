package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class representing the innkeeper character card effect.
 */
public class Innkeeper implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the innkeeper character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        gameEngine.setTeachersEqualCount(true);
        gameEngine.changeTeachersPower(username);
    }
    //endregion
}
