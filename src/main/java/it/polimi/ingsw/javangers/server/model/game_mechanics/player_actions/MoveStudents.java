package it.polimi.ingsw.javangers.server.model.game_mechanics.player_actions;

import it.polimi.ingsw.javangers.server.model.game_data.Archipelago;
import it.polimi.ingsw.javangers.server.model.game_data.GameState;
import it.polimi.ingsw.javangers.server.model.game_data.enums.TokenColor;
import it.polimi.ingsw.javangers.server.model.game_data.token_containers.TokenContainer;

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
     * @param studentsToHall students to move in the hall
     * @param studentsToIslands students to move on the islands
     */
    public MoveStudents(List<TokenColor> studentsToHall, Map<Integer, List<TokenColor>> studentsToIslands) {
        this.studentsToHall = studentsToHall;
        this.studentsToIslands = studentsToIslands;
    }

    /**
     * Action implementation to move students.
     *
     * @param gameState game state instance
     * @param username  player username
     */
    @Override
    public void doAction(GameState gameState, String username) {
        TokenContainer entrance = gameState.getPlayerDashboards().get(username).getEntrance();
        List<TokenColor> tokens = new ArrayList<>(this.studentsToHall);
        this.studentsToIslands.values().forEach(tokens::addAll);
        if (!entrance.containsSubList(tokens)) throw new IllegalStateException("Tokens not present in entrance");
        TokenContainer hall = gameState.getPlayerDashboards().get(username).getHall();
        hall.addTokens(entrance.extractTokens(this.studentsToHall));
        Archipelago archipelago = gameState.getArchipelago();
        for (Map.Entry<Integer, List<TokenColor>> entry : this.studentsToIslands.entrySet()) {
            archipelago.getIslands().get(entry.getKey()).getTokenContainer().addTokens(entrance.extractTokens(entry.getValue()));
        }
    }
}
