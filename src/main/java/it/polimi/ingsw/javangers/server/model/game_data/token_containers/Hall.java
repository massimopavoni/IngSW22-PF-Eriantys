package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.Collections;
import java.util.List;

/**
 * Class representing the dining hall of a dashboard.
 */
public class Hall extends TokenContainer<List<TokenColor>> {
    /**
     * Method used to grab the tokens that will be moved to another token container.
     *
     * @param tokens list of tokens that have to be grabbed
     * @return list of tokens grabbed if they are present in the list or an empty list if they aren't
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
