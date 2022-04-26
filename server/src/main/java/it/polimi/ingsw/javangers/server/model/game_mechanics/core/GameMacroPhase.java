package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a macro phase of the game.
 */
public class GameMacroPhase {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * List of phases in this macro phase.
     */
    private final List<GamePhase> phases;
    /**
     * Flag for repeating the macro phase.
     */
    private final boolean repeat;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for game macro phase, initializing phases and repeat flag.
     *
     * @param phases list of phases in this macro phase
     * @param repeat flag for repeating the macro phase
     */
    @JsonCreator
    public GameMacroPhase(@JsonProperty("phases") List<GamePhase> phases, @JsonProperty("repeat") boolean repeat) {
        this.phases = phases;
        this.repeat = repeat;
    }

    /**
     * Get shallow copy of phases.
     *
     * @return shallow copy of phases
     */
    public List<GamePhase> getPhases() {
        return new ArrayList<>(this.phases);
    }

    /**
     * Get flag for repeating the macro phase.
     *
     * @return flag for repeating the macro phase
     */
    public boolean isRepeat() {
        return this.repeat;
    }
    //endregion
}
