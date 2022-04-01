package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.*;

/**
 * Class representing a container of students.
 */
public class StudentsBag {
    /**
     * Token container instance.
     */
    private final TokenContainer tokenContainer;
    /**
     * Random instance for this game's bag.
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
        for (TokenColor color : TokenColor.values()) {
            this.tokenContainer.addTokens(Collections.nCopies(studentsPerColor.get(color), color));
        }
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
     * Grab random tokens from bag.
     *
     * @param amountOfTokens number of tokens to grab
     * @return list of randomly grabbed tokens or an empty list if parameter is zero
     */
    public List<TokenColor> grabTokens(int amountOfTokens) {
        List<TokenColor> tokens = new ArrayList<>();
        List<TokenColor> tokensCopy = this.tokenContainer.getTokens();
        while (amountOfTokens > 0) {
            tokens.add(tokensCopy.get(random.nextInt(this.tokenContainer.getTokens().size())));
            amountOfTokens--;
        }
        return this.tokenContainer.extractTokens(tokens);
    }
}
