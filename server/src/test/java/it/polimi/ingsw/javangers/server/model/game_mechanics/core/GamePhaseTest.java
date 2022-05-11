package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GamePhaseTest {
    GamePhase gamePhase;

    @Test
    @DisplayName("Test GamePhase constructor")
    void GameConfiguration_constructor() {
        gamePhase = new GamePhase(new ArrayList<>() {{
            add("FillClouds");
        }}, false, true);
        assertAll(
                () -> assertEquals(Collections.singletonList("FillClouds"), gamePhase.availableActions()),
                () -> assertFalse(gamePhase.repeat()),
                () -> assertTrue(gamePhase.changePlayer())
        );
    }

    @Test
    @DisplayName("Test getAvailableActions for shallow copy")
    void getAvailableActions_shallowCopy() {
        gamePhase = new GamePhase(new ArrayList<>() {{
            add("FillClouds");
        }}, false, true);
        List<String> availableActionsCopy = gamePhase.availableActions();
        availableActionsCopy.remove(0);
        assertEquals(Collections.singletonList("FillClouds"), gamePhase.availableActions());
    }
}