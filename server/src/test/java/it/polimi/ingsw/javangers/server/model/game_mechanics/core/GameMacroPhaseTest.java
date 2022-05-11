package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameMacroPhaseTest {
    GameMacroPhase gameMacroPhase;

    @Test
    @DisplayName("Test GamePhase constructor")
    void GameConfiguration_constructor() {
        gameMacroPhase = new GameMacroPhase(
                new ArrayList<>() {{
                    add(new GamePhase(new ArrayList<>() {{
                        add("FillClouds");
                    }}, false, true));
                }},
                false);
        assertAll(
                () -> assertEquals(Collections.singletonList("FillClouds"), gameMacroPhase.phases().get(0).availableActions()),
                () -> assertFalse(gameMacroPhase.phases().get(0).repeat()),
                () -> assertTrue(gameMacroPhase.phases().get(0).changePlayer()),
                () -> assertFalse(gameMacroPhase.repeat())
        );
    }

    @Test
    @DisplayName("Test getAvailableActions for shallow copy")
    void getAvailableActions_shallowCopy() {
        gameMacroPhase = new GameMacroPhase(
                new ArrayList<>() {{
                    add(new GamePhase(new ArrayList<>() {{
                        add("FillClouds");
                    }}, false, true));
                }},
                false);
        List<GamePhase> phasesCopy = gameMacroPhase.phases();
        phasesCopy.remove(0);
        assertEquals(Collections.singletonList("FillClouds"), gameMacroPhase.phases().get(0).availableActions());
    }
}