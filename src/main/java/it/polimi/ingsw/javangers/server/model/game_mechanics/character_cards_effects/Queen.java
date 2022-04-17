package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
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
    private final List<TokenColor> tokensFromThis;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Queen class, initializing the list of tokens that will be moved.
     *
     * @param tokensFromThis list of tokens that will be moved from this card to the hall
     */
    public Queen(List<TokenColor> tokensFromThis) {
        this.tokensFromThis = tokensFromThis;
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
        if (queen.getTokenContainer().getTokens().size() <= 0 ||
                this.tokensFromThis.size() != queen.getMultipurposeCounter()) {
            throw new IllegalStateException("Impossible activation of the card because token container is empty or size of the list is out of bound");
        }
        if (this.tokensFromThis.size() == queen.getMultipurposeCounter()) {
            StudentsBag bag = gameEngine.getGameState().getStudentsBag();
            TokenContainer hall = gameEngine.getGameState().getPlayerDashboards().get(username).getHall();
            hall.addTokens(queen.getTokenContainer().extractTokens(this.tokensFromThis));
            queen.getTokenContainer().addTokens(bag.grabTokens(queen.getMultipurposeCounter()));
        }
    }
    //endregion
}
