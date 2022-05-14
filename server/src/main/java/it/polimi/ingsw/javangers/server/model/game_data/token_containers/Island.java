package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import org.javatuples.Pair;

/**
 * Class representing an island.
 */
public class Island {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Island id for merging collisions.
     */
    private final int id;
    /**
     * Token container instance.
     */
    private final TokenContainer tokenContainer;
    /**
     * Pair of tower color and number.
     */
    private Pair<TowerColor, Integer> towers;
    /**
     * Counter for island power computation.
     */
    private int enabled;
    /**
     * Mother nature position flag.
     */
    private boolean motherNature;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for island, initializing id, token container, towers and enabled counter.
     *
     * @param id island id
     */
    public Island(int id) {
        this.id = id;
        this.tokenContainer = new TokenContainer();
        this.towers = new Pair<>(TowerColor.NONE, 0);
        this.enabled = 0;
        this.motherNature = false;
    }

    /**
     * Getter for island id.
     *
     * @return island id
     */
    public int getId() {
        return id;
    }

    /**
     * Get token container instance.
     *
     * @return token container instance
     */
    public TokenContainer getTokenContainer() {
        return this.tokenContainer;
    }

    /**
     * Get a copy of the towers pair.
     *
     * @return pair of tower color and number
     */
    public Pair<TowerColor, Integer> getTowers() {
        return new Pair<>(this.towers.getValue0(), this.towers.getValue1());
    }

    /**
     * Set towers pair color and number.
     *
     * @param towers pair of tower color and number
     */
    public void setTowers(Pair<TowerColor, Integer> towers) {
        this.towers = towers;
    }

    /**
     * Get enabled counter.
     *
     * @return enabled counter
     */
    public int getEnabled() {
        return this.enabled;
    }

    /**
     * Set enabled counter.
     *
     * @param enabled counter
     */
    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    /**
     * Get mother nature position flag.
     *
     * @return mother nature position flag
     */
    public boolean hasMotherNature() {
        return this.motherNature;
    }

    /**
     * Set mother nature position flag.
     *
     * @param motherNature mother nature position flag
     */
    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Merge current island with another island.
     *
     * @param island island to merge with
     */
    public void mergeWith(Island island) {
        this.tokenContainer.addTokens(island.tokenContainer.getTokens());
        this.towers = new Pair<>(this.towers.getValue0(), this.towers.getValue1() + island.towers.getValue1());
        this.enabled = this.enabled + island.enabled;
        this.motherNature = this.motherNature || island.motherNature;
    }
    //endregion
}
