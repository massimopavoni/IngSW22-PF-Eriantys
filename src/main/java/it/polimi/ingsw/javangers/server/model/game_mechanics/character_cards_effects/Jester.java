package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
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
    List<TokenColor> tokensToEntrance;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Jester class initializing the lists that will be exchanged.
     *
     * @param tokensFromEntrance list of tokens that will be moved from entrance to this card
     * @param tokensToEntrance   list of tokens that will be moved from this card to entrance
     */
    @JsonCreator
    public Jester(@JsonProperty("tokensFromEntrance") List<TokenColor> tokensFromEntrance, @JsonProperty("tokensToEntrance") List<TokenColor> tokensToEntrance) {
        this.tokensFromEntrance = tokensFromEntrance;
        this.tokensToEntrance = tokensToEntrance;
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
        if (this.tokensToEntrance.size() != this.tokensFromEntrance.size() ||
                this.tokensFromEntrance.size() > gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase()).getMultipurposeCounter()) {
            throw new IllegalStateException("Impossible activation of the card because sizes of the lists are different or out of bound");
        }
        TokenContainer entrance = gameEngine.getGameState().getPlayerDashboards().get(username).getEntrance();
        CharacterCard characterCard = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        entrance.addTokens(characterCard.getTokenContainer().extractTokens(tokensToEntrance));
        characterCard.getTokenContainer().addTokens(entrance.extractTokens(tokensFromEntrance));
    }
    //endregion
}
