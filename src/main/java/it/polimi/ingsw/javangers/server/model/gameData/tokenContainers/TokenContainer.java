package it.polimi.ingsw.javangers.server.model.gameData.tokenContainers;

import it.polimi.ingsw.javangers.server.model.gameData.enums.TokenColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representing a container of tokens.
 *
 * @param <C> type of condition for grabbing tokens
 */
public abstract class TokenContainer<C> {
    /**
     * List of all the tokens in the container.
     */
    protected List<TokenColor> tokensList;

    /**
     * Base constructor for inherited token containers, creates tokens list as empty ArrayList.
     */
    protected TokenContainer() {
        this.tokensList = new ArrayList<>();
    }

    /**
     * Get a copy of the tokens list.
     *
     * @return list of tokens
     */
    public List<TokenColor> getTokens() {
        return new ArrayList<>(this.tokensList);
    }

    /**
     * Get map of token colors and number of occurrences of each token color.
     *
     * @return map with token colors as keys and number of occurrences as values
     */
    public Map<TokenColor, Integer> getColorCounts() {
        return this.tokensList.stream().collect(Collectors.toMap(color -> color, color -> 1, Integer::sum));
    }

    /**
     * Add tokens to tokens list inside container.
     *
     * @param tokens list of tokens to add
     */
    public void addTokens(List<TokenColor> tokens) {
        this.tokensList.addAll(tokens);
    }

    /**
     * Grab tokens from container with class specific condition and logic.
     *
     * @param condition condition to grab tokens
     * @return list of grabbed tokens
     */
    public abstract List<TokenColor> grabTokens(C condition);
}
