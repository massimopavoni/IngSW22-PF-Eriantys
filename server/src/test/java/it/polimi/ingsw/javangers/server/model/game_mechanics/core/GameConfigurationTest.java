package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameConfigurationTest {
    GameConfiguration gameConfiguration;

    @Test
    @DisplayName("Test GameConfiguration constructor")
    void GameConfiguration_constructor() {
        gameConfiguration = new GameConfiguration(
                "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                "/it/polimi/ingsw/javangers/server/model/game_mechanics/character_cards.json",
                12, 26, 3, 7,
                8, 3, 1);
        assertAll(
                () -> assertEquals("/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                        gameConfiguration.assistantCardsResourceLocation()),
                () -> assertEquals("/it/polimi/ingsw/javangers/server/model/game_mechanics/character_cards.json",
                        gameConfiguration.characterCardsResourceLocation()),
                () -> assertEquals(12, gameConfiguration.numberOfIslands()),
                () -> assertEquals(26, gameConfiguration.studentsPerColor()),
                () -> assertEquals(3, gameConfiguration.studentsPerCloud()),
                () -> assertEquals(7, gameConfiguration.studentsPerEntrance()),
                () -> assertEquals(8, gameConfiguration.towersPerDashboard()),
                () -> assertEquals(3, gameConfiguration.numberOfCharacterCards()),
                () -> assertEquals(1, gameConfiguration.coinsPerDashBoard())
        );
    }
}