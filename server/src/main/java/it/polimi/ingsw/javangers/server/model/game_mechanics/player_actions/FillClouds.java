package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.List;

/**
 * Class representing the fill clouds action.
 */
public class FillClouds implements ActionStrategy {
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
            cloud.getTokenContainer().addTokens(studentsBag.grabTokens(gameEngine.getGameConfiguration().getStudentsPerCloud()));
        }
    }
    //endregion
}
