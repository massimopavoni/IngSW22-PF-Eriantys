package it.polimi.ingsw.javangers.server.model.game_mechanics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;

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
     * @param cost initial cost of the character card
     */
    @JsonCreator
    public CharacterCard(@JsonProperty("cost") int cost) {
        this.cost = cost;
        this.costDelta = 0;
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
     * @param gameState gameState instance for updates
     * @param username  username of the player activating the character card
     */
    public void activateEffect(GameState gameState, String username) {
        this.effect.useEffect(gameState, username);
    }
    //endregion
}
