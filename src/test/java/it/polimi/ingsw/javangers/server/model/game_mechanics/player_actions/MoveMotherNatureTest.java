package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.AssistantCard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MoveMotherNatureTest {
    GameEngine gameEngine;
    MoveMotherNature moveMotherNature;

    @Test
    @DisplayName("Test movement of Mother Nature")
    void doAction_correctMovement() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getArchipelago().setMotherNaturePosition(5);
        AssistantCard eagle = gameEngine.getGameState().getPlayerDashboards().get("pippo").getAssistantCards().get("eagle");
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getDiscardedAssistantCards().put("eagle", eagle);
        gameEngine.getGameState().getArchipelago().getIslands().get(7).getTokenContainer().addTokens(new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN)));
        gameEngine.getGameState().getTeachers().get(TokenColor.BLUE_UNICORN).setOwner("pippo", 2);
        moveMotherNature = new MoveMotherNature(2);
        moveMotherNature.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(7, gameEngine.getGameState().getArchipelago().getMotherNaturePosition()),
                () -> assertEquals(
                        TowerColor.WHITE,
                        gameEngine.getGameState().getArchipelago().getIslands().get(7).getTowers().getValue0())
        );
    }

    @Test
    @DisplayName("Test illegal number of steps for movement (0)")
    void doAction_illegalStepsLess() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getArchipelago().setMotherNaturePosition(5);
        AssistantCard eagle = gameEngine.getGameState().getPlayerDashboards().get("pippo").getAssistantCards().get("eagle");
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getDiscardedAssistantCards().put("eagle", eagle);
        moveMotherNature = new MoveMotherNature(0);
        assertThrowsExactly(IllegalStateException.class, () -> moveMotherNature.doAction(gameEngine, "pippo"));
    }

    @Test
    @DisplayName("Test illegal number of steps for movement (more than allowed)")
    void doAction_illegalStepsMore() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        gameEngine.getGameState().getArchipelago().setMotherNaturePosition(5);
        AssistantCard eagle = gameEngine.getGameState().getPlayerDashboards().get("pippo").getAssistantCards().get("eagle");
        gameEngine.getGameState().getPlayerDashboards().get("pippo").getDiscardedAssistantCards().put("eagle", eagle);
        moveMotherNature = new MoveMotherNature(5);
        assertThrowsExactly(IllegalStateException.class, () -> moveMotherNature.doAction(gameEngine, "pippo"));
    }
}