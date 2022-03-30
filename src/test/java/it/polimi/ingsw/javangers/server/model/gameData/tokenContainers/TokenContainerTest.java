package it.polimi.ingsw.javangers.server.model.gameData.tokenContainers;

import it.polimi.ingsw.javangers.server.model.gameData.enums.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TokenContainerTest {
    TokenContainer<Void> container;

    @BeforeEach
    void setUp() {
        container = new TokenContainer<Void>() {
            @Override
            public List<TokenColor> grabTokens(Void condition) {
                return null;
            }
        };
    }

    @Test
    @DisplayName("Test getTokens for tokensList deep copy")
    void getTokens() {
        container.addTokens(Arrays.asList(TokenColor.RedDragon, TokenColor.RedDragon));
        List<TokenColor> tokens = container.getTokens();
        tokens.add(TokenColor.BlueUnicorn);
        assertNotSame(tokens, container.getTokens());
    }

    @Test
    @DisplayName("Test getColorCounts for correct counts")
    void getColorCounts() {
        container.addTokens(Arrays.asList(TokenColor.RedDragon, TokenColor.RedDragon));
        Map<TokenColor, Integer> colorCounts = container.getColorCounts();
        assertEquals(2, colorCounts.get(TokenColor.RedDragon));
    }

    @Test
    @DisplayName("Test addTokens for adding tokens to tokensList")
    void addTokens() {
        container.addTokens(Arrays.asList(TokenColor.RedDragon, TokenColor.RedDragon));
        assertAll(
                () -> assertEquals(2, container.getTokens().size()),
                () -> assertEquals(TokenColor.RedDragon, container.getTokens().get(0)),
                () -> assertEquals(TokenColor.RedDragon, container.getTokens().get(1))
        );
    }
}