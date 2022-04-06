package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.AssistantCard;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayAssistantCardTest {
    GameState gameState;

    @Test
    @DisplayName("Test constructor")
    void PlayAssistantCard_constructor() {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("test");
        assertTrue(true);
    }

    @Test
    @DisplayName("Test play a non specified card")
    void doAction_notSpecifiedCard() throws GameState.GameStateException {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("test");
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                }}, 8, 12, new HashMap<TokenColor, Integer>(), 1);
        assertThrowsExactly(IllegalStateException.class,
                () -> playAssistantCard.doAction(gameState, "pippo"),
                "Specified card does not exist");
    }

    @Test
    @DisplayName("Test play a specified card")
    void doAction_SpecifiedCard() throws GameState.GameStateException {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("cheetah");
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                }}, 8, 12, new HashMap<TokenColor, Integer>(), 1);
        playAssistantCard.doAction(gameState, "pippo");
        Map<String, AssistantCard> cardPlayed = gameState.getPlayerDashboards().get("pippo").getDiscardedAssistantCards();
        assertNotNull(cardPlayed.get("cheetah"));
    }

    @Test
    @DisplayName("Test play a specified card and is not discard the same")
    void doAction_notDiscardTheSame() throws GameState.GameStateException {
        PlayAssistantCard playAssistantCard = new PlayAssistantCard("cheetah");
        gameState = new GameState("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                }}, 8, 12, new HashMap<TokenColor, Integer>(), 1);
        playAssistantCard.doAction(gameState, "pippo");
        Map<String, AssistantCard> cardPlayed = gameState.getPlayerDashboards().get("pippo").getDiscardedAssistantCards();
        assertNull(cardPlayed.get("ostrich"));

    }
}