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
 * Class representing the directives' dispatcher.
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
     * Constant for action json key.
     */
    private static final String ACTION_KEY = "action";
    /**
     * Constant for effect json key.
     */
    private static final String EFFECT_KEY = "effect";
    /**
     * Constant for island index json key.
     */
    private static final String ISLAND_INDEX_KEY = "islandIndex";
    /**
     * Directives dispatcher singleton instance.
     */
    private static DirectivesDispatcher singleton = null;
    /**
     * Map for directives types mappings.
     */
    private final Map<MessageType, Map<String, String>> directivesTypesMappings;
    /**
     * Map for directives actions mappings.
     */
    private final Map<String, Map<String, String>> directivesActionsMappings;
    /**
     * Map for directives effects mappings.
     */
    private final Map<String, Map<String, String>> directivesEffectsMappings;
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
            this.directivesTypesMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_ACTIONS_MAPPINGS_RESOURCE_LOCATION);
            this.directivesActionsMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesDispatcher.class.getResourceAsStream(DIRECTIVES_EFFECTS_MAPPINGS_RESOURCE_LOCATION);
            this.directivesEffectsMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
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
        Map<String, String> createMappings = this.directivesTypesMappings.get(MessageType.CREATE);
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
        Map<String, String> addPlayerMappings = this.directivesTypesMappings.get(MessageType.PLAYER);
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
        Map<String, String> actionMappings = this.directivesTypesMappings.get(MessageType.ACTION);
        contentJSON.put(actionMappings.get(ACTION_KEY), action);
        contentJSON.set(actionMappings.get("args"), args);
        this.messageHandler.sendOutgoingDirective(MessageType.ACTION, username, contentJSON.toString());
    }

    /**
     * Dispatch fill clouds action directive.
     *
     * @param username player username
     */
    public void actionFillClouds(String username) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> fillCloudsMappings = this.directivesActionsMappings.get("fillClouds");
        this.actionDirective(username, fillCloudsMappings.get(ACTION_KEY), argsJSON);
    }

    /**
     * Dispatch play assistant card action directive.
     *
     * @param username player username
     * @param cardName card name
     */
    public void actionPlayAssistantCard(String username, String cardName) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> playAssistantCardMappings = this.directivesActionsMappings.get("playAssistantCard");
        argsJSON.put(playAssistantCardMappings.get("cardName"), cardName);
        this.actionDirective(username, playAssistantCardMappings.get(ACTION_KEY), argsJSON);
    }

    /**
     * Dispatch move students action directive.
     *
     * @param username          player username
     * @param studentsToHall    students to hall list
     * @param studentsToIslands students to islands map
     */
    public void actionMoveStudents(String username, List<String> studentsToHall, Map<Integer, List<String>> studentsToIslands) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> moveStudentsMappings = this.directivesActionsMappings.get("moveStudents");
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
        this.actionDirective(username, moveStudentsMappings.get(ACTION_KEY), argsJSON);
    }

    /**
     * Dispatch move mother nature action directive.
     *
     * @param username player username
     * @param steps    number of steps
     */
    public void actionMoveMotherNature(String username, int steps) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> moveMotherNatureMappings = this.directivesActionsMappings.get("moveMotherNature");
        argsJSON.put(moveMotherNatureMappings.get("steps"), steps);
        this.actionDirective(username, moveMotherNatureMappings.get(ACTION_KEY), argsJSON);
    }

    /**
     * Dispatch choose cloud action directive.
     *
     * @param username   player username
     * @param cloudIndex cloud index
     */
    public void actionChooseCloud(String username, int cloudIndex) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> chooseCloudMappings = this.directivesActionsMappings.get("chooseCloud");
        argsJSON.put(chooseCloudMappings.get("cloudIndex"), cloudIndex);
        this.actionDirective(username, chooseCloudMappings.get(ACTION_KEY), argsJSON);
    }

    /**
     * Dispatch activate character card action directive.
     *
     * @param username player username
     * @param effect   card effect
     * @param args     card args
     */
    private void actionActivateCharacterCard(String username, String effect, ObjectNode args) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> activateCharacterCardMappings = this.directivesActionsMappings.get("activateCharacterCard");
        argsJSON.put(activateCharacterCardMappings.get(EFFECT_KEY), effect);
        argsJSON.set(activateCharacterCardMappings.get("args"), args);
        this.actionDirective(username, activateCharacterCardMappings.get(ACTION_KEY), argsJSON);
    }

    /**
     * Dispatch monk character card directive.
     *
     * @param username       player username
     * @param tokensToIsland list of tokens to island
     * @param islandIndex    island index
     */
    public void activateMonk(String username, List<String> tokensToIsland, int islandIndex) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> monkMappings = this.directivesEffectsMappings.get("monk");
        ArrayNode tokensToIslandJSON = this.jsonMapper.createArrayNode();
        tokensToIsland.forEach(tokensToIslandJSON::add);
        argsJSON.set(monkMappings.get("tokensToIsland"), tokensToIslandJSON);
        argsJSON.put(monkMappings.get(ISLAND_INDEX_KEY), islandIndex);
        this.actionActivateCharacterCard(username, monkMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch innkeeper character card directive.
     *
     * @param username player username
     */
    public void activateInnkeeper(String username) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> innkeeperMappings = this.directivesEffectsMappings.get("innkeeper");
        this.actionActivateCharacterCard(username, innkeeperMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch herald character card directive.
     *
     * @param username    player username
     * @param islandIndex island index
     */
    public void activateHerald(String username, int islandIndex) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> heraldMappings = this.directivesEffectsMappings.get("herald");
        argsJSON.put(heraldMappings.get(ISLAND_INDEX_KEY), islandIndex);
        this.actionActivateCharacterCard(username, heraldMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch mailman character card directive.
     *
     * @param username player username
     */
    public void activateMailman(String username) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> mailmanMappings = this.directivesEffectsMappings.get("mailman");
        this.actionActivateCharacterCard(username, mailmanMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch herbalist character card directive.
     *
     * @param username    player username
     * @param islandIndex island index
     */
    public void activateHerbalist(String username, int islandIndex) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> herbalistMappings = this.directivesEffectsMappings.get("herbalist");
        argsJSON.put(herbalistMappings.get(ISLAND_INDEX_KEY), islandIndex);
        this.actionActivateCharacterCard(username, herbalistMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch centaur character card directive.
     *
     * @param username player username
     */
    public void activateCentaur(String username) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> centaurMappings = this.directivesEffectsMappings.get("centaur");
        this.actionActivateCharacterCard(username, centaurMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch jester character card directive.
     *
     * @param username           player username
     * @param tokensFromEntrance list of tokens from entrance
     * @param tokensToEntrance   list of tokens to entrance
     */
    public void activateJester(String username, List<String> tokensFromEntrance, List<String> tokensToEntrance) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> jesterMappings = this.directivesEffectsMappings.get("jester");
        ArrayNode tokensFromEntranceJSON = this.jsonMapper.createArrayNode();
        tokensFromEntrance.forEach(tokensFromEntranceJSON::add);
        argsJSON.set(jesterMappings.get("tokensFromEntrance"), tokensFromEntranceJSON);
        ArrayNode tokensToEntranceJSON = this.jsonMapper.createArrayNode();
        tokensToEntrance.forEach(tokensToEntranceJSON::add);
        argsJSON.set(jesterMappings.get("tokensToEntrance"), tokensToEntranceJSON);
        this.actionActivateCharacterCard(username, jesterMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch knight character card directive.
     *
     * @param username player username
     */
    public void activateKnight(String username) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> knightMappings = this.directivesEffectsMappings.get("knight");
        this.actionActivateCharacterCard(username, knightMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch mushroomer character card directive.
     *
     * @param username       player username
     * @param forbiddenColor forbidden color
     */
    public void activateMushroomer(String username, String forbiddenColor) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> mushroomerMappings = this.directivesEffectsMappings.get("mushroomer");
        argsJSON.put(mushroomerMappings.get("forbiddenColor"), forbiddenColor);
        this.actionActivateCharacterCard(username, mushroomerMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch bard character card directive.
     *
     * @param username           player username
     * @param tokensFromHall     list of tokens from hall
     * @param tokensFromEntrance list of tokens from entrance
     */
    public void activateBard(String username, List<String> tokensFromHall, List<String> tokensFromEntrance) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> bardMappings = this.directivesEffectsMappings.get("bard");
        ArrayNode tokensFromHallJSON = this.jsonMapper.createArrayNode();
        tokensFromHall.forEach(tokensFromHallJSON::add);
        argsJSON.set(bardMappings.get("tokensFromHall"), tokensFromHallJSON);
        ArrayNode tokensFromEntranceJSON = this.jsonMapper.createArrayNode();
        tokensFromEntrance.forEach(tokensFromEntranceJSON::add);
        argsJSON.set(bardMappings.get("tokensFromEntrance"), tokensFromEntranceJSON);
        this.actionActivateCharacterCard(username, bardMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch queen character card directive.
     *
     * @param username     player username
     * @param tokensToHall list of tokens to hall
     */
    public void activateQueen(String username, List<String> tokensToHall) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> queenMappings = this.directivesEffectsMappings.get("queen");
        ArrayNode tokensToHallJSON = this.jsonMapper.createArrayNode();
        tokensToHall.forEach(tokensToHallJSON::add);
        argsJSON.set(queenMappings.get("tokensToHall"), tokensToHallJSON);
        this.actionActivateCharacterCard(username, queenMappings.get(EFFECT_KEY), argsJSON);
    }

    /**
     * Dispatch scoundrel character card directive.
     *
     * @param username   player username
     * @param tokenColor token color
     */
    public void activateScoundrel(String username, String tokenColor) {
        ObjectNode argsJSON = this.jsonMapper.createObjectNode();
        Map<String, String> scoundrelMappings = this.directivesEffectsMappings.get("scoundrel");
        argsJSON.put(scoundrelMappings.get("tokenColor"), tokenColor);
        this.actionActivateCharacterCard(username, scoundrelMappings.get(EFFECT_KEY), argsJSON);
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
