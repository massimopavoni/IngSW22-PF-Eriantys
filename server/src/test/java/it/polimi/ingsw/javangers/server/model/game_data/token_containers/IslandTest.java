package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    Island island;

    @BeforeEach
    void setUp() {
        island = new Island(0);
    }

    @Test
    @DisplayName("Test constructor")
    void Island_constructor() {
        assertAll(
                () -> assertEquals(0, island.getId()),
                () -> assertNotNull(island.getTokenContainer()),
                () -> assertTrue(island.getTokenContainer().getTokens().isEmpty()),
                () -> assertNotNull(island.getTowers()),
                () -> assertEquals(new Pair<>(TowerColor.NONE, 0), island.getTowers()),
                () -> assertEquals(0, island.getEnabled()),
                () -> assertFalse(island.hasMotherNature())
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
    @DisplayName("Test setEnabled for enabled counter")
    void setEnabled_changeCounter() {
        island.setEnabled(1);
        assertEquals(1, island.getEnabled());
    }

    @Test
    @DisplayName("Test setMotherNature for flag")
    void setMotherNature_changeFlag() {
        island.setMotherNature(true);
        assertTrue(island.hasMotherNature());
    }

    @Test
    @DisplayName("Test mergeWith for correct information merging")
    void mergeWith_correctInfo() {
        island.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        island.setTowers(new Pair<>(TowerColor.WHITE, 1));
        island.setEnabled(1);
        island.setMotherNature(true);
        Island islandToMerge = new Island(1);
        islandToMerge.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.PINK_FAIRY));
        islandToMerge.setTowers(new Pair<>(TowerColor.WHITE, 2));
        island.mergeWith(islandToMerge);
        assertAll(
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 2);
                    put(TokenColor.BLUE_UNICORN, 1);
                    put(TokenColor.PINK_FAIRY, 1);
                }}, island.getTokenContainer().getColorCounts()),
                () -> assertEquals(new Pair<>(TowerColor.WHITE, 3), island.getTowers()),
                () -> assertEquals(1, island.getEnabled()),
                () -> assertTrue(island.hasMotherNature())
        );
    }
}