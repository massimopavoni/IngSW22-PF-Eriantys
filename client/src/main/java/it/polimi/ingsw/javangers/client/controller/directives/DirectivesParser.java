package it.polimi.ingsw.javangers.client.controller.directives;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.client.controller.MessageType;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the directives' parser.
 */
public class DirectivesParser {
    /**
     * Message for json exploration errors.
     */
    private static final String GAME_JSON_EXPLORATION_ERROR = "Error while exploring game json (%s)";
    /**
     * Resource location for game json mappings file.
     */
    private static final String GAME_JSON_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/game_json_mappings.json";
    /**
     * Resource location for game macro phases mappings file.
     */
    private static final String GAME_MACRO_PHASES_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/game_macro_phases_mappings.json";
    /**
     * Resource location for game phases mappings file.
     */
    private static final String GAME_PHASES_MAPPINGS_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/controller/directives/game_phases_mappings.json";
    /**
     * Directives parser singleton instance.
     */
    private static DirectivesParser singleton = null;
    /**
     * Map of game json mappings.
     */
    private final Map<String, String> gameJSONMappings;
    /**
     * Map of game macro phases mappings.
     */
    private final Map<String, String> gameMacroPhasesMappings;
    /**
     * Map of game phases mappings.
     */
    private final Map<String, String> gamePhasesMappings;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;
    /**
     * Client chosen view.
     */
    private View view;
    /**
     * Received message type.
     */
    private MessageType messageType;
    /**
     * Received message content.
     */
    private JsonNode messageContent;

    /**
     * Constructor for directives parser, initializing json mapper and game json, macro phases and phases mappings.
     *
     * @throws DirectivesParserException if there was an error while parsing game json/macro phases/phase mappings
     */
    private DirectivesParser() throws DirectivesParserException {
        try {
            this.jsonMapper = new ObjectMapper();
            InputStream jsonInputStream = DirectivesParser.class.getResourceAsStream(GAME_JSON_MAPPINGS_RESOURCE_LOCATION);
            this.gameJSONMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesParser.class.getResourceAsStream(GAME_MACRO_PHASES_MAPPINGS_RESOURCE_LOCATION);
            this.gameMacroPhasesMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
            jsonInputStream = DirectivesParser.class.getResourceAsStream(GAME_PHASES_MAPPINGS_RESOURCE_LOCATION);
            this.gamePhasesMappings = this.jsonMapper.readValue(jsonInputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new DirectivesParserException(String.format("Error while loading json mappings (%s)", e.getMessage()), e);
        }
    }

    /**
     * Get directives parser singleton instance.
     *
     * @return directives parser singleton instance
     * @throws DirectivesParserException if there was an error while creating directives parser
     */
    public static DirectivesParser getInstance() throws DirectivesParserException {
        if (singleton == null) singleton = new DirectivesParser();
        return singleton;
    }

    /**
     * Get received message type.
     *
     * @return received message type
     */
    public MessageType getMessageType() {
        return this.messageType;
    }

    /**
     * Get received message content.
     *
     * @return received message content
     */
    public String getMessageContent() {
        return this.messageContent.isTextual() ? this.messageContent.textValue() : "";
    }

    /**
     * Set message type and content from received directive.
     *
     * @param jsonDirective received directive json
     * @throws DirectivesParserException if there was an error while deserializing received directive
     */
    public void setMessage(String jsonDirective) throws DirectivesParserException {
        try {
            JsonNode directiveTree = this.jsonMapper.readTree(jsonDirective);
            String directiveType = directiveTree.get("type").asText();
            this.messageType = MessageType.valueOf(directiveType);
            this.messageContent = this.jsonMapper.readTree(directiveTree.get("content").toString());
            this.view.unlockUpdate();
        } catch (JsonProcessingException e) {
            throw new DirectivesParserException((String.format("Error while deserializing directive (%s)", e.getMessage())), e);
        }
    }

    /**
     * Set client chosen view.
     *
     * @param view client chosen view
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Get integer node from content json.
     *
     * @param jsonPath path to integer node
     * @return integer value
     */
    private int getJSONInteger(String jsonPath) {
        return this.messageContent.at(jsonPath).intValue();
    }

    /**
     * Get exact players number.
     *
     * @return exact players number
     */
    public int getExactPlayersNumber() {
        return this.getJSONInteger(this.gameJSONMappings.get("exactPlayersNumber"));
    }

    /**
     * Get students per cloud.
     *
     * @return students per cloud
     */
    public int getStudentsPerCloud() {
        return this.getJSONInteger(this.gameJSONMappings.get("studentsPerCloud"));
    }

    /**
     * Get mother nature position in archipelago.
     *
     * @return mother nature position
     */
    public int getMotherNaturePosition() {
        return this.getJSONInteger(this.gameJSONMappings.get("motherNaturePosition"));
    }

    /**
     * Get a specific island's enabled counter.
     *
     * @param islandIndex island index
     * @return enabled counter
     */
    public int getIslandEnabled(int islandIndex) {
        return this.getJSONInteger(String.format(this.gameJSONMappings.get("islandEnabled"), islandIndex));
    }

    /**
     * Get a specific dashboard's coins number.
     *
     * @param username player username
     * @return dashboard coins number
     */
    public int getDashboardCoins(String username) {
        return this.getJSONInteger(String.format(this.gameJSONMappings.get("dashboardCoinsNumber"), username));
    }

    /**
     * Get additional mother nature steps.
     *
     * @return additional mother nature steps
     */
    public int getAdditionalMotherNatureSteps() {
        return this.getJSONInteger(this.gameJSONMappings.get("additionalMotherNatureSteps"));
    }

    /**
     * Get additional power on island.
     *
     * @return additional power
     */
    public int getAdditionalPower() {
        return this.getJSONInteger(this.gameJSONMappings.get("additionalPower"));
    }

    /**
     * Get a specific character card's multipurpose counter.
     *
     * @param cardName card name
     * @return multipurpose counter
     */
    public int getCharacterCardMultipurposeCounter(String cardName) {
        return this.getJSONInteger(String.format(this.gameJSONMappings.get("characterCardMultipurposeCounter"), cardName));
    }

    /**
     * Get boolean node from content json.
     *
     * @param key key to boolean node
     * @return boolean value
     */
    private boolean isJSONBoolean(String key) {
        return this.messageContent.at(this.gameJSONMappings.get(key)).booleanValue();
    }

    /**
     * Get expert mode flag.
     *
     * @return expert mode flag
     */
    public boolean isExpertMode() {
        return this.isJSONBoolean("expertMode");
    }

    /**
     * Get teachers equal count flag.
     *
     * @return teachers equal count flag
     */
    public boolean isTeachersEqualCount() {
        return this.isJSONBoolean("teachersEqualCount");
    }

    /**
     * Get enabled island towers flag.
     *
     * @return enabled island towers flag
     */
    public boolean isEnabledIslandTowers() {
        return this.isJSONBoolean("enabledIslandTowers");
    }

    /**
     * Get array of strings node from content json.
     *
     * @param key key to array of strings node
     * @return list of strings
     * @throws DirectivesParserException if there was an error while exploring the json
     */
    private List<String> getJSONList(String key) throws DirectivesParserException {
        try {
            return this.jsonMapper.readValue(this.messageContent.at(this.gameJSONMappings.get(key)).toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new DirectivesParserException((String.format(GAME_JSON_EXPLORATION_ERROR, e.getMessage())), e);
        }
    }

    /**
     * Get players order list.
     *
     * @return players order list
     * @throws DirectivesParserException if there was an error while retrieving the list
     */
    public List<String> getPlayersOrder() throws DirectivesParserException {
        return this.getJSONList("playersOrder");
    }

    /**
     * Get winners list.
     *
     * @return winners list
     * @throws DirectivesParserException if there was an error while retrieving the list
     */
    public List<String> getWinners() throws DirectivesParserException {
        return this.getJSONList("winners");
    }

    /**
     * Get string node from content json.
     *
     * @param jsonPath path to string node
     * @return string value
     */
    private String getJSONString(String jsonPath) {
        return this.messageContent.at(jsonPath).textValue();
    }

    /**
     * Get current player username.
     *
     * @return current player username
     */
    public String getCurrentPlayer() {
        return this.getJSONString(this.gameJSONMappings.get("currentPlayer"));
    }

    /**
     * Get endgame type.
     *
     * @return endgame type
     */
    public String getEndGame() {
        return this.getJSONString(this.gameJSONMappings.get("endgame"));
    }

    /**
     * Get a specific dashboard's cards back.
     *
     * @param username player username
     * @return cards back
     */
    public String getDashboardCardsBack(String username) {
        return this.getJSONString(String.format(this.gameJSONMappings.get("dashboardCardsBack"), username));
    }

    /**
     * Get forbidden color on island.
     *
     * @return forbidden color
     */
    public String getForbiddenColor() {
        return this.getJSONString(this.gameJSONMappings.get("forbiddenColor"));
    }

    /**
     * Get size of array node from content json.
     *
     * @param key key to array node
     * @return size of array node
     */
    private int getJSONArraySize(String key) {
        return this.messageContent.at(this.gameJSONMappings.get(key)).size();
    }

    /**
     * Get islands size.
     *
     * @return islands size
     */
    public int getIslandsSize() {
        return this.getJSONArraySize("islands");
    }

    /**
     * Get clouds size.
     *
     * @return clouds size
     */
    public int getCloudsSize() {
        return this.getJSONArraySize("clouds");
    }

    /**
     * Get map of string and integer node from content json.
     *
     * @param jsonPath path to object node
     * @return map of string and integer
     * @throws DirectivesParserException if there was an error while exploring the json
     */
    private Map<String, Integer> getJSONMap(String jsonPath) throws DirectivesParserException {
        try {
            return this.jsonMapper.readValue(this.messageContent.at(jsonPath).toString(),
                    new TypeReference<LinkedHashMap<String, Integer>>() {
                    });
        } catch (JsonProcessingException e) {
            throw new DirectivesParserException((String.format(GAME_JSON_EXPLORATION_ERROR, e.getMessage())), e);
        }
    }

    /**
     * Get a specific island's tokens.
     *
     * @param islandIndex island index
     * @return islands tokens map
     * @throws DirectivesParserException if there was an error while retrieving the map
     */
    public Map<String, Integer> getIslandTokens(int islandIndex) throws DirectivesParserException {
        return this.getJSONMap(String.format(this.gameJSONMappings.get("islandTokens"), islandIndex));
    }

    /**
     * Get students bag tokens.
     *
     * @return students bag tokens map
     * @throws DirectivesParserException if there was an error while retrieving the map
     */
    public Map<String, Integer> getStudentsBagTokens() throws DirectivesParserException {
        return this.getJSONMap(this.gameJSONMappings.get("studentsBagTokens"));
    }

    /**
     * Get a specific cloud's tokens.
     *
     * @param cloudIndex cloud index
     * @return clouds tokens map
     * @throws DirectivesParserException if there was an error while retrieving the map
     */
    public Map<String, Integer> getCloudTokens(int cloudIndex) throws DirectivesParserException {
        return this.getJSONMap(String.format(this.gameJSONMappings.get("cloudTokens"), cloudIndex));
    }

    /**
     * Get a specific dashboard's entrance tokens.
     *
     * @param username player username
     * @return dashboard entrance tokens map
     * @throws DirectivesParserException if there was an error while retrieving the map
     */
    public Map<String, Integer> getDashboardEntranceTokens(String username) throws DirectivesParserException {
        return this.getJSONMap(String.format(this.gameJSONMappings.get("dashboardEntranceTokens"), username));
    }

    /**
     * Get a specific dashboard's hall tokens.
     *
     * @param username player username
     * @return dashboard hall tokens map
     * @throws DirectivesParserException if there was an error while retrieving the map
     */
    public Map<String, Integer> getDashboardHallTokens(String username) throws DirectivesParserException {
        return this.getJSONMap(String.format(this.gameJSONMappings.get("dashboardHallTokens"), username));
    }

    /**
     * Get a specific character card's tokens.
     *
     * @param cardName card name
     * @return character card tokens map
     * @throws DirectivesParserException if there was an error while retrieving the map
     */
    public Map<String, Integer> getCharacterCardTokens(String cardName) throws DirectivesParserException {
        return this.getJSONMap(String.format(this.gameJSONMappings.get("characterCardTokens"), cardName));
    }

    /**
     * Get list of field names of a node from content json.
     *
     * @param key key to object node
     * @return list of field names
     */
    private List<String> getJSONFieldNames(String key) {
        List<String> fieldNames = new ArrayList<>();
        this.messageContent.at(this.gameJSONMappings.get(key)).fieldNames().forEachRemaining(fieldNames::add);
        return fieldNames;
    }

    /**
     * Get dashboard names.
     *
     * @return dashboard names
     */
    public List<String> getDashboardNames() {
        return this.getJSONFieldNames("dashboards");
    }

    /**
     * Get character cards names.
     *
     * @return character cards names
     */
    public List<String> getCharacterCardNames() {
        return this.getJSONFieldNames("characterCards");
    }

    /**
     * Get a pair of string and integer from content json.
     *
     * @param jsonPath path to object node
     * @return pair of string and integer
     */
    private Pair<String, Integer> getJSONPair(String jsonPath) {
        JsonNode pair = this.messageContent.at(jsonPath);
        return new Pair<>(pair.get("value0").textValue(), pair.get("value1").intValue());
    }

    /**
     * Get a specific island's towers.
     *
     * @param islandIndex island index
     * @return island towers pair
     */
    public Pair<String, Integer> getIslandTowers(int islandIndex) {
        return this.getJSONPair(String.format(this.gameJSONMappings.get("islandTowers"), islandIndex));
    }

    /**
     * Get a specific dashboard's towers.
     *
     * @param username player username
     * @return dashboard towers pair
     */
    public Pair<String, Integer> getDashboardTowers(String username) {
        return this.getJSONPair(String.format(this.gameJSONMappings.get("dashboardTowers"), username));
    }

    /**
     * Get map of string and pairs of string and integer node from content json.
     *
     * @param jsonPath path to object node
     * @return map of string and pairs of string and integer
     */
    private Map<String, Pair<Integer, Integer>> getJSONMapPair(String jsonPath) {
        Map<String, Pair<Integer, Integer>> mapPair = new LinkedHashMap<>();
        this.messageContent.at(jsonPath).fieldNames().forEachRemaining(key -> {
            JsonNode pair = this.messageContent.at(jsonPath).get(key);
            mapPair.put(key, new Pair<>(pair.get("value").intValue(), pair.get("steps").intValue()));
        });
        return mapPair;
    }

    /**
     * Get a specific dashboard's assistant cards.
     *
     * @param username player username
     * @return dashboard assistant cards map
     */
    public Map<String, Pair<Integer, Integer>> getDashboardAssistantCards(String username) {
        return this.getJSONMapPair(String.format(this.gameJSONMappings.get("dashboardAssistantCards"), username));
    }

    /**
     * Get a specific dashboard's discarded assistant cards.
     *
     * @param username player username
     * @return dashboard discarded assistant cards map
     */
    public Map<String, Pair<Integer, Integer>> getDashboardDiscardedAssistantCards(String username) {
        return this.getJSONMapPair(String.format(this.gameJSONMappings.get("dashboardDiscardedAssistantCards"), username));
    }

    /**
     * Get list of available actions during the current phase.
     *
     * @return list of available actions
     * @throws DirectivesParserException if there was an error while exploring the json
     */
    public List<String> getAvailableActions() throws DirectivesParserException {
        try {
            String[] currentPhase = this.messageContent.at(this.gameJSONMappings.get("currentPhase")).textValue().split(":");
            return this.jsonMapper.readValue(this.messageContent.at(String.format(this.gameJSONMappings.get("availableActions"),
                    currentPhase[0], currentPhase[1])).toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new DirectivesParserException(String.format(GAME_JSON_EXPLORATION_ERROR, e.getMessage()), e);
        }
    }

    /**
     * Get current phase's pair of strings.
     *
     * @return current phase pair
     */
    public Pair<String, String> getCurrentPhase() {
        String currentPhase = this.messageContent.at(this.gameJSONMappings.get("currentPhase")).textValue();
        return new Pair<>(this.gameMacroPhasesMappings.get(currentPhase.split(":")[0]), this.gamePhasesMappings.get(currentPhase));
    }

    /**
     * Get teachers map with owners usernames.
     *
     * @return teachers map
     * @throws DirectivesParserException if there was an error while exploring the json
     */
    public Map<String, String> getTeachers() throws DirectivesParserException {
        try {
            Map<String, Map<String, String>> teachers = this.jsonMapper.readValue(
                    this.messageContent.at(this.gameJSONMappings.get("teachers")).toString(),
                    new TypeReference<LinkedHashMap<String, Map<String, String>>>() {
                    });
            return teachers.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get("ownerUsername")));
        } catch (JsonProcessingException e) {
            throw new DirectivesParserException((String.format(GAME_JSON_EXPLORATION_ERROR, e.getMessage())), e);
        }
    }

    /**
     * Get a specific dashboard's last discarded assistant card.
     *
     * @param username player username
     * @return dashboard last discarded assistant card
     */
    public Map.Entry<String, Pair<Integer, Integer>> getDashboardLastDiscardedAssistantCard(String username) {
        JsonNode entry = this.messageContent.at(
                String.format(this.gameJSONMappings.get("dashboardLastDiscardedAssistantCard"), username));
        Iterator<String> fieldNames = entry.fieldNames();
        if (!fieldNames.hasNext()) {
            return null;
        } else {
            String key = fieldNames.next();
            return new AbstractMap.SimpleEntry<>(key,
                    new Pair<>(entry.get("value").intValue(), entry.get("steps").intValue()));
        }
    }

    /**
     * Get a specific character card's cost's pair of integer and integer.
     *
     * @param cardName card name
     * @return card cost's pair
     */
    public Pair<Integer, Integer> getCharacterCardCost(String cardName) {
        JsonNode pair = this.messageContent.at(String.format(this.gameJSONMappings.get("characterCardCost"), cardName));
        return new Pair<>(pair.get("cost").intValue(), pair.get("costDelta").intValue());
    }

    /**
     * Exception for errors within directives parser class.
     */
    public static class DirectivesParserException extends Exception {
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
