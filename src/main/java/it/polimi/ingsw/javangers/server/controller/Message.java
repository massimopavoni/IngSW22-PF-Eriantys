package it.polimi.ingsw.javangers.server.controller;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class representing a message.
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
     * Constructor of the message class, initializing type and content.
     *
     * @param type    type of the message
     * @param content content of the message
     */
    public Message(MessageType type, JsonNode content) {
        this.type = type;
        this.content = content;
    }

    /**
     * Getter of the type of the message.
     *
     * @return type of the message
     */
    public MessageType getType() {
        return this.type;
    }

    /**
     * Getter of the content of the message.
     *
     * @return content of the message
     */
    public JsonNode getContent() {
        return this.content;
    }


}
