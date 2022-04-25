package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
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

class BardTest {
    GameEngine gameEngine;
    PlayerDashboard playerDashboard;
    CharacterCard bard;
    Bard bardEffect;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Thor", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Cap", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        playerDashboard = gameEngine.getGameState().getPlayerDashboards().get("Thor");
        bard = gameEngine.getCharacterCards().get("bard");
    }

    @Test
    @DisplayName("Test useEffect throwing exception for different sizes of the lists")
    void useEffect_exceptionDifferentSize() {
        bardEffect = new Bard(Collections.singletonList(TokenColor.RED_DRAGON), Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        bard.setEffect(bardEffect);
        assertThrowsExactly(IllegalStateException.class, () -> bard.activateEffect(gameEngine, "Thor"));
    }

    @Test
    @DisplayName("Test useEffect throwing exception for size bigger than maximum")
    void useEffect_exceptionBiggerThanMaximum() {
        bardEffect = new Bard(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.RED_DRAGON,
                TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.RED_DRAGON),
                Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN,
                        TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        bard.setEffect(bardEffect);
        assertThrowsExactly(IllegalStateException.class, () -> bard.activateEffect(gameEngine, "Thor"));
    }

    @Test
    @DisplayName("Test useEffect of Bard class for correct behavior")
    void useEffect_correct() {
        playerDashboard.getEntrance().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN,
                TokenColor.PINK_FAIRY, TokenColor.GREEN_FROG, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON,
                TokenColor.RED_DRAGON));
        playerDashboard.getHall().addTokens(Arrays.asList(TokenColor.PINK_FAIRY, TokenColor.GREEN_FROG));
        bardEffect = new Bard(Arrays.asList(TokenColor.PINK_FAIRY, TokenColor.GREEN_FROG),
                Arrays.asList(TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON));
        bard.setEffect(bardEffect);
        bard.activateEffect(gameEngine, "Thor");
        assertAll(
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 2);
                    put(TokenColor.BLUE_UNICORN, 1);
                    put(TokenColor.GREEN_FROG, 2);
                    put(TokenColor.PINK_FAIRY, 2);
                }}, playerDashboard.getEntrance().getColorCounts()),
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 1);
                    put(TokenColor.YELLOW_ELF, 1);
                }}, playerDashboard.getHall().getColorCounts())
        );

    }
}