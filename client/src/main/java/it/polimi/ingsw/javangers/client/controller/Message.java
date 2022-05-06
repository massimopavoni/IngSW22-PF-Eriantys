package it.polimi.ingsw.javangers.client.controller;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class representing a network message.
 *
 * @param type     type of the message
 * @param username username of the player sending the message
 * @param content  content of the message
 */
public record Message(MessageType type, String username, JsonNode content) {
}