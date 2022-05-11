package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoundrelTest {
    GameEngine gameEngine;
    CharacterCard scoundrel;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<>() {{
                    put("Strange", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Panther", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        scoundrel = gameEngine.getCharacterCards().get("scoundrel");
    }

    @Test
    @DisplayName("Test useEffect of Scoundrel class for correct behavior")
    void useEffect_correct() {
        PlayerDashboard player1 = gameEngine.getGameState().getPlayerDashboards().get("Strange");
        PlayerDashboard player2 = gameEngine.getGameState().getPlayerDashboards().get("Panther");
        player1.getHall().addTokens(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.YELLOW_ELF));
        player2.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.PINK_FAIRY));
        Scoundrel scoundrelEffect = new Scoundrel(TokenColor.RED_DRAGON);
        scoundrel.setEffect(scoundrelEffect);
        scoundrel.activateEffect(gameEngine, "Strange");
        assertAll(
                () -> assertEquals(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.YELLOW_ELF), player1.getHall().getTokens()),
                () -> assertEquals(Collections.singletonList(TokenColor.PINK_FAIRY), player2.getHall().getTokens())
        );
    }
}