package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Class representing the cli actions executor for playing the game.
 */
public class CLIActionsExecutor {
    /**
     * The scanner for user input.
     */
    private static final Scanner input = new Scanner(System.in);
    /**
     * CLI actions executor singleton instance.
     */
    private static CLIActionsExecutor singleton = null;
    /**
     * Client directives dispatcher instance.
     */
    private final DirectivesDispatcher directivesDispatcher;
    /**
     * Client directives parser instance.
     */
    private final DirectivesParser directivesParser;

    /**
     * Private constructor for utility class.
     *
     * @param directivesDispatcher client directives dispatcher instance
     * @param directivesParser     client directives parser instance
     */
    private CLIActionsExecutor(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        this.directivesDispatcher = directivesDispatcher;
        this.directivesParser = directivesParser;
    }

    /**
     * Get cli actions executor singleton instance.
     *
     * @param directivesDispatcher client directives dispatcher instance
     * @param directivesParser     client directives parser instance
     * @return singleton instance
     */
    public static CLIActionsExecutor getInstance(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        if (singleton == null)
            singleton = new CLIActionsExecutor(directivesDispatcher, directivesParser);
        return singleton;
    }

    /**
     * Choose an action from the available ones for current phase.
     *
     * @param username player username
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    public void chooseAction(String username) throws DirectivesParser.DirectivesParserException {
        List<String> availableActions = this.directivesParser.getAvailableActions();
        if (!this.directivesParser.getPlayersEnabledCharacterCard().get(username))
            availableActions.remove("ActivateCharacterCard");
        List<String> actionNameArgs;
        int chosenAction = -1;
        String chosenActionString;
        System.out.printf("%n> Choose an action:");
        while (chosenAction < 0 || chosenAction >= availableActions.size()) {
            for (int i = 0; i < availableActions.size(); i++) {
                actionNameArgs = Arrays.stream(availableActions.get(i).split("(?=\\p{Lu})")).toList();
                System.out.printf("- %s%s [%d]%s%n", CLIConstants.ANSI_BRIGHT_WHITE,
                        actionNameArgs.stream().skip(1).map(s -> " " + s).reduce(actionNameArgs.get(0), String::concat),
                        i + 1, CLIConstants.ANSI_RESET);
            }
            System.out.print("> ");
            try {
                chosenActionString = input.nextLine().strip();
                chosenAction = Integer.parseInt(chosenActionString) - 1;
                if (chosenAction < 0 || chosenAction >= availableActions.size())
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("> Invalid input, choose an action:");
            }
        }
    }
}
