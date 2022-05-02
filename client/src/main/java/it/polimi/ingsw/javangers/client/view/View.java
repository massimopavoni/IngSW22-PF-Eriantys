package it.polimi.ingsw.javangers.client.view;

/**
 * Class representing a client view.
 */
public abstract class View {
    /**
     * Locking object for view update wait.
     */
    private final Object updateLock = new Object();
    /**
     * Chosen number of players for the game.
     */
    private int exactPlayersNumber;
    /**
     * Chosen expert mode flag.
     */
    private boolean expertMode;
    /**
     * Chosen player username.
     */
    private String username;
    /**
     * Chosen player wizard type.
     */
    private String wizardType;
    /**
     * Chosen player tower color
     */
    private String towerColor;

    /**
     * Method called to unlock the view update wait object.
     */
    public void unlockUpdate() {
        synchronized (this.updateLock) {
            this.updateLock.notifyAll();
        }
    }

    /**
     * Method called to update the view game information.
     */
    protected abstract void updateGame();
}
