package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class representing a container of tokens.
 */
public class TokenContainer {
    /**
     * List of all the tokens in the container.
     */
    private final List<TokenColor> tokensList;

    /**
     * Constructor for token container, creates tokens list as empty ArrayList.
     */
    public TokenContainer() {
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
     * Extract tokens from container with class specific function logic.
     *
     * @param tokens tokens to be extracted
     * @return list of extracted tokens
     */
    public List<TokenColor> extractTokens(List<TokenColor> tokens) {
        List<TokenColor> tokensCopy = getTokens();
        for (TokenColor token : tokens) {
            if (!tokensCopy.contains(token)) return Collections.emptyList();
            tokensCopy.remove(token);
        }
        for (TokenColor token : tokens)
            this.tokensList.remove(token);
        return tokens;
    }
}
