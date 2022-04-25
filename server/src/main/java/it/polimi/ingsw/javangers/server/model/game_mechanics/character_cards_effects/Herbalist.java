package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class that represents the herbalist character card.
 */
public class Herbalist implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Index of the island where the inhibition will be set.
     */
    private final int islandIndex;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Herbalist class, initializing the index of the island where the inhibition will be set.
     *
     * @param islandIndex index of the island where the inhibition will be set
     */
    @JsonCreator
    public Herbalist(@JsonProperty("islandIndex") int islandIndex) {
        this.islandIndex = islandIndex;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the herbalist character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        CharacterCard herbalist = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        Island island = gameEngine.getGameState().getArchipelago().getIslands().get(this.islandIndex);
        if (herbalist.getMultipurposeCounter() == 0 || island.getEnabled() > 0)
            throw new IllegalStateException("Impossible activation of the card because inhibition tokens are finished or because island is already disabled");
        island.setEnabled(island.getEnabled() + 1);
        herbalist.setMultipurposeCounter(herbalist.getMultipurposeCounter() - 1);
    }
    //endregion
}
