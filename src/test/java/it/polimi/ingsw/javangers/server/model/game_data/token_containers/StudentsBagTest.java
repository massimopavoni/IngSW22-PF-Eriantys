package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentsBagTest {
    StudentsBag studentsBag;

    @Test
    @DisplayName("Test constructor")
    void StudentsBag_constructor() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 5);
        studentsBag = new StudentsBag(studentsPerColor);
        assertAll(
                () -> assertNotNull(studentsBag.getTokenContainer()),
                () -> assertEquals(Collections.nCopies(5, TokenColor.RED_DRAGON), studentsBag.getTokenContainer().getTokens()),
                () -> assertNotEquals(Collections.nCopies(4, TokenColor.RED_DRAGON), studentsBag.getTokenContainer().getTokens()),
                () -> assertNotEquals(Collections.nCopies(6, TokenColor.RED_DRAGON), studentsBag.getTokenContainer().getTokens()),
                () -> assertNotEquals(Collections.nCopies(5, TokenColor.BLUE_UNICORN), studentsBag.getTokenContainer().getTokens())
        );
    }

    @Test
    @DisplayName("Test grabTokens extract a correctly number of tokens from tokensList")
    void grabTokens_numberOfExtraction() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 2);
        studentsPerColor.put(TokenColor.PINK_FAIRY, 4);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 3);
        int amout = 6;
        studentsBag = new StudentsBag(studentsPerColor);
        List<TokenColor> grabbedTokens = studentsBag.grabTokens(amout);
        assertAll(
                () -> assertEquals(studentsBag.getTokenContainer().getTokens().size(), (9 - amout)),
                () -> assertTrue(grabbedTokens.contains(TokenColor.PINK_FAIRY))
        );

    }

    @Test
    @DisplayName("Test grabTokens modify the initial list")
    void grabTokens_InitialList() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 1);
        studentsPerColor.put(TokenColor.PINK_FAIRY, 2);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 1);
        studentsBag = new StudentsBag(studentsPerColor);
        List<TokenColor> prevList = studentsBag.getTokenContainer().getTokens();
        List<TokenColor> grabbedTokens = studentsBag.grabTokens(2);
        assertAll(
                () -> assertNotEquals(prevList, grabbedTokens)
        );

    }

    @Test
    @DisplayName("Test grabTokens throws IllegalArgumentException if amount is < 1")
    void grabTokens_notPresent() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 1);
        studentsPerColor.put(TokenColor.PINK_FAIRY, 2);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 1);
        studentsBag = new StudentsBag(studentsPerColor);
        assertAll(
                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> studentsBag.grabTokens(0), "Invalid amount of tokens: amount < 1")
        );

    }

    @Test
    @DisplayName("Test grabTokens throws IllegalArgumentException if amount is > tokens.size()")
    void grabTokens_notEnoughToken() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 1);
        studentsPerColor.put(TokenColor.PINK_FAIRY, 2);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 1);
        studentsBag = new StudentsBag(studentsPerColor);
        assertAll(
                () -> assertThrowsExactly(IllegalArgumentException.class,
                        () -> studentsBag.grabTokens(5), "Invalid amount of tokens: amount > tokens.size()")
        );

    }

}