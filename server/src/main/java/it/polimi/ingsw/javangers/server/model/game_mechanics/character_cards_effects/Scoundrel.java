package it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.Collections;

/**
 * Class representing the scoundrel character card effect.
 */
public class Scoundrel implements EffectStrategy {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Token color to remove from all the halls.
     */
    private final TokenColor tokenColor;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor of the Scoundrel class, initializing the token color that will be removed from the halls.
     *
     * @param tokenColor token color that will be removed from the halls
     */
    @JsonCreator
    public Scoundrel(@JsonProperty("tokenColor") TokenColor tokenColor) {
        this.tokenColor = tokenColor;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Method called to use the effect of the scoundrel character card.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void useEffect(GameEngine gameEngine, String username) {
        int tokenCount;
        CharacterCard scoundrel = gameEngine.getCharacterCards().get(this.getClass().getSimpleName().toLowerCase());
        for (TokenContainer playerHall : gameEngine.getGameState().getPlayerDashboards().values().stream()
                .map(PlayerDashboard::getHall).toList()) {
            tokenCount = playerHall.getColorCounts().get(this.tokenColor) != null ? playerHall.getColorCounts().get(this.tokenColor) : 0;
            if (tokenCount > 0) {
                gameEngine.getGameState().getStudentsBag().getTokenContainer()
                        .addTokens(playerHall.extractTokens(
                                Collections.nCopies(Math.min(tokenCount, scoundrel.getMultipurposeCounter()), tokenColor)));
            }
        }
    }
    //endregion
}