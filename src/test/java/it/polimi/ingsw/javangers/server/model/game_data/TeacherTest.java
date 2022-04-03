package it.polimi.ingsw.javangers.server.model.game_data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    @Test
    @DisplayName("Test constructor")
    void Teacher_constructor() {
        Teacher teacher = new Teacher("",0);
        assertAll(
                () -> assertEquals("", teacher.getOwnerUsername()),
                () -> assertEquals(0, teacher.getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test setOwner for setting correct ownerUsername and ownerStudentsNumber")
    void setOwner_correct() {
        Teacher teacher = new Teacher("Pippo",2);
        teacher.setOwner("Pluto", 1);
        assertAll(
                () -> assertEquals("Pluto", teacher.getOwnerUsername()),
                () -> assertEquals(1, teacher.getOwnerStudentsNumber())
        );
    }
}