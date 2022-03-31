package it.polimi.ingsw.javangers.server.model.gameData.tokenContainers;

import it.polimi.ingsw.javangers.server.model.gameData.enums.TokenColor;

import java.util.Collections;
import java.util.List;

public class Hall extends TokenContainer<List<TokenColor>> {
    /**
     * Method used to grab the tokens that will be moved to another token container
     *
     * @param condition List of tokens that have to be grabbed
     * @return list of tokens grabbed if they are present in the list or an empty list if they aren't
     */
    @Override
    public List<TokenColor> grabTokens(List<TokenColor> condition) {
        if (this.tokensList.containsAll(condition)) {
            this.tokensList.remove(condition);
            return condition;
        }
        return Collections.emptyList();
    }
}
