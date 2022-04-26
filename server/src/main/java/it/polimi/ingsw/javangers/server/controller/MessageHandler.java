package it.polimi.ingsw.javangers.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.controller.network.ConnectionsPool;
import it.polimi.ingsw.javangers.server.controller.network.PlayerConnection;
import org.javatuples.Pair;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class representing the server message handler.
 */
public class MessageHandler implements Runnable {
    /**
     * Server launcher logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class.getName());
    /**
     * Message handler singleton instance.
     */
    private static MessageHandler singleton = null;
    /**
     * Alive message handler flag.
     */
    private static volatile boolean alive = true;
    /**
     * Connections pool singleton instance.
     */
    private final ConnectionsPool connectionsPool;
    /**
     * Polling interval in milliseconds.
     */
    private final int pollingInterval;
    /**
     * Locking object for polling wait.
     */
    private final Object pollingWaitingLock;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;
    /**
     * Model gate instance for single game.
     */
    private final ModelGate modelGate;

    /**
     * Constructor for message handler, initializing connections pool, polling interval, locking object, json mapper and model gate.
     *
     * @param connectionsPool         connections pool singleton instance
     * @param serverResourceLocations server resource locations map for model gate initialization
     * @param pollingInterval         polling interval in milliseconds
     */
    private MessageHandler(ConnectionsPool connectionsPool, Map<String, String> serverResourceLocations, int pollingInterval) {
        this.connectionsPool = connectionsPool;
        this.pollingInterval = pollingInterval;
        this.pollingWaitingLock = new Object();
        this.jsonMapper = new ObjectMapper();
        LOGGER.info("Creating model gate");
        this.modelGate = new ModelGate(serverResourceLocations.get("gameConfigurationsResourceLocation"),
                serverResourceLocations.get("gamePhasesResourceLocation"),
                serverResourceLocations.get("actionStrategyClassMappingsResourceLocation"),
                serverResourceLocations.get("effectStrategyClassMappingsResourceLocation"));
    }

    /**
     * Get message handler singleton instance.
     *
     * @param connectionsPool         connections pool singleton instance
     * @param serverResourceLocations server resource locations map for model gate initialization
     * @param pollingInterval         polling interval in milliseconds
     * @return singleton instance
     */
    public static MessageHandler getInstance(ConnectionsPool connectionsPool, Map<String, String> serverResourceLocations, int pollingInterval) {
        if (singleton == null)
            singleton = new MessageHandler(connectionsPool, serverResourceLocations, pollingInterval);
        return singleton;
    }

    /**
     * Close message handler.
     */
    public static void closeMessageHandler() {
        alive = false;
    }

    /**
     * Compose message from json.
     *
     * @param messageType    type of the message
     * @param messageContent message content json string
     * @return serialized message
     */
    private String composeJSONMessage(MessageType messageType, String messageContent) {
        try {
            return this.jsonMapper.writeValueAsString(new Message(messageType, this.jsonMapper.readTree(messageContent)));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Logging exception:",
                    new ModelGate.ModelGateException(String.format("Error while serializing message (%s)", e.getMessage()), e));
            return "Server message error";
        }
    }

    /**
     * Run message handler inside main server thread, managing directives.
     */
    @Override
    public void run() {
        LOGGER.info("Main infinite loop starting");
        List<PlayerConnection> playerConnectionsList;
        String incomingDirective;
        while (alive) {
            try {
                synchronized (this.pollingWaitingLock) {
                    this.pollingWaitingLock.wait(this.pollingInterval);
                }
            } catch (InterruptedException e) {
                LOGGER.severe("Thread interrupted");
                Thread.currentThread().interrupt();
                System.exit(1);
            }
            playerConnectionsList = this.connectionsPool.getAlivePlayerConnections();
            Collections.shuffle(playerConnectionsList);
            Set<Integer> playerConnectionsIDs = playerConnectionsList.stream().map(PlayerConnection::getID).collect(Collectors.toSet());
            List<Integer> allowedPlayerConnectionIDs = this.modelGate.getPlayerConnectionIDs();
            List<PlayerConnection> allowedPlayerConnections = playerConnectionsList.stream()
                    .filter(playerConnection -> allowedPlayerConnectionIDs
                            .contains(playerConnection.getID())).collect(Collectors.toList());
            if (!new HashSet<>(playerConnectionsIDs).containsAll(allowedPlayerConnectionIDs)) {
                LOGGER.severe("Aborting game because of missing player connections");
                this.modelGate.reset();
                for (PlayerConnection playerConnection : allowedPlayerConnections) {
                    String outgoingDirective = this.composeJSONMessage(MessageType.ERROR, "\"Game aborted because of missing players\"");
                    playerConnection.setOutgoingDirective(outgoingDirective);
                }
            }
            for (PlayerConnection playerConnection : playerConnectionsList) {
                incomingDirective = playerConnection.getIncomingDirective();
                if (incomingDirective != null) {
                    LOGGER.log(Level.INFO, "Receiving directive from player connection {0}", playerConnection.getID());
                    if (this.modelGate.isGameFull() && !allowedPlayerConnections.contains(playerConnection)) {
                        String outgoingDirective = this.composeJSONMessage(MessageType.ERROR, "\"Player connection not added to the game\"");
                        LOGGER.log(Level.INFO, "Sending directive to player connection {0}", playerConnection.getID());
                        playerConnection.setOutgoingDirective(outgoingDirective);
                    } else {
                        Pair<MessageType, String> outgoingDirectivePair = this.modelGate.executeDirective(playerConnection.getID(), incomingDirective);
                        String outgoingDirective = this.composeJSONMessage(outgoingDirectivePair.getValue0(), outgoingDirectivePair.getValue1());
                        if (outgoingDirectivePair.getValue0() == MessageType.START || outgoingDirectivePair.getValue0() == MessageType.ACTION) {
                            LOGGER.info("Sending directive to all player connections in the game");
                            allowedPlayerConnections.forEach(p -> p.setOutgoingDirective(outgoingDirective));
                        } else {
                            LOGGER.log(Level.INFO, "Sending directive to player connection {0}", playerConnection.getID());
                            playerConnection.setOutgoingDirective(outgoingDirective);
                        }
                        break;
                    }
                }
            }
        }
    }
}

