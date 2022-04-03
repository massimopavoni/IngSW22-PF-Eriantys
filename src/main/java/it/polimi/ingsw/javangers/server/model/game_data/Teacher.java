package it.polimi.ingsw.javangers.server.model.game_data;

/**
 * Class representing a teacher.
 */
public class Teacher {
    /**
     * Username of current teacher owner.
     */
    private String ownerUsername;
    /**
     * Students number of current teacher owner.
     */
    private int ownerStudentsNumber;

    /**
     * Constructor for teacher, initializing owner username and students number.
     *
     * @param ownerUsername       the owner of teacher
     * @param ownerStudentsNumber number of students owned
     */
    public Teacher(String ownerUsername, Integer ownerStudentsNumber) {
        this.ownerUsername = ownerUsername;
        this.ownerStudentsNumber = ownerStudentsNumber;
    }

    /**
     * Get owner username.
     *
     * @return owner username
     */
    public String getOwnerUsername() {
        return this.ownerUsername;
    }

    /**
     * Get number of students owned.
     *
     * @return number of students owned
     */
    public int getOwnerStudentsNumber() {
        return this.ownerStudentsNumber;
    }

    /**
     * Set teacher owner.
     *
     * @param username       new owner username
     * @param studentsNumber new number of students owned
     */
    public void setOwner(String username, int studentsNumber) {
        this.ownerUsername = username;
        this.ownerStudentsNumber = studentsNumber;
    }
}