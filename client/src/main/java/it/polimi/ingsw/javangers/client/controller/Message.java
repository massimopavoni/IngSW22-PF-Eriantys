package it.polimi.ingsw.javangers.client.controller;

/**
 * Class representing a network message.
 */
public class Message {
    /**
     * Type of the message.
     */
    private final MessageType type;
    /**
     * Username of the player sending the message.
     */
    private final String username;
    /**
     * Content of the message.
     */
    private final String content;

    /**
     * Constructor for message, initializing message type, username and content.
     *
     * @param type     type of the message
     * @param username username of the player sending the message
     * @param content  content of the message
     */
    public Message(MessageType type, String username, String content) {
        this.type = type;
        this.username = username;
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
     * Get message username.
     *
     * @return message username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get message content.
     *
     * @return message content
     */
    public String getContent() {
        return this.content;
    }
}