package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.Archipelago;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.Map;

/**
 * Class representing the action of moving mother nature.
 */
public class MoveMotherNature implements ActionStrategy {
    /**
     * Requested number of steps to move.
     */
    public final int steps;

    /**
     * Constructor for move mother nature action, initializing the number of steps to move.
     *
     * @param steps number of steps to move
     */
    public MoveMotherNature(int steps) {
        this.steps = steps;
    }

    /**
     * Action implementation for moving mother nature
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void doAction(GameEngine gameEngine, String username) {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<String, PlayerDashboard> playerDashboards = gameEngine.getGameState().getPlayerDashboards();
        if (this.steps < 1 || this.steps > playerDashboards.get(username).getLastDiscardedAssistantCard().getValue().getSteps()
                + gameEngine.getAdditionalMotherNatureSteps())
            throw new IllegalStateException("Illegal number of steps");
        // Get new mother nature position with cyclic movement
        archipelago.setMotherNaturePosition(
                (archipelago.getMotherNaturePosition() + this.steps) % archipelago.getIslands().size());
        // Trigger island power changes
        gameEngine.changeIslandPower(archipelago.getMotherNaturePosition(), username);
    }
}