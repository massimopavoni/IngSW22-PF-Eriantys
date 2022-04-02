package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing an Archipelago.
 */
public class Archipelago {
    /**
     * List of all island in Archipelago.
     */
    private final List<Island> islandList;
    /**
     * mather nature position in Archipelago.
     */
    private int motherNaturePosition;

    /**
     * Constructor for Archipelago, initializing island list and add the amount of island.
     */
    public Archipelago(int amountOfIsland) {
        this.islandList = new ArrayList<>();
        this.islandList.addAll(Collections.nCopies(amountOfIsland, new Island()));
    }

    /**
     * Get a copy of the island list.
     *
     * @return list of island
     */
    public List<Island> getIslandList() {
        return new ArrayList<>(this.islandList);
    }

    /**
     * Get the position of mother nature in the island list.
     *
     * @return position of mother nature
     */
    public int getMotherNaturePosition() {
        return this.motherNaturePosition;
    }

    /**
     * Set the position of mother nature in the island list.
     *
     * @param motherNaturePosition mother nature position to set
     */
    public void setMotherNaturePosition(int motherNaturePosition) {
        this.motherNaturePosition = motherNaturePosition;
    }

    /**
     * Get the island from the island list.
     *
     * @param index the island to select
     * @return selected island
     */
    public Island popIsland(int index) {
        return this.islandList.remove(index);
    }

    /**
     * Set the island in the island list.
     *
     * @param island the island to insert
     * @param index  the position to set
     */
    public void insertIsland(Island island, int index) {
        this.islandList.add(index, island);
    }
}
