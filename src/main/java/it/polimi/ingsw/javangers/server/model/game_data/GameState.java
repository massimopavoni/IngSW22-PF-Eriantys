package it.polimi.ingsw.javangers.server.model.game_data;

import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TowerColor;
import it.polimi.ingsw.javangers.server.model.game_data.enums.WizardType;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Cloud;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.StudentsBag;
import org.javatuples.Pair;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * Class representing the state of the game.
 */
public class GameState {
    /**
     * Map of players' usernames and dashboards.
     */
    private final Map<String, PlayerDashboard> playerDashboardsMap;
    /**
     * Map of teachers' color and info.
     */
    private final Map<TokenColor, Teacher> teachersMap;
    /**
     * Game archipelago instance.
     */
    private final Archipelago archipelago;
    /**
     * Game students bag instance.
     */
    private final StudentsBag studentsBag;
    /**
     * List of game clouds.
     */
    private final List<Cloud> cloudsList;

    /**
     * Constructor for game state, defining assistant card resource location, initial number of towers, islands, students tokens and coins;
     * initializing player dashboards, teachers, archipelago, students bag and clouds.
     *
     * @param assistantCardResourceLocation path to the assistant card resource file
     * @param playerInfo map of players' usernames and dashboards
     * @param numberOfTowers initial number of towers
     * @param numberOfIslands initial number of islands
     * @param studentsBagInfo map of students color and number
     * @param coinsNumber initial number of coins
     * @throws GameStateException if an error occurs during player dashboards initialization
     */
    public GameState(String assistantCardResourceLocation, Map<String, Pair<WizardType, TowerColor>> playerInfo,
                     int numberOfTowers, int numberOfIslands, Map<TokenColor, Integer> studentsBagInfo, int coinsNumber) throws GameStateException {
        this.playerDashboardsMap = new HashMap<>();
        try {
            for (Map.Entry<String, Pair<WizardType, TowerColor>> entry : playerInfo.entrySet()) {
                this.playerDashboardsMap.put(entry.getKey(),
                        new PlayerDashboard(assistantCardResourceLocation, entry.getValue().getValue0(),
                                new Pair<>(entry.getValue().getValue1(), numberOfTowers), coinsNumber));
            }
        } catch (IOException e) {
            throw new GameStateException("Error while creating player dashboards", e);
        }
        this.teachersMap = new EnumMap<>(TokenColor.class);
        for (TokenColor color : TokenColor.values()) {
            this.teachersMap.put(color, new Teacher("", 0));
        }
        this.archipelago = new Archipelago(numberOfIslands);
        this.studentsBag = new StudentsBag(studentsBagInfo);
        this.cloudsList = new ArrayList<>();
        this.cloudsList.addAll(Stream.generate(Cloud::new).limit(playerInfo.size()).collect(java.util.stream.Collectors.toList()));
    }


    /**
     * Get player dashboards map.
     *
     * @return player dashboards map
     */
    public Map<String, PlayerDashboard> getPlayerDashboards() {
        return new HashMap<>(this.playerDashboardsMap);
    }

    /**
     * Get teacher map.
     *
     * @return teacher map
     */
    public Map<TokenColor, Teacher> getTeachers() {
        return new EnumMap<>(this.teachersMap);
    }

    /**
     * Get archipelago instance.
     *
     * @return archipelago instance
     */
    public Archipelago getArchipelago() {
        return this.archipelago;
    }

    /**
     * Get students bag instance.
     *
     * @return students bag instance
     */
    public StudentsBag getStudentsBag() {
        return this.studentsBag;
    }

    /**
     * Get clouds list.
     *
     * @return clouds list
     */
    public List<Cloud> getCloudsList() {
        return new ArrayList<>(this.cloudsList);
    }

    /**
     * Exception for errors within game state class.
     */
    public static class GameStateException extends Exception {
        /**
         * GameStateException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause cause of the exception
         */
        public GameStateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
