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

import static org.junit.jupiter.api.Assertions.*;

class HerbalistTest {
    GameEngine gameEngine;
    CharacterCard herbalist;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Hulk", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("IronMan", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        herbalist = gameEngine.getCharacterCards().get("herbalist");
    }

    @Test
    @DisplayName("Test useEffect for throwing exception for lack of inhibition tokens on the card")
    void useEffect_exceptionNoMoreInhibitions() {
        herbalist.setMultipurposeCounter(0);
        Herbalist herbalistEffect = new Herbalist(0);
        herbalist.setEffect(herbalistEffect);
        assertThrowsExactly(IllegalStateException.class,
                () -> herbalist.activateEffect(gameEngine, "Hulk"));

    }

    @Test
    @DisplayName("Test useEffect for throwing exception for island already disabled")
    void useEffect_exceptionAlreadyDisabledIsland() {
        gameEngine.getGameState().getArchipelago().getIslands().get(0).setEnabled(1);
        Herbalist herbalistEffect = new Herbalist(0);
        herbalist.setEffect(herbalistEffect);
        assertThrowsExactly(IllegalStateException.class,
                () -> herbalist.activateEffect(gameEngine, "Hulk"));

    }

    @Test
    @DisplayName("Test useEffect of Herbalist class for correct behavior")
    void useEffect_correct() {
        Herbalist herbalistEffect = new Herbalist(0);
        herbalist.setEffect(herbalistEffect);
        herbalist.activateEffect(gameEngine, "Hulk");
        assertAll(
                () -> assertEquals(1, gameEngine.getGameState().getArchipelago().getIslands().get(0).getEnabled()),
                () -> assertEquals(3, herbalist.getMultipurposeCounter())
        );
    }
}