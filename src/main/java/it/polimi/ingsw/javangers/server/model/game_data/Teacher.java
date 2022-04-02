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
     * Students amount of current teacher owner.
     */
    private int ownerStudentsAmount;

    /**
     * Constructor for teacher, initializing owner username and students amount.
     *
     * @param ownerUsername       the owner of teacher
     * @param ownerStudentsAmount number of students owned
     */
    public Teacher(String ownerUsername, Integer ownerStudentsAmount) {
        this.ownerUsername = ownerUsername;
        this.ownerStudentsAmount = ownerStudentsAmount;
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
    public int getOwnerStudentsAmount() {
        return this.ownerStudentsAmount;
    }

    /**
     * Set teacher owner.
     *
     * @param ownerUsername       new owner username
     * @param ownerStudentsAmount new number of students owned
     */
    public void setOwner(String ownerUsername, int ownerStudentsAmount) {
        this.ownerUsername = ownerUsername;
        this.ownerStudentsAmount = ownerStudentsAmount;
    }
}