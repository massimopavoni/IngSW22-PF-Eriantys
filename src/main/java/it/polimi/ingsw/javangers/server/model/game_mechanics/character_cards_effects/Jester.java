package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.List;

/**
 * Class representing the jester character card.
 */
public class Jester implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Tokens that will be moved from entrance to this card.
     */
    List<TokenColor> tokensFromEntrance;

    /**
     * Tokens that will be moved from this card to entrance.
     */
    List<TokenColor> tokensFromThis;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Jester class initializing the lists that will be exchanged.
     *
     * @param tokensFromThis     list of tokens that will be moved from this card to entrance
     * @param tokensFromEntrance list of tokens that will be moved from entrance to this card
     */
    public Jester(List<TokenColor> tokensFromEntrance, List<TokenColor> tokensFromThis) {
        this.tokensFromThis = tokensFromThis;
        this.tokensFromEntrance = tokensFromEntrance;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the jester character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        if (this.tokensFromThis.size() != this.tokensFromEntrance.size() ||
                this.tokensFromEntrance.size() > gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase()).getMultipurposeCounter()) {
            throw new IllegalStateException("Impossible activation of the card because sizes of the lists are different or out of bound");
        }
        PlayerDashboard playerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        CharacterCard characterCard = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        playerDashboard.getEntrance().addTokens(characterCard.getTokenContainer().extractTokens(tokensFromThis));
        characterCard.getTokenContainer().addTokens(playerDashboard.getEntrance().extractTokens(tokensFromEntrance));
    }
    //endregion
}
