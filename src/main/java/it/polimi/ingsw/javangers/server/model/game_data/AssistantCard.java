package it.polimi.ingsw.javangers.server.model.game_data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing an assistant card.
 */
public class AssistantCard {
    /**
     * The name of the card.
     */
    private final String name;
    /**
     * The value of the card.
     */
    private final int value;
    /**
     * d
     * The mother nature steps limit of the card.
     */
    private final int steps;

    /**
     * Constructor for assistant card, initializing name, value and steps.
     *
     * @param name  name of the card
     * @param value value of the card
     * @param steps mother nature steps limit of the card
     */
    @JsonCreator
    public AssistantCard(@JsonProperty("name")String name, @JsonProperty("value")int value, @JsonProperty("steps")int steps) {
        this.name = name;
        this.value = value;
        this.steps = steps;
    }

    /**
     * Get card name.
     *
     * @return name of the card
     */
    public String getName() {
        return this.name;
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
