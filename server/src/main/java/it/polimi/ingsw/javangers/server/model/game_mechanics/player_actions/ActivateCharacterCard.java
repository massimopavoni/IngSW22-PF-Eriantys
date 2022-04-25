package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;

/**
 * Class representing the play assistant card action.
 */
public class ActivateCharacterCard implements ActionStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Name of character card to play.
     */
    private final String cardName;
    /**
     * Effect of the card.
     */
    private final EffectStrategy effect;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for play character card action, initializing card name and effect.
     *
     * @param cardName name of character card to play
     * @param effect   effect of the card
     */
    @JsonCreator
    public ActivateCharacterCard(@JsonProperty("cardName") String cardName, @JsonProperty("effect") EffectStrategy effect) {
        this.cardName = cardName;
        this.effect = effect;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Action implementation for playing character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void doAction(GameEngine gameEngine, String username) {
        if (!this.effect.getClass().getSimpleName().equalsIgnoreCase(this.cardName))
            throw new IllegalStateException("Provided effect cannot be used with the specified card");
        CharacterCard card = gameEngine.getCharacterCards().get(this.cardName);
        PlayerDashboard playerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        if (card.getCost() + card.getCostDelta() > playerDashboard.getCoinsNumber())
            throw new IllegalStateException("Specified player does not have enough coins to activate specified card");
        card.setEffect(this.effect);
        card.activateEffect(gameEngine, username);
        playerDashboard.setCoinsNumber(playerDashboard.getCoinsNumber() - (card.getCost() + card.getCostDelta()));
        card.setCostDelta(card.getCostDelta() + 1);
    }
    //endregion
}
