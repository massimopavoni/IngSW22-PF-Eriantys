package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void doAction(GameEngine gameEngine, String username) {
        List<String> notUsableAssistantCard = new ArrayList<>();
        PlayerDashboard myplayerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        if (!myplayerDashboard.getAssistantCards().containsKey(this.cardName))
            throw new IllegalStateException("Specified card does not exist");
        Map<String, PlayerDashboard> othersPlayerDashboards = gameEngine.getGameState().getPlayerDashboards();
        othersPlayerDashboards.remove(username);
        for (Map.Entry<String, PlayerDashboard> playerDashboardEntry : othersPlayerDashboards.entrySet()) {
            if (myplayerDashboard.getDiscardedAssistantCards().size() < playerDashboardEntry.getValue().getDiscardedAssistantCards().size()) {
                notUsableAssistantCard.add(playerDashboardEntry.getValue().getLastDiscardedAssistantCard().getKey());
            }
        }
        if (notUsableAssistantCard.contains(this.cardName)) {
            for (String name : myplayerDashboard.getAssistantCards().keySet()) {
                if (!notUsableAssistantCard.contains(name))
                    throw new IllegalStateException("Specified player must play a card different from this turn's discards");
            }
        }
        myplayerDashboard.getDiscardedAssistantCards().put(this.cardName, myplayerDashboard.getAssistantCards().remove(this.cardName));
    }
    //endregion
}
