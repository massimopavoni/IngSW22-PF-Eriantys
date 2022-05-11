package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a phase of the game.
 *
 * @param availableActions list of available actions in this phase
 * @param repeat           flag for repeating the phase
 * @param changePlayer     flag for changing player before next phase
 */
public record GamePhase(@JsonProperty("availableActions") List<String> availableActions,
                        @JsonProperty("repeat") boolean repeat, @JsonProperty("changePlayer") boolean changePlayer) {
    /**
     * Get shallow copy of available actions.
     *
     * @return shallow copy of available actions
     */
    @Override
    public List<String> availableActions() {
        return new ArrayList<>(this.availableActions);
    }
}
