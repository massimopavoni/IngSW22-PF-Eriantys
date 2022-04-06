package it.polimi.ingsw.javangers.server.model.game_mechanics;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActionStrategy;

/**
 * Class representing a player.
 */
public class Player {
    /**
     * Action set for the player to perform.
     */
    private ActionStrategy action;
    /**
     * Flag for character card already used in the same round.
     */
    private boolean enabledCharacterCard;
    /**
     * Number of mother nature steps.
     */
    private int motherNatureSteps;

    /**
     * Constructor for player, initializing enabled character card and mother nature steps.
     */
    public Player() {
        this.enabledCharacterCard = true;
        this.motherNatureSteps = 0;
    }

    /**
     * Execute an action.
     *
     * @param gameState game state instance for modifications
     * @param username  player username
     */
    public void executeAction(GameState gameState, String username) {
        this.action.doAction(gameState, username);
    }

    /**
     * Set action strategy.
     *
     * @param action action to set
     */
    public void setActionStrategy(ActionStrategy action) {
        this.action = action;
    }

    /**
     * Get enabled character card flag value.
     *
     * @return enabled character card flag
     */
    public boolean isEnabledCharacterCard() {
        return this.enabledCharacterCard;
    }

    /**
     * Set enabled character card flag.
     *
     * @param enabled character card flag new value
     */
    public void setEnabledCharacterCard(boolean enabled) {
        this.enabledCharacterCard = enabled;
    }

    /**
     * Get mother nature steps.
     *
     * @return mother nature steps
     */
    public int getMotherNatureSteps() {
        return this.motherNatureSteps;
    }

    /**
     * Set mother nature steps.
     *
     * @param steps new number of steps
     */
    public void setMotherNatureSteps(int steps) {
        this.motherNatureSteps = steps;
    }
}








