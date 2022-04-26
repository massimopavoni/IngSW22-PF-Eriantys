package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.model.game_data.Archipelago;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.Teacher;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class representing the game engine.
 */
public class GameEngine {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Game configuration instance.
     */
    private final GameConfiguration gameConfiguration;
    /**
     * Game state instance.
     */
    private final GameState gameState;
    /**
     * Random instance for game initialization.
     */
    private final Random initializationRandom;
    /**
     * Flag for expert mode game.
     */
    private final boolean expertMode;
    /**
     * Map of character cards for the game.
     */
    private final Map<String, CharacterCard> characterCardsMap;
    /**
     * Flag for teachers power comparison.
     */
    private boolean teachersEqualCount;
    /**
     * Additional mother nature steps allowed for the current turn.
     */
    private int additionalMotherNatureSteps;
    /**
     * Flag for island towers power bonus.
     */
    private boolean enabledIslandTowers;
    /**
     * Additional power points for the current turn.
     */
    private int additionalPower;
    /**
     * Forbidden token color for island power evaluation.
     */
    private TokenColor forbiddenColor;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for the game engine, initializing game configuration, game state, character cards and game engine parameters,
     * based on players' info, selected configuration and expert mode flag.
     *
     * @param gameConfigurationsResourceLocation resource location of game configurations json file
     * @param configurationName                  selected configuration name
     * @param playersInfo                        players specific info for game state initialization
     * @param expertMode                         flag for expert mode game
     * @throws GameEngineException if json parsing or game state initialization fails for some reason (stack trace can be examined)
     */
    public GameEngine(String gameConfigurationsResourceLocation, String configurationName, Map<String, Pair<WizardType, TowerColor>> playersInfo, boolean expertMode) throws GameEngineException {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            // Get game configuration based on number of players
            InputStream jsonInputStream = GameEngine.class.getResourceAsStream(gameConfigurationsResourceLocation);
            Map<String, GameConfiguration> gameConfigurations = jsonMapper.readValue(jsonInputStream, new TypeReference<Map<String, GameConfiguration>>() {
            });
            this.gameConfiguration = gameConfigurations.get(configurationName);
            this.expertMode = expertMode;

            // Create game state based on game configuration, player info and expert mode
            Map<TokenColor, Integer> studentsBagMap = new EnumMap<>(TokenColor.class);
            Arrays.stream(TokenColor.values()).forEach(color -> studentsBagMap.put(color, this.gameConfiguration.getStudentsPerColor()));
            this.gameState = new GameState(this.gameConfiguration.getAssistantCardsResourceLocation(), playersInfo,
                    this.gameConfiguration.getTowersPerDashboard(), this.gameConfiguration.getNumberOfIslands(), studentsBagMap,
                    this.expertMode ? this.gameConfiguration.getCoinsPerDashBoard() : 0);

            // Create random instance for later game initialization
            this.initializationRandom = new Random();

            this.characterCardsMap = new HashMap<>();
            // Read character cards and select some random ones if expert mode is enabled
            if (this.expertMode) {
                jsonInputStream = GameEngine.class.getResourceAsStream(this.gameConfiguration.getCharacterCardsResourceLocation());
                Map<String, CharacterCard> allCharacterCardsMap = jsonMapper.readValue(jsonInputStream, new TypeReference<Map<String, CharacterCard>>() {
                });
                List<String> characterCardsKeys = new ArrayList<>(allCharacterCardsMap.keySet());
                Collections.shuffle(characterCardsKeys, this.initializationRandom);
                characterCardsKeys.subList(0, this.gameConfiguration.getNumberOfCharacterCards())
                        .forEach(key -> this.characterCardsMap.put(key, allCharacterCardsMap.get(key)));
            }

            // Set default values for character cards parameters
            this.teachersEqualCount = false;
            this.additionalMotherNatureSteps = 0;
            this.enabledIslandTowers = true;
            this.additionalPower = 0;
            this.forbiddenColor = null;
        } catch (IOException e) {
            throw new GameEngineException("Error while reading game configurations or character cards json files", e);
        } catch (GameState.GameStateException e) {
            throw new GameEngineException(String.format("Error while creating game state (%s)", e.getMessage()), e);
        }
    }

    /**
     * Get game configuration instance.
     *
     * @return game configuration instance
     */
    public GameConfiguration getGameConfiguration() {
        return this.gameConfiguration;
    }

    /**
     * Get game state instance.
     *
     * @return game state instance
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Get expert mode flag.
     *
     * @return expert mode flag
     */
    public boolean isExpertMode() {
        return this.expertMode;
    }

    /**
     * Get shallow copy of character cards map.
     *
     * @return character cards map
     */
    public Map<String, CharacterCard> getCharacterCards() {
        return new HashMap<>(this.characterCardsMap);
    }

    /**
     * Get teachers power comparison flag.
     *
     * @return teachers power comparison flag
     */
    public boolean getTeachersEqualCount() {
        return this.teachersEqualCount;
    }

    /**
     * Set teachers power comparison flag.
     *
     * @param teachersEqualCount new teachers power comparison flag value
     */
    public void setTeachersEqualCount(boolean teachersEqualCount) {
        this.teachersEqualCount = teachersEqualCount;
    }

    /**
     * Get additional mother nature steps for current turn.
     *
     * @return additional mother nature steps for current turn
     */
    public int getAdditionalMotherNatureSteps() {
        return this.additionalMotherNatureSteps;
    }

    /**
     * Set additional mother nature steps for current turn.
     *
     * @param additionalMotherNatureSteps new additional mother nature steps value
     */
    public void setAdditionalMotherNatureSteps(int additionalMotherNatureSteps) {
        this.additionalMotherNatureSteps = additionalMotherNatureSteps;
    }

    /**
     * Get island towers power bonus flag.
     *
     * @return island towers power bonus flag
     */
    public boolean getEnabledIslandTowers() {
        return this.enabledIslandTowers;
    }

    /**
     * Set island towers power bonus flag.
     *
     * @param enabledIslandTowers new island towers power bonus flag value
     */
    public void setEnabledIslandTowers(boolean enabledIslandTowers) {
        this.enabledIslandTowers = enabledIslandTowers;
    }

    /**
     * Get additional power points for current turn.
     *
     * @return additional power points for current turn
     */
    public int getAdditionalPower() {
        return this.additionalPower;
    }

    /**
     * Set additional power points for current turn.
     *
     * @param additionalPower new additional power points value
     */
    public void setAdditionalPower(int additionalPower) {
        this.additionalPower = additionalPower;
    }

    /**
     * Get forbidden token color for island power evaluation.
     *
     * @return forbidden token color for island power evaluation
     */
    public TokenColor getForbiddenColor() {
        return this.forbiddenColor;
    }

    /**
     * Set forbidden token color for island power evaluation.
     *
     * @param forbiddenColor new forbidden token color value
     */
    public void setForbiddenColor(TokenColor forbiddenColor) {
        this.forbiddenColor = forbiddenColor;
    }
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Methods

    /**
     * Initialize game as rules dictate.
     */
    public void initializeGame() {
        // Local variables for easier lines
        Archipelago archipelago = this.gameState.getArchipelago();
        List<Island> islands = archipelago.getIslands();
        // Set initial mother nature position
        archipelago.setMotherNaturePosition(this.initializationRandom.nextInt(islands.size()));
        // Distribute tokens on islands
        List<TokenColor> initialIslandsTokens = new ArrayList<>();
        List<Island> islandsWithTokens = islands.stream().filter(island -> !Arrays.asList(archipelago.getMotherNaturePosition(),
                        (archipelago.getMotherNaturePosition() + islands.size() / 2) % islands.size())
                .contains(islands.indexOf(island))).collect(Collectors.toList());
        int tokensPerColor = (int) Math.ceil((double) islandsWithTokens.size() / TokenColor.values().length);
        Arrays.stream(TokenColor.values()).forEach(color -> initialIslandsTokens
                .addAll(Collections.nCopies(tokensPerColor, color)));
        this.gameState.getStudentsBag().getTokenContainer().extractTokens(initialIslandsTokens);
        Collections.shuffle(initialIslandsTokens, this.initializationRandom);
        IntStream.range(0, initialIslandsTokens.size()).boxed().sorted(Collections.reverseOrder())
                .forEach(i -> islandsWithTokens.get(i % islandsWithTokens.size()).getTokenContainer()
                        .addTokens(Collections.singletonList(initialIslandsTokens.remove(i.intValue()))));
        // Fill dashboards' entrances
        this.gameState.getPlayerDashboards().values().forEach(dashboard -> dashboard.getEntrance()
                .addTokens(this.gameState.getStudentsBag().grabTokens(this.gameConfiguration.getStudentsPerEntrance())));
        // Fill token containers of character cards, if needed
        for (CharacterCard characterCard : this.characterCardsMap.values())
            if (characterCard.getTokenContainerSize() > 0)
                characterCard.getTokenContainer().addTokens(this.gameState.getStudentsBag().grabTokens(characterCard.getTokenContainerSize()));
    }

    /**
     * Reset character cards parameters to their default values.
     */
    public void resetCharacterCardsParameters() {
        this.teachersEqualCount = false;
        this.additionalMotherNatureSteps = 0;
        this.enabledIslandTowers = true;
        this.additionalPower = 0;
        this.forbiddenColor = null;
    }

    /**
     * Change teachers owners and students number based on game state changes.
     *
     * @param username username of the player who triggered the update
     */
    public void changeTeachersPower(String username) {
        this.updateTeachersPower();
        Map<TokenColor, Teacher> teachers = this.gameState.getTeachers();
        int candidatePower;
        int currentPower;
        for (Map.Entry<String, PlayerDashboard> playerDashboard : this.gameState.getPlayerDashboards().entrySet()) {
            for (Map.Entry<TokenColor, Integer> colorCount : playerDashboard.getValue().getHall().getColorCounts().entrySet()) {
                candidatePower = colorCount.getValue();
                currentPower = teachers.get(colorCount.getKey()).getOwnerStudentsNumber();
                if (playerDashboard.getKey().equals(username) && this.teachersEqualCount ?
                        candidatePower >= currentPower : candidatePower > currentPower) {
                    teachers.get(colorCount.getKey()).setOwner(playerDashboard.getKey(), colorCount.getValue());
                }
            }
        }
    }

    /**
     * Update teachers students numbers to make up for character cards shenanigans.
     */
    private void updateTeachersPower() {
        Map<String, PlayerDashboard> playerDashboards = this.gameState.getPlayerDashboards();
        this.gameState.getTeachers().entrySet().stream().filter(entry -> !entry.getValue().getOwnerUsername().isEmpty())
                .forEach(entry -> entry.getValue().setOwner(entry.getValue().getOwnerUsername(),
                        playerDashboards.get(entry.getValue().getOwnerUsername()).getHall().getColorCounts().get(entry.getKey())));
    }

    /**
     * Change island towers power and archipelago merging situation based on game state changes.
     *
     * @param selectedIslandIndex selected island index
     * @param username            username of player who selected island
     */
    public void changeIslandPower(int selectedIslandIndex, String username) {
        // Trigger island power changes if island is enabled
        Archipelago archipelago = this.gameState.getArchipelago();
        Island selectedIsland = archipelago.getIslands().get(selectedIslandIndex);
        if (selectedIsland.getEnabled() == 0) {
            // Get base players power map
            Map<String, Integer> playersPower = this.assignPlayersPower(selectedIslandIndex);

            // Add additional power
            playersPower.put(username, playersPower.get(username) + this.additionalPower);

            // Find all players with the highest power
            int highestPower = Collections.max(playersPower.values());
            List<String> islandWinners = playersPower.entrySet().stream()
                    .filter(entry -> entry.getValue() == highestPower).map(Map.Entry::getKey).collect(Collectors.toList());

            // Make changes based on island winners
            // "There can be only one (one, one, one)"
            PlayerDashboard winnerDashboard = this.gameState.getPlayerDashboards().get(islandWinners.get(0));
            if (islandWinners.size() == 1 && selectedIsland.getTowers().getValue0() != winnerDashboard.getTowers().getValue0()) {
                this.updateIslandData(selectedIslandIndex, winnerDashboard);
            }
        } else {
            selectedIsland.setEnabled(selectedIsland.getEnabled() - 1);
            CharacterCard herbalist = this.characterCardsMap.get("herbalist");
            herbalist.setMultipurposeCounter(herbalist.getMultipurposeCounter() + 1);
        }
    }

    /**
     * Create players power map based on selected island and players' power on teachers.
     *
     * @param selectedIslandIndex selected island index
     * @return players power map (without additional power)
     */
    private Map<String, Integer> assignPlayersPower(int selectedIslandIndex) {
        // Temporary variables for easier lines
        Island selectedIsland = this.gameState.getArchipelago().getIslands().get(selectedIslandIndex);
        Pair<TowerColor, Integer> selectedIslandTowers = selectedIsland.getTowers();
        TokenContainer selectedIslandTokenContainer = selectedIsland.getTokenContainer();
        Map<String, PlayerDashboard> playerDashboards = this.gameState.getPlayerDashboards();

        // Generate a players power map
        Map<String, Integer> playersPower = playerDashboards.keySet().stream().collect(Collectors.toMap(u -> u, u -> 0));
        // Add island number of students only if player is corresponding teacher's owner
        this.gameState.getTeachers().entrySet().stream()
                .filter(entry -> selectedIslandTokenContainer.getTokens().contains(entry.getKey()) &&
                        entry.getKey() != this.forbiddenColor && !(entry.getValue().getOwnerUsername()).equals(""))
                .forEach(entry -> playersPower.put(entry.getValue().getOwnerUsername(),
                        selectedIslandTokenContainer.getColorCounts().get(entry.getKey())));

        // Add initial values from placed towers
        if (this.enabledIslandTowers) {
            playerDashboards.forEach((key, value) -> playersPower.put(key,
                    playersPower.get(key) + (value.getTowers().getValue0().equals(selectedIslandTowers.getValue0())
                            ? selectedIslandTowers.getValue1() : 0)));
        }

        return playersPower;
    }

    /**
     * Update island towers and archipelago merging situation based on winning player.
     *
     * @param selectedIslandIndex selected island index
     * @param winnerDashboard     winner dashboard
     */
    private void updateIslandData(int selectedIslandIndex, PlayerDashboard winnerDashboard) {
        // Temporary variables for easier lines
        Island selectedIsland = this.gameState.getArchipelago().getIslands().get(selectedIslandIndex);
        Pair<TowerColor, Integer> selectedIslandTowers = selectedIsland.getTowers();

        // Get towers number based on remaining towers on winner dashboard
        int towersNumber = Math.min(
                selectedIslandTowers.getValue0().equals(TowerColor.NONE) ? 1 : selectedIslandTowers.getValue1(),
                winnerDashboard.getTowers().getValue1());

        // Update island and dashboard towers
        TowerColor winnerTowerColor = winnerDashboard.getTowers().getValue0();
        selectedIsland.setTowers(new Pair<>(winnerTowerColor, towersNumber));
        winnerDashboard.setTowersNumber(winnerDashboard.getTowers().getValue1() - towersNumber);

        // Update archipelago for island merging
        Archipelago archipelago = this.gameState.getArchipelago();
        List<Island> islands = archipelago.getIslands();
        archipelago.mergeIslands(selectedIslandIndex,
                winnerTowerColor == islands.get((selectedIslandIndex - 1 + islands.size()) % islands.size()).getTowers().getValue0(),
                winnerTowerColor == islands.get((selectedIslandIndex + 1) % islands.size()).getTowers().getValue0());
    }
    //endregion

    /**
     * Exception for errors within game engine class.
     */
    public static class GameEngineException extends Exception {
        /**
         * GameEngineException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public GameEngineException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
