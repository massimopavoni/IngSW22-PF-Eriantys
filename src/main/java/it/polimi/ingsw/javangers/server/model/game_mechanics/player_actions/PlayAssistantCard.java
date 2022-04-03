package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.AssistantCard;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to play an assistant card.
 */
public class PlayAssistantCard implements ActionStrategy{

    /**
     * name of assistant card to play.
     */
    private final String cardName;

    /**
     * Constructor of PlayAssistantCard.
     *
     * @param cardName name of assistant card to play
     */
    public PlayAssistantCard(String cardName){
        this.cardName = cardName;
    }

    /**
     * Action to play assistant card.
     *
     * @param gameState status of the game
     * @param username  nickname of the player
     */
    @Override
    public void doAction(GameState gameState, String username) {
        Map<String, PlayerDashboard> playerDashboardMap = gameState.getPlayerDashboards();
        PlayerDashboard playerDashboard = playerDashboardMap.get(username);
        AssistantCard assistantCard = playerDashboard.getAssistantCards().stream()
                .filter(a -> a.getName().equalsIgnoreCase(this.cardName)).collect(Collectors.toList()).get(0);
        playerDashboard.getAssistantCards().remove(assistantCard);
        playerDashboard.getDiscardedAssistantCards().add(0, assistantCard);
    }
}
