package it.polimi.ingsw.javangers.client.controller;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class representing a network message.
 */
public class Message {
    /**
     * Type of the message.
     */
    private final MessageType type;
    /**
     * Content of the message.
     */
    private final JsonNode content;

    /**
     * Constructor for message, initializing type and content.
     *
     * @param type    type of the message
     * @param content content of the message
     */
    public Message(MessageType type, JsonNode content) {
        this.type = type;
        this.content = content;
    }

    /**
     * Get message type.
     *
     * @return message type
     */
    public MessageType getType() {
        return this.type;
    }

    /**
     * Get message content.
     *
     * @return message content
     */
    public JsonNode getContent() {
        return this.content;
    }


}
