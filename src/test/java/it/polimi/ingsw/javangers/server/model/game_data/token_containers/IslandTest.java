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

    @Test
    @DisplayName("Test mergeWith for correct information merging (false && true condition)")
    void mergeWith_correctInfoFalseTrue() {
        island.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        island.setTowers(new Pair<>(TowerColor.WHITE, 1));
        island.setEnabled(false);
        Island islandToMerge = new Island();
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
                () -> assertFalse(island.isEnabled())
        );
    }

    @Test
    @DisplayName("Test mergeWith for correct information merging (true && false condition)")
    void mergeWith_correctInfoTrueFalse() {
        island.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        island.setTowers(new Pair<>(TowerColor.WHITE, 1));
        Island islandToMerge = new Island();
        islandToMerge.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.PINK_FAIRY));
        islandToMerge.setTowers(new Pair<>(TowerColor.WHITE, 2));
        islandToMerge.setEnabled(false);
        island.mergeWith(islandToMerge);
        assertAll(
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 2);
                    put(TokenColor.BLUE_UNICORN, 1);
                    put(TokenColor.PINK_FAIRY, 1);
                }}, island.getTokenContainer().getColorCounts()),
                () -> assertEquals(new Pair<>(TowerColor.WHITE, 3), island.getTowers()),
                () -> assertFalse(island.isEnabled())
        );
    }
}