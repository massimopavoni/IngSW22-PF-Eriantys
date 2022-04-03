package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        this.islandsList.addAll(Stream.generate(Island::new).limit(numberOfIslands).collect(Collectors.toList()));
    }

    /**
     * Get a shallow copy of the islands list.
     *
     * @return list of islands
     */
    public List<Island> getIslands() {
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
     * @throws IllegalArgumentException if mother nature position is less than 0 or greater than islands list size
     */
    public void setMotherNaturePosition(int motherNaturePosition) throws IllegalArgumentException {
        if (motherNaturePosition < 0 || motherNaturePosition >= this.getIslands().size())
            throw new IllegalArgumentException("Invalid mother nature position");
        this.motherNaturePosition = motherNaturePosition;
    }

    /**
     * Pop an island from the islands list.
     *
     * @param index index of island to pop
     * @throws IllegalArgumentException if index is less than 0 or greater than islands list size
     */
    public Island popIsland(int index) throws IllegalArgumentException{
        if (index < 0 || index >= this.getIslands().size())
            throw new IllegalArgumentException("Invalid index for pop");
        return this.islandsList.remove(index);
    }

    /**
     * Insert an island in the islands list.
     *
     * @param island island to insert
     * @param index  insertion index
     * @throws IllegalArgumentException if index is less than 0 or greater than islands list size
     */
    public void insertIsland(Island island, int index) throws IllegalArgumentException {
        if (index < 0 || index > this.getIslands().size())
            throw new IllegalArgumentException("Invalid index for insert");
        this.islandsList.add(index, island);
    }
}
