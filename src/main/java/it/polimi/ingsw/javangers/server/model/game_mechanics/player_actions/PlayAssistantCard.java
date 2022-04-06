package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;

/**
 * Class representing the play assistant card action.
 */
public class PlayAssistantCard implements ActionStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Name of assistant card to play.
     */
    private final String cardName;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for play assistant card action, initializing card name.
     *
     * @param cardName name of assistant card to play
     */
    public PlayAssistantCard(String cardName) {
        this.cardName = cardName;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Action implementation for playing assistant card.
     *
     * @param gameState game state instance
     * @param username  player username
     */
    @Override
    public void doAction(GameState gameState, String username) {
        PlayerDashboard playerDashboard = gameState.getPlayerDashboards().get(username);
        if (!playerDashboard.getAssistantCards().containsKey(this.cardName))
            throw new IllegalStateException("Specified card does not exist");
        playerDashboard.getDiscardedAssistantCards().put(this.cardName, playerDashboard.getAssistantCards().remove(this.cardName));
    }
    //endregion
}
