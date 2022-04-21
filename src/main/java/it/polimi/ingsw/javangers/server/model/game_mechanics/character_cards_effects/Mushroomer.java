package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class representing the mushroomer character card.
 */
public class Mushroomer implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Forbidden color in the computation of power on the islands.
     */
    private final TokenColor forbiddenColor;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the mushroomer class, initializing the forbidden color.
     *
     * @param forbiddenColor token color forbidden during the computation of the power on the islands
     */
    public Mushroomer(TokenColor forbiddenColor) {
        this.forbiddenColor = forbiddenColor;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the mushroomer character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        gameEngine.setForbiddenColor(this.forbiddenColor);
    }
    //endregion
}
