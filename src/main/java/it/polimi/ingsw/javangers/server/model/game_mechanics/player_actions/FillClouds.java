package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;

import java.util.List;

/**
 * Class representing the fill clouds action.
 */
public class FillClouds implements ActionStrategy {
    /**
     * Number of students to insert in clouds.
     */
    private final int numberOfStudents;

    /**
     * Constructor for fill clouds action, initializing number of students.
     *
     * @param numberOfStudents students number to add to clouds
     */
    public FillClouds(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    /**
     * Action implementation for filling clouds.
     *
     * @param gameState game state instance
     * @param username  player username
     */
    @Override
    public void doAction(GameState gameState, String username) {
        StudentsBag studentsBag = gameState.getStudentsBag();
        List<TokenColor> studentsBagTokens = studentsBag.getTokenContainer().getTokens();
        for (Cloud cloud : gameState.getCloudsList()) {
            if (studentsBagTokens.isEmpty()) break;
            cloud.getTokenContainer().addTokens(studentsBag.grabTokens(this.numberOfStudents));
        }
    }
}
