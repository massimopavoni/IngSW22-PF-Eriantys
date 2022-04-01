package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import org.javatuples.Pair;

/**
 * Class representing an island.
 */
public class Island {
    /**
     * Token container instance.
     */
    private final TokenContainer tokenContainer;
    /**
     * Pair of tower color and amount.
     */
    private Pair<TowerColor, Integer> towers;
    /**
     * Flag for island power computation.
     */
    private boolean enabled;

    /**
     * Constructor for island, initializing token container, towers and enabled flag.
     */
    public Island() {
        this.tokenContainer = new TokenContainer();
        this.towers = new Pair<>(TowerColor.NONE, 0);
        this.enabled = true;
    }

    /**
     * Get token container instance.
     *
     * @return token container instance
     */
    public TokenContainer getTokenContainer() {
        return tokenContainer;
    }

    /**
     * Get a copy of the towers pair.
     *
     * @return pair of tower color and amount
     */
    public Pair<TowerColor, Integer> getTowers() {
        return new Pair<>(this.towers.getValue0(), this.towers.getValue1());
    }

    /**
     * Set towers pair color and amount.
     *
     * @param towers pair of tower color and amount
     */
    public void setTowers(Pair<TowerColor, Integer> towers) {
        this.towers = towers;
    }

    /**
     * Get enabled flag.
     *
     * @return enabled flag
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set enabled flag.
     *
     * @param enabled flag
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
