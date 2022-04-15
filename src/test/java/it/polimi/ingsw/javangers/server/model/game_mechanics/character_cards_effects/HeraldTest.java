package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HeraldTest {
    GameEngine gameEngine;
    CharacterCard herald;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Thor", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Cap", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, true);
        herald = gameEngine.getCharacterCards().get("herald");
    }

    @Test
    @DisplayName("Test useEffect of Herald class for correct behavior")
    void useEffect() {
        for (TokenColor color : TokenColor.values())
            gameEngine.getGameState().getTeachers().get(color).setOwner("Cap", 1);
        for (int i = 0; i < gameEngine.getGameState().getArchipelago().getIslands().size(); i++)
            gameEngine.getGameState().getArchipelago().getIslands().get(i).getTokenContainer().addTokens(
                    Arrays.asList(TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON));
        Herald heraldEffect = new Herald(11);
        herald.setEffect(heraldEffect);
        herald.activateEffect(gameEngine, "Thor");
        assertAll(
                () -> assertEquals(TowerColor.WHITE, gameEngine.getGameState().getArchipelago().getIslands().get(11).getTowers().getValue0()),
                () -> assertEquals(1, gameEngine.getGameState().getArchipelago().getIslands().get(11).getTowers().getValue1())
        );
    }
}