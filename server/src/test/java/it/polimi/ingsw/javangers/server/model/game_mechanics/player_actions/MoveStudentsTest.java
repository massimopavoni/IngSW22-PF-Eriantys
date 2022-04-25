package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MoveStudentsTest {
    MoveStudents moveStudents;
    GameEngine gameEngine;

    @Test
    @DisplayName("Test illegal total students for less")
    void doAction_illegalNumberOfStudentsLess() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN,
                        TokenColor.BLUE_UNICORN, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG));
        Map<Integer, List<TokenColor>> studentsToIsland = new HashMap<Integer, List<TokenColor>>() {{
            put(3, Collections.singletonList(TokenColor.BLUE_UNICORN));
        }};
        moveStudents = new MoveStudents(Collections.singletonList(TokenColor.BLUE_UNICORN), studentsToIsland);
        assertThrowsExactly(IllegalStateException.class, () -> moveStudents.doAction(gameEngine, "pippo"));
    }

    @Test
    @DisplayName("Test illegal total students for more")
    void doAction_illegalNumberOfStudentsMore() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN,
                        TokenColor.BLUE_UNICORN, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG));
        Map<Integer, List<TokenColor>> studentsToIsland = new HashMap<Integer, List<TokenColor>>() {{
            put(3, Collections.singletonList(TokenColor.BLUE_UNICORN));
            put(4, Collections.singletonList(TokenColor.GREEN_FROG));
        }};
        moveStudents = new MoveStudents(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN), studentsToIsland);
        assertThrowsExactly(IllegalStateException.class, () -> moveStudents.doAction(gameEngine, "pippo"));
    }

    @Test
    @DisplayName("Test correct movements")
    void doAction_correctMovements() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN,
                        TokenColor.BLUE_UNICORN, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG, TokenColor.GREEN_FROG));
        Map<Integer, List<TokenColor>> studentsToIsland = new HashMap<Integer, List<TokenColor>>() {{
            put(3, Collections.singletonList(TokenColor.BLUE_UNICORN));
            put(4, Collections.singletonList(TokenColor.GREEN_FROG));
        }};
        moveStudents = new MoveStudents(Collections.singletonList(TokenColor.BLUE_UNICORN), studentsToIsland);
        moveStudents.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(1, gameEngine.getGameState().getArchipelago().getIslands().get(3).getTokenContainer().getTokens().size()),
                () -> assertEquals(1, gameEngine.getGameState().getArchipelago().getIslands().get(4).getTokenContainer().getTokens().size()),
                () -> assertEquals(4, gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().getTokens().size()),
                () -> assertEquals("pippo", gameEngine.getGameState().getTeachers().get(TokenColor.BLUE_UNICORN).getOwnerUsername())
        );
    }

    @Test
    @DisplayName("Test correct coins")
    void doAction_correctCoins() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, true);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.RED_DRAGON));
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().addTokens(
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        List<TokenColor> studentToHall = Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.RED_DRAGON);
        moveStudents = new MoveStudents(studentToHall, Collections.emptyMap());
        moveStudents.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(0, gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().getTokens().size()),
                () -> assertEquals(3, gameEngine.getGameState().getPlayerDashboards().get("pippo").getCoinsNumber()),
                () -> assertEquals(3, gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().getColorCounts().get(TokenColor.BLUE_UNICORN)),
                () -> assertEquals(3, gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().getColorCounts().get(TokenColor.RED_DRAGON))
        );
    }

    @Test
    @DisplayName("Test Illegal State Exception")
    void doAction_exceptionMovement() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().addTokens(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.GREEN_FROG));
        List<TokenColor> studentToHall = Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN);
        Map<Integer, List<TokenColor>> studentToIsland = new HashMap<Integer, List<TokenColor>>() {{
            put(4, Collections.singletonList(TokenColor.GREEN_FROG));
        }};
        moveStudents = new MoveStudents(studentToHall, studentToIsland);
        assertAll(
                () -> assertThrowsExactly(IllegalStateException.class, () -> moveStudents.doAction(gameEngine, "pippo"))
        );
    }
}