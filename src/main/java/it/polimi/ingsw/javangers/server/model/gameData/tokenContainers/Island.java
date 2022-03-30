package it.polimi.ingsw.javangers.server.model.gameData.tokenContainers;

import it.polimi.ingsw.javangers.server.model.gameData.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.gameData.enums.TowerColor;
import org.javatuples.Pair;

import java.util.List;

/**
 * Class representing an island.
 */
public class Island extends TokenContainer<List<TokenColor>> {
    /**
     * Pair of tower color and amount.
     */
    private Pair<TowerColor, Integer> towers;
    /**
     * Flag for island power computation.
     */
    private boolean enabled;

    /**
     * Constructor for island, initializing towers and enabled flag.
     */
    public Island() {
        super();
        this.towers = new Pair<>(TowerColor.None, 0);
        this.enabled = true;
    }

    /**
     * Get a copy of the towers pair.
     *
     * @return pair of tower color and amount
     */
    public Pair<TowerColor, Integer> getTowers() {
        return new Pair<>(towers.getValue0(), towers.getValue1());
    }

    /**
     * Set towers pair color and amount.
     *
     * @param towers paid of tower color and amount
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

    /**
     * Grab specified tokens list from island.
     *
     * @param tokens list of tokens to grab
     * @return list of grabbed tokens or null if island does not contain requested tokens
     */
    @Override
    public List<TokenColor> grabTokens(List<TokenColor> tokens) {
        if (this.tokensList.containsAll(tokens)) {
            this.tokensList.removeAll(tokens);
            return tokens;
        }
        return null;
    }
}
