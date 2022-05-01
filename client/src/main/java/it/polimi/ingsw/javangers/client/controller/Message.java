package it.polimi.ingsw.javangers.client.controller;

/**
 * Class representing a network message.
 *
 * @param type     Type of the message.
 * @param username Username of the player sending the message.
 * @param content  Content of the message.
 */
public record Message(MessageType type, String username, String content) {
}