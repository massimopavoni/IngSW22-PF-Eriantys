package it.polimi.ingsw.javangers.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing the client message handler.
 */
public class MessageHandler {
    /**
     * Message handler logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MessageHandler.class.getName());
    /**
     * Message handler singleton instance.
     */
    private static MessageHandler singleton = null;
    /**
     * Network manager singleton instance.
     */
    private final NetworkManager networkManager;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;

    /**
     * Constructor for message handler, initializing network manager and json mapper.
     *
     * @param networkManager network manager singleton instance
     */
    private MessageHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.jsonMapper = new ObjectMapper();
    }

    /**
     * Get message handler singleton instance.
     *
     * @param networkManager network manager singleton instance
     * @return singleton instance
     */
    public static MessageHandler getInstance(NetworkManager networkManager) {
        if (singleton == null)
            singleton = new MessageHandler(networkManager);
        return singleton;
    }

    /**
     * Compose message from json.
     *
     * @param messageType     type of the message
     * @param messageUsername username of the player sending the message
     * @param messageContent  message content json node
     * @return serialized message
     */
    private String composeJSONMessage(MessageType messageType, String messageUsername, JsonNode messageContent) {
        try {
            return this.jsonMapper.writeValueAsString(new Message(messageType, messageUsername, messageContent));
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "Logging exception:",
                    new MessageHandlerException(String.format("Error while serializing message (%s)", e.getMessage()), e));
            System.exit(1);
            return null;
        }
    }

    /**
     * Send directive message to server.
     *
     * @param type     type of the message
     * @param username username of the player sending the message
     * @param content  message content json node
     */
    public void sendOutgoingDirective(MessageType type, String username, JsonNode content) {
        this.networkManager.setOutgoingDirective(this.composeJSONMessage(type, username, content));
    }

    /**
     * Exception for errors within message handler class.
     */
    public static class MessageHandlerException extends Exception {
        /**
         * MessageHandlerException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public MessageHandlerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}