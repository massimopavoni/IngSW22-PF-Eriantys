package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.Knight;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.Monk;
import it.polimi.ingsw.javangers.server.model.game_mechanics.character_cards_effects.Queen;
import it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions.*;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {
    GameManager gameManager;
    ObjectMapper jsonMapper;

    @BeforeEach
    void setUp() throws GameManager.GameManagerException {
        gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 3, true,
                "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
        jsonMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Test constructor with illegal number of players 0")
    void GameManager_illegalNumberOfPlayers0() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                        "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 0, true,
                        "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.BLACK)));
    }

    @Test
    @DisplayName("Test constructor with illegal number of players 4")
    void GameManager_illegalNumberOfPlayers4() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                        "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 4, true,
                        "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.BLACK)));
    }

    @Test
    @DisplayName("Test constructor with invalid tower color")
    void GameManager_invalidTowerColor() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                        "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 2, true,
                        "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.NONE)));
    }

    @Test
    @DisplayName("Test constructor with empty username")
    void GameManager_emptyUsername() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                        "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 2, true,
                        "", new Pair<>(WizardType.DRUID, TowerColor.BLACK)));
    }

    @Test
    @DisplayName("Test constructor for invalid game phases file path")
    void GameManager_invalidGamePhasesFilePath() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                        "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/GamePhase.class", 2, true,
                        "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.BLACK)));
    }

    @Test
    @DisplayName("Test constructor for correct usage")
    void GameManager_correctConstructor() throws GameManager.GameManagerException {
        gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_configurations.json",
                "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 3, true,
                "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.BLACK));
        assertAll(
                () -> assertFalse(gameManager.isStarted()),
                () -> assertEquals(3, gameManager.getExactPlayersNumber()),
                () -> assertTrue(gameManager.isExpertMode()),
                () -> assertEquals("0:0", gameManager.getCurrentPhaseString()),
                () -> assertEquals(Collections.emptyList(), gameManager.getPlayersOrder()),
                () -> assertEquals(Endgame.NONE, gameManager.getEndgame()),
                () -> assertEquals(Collections.emptyList(), gameManager.getWinners()),
                () -> assertFalse(gameManager.getGameJSON().isEmpty())
        );
    }

    @Test
    @DisplayName("Test getPlayersOrder for shallow copy")
    void getPlayersOrder_shallowCopy() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        List<String> playersOrderCopy = gameManager.getPlayersOrder();
        playersOrderCopy.add("K");
        assertAll(
                () -> assertEquals(3, gameManager.getPlayersOrder().size()),
                () -> assertTrue(gameManager.getPlayersOrder().contains("Rick Deckard")),
                () -> assertTrue(gameManager.getPlayersOrder().contains("Roy Batty")),
                () -> assertTrue(gameManager.getPlayersOrder().contains("Rachael"))
        );
    }

    @Test
    @DisplayName("Test getWinners for shallow copy")
    void getWinners_shallowCopy() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        List<String> winnersCopy = gameManager.getWinners();
        winnersCopy.add("Rick Deckard");
        assertEquals(Collections.emptyList(), gameManager.getWinners());
    }

    @Test
    @DisplayName("Test getAvailableWizardTypes for correct remaining wizard types")
    void getAvailableWizardTypes_correctRemaining() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        assertEquals(Arrays.asList(WizardType.WITCH, WizardType.SENSEI), gameManager.getAvailableWizardTypes());
    }

    @Test
    @DisplayName("Test getAvailableTowerColors for correct remaining tower colors")
    void getAvailableTowerColors_correctRemaining() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        assertEquals(Collections.singletonList(TowerColor.GRAY), gameManager.getAvailableTowerColors());
    }

    @Test
    @DisplayName("Test addPlayer with already full game")
    void addPlayer_gameAlreadyFull() {
        assertAll(
                () -> assertDoesNotThrow(() -> gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertDoesNotThrow(() -> gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY))),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.addPlayer("K", new Pair<>(WizardType.SENSEI, TowerColor.GRAY)))
        );
    }

    @Test
    @DisplayName("Test addPlayer with invalid tower color")
    void addPlayer_invalidTowerColor() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.NONE)));
    }

    @Test
    @DisplayName("Test addPlayer with empty username")
    void addPlayer_emptyUsername() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager.addPlayer("", new Pair<>(WizardType.KING, TowerColor.WHITE)));
    }

    @Test
    @DisplayName("Test addPlayer with already chosen username")
    void addPlayer_duplicateUsername() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager.addPlayer("rickdeckard", new Pair<>(WizardType.WITCH, TowerColor.GRAY)));
    }

    @Test
    @DisplayName("Test addPlayer with already chosen wizard type")
    void addPlayer_duplicateWizardType() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.DRUID, TowerColor.WHITE)));
    }

    @Test
    @DisplayName("Test addPlayer with already chosen tower color")
    void addPlayer_duplicateTowerColor() {
        assertThrowsExactly(GameManager.GameManagerException.class,
                () -> gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.BLACK)));
    }

    @Test
    @DisplayName("Test addPlayer with some mistake in configuration for game engine exception")
    void addPlayer_gameEngineException() {
        assertAll(
                () -> assertDoesNotThrow(
                        () -> gameManager = new GameManager("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                                "/it/polimi/ingsw/javangers/server/model/game_mechanics/core/game_phases.json", 2, true,
                                "Rick Deckard", new Pair<>(WizardType.DRUID, TowerColor.BLACK))),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE)))
        );
    }

    @Test
    @DisplayName("Test addPlayer for correct adding")
    void addPlayer_correct() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        assertAll(
                () -> assertFalse(gameManager.getGameEngineJSON().isEmpty()),
                () -> assertEquals(1, gameManager.getAvailableWizardTypes().size()),
                () -> assertEquals(3, gameManager.getPlayersOrder().size()),
                () -> assertTrue(Arrays.asList("Rick Deckard", "Roy Batty", "Rachael").contains(gameManager.getCurrentPlayer()))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction with game not full")
    void executePlayerAction_gameNotFull() {
        assertAll(
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.executePlayerAction("indifferent", new FillClouds()))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction with game not started")
    void executePlayerAction_gameNotStarted() {
        assertAll(
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY))),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds()))
        );
    }

    @Test
    @DisplayName("Test initializeGame with game already started")
    void initializeGame_gameAlreadyStarted() {
        assertAll(
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY))),
                () -> assertDoesNotThrow(() -> gameManager.initializeGame()),
                () -> assertTrue(gameManager.isStarted()),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.initializeGame())
        );
    }

    @Test
    @DisplayName("Test executePlayerAction with wrong current player")
    void executePlayerAction_notCurrentPlayer() {
        assertAll(
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY))),
                () -> assertDoesNotThrow(() -> gameManager.initializeGame()),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.executePlayerAction(gameManager.getPlayersOrder().get(1), new FillClouds()))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction with wrong action in current phase")
    void executePlayerAction_unavailableAction() {
        assertAll(
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY))),
                () -> assertDoesNotThrow(() -> gameManager.initializeGame()),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle")))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction with action illegal state")
    void executePlayerAction_actionIllegalState() {
        assertAll(
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE))),
                () -> assertDoesNotThrow(() ->
                        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY))),
                () -> assertDoesNotThrow(() -> gameManager.initializeGame()),
                () -> assertDoesNotThrow(() -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds())),
                () -> assertDoesNotThrow(() -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"))),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle")))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction for correct phase and player change")
    void executePlayerAction_correctPhaseAndPlayer() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        List<String> playersOrder = gameManager.getPlayersOrder();
        assertAll(
                () -> assertEquals("0:0", gameManager.getCurrentPhaseString()),
                () -> assertDoesNotThrow(() -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds())),
                () -> assertEquals("0:1", gameManager.getCurrentPhaseString()),
                () -> assertEquals(playersOrder.get(0), gameManager.getCurrentPlayer()),
                () -> assertDoesNotThrow(() -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"))),
                () -> assertEquals("0:1", gameManager.getCurrentPhaseString()),
                () -> assertEquals(playersOrder.get(1), gameManager.getCurrentPlayer())
        );
    }

    @Test
    @DisplayName("Test executePlayerAction for correct player order change")
    void executePlayerAction_correctChangeOrder() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        List<String> expectedOrder = new ArrayList<>();
        expectedOrder.add(gameManager.getCurrentPlayer());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
        expectedOrder.add(0, gameManager.getCurrentPlayer());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
        expectedOrder.add(1, gameManager.getCurrentPlayer());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
        assertEquals(expectedOrder, gameManager.getPlayersOrder());
    }

    @Test
    @DisplayName("Test executePlayerAction for correct macro phase change")
    void executePlayerAction_correctMacroPhaseChange() throws GameManager.GameManagerException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
        String expectedPlayer = gameManager.getCurrentPlayer();
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
        assertAll(
                () -> assertEquals("1:0", gameManager.getCurrentPhaseString()),
                () -> assertEquals(expectedPlayer, gameManager.getCurrentPlayer())
        );
    }

    @Test
    @DisplayName("Test executePlayerAction for skipping change phase if character card played")
    void executePlayerAction_characterCardSkipChangePhase() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ActivateCharacterCard("knight", new Knight()));
        JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
        List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
        gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
        assertAll(
                () -> assertEquals("1:0", gameManager.getCurrentPhaseString()),
                () -> assertThrowsExactly(GameManager.GameManagerException.class,
                        () -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ActivateCharacterCard("knight", new Knight()))),
                () -> assertDoesNotThrow(() -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                        new MoveStudents(currentPlayerEntranceTokens.subList(0, 3), Collections.emptyMap())))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction for repeating macro phase with next player and enabling character card for previous player")
    void executePlayerAction_repeatMacroPhaseAndEnableCharacterForPrevious() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ActivateCharacterCard("knight", new Knight()));
        JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
        List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
        gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                new MoveStudents(currentPlayerEntranceTokens.subList(0, 3), Collections.emptyMap()));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(1));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(0));
        assertAll(
                () -> assertEquals("1:0", gameManager.getCurrentPhaseString()),
                () -> assertEquals(gameManager.getPlayersOrder().get(1), gameManager.getCurrentPlayer()),
                () -> assertTrue(gameManager.getPlayersEnabledCharacterCard().get(gameManager.getPlayersOrder().get(0)))
        );
    }

    @Test
    @DisplayName("Test executePlayerAction for restarting from first macro phase with same player as order winner")
    void executePlayerAction_restartFromFirstMacroPhase() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
        gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
        List<String> newPlayersOrder = gameManager.getPlayersOrder();
        JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
        for (int i = 0; i < 3; i++) {
            List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
            gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                    .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                    new MoveStudents(currentPlayerEntranceTokens.subList(0, 3), Collections.emptyMap()));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(1));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(i));
        }
        assertAll(
                () -> assertEquals("0:0", gameManager.getCurrentPhaseString()),
                () -> assertEquals(newPlayersOrder.get(0), gameManager.getCurrentPlayer())
        );
    }

    @Test
    @DisplayName("Test executePlayerAction for all towers endgame")
    void executePlayerAction_allTowersEndgame() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        for (int i = 0; i < 3; i++) {
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
            for (int j = 0; j < 3; j++) {
                JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
                gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                        .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
                if (gameManager.getCurrentPlayer().equals("Roy Batty")) {
                    gameManager.executePlayerAction("Roy Batty", new ActivateCharacterCard("knight", new Knight()));
                }
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                        new MoveStudents(Collections.emptyList(), Collections.singletonMap(0, currentPlayerEntranceTokens.subList(0, 3))));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(1));
                if (!gameManager.getWinners().isEmpty()) {
                    JsonNode gameEngineFinalTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                    assertAll(
                            () -> assertEquals(Endgame.ALL_TOWERS, gameManager.getEndgame()),
                            () -> assertEquals(0, gameEngineFinalTree.at("/gameState/playerDashboards/Roy Batty/towers/value1").intValue()),
                            () -> assertEquals(Collections.singletonList("Roy Batty"), gameManager.getWinners())
                    );
                    return;
                }
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(j));
            }
        }
        fail("Endgame not reached");
    }

    @Test
    @DisplayName("Test executePlayerAction for few islands endgame")
    void executePlayerAction_fewIslandsEndgame() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        for (int i = 0; i < 2; i++) {
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
            for (int j = 0; j < 3; j++) {
                JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
                gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                        .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                        new MoveStudents(Collections.emptyList(), Collections.singletonMap(0, currentPlayerEntranceTokens.subList(0, 3))));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ActivateCharacterCard("knight", new Knight()));
                if (i < 1) {
                    String player = gameManager.getCurrentPlayer();
                    gameManager.executePlayerAction(player, new MoveMotherNature(2));
                } else {
                    gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(
                            gameManager.getCurrentPlayer().equals(gameManager.getPlayersOrder().get(0)) ? 5 : 2));
                }
                if (!gameManager.getWinners().isEmpty()) {
                    JsonNode gameEngineFinalTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                    assertAll(
                            () -> assertEquals(Endgame.FEW_ISLANDS, gameManager.getEndgame()),
                            () -> assertEquals(3, gameEngineFinalTree.at("/gameState/archipelago/islands").size()),
                            () -> assertEquals(1, gameEngineFinalTree.at("/gameState/playerDashboards/Rick Deckard/towers/value1").intValue()),
                            () -> assertEquals(1, gameEngineFinalTree.at("/gameState/playerDashboards/Roy Batty/towers/value1").intValue()),
                            () -> assertEquals(1, gameEngineFinalTree.at("/gameState/playerDashboards/Rachael/towers/value1").intValue()),
                            () -> assertTrue(Stream.of("Rick Deckard", "Roy Batty", "Rachael").allMatch(gameManager.getWinners()::contains))
                    );
                    return;
                }
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(j));
            }
        }
        fail("Endgame not reached");
    }

    @Test
    @DisplayName("Test executePlayerAction for no assistants endgame")
    void executePlayerAction_noAssistantsEndgame() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        for (int i = 0; i < 6; i++) {
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
            if (i < 3) {
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("octopus"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("fox"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("snake"));
            }
            if (i > 2) {
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
            }
            for (int j = 0; j < 3; j++) {
                JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
                gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                        .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
                if (gameManager.getCurrentPlayer().equals("Rick Deckard") && i == 0) {
                    gameManager.executePlayerAction("Rick Deckard", new ActivateCharacterCard("knight", new Knight()));
                }
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                        new MoveStudents(Collections.emptyList(), Collections.singletonMap(0, currentPlayerEntranceTokens.subList(0, 3))));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(1));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(j));
                if (!gameManager.getWinners().isEmpty()) {
                    JsonNode gameEngineFinalTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                    assertAll(
                            () -> assertEquals(Endgame.NO_ASSISTANTS, gameManager.getEndgame()),
                            () -> assertEquals(0, gameEngineFinalTree.at("/gameState/playerDashboards/Rick Deckard/assistantCards").size()),
                            () -> assertEquals(0, gameEngineFinalTree.at("/gameState/playerDashboards/Roy Batty/assistantCards").size()),
                            () -> assertEquals(0, gameEngineFinalTree.at("/gameState/playerDashboards/Rachael/assistantCards").size()),
                            () -> assertEquals(Collections.singletonList("Rick Deckard"), gameManager.getWinners())
                    );
                    return;
                }
            }
        }
        fail("Endgame not reached");
    }

    @Test
    @DisplayName("Test executePlayerAction for empty bag endgame")
    void executePlayerAction_emptyBagEndgame() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        for (int i = 0; i < 5; i++) {
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
            if (i < 3) {
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("octopus"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("fox"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("snake"));
            }
            if (i > 2) {
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
            }
            for (int j = 0; j < 3; j++) {
                JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
                gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                        .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                        new MoveStudents(Collections.emptyList(), Collections.singletonMap(0, currentPlayerEntranceTokens.subList(0, 3))));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(1));
                if (i < 4) {
                    List<TokenColor> currentMonkTokens = new ArrayList<>();
                    gameEngineTree.at("/characterCards/monk/tokenContainer/tokens")
                            .forEach(colorNode -> currentMonkTokens.add(TokenColor.valueOf(colorNode.textValue())));
                    gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ActivateCharacterCard("monk",
                            new Monk(Collections.singletonList(currentMonkTokens.get(0)), 0)));
                }
                if (i == 4 && gameManager.getCurrentPlayer().equals(gameManager.getPlayersOrder().get(2))) {
                    List<TokenColor> currentQueenTokens = new ArrayList<>();
                    gameEngineTree.at("/characterCards/queen/tokenContainer/tokens")
                            .forEach(colorNode -> currentQueenTokens.add(TokenColor.valueOf(colorNode.textValue())));
                    gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                            new ActivateCharacterCard("queen", new Queen(Collections.singletonList(currentQueenTokens.get(0)))));
                }
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(j));
                if (!gameManager.getWinners().isEmpty()) {
                    JsonNode gameEngineFinalTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                    assertAll(
                            () -> assertEquals(Endgame.EMPTY_BAG, gameManager.getEndgame()),
                            () -> assertEquals(0, gameEngineFinalTree.at("/gameState/studentsBag/tokenContainer/tokens").size()),
                            () -> assertEquals(Collections.singletonList(gameManager.getPlayersOrder().get(2)), gameManager.getWinners())
                    );
                    return;
                }
            }
        }
        fail("Endgame not reached");
    }

    @Test
    @DisplayName("Test executePlayerAction with game already ended")
    void executePlayerAction_gameAlreadyEnded() throws GameManager.GameManagerException, JsonProcessingException {
        gameManager.addPlayer("Roy Batty", new Pair<>(WizardType.KING, TowerColor.WHITE));
        gameManager.addPlayer("Rachael", new Pair<>(WizardType.WITCH, TowerColor.GRAY));
        gameManager.initializeGame();
        for (int i = 0; i < 3; i++) {
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new FillClouds());
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("turtle"));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("dog"));
            gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new PlayAssistantCard("elephant"));
            for (int j = 0; j < 3; j++) {
                JsonNode gameEngineTree = jsonMapper.readTree(gameManager.getGameEngineJSON());
                List<TokenColor> currentPlayerEntranceTokens = new ArrayList<>();
                gameEngineTree.at(String.format("/gameState/playerDashboards/%s/entrance/tokens", gameManager.getCurrentPlayer()))
                        .forEach(colorNode -> currentPlayerEntranceTokens.add(TokenColor.valueOf(colorNode.textValue())));
                if (gameManager.getCurrentPlayer().equals("Roy Batty")) {
                    gameManager.executePlayerAction("Roy Batty", new ActivateCharacterCard("knight", new Knight()));
                }
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(),
                        new MoveStudents(Collections.emptyList(), Collections.singletonMap(0, currentPlayerEntranceTokens.subList(0, 3))));
                gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new MoveMotherNature(1));
                if (!gameManager.getWinners().isEmpty()) {
                    int finalJ = j;
                    assertThrowsExactly(GameManager.GameManagerException.class, () -> gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(finalJ)));
                    return;
                } else {
                    gameManager.executePlayerAction(gameManager.getCurrentPlayer(), new ChooseCloud(j));
                }
            }
        }
    }
}