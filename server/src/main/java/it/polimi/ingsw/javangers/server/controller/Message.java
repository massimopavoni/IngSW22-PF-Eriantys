package it.polimi.ingsw.javangers.server.controller;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Class representing a network message.
 *
 * @param type    type of the message
 * @param content content of the message
 */
public record Message(MessageType type, JsonNode content) {
}