package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing the archipelago.
 */
public class Archipelago {
    /**
     * List of all islands.
     */
    private final List<Island> islandsList;
    /**
     * Mother nature position in archipelago.
     */
    private int motherNaturePosition;

    /**
     * Constructor for Archipelago, initializing islands list and add the number of islands.
     */
    public Archipelago(int numberOfIslands) {
        this.islandsList = new ArrayList<>();
        this.islandsList.addAll(Collections.nCopies(numberOfIslands, new Island()));
    }

    /**
     * Get a shallow copy of the islands list.
     *
     * @return list of islands
     */
    public List<Island> getIslandsList() {
        return new ArrayList<>(this.islandsList);
    }

    /**
     * Get the position of mother nature in the islands list.
     *
     * @return position of mother nature
     */
    public int getMotherNaturePosition() {
        return this.motherNaturePosition;
    }

    /**
     * Set the position of mother nature in the islands list.
     *
     * @param motherNaturePosition new mother nature position
     */
    public void setMotherNaturePosition(int motherNaturePosition) {
        this.motherNaturePosition = motherNaturePosition;
    }

    /**
     * Pop an island from the islands list.
     *
     * @param index index of island to pop
     * @return popped island
     */
    public Island popIsland(int index) {
        return this.islandsList.remove(index);
    }

    /**
     * Insert an island in the islands list.
     *
     * @param island island to insert
     * @param index  insertion index
     */
    public void insertIsland(Island island, int index) {
        this.islandsList.add(index, island);
    }
}
