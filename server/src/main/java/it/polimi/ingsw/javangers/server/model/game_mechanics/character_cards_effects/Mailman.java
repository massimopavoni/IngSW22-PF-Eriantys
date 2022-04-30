package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class representing the mailman character card effect.
 */
public class Mailman implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the mailman character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        CharacterCard mailman = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        gameEngine.setAdditionalMotherNatureSteps(mailman.getMultipurposeCounter());
    }
    //endregion
}
