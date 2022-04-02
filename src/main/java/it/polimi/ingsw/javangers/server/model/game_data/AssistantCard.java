package it.polimi.ingsw.javangers.server.model.game_data;

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
    public AssistantCard(String name, int value, int steps) {
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
        return name;
    }

    /**
     * Get card value.
     *
     * @return value of the card
     */
    public int getValue() {
        return value;
    }

    /**
     * Get card steps.
     *
     * @return mother nature steps limit of the card
     */
    public int getSteps() {
        return steps;
    }
}
