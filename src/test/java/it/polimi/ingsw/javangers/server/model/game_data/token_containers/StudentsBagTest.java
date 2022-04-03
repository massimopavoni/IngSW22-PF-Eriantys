package it.polimi.ingsw.javangers.server.model.game_data.token_containers;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StudentsBagTest {
    StudentsBag studentsBag;

    @Test
    @DisplayName("Test constructor with illegal number of student of color")
    void StudentsBag_illegalMapConstructor() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 5);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, -5);
        assertThrowsExactly(IllegalArgumentException.class, () -> new StudentsBag(studentsPerColor), "Invalid number of students per color");
    }

    @Test
    @DisplayName("Test correct constructor")
    void StudentsBag_correctConstructor() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 5);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 3);
        studentsBag = new StudentsBag(studentsPerColor);
        assertAll(
                () -> assertNotNull(studentsBag.getTokenContainer()),
                () -> assertEquals(new HashMap<TokenColor, Integer>() {{
                                       put(TokenColor.RED_DRAGON, 5);
                                       put(TokenColor.BLUE_UNICORN, 3);
                                   }},
                        studentsBag.getTokenContainer().getColorCounts())
        );
    }

    @Test
    @DisplayName("Test grabTokens for correct remaining tokens")
    void grabTokens_correctRemaining() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 2);
        studentsPerColor.put(TokenColor.PINK_FAIRY, 4);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 3);
        studentsBag = new StudentsBag(studentsPerColor);
        List<TokenColor> remainingTokens = studentsBag.getTokenContainer().getTokens();
        List<TokenColor> grabbedTokens = studentsBag.grabTokens(6);
        grabbedTokens.forEach(remainingTokens::remove);
        assertEquals(remainingTokens, studentsBag.getTokenContainer().getTokens());
    }

    @Test
    @DisplayName("Test grabTokens for grabbing all tokens if less than available are requested")
    void grabTokens_lessThanAvailable() {
        Map<TokenColor, Integer> studentsPerColor = new HashMap<>();
        studentsPerColor.put(TokenColor.RED_DRAGON, 1);
        studentsPerColor.put(TokenColor.PINK_FAIRY, 2);
        studentsPerColor.put(TokenColor.BLUE_UNICORN, 1);
        studentsBag = new StudentsBag(studentsPerColor);
        assertEquals(studentsPerColor, new TokenContainer() {{
            addTokens(studentsBag.grabTokens(7));
        }}.getColorCounts());
    }
}