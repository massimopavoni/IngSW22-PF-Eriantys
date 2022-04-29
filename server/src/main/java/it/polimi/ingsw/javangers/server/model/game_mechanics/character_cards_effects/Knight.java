package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class representing the knight character card effect.
 */
public class Knight implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the knight character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        CharacterCard knight = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        gameEngine.setAdditionalPower(knight.getMultipurposeCounter());
    }
    //endregion
}
