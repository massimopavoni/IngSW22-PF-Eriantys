package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CloudTest {
    Cloud cloud;

    @BeforeEach
    void setUp() {
        cloud = new Cloud();
    }

    @Test
    @DisplayName("Test constructor")
    void Cloud_constructor() {
        assertAll(
                () -> assertNotNull(cloud.getTokenContainer()),
                () -> assertTrue(cloud.getTokenContainer().getTokens().isEmpty())
        );
    }

    @Test
    @DisplayName("Test grabTokens for taking all tokens from the cloud")
    void grabTokens_takesAll() {
        cloud.getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN,
                TokenColor.GREEN_FROG, TokenColor.PINK_FAIRY, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON));
        List<TokenColor> grabbedTokens = cloud.grabTokens();
        assertAll(
                () -> assertTrue(cloud.getTokenContainer().getTokens().isEmpty()),
                () -> assertEquals(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN,
                                TokenColor.GREEN_FROG, TokenColor.PINK_FAIRY, TokenColor.YELLOW_ELF, TokenColor.RED_DRAGON),
                        grabbedTokens)
        );
    }

    @Test
    @DisplayName("Test grabTokens when the cloud is empty")
    void grabTokens_emptyCloud() {
        List<TokenColor> grabbedTokens = cloud.grabTokens();
        assertAll(
                () -> assertTrue(cloud.getTokenContainer().getTokens().isEmpty()),
                () -> assertTrue(grabbedTokens.isEmpty())
        );
    }
}