package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FillCloudsTest {
    FillClouds fillClouds;
    GameState gameState;

    @Test
    @DisplayName("Test do action with empty StudentsBag")
    void doAction_emptyStudentsBag() throws GameState.GameStateException {
        fillClouds = new FillClouds(0);
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, 8, 12, new HashMap<TokenColor, Integer>(), 1);
        fillClouds.doAction(gameState, "test");
        assertAll(
                () -> assertEquals(0, gameState.getStudentsBag().getTokenContainer().getTokens().size()),
                () -> assertEquals(0, gameState.getCloudsList().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(0, gameState.getCloudsList().get(1).getTokenContainer().getTokens().size())
        );
    }

    @Test
    @DisplayName("Test fill 1 cloud")
    void doAction_oneCloudToFill() throws GameState.GameStateException {
        fillClouds = new FillClouds(3);
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
            put(TokenColor.RED_DRAGON, 2);
            put(TokenColor.BLUE_UNICORN, 2);
            put(TokenColor.GREEN_FROG, 2);
            put(TokenColor.PINK_FAIRY, 2);
            put(TokenColor.YELLOW_ELF, 2);
        }}, 1);
        fillClouds.doAction(gameState, "test");
        assertAll(
                () -> assertEquals(7, gameState.getStudentsBag().getTokenContainer().getTokens().size()),
                () -> assertEquals(3, gameState.getCloudsList().get(0).getTokenContainer().getTokens().size())
        );
    }

    @Test
    @DisplayName("Test fill 2 cloud")
    void doAction_twoCloudsToFill() throws GameState.GameStateException {
        fillClouds = new FillClouds(3);
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
            put(TokenColor.RED_DRAGON, 2);
            put(TokenColor.BLUE_UNICORN, 2);
            put(TokenColor.GREEN_FROG, 2);
            put(TokenColor.PINK_FAIRY, 2);
            put(TokenColor.YELLOW_ELF, 2);
        }}, 1);
        fillClouds.doAction(gameState, "test");
        assertAll(
                () -> assertEquals(4, gameState.getStudentsBag().getTokenContainer().getTokens().size()),
                () -> assertEquals(3, gameState.getCloudsList().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(3, gameState.getCloudsList().get(1).getTokenContainer().getTokens().size())
        );
    }

    @Test
    @DisplayName("Test fill 2 clouds with remaining students")
    void doAction_remainingStudents() throws GameState.GameStateException {
        fillClouds = new FillClouds(3);
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, 8, 12, new HashMap<TokenColor, Integer>() {{
            put(TokenColor.RED_DRAGON, 1);
            put(TokenColor.BLUE_UNICORN, 1);
            put(TokenColor.GREEN_FROG, 1);
            put(TokenColor.PINK_FAIRY, 1);
            put(TokenColor.YELLOW_ELF, 1);
        }}, 1);
        fillClouds.doAction(gameState, "test");
        assertAll(
                () -> assertEquals(0, gameState.getStudentsBag().getTokenContainer().getTokens().size()),
                () -> assertEquals(3, gameState.getCloudsList().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(2, gameState.getCloudsList().get(1).getTokenContainer().getTokens().size())
        );
    }

}