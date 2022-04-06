package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TokenContainerTest {
    TokenContainer tokenContainer;

    @BeforeEach
    void setUp() {
        tokenContainer = new TokenContainer();
    }

    @Test
    @DisplayName("Test constructor")
    void TokenContainer_constructor() {
        assertNotNull(tokenContainer.getTokens());
    }

    @Test
    @DisplayName("Test getTokens for tokensList deep copy")
    void getTokens_deepCopy() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        List<TokenColor> tokens = tokenContainer.getTokens();
        tokens.add(TokenColor.BLUE_UNICORN);
        assertNotEquals(tokens, tokenContainer.getTokens());
    }

    @Test
    @DisplayName("Test getColorCounts for correct counts")
    void getColorCounts_correctCount() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        Map<TokenColor, Integer> colorCounts = tokenContainer.getColorCounts();
        assertEquals(2, colorCounts.get(TokenColor.RED_DRAGON));
    }

    @Test
    @DisplayName("Test addTokens for adding tokens to tokensList")
    void addTokens_correctList() {
        List<TokenColor> tokens = Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON);
        tokenContainer.addTokens(tokens);
        assertAll(
                () -> assertEquals(2, tokenContainer.getTokens().size()),
                () -> assertEquals(tokens, tokenContainer.getTokens())
        );
    }

    @Test
    @DisplayName("Test containsSubList for true")
    void containsSubList_true() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        assertTrue(tokenContainer.containsSubList(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON)));
    }

    @Test
    @DisplayName("Test containsSubList for false")
    void containsSubList_false() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        assertFalse(tokenContainer.containsSubList(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN)));
    }

    @Test
    @DisplayName("Test extractTokens only specified tokens")
    void extractTokens_onlySpecifiedTokens() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN,
                TokenColor.GREEN_FROG, TokenColor.PINK_FAIRY));
        List<TokenColor> tokens = tokenContainer.extractTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        assertAll(
                () -> assertEquals(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN), tokens),
                () -> assertEquals(Arrays.asList(TokenColor.GREEN_FROG, TokenColor.PINK_FAIRY), tokenContainer.getTokens())
        );
    }

    @Test
    @DisplayName("Test extractTokens throws IllegalArgumentException if cardinality wrong")
    void extractTokens_wrongCardinality() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        assertAll(
                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> tokenContainer.extractTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON)), "Token not in container"),
                () -> assertEquals(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN), tokenContainer.getTokens())
        );
    }

    @Test
    @DisplayName("Test extractTokens throws IllegalArgumentException if tokens not present")
    void extractTokens_notPresent() {
        tokenContainer.addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        assertAll(
                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> tokenContainer.extractTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.GREEN_FROG)), "Token not in container"),
                () -> assertEquals(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN), tokenContainer.getTokens())
        );
    }
}