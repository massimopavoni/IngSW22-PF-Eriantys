package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;

class CentaurTest {
    GameEngine gameEngine;
    CharacterCard centaur;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Thor", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Cap", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        centaur = gameEngine.getCharacterCards().get("centaur");
    }

    @Test
    @DisplayName("Test useEffect of Centaur class for correct behavior")
    void useEffect_correct() {
        Centaur centaurEffect = new Centaur();
        centaur.setEffect(centaurEffect);
        centaur.activateEffect(gameEngine, "Thor");
        assertFalse(gameEngine.getEnabledIslandTowers());
    }
}