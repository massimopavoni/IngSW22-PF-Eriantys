package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.List;

/**
 * Class representing the queen character card.
 */
public class Queen implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Tokens that will be moved from this card to the hall.
     */
    private final List<TokenColor> tokensToHall;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Queen class, initializing the list of tokens that will be moved.
     *
     * @param tokensToHall list of tokens that will be moved from this card to the hall
     */
    @JsonCreator
    public Queen(@JsonProperty("tokensToHall") List<TokenColor> tokensToHall) {
        this.tokensToHall = tokensToHall;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the queen character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        CharacterCard queen = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        if (queen.getTokenContainer().getTokens().isEmpty() ||
                this.tokensToHall.size() > queen.getMultipurposeCounter()) {
            throw new IllegalStateException("Impossible activation of the card because token container is empty or size of the list is out of bound");
        }
        GameState gameState = gameEngine.getGameState();
        gameState.getPlayerDashboards().get(username)
                .getHall().addTokens(queen.getTokenContainer().extractTokens(this.tokensToHall));
        queen.getTokenContainer().addTokens(gameState.getStudentsBag().grabTokens(queen.getMultipurposeCounter()));
        gameEngine.changeTeachersPower(username);
    }
    //endregion
}
