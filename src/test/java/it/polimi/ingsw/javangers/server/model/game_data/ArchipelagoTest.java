package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchipelagoTest {
    Archipelago archipelago;

    @BeforeEach
    void setUp() {
        archipelago = new Archipelago(3);
    }

    @Test
    @DisplayName("Test constructor for invalid number of islands")
    void Archipelago_invalidNumber() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new Archipelago(0));
    }

    @Test
    @DisplayName("Test constructor")
    void Archipelago_constructor() {
        assertEquals(3, archipelago.getIslands().size());
    }

    @Test
    @DisplayName("Test getIslandsList for getting a shallow copy")
    void getIslands_shallow() {
        List<Island> shallowCopy = archipelago.getIslands();
        shallowCopy.remove(0);
        archipelago.getIslands().get(0).setEnabled(false);
        assertAll(
                () -> assertNotEquals(shallowCopy, archipelago.getIslands()),
                () -> assertFalse(archipelago.getIslands().get(0).isEnabled())
        );
    }

    @Test
    @DisplayName("Test setMotherNaturePosition for invalid position exception")
    void setMotherNaturePosition_invalidPosition() {
        assertAll(
                () -> assertThrowsExactly(IllegalArgumentException.class, () -> archipelago.setMotherNaturePosition(-1)),
                () -> assertThrowsExactly(IllegalArgumentException.class, () -> archipelago.setMotherNaturePosition(3))
        );
    }

    @Test
    @DisplayName("Test setMotherNaturePosition for setting correct index")
    void setMotherNaturePosition_correctSet() {
        archipelago.setMotherNaturePosition(1);
        assertEquals(1, archipelago.getMotherNaturePosition());
    }

    @Test
    @DisplayName("Test mergeIslands for only left merge")
    void mergeIslands_leftMerge() {
        archipelago.getIslands().get(2).setEnabled(false);
        Island otherIsland = archipelago.getIslands().get(1);
        archipelago.mergeIslands(0, true, false);
        assertAll(
                () -> assertEquals(2, archipelago.getIslands().size()),
                () -> assertFalse(archipelago.getIslands().get(0).isEnabled()),
                () -> assertEquals(otherIsland, archipelago.getIslands().get(1))
        );
    }

    @Test
    @DisplayName("Test mergeIslands for only right merge")
    void mergeIslands_rightMerge() {
        archipelago.getIslands().get(2).setTowers(new Pair<>(TowerColor.WHITE, 1));
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.WHITE, 1));
        Island otherIsland = archipelago.getIslands().get(1);
        archipelago.mergeIslands(2, false, true);
        assertAll(
                () -> assertEquals(2, archipelago.getIslands().size()),
                () -> assertEquals(2, archipelago.getIslands().get(1).getTowers().getValue1()),
                () -> assertEquals(otherIsland, archipelago.getIslands().get(0))
        );
    }

    @Test
    @DisplayName("Test mergeIslands for both left and right merge")
    void mergeIslands_leftAndRightMerge() {
        archipelago.getIslands().get(2).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.YELLOW_ELF));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        archipelago.getIslands().get(1).getTokenContainer().addTokens(Arrays.asList(TokenColor.GREEN_FROG, TokenColor.GREEN_FROG));
        archipelago.mergeIslands(0, true, true);
        assertAll(
                () -> assertEquals(1, archipelago.getIslands().size()),
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                    put(TokenColor.RED_DRAGON, 1);
                    put(TokenColor.BLUE_UNICORN, 2);
                    put(TokenColor.YELLOW_ELF, 1);
                    put(TokenColor.GREEN_FROG, 2);
                }}, archipelago.getIslands().get(0).getTokenContainer().getColorCounts())
        );
    }
}