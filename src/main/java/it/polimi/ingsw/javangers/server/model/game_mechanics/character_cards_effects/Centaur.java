package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;

/**
 * Class that represents the centaur character card.
 */
public class Centaur implements EffectStrategy {

    /**
     * Method called to use the effect of the centaur character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        gameEngine.setEnabledIslandTowers(false);
    }
}
