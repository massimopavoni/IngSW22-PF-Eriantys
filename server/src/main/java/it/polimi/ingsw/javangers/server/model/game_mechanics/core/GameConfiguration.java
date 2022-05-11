package it.polimi.ingsw.javangers.server.model.game_mechanics.core;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class representing a game configuration.
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
public record GameConfiguration(@JsonProperty("assistantCardsResourceLocation") String assistantCardsResourceLocation,
                                @JsonProperty("characterCardsResourceLocation") String characterCardsResourceLocation,
                                @JsonProperty("numberOfIslands") int numberOfIslands,
                                @JsonProperty("studentsPerColor") int studentsPerColor,
                                @JsonProperty("studentsPerCloud") int studentsPerCloud,
                                @JsonProperty("studentsPerEntrance") int studentsPerEntrance,
                                @JsonProperty("towersPerDashboard") int towersPerDashboard,
                                @JsonProperty("numberOfCharacterCards") int numberOfCharacterCards,
                                @JsonProperty("coinsPerDashBoard") int coinsPerDashBoard) {
}
