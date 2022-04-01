package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    Island island;

    @BeforeEach
    void setUp() {
        island = new Island();
    }

    @Test
    @DisplayName("Test constructor")
    void Island_constructor() {
        assertAll(
                () -> assertNotNull(island.getTokenContainer()),
                () -> assertTrue(island.getTokenContainer().getTokens().isEmpty()),
                () -> assertNotNull(island.getTowers()),
                () -> assertEquals(new Pair<>(TowerColor.NONE, 0), island.getTowers()),
                () -> assertTrue(island.isEnabled())
        );
    }

    @Test
    @DisplayName("Test getTowers for towers deep copy")
    void getTowers_deepCopy() {
        Pair<TowerColor, Integer> towers = island.getTowers();
        towers = towers.setAt0(TowerColor.WHITE);
        assertNotEquals(towers, island.getTowers());
    }

    @Test
    @DisplayName("Test setTowers for changing towers pair")
    void setTowers_changePair() {
        island.setTowers(new Pair<>(TowerColor.WHITE, 1));
        assertEquals(new Pair<>(TowerColor.WHITE, 1), island.getTowers());
    }

    @Test
    @DisplayName("Test setEnabled for enabled flag")
    void setEnabled_changeFlag() {
        island.setEnabled(false);
        assertFalse(island.isEnabled());
    }
}