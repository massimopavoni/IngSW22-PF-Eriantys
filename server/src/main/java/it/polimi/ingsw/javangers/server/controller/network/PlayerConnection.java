package it.polimi.ingsw.javangers.server.controller.network;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing a player connection.
 */
public class PlayerConnection implements Runnable {
    /**
     * Player connection logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PlayerConnection.class.getName());
    /**
     * Exception message for logging.
     */
    private static final String EXCEPTION_MESSAGE = "Player connection %d - logging:";
    /**
     * Buffered reader/writer error message for logging.
     */
    private static final String BUFFER_ERROR_MESSAGE = "Buffer read/write error (%s)";
    /**
     * Unique id generator.
     */
    private static int idGenerator = 0;
    /**
     * Player socket.
     */
    private final Socket playerSocket;
    /**
     * Player connection id.
     */
    private final int id;
    /**
     * Buffered reader for incoming directives.
     */
    private BufferedReader in;
    /**
     * Buffered writer for outgoing directives.
     */
    private BufferedWriter out;
    /**
     * Locking object for outgoing directives wait.
     */
    private final Object directiveWaitingLock;
    /**
     * Alive connection flag.
     */
    private volatile boolean alive;
    /**
     * Incoming directive string.
     */
    private volatile String incomingDirective;
    /**
     * Outgoing directive string.
     */
    private String outgoingDirective;

    /**
     * Constructor for player connection, initializing player socket, id, buffered reader and writer,
     * directive locking object, alive connection flag and incoming directive.
     *
     * @param socket player connection socket from connections pool
     */
    public PlayerConnection(Socket socket) {
        this.playerSocket = socket;
        this.id = idGenerator++;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.playerSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.playerSocket.getOutputStream()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, String.format(EXCEPTION_MESSAGE, this.id),
                    new PlayerConnectionException(String.format(BUFFER_ERROR_MESSAGE, e.getMessage()), e));
        }
        this.directiveWaitingLock = new Object();
        this.alive = true;
        this.incomingDirective = null;
    }

    /**
     * Get player connection id.
     *
     * @return id
     */
    public int getID() {
        return this.id;
    }

    /**
     * Get alive connection flag.
     *
     * @return alive connection flag
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Get incoming directive.
     *
     * @return incoming directive string
     */
    public String getIncomingDirective() {
        return this.incomingDirective;
    }

    /**
     * Set outgoing directive, unlocking the thread.
     *
     * @param outgoingDirective new outgoing directive
     */
    public void setOutgoingDirective(String outgoingDirective) {
        if (this.alive) {
            synchronized (this.directiveWaitingLock){
                this.outgoingDirective = outgoingDirective;
            }
        }
    }

    /**
     * Run player connection inside thread, reading incoming directives and writing outgoing ones.
     */
    @Override
    public void run() {
        LOGGER.log(Level.INFO, "Player connection {0} - running", this.id);
        new Thread(() -> {
            try {
                while (this.playerSocket.isConnected()) {
                    String input = this.in.readLine();
                    if (input.equals("exit")) {
                        LOGGER.log(Level.WARNING, "Player connection {0} - disconnected", this.id);
                        this.alive = false;
                        break;
                    }
                    this.incomingDirective = input;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, String.format(EXCEPTION_MESSAGE, this.id),
                        new PlayerConnectionException(String.format(BUFFER_ERROR_MESSAGE, e.getMessage()), e));
            } finally {
                Thread.currentThread().interrupt();
                this.alive = false;
            }
        }).start();
        new Thread(() -> {
            try {
                while (this.playerSocket.isConnected()) {
                    synchronized (this.directiveWaitingLock) {
                        if (this.outgoingDirective != null) {
                            this.out.write(this.outgoingDirective);
                            this.out.newLine();
                            this.out.flush();
                            this.incomingDirective = null;
                            this.outgoingDirective = null;
                        }
                    }
                }
                LOGGER.log(Level.WARNING, "Player connection {0} - disconnected", this.id);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, String.format(EXCEPTION_MESSAGE, this.id),
                        new PlayerConnectionException(String.format(BUFFER_ERROR_MESSAGE, e.getMessage()), e));
            } finally {
                Thread.currentThread().interrupt();
                this.alive = false;
            }
        }).start();
    }

    /**
     * Exception for errors within player connection class.
     */
    public static class PlayerConnectionException extends Exception {
        /**
         * PlayerConnectionException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public PlayerConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
