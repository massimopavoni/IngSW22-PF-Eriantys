package it.polimi.ingsw.javangers.server.model.game_mechanics;

import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActionStrategy;

/**
 * Class representing a player.
 */

public class Player {
    /**
     * Action the player performs.
     */
    private ActionStrategy action;
    /**
     * Flag for has already been used in the same round.
     */
    private boolean enabledCharacterCard;
    /**
     * Number of mother nature steps.
     */
    private int motherNatureSteps;

    /**
     * Constructor for player, initializing enabledCharacterCard, motherNatureSteps and action.
     * @param enabledCharacterCard
     * @param motherNatureSteps
     * @param action
     */
    public Player(boolean enabledCharacterCard, int motherNatureSteps, ActionStrategy action) {

        this.action = action;
        this.enabledCharacterCard = true;
        this.motherNatureSteps = motherNatureSteps;

    }

    /**
     * Execute an Action.
     * @param gamestate
     * @param username
     * @return action used
     */
    public boolean executeAction(GameState gamestate, String username) {
        return action.doAction(gamestate, username);
    }

    /**
     * Set action strategy.
     * @param action action used
     */
    public void setActionStrategy(ActionStrategy action) {
        this.action = action;
    }

    /**
     * Get status of enabled character card.
     * @return enabled character card
     */
    public boolean isEnabledCharacterCard() {
        return this.enabledCharacterCard;
    }

    /**
     * Set enabled character card.
     * @param enabled character card
     */
    public void setEnabledCharacterCard(boolean enabled) {
        this.enabledCharacterCard = enabled;
    }

    /**
     * Set steps of mather nature.
      * @param steps number of steps
     */
    public void setMotherNatureSteps(int steps) {
        this.motherNatureSteps = steps;
    }

}








