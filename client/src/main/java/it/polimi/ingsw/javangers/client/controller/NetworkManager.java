package it.polimi.ingsw.javangers.client.controller;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing network manager for client.
 */
public class NetworkManager implements Runnable {
    /**
     * Network manager logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NetworkManager.class.getName());
    /**
     * Exception message for logging.
     */
    private static final String EXCEPTION_MESSAGE = "Logging exception:";
    /**
     * Socket error message for logging.
     */
    private static final String SOCKET_ERROR_MESSAGE = "Socket error (%s)";
    /**
     * Buffered reader/writer error message for logging.
     */
    private static final String BUFFER_ERROR_MESSAGE = "Buffer read/write error (%s)";
    /**
     * Connections pool singleton instance.
     */
    private static NetworkManager singleton = null;
    /**
     * Client socket.
     */
    private final Socket socket;
    /**
     * Locking object for outgoing directives wait.
     */
    private final Object directiveWaitingLock;
    /**
     * Buffered reader for incoming directives.
     */
    private final BufferedReader in;
    /**
     * Buffered writer for outgoing directives.
     */
    private final BufferedWriter out;
    /**
     * Incoming directive string.
     */
    private volatile String incomingDirective;
    /**
     * Outgoing directive string.
     */
    private String outgoingDirective;

    /**
     * Constructor for network manager, initializing socket, buffered reader and writer and directive locking object
     *
     * @param serverAddress server socket address
     * @param port          server socket port
     * @throws NetworkManagerException if there was an error while creating socket or buffered reader/writer
     */
    private NetworkManager(String serverAddress, int port) throws NetworkManagerException {
        try {
            this.socket = new Socket(serverAddress, port);
        } catch (IOException e) {
            throw new NetworkManagerException(String.format(SOCKET_ERROR_MESSAGE, e.getMessage()), e);
        }
        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (IOException e) {
            throw new NetworkManagerException(String.format(BUFFER_ERROR_MESSAGE, e.getMessage()), e);
        }
        this.directiveWaitingLock = new Object();
    }

    /**
     * Get network manager singleton instance.
     *
     * @param serverAddress server socket address
     * @param port          server socket port
     * @return singleton instance
     * @throws NetworkManagerException if network manager constructor throws any exceptions
     */
    public static NetworkManager getInstance(String serverAddress, int port) throws NetworkManagerException {
        if (singleton == null)
            singleton = new NetworkManager(serverAddress, port);
        return singleton;
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
        synchronized (this.directiveWaitingLock) {
            this.outgoingDirective = outgoingDirective;
            this.directiveWaitingLock.notifyAll();
        }
    }

    /**
     * Function to run inside incoming directive thread.
     */
    private void incomingDirectiveThreadFunction() {
        try {
            while (this.socket.isConnected()) {
                this.incomingDirective = this.in.readLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new NetworkManagerException(String.format(BUFFER_ERROR_MESSAGE, e.getMessage()), e));
            Thread.currentThread().interrupt();
            System.exit(1);
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Function to run inside outgoing directive thread.
     */
    private void outgoingDirectiveThreadFunction() {
        try {
            while (this.socket.isConnected()) {
                synchronized (this.directiveWaitingLock) {
                    this.directiveWaitingLock.wait();
                }
                if (this.outgoingDirective != null) {
                    this.out.write(this.outgoingDirective);
                    this.out.newLine();
                    this.out.flush();
                    this.incomingDirective = null;
                    this.outgoingDirective = null;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new NetworkManagerException(String.format(BUFFER_ERROR_MESSAGE, e.getMessage()), e));
            Thread.currentThread().interrupt();
            System.exit(1);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new NetworkManagerException(String.format("Thread interrupted %s", e.getMessage()), e));
            Thread.currentThread().interrupt();
            System.exit(1);
        } finally {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Run network connection inside thread, reading incoming directives and writing outgoing ones.
     */
    @Override
    public void run() {
        new Thread(this::incomingDirectiveThreadFunction).start();
        new Thread(this::outgoingDirectiveThreadFunction).start();
    }

    /**
     * Exception for errors within network manager class.
     */
    public static class NetworkManagerException extends Exception {
        /**
         * NetworkManagerException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public NetworkManagerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}