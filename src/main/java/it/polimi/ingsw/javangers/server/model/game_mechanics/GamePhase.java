package it.polimi.ingsw.javangers.server.model.game_mechanics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a phase of the game.
 */
public class GamePhase {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * List of available actions in this phase.
     */
    private final List<String> availableActions;
    /**
     * Flag for repeating the phase.
     */
    private final boolean repeat;
    /**
     * Flag for changing player before next phase.
     */
    private final boolean changePlayer;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for game phase, initializing available actions and flags.
     *
     * @param availableActions list of available actions in this phase
     * @param repeat           flag for repeating the phase
     * @param changePlayer     flag for changing player before next phase
     */
    @JsonCreator
    public GamePhase(@JsonProperty("availableActions") List<String> availableActions,
                     @JsonProperty("repeat") boolean repeat, @JsonProperty("changePlayer") boolean changePlayer) {
        this.availableActions = availableActions;
        this.repeat = repeat;
        this.changePlayer = changePlayer;
    }

    /**
     * Get shallow copy of available actions.
     *
     * @return shallow copy of available actions
     */
    public List<String> getAvailableActions() {
        return new ArrayList<>(this.availableActions);
    }

    /**
     * Get flag for repeating the phase.
     *
     * @return flag for repeating the phase
     */
    public boolean isRepeat() {
        return this.repeat;
    }

    /**
     * Get flag for changing player before next phase.
     *
     * @return flag for changing player before next phase
     */
    public boolean isChangePlayer() {
        return this.changePlayer;
    }
    //endregion
}
