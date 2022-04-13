package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;

/**
 * Class representing the play assistant card action.
 */
public class ActivateCharacterCard implements ActionStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Name of character card to play.
     * Effect of the card is applied to the player.
     */
    private final String cardName;
    private final EffectStrategy effect;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for play character card action, initializing card name.
     *
     * @param cardName name of character card to play
     * @param effect   effect of the card
     */
    public ActivateCharacterCard(String cardName, EffectStrategy effect) {
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
        CharacterCard card = gameEngine.getCharacterCards().get(this.cardName);
        PlayerDashboard playerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        if (card.getCost() + card.getCostDelta() > playerDashboard.getCoinsNumber()) {
            throw new IllegalStateException("You don't have enough coins to play this card.");
        }
        card.setEffect(this.effect);
        card.activateEffect(gameEngine, username);

        playerDashboard.setCoinsNumber(playerDashboard.getCoinsNumber() - (card.getCost() + card.getCostDelta()));
        card.setCostDelta(card.getCostDelta() + 1);
    }
    //endregion
}
