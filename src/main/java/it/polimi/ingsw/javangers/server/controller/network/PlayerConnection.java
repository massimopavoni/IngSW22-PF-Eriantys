package it.polimi.ingsw.javangers.server.controller.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class representing the player connection.
 */
public class PlayerConnection implements Runnable {
    /**
     * Generator of unique ID.
     */
    private static int idGenerator = 0;
    /**
     * Player socket.
     */
    private final Socket socket;
    /**
     * Lock to wait directives.
     */
    private final Object directiveLock;
    /**
     * ID of player connection.
     */
    public int id;
    /**
     * Scanner of arrive messages.
     */
    private Scanner in;
    /**
     * PrintWriter for exit messages.
     */
    private PrintWriter out;
    /**
     * String to save input directive.
     */
    private String inputDirective;
    /**
     * String to save output directive.
     */
    private String outputDirective;
    /**
     * Status of player connection.
     */
    private boolean alive;

    /**
     * Constructor for PlayerConnection that set the socket, alive = true, the directiveLock and increment idGenerator.
     *
     * @param socket player connection socket that ConnectionsPool accept
     */
    public PlayerConnection(Socket socket) {
        this.socket = socket;
        this.alive = true;
        this.directiveLock = new Object();
        id = idGenerator++;
    }

    /**
     * Start PlayerConnection that wait input directive and flush
     * the output directive that arrives.
     */
    @Override
    public void run() {

        try {
            System.out.println(id + " player connection started");
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            while (!this.socket.isClosed()) {

                inputDirective = in.nextLine();
                outputDirective = "";
                synchronized (this.directiveLock) {
                    this.directiveLock.wait();
                }
                inputDirective = "";
                out.println(outputDirective);
                out.flush();
            }
        } catch (IOException | NoSuchElementException e) {
            System.out.println(id + " player connection closed");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            in.close();
            out.close();
            this.alive = false;
        }

    }

    /**
     * Getter method for alive attribute.
     *
     * @return alive status
     */
    public boolean isAlive() {
        return this.alive;
    }

    /**
     * Setter method for outputDirective attribute.
     *
     * @param outputDirective value to set in the outputDirective
     */
    public void setOutputDirective(String outputDirective) {
        this.outputDirective = outputDirective;
        synchronized (this.directiveLock) {
            this.directiveLock.notify();
        }
    }

    /**
     * Getter method for inputDirective
     *
     * @return the value of inputDirective
     */
    public String getInputDirective() {
        return this.inputDirective;
    }
}
