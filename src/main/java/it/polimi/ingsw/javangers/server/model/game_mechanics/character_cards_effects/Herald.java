package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class representing the herald character card.
 */
public class Herald implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Index of the Island where the power will be checked.
     */
    private final int islandIndex;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Herald class, initializing the index of the island where the power is checked.
     *
     * @param islandIndex index of the island where the power is checked
     */
    public Herald(int islandIndex) {
        this.islandIndex = islandIndex;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the herald character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        gameEngine.changeIslandPower(this.islandIndex, username);
    }
    //endregion
}
