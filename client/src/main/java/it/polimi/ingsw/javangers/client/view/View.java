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
