package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import it.polimi.ingsw.javangers.server.model.game_data.Archipelago;
import it.polimi.ingsw.javangers.server.model.game_data.AssistantCard;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.Teacher;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import it.polimi.ingsw.javangers.server.model.game_mechanics.CharacterCard;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    GameEngine gameEngine;

    @BeforeEach
    void setUp() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "test_loadAllCharacterCards",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Neo", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Trinity", new Pair<>(WizardType.DRUID, TowerColor.WHITE));
                }}, true);
    }

    @Test
    @DisplayName("Test constructor for invalid game configurations file path")
    void GameEngine_invalidGameConfigurationsFilePath() {
        assertThrowsExactly(GameEngine.GameEngineException.class,
                () -> new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/GameConfiguration.class",
                        "2_players",
                        new HashMap<String, Pair<WizardType, TowerColor>>() {{
                            put("Neo", new Pair<>(WizardType.KING, TowerColor.BLACK));
                            put("Trinity", new Pair<>(WizardType.DRUID, TowerColor.WHITE));
                        }}, true));
    }

    @Test
    @DisplayName("Test constructor for invalid configuration assistant cards file path")
    void GameEngine_invalidAssistantCardsFilePath() {
        assertThrowsExactly(GameEngine.GameEngineException.class,
                () -> new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                        "test_invalidAssistantCardsResourceLocation",
                        new HashMap<String, Pair<WizardType, TowerColor>>() {{
                            put("Neo", new Pair<>(WizardType.KING, TowerColor.BLACK));
                            put("Trinity", new Pair<>(WizardType.DRUID, TowerColor.WHITE));
                        }}, true));
    }

    @Test
    @DisplayName("Test constructor for invalid configuration character cards file path")
    void GameEngine_invalidCharacterCardsFilePath() {
        assertThrowsExactly(GameEngine.GameEngineException.class,
                () -> new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                        "test_invalidCharacterCardsResourceLocation",
                        new HashMap<String, Pair<WizardType, TowerColor>>() {{
                            put("Neo", new Pair<>(WizardType.KING, TowerColor.BLACK));
                            put("Trinity", new Pair<>(WizardType.DRUID, TowerColor.WHITE));
                        }}, true));
    }

    @Test
    @DisplayName("Test constructor for correct initialization")
    void GameEngine_correctConstructor() {
        assertAll(
                () -> assertEquals(3, gameEngine.getGameConfiguration().getStudentsPerCloud()),
                () -> assertEquals(1, gameEngine.getGameState().getPlayerDashboards().get("Neo").getCoinsNumber()),
                () -> assertEquals(12, gameEngine.getCharacterCards().size()),
                () -> assertTrue(gameEngine.isExpertMode()),
                () -> assertFalse(gameEngine.getTeachersEqualCount()),
                () -> assertEquals(0, gameEngine.getAdditionalMotherNatureSteps()),
                () -> assertTrue(gameEngine.getEnabledIslandTowers()),
                () -> assertEquals(0, gameEngine.getAdditionalPower()),
                () -> assertNull(gameEngine.getForbiddenColor())
        );
    }

    @Test
    @DisplayName("Test getCharacterCards for shallow copy")
    void getCharacterCards_shallowCopy() {
        Map<String, CharacterCard> characterCardsCopy = gameEngine.getCharacterCards();
        String[] characterCardsNames = characterCardsCopy.keySet().toArray(new String[0]);
        characterCardsCopy.remove(characterCardsNames[0]);
        characterCardsCopy.get(characterCardsNames[1]).setCostDelta(1);
        assertAll(
                () -> assertEquals(12, gameEngine.getCharacterCards().size()),
                () -> assertEquals(1, gameEngine.getCharacterCards()
                        .get(characterCardsNames[1]).getCostDelta())
        );
    }

    @Test
    @DisplayName("Test setTeachersEqualAmount for correct value")
    void setTeachersEqualCount_correctValue() {
        gameEngine.setTeachersEqualCount(true);
        assertTrue(gameEngine.getTeachersEqualCount());
    }

    @Test
    @DisplayName("Test setAdditionalMotherNatureSteps for correct value")
    void setAdditionalMotherNatureSteps_correctValue() {
        gameEngine.setAdditionalMotherNatureSteps(2);
        assertEquals(2, gameEngine.getAdditionalMotherNatureSteps());
    }

    @Test
    @DisplayName("Test setEnabledIslandTowers for correct value")
    void setEnabledIslandTowers_correctValue() {
        gameEngine.setEnabledIslandTowers(false);
        assertFalse(gameEngine.getEnabledIslandTowers());
    }

    @Test
    @DisplayName("Test setAdditionalPower for correct value")
    void setAdditionalPower_correctValue() {
        gameEngine.setAdditionalPower(2);
        assertEquals(2, gameEngine.getAdditionalPower());
    }

    @Test
    @DisplayName("Test setForbiddenColor for correct value")
    void setForbiddenColor_correctValue() {
        gameEngine.setForbiddenColor(TokenColor.YELLOW_ELF);
        assertEquals(TokenColor.YELLOW_ELF, gameEngine.getForbiddenColor());
    }

    @Test
    @DisplayName("Test initializeGame with 12 islands for correct initialization based on game configuration and game rules")
    void initializeGame_correctInitialization12Islands() {
        gameEngine.initializeGame();
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        List<Island> allIslands = archipelago.getIslands();
        List<Island> voidIslands = Arrays.asList(allIslands.get(archipelago.getMotherNaturePosition()),
                allIslands.get((archipelago.getMotherNaturePosition() + allIslands.size() / 2) % allIslands.size()));
        List<Island> islandsWithTokens = archipelago.getIslands().stream()
                .filter(island -> !voidIslands.contains(island)).collect(Collectors.toList());
        assertAll(
                () -> voidIslands.forEach(island -> assertEquals(0, island.getTokenContainer().getTokens().size())),
                () -> islandsWithTokens.forEach(island -> assertEquals(1, island.getTokenContainer().getTokens().size())),
                () -> gameEngine.getGameState().getPlayerDashboards().values()
                        .forEach(dashboard -> assertEquals(7, dashboard.getEntrance().getTokens().size())),
                () -> assertEquals(4, gameEngine.getCharacterCards().get("queen").getTokenContainer().getTokens().size())
        );
    }

    @Test
    @DisplayName("Test initializeGame with 6 islands for correct initialization based on game configuration and game rules")
    void initializeGame_correctInitialization6Islands() throws GameEngine.GameEngineException {
        gameEngine = new GameEngine("/it/polimi/ingsw/javangers/server/model/game_mechanics/core/test_game_configurations.json",
                "3_players",
                new HashMap<String, Pair<WizardType, TowerColor>>() {{
                    put("Neo", new Pair<>(WizardType.KING, TowerColor.BLACK));
                    put("Trinity", new Pair<>(WizardType.DRUID, TowerColor.GRAY));
                    put("Morpheus", new Pair<>(WizardType.SENSEI, TowerColor.WHITE));
                }}, false);
        gameEngine.initializeGame();
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        List<Island> allIslands = archipelago.getIslands();
        List<Island> voidIslands = Arrays.asList(allIslands.get(archipelago.getMotherNaturePosition()),
                allIslands.get((archipelago.getMotherNaturePosition() + allIslands.size() / 2) % allIslands.size()));
        assertAll(
                () -> voidIslands.forEach(island -> assertEquals(0, island.getTokenContainer().getTokens().size())),
                () -> assertEquals(3, allIslands.stream().filter(island -> island.getTokenContainer().getTokens().size() == 1).count()),
                () -> assertEquals(1, allIslands.stream().filter(island -> island.getTokenContainer().getTokens().size() == 2).count()),
                () -> gameEngine.getGameState().getPlayerDashboards().values()
                        .forEach(dashboard -> assertEquals(9, dashboard.getEntrance().getTokens().size()))
        );
    }

    @Test
    @DisplayName("Test resetCharacterCardsParameters for correct reset")
    void resetCharacterCardsParameters_correctReset() {
        gameEngine.setTeachersEqualCount(true);
        gameEngine.setAdditionalMotherNatureSteps(2);
        gameEngine.setEnabledIslandTowers(false);
        gameEngine.setAdditionalPower(2);
        gameEngine.setForbiddenColor(TokenColor.YELLOW_ELF);
        gameEngine.resetCharacterCardsParameters();
        assertAll(
                () -> assertFalse(gameEngine.getTeachersEqualCount()),
                () -> assertEquals(0, gameEngine.getAdditionalMotherNatureSteps()),
                () -> assertTrue(gameEngine.getEnabledIslandTowers()),
                () -> assertEquals(0, gameEngine.getAdditionalPower()),
                () -> assertNull(gameEngine.getForbiddenColor())
        );
    }

    @Test
    @DisplayName("Test changeTeachersPower for updating current owner data on teacher")
    void changeTeachersPower_updateCurrentOwnerInTeacher() {
        PlayerDashboard neoDashboard = gameEngine.getGameState().getPlayerDashboards().get("Neo");
        neoDashboard.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        neoDashboard.getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.changeTeachersPower("Neo");
        assertAll(
                () -> assertEquals("Neo", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(3, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test changeTeachersPower for leaving current owner after others' changes")
    void changeTeachersPower_leaveUnchanged() {
        PlayerDashboard neoDashboard = gameEngine.getGameState().getPlayerDashboards().get("Neo");
        PlayerDashboard trinityDashboard = gameEngine.getGameState().getPlayerDashboards().get("Trinity");
        neoDashboard.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        trinityDashboard.getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.changeTeachersPower("Neo");
        assertAll(
                () -> assertEquals("Neo", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(2, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test changeTeachersPower for adding owner to teacher")
    void changeTeachersPower_addOwner() {
        PlayerDashboard neoDashboard = gameEngine.getGameState().getPlayerDashboards().get("Neo");
        neoDashboard.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        gameEngine.changeTeachersPower("Neo");
        assertAll(
                () -> assertEquals("Neo", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(2, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test changeTeachersPower for changing owner without equal count")
    void changeTeachersPower_changeOwnerDisabledEqualCount() {
        PlayerDashboard neoDashboard = gameEngine.getGameState().getPlayerDashboards().get("Neo");
        PlayerDashboard trinityDashboard = gameEngine.getGameState().getPlayerDashboards().get("Trinity");
        neoDashboard.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        trinityDashboard.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        gameEngine.changeTeachersPower("Neo");
        assertAll(
                () -> assertEquals("Neo", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(2, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber()),
                () -> trinityDashboard.getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON)),
                () -> gameEngine.changeTeachersPower("Neo"),
                () -> assertEquals("Trinity", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(3, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test changeTeachersPower for changing owner with equal count")
    void changeTeachersPower_changeOwnerEnabledEqualCount() {
        PlayerDashboard neoDashboard = gameEngine.getGameState().getPlayerDashboards().get("Neo");
        PlayerDashboard trinityDashboard = gameEngine.getGameState().getPlayerDashboards().get("Trinity");
        trinityDashboard.getHall().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON));
        gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).setOwner("Trinity", 2);
        neoDashboard.getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.setTeachersEqualCount(true);
        gameEngine.changeTeachersPower("Neo");
        assertAll(
                () -> assertEquals("Trinity", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(2, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber()),
                () -> neoDashboard.getHall().addTokens(Collections.singletonList(TokenColor.RED_DRAGON)),
                () -> gameEngine.changeTeachersPower("Neo"),
                () -> assertEquals("Neo", gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerUsername()),
                () -> assertEquals(2, gameEngine.getGameState().getTeachers().get(TokenColor.RED_DRAGON).getOwnerStudentsNumber())
        );
    }

    @Test
    @DisplayName("Test changeTeachersPower leaving island unchanged for same power")
    void changeIslandPower_leaveUnchangedSamePower() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        gameEngine.changeIslandPower(0, "Trinity");
        assertAll(
                () -> assertEquals(TowerColor.NONE, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(0, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower with disabled island")
    void changeIslandPower_disabledIsland() {
        gameEngine.getGameState().getArchipelago().setMotherNaturePosition(3);
        AssistantCard eagle = gameEngine.getGameState().getPlayerDashboards().get("Neo").getAssistantCards().get("eagle");
        gameEngine.getGameState().getPlayerDashboards().get("Neo").getDiscardedAssistantCards().put("eagle", eagle);
        gameEngine.getGameState().getArchipelago().getIslands().get(2).getTokenContainer().addTokens(new ArrayList<>(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN)));
        gameEngine.getGameState().getTeachers().get(TokenColor.BLUE_UNICORN).setOwner("Neo", 2);
        gameEngine.getGameState().getArchipelago().getIslands().get(3).setEnabled(1);
        gameEngine.getCharacterCards().get("herbalist").setMultipurposeCounter(3);
        gameEngine.changeIslandPower(3, "Neo");
        assertAll(
                () -> assertEquals(TowerColor.NONE, gameEngine.getGameState().getArchipelago().getIslands().get(5).getTowers().getValue0()),
                () -> assertEquals(4, gameEngine.getCharacterCards().get("herbalist").getMultipurposeCounter())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct leave unchanged with tower points")
    void changeIslandPower_leaveUnchangedWithTowerPoints() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(TowerColor.BLACK, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct leave unchanged with forbidden color")
    void changeIslandPower_correctLeaveUnchangedWithForbiddenColor() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        gameEngine.getGameState().getPlayerDashboards().get("Trinity").setTowersNumber(1);
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        gameEngine.setForbiddenColor(TokenColor.BLUE_UNICORN);
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(TowerColor.BLACK, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct new power")
    void changeIslandPower_correctNewPower() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        gameEngine.changeIslandPower(0, "Trinity");
        assertAll(
                () -> assertEquals(TowerColor.BLACK, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct change without tower points")
    void changeIslandPower_correctChangeWithoutTowerPoints() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        gameEngine.setEnabledIslandTowers(false);
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(TowerColor.WHITE, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct change existing power")
    void changeIslandPower_correctChangePower() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.YELLOW_ELF, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(TowerColor.WHITE, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct change with additional power")
    void changeIslandPower_correctChangeWithAdditionalPower() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.RED_DRAGON, TokenColor.BLUE_UNICORN));
        gameEngine.setAdditionalPower(2);
        gameEngine.changeIslandPower(0, "Trinity");
        assertAll(
                () -> assertEquals(TowerColor.WHITE, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct select number of towers from dashboard")
    void changeIslandPower_correctTowersRemaining() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        teachers.get(TokenColor.BLUE_UNICORN).setOwner("Trinity", 2);
        gameEngine.getGameState().getPlayerDashboards().get("Trinity").setTowersNumber(1);
        archipelago.getIslands().get(0).setTowers(new Pair<>(TowerColor.BLACK, 2));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Arrays.asList(TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN, TokenColor.BLUE_UNICORN));
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(TowerColor.WHITE, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(1, archipelago.getIslands().get(0).getTowers().getValue1()),
                () -> assertEquals(0, gameEngine.getGameState().getPlayerDashboards().get("Trinity").getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct island left merge")
    void changeIslandPower_correctIslandLeftMerge() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        archipelago.getIslands().get(11).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(11, archipelago.getIslands().size()),
                () -> assertEquals(TowerColor.BLACK, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(2, archipelago.getIslands().get(0).getTowers().getValue1()),
                () -> assertEquals(TowerColor.NONE, archipelago.getIslands().get(1).getTowers().getValue0()),
                () -> assertEquals(0, archipelago.getIslands().get(1).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct island right merge")
    void changeIslandPower_correctIslandRightMerge() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        archipelago.getIslands().get(1).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(11, archipelago.getIslands().size()),
                () -> assertEquals(TowerColor.BLACK, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(2, archipelago.getIslands().get(0).getTowers().getValue1()),
                () -> assertEquals(TowerColor.NONE, archipelago.getIslands().get(10).getTowers().getValue0()),
                () -> assertEquals(0, archipelago.getIslands().get(10).getTowers().getValue1())
        );
    }

    @Test
    @DisplayName("Test changeIslandPower for correct island left and right merge")
    void changeIslandPower_correctIslandLeftRightMerge() {
        Archipelago archipelago = gameEngine.getGameState().getArchipelago();
        Map<TokenColor, Teacher> teachers = gameEngine.getGameState().getTeachers();
        teachers.get(TokenColor.RED_DRAGON).setOwner("Neo", 2);
        archipelago.getIslands().get(11).setTowers(new Pair<>(TowerColor.BLACK, 1));
        archipelago.getIslands().get(0).getTokenContainer().addTokens(Collections.singletonList(TokenColor.RED_DRAGON));
        archipelago.getIslands().get(1).setTowers(new Pair<>(TowerColor.BLACK, 1));
        gameEngine.changeIslandPower(0, "Neo");
        assertAll(
                () -> assertEquals(10, archipelago.getIslands().size()),
                () -> assertEquals(TowerColor.NONE, archipelago.getIslands().get(9).getTowers().getValue0()),
                () -> assertEquals(0, archipelago.getIslands().get(9).getTowers().getValue1()),
                () -> assertEquals(TowerColor.BLACK, archipelago.getIslands().get(0).getTowers().getValue0()),
                () -> assertEquals(3, archipelago.getIslands().get(0).getTowers().getValue1()),
                () -> assertEquals(TowerColor.NONE, archipelago.getIslands().get(1).getTowers().getValue0()),
                () -> assertEquals(0, archipelago.getIslands().get(1).getTowers().getValue1())
        );
    }
}