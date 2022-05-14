package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Class representing the archipelago.
 */
public class Archipelago {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * List of all islands.
     */
    private final List<Island> islandsList;
    /**
     * Mother nature position in archipelago.
     */
    private int motherNaturePosition;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for Archipelago, initializing islands list and add the number of islands.
     *
     * @param numberOfIslands number of islands in the archipelago
     */
    public Archipelago(int numberOfIslands) {
        if (numberOfIslands < 1) throw new IllegalArgumentException("Invalid number of islands");
        this.islandsList = new ArrayList<>();
        IntStream.range(0, numberOfIslands).forEach(id -> this.islandsList.add(new Island(id)));
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
     * @param position new mother nature position
     */
    public void setMotherNaturePosition(int position) {
        if (position < 0 || position >= this.getIslands().size())
            throw new IllegalArgumentException("Invalid mother nature position");
        this.islandsList.get(this.motherNaturePosition).setMotherNature(false);
        this.motherNaturePosition = position;
        this.islandsList.get(this.motherNaturePosition).setMotherNature(true);
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Merge islands in the archipelago.
     *
     * @param selectedIslandIndex index of the starting island for merge
     * @param leftMerge           true if merge should happen on the left
     * @param rightMerge          true if merge should happen on the right
     */
    public void mergeIslands(int selectedIslandIndex, boolean leftMerge, boolean rightMerge) {
        Island selectedIsland = this.islandsList.get(selectedIslandIndex);
        if (leftMerge) {
            Island leftIsland = this.islandsList.remove((selectedIslandIndex - 1 + this.islandsList.size()) % this.islandsList.size());
            selectedIsland.mergeWith(leftIsland);
        }
        if (rightMerge) {
            Island rightIsland = this.islandsList.remove((this.islandsList.indexOf(selectedIsland) + 1) % this.islandsList.size());
            selectedIsland.mergeWith(rightIsland);
        }
        Optional<Island> motherNatureIsland = this.islandsList.stream().filter(Island::hasMotherNature).findFirst();
        if (motherNatureIsland.isEmpty())
            throw new IllegalStateException("No island has mother nature on it");
        this.motherNaturePosition = this.islandsList.indexOf(motherNatureIsland.get());
    }
    //endregion
}
