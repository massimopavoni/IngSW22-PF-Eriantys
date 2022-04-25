package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import org.javatuples.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDashboardTest {
    PlayerDashboard playerDashboard;

    @Test
    @DisplayName("Test correct constructor")
    void PlayerDashboard_correctConstructor() {
        assertAll(
                () -> assertDoesNotThrow(() -> playerDashboard = new PlayerDashboard(
                        "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                        WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1)),
                () -> assertNotNull(playerDashboard.getEntrance()),
                () -> assertTrue(playerDashboard.getEntrance().getTokens().isEmpty()),
                () -> assertNotNull(playerDashboard.getHall()),
                () -> assertTrue(playerDashboard.getHall().getTokens().isEmpty()),
                () -> assertNotNull(playerDashboard.getAssistantCards()),
                () -> assertEquals(10, playerDashboard.getAssistantCards().size()),
                () -> assertEquals("eagle", playerDashboard.getAssistantCards().keySet().toArray()[3]),
                () -> assertEquals(4, playerDashboard.getAssistantCards().get("eagle").getValue()),
                () -> assertEquals(2, playerDashboard.getAssistantCards().get("eagle").getSteps()),
                () -> assertNotNull(playerDashboard.getDiscardedAssistantCards()),
                () -> assertTrue(playerDashboard.getDiscardedAssistantCards().isEmpty()),
                () -> assertEquals(WizardType.DRUID, playerDashboard.getCardsBack()),
                () -> assertEquals(new Pair<>(TowerColor.WHITE, 8), playerDashboard.getTowers()),
                () -> assertEquals(1, playerDashboard.getCoinsNumber())
        );
    }

    @Test
    @DisplayName("Test constructor with non json file type")
    void PlayerDashboard_wrongFileType() {
        assertThrowsExactly(IOException.class,
                () -> new PlayerDashboard("/it/polimi/ingsw/javangers/server/model/game_data/AssistantCard.class",
                        WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1));
    }

    @Test
    @DisplayName("Test getLastDiscardedAssistantCard for null last discard")
    void getLastDiscardedAssistantCard_nullLastDiscard() throws IOException {
        playerDashboard = new PlayerDashboard(
                "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1);
        assertNull(playerDashboard.getLastDiscardedAssistantCard());
    }

    @Test
    @DisplayName("Test getLastDiscardedAssistantCard for correct last discard")
    void getLastDiscardedAssistantCard_correctLastDiscard() throws IOException {
        playerDashboard = new PlayerDashboard(
                "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1);
        playerDashboard.getDiscardedAssistantCards().put("eagle", playerDashboard.getAssistantCards().remove("eagle"));
        playerDashboard.getDiscardedAssistantCards().put("octopus", playerDashboard.getAssistantCards().remove("octopus"));
        assertAll(
                () -> assertEquals("octopus", playerDashboard.getLastDiscardedAssistantCard().getKey()),
                () -> assertEquals(7, playerDashboard.getLastDiscardedAssistantCard().getValue().getValue()),
                () -> assertEquals(4, playerDashboard.getLastDiscardedAssistantCard().getValue().getSteps())
        );
    }

    @Test
    @DisplayName("Test setTowersNumber for changing towers number")
    void setTowersNumber_changePair() throws IOException {
        playerDashboard = new PlayerDashboard(
                "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1);
        TowerColor previousTowerColor = playerDashboard.getTowers().getValue0();
        playerDashboard.setTowersNumber(6);
        assertAll(
                () -> assertEquals(previousTowerColor, playerDashboard.getTowers().getValue0()),
                () -> assertEquals(6, playerDashboard.getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test setCoinsNumber for updating coins")
    void setCoinsNumber_updateCoins() throws IOException {
        playerDashboard = new PlayerDashboard(
                "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1);
        playerDashboard.setCoinsNumber(5);
        assertEquals(5, playerDashboard.getCoinsNumber());
    }
}