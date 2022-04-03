package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class GameStateTest {
    GameState gameState;

    @Test
    @DisplayName("Test GameState if constructor is correct")
    void GameState_correctConstructor() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
                            put(TokenColor.RED_DRAGON, 26);
                            put(TokenColor.BLUE_UNICORN, 26);
                            put(TokenColor.GREEN_FROG, 26);
                            put(TokenColor.PINK_FAIRY, 26);
                            put(TokenColor.YELLOW_ELF, 26);
                        }}, 1)),
                () -> assertNotNull(gameState.getArchipelago()),
                () -> assertNotNull(gameState.getStudentsBag()),
                () -> assertEquals(new HashSet<String>() {{
                    add("pippo");
                    add("pluto");
                }}, gameState.getPlayerDashboards().keySet()),
                () -> assertEquals(new HashSet<TokenColor>() {{
                    add(TokenColor.GREEN_FROG);
                    add(TokenColor.RED_DRAGON);
                    add(TokenColor.PINK_FAIRY);
                    add(TokenColor.YELLOW_ELF);
                    add(TokenColor.BLUE_UNICORN);
                }}, gameState.getTeachers().keySet()),
                () -> assertEquals(2, gameState.getCloudsList().size())
        );
    }

    @Test
    @DisplayName("Test constructor throws GameStateException")
    void GameState_gameStateException() {
        assertThrowsExactly(GameState.GameStateException.class,
                () -> gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/AssistantCard.class",
                        new HashMap<String, Pair<WizardType, TowerColor>>() {{
                            put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                            put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                        }}, 8, 12, new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 26);
                    put(TokenColor.BLUE_UNICORN, 26);
                    put(TokenColor.GREEN_FROG, 26);
                    put(TokenColor.PINK_FAIRY, 26);
                    put(TokenColor.YELLOW_ELF, 26);
                }}, 1), "Error while creating player dashboards");
    }

    @Test
    @DisplayName("Test getPlayerDashboards for getting a shallow copy")
    void getPlayerDashboards_shallow() throws GameState.GameStateException {
        GameState gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
            put(TokenColor.RED_DRAGON, 26);
            put(TokenColor.BLUE_UNICORN, 26);
            put(TokenColor.GREEN_FROG, 26);
            put(TokenColor.PINK_FAIRY, 26);
            put(TokenColor.YELLOW_ELF, 26);
        }}, 1);
        Map<String, PlayerDashboard> shallowCopy = gameState.getPlayerDashboards();
        shallowCopy.remove("pippo");
        gameState.getPlayerDashboards().get("pippo").setCoinsNumber(2);
        assertAll(
                () -> assertNotEquals(shallowCopy, gameState.getPlayerDashboards()),
                () -> assertEquals(2, gameState.getPlayerDashboards().get("pippo").getCoinsNumber())
        );
    }

    @Test
    @DisplayName("Test getTeachers for getting a shallow copy")
    void getTeachers_shallow() throws GameState.GameStateException {
        GameState gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
            put(TokenColor.RED_DRAGON, 26);
            put(TokenColor.BLUE_UNICORN, 26);
            put(TokenColor.GREEN_FROG, 26);
            put(TokenColor.PINK_FAIRY, 26);
            put(TokenColor.YELLOW_ELF, 26);
        }}, 1);
        Map<TokenColor, Teacher> shallowCopy = gameState.getTeachers();
        shallowCopy.remove(TokenColor.RED_DRAGON);
        gameState.getTeachers().get(TokenColor.RED_DRAGON).setOwner("Topolino", 1);
        assertAll(
                () -> assertNotEquals(shallowCopy, gameState.getTeachers()),
                () -> assertEquals("Topolino", gameState.getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(1, gameState.getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test getCloudsList for getting shallow copy")
    void getCloudsList_shallow() throws GameState.GameStateException {
        GameState gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
            put(TokenColor.RED_DRAGON, 26);
            put(TokenColor.BLUE_UNICORN, 26);
            put(TokenColor.GREEN_FROG, 26);
            put(TokenColor.PINK_FAIRY, 26);
            put(TokenColor.YELLOW_ELF, 26);
        }}, 1);
        List<Cloud> shallowCopy = gameState.getCloudsList();
        shallowCopy.remove(0);
        gameState.getCloudsList().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        assertAll(
                () -> assertNotEquals(shallowCopy, gameState.getCloudsList()),
                () -> assertEquals(TokenColor.RED_DRAGON, gameState.getCloudsList().get(0).getTokenContainer().getTokens().get(0)),
                () -> assertEquals(TokenColor.YELLOW_ELF, gameState.getCloudsList().get(0).getTokenContainer().getTokens().get(1))
        );
    }
}