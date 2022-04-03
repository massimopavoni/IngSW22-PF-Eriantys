package it.polimi.ingsw.javangers.server.model.game_data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing an assistant card.
 */
public class AssistantCard {
    /**
     * The value of the card.
     */
    private final int value;
    /**
     * The mother nature steps limit of the card.
     */
    private final int steps;

    /**
     * Constructor for assistant card, initializing value and steps.
     *
     * @param value value of the card
     * @param steps mother nature steps limit of the card
     */
    @JsonCreator
    public AssistantCard(@JsonProperty("value") int value, @JsonProperty("steps") int steps) {
        this.value = value;
        this.steps = steps;
    }

    /**
     * Get card value.
     *
     * @return value of the card
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Get card steps.
     *
     * @return mother nature steps limit of the card
     */
    public int getSteps() {
        return this.steps;
    }
}
