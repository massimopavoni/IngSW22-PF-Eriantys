package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;

import java.util.List;

/**
 * Class sto fill clouds.
 */
public class FillClouds implements ActionStrategy {

    /**
     * Number of students to insert in clouds.
     */
    private final int numberOfStudents;

    /**
     * Constructor of FillClouds.
     *
     * @param numberOfStudents to insert in clouds
     */
    public FillClouds(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    /**
     * Action to fill clouds.
     *
     * @param gameState status of the game
     * @param username  nickname of the player
     */
    @Override
    public void doAction(GameState gameState, String username) {
        List<Cloud> cloudList = gameState.getCloudsList();
        StudentsBag studentsBag = gameState.getStudentsBag();
        for (Cloud cloud :
                cloudList) {
            if (studentsBag.getTokenContainer().getTokens().size() < 1)
                break;
            else if (studentsBag.getTokenContainer().getTokens().size() < this.numberOfStudents)
                cloud.getTokenContainer().addTokens(studentsBag.grabTokens(studentsBag.getTokenContainer().getTokens().size()));
            else
                cloud.getTokenContainer().addTokens(studentsBag.grabTokens(this.numberOfStudents));
        }
    }
}
