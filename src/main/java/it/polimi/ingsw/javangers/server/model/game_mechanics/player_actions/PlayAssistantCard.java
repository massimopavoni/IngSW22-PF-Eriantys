package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        PlayerDashboard currentPlayerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        if (!currentPlayerDashboard.getAssistantCards().containsKey(this.cardName))
            throw new IllegalStateException("Specified card does not exist in player's available assistant cards");
        List<String> forbiddenAssistantCards = new ArrayList<>();
        Map<String, PlayerDashboard> otherPlayersDashboard = gameEngine.getGameState().getPlayerDashboards();
        otherPlayersDashboard.remove(username);
        otherPlayersDashboard.values().stream()
                .filter(dashboard -> currentPlayerDashboard.getDiscardedAssistantCards().size() < dashboard.getDiscardedAssistantCards().size())
                .forEach(dashboard -> forbiddenAssistantCards.add(dashboard.getLastDiscardedAssistantCard().getKey()));
        if (forbiddenAssistantCards.contains(this.cardName))
            for (String name : currentPlayerDashboard.getAssistantCards().keySet())
                if (!forbiddenAssistantCards.contains(name))
                    throw new IllegalStateException("Specified player must play a card different from this turn's discards");
        currentPlayerDashboard.getDiscardedAssistantCards().put(this.cardName, currentPlayerDashboard.getAssistantCards().remove(this.cardName));
    }
    //endregion
}
