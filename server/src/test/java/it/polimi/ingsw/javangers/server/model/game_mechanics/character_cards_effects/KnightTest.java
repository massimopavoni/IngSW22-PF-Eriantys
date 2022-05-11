package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KnightTest {
    GameEngine gameEngine;
    CharacterCard knight;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<>() {{
                    put("Hawkeye", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("BlackWidow", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        knight = gameEngine.getCharacterCards().get("knight");
    }

    @Test
    @DisplayName("Test useEffect of Knight class for correct behavior")
    void useEffect_correct() {
        Knight knightEffect = new Knight();
        knight.setEffect(knightEffect);
        knight.activateEffect(gameEngine, "Hawkeye");
        assertEquals(knight.getMultipurposeCounter(), gameEngine.getAdditionalPower());
    }
}