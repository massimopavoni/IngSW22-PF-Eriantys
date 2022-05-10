package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;

import java.util.Arrays;
import java.util.List;

/**
 * Class representing the cli actions executor for playing the game.
 */
public class CLIActionsExecutor {
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
     * List available actions for current phase.
     *
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    public void listAvailableActions() throws DirectivesParser.DirectivesParserException {
        System.out.println("> Choose an action:");
        List<String> availableActions = this.directivesParser.getAvailableActions();
        String actionName;
        List<String> actionNameArgs;
        for (int i = 0; i < availableActions.size(); i++) {
            actionName = availableActions.get(i);
            actionNameArgs = Arrays.stream(actionName.split("(?=\\p{Lu})")).toList();
            actionName = actionNameArgs.get(0) + actionNameArgs.stream().skip(1).map(s -> " " + s).reduce("", String::concat);
            System.out.printf("- %s%s [%d]%s%n", CLIConstants.ANSI_BRIGHT_WHITE, actionName, i + 1, CLIConstants.ANSI_RESET);
        }
        System.out.print("> ");
    }
}
