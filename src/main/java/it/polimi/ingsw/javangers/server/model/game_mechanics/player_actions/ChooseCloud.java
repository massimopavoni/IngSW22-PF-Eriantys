package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;

/**
 * Class representing choose clouds action.
 */
public class ChooseCloud implements ActionStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Index of the chosen cloud
     */
    private final int cloudIndex;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for choose cloud action, initializing cloud index.
     *
     * @param cloudIndex index of the chosen cloud
     */
    public ChooseCloud(int cloudIndex) {
        this.cloudIndex = cloudIndex;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Action implementation for choosing clouds.
     *
     * @param gameState game state instance
     * @param username  player username
     */
    @Override
    public void doAction(GameState gameState, String username) {
        gameState.getPlayerDashboards().get(username).getEntrance().addTokens(gameState.getClouds().get(cloudIndex).grabTokens());
    }
    //endregion
}
