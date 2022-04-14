package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentsTest {
    MoveStudents moveStudents;
    GameEngine gameEngine;

    @Test
    @DisplayName("Test movement to island")
    void doAction_movementToIsland() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG)));
        List<TokenColor> studentToHall = new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        Map<Integer, List<TokenColor>> studentToIsland = new HashMap<Integer, List<TokenColor>>() {{
            put(3, new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN)));
            put(4, new ArrayList<>(Arrays.asList(TokenColor.GREEN_FROG, TokenColor.GREEN_FROG)));
        }};
        moveStudents = new MoveStudents(studentToHall, studentToIsland);
        moveStudents.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(2, gameEngine.getGameState().getArchipelago().getIslands().get(3).getTokenContainer().getTokens().size()),
                () -> assertEquals(2, gameEngine.getGameState().getArchipelago().getIslands().get(4).getTokenContainer().getTokens().size()),
                () -> assertEquals(2, gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().getTokens().size()),
                () -> assertEquals("pippo", gameEngine.getGameState().getTeachers().get(TokenColor.BLUE_UNICORN).getOwnerUsername())
        );
    }

    @Test
    @DisplayName("Test Illegal Argument Exception")
    void doAction_exceptionMovement() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.GREEN_FROG)));
        List<TokenColor> studentToHall = new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        Map<Integer, List<TokenColor>> studentToIsland = new HashMap<Integer, List<TokenColor>>() {{
            put(4, new ArrayList<>(Arrays.asList(TokenColor.GREEN_FROG)));
        }};
        moveStudents = new MoveStudents(studentToHall, studentToIsland);
        assertAll(
                () -> assertThrowsExactly(IllegalStateException.class, () -> moveStudents.doAction(gameEngine, "pippo"))
        );
    }
}