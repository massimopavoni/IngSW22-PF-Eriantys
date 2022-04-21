package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
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
    private final List<TokenColor> tokensToIsland;
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
     * @param tokensToIsland list of tokens that will be moved from this card to an island
     * @param islandIndex    index of the island where the tokens will be moved
     */
    public Monk(List<TokenColor> tokensToIsland, int islandIndex) {
        this.tokensToIsland = tokensToIsland;
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
        if (monk.getTokenContainer().getTokens().isEmpty() ||
                this.tokensToIsland.size() > monk.getMultipurposeCounter()) {
            throw new IllegalStateException("Impossible activation of the card because token container is empty or size of the list is out of bound");
        }
        GameState gameState = gameEngine.getGameState();
        gameState.getArchipelago().getIslands().get(this.islandIndex)
                .getTokenContainer().addTokens(monk.getTokenContainer().extractTokens(this.tokensToIsland));
        monk.getTokenContainer().addTokens(gameState.getStudentsBag().grabTokens(monk.getMultipurposeCounter()));
    }
    //endregion
}
