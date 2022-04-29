package it.polimi.ingsw.javangers.server.model.game_mechanics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

/**
 * Class representing a character card.
 */
public class CharacterCard {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Activation cost of the character card.
     */
    private final int cost;
    /**
     * Optional token container size.
     */
    private final int tokenContainerSize;
    /**
     * Optional token container.
     */
    private final TokenContainer tokenContainer;
    /**
     * Optional counter used for different effects.
     */
    private int multipurposeCounter;
    /**
     * Cost delta of the character card.
     */
    private int costDelta;
    /**
     * Effect strategy to activate.
     */
    private EffectStrategy effect;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for character card, initializing cost, cost delta and effect strategy.
     *
     * @param cost                initial cost of the character card
     * @param tokenContainerSize  optional token container size
     * @param multipurposeCounter optional counter used for different effects
     */
    @JsonCreator
    public CharacterCard(@JsonProperty("cost") int cost,
                         @JsonProperty("tokenContainerSize") int tokenContainerSize,
                         @JsonProperty("multipurposeCounter") int multipurposeCounter) {
        this.cost = cost;
        this.costDelta = 0;
        this.tokenContainerSize = tokenContainerSize;
        this.tokenContainer = new TokenContainer();
        this.multipurposeCounter = multipurposeCounter;
    }

    /**
     * Get initial cost of the card.
     *
     * @return initial cost of the card
     */
    public int getCost() {
        return cost;
    }

    /**
     * Get cost delta of the card.
     *
     * @return cost delta of the card
     */
    public int getCostDelta() {
        return costDelta;
    }

    /**
     * Set cost delta of the card.
     *
     * @param costDelta new cost delta of the card
     */
    public void setCostDelta(int costDelta) {
        this.costDelta = costDelta;
    }

    /**
     * Get optional token container size.
     *
     * @return optional token container size
     */
    public int getTokenContainerSize() {
        return this.tokenContainerSize;
    }

    /**
     * Get optional token container.
     *
     * @return optional token container
     */
    public TokenContainer getTokenContainer() {
        return this.tokenContainer;
    }

    /**
     * Get optional counter used for different effects.
     *
     * @return optional counter
     */
    public int getMultipurposeCounter() {
        return this.multipurposeCounter;
    }

    /**
     * Set optional counter used for different effects.
     *
     * @param counter new counter value
     */
    public void setMultipurposeCounter(int counter) {
        this.multipurposeCounter = counter;
    }

    /**
     * Set effect strategy of the card.
     *
     * @param effect effect strategy of the card
     */
    public void setEffect(EffectStrategy effect) {
        this.effect = effect;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Activate the effect of the character card.
     *
     * @param gameEngine gameEngine instance for updates
     * @param username   username of the player activating the character card
     */
    public void activateEffect(GameEngine gameEngine, String username) {
        this.effect.useEffect(gameEngine, username);
    }
    //endregion
}
