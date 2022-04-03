package it.polimi.ingsw.javangers.server.model.game_data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AssistantCardTest {
    AssistantCard assistantCard;

    @Test
    @DisplayName("Test constructor")
    void AssistantCard_constructor() {
        assistantCard = new AssistantCard(1, 1);
        assertAll(
                () -> assertEquals(1, assistantCard.getValue()),
                () -> assertEquals(1, assistantCard.getSteps())
        );
    }
}
