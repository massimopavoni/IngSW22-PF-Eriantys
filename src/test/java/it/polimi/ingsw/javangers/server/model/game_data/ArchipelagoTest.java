package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchipelagoTest {
    Archipelago archipelago;

    @BeforeEach
    void setUp() {
        archipelago = new Archipelago(2);
    }

    @Test
    @DisplayName("Test constructor for invalid number of islands")
    void Archipelago_invalidNumber() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new Archipelago(0),
                "Invalid number of islands");
    }

    @Test
    @DisplayName("Test constructor")
    void Archipelago_constructor() {
        assertEquals(2, archipelago.getIslands().size());
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
                () -> assertThrowsExactly(IllegalArgumentException.class, () -> archipelago.setMotherNaturePosition(-1),
                        "Invalid mother nature position"),
                () -> assertThrowsExactly(IllegalArgumentException.class, () -> archipelago.setMotherNaturePosition(3),
                        "Invalid mother nature position")
        );
    }

    @Test
    @DisplayName("Test setMotherNaturePosition for setting correct index")
    void setMotherNaturePosition_correctSet() {
        archipelago.setMotherNaturePosition(1);
        assertEquals(1, archipelago.getMotherNaturePosition());
    }

    @Test
    @DisplayName("Test popIsland for correct pop")
    void popIsland_correctPop() {
        archipelago.getIslands().get(0).setEnabled(false);
        Island poppedIsland = archipelago.getIslands().get(0);
        List<Island> remainingIslands = archipelago.getIslands().subList(1, 2);
        assertAll(
                () -> assertEquals(poppedIsland, archipelago.popIsland(0)),
                () -> assertEquals(remainingIslands, archipelago.getIslands())
        );
    }

    @Test
    @DisplayName("Test insertIsland for correct insert")
    void insertIsland_correct() {
        Island insertedIsland = new Island();
        insertedIsland.setEnabled(false);
        archipelago.insertIsland(insertedIsland, 2);
        assertFalse(archipelago.getIslands().get(2).isEnabled());
    }
}