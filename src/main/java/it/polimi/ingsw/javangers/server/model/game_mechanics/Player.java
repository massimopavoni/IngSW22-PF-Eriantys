package it.polimi.ingsw.javangers.server.model.game_mechanics;

import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActionStrategy;

/**
 * Class representing a player.
 */
public class Player {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Action set for the player to perform.
     */
    private ActionStrategy action;
    /**
     * Flag for character card already used in the same round.
     */
    private boolean enabledCharacterCard;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for player, initializing enabled character card and mother nature steps.
     */
    public Player() {
        this.enabledCharacterCard = true;
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
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Execute an action.
     *
     * @param gameEngine game state instance for modifications
     * @param username   player username
     */
    public void executeAction(GameEngine gameEngine, String username) {
        this.action.doAction(gameEngine, username);
    }
    //endregion
}








