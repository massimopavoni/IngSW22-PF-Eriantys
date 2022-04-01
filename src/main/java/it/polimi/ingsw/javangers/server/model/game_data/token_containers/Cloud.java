package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a cloud.
 */
public class Cloud {
    /**
     * Token container instance.
     */
    private final TokenContainer tokenContainer;

    /**
     * Constructor for cloud, initializing token container.
     */
    public Cloud() {
        this.tokenContainer = new TokenContainer();
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
     * Grab all tokens from cloud.
     *
     * @return list of grabbed tokens or an empty list if cloud is empty
     */
    public List<TokenColor> grabTokens() {
        List<TokenColor> tokens = new ArrayList<>(this.tokenContainer.getTokens());
        this.tokenContainer.extractTokens(tokens);
        return tokens;
    }
}
