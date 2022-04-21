package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FillCloudsTest {
    FillClouds fillClouds;
    GameEngine gameEngine;

    @BeforeEach
    void setUp() {
        fillClouds = new FillClouds();
    }

    @Test
    @DisplayName("Test do action with empty StudentsBag")
    void doAction_emptyStudentsBag() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getStudentsBag().grabTokens(130);
        fillClouds.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(0, gameEngine.getGameState().getClouds().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(0, gameEngine.getGameState().getClouds().get(1).getTokenContainer().getTokens().size())
        );
    }

    @Test
    @DisplayName("Test completely fill all cloud")
    void doAction_completelyFill() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        fillClouds.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(3, gameEngine.getGameState().getClouds().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(3, gameEngine.getGameState().getClouds().get(1).getTokenContainer().getTokens().size())
        );
    }

    @Test
    @DisplayName("Test fill clouds with remaining students")
    void doAction_remainingStudents() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getStudentsBag().grabTokens(125);
        fillClouds.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(3, gameEngine.getGameState().getClouds().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(2, gameEngine.getGameState().getClouds().get(1).getTokenContainer().getTokens().size())
        );
    }
}