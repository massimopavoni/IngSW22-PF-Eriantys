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
        for (Map.Entry<TokenColor, Integer> entry : studentsPerColor.entrySet()) {
            if (entry.getValue() < 1) throw new IllegalArgumentException("Invalid number of students per color");
            this.tokenContainer.addTokens(Collections.nCopies(entry.getValue(), entry.getKey()));
        }
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
     * @param number number of tokens to grab
     * @return list of randomly grabbed tokens
     */
    public List<TokenColor> grabTokens(int number) {
        List<TokenColor> tokensCopy = this.tokenContainer.getTokens();
        Collections.shuffle(tokensCopy, this.random);
        List<TokenColor> tokens = tokensCopy.subList(0, Math.min(number, tokensCopy.size()));
        return this.tokenContainer.extractTokens(tokens);
    }
}
