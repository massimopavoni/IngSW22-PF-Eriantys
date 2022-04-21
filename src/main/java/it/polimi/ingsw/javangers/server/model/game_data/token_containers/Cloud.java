package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a cloud.
 */
public class Cloud {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Token container instance.
     */
    private final TokenContainer tokenContainer;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

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
        return this.tokenContainer;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Grab all tokens from cloud.
     *
     * @return list of grabbed tokens or an empty list if cloud is empty
     */
    public List<TokenColor> grabTokens() {
        List<TokenColor> tokens = new ArrayList<>(this.tokenContainer.getTokens());
        return this.tokenContainer.extractTokens(tokens);
    }
    //endregion
}
