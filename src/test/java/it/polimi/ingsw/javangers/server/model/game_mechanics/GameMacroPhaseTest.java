package it.polimi.ingsw.javangers.server.model.game_mechanics;

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
                new ArrayList<GamePhase>() {{
                    add(new GamePhase(new ArrayList<String>() {{
                        add("FillClouds");
                    }}, false, true));
                }},
                false);
        assertAll(
                () -> assertEquals(Collections.singletonList("FillClouds"), gameMacroPhase.getPhases().get(0).getAvailableActions()),
                () -> assertFalse(gameMacroPhase.getPhases().get(0).isRepeat()),
                () -> assertTrue(gameMacroPhase.getPhases().get(0).isChangePlayer()),
                () -> assertFalse(gameMacroPhase.isRepeat())
        );
    }

    @Test
    @DisplayName("Test getAvailableActions for shallow copy")
    void getAvailableActions_shallowCopy() {
        gameMacroPhase = new GameMacroPhase(
                new ArrayList<GamePhase>() {{
                    add(new GamePhase(new ArrayList<String>() {{
                        add("FillClouds");
                    }}, false, true));
                }},
                false);
        List<GamePhase> phasesCopy = gameMacroPhase.getPhases();
        phasesCopy.remove(0);
        assertEquals(Collections.singletonList("FillClouds"), gameMacroPhase.getPhases().get(0).getAvailableActions());
    }
}