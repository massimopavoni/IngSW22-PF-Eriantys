package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.List;

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
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void doAction(GameEngine gameEngine, String username) {
        List<Cloud> clouds = gameEngine.getGameState().getClouds();
        Cloud selectedCloud = clouds.get(this.cloudIndex);
        if (selectedCloud.getTokenContainer().getTokens().isEmpty()
                && clouds.stream().anyMatch(cloud -> !cloud.getTokenContainer().getTokens().isEmpty()))
            throw new IllegalStateException("There is a cloud that is not empty");
        gameEngine.getGameState().getPlayerDashboards().get(username).getEntrance()
                .addTokens(selectedCloud.grabTokens());
    }
    //endregion
}
