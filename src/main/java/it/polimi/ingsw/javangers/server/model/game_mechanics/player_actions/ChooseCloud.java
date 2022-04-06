package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;

/**
 * Class representing choose clouds action.
 */
public class ChooseCloud implements ActionStrategy{
    /**
     *  Index of the chosen cloud
     */
    private final int cloudIndex;

    /**
     * Constructor for choose cloud action, initializing cloud index.
     * @param cloudIndex
     */
    public ChooseCloud(int cloudIndex) {
        this.cloudIndex = cloudIndex;
    }

    /**
     * Action implementation for choosing clouds.
     *
     * @param gameState game state instance
     * @param username player username
     */
    @Override
    public void doAction(GameState gameState, String username) {
        gameState.getPlayerDashboards().get(username).getEntrance()
                .addTokens(gameState.getCloudsList().get(cloudIndex).grabTokens());
    }
}
