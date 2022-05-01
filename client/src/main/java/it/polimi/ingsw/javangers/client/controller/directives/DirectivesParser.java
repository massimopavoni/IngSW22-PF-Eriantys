package it.polimi.ingsw.javangers.client.controller.directives;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.client.controller.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class DirectivesParser {
    private static final String GAME_JSON_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/game_json_mappings.json";
    private static DirectivesParser singleton = null;
    private static Map<String, String> gameJSONMappings;
    private static boolean newData;
    private static MessageType messageType;
    private static JsonNode messageContent;
    private final ObjectMapper jsonMapper;

    private DirectivesParser() throws DirectivesParserException {
        try {
            this.jsonMapper = new ObjectMapper();
            InputStream jsonInputStream = DirectivesParser.class.getResourceAsStream(GAME_JSON_MAPPINGS_RESOURCE_LOCATION);
            gameJSONMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new DirectivesParserException(String.format("Error while loading game json mappings (%s)", e.getMessage()), e);
        }
        newData = false;
    }

    public static DirectivesParser getInstance() throws DirectivesParserException {
        if (singleton == null) singleton = new DirectivesParser();
        return singleton;
    }

    public boolean isNewData() {
        return newData;
    }

    public static void setNewData(boolean newData) {
        DirectivesParser.newData = newData;
    }

    public String getContent() {
        return messageContent.isTextual() ? messageContent.textValue() : "";

    }

    public MessageType getType() {
        return messageType;
    }

    public void setMessage(String jsonDirective) throws DirectivesParserException {
        try {
            JsonNode directiveTree = this.jsonMapper.readTree(jsonDirective);
            String directiveType = directiveTree.get("type").asText();
            messageType = MessageType.valueOf(directiveType);
            messageContent = this.jsonMapper.readTree(directiveTree.get("content").toString());
            newData = true;
        } catch (JsonProcessingException e) {
            throw new DirectivesParserException((String.format("Error while deserializing directive (%s)", e.getMessage())), e);
        }
    }

    public String getCurrentPlayer() {
        return messageContent.at(gameJSONMappings.get("currentPlayer")).textValue();
    }

    public int getExactPlayersNumber() {
        return messageContent.at(gameJSONMappings.get("exactPlayerNumber")).intValue();
    }


    /**
     * Exception for errors within directives parser class.
     */
    public static class DirectivesParserException extends Exception {
        /**
         * DirectivesParserException constructor with message.
         *
         * @param message message to be shown
         */
        public DirectivesParserException(String message) {
            super(message);
        }

        /**
         * DirectivesParserException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public DirectivesParserException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
