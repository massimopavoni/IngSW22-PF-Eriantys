package it.polimi.ingsw.javangers.server.model.game_data;

/**
 * Class representing a Teachers.
 */
public class Teacher {
    private String ownerUsername;
    private int ownerStudentsAmount;

    /**
     * Constructor for Teacher, assign ownerUsername and ownerStudentsAmount.
     *
     * @param ownerUsername the owner of teacher
     * @param ownerStudentsAmount number of students owned
     */
    public Teacher(String ownerUsername, Integer ownerStudentsAmount) {
        this.ownerUsername = ownerUsername;
        this.ownerStudentsAmount = ownerStudentsAmount;
    }
    /**
     * Get owner Username.
     *
     * @return name of the owner
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
        return this.ownerStudentsAmount;}

    /**
     * Set Teacher Owner.
     *
     * @param ownerUsername
     * @param ownerStudentsAmount
     */
    public void setOwner(String ownerUsername, int ownerStudentsAmount) {
        this.ownerUsername = ownerUsername;
        this.ownerStudentsAmount = ownerStudentsAmount;
    }
}