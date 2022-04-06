package it.polimi.ingsw.javangers.server.model.game_mechanics;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;

/**
 * Class representing a character card.
 */
public class CharacterCard {
    /**
     * Activation price of the character card.
     */
    private final int price;

    /**
     * Delta of the price of the character card.
     */
    private int priceDelta;

    /**
     * Effect of the card that was activated.
     */
    private EffectStrategy effect;

    /**
     * Constructor for character card, initializing price, price delta and effect strategy.
     *
     * @param price  initial price of the character card
     * @param effect effect of the character card
     */
    public CharacterCard(int price, EffectStrategy effect) {
        this.price = price;
        this.priceDelta = 0;
        this.effect = effect;
    }

    /**
     * Get initial price of the card.
     *
     * @return initial price of the card
     */
    public int getPrice() {
        return price;
    }

    /**
     * Get price delta of the card.
     *
     * @return price delta of the card
     */
    public int getPriceDelta() {
        return priceDelta;
    }

    /**
     * Set price delta of the card.
     *
     * @param priceDelta new price delta of the card
     */
    public void setPriceDelta(int priceDelta) {
        this.priceDelta = priceDelta;
    }

    /**
     * Set effect strategy of the card.
     *
     * @param effect effect strategy of the card
     */
    public void setEffect(EffectStrategy effect) {
        this.effect = effect;
    }

    /**
     * Activate the effect of the character card.
     *
     * @param gameState gameState instance for updates
     * @param username  username of the player activating the character card
     */
    public void activateEffect(GameState gameState, String username) {
        this.effect.useEffect(gameState, username);
    }
}
