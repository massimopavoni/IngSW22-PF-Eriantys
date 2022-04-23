package it.polimi.ingsw.javangers.server.controller.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the pool of connections.
 */
public class ConnectionsPool implements Runnable {

    /**
     * Attribute to instantiate ConnectionPool only once.
     */
    private static ConnectionsPool singleConnectionsPool = null;
    /**
     * Server socket to accept connection.
     */
    private final ServerSocket serverSocket;
    /**
     * List that contains the player connections.
     */
    private final List<PlayerConnection> playerConnectionsList;
    /**
     * Number of max connections.
     */
    private final int maxConnections;

    /**
     * Constructor for ConnectionsPool.
     *
     * @param port           number of port for server socket
     * @param maxConnections number of max connections
     * @throws IOException if an error occurred while creating the server socket
     */
    private ConnectionsPool(int port, int maxConnections) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.playerConnectionsList = new ArrayList<>();
        this.maxConnections = maxConnections;
    }

    /**
     * Get instance of private constructor of ConnectionsPool.
     *
     * @param port           number of port for server socket
     * @param maxConnections number of max connections
     * @return the new instance if is the first call, otherwise the ConnectionsPool already instantiated
     * @throws IOException if an error occurred while creating the server socket
     */
    public static ConnectionsPool getInstance(int port, int maxConnections) throws IOException {
        if (singleConnectionsPool == null) singleConnectionsPool = new ConnectionsPool(port, maxConnections);
        return singleConnectionsPool;
    }

    /**
     * Start ConnectionPool that accept new connections if number of players connections
     * is less of max connections.
     */
    @Override
    public void run() {
        try {
            while (!this.serverSocket.isClosed()) {
                getPlayerConnectionsList().removeIf(playerConnection -> !playerConnection.isAlive());
                if (this.playerConnectionsList.size() <= this.maxConnections) {
                    Socket socket = this.serverSocket.accept();
                    PlayerConnection playerConnection = new PlayerConnection(socket);
                    this.playerConnectionsList.add(playerConnection);
                    new Thread(playerConnection).start();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter method of playerConnectionsList.
     *
     * @return reference of playerConnectionsList
     */
    public List<PlayerConnection> getPlayerConnectionsList() {
        return this.playerConnectionsList;
    }

    /**
     * Close server socket connection.
     */
    public void closeServerSocket() {
        System.out.println("ConnectionPool closes connection");
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}