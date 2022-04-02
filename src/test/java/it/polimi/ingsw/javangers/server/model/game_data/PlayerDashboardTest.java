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
                () -> assertEquals("Cheetah", playerDashboard.getAssistantCards().get(0).getName()),
                () -> assertEquals(1, playerDashboard.getAssistantCards().get(0).getValue()),
                () -> assertEquals(1, playerDashboard.getAssistantCards().get(0).getSteps()),
                () -> assertNotNull(playerDashboard.getDiscardedAssistantCards()),
                () -> assertTrue(playerDashboard.getDiscardedAssistantCards().isEmpty()),
                () -> assertEquals(WizardType.DRUID, playerDashboard.getCardsBack()),
                () -> assertEquals(new Pair<>(TowerColor.WHITE, 8), playerDashboard.getTowers()),
                () -> assertEquals(1, playerDashboard.getCoinsNumber())
        );
    }

    @Test
    @DisplayName("Test constructor with non existing resource path")
    void PlayerDashboard_nonExistingResourcePath() {
        assertThrowsExactly(NullPointerException.class,
                () -> new PlayerDashboard("/it/polimi/ingsw/javangers/server/model/game_data/trolol.json",
                        WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1),
                "Assistant cards json file resource not found");
    }

    @Test
    @DisplayName("Test constructor with null resource path")
    void PlayerDashboard_nullResourcePath() {
        assertThrowsExactly(NullPointerException.class,
                () -> new PlayerDashboard(null,
                        WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1),
                "Assistant cards json file resource not found");
    }

    @Test
    @DisplayName("Test constructor with non json file type")
    void PlayerDashboard() {
        assertThrowsExactly(IOException.class,
                () -> new PlayerDashboard("/it/polimi/ingsw/javangers/server/model/game_data/AssistantCard.class",
                        WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1),
                "Error while reading assistant cards json file");
    }

    @Test
    @DisplayName("Test setTowersAmount for changing towers amount")
    void setTowersAmount_changePair() throws IOException {
        playerDashboard = new PlayerDashboard(
                "/it/polimi/ingsw/javangers/server/model/game_data/assistant_cards.json",
                WizardType.DRUID, new Pair<>(TowerColor.WHITE, 8), 1);
        TowerColor previousTowerColor = playerDashboard.getTowers().getValue0();
        playerDashboard.setTowersAmount(6);
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