package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_mechanics.GameEngine;

import java.util.List;

/**
 * Class representing bard character card.
 */
public class Bard implements EffectStrategy {
    /**
     * Tokens that will be moved from hall to entrance.
     */
    private final List<TokenColor> tokensFromHall;

    /**
     * Tokens that will be moved from entrance to hall.
     */
    private final List<TokenColor> tokensFromEntrance;

    /**
     * Constructor of the Bard class, initializing the lists that will be exchanged.
     *
     * @param tokensFromHall     list of tokens that will be moved from hall to entrance
     * @param tokensFromEntrance list of tokens that will be moved from entrance to hall
     */
    public Bard(List<TokenColor> tokensFromHall, List<TokenColor> tokensFromEntrance) {
        this.tokensFromEntrance = tokensFromEntrance;
        this.tokensFromHall = tokensFromHall;
    }

    /**
     * Method called to use the effect of the bard character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        if (this.tokensFromHall.size() != this.tokensFromEntrance.size()
                || this.tokensFromHall.size() > gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase()).getMultipurposeCounter()
                || this.tokensFromEntrance.size() > gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase()).getMultipurposeCounter()) {
            throw new IllegalArgumentException();
        }
        PlayerDashboard playerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        playerDashboard.getHall().addTokens(playerDashboard.getEntrance().extractTokens(tokensFromEntrance));
        playerDashboard.getEntrance().addTokens(playerDashboard.getHall().extractTokens(tokensFromHall));
    }
}
