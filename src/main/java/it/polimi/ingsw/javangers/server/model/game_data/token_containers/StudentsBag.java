package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.*;

/**
 * Class representing a container of students.
 */
public class StudentsBag extends TokenContainer<Integer> {

    private final Random random;

    /**
     * Constructor for studentsBag initializing tokensList according to
     * the input parameter Map studentsPerColor.
     *
     * @param studentsPerColor map with token colors as keys and number of occurrences as values
     */
    public StudentsBag(Map<TokenColor, Integer> studentsPerColor) {
        super();
        this.random = new Random();
        for (TokenColor color :
                TokenColor.values()) {
            this.tokensList = Collections.nCopies(studentsPerColor.get(color), color);
        }
    }

    /**
     * Grab casual tokens from studentsBag.
     *
     * @param amountOfTokens that need to be grab casually
     * @return list of casual grabbed tokens or an empty list if amountOfTokens is zero.
     */
    @Override
    public List<TokenColor> grabTokens(Integer amountOfTokens) {
        List<TokenColor> tokens = new ArrayList<>();
        for (int i = 0; i < amountOfTokens; i++) {
            tokens.add(this.tokensList.remove(random.nextInt(this.tokensList.size())));
        }
        return tokens;
    }
}
