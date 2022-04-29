package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.Player;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActionStrategy;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.ActivateCharacterCard;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.PlayAssistantCard;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the game manager.
 */
public class GameManager {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Object mapper instance for serialization.
     */
    private final ObjectMapper jsonMapper;
    /**
     * Game configurations json file resource location.
     */
    private final String gameConfigurationsResourceLocation;
    /**
     * Exact players number needed to start the game.
     */
    private final int exactPlayersNumber;
    /**
     * Flag for expert mode.
     */
    private final boolean expertMode;
    /**
     * Map of players' names and corresponding player instances.
     */
    private final Map<String, Player> playersMap;
    /**
     * Map of players' names and corresponding chosen wizard type and tower color.
     */
    private final Map<String, Pair<WizardType, TowerColor>> playersInfo;
    /**
     * Map of configured game macro phases.
     */
    private final Map<Integer, GameMacroPhase> gameMacroPhases;
    /**
     * Pair of current macro phase and phase indexes.
     */
    private Pair<Integer, Integer> currentPhase;
    /**
     * Game engine instance.
     */
    private GameEngine gameEngine;
    /**
     * Flag for game started.
     */
    private boolean started;
    /**
     * Ordered list of players' playing order.
     */
    private List<String> playersOrder;
    /**
     * Index of the current player.
     */
    private int currentPlayerIndex;
    /**
     * Endgame type for winners.
     */
    private Endgame endgame;
    /**
     * List of winners' usernames.
     */
    private List<String> winners;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for game manager, initializing game configurations' resource location, exact number of players, expert mode flag,
     * players map, players info, game macro phases and current phase.
     *
     * @param gameConfigurationsResourceLocation game configurations json file resource location
     * @param gamePhasesResourceLocation         game phases json file resource location
     * @param exactPlayersNumber                 exact number of players needed to start the game
     * @param expertMode                         flag for expert mode
     * @param firstPlayerUsername                first player's username
     * @param firstPlayerInfo                    first player's chosen wizard type and tower color
     * @throws GameManagerException if number of players chosen is not permitted, or there was an error with game phases json parsing
     */
    public GameManager(String gameConfigurationsResourceLocation, String gamePhasesResourceLocation, int exactPlayersNumber,
                       boolean expertMode, String firstPlayerUsername, Pair<WizardType, TowerColor> firstPlayerInfo) throws GameManagerException {
        if (exactPlayersNumber < 2 || exactPlayersNumber > 3)
            throw new GameManagerException("Invalid number of players");
        if (firstPlayerInfo.getValue1() == TowerColor.NONE)
            throw new GameManagerException("Invalid first player's chosen tower color");
        if (firstPlayerUsername.isEmpty())
            throw new GameManagerException("Username is empty");
        this.jsonMapper = new ObjectMapper();
        this.gameConfigurationsResourceLocation = gameConfigurationsResourceLocation;
        this.exactPlayersNumber = exactPlayersNumber;
        this.expertMode = expertMode;
        this.playersMap = new HashMap<>();
        this.playersMap.put(firstPlayerUsername, new Player());
        this.playersInfo = new HashMap<>();
        this.playersInfo.put(firstPlayerUsername, firstPlayerInfo);
        this.started = false;
        this.playersOrder = Collections.emptyList();
        this.endgame = Endgame.NONE;
        this.winners = Collections.emptyList();
        try {
            InputStream jsonInputStream = GameManager.class.getResourceAsStream(gamePhasesResourceLocation);
            this.gameMacroPhases = jsonMapper.readValue(jsonInputStream, new TypeReference<Map<Integer, GameMacroPhase>>() {
            });
            this.currentPhase = new Pair<>(0, 0);
        } catch (IOException e) {
            throw new GameManagerException("Error while reading game phases json files", e);
        }
    }

    /**
     * Get a serialized copy of the game engine instance.
     *
     * @return game engine serialized copy
     * @throws GameManagerException if there was an error while serializing the game engine
     */
    public String getGameEngineJSON() throws GameManagerException {
        try {
            return this.jsonMapper.writeValueAsString(this.gameEngine);
        } catch (JsonProcessingException e) {
            throw new GameManagerException("Error while serializing game engine", e);
        }
    }

    /**
     * Get a serialized copy of the full game information.
     *
     * @return full game information serialized copy
     * @throws GameManagerException if there was an error while serializing the full game information
     */
    public String getGameJSON() throws GameManagerException {
        ObjectNode gameJSON = this.jsonMapper.createObjectNode();
        gameJSON.put("exactPlayersNumber", this.exactPlayersNumber);
        gameJSON.put("expertMode", this.expertMode);
        ObjectNode gameMacroPhasesJSON = this.jsonMapper.createObjectNode();
        this.gameMacroPhases.forEach((phaseNumber, gameMacroPhase) ->
                gameMacroPhasesJSON.set(phaseNumber.toString(), this.jsonMapper.valueToTree(gameMacroPhase)));
        gameJSON.set("gameMacroPhases", gameMacroPhasesJSON);
        gameJSON.put("currentPhase", this.getCurrentPhaseString());
        ArrayNode playersOrderJSON = this.jsonMapper.createArrayNode();
        this.playersOrder.forEach(playersOrderJSON::add);
        gameJSON.set("playersOrder", playersOrderJSON);
        gameJSON.put("currentPlayer", this.getCurrentPlayer());
        gameJSON.put("endgame", this.endgame.name());
        ArrayNode winnersJSON = this.jsonMapper.createArrayNode();
        this.winners.forEach(winnersJSON::add);
        gameJSON.set("winners", winnersJSON);
        gameJSON.set("gameEngine", this.jsonMapper.valueToTree(this.gameEngine));
        return gameJSON.toString();
    }

    /**
     * Get number of players needed to start the game.
     *
     * @return number of players needed to start the game
     */
    public int getExactPlayersNumber() {
        return this.exactPlayersNumber;
    }

    /**
     * Get flag for expert mode.
     *
     * @return flag for expert mode
     */
    public boolean isExpertMode() {
        return this.expertMode;
    }

    /**
     * Get current phase as a string "macro phase name:phase number" for phase names mapping in controller.
     *
     * @return current phase as a string
     */
    public String getCurrentPhaseString() {
        return String.format("%d:%d", this.currentPhase.getValue0(), this.currentPhase.getValue1());
    }

    /**
     * Get flag for game started.
     *
     * @return flag for game started
     */
    public boolean isStarted() {
        return this.started;
    }

    /**
     * Get shallow copy of players order list.
     *
     * @return shallow copy of players order list
     */
    public List<String> getPlayersOrder() {
        return new ArrayList<>(this.playersOrder);
    }

    /**
     * Get current player as string.
     *
     * @return current player
     */
    public String getCurrentPlayer() {
        return !this.playersOrder.isEmpty() ? this.playersOrder.get(this.currentPlayerIndex) : "";
    }

    /**
     * Get enabled character card flag of all players.
     *
     * @return enabled character card flag of all players
     */
    public Map<String, Boolean> getPlayersEnabledCharacterCard() {
        return this.playersMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().isEnabledCharacterCard()));
    }

    /**
     * Get endgame type.
     *
     * @return endgame type
     */
    public Endgame getEndgame() {
        return this.endgame;
    }

    /**
     * Get a shallow copy of winners list.
     *
     * @return shallow copy of winners list
     */
    public List<String> getWinners() {
        return new ArrayList<>(this.winners);
    }

    /**
     * Get list of available wizard types.
     *
     * @return list of available wizard types
     */
    public List<WizardType> getAvailableWizardTypes() {
        return Arrays.stream(WizardType.values())
                .filter(wizardType -> this.playersInfo.values().stream().noneMatch(value -> value.getValue0() == wizardType))
                .collect(Collectors.toList());
    }

    /**
     * Get list of available tower colors.
     *
     * @return list of available tower colors
     */
    public List<TowerColor> getAvailableTowerColors() {
        return Arrays.stream(TowerColor.values())
                .filter(towerColor -> this.playersInfo.values().stream().noneMatch(value -> value.getValue1() == towerColor)
                        && towerColor != TowerColor.NONE)
                .collect(Collectors.toList());
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Add a player to the game and create a new engine instance if game is full.
     *
     * @param username   username of the player to add
     * @param playerInfo player's chosen wizard type and tower color
     * @throws GameManagerException if game is full, username, wizard type or tower color it's already taken (or is invalid),
     *                              username is empty or there was an error while creating a new engine instance
     */
    public void addPlayer(String username, Pair<WizardType, TowerColor> playerInfo) throws GameManagerException {
        if (this.playersMap.size() == this.exactPlayersNumber)
            throw new GameManagerException("The game is already full");
        if (playerInfo.getValue1() == TowerColor.NONE)
            throw new GameManagerException("Chosen tower color is invalid");
        if (username.isEmpty())
            throw new GameManagerException("Username is empty");
        if (this.playersMap.keySet().stream()
                .anyMatch(value -> value.replace(" ", "")
                        .equalsIgnoreCase(username.replace(" ", ""))))
            throw new GameManagerException("Chosen username is already taken");
        if (this.playersInfo.values().stream().anyMatch(value -> value.getValue0() == playerInfo.getValue0()))
            throw new GameManagerException("Chosen wizard type is already taken");
        if (this.playersInfo.values().stream().anyMatch(value -> value.getValue1() == playerInfo.getValue1()))
            throw new GameManagerException("Chosen tower color is already taken");
        this.playersMap.put(username, new Player());
        this.playersInfo.put(username, playerInfo);
        if (this.playersMap.size() == this.exactPlayersNumber) {
            try {
                this.gameEngine = new GameEngine(this.gameConfigurationsResourceLocation,
                        String.format("%d_players", this.exactPlayersNumber), this.playersInfo, this.expertMode);
            } catch (GameEngine.GameEngineException e) {
                throw new GameManagerException(String.format("Error while creating game engine (%s)", e.getMessage()), e);
            }
            this.playersOrder = Arrays.asList(this.playersMap.keySet().toArray(new String[0]));
            Collections.shuffle(this.playersOrder, new Random());
            this.currentPlayerIndex = 0;
        }
    }

    /**
     * Initialize game as rules dictate.
     *
     * @throws GameManagerException if game had already started
     */
    public void initializeGame() throws GameManagerException {
        if (this.started)
            throw new GameManagerException("Game has already started");
        this.gameEngine.initializeGame();
        this.started = true;
    }

    /**
     * Execute the provided player action with the player identified by the provided username.
     *
     * @param username     player's username
     * @param playerAction action to be executed
     * @throws GameManagerException if game has not started yet, the provided username does not correspond to current player, the game has already ended,
     *                              the specified action is not playable during the current phase, or there was an error while executing the action
     */
    public void executePlayerAction(String username, ActionStrategy playerAction) throws GameManagerException {
        if (this.gameEngine == null)
            throw new GameManagerException("Game is not full yet");
        if (!this.started)
            throw new GameManagerException("Game has not started yet");
        if (!this.getCurrentPlayer().equals(username))
            throw new GameManagerException("Specified player does not correspond to the current player");
        if (!this.winners.isEmpty())
            throw new GameManagerException("Game has already ended");
        String playerActionClassString = playerAction.getClass().getSimpleName();
        if (!this.gameMacroPhases.get(this.currentPhase.getValue0())
                .getPhases().get(this.currentPhase.getValue1()).getAvailableActions()
                .contains(playerActionClassString))
            throw new GameManagerException("Specified action is not playable during the current phase");

        boolean isCharacterCard = playerActionClassString.equalsIgnoreCase(ActivateCharacterCard.class.getSimpleName());
        if (!this.playersMap.get(username).isEnabledCharacterCard() && isCharacterCard)
            throw new GameManagerException("Specified player has already used a character card during this turn");

        // Try to execute the action
        Player currentPlayer = this.playersMap.get(username);
        try {
            currentPlayer.setAction(playerAction);
            currentPlayer.executeAction(this.gameEngine, username);
        } catch (IllegalStateException e) {
            throw new GameManagerException(String.format("Error while executing action (%s)", e.getMessage()), e);
        }

        // Check if the game is finished only if playing last macro phase
        if (this.currentPhase.getValue0() == Collections.max(new ArrayList<>(this.gameMacroPhases.keySet())).intValue()) {
            this.endgame = this.checkEndgame();
            // Find winners if game is finished
            this.checkWinners(isCharacterCard);
        }

        // Change player order if needed
        if (this.currentPlayerIndex == this.playersOrder.size() - 1
                && playerActionClassString.equalsIgnoreCase(PlayAssistantCard.class.getSimpleName()))
            this.changePlayerOrder();

        // Block further character cards activations for current player
        if (isCharacterCard) {
            currentPlayer.setEnabledCharacterCard(false);
        } else {
            // Update current phase and player
            this.updateCurrentPhaseAndPlayer();
        }
    }

    /**
     * Update current phase and player depending on game phases configuration.
     */
    private void updateCurrentPhaseAndPlayer() {
        // Local variables for easier lines
        int macroPhaseIndex = this.currentPhase.getValue0();
        int phaseIndex = this.currentPhase.getValue1();
        GameMacroPhase macroPhase = this.gameMacroPhases.get(macroPhaseIndex);
        GamePhase phase = macroPhase.getPhases().get(phaseIndex);
        int nextPlayerIndex = (this.currentPlayerIndex + 1) % this.playersOrder.size();

        // First, change current phase if needed
        if (!phase.isRepeat() || nextPlayerIndex == 0) {
            if ((!macroPhase.isRepeat() || nextPlayerIndex == 0)
                    && phaseIndex == macroPhase.getPhases().size() - 1) {
                this.currentPhase = new Pair<>((macroPhaseIndex + 1) % this.gameMacroPhases.size(), 0);
            } else {
                this.currentPhase = this.currentPhase.setAt1((phaseIndex + 1) % macroPhase.getPhases().size());
            }
        }

        // Second, change current player
        if (phase.isChangePlayer()) {
            // Reset character cards parameters and flag for current player
            this.gameEngine.resetCharacterCardsParameters();
            this.playersMap.get(this.playersOrder.get(this.currentPlayerIndex)).setEnabledCharacterCard(true);
            // Change player
            this.currentPlayerIndex = nextPlayerIndex;
        }
    }

    /**
     * Change players' order of playing based on last discarded assistant cards.
     */
    private void changePlayerOrder() {
        Map<Integer, String> assistantCardsValuesPlayed = new HashMap<>();
        Map<String, PlayerDashboard> playerDashboardsMap = this.gameEngine.getGameState().getPlayerDashboards();
        this.playersOrder.forEach(username ->
                assistantCardsValuesPlayed.put(playerDashboardsMap.get(username).getLastDiscardedAssistantCard().getValue().getValue(), username));
        this.playersOrder = new ArrayList<>(assistantCardsValuesPlayed.values());
    }

    /**
     * Check for endgame conditions.
     *
     * @return current endgame type
     */
    private Endgame checkEndgame() {
        GameState gameState = this.gameEngine.getGameState();
        List<PlayerDashboard> playerDashboards = new ArrayList<>(gameState.getPlayerDashboards().values());
        if (playerDashboards.stream().anyMatch(value -> value.getTowers().getValue1() == 0))
            return Endgame.ALL_TOWERS;
        if (gameState.getArchipelago().getIslands().size() < 4)
            return Endgame.FEW_ISLANDS;
        if (playerDashboards.stream().anyMatch(value -> value.getAssistantCards().isEmpty()))
            return Endgame.NO_ASSISTANTS;
        if (gameState.getStudentsBag().getTokenContainer().getTokens().isEmpty())
            return Endgame.EMPTY_BAG;
        return Endgame.NONE;
    }

    /**
     * Check which players won, based on endgame type and default comparisons.
     *
     * @param isCharacterCard flag for character card action triggering the check
     */
    private void checkWinners(boolean isCharacterCard) {
        // Only if game is finished
        Map<String, PlayerDashboard> playerDashboards = this.gameEngine.getGameState().getPlayerDashboards();
        if (this.endgame == Endgame.ALL_TOWERS) {
            this.winners = playerDashboards.entrySet().stream()
                    .filter(entry -> entry.getValue().getTowers().getValue1() == 0)
                    .map(Map.Entry::getKey).collect(Collectors.toList());
        } else if (this.endgame == Endgame.FEW_ISLANDS
                || (this.endgame != Endgame.NONE
                && this.currentPhase.getValue1() == this.gameMacroPhases.get(this.currentPhase.getValue0()).getPhases().size() - 1
                && this.currentPlayerIndex == this.playersOrder.size() - 1 && !isCharacterCard)) {
            int lowestTowersNumber = Collections.min(playerDashboards.values().stream()
                    .map(PlayerDashboard::getTowers).map(Pair::getValue1).collect(Collectors.toList()));
            this.winners = playerDashboards.entrySet().stream()
                    .filter(entry -> entry.getValue().getTowers().getValue1() == lowestTowersNumber)
                    .map(Map.Entry::getKey).collect(Collectors.toList());
            if (this.winners.size() > 1) {
                Map<String, Integer> ownedTeachers = this.playersOrder.stream().collect(Collectors.toMap(username -> username, username -> 0));
                this.gameEngine.getGameState().getTeachers().values().stream()
                        .filter(value -> this.winners.contains(value.getOwnerUsername()))
                        .forEach(value -> ownedTeachers.put(value.getOwnerUsername(), ownedTeachers.get(value.getOwnerUsername()) + 1));
                int highestTeachersOwned = Collections.max(ownedTeachers.values());
                ownedTeachers.forEach((username, value) -> {
                    if (value != highestTeachersOwned)
                        this.winners.remove(username);
                });
            }
        }
    }
    //endregion

    /**
     * Exception for errors within game manager class.
     */
    public static class GameManagerException extends Exception {
        /**
         * GameManagerException constructor with message.
         *
         * @param message message to be shown
         */
        public GameManagerException(String message) {
            super(message);
        }

        /**
         * GameManagerException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public GameManagerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
