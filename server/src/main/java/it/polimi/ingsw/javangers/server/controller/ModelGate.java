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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Class representing the model gate for controller-model communication.
 */
public class ModelGate {
    /**
     * Model gate logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ModelGate.class.getName());
    /**
     * Exception message for logging.
     */
    private static final String EXCEPTION_MESSAGE = "Logging exception:";
    /**
     * Success message for outgoing directives.
     */
    private static final String SUCCESS_MESSAGE = "\"OK\"";
    /**
     * Game configurations json file resource location.
     */
    private final String gameConfigurationsResourceLocation;
    /**
     * Game phases json file resource location.
     */
    private final String gamePhasesResourceLocation;
    /**
     * Object mapper for json serialization/deserialization.
     */
    private final ObjectMapper jsonMapper;
    /**
     * List of player connections ids participating in the game.
     */
    private final List<Integer> playerConnectionsIDsList;
    /**
     * Flag for player connections filter after game filled.
     */
    private boolean gameFull;
    /**
     * Directives forge instance for incoming directives' deserialization.
     */
    private DirectivesForge directivesForge;
    /**
     * Game manager instance.
     */
    private GameManager gameManager;

    /**
     * Constructor for model gate, initializing json file resource locations, directives forge,
     * json mapper, player connections ids and game full flag.
     *
     * @param gameConfigurationsResourceLocation          game configurations json file resource location
     * @param gamePhasesResourceLocation                  game phases json file resource location
     * @param actionStrategyClassMappingsResourceLocation action strategy class mappings json file resource location
     * @param effectStrategyClassMappingsResourceLocation effect strategy class mappings json file resource location
     */
    public ModelGate(String gameConfigurationsResourceLocation, String gamePhasesResourceLocation,
                     String actionStrategyClassMappingsResourceLocation, String effectStrategyClassMappingsResourceLocation) {
        this.gameConfigurationsResourceLocation = gameConfigurationsResourceLocation;
        this.gamePhasesResourceLocation = gamePhasesResourceLocation;
        LOGGER.info("Creating directives forge");
        try {
            this.directivesForge = new DirectivesForge(actionStrategyClassMappingsResourceLocation, effectStrategyClassMappingsResourceLocation);
        } catch (DirectivesForge.DirectivesForgeException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ModelGateException(String.format("Error while creating directives forge (%s)", e.getMessage()), e));
            System.exit(1);
        }
        this.jsonMapper = new ObjectMapper();
        this.playerConnectionsIDsList = new ArrayList<>();
        this.gameFull = false;
    }

    /**
     * Get shallow copy of player connections ids list
     *
     * @return list of player connections ids participating in the game
     */
    public List<Integer> getPlayerConnectionIDs() {
        return new ArrayList<>(this.playerConnectionsIDsList);
    }

    /**
     * Get game full flag
     *
     * @return game full flag
     */
    public boolean isGameFull() {
        return this.gameFull;
    }

    /**
     * Get flag for game started.
     *
     * @return flag for game started
     */
    public boolean isGameStarted() {
        return this.gameManager != null && this.gameManager.isStarted();
    }

    /**
     * Execute incoming directive depending on message type.
     *
     * @param playerConnectionID player connection id
     * @param jsonDirective      json string to be deserialized by the forge
     * @return pair with message type and content for outgoing directive
     */
    public Pair<MessageType, String> executeDirective(int playerConnectionID, String jsonDirective) {
        try {
            JsonNode directiveTree = this.jsonMapper.readTree(jsonDirective);
            if (!Stream.of("type", "username", "content").allMatch(directiveTree::has)) {
                LOGGER.warning("Invalid directive json format");
                return new Pair<>(MessageType.ERROR, "\"Invalid directive json format\"");
            }
            String directiveType = directiveTree.get("type").asText();
            if (Arrays.stream(MessageType.values()).noneMatch(messageType -> messageType.name().equals(directiveType))) {
                LOGGER.warning("Unknown directive message type");
                return new Pair<>(MessageType.ERROR, "\"Unknown directive message type\"");
            }
            MessageType type = MessageType.valueOf(directiveType);
            String username = directiveTree.get("username").textValue();
            String content = directiveTree.get("content").toString();
            LOGGER.log(Level.INFO, "Executing directive - {0}", type);
            return this.getOutgoingDirective(playerConnectionID, type, username, content);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ModelGateException(String.format("Error while deserializing/serializing directive (%s)", e.getMessage()), e));
            return new Pair<>(MessageType.ERROR, "\"Error while deserializing/serializing directive\"");
        } catch (GameManager.GameManagerException | DirectivesForge.DirectivesForgeException e) {
            LOGGER.log(Level.SEVERE, EXCEPTION_MESSAGE,
                    new ModelGateException(e.getMessage(), e));
            return new Pair<>(MessageType.ERROR, String.format("\"%s\"", e.getMessage()));
        }
    }

    /**
     * Check game full for setting flag.
     */
    private void checkGameFull() {
        if (this.gameManager.getPlayersOrder().size() == this.gameManager.getExactPlayersNumber()) {
            LOGGER.info("Game full");
            this.gameFull = true;
        }
    }

    /**
     * Check game ended for resetting.
     */
    private void checkGameEnded() {
        if (!this.gameManager.getWinners().isEmpty()) {
            LOGGER.log(Level.INFO, "Game ended - winners: {0}", this.gameManager.getWinners());
            this.reset();
        }
    }

    /**
     * Reset game manager and player connections IDs list.
     */
    public void reset() {
        this.gameManager = null;
        this.playerConnectionsIDsList.clear();
        this.gameFull = false;
    }

    /**
     * Execute incoming directive depending on message type.
     *
     * @param playerConnectionID player connection id
     * @param type               message type
     * @param username           player username
     * @param content            message content
     * @return pair with message type and content for outgoing directive
     * @throws GameManager.GameManagerException         if there was an error while calling game manager methods
     * @throws DirectivesForge.DirectivesForgeException if there was an error while parsing directives through the forge
     */
    private Pair<MessageType, String> getOutgoingDirective(int playerConnectionID, MessageType type, String username, String content)
            throws GameManager.GameManagerException, DirectivesForge.DirectivesForgeException {
        switch (type) {
            case CREATE:
                if (this.gameManager != null) {
                    LOGGER.warning("Game already created");
                    return new Pair<>(MessageType.ERROR, "\"Game already created\"");
                }
                Triplet<Integer, Boolean, Pair<WizardType, TowerColor>> creationParameters = directivesForge.parseGameManager(content);
                LOGGER.log(Level.INFO, "Creating game - {0}", username);
                this.gameManager = new GameManager(this.gameConfigurationsResourceLocation, this.gamePhasesResourceLocation, creationParameters.getValue0(),
                        creationParameters.getValue1(), username, creationParameters.getValue2());
                this.playerConnectionsIDsList.add(playerConnectionID);
                return new Pair<>(type, SUCCESS_MESSAGE);
            case PLAYER:
                if (this.gameManager == null) {
                    LOGGER.warning("Cannot add player - game not created yet");
                    return new Pair<>(MessageType.ERROR, "\"Cannot add player: game not created yet\"");
                }
                Pair<WizardType, TowerColor> playerInfo = directivesForge.parseAddPlayer(content);
                LOGGER.log(Level.INFO, "Adding player - {0}", username);
                this.gameManager.addPlayer(username, playerInfo);
                if (!this.playerConnectionsIDsList.contains(playerConnectionID))
                    this.playerConnectionsIDsList.add(playerConnectionID);
                this.checkGameFull();
                return new Pair<>(type, SUCCESS_MESSAGE);
            case START:
                if (this.gameManager == null) {
                    LOGGER.warning("Cannot start - game not created yet");
                    return new Pair<>(MessageType.ERROR, "\"Cannot start: game not created yet\"");
                }
                if (!this.playerConnectionsIDsList.contains(playerConnectionID)) {
                    LOGGER.log(Level.WARNING, "Cannot start - player connection {0} not added to the game", playerConnectionID);
                    return new Pair<>(MessageType.ERROR, "\"Cannot start: player connection not added to the game\"");
                }
                LOGGER.info("Initializing game");
                this.gameManager.initializeGame();
                return new Pair<>(type, this.gameManager.getGameJSON());
            case ACTION:
                if (this.gameManager == null) {
                    LOGGER.warning("Cannot execute action - game not created yet");
                    return new Pair<>(MessageType.ERROR, "\"Cannot execute action: game not created yet\"");
                }
                if (!this.playerConnectionsIDsList.contains(playerConnectionID)) {
                    LOGGER.log(Level.WARNING, "Cannot execute action - player connection {0} not added to the game", playerConnectionID);
                    return new Pair<>(MessageType.ERROR, "\"Cannot execute action: player connection not added to the game\"");
                }
                ActionStrategy action = directivesForge.parseAction(content);
                LOGGER.log(Level.INFO, "Executing player action - {0} ({1})", new Object[]{username, action.getClass().getSimpleName()});
                this.gameManager.executePlayerAction(username, action);
                String gameJSON = this.gameManager.getGameJSON();
                this.checkGameEnded();
                return new Pair<>(type, gameJSON);
            default:
                LOGGER.warning("Unsupported directive message type");
                return new Pair<>(type, "\"Unsupported directive message type\"");
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
