package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import org.javatuples.Pair;

import java.util.Collections;
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
        this.towers = new Pair<>(TowerColor.NONE, 0);
        this.enabled = true;
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

    /**
     * Grab specified tokens list from island.
     *
     * @param tokens list of tokens to grab
     * @return list of grabbed tokens or an empty list if island does not contain requested tokens
     */
    @Override
    public List<TokenColor> grabTokens(List<TokenColor> tokens) {
        if (this.tokensList.containsAll(tokens)) {
            for (TokenColor token : tokens) {
                this.tokensList.remove(token);
            }
            return tokens;
        }
        return Collections.emptyList();
    }
}
