package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.List;

/**
 * Class representing the monk character card.
 */
public class Monk implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Tokens that will be moved from this card to an island.
     */
    private final List<TokenColor> tokensFromThis;

    /**
     * Index of the island where the tokens will be moved.
     */
    private final int islandIndex;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Monk class, initializing the list of tokens that will be moved and the index of the island
     * where they will be moved.
     *
     * @param tokensFromThis list of tokens that will be moved from this card to an island
     * @param islandIndex    index of the island where the tokens will be moved
     */
    public Monk(List<TokenColor> tokensFromThis, int islandIndex) {
        this.tokensFromThis = tokensFromThis;
        this.islandIndex = islandIndex;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the monk character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        CharacterCard monk = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        if (monk.getTokenContainer().getTokens().size() <= 0 ||
                this.tokensFromThis.size() != monk.getMultipurposeCounter()) {
            throw new IllegalStateException("Impossible activation of the card because token container is empty or size of the list is out of bound");
        }
        StudentsBag bag = gameEngine.getGameState().getStudentsBag();
        Island island = gameEngine.getGameState().getArchipelago().getIslands().get(this.islandIndex);
        island.getTokenContainer().addTokens(monk.getTokenContainer().extractTokens(this.tokensFromThis));
        monk.getTokenContainer().addTokens(bag.grabTokens(monk.getMultipurposeCounter()));
    }
    //endregion
}
