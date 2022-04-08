package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChooseCloudTest {
    ChooseCloud chooseCloud;
    GameEngine gameEngine;

    @Test
    @DisplayName("Test doAction for correct students movements")
    void doAction_correctMovements() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configuration.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        TokenContainer entrance = gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance();
        entrance.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        new FillClouds(3).doAction(gameEngine, "pippo");
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