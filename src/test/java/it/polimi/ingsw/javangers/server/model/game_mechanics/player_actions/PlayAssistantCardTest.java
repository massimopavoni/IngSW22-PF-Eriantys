package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.AssistantCard;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayAssistantCardTest {
    GameEngine gameEngine;

    @Test
    @DisplayName("Test play a non specified card")
    void doAction_notSpecifiedCard() throws GameEngine.GameEngineException {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("test");
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        assertThrowsExactly(IllegalStateException.class,
                () -> playAssistantCard.doAction(gameEngine, "pippo"),
                "Specified card does not exist");
    }

    @Test
    @DisplayName("Test play a specified card")
    void doAction_SpecifiedCard() throws GameEngine.GameEngineException {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("cheetah");
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        playAssistantCard.doAction(gameEngine, "pippo");
        Map<String, AssistantCard> cardPlayed = gameEngine.getGameState().getPlayerDashboards().get("pippo").getDiscardedAssistantCards();
        assertNotNull(cardPlayed.get("cheetah"));
    }

    @Test
    @DisplayName("Test play a specified card and is not discard the same")
    void doAction_notDiscardTheSame() throws GameEngine.GameEngineException {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("cheetah");
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                "2_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        playAssistantCard.doAction(gameEngine, "pippo");
        Map<String, AssistantCard> cardPlayed = gameEngine.getGameState().getPlayerDashboards().get("pippo").getDiscardedAssistantCards();
        assertNull(cardPlayed.get("ostrich"));
    }

    @Test
    @DisplayName("Test play a specified already played with other options")
    void doAction_useCardAlreadyPlayed() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("cheetah");
        playAssistantCard.doAction(gameEngine, "pippo");
        assertThrowsExactly(IllegalStateException.class,
                () -> playAssistantCard.doAction(gameEngine, "pluto"),
                "Specified player must play a card different from this turn's discards");
    }

    @Test
    @DisplayName("Test play a specified already played without other options")
    void doAction_useCardAlreadyPlayedButWithNoChoice() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/game_configurations.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, false);
        String[] cardNames;
        for (Map.Entry<String, PlayerDashboard> playerDashboardEntry : gameEngine.getGameState().getPlayerDashboards().entrySet()) {
            cardNames = playerDashboardEntry.getValue().getAssistantCards().keySet().toArray(new String[0]);
            for (int i = 0; i < 9; i++) {
                playerDashboardEntry.getValue().getDiscardedAssistantCards().put(cardNames[i],
                        playerDashboardEntry.getValue().getAssistantCards().remove(cardNames[i]));
            }
        }
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("turtle");
        playAssistantCard.doAction(gameEngine, "pippo");
        playAssistantCard.doAction(gameEngine, "pluto");
        assertEquals("turtle", gameEngine.getGameState().getPlayerDashboards().get("pluto").getLastDiscardedAssistantCard().getKey());
    }

}