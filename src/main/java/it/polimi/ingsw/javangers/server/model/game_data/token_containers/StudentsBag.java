package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Class representing a container of students.
 */
public class StudentsBag {
    /**
     * Token container instance.
     */
    private final TokenContainer tokenContainer;
    /**
     * Random number generator.
     */
    private final Random random;

    /**
     * Constructor for studentsBag initializing token container (according to students per color map) and random instance.
     *
     * @param studentsPerColor map with token colors as keys and number of occurrences as values
     */
    public StudentsBag(Map<TokenColor, Integer> studentsPerColor) {
        this.tokenContainer = new TokenContainer();
        this.random = new Random();
        for (Map.Entry<TokenColor, Integer> entry : studentsPerColor.entrySet())
            this.tokenContainer.addTokens(Collections.nCopies(entry.getValue(), entry.getKey()));
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
     * Grab random tokens from bag.
     *
     * @param amount number of tokens to grab
     * @return list of randomly grabbed tokens or an empty list if parameter is zero
     * @throws IllegalArgumentException if parameter is negative, zero or greater than number of tokens in bag
     */
    public List<TokenColor> grabTokens(int amount) throws IllegalArgumentException {
        List<TokenColor> tokensCopy = this.tokenContainer.getTokens();
        if (amount < 1 || amount > tokensCopy.size()) throw new IllegalArgumentException("Invalid amount of tokens");
        // Shuffle copy
        Collections.shuffle(tokensCopy, this.random);
        // Get amount number of tokens from token container list
        List<TokenColor> tokens = tokensCopy.subList(0, amount);
        return this.tokenContainer.extractTokens(tokens);
    }
}
