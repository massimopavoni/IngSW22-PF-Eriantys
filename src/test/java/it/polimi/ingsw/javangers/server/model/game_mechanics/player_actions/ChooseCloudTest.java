package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
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
    GameState gameState;

    @Test
    @DisplayName("Test doAction for correct students movements")
    void doAction_correctMovements() throws GameState.GameStateException {
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
        TokenContainer entrance = gameState.getPlayerDashboards().get("pippo").getEntrance();
        entrance.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        new FillClouds(3).doAction(gameState, "pippo");
        List<TokenColor> tokens = new ArrayList<TokenColor>() {{
            addAll(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        }};
        tokens.addAll(gameState.getClouds().get(0).getTokenContainer().getTokens());
        chooseCloud = new ChooseCloud(0);
        chooseCloud.doAction(gameState, "pippo");
        assertAll(
                () -> assertTrue(gameState.getClouds().get(0).getTokenContainer().getTokens().isEmpty()),
                () -> assertEquals(tokens, entrance.getTokens())
        );
    }
}