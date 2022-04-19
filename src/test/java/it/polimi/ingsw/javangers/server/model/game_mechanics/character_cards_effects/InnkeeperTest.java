package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InnkeeperTest {
    GameEngine gameEngine;
    CharacterCard innkeeper;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Hulk", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("IronMan", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        innkeeper = gameEngine.getCharacterCards().get("innkeeper");
    }

    @Test
    @DisplayName("Test useEffect of the Innkeeper class for correct behavior")
    void useEffect_correct() {
        gameEngine.getGameState().getPlayerDashboards().get("Hulk").getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.getGameState().getPlayerDashboards().get("IronMan").getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).setOwner("Hulk", 1);
        Innkeeper innkeeperEffect = new Innkeeper();
        innkeeper.setEffect(innkeeperEffect);
        innkeeper.activateEffect(gameEngine, "IronMan");
        assertAll(
                () -> assertTrue(gameEngine.getTeachersEqualCount()),
                () -> assertEquals("IronMan", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername())
        );
    }
}