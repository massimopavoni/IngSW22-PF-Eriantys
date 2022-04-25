package it.polimi.ingsw.javangers.server.model.game_mechanics;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.Knight;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterCardTest {
    CharacterCard knight;

    @BeforeEach
    void setUp() {
        knight = new CharacterCard(2, 0, 2);
    }

    @Test
    @DisplayName("Test CharacterCard constructor")
    void CharacterCard_correctConstructor() {
        assertAll(
                () -> assertEquals(2, knight.getCost()),
                () -> assertEquals(0, knight.getCostDelta()),
                () -> assertEquals(0, knight.getTokenContainerSize()),
                () -> assertEquals(0, knight.getTokenContainer().getTokens().size()),
                () -> assertEquals(2, knight.getMultipurposeCounter())
        );
    }

    @Test
    @DisplayName("Test setCostDelta correct set")
    void setCostDelta_correct() {
        knight.setCostDelta(1);
        assertAll(
                () -> assertEquals(2, knight.getCost()),
                () -> assertEquals(1, knight.getCostDelta())
        );
    }

    @Test
    @DisplayName("Test setMultipurposeCounter correct set")
    void setMultipurposeCounter_correct() {
        knight.setMultipurposeCounter(0);
        assertEquals(0, knight.getMultipurposeCounter());
    }

    @Test
    @DisplayName("Test activateEffect correct effect")
    void activateEffect_correct() throws GameEngine.GameEngineException {
        GameEngine gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Aragorn II Elessar", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Elrond", new Pair<>(WizardType.KING, TowerColor.WHITE));
                }}, true);
        knight.setEffect(new Knight());
        knight.activateEffect(gameEngine, "Aragorn II Elessar");
        assertEquals(2, gameEngine.getAdditionalPower());
    }
}