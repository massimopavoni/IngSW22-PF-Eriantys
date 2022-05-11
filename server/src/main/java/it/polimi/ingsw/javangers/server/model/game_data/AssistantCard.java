package it.polimi.ingsw.javangers.server.model.game_data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing an assistant card.
 *
 * @param value The value of the card.
 * @param steps The mother nature steps limit of the card.
 */
public record AssistantCard(@JsonProperty("value") int value, @JsonProperty("steps") int steps) {
}
