package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a cloud.
 */
public class Cloud extends TokenContainer<Void> {

    /**
     * Grab all tokens from cloud.
     *
     * @param condition is unnecessary to grab tokens
     * @return list of grabbed tokens or an empty list if cloud is empty
     */
    @Override
    public List<TokenColor> grabTokens(Void condition) {
        List<TokenColor> tokens = new ArrayList<>(this.tokensList);
        this.tokensList.clear();
        return tokens;
    }
}
