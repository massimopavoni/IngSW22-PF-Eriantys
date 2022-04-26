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

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MushroomerTest {
    GameEngine gameEngine;
    CharacterCard mushroomer;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Strange", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Panther", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        mushroomer = gameEngine.getCharacterCards().get("mushroomer");
    }

    @Test
    @DisplayName("Test useEffect of Mushroomer class for correct behavior")
    void useEffect_correct() {
        Mushroomer mushroomerEffect = new Mushroomer(TokenColor.RED_DRAGON);
        mushroomer.setEffect(mushroomerEffect);
        mushroomer.activateEffect(gameEngine, "Panther");
        assertEquals(TokenColor.RED_DRAGON, gameEngine.getForbiddenColor());
    }
}