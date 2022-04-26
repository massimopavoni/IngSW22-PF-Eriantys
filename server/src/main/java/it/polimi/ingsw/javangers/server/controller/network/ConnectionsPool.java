package it.polimi.ingsw.javangers.server.controller.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing pool of connections.
 */
public class ConnectionsPool implements Runnable {
    /**
     * Connections pool logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ConnectionsPool.class.getName());
    /**
     * Exception message for logging.
     */
    private static final String EXCEPTION_MESSAGE = "Logging exception:";
    /**
     * Socket error message for logging.
     */
    private static final String SOCKET_ERROR_MESSAGE = "Socket error (%s)";
    /**
     * Connections pool singleton instance.
     */
    private static ConnectionsPool singleton = null;
    /**
     * Server socket for incoming connections.
     */
    private final ServerSocket serverSocket;
    /**
     * Number of maximum simultaneous connections.
     */
    private final int maxConnections;
    /**
     * List of player connections.
     */
    private final List<PlayerConnection> playerConnectionsList;

    /**
     * Constructor for connections pool, initializing server socket, max connections and player connections list.
     *
     * @param port           server socket port
     * @param maxConnections maximum simultaneous connections
     * @throws ConnectionsPoolException if an error occurs while creating server socket
     */
    private ConnectionsPool(int port, int maxConnections) throws ConnectionsPoolException {
        try {
            LOGGER.info("Creating connections pool server socket");
            this.serverSocket = new ServerSocket();
            this.serverSocket.setReuseAddress(true);
            this.serverSocket.bind(new InetSocketAddress(port));
            this.maxConnections = maxConnections;
            this.playerConnectionsList = new ArrayList<>();
        } catch (IOException e) {
            throw new ConnectionsPoolException(String.format(SOCKET_ERROR_MESSAGE, e.getMessage()), e);
        }
    }

    /**
     * Get connections pool singleton instance.
     *
     * @param port           server socket port
     * @param maxConnections maximum simultaneous connections
     * @return singleton instance
     * @throws ConnectionsPoolException if an error occurs while creating server socket
     */
    public static ConnectionsPool getInstance(int port, int maxConnections) throws ConnectionsPoolException {
        if (singleton == null)
            singleton = new ConnectionsPool(port, maxConnections);
        return singleton;
    }

    /**
     * Get shallow copy of player connections list, after removing dead connections.
     *
     * @return alive player connections list
     */
    public List<PlayerConnection> getAlivePlayerConnections() {
        synchronized (this.playerConnectionsList) {
            this.playerConnectionsList.removeIf(playerConnection -> !playerConnection.isAlive());
        }
        return new ArrayList<>(this.playerConnectionsList);
    }

    /**
     * Close server socket.
     */
    public void closeServerSocket() {
        try {
            LOGGER.info("Closing connections pool server socket");
            this.serverSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ConnectionsPoolException(String.format(SOCKET_ERROR_MESSAGE, e.getMessage()), e));
        }
    }

    /**
     * Run connections pool inside thread, accepting incoming connections and delegating each one to a player connection.
     */
    @Override
    public void run() {
        try {
            LOGGER.info("Connections pool running");
            while (!this.serverSocket.isClosed()) {
                if (this.playerConnectionsList.size() < this.maxConnections) {
                    Socket socket = this.serverSocket.accept();
                    PlayerConnection playerConnection = new PlayerConnection(socket);
                    new Thread(playerConnection).start();
                    synchronized (this.playerConnectionsList) {
                        this.playerConnectionsList.add(playerConnection);
                    }
                    LOGGER.log(Level.INFO, "Accepting new connection - {0}/{1} connections",
                            new Object[]{this.playerConnectionsList.size(), this.maxConnections});
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ConnectionsPoolException(String.format(SOCKET_ERROR_MESSAGE, e.getMessage()), e));
        }
    }

    /**
     * Exception for errors within connections pool class.
     */
    public static class ConnectionsPoolException extends Exception {
        /**
         * ConnectionsPoolException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public ConnectionsPoolException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}