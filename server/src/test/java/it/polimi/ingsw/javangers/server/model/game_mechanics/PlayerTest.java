package it.polimi.ingsw.javangers.server.model.game_mechanics;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.FillClouds;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    @DisplayName("Test Player constructor")
    void Player_correctConstructor() {
        assertTrue(player.isEnabledCharacterCard());
    }

    @Test
    @DisplayName("Test setEnabledCharacterCard correct set")
    void setEnabledCharacterCard_correct() {
        player.setEnabledCharacterCard(false);
        assertFalse(player.isEnabledCharacterCard());
    }

    @Test
    @DisplayName("Test executeAction correct action")
    void executeAction() throws GameEngine.GameEngineException {
        GameEngine gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<>() {{
                    put("William Wallace", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Murron MacClannough", new Pair<>(WizardType.DRUID, TowerColor.WHITE));
                }}, false);
        player.setAction(new FillClouds());
        player.executeAction(gameEngine, "William Wallace");
        assertAll(
                () -> assertEquals(3, gameEngine.getGameState().getClouds().get(0).getTokenContainer().getTokens().size()),
                () -> assertEquals(3, gameEngine.getGameState().getClouds().get(1).getTokenContainer().getTokens().size())
        );
    }
}