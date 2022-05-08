package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the cli printer for game information.
 */
public class CLIGamePrinter {
    /**
     * CLI game printer singleton instance.
     */
    private static CLIGamePrinter singleton = null;
    /**
     * Client directives dispatcher instance.
     */
    private final DirectivesParser directivesParser;

    /**
     * Private constructor for utility class.
     *
     * @param directivesParser client directives dispatcher instance
     */
    private CLIGamePrinter(DirectivesParser directivesParser) {
        this.directivesParser = directivesParser;
    }

    /**
     * Get cli game printer singleton instance.
     *
     * @param directivesParser client directives dispatcher instance
     * @return singleton instance
     */
    public static CLIGamePrinter getInstance(DirectivesParser directivesParser) {
        if (singleton == null)
            singleton = new CLIGamePrinter(directivesParser);
        return singleton;
    }

    /**
     * Print the game information.
     *
     * @param username player username
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    public void printGame(String username) throws DirectivesParser.DirectivesParserException {
        this.printGamePhase();
        this.printPlayersOrder(username);
        this.printCloud();
        this.printCharacterCard();
    }

    /**
     * Print current game phase information.
     */
    private void printGamePhase() {
        Pair<String, String> currentPhase = this.directivesParser.getCurrentPhase();
        System.out.printf("%sCurrent phase:%s %s => %s%s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_BRIGHT_WHITE,
                currentPhase.getKey(), currentPhase.getValue(), CLIConstants.ANSI_RESET);
    }

    /**
     * Print players order and current player information.
     *
     * @param username player username
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printPlayersOrder(String username) throws DirectivesParser.DirectivesParserException {
        System.out.printf("%sPlayers order: ", CLIConstants.ANSI_BRIGHT_BLUE);
        String currentPlayer = this.directivesParser.getCurrentPlayer();
        List<String> coloredPlayersOrder = new ArrayList<>();
        this.directivesParser.getPlayersOrder().forEach(player -> coloredPlayersOrder.add(
                String.format("%s%s%s", player.equals(currentPlayer) ? CLIConstants.ANSI_BRIGHT_GREEN
                        : CLIConstants.ANSI_BRIGHT_WHITE, player, CLIConstants.ANSI_RESET)));
        System.out.printf("%s (You are %s)%n", String.join(", ", coloredPlayersOrder), username);
    }

    private void printCloud() throws DirectivesParser.DirectivesParserException {
        System.out.printf("%sClouds:%s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
        int cloud = this.directivesParser.getCloudsSize();
        for (int i = 0; i < cloud; i++) {
            System.out.printf("%s - %s%n", i + 1, this.directivesParser.getCloudTokens(i));
        }
    }

    private void printCharacterCard() throws DirectivesParser.DirectivesParserException {
        if (this.directivesParser.isExpertMode()) {
            System.out.printf("%sCharacter card:%s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
            for (String name : this.directivesParser.getCharacterCardNames()){
                        System.out.printf("%s%n - cost: %s%n",
                                name,
                                this.directivesParser.getCharacterCardCost(name).getKey() + this.directivesParser.getCharacterCardCost(name).getValue(),
                                this.directivesParser.getCharacterCardMultipurposeCounter(name));
                                if (this.directivesParser.getCharacterCardTokens(name).size() > 0) {
                                    System.out.printf("- tokens: %s%n", this.directivesParser.getCharacterCardTokens(name));
                        };
                    }
                }

            };
        }

