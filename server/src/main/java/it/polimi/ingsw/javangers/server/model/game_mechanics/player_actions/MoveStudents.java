package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.javangers.server.model.game_data.PlayerDashboard;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.Island;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;
import it.polimi.ingsw.javangers.server.model.game_mechanics.core.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class representing the move students action.
 */
public class MoveStudents implements ActionStrategy {
    /**
     * List of students to move in hall.
     */
    private final List<TokenColor> studentsToHall;
    /**
     * List of students to move on islands.
     */
    private final Map<Integer, List<TokenColor>> studentsToIslands;

    /**
     * Constructor for move students action, initializing students to hall
     * and students to islands.
     *
     * @param studentsToHall    students to move in the hall
     * @param studentsToIslands students to move on the islands
     */
    @JsonCreator
    public MoveStudents(@JsonProperty("studentsToHall") List<TokenColor> studentsToHall, @JsonProperty("studentsToIslands") Map<Integer, List<TokenColor>> studentsToIslands) {
        this.studentsToHall = studentsToHall;
        this.studentsToIslands = studentsToIslands;
    }

    /**
     * Action implementation to move students.
     *
     * @param gameEngine game engine instance
     * @param username   player username
     */
    @Override
    public void doAction(GameEngine gameEngine, String username) {
        PlayerDashboard playerDashboard = gameEngine.getGameState().getPlayerDashboards().get(username);
        TokenContainer hall = playerDashboard.getHall();
        TokenContainer entrance = playerDashboard.getEntrance();
        List<TokenColor> tokensFromEntrance = new ArrayList<>(this.studentsToHall);
        this.studentsToIslands.values().forEach(tokensFromEntrance::addAll);
        if (tokensFromEntrance.size() != gameEngine.getGameConfiguration().studentsPerCloud())
            throw new IllegalStateException("Students to move must be equal to students per cloud");
        if (!entrance.containsSubList(tokensFromEntrance))
            throw new IllegalStateException("Specified tokens not present in entrance");
        int oldHallCoins = hall.getColorCounts().values().stream().mapToInt(c -> c / 3).sum();
        hall.addTokens(entrance.extractTokens(this.studentsToHall));
        if (gameEngine.isExpertMode())
            playerDashboard.setCoinsNumber(playerDashboard.getCoinsNumber() +
                    hall.getColorCounts().values().stream().mapToInt(c -> c / 3).sum() - oldHallCoins);
        List<Island> islands = gameEngine.getGameState().getArchipelago().getIslands();
        this.studentsToIslands.forEach((islandIndex, tokensToIsland) ->
                islands.get(islandIndex).getTokenContainer().addTokens(entrance.extractTokens(tokensToIsland)));
        gameEngine.changeTeachersPower(username);
    }


}
