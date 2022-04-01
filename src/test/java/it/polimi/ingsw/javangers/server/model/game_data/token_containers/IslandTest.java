package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {
    Island island;

    @BeforeEach
    void setUp() {
        island = new Island();
    }

    @Test
    @DisplayName("Test constructor")
    void Island() {
        assertAll(
                () -> assertNotNull(island.getTokens()),
                () -> assertTrue(island.getTokens().isEmpty()),
                () -> assertNotNull(island.getTowers()),
                () -> assertEquals(TowerColor.NONE, island.getTowers().getValue0()),
                () -> assertEquals(0, island.getTowers().getValue1()),
                () -> assertTrue(island.isEnabled())
        );
    }

    @Test
    @DisplayName("Test getTowers for towers deep copy")
    void getTowers() {
        Pair<TowerColor, Integer> towers = island.getTowers();
        towers = towers.setAt0(TowerColor.WHITE);
        assertNotEquals(towers, island.getTowers());
    }

    @Test
    @DisplayName("Test setTowers for changing towers pair")
    void setTowers() {
        island.setTowers(new Pair<>(TowerColor.WHITE, 1));
        assertAll(
                () -> assertEquals(TowerColor.WHITE, island.getTowers().getValue0()),
                () -> assertEquals(1, island.getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test isEnabled")
    void isEnabled() {
        assertTrue(island.isEnabled());
    }

    @Test
    @DisplayName("Test setEnabled for enabled flag")
    void setEnabled() {
        island.setEnabled(false);
        assertFalse(island.isEnabled());
    }

    @Test
    @DisplayName("Test grabTokens for selected tokens")
    void grabTokens() {
        island.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON,
                TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        List<TokenColor> grabbed = island.grabTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        List<TokenColor> notGrabbed = island.grabTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.GREEN_FROG));
        assertAll(
                () -> assertEquals(2, grabbed.size()),
                () -> assertEquals(TokenColor.RED_DRAGON, grabbed.get(0)),
                () -> assertEquals(TokenColor.BLUE_UNICORN, grabbed.get(1)),
                () -> assertEquals(2, island.getTokens().size()),
                () -> assertEquals(TokenColor.RED_DRAGON, island.getTokens().get(0)),
                () -> assertEquals(TokenColor.BLUE_UNICORN, island.getTokens().get(1)),
                () -> assertEquals(Collections.emptyList(), notGrabbed),
                () -> assertEquals(2, island.getTokens().size()),
                () -> assertEquals(TokenColor.RED_DRAGON, island.getTokens().get(0)),
                () -> assertEquals(TokenColor.BLUE_UNICORN, island.getTokens().get(1))
        );
    }
}