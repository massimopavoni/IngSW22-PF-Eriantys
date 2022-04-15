package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.Bard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.EffectStrategy;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ActivateCharacterCardTest {
    GameEngine gameEngine;
    ActivateCharacterCard characterCard;

    @Test
    @DisplayName("Test illegal coin number")
    void doAction_coinException() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, true);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").setCoinsNumber(0);
        EffectStrategy effect = new Bard(gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().getTokens(), gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().getTokens());
        characterCard = new ActivateCharacterCard("bard", effect);
        assertThrowsExactly(IllegalStateException.class, () -> characterCard.doAction(gameEngine, "pippo"));
    }

    @Test
    @DisplayName("Test assign effect with card name")
    void doAction_assignEffect() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, true);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").setCoinsNumber(4);
        EffectStrategy effect = new Bard(gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().getTokens(),
                gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().getTokens());
        characterCard = new ActivateCharacterCard("bard", effect);
        characterCard.doAction(gameEngine, "pippo");
        assertAll(
                () -> assertEquals(3, gameEngine.getGameState().getPlayerDashboards().get("pippo").getCoinsNumber()),
                () -> assertEquals(1, gameEngine.getCharacterCards().get("bard").getCostDelta())
        );
    }

    @Test
    @DisplayName("Test illegal effect with card name")
    void doAction_illegalEffect() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("pippo", new Pair<>(WizardType.KING, TowerColor.WHITE));
                    put("pluto", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
                }}, true);
        gameEngine.getGameState().getPlayerDashboards().get("pippo").setCoinsNumber(0);
        EffectStrategy effect = new Bard(gameEngine.getGameState().getPlayerDashboards().get("pippo").getHall().getTokens(),
                gameEngine.getGameState().getPlayerDashboards().get("pippo").getEntrance().getTokens());
        characterCard = new ActivateCharacterCard("centaur", effect);
        assertThrowsExactly(IllegalStateException.class, () -> characterCard.doAction(gameEngine, "pippo"));
    }


}