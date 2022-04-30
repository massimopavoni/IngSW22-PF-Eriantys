package it.polimi.ingsw.javangers.client.controller.directives;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.javangers.client.controller.MessageHandler;
import it.polimi.ingsw.javangers.client.controller.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Class representing the directives dispatcher.
 */
public class DirectivesDispatcher {
    /**
     * Resource location for directives types mappings json file.
     */
    private static final String DIRECTIVES_TYPES_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/directives_types_mappings.json";
    /**
     * Resource location for directives actions mappings json file.
     */
    private static final String DIRECTIVES_ACTIONS_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/directives_actions_mappings.json";
    /**
     * Resource location for directives actions effects json file.
     */
    private static final String DIRECTIVES_EFFECTS_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/directives_effects_mappings.json";
    /**
     * Directives dispatcher singleton instance.
     */
    private static DirectivesDispatcher singleton = null;
    /**
     * Map for directives types mappings.
     */
    private static Map<MessageType, Map<String, String>> directivesTypesMappings;
    /**
     * Map for directives actions mappings.
     */
    private static Map<String, Map<String, String>> directivesActionsMappings;
    /**
     * Map for directives effects mappings.
     */
    private static Map<String, Map<String, String>> directivesEffectsMappings;
    /**
     * Message handler singleton instance.
     */
    private final MessageHandler messageHandler;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;

    /**
     * Constructor for directives dispatcher, initializing message handler instance reference, json mapper
     * and directives types, actions and effects mappings.
     *
     * @param messageHandler message handler singleton instance
     * @throws DirectivesDispatcherException if there was an error while parsing directives types/actions/effects mappings
     */
    private DirectivesDispatcher(MessageHandler messageHandler) throws DirectivesDispatcherException {
        this.messageHandler = messageHandler;
        this.jsonMapper = new ObjectMapper();
        try {
            InputStream jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_TYPES_MAPPINGS_RESOURCE_LOCATION);
            directivesTypesMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_ACTIONS_MAPPINGS_RESOURCE_LOCATION);
            directivesActionsMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_EFFECTS_MAPPINGS_RESOURCE_LOCATION);
            directivesEffectsMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new DirectivesDispatcherException(
                    String.format("Error while parsing directives types/actions/effects mappings (%s)", e.getMessage()), e);
        }
    }

    /**
     * Get directives dispatcher singleton instance.
     *
     * @param messageHandler message handler singleton instance
     * @return directives dispatcher singleton instance
     * @throws DirectivesDispatcherException if there was an error while creating directives dispatcher
     */
    public static DirectivesDispatcher getInstance(MessageHandler messageHandler) throws DirectivesDispatcherException {
        if (singleton == null)
            singleton = new DirectivesDispatcher(messageHandler);
        return singleton;
    }

    /**
     * Dispatch create directive.
     *
     * @param username           player username
     * @param exactPlayersNumber exact number of players for the game
     * @param expertMode         game expert mode flag
     * @param wizardType         player wizard type
     * @param towerColor         player tower color
     */
    public void createGame(String username, int exactPlayersNumber, boolean expertMode, String wizardType, String towerColor) {
        ObjectNode contentJSON = this.jsonMapper.createObjectNode();
        Map<String, String> createMappings = directivesTypesMappings.get(MessageType.CREATE);
        contentJSON.put(createMappings.get("exactPlayersNumber"), exactPlayersNumber);
        contentJSON.put(createMappings.get("expertMode"), expertMode);
        ObjectNode firstPlayerInfoJSON = this.jsonMapper.createObjectNode();
        firstPlayerInfoJSON.put(createMappings.get("wizardType"), wizardType);
        firstPlayerInfoJSON.put(createMappings.get("towerColor"), towerColor);
        contentJSON.set(createMappings.get("firstPlayerInfo"), firstPlayerInfoJSON);
        this.messageHandler.sendOutgoingDirective(MessageType.CREATE, username, contentJSON.toString());
    }

    /**
     * Dispatch player directive.
     *
     * @param username   player username
     * @param wizardType player wizard type
     * @param towerColor player tower color
     */
    public void addPlayer(String username, String wizardType, String towerColor) {
        ObjectNode contentJSON = this.jsonMapper.createObjectNode();
        Map<String, String> addPlayerMappings = directivesTypesMappings.get(MessageType.PLAYER);
        contentJSON.put(addPlayerMappings.get("wizardType"), wizardType);
        contentJSON.put(addPlayerMappings.get("towerColor"), towerColor);
        this.messageHandler.sendOutgoingDirective(MessageType.PLAYER, username, contentJSON.toString());
    }

    /**
     * Dispatch start directive.
     *
     * @param username player username
     */
    public void startGame(String username) {
        ObjectNode contentJSON = this.jsonMapper.createObjectNode();
        this.messageHandler.sendOutgoingDirective(MessageType.START, username, contentJSON.toString());
    }

    /**
     * Dispatch action directive.
     *
     * @param username player username
     * @param action   action class
     * @param args     action arguments
     */
    private void actionDirective(String username, String action, ObjectNode args) {
        ObjectNode contentJSON = this.jsonMapper.createObjectNode();
        Map<String, String> actionMappings = directivesTypesMappings.get(MessageType.ACTION);
        contentJSON.put(actionMappings.get("action"), action);
        contentJSON.set(actionMappings.get("args"), args);
        this.messageHandler.sendOutgoingDirective(MessageType.ACTION, username, contentJSON.toString());
    }

    /**
     * Dispatch fill clouds directive.
     *
     * @param username player username
     */
    public void actionFillClouds(String username) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> fillCloudsMappings = directivesActionsMappings.get("fillClouds");
        this.actionDirective(username, fillCloudsMappings.get("action"), argsJSON);
    }

    /**
     * Dispatch play assistant card directive.
     *
     * @param username player username
     * @param cardName card name
     */
    public void actionPlayAssistantCard(String username, String cardName) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> playAssistantCardMappings = directivesActionsMappings.get("playAssistantCard");
        argsJSON.put(playAssistantCardMappings.get("cardName"), cardName);
        this.actionDirective(username, playAssistantCardMappings.get("action"), argsJSON);
    }

    /**
     * Dispatch move students directive.
     *
     * @param username          player username
     * @param studentsToHall    students to hall list
     * @param studentsToIslands students to islands map
     */
    public void actionMoveStudents(String username, List<String> studentsToHall, Map<Integer, List<String>> studentsToIslands) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> moveStudentsMappings = directivesActionsMappings.get("moveStudents");
        ArrayNode studentsToHallJSON = this.jsonMapper.createArrayNode();
        studentsToHall.forEach(studentsToHallJSON::add);
        argsJSON.set(moveStudentsMappings.get("studentsToHall"), studentsToHallJSON);
        ObjectNode studentsToIslandsJSON = this.jsonMapper.createObjectNode();
        studentsToIslands.forEach((islandIndex, students) -> {
            ArrayNode studentsJSON = this.jsonMapper.createArrayNode();
            students.forEach(studentsJSON::add);
            studentsToIslandsJSON.set(String.valueOf(islandIndex), studentsJSON);
        });
        argsJSON.set(moveStudentsMappings.get("studentsToIslands"), studentsToIslandsJSON);
        this.actionDirective(username, moveStudentsMappings.get("action"), argsJSON);
    }

    /**
     * Exception for errors within directives dispatcher class.
     */
    public static class DirectivesDispatcherException extends Exception {
        /**
         * DirectivesDispatcherException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public DirectivesDispatcherException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
