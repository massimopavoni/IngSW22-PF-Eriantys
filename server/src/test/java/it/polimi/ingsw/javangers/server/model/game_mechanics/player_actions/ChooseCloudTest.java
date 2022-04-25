package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ChooseCloudTest {
    ChooseCloud chooseCloud;
    GameEngine gameEngine;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
    }

    @Test
    @DisplayName("Test doAction for illegal state if empty")
    void doAction_illegalEmpty() {
        gameEngine.getGameState().getClouds().get(1).getTokenContainer().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        chooseCloud = new ChooseCloud(0);
        assertThrows(IllegalStateException.class, () -> chooseCloud.doAction(gameEngine, "pippo"));
    }

    @Test
    @DisplayName("Test doAction for all empty")
    void doAction_empty() {
        chooseCloud = new ChooseCloud(0);
        assertDoesNotThrow(() -> chooseCloud.doAction(gameEngine, "pippo"));
    }

    @Test
    @DisplayName("Test doAction for correct students movements")
    void doAction_correctMovements() throws GameEngine.GameEngineException {
        TokenContainer entrance = gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance();
        entrance.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        new FillClouds().doAction(gameEngine, "pippo");
        List<TokenColor> tokens = new ArrayList<TokenColor>() {{
            addAll(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        }};
        tokens.addAll(gameEngine.getGameState().getClouds().get(0).getTokenContainer().getTokens());
        chooseCloud = new ChooseCloud(0);
        chooseCloud.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertTrue(gameEngine.getGameState().getClouds().get(0).getTokenContainer().getTokens().isEmpty()),
                () -> assertEquals(tokens, entrance.getTokens())
        );
    }
}