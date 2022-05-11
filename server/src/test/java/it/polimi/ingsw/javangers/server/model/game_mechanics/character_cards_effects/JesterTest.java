package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JesterTest {
    GameEngine gameEngine;
    StudentsBag bag;
    PlayerDashboard playerDashboard;
    CharacterCard jester;
    Jester jesterEffect;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<>() {{
                    put("Hulk", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("IronMan", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        bag = gameEngine.getGameState().getStudentsBag();
        playerDashboard = gameEngine.getGameState().getPlayerDashboards().get("Hulk");
        jester = gameEngine.getCharacterCards().get("jester");
    }

    @Test
    @DisplayName("Test useEffect throwing exception for different sizes of the lists")
    void useEffect_exceptionDifferentSize() {
        jesterEffect = new Jester(Collections.singletonList(TokenColor.RED_DRAGON), Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        jester.setEffect(jesterEffect);
        assertThrowsExactly(IllegalStateException.class, () -> jester.activateEffect(gameEngine, "Hulk"));
    }

    @Test
    @DisplayName("Test useEffect throwing exception for size bigger than maximum")
    void useEffect_exceptionBiggerThanMaximum() {
        jesterEffect = new Jester(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.RED_DRAGON),
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        jester.setEffect(jesterEffect);
        assertThrowsExactly(IllegalStateException.class, () -> jester.activateEffect(gameEngine, "Hulk"));
    }

    @Test
    @DisplayName("Test useEffect of Jester class for correct behavior")
    void useEffect_correct() {
        playerDashboard.getEntrance().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN,
                TokenColor.PINK_FAIRY, TokenColor.GREEN_FROG, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON,
                TokenColor.RED_DRAGON));
        jester.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN,
                TokenColor.PINK_FAIRY, TokenColor.GREEN_FROG, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON));
        jesterEffect = new Jester(Arrays.asList(TokenColor.PINK_FAIRY, TokenColor.GREEN_FROG, TokenColor.YELLOW_ELF),
                Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        jester.setEffect(jesterEffect);
        jester.activateEffect(gameEngine, "Hulk");
        assertAll(
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 5);
                    put(TokenColor.BLUE_UNICORN, 2);
                }}, playerDashboard.getEntrance().getColorCounts()),
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.PINK_FAIRY, 2);
                    put(TokenColor.YELLOW_ELF, 2);
                    put(TokenColor.GREEN_FROG, 2);
                }}, jester.getTokenContainer().getColorCounts())
        );

    }
}