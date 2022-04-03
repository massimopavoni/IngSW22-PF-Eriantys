package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;

/**
 * Class representing the play assistant card action.
 */
public class PlayAssistantCard implements ActionStrategy {
    /**
     * Name of assistant card to play.
     */
    private final String cardName;

    /**
     * Constructor for play assistant card action, initializing card name.
     *
     * @param cardName name of assistant card to play
     */
    public PlayAssistantCard(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Action implementation for playing assistant card.
     *
     * @param gameState game state instance
     * @param username  player username
     */
    @Override
    public void doAction(GameState gameState, String username) {
        PlayerDashboard playerDashboard = gameState.getPlayerDashboards().get(username);
        playerDashboard.getDiscardedAssistantCards().put(this.cardName, playerDashboard.getAssistantCards().remove(this.cardName));
    }
}
