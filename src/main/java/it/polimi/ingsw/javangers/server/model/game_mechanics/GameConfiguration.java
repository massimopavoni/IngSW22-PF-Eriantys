package it.polimi.ingsw.javangers.server.model.game_mechanics;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a game configuration.
 */
public class GameConfiguration {
    //--------------------------------------------------------------------------------------------------------------------------------
    //region Attributes
    /**
     * Resource location of assistant cards json file.
     */
    private final String assistantCardsResourceLocation;
    /**
     * Resource location of character cards json file.
     */
    private final String characterCardsResourceLocation;
    /**
     * Initial number of islands.
     */
    private final int numberOfIslands;
    /**
     * Number of students per color.
     */
    private final int studentsPerColor;
    /**
     * Number of students per cloud.
     */
    private final int studentsPerCloud;
    /**
     * Number of students per dashboard entrance.
     */
    private final int studentsPerEntrance;
    /**
     * Number of towers per dashboard.
     */
    private final int towersPerDashboard;
    /**
     * Number of character cards for the game.
     */
    private final int numberOfCharacterCards;
    /**
     * Initial number of coins per dashboard.
     */
    private final int coinsPerDashBoard;
    //endregion

    //--------------------------------------------------------------------------------------------------------------------------------
    //region Constructor, get and set methods

    /**
     * Constructor for game configuration, initializing cards resource locations,
     * students per cloud, students per entrance and towers per dashboard.
     *
     * @param assistantCardsResourceLocation resource location of assistant cards json file
     * @param characterCardsResourceLocation resource location of character cards json file
     * @param numberOfIslands                initial number of islands
     * @param studentsPerColor               number of students per color
     * @param studentsPerCloud               number of students per cloud
     * @param studentsPerEntrance            number of students per dashboard entrance
     * @param towersPerDashboard             number of towers per dashboard
     * @param numberOfCharacterCards         number of character cards for the game
     * @param coinsPerDashBoard              initial number of coins per dashboard
     */
    @JsonCreator
    public GameConfiguration(@JsonProperty("assistantCardsResourceLocation") String assistantCardsResourceLocation,
                             @JsonProperty("characterCardsResourceLocation") String characterCardsResourceLocation,
                             @JsonProperty("numberOfIslands") int numberOfIslands,
                             @JsonProperty("studentsPerColor") int studentsPerColor,
                             @JsonProperty("studentsPerCloud") int studentsPerCloud,
                             @JsonProperty("studentsPerEntrance") int studentsPerEntrance,
                             @JsonProperty("towersPerDashboard") int towersPerDashboard,
                             @JsonProperty("numberOfCharacterCards") int numberOfCharacterCards,
                             @JsonProperty("coinsPerDashBoard") int coinsPerDashBoard) {
        this.assistantCardsResourceLocation = assistantCardsResourceLocation;
        this.characterCardsResourceLocation = characterCardsResourceLocation;
        this.numberOfIslands = numberOfIslands;
        this.studentsPerColor = studentsPerColor;
        this.studentsPerCloud = studentsPerCloud;
        this.studentsPerEntrance = studentsPerEntrance;
        this.towersPerDashboard = towersPerDashboard;
        this.numberOfCharacterCards = numberOfCharacterCards;
        this.coinsPerDashBoard = coinsPerDashBoard;
    }

    /**
     * Get assistant cards resource location.
     *
     * @return assistant cards resource location
     */
    public String getAssistantCardsResourceLocation() {
        return this.assistantCardsResourceLocation;
    }

    /**
     * Get character cards resource location.
     *
     * @return character cards resource location
     */
    public String getCharacterCardsResourceLocation() {
        return this.characterCardsResourceLocation;
    }

    /**
     * Get number of islands.
     *
     * @return number of islands
     */
    public int getNumberOfIslands() {
        return this.numberOfIslands;
    }

    /**
     * Get number of students per color.
     *
     * @return number of students per color
     */
    public int getStudentsPerColor() {
        return this.studentsPerColor;
    }

    /**
     * Get number of students per cloud.
     *
     * @return number of students per cloud
     */
    public int getStudentsPerCloud() {
        return this.studentsPerCloud;
    }

    /**
     * Get number of students per dashboard entrance.
     *
     * @return number of students per dashboard entrance
     */
    public int getStudentsPerEntrance() {
        return this.studentsPerEntrance;
    }

    /**
     * Get number of towers per dashboard.
     *
     * @return number of towers per dashboard
     */
    public int getTowersPerDashboard() {
        return this.towersPerDashboard;
    }


    /**
     * Get number of character cards for the game.
     *
     * @return number of character cards for the game
     */
    public int getNumberOfCharacterCards() {
        return this.numberOfCharacterCards;
    }

    /**
     * Get number of coins per dashboard.
     *
     * @return number of coins per dashboard
     */
    public int getCoinsPerDashBoard() {
        return this.coinsPerDashBoard;
    }
    //endregion
}