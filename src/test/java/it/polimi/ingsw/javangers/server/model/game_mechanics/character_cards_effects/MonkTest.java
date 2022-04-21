package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

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

class MonkTest {
    GameEngine gameEngine;
    CharacterCard monk;
    Monk monkEffect;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Hawkeye", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("BlackWidow", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        monk = gameEngine.getCharacterCards().get("monk");
    }

    @Test
    @DisplayName("Test useEffect for throwing exception for empty token container")
    void useEffect_exceptionTokenContainer() {
        monk.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN, TokenColor.YELLOW_ELF, TokenColor.GREEN_FROG));
        monk.getTokenContainer().extractTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN, TokenColor.YELLOW_ELF, TokenColor.GREEN_FROG));
        monkEffect = new Monk(Collections.singletonList(TokenColor.RED_DRAGON), 0);
        monk.setEffect(monkEffect);
        assertThrowsExactly(IllegalStateException.class, () -> monk.activateEffect(gameEngine, "BlackWidow"));
    }

    @Test
    @DisplayName("Test useEffect for throwing exception for empty token container")
    void useEffect_exceptionWrongListSize() {
        monk.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN, TokenColor.YELLOW_ELF, TokenColor.GREEN_FROG));
        monkEffect = new Monk(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN), 0);
        monk.setEffect(monkEffect);
        assertThrowsExactly(IllegalStateException.class, () -> monk.activateEffect(gameEngine, "BlackWidow"));
    }

    @Test
    @DisplayName("Test useEffect of Monk class for correct behavior")
    void useEffect_correct() {
        StudentsBag bag = gameEngine.getGameState().getStudentsBag();
        bag.grabTokens(bag.getTokenContainer().getTokens().size());
        bag.getTokenContainer().addTokens(Collections.singletonList(TokenColor.BLUE_UNICORN));
        monk.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN, TokenColor.YELLOW_ELF, TokenColor.GREEN_FROG));
        monkEffect = new Monk(Collections.singletonList(TokenColor.RED_DRAGON), 0);
        monk.setEffect(monkEffect);
        monk.activateEffect(gameEngine, "BlackWidow");
        assertAll(
                () -> assertEquals(Collections.singletonList(TokenColor.RED_DRAGON), gameEngine.getGameState().getArchipelago().getIslands().get(0).getTokenContainer().getTokens()),
                () -> assertEquals(monk.getTokenContainerSize(), monk.getTokenContainer().getTokens().size()),
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.BLUE_UNICORN, 2);
                    put(TokenColor.GREEN_FROG, 1);
                    put(TokenColor.YELLOW_ELF, 1);
                }}, monk.getTokenContainer().getColorCounts())
        );
    }
}