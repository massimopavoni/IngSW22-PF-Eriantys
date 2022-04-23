package it.polimi.ingsw.javangers.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameManager;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActionStrategy;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * Class representing the gate that connects model and controller.
 */
public class ModelGate {
    /**
     * Directives forge for deserialization of player commands.
     */
    private final DirectivesForge directivesForge;

    /**
     * Pattern with location of game configurations.
     */
    private final String gameConfigurationsResourceLocation;

    /**
     * Pattern with location of game phases configurations.
     */
    private final String gamePhasesResourceLocation;

    /**
     * Object mapper for json serializing/deserializing
     */
    private final ObjectMapper jsonMapper;

    /**
     * Game manager instance in order to execute the commands of the players
     */
    private GameManager gameManager;

    /**
     * Constructor for the model gate, initializing game configurations, game phase configurations, directives forge
     * and json mapper.
     *
     * @param gameConfigurationsResourceLocation          pattern with location of game configurations
     * @param gamePhasesResourceLocation                  pattern with location of game phases configurations
     * @param actionStrategyClassMappingsResourceLocation pattern with location of action strategy class mappings used in directives forge
     * @param effectStrategyClassMappingsResourceLocation pattern with location of effect strategy class mappings used in directives forge
     * @throws DirectivesForge.DirectivesForgeException if an error occurred instantiating the directives forge
     */
    public ModelGate(String gameConfigurationsResourceLocation, String gamePhasesResourceLocation,
                     String actionStrategyClassMappingsResourceLocation, String effectStrategyClassMappingsResourceLocation) throws DirectivesForge.DirectivesForgeException {
        this.gameConfigurationsResourceLocation = gameConfigurationsResourceLocation;
        this.gamePhasesResourceLocation = gamePhasesResourceLocation;
        this.directivesForge = new DirectivesForge(actionStrategyClassMappingsResourceLocation, effectStrategyClassMappingsResourceLocation);
        this.jsonMapper = new ObjectMapper();
    }

    /**
     * Compose message from json.
     *
     * @param messageType    type of the message derivated from json
     * @param messageContent content of the message derivated from json
     * @return string of the message
     * @throws ModelGateException if an error occurred during the composition of the message
     */
    private String composeJSONMessage(MessageType messageType, String messageContent) throws ModelGateException {
        try {
            return this.jsonMapper.writeValueAsString(new Message(messageType, this.jsonMapper.readTree(messageContent)));
        } catch (JsonProcessingException e) {
            throw new ModelGateException(String.format("Error while serializing message (%s)", e.getMessage()), e);
        }
    }

    /**
     * Execution of the directives deserialized by the forge.
     *
     * @param jsonDirective string json that will be deserialized by the forge
     * @return message to understand if the method executed the directive correctly or not
     * @throws ModelGateException if an error occurred during the composition of the message
     */
    public String executeDirective(String jsonDirective) throws ModelGateException {
        try {
            JsonNode directiveTree = this.jsonMapper.readTree(jsonDirective);
            MessageType type = MessageType.valueOf(directiveTree.get("type").textValue());
            String username = directiveTree.get("username").textValue();
            String content = directiveTree.get("content").toString();

            switch (type) {
                case CREATE:
                    Triplet<Integer, Boolean, Pair<WizardType, TowerColor>> creationParameters = directivesForge.parseGameManager(content);
                    this.gameManager = new GameManager(this.gameConfigurationsResourceLocation, this.gamePhasesResourceLocation, creationParameters.getValue0(),
                            creationParameters.getValue1(), username, creationParameters.getValue2());
                    return this.composeJSONMessage(type, "\"OK\"");
                case PLAYER:
                    Pair<WizardType, TowerColor> playerInfo = directivesForge.parseAddPlayer(content);
                    this.gameManager.addPlayer(username, playerInfo);
                    return this.composeJSONMessage(type, this.gameManager.getGameJSON());
                case START:
                    this.gameManager.initializeGame();
                    return this.composeJSONMessage(type, this.gameManager.getGameJSON());
                case ACTION:
                    ActionStrategy parameter = directivesForge.parseAction(content);
                    this.gameManager.executePlayerAction(username, parameter);
                    return this.composeJSONMessage(type, this.gameManager.getGameJSON());
                default:
                    return this.composeJSONMessage(type, "\"Unsupported message type received\"");
            }

        } catch (JsonProcessingException e) {
            return this.composeJSONMessage(MessageType.ERROR, String.format("\"Error while deserializing directive (%s)\"", e.getMessage()));
        } catch (GameManager.GameManagerException | DirectivesForge.DirectivesForgeException e) {
            return this.composeJSONMessage(MessageType.ERROR, String.format("\"%s\"", e.getMessage()));
        }
    }

    /**
     * Exception for errors within model gate class.
     */
    public static class ModelGateException extends Exception {
        /**
         * ModelGateException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public ModelGateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
