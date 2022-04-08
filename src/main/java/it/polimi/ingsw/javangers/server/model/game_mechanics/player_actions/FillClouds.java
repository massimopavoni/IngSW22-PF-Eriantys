package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;

import java.util.List;

/**
 * Class representing the fill clouds action.
 */
public class FillClouds implements ActionStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Number of students to insert in clouds.
     */
    private final int numberOfStudents;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for fill clouds action, initializing number of students.
     *
     * @param numberOfStudents students number to add to clouds
     */
    public FillClouds(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Action implementation for filling clouds.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void doAction(GameEngine gameEngine, String username) {
        StudentsBag studentsBag = gameEngine.getGameState().getStudentsBag();
        List<TokenColor> studentsBagTokens = studentsBag.getTokenContainer().getTokens();
        for (Cloud cloud : gameEngine.getGameState().getClouds()) {
            if (studentsBagTokens.isEmpty()) break;
            cloud.getTokenContainer().addTokens(studentsBag.grabTokens(this.numberOfStudents));
        }
    }
    //endregion
}
