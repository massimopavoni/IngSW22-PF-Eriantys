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

class QueenTest {
    GameEngine gameEngine;
    CharacterCard queen;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Strange", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Panther", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        queen = gameEngine.getCharacterCards().get("queen");
    }

    @Test
    @DisplayName("Test useEffect for throwing exception for empty token container")
    void useEffect_exceptionTokenContainer() {
        queen.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON, TokenColor.GREEN_FROG));
        queen.getTokenContainer().extractTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON, TokenColor.GREEN_FROG));
        Queen queenEffect = new Queen(Collections.singletonList(TokenColor.RED_DRAGON));
        queen.setEffect(queenEffect);
        assertThrowsExactly(IllegalStateException.class,
                () -> queen.activateEffect(gameEngine, "Panther"));
    }

    @Test
    @DisplayName("Test useEffect for throwing exception for empty token container")
    void useEffect_exceptionWrongListSize() {
        queen.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON, TokenColor.GREEN_FROG));
        Queen queenEffect = new Queen(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.GREEN_FROG));
        queen.setEffect(queenEffect);
        assertThrowsExactly(IllegalStateException.class,
                () -> queen.activateEffect(gameEngine, "Panther"));
    }

    @Test
    @DisplayName("Test useEffect of Queen class for correct behavior")
    void useEffect_correct() {
        PlayerDashboard playerDashboard = gameEngine.getGameState().getPlayerDashboards().get("Panther");
        StudentsBag bag = gameEngine.getGameState().getStudentsBag();
        bag.grabTokens(bag.getTokenContainer().getTokens().size());
        bag.getTokenContainer().addTokens(Collections.singletonList(TokenColor.BLUE_UNICORN));
        queen.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON, TokenColor.GREEN_FROG));
        Queen queenEffect = new Queen(Collections.singletonList(TokenColor.RED_DRAGON));
        queen.setEffect(queenEffect);
        queen.activateEffect(gameEngine, "Panther");
        assertAll(
                () -> assertEquals(Collections.singletonList(TokenColor.RED_DRAGON), playerDashboard.getHall().getTokens()),
                () -> assertEquals(queen.getTokenContainerSize(), queen.getTokenContainer().getTokens().size()),
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 1);
                    put(TokenColor.BLUE_UNICORN, 1);
                    put(TokenColor.YELLOW_ELF, 1);
                    put(TokenColor.GREEN_FROG, 1);
                }}, queen.getTokenContainer().getColorCounts())
        );
    }
}