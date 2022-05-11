package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a macro phase of the game.
 *
 * @param phases list of phases in this macro phase
 * @param repeat flag for repeating the macro phase
 */
public record GameMacroPhase(@JsonProperty("phases") List<GamePhase> phases, @JsonProperty("repeat") boolean repeat) {
    /**
     * Get shallow copy of phases.
     *
     * @return shallow copy of phases
     */
    @Override
    public List<GamePhase> phases() {
        return new ArrayList<>(this.phases);
    }
}
