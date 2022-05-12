package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class representing the cli actions executor for playing the game.
 */
public class CLIActionsExecutor {
    /**
     * The scanner for user input.
     */
    private static final Scanner input = new Scanner(System.in);
    /**
     * Map for methods corresponding to game actions.
     */
    private static final Map<String, Method> ACTION_METHOD_MAPPINGS;
    /**
     * Map for methods corresponding to character cards effects.
     */
    private static final Map<String, Method> EFFECT_METHOD_MAPPINGS;
    /**
     * Message for requesting a tokens list from the user.
     */
    private static final String TOKENS_LIST_MESSAGE = "> Provide a list of tokens in the form of [y/b/g/r/p][number], " +
            "separated by a whitespace (e.g. y1 r4 p2): ";
    /**
     * Regex for parsing a list of tokens.
     */
    private static final String TOKENS_LIST_REGEX = "([ybgrp])(\\d)";
    /**
     * CLI actions executor singleton instance.
     */
    private static CLIActionsExecutor singleton = null;

    static {
        Map<String, Method> actionMethodMappings = new HashMap<>();
        Map<String, Method> effectMethodMappings = new HashMap<>();
        try {
            actionMethodMappings.put("FillClouds",
                    CLIActionsExecutor.class.getDeclaredMethod("fillClouds", String.class));
            actionMethodMappings.put("PlayAssistantCard",
                    CLIActionsExecutor.class.getDeclaredMethod("playAssistantCard", String.class));
            actionMethodMappings.put("MoveStudents",
                    CLIActionsExecutor.class.getDeclaredMethod("moveStudents", String.class));
            actionMethodMappings.put("MoveMotherNature",
                    CLIActionsExecutor.class.getDeclaredMethod("moveMotherNature", String.class));
            actionMethodMappings.put("ChooseCloud",
                    CLIActionsExecutor.class.getDeclaredMethod("chooseCloud", String.class));
            actionMethodMappings.put("ActivateCharacterCard",
                    CLIActionsExecutor.class.getDeclaredMethod("activateCharacterCard", String.class));

            effectMethodMappings.put("monk",
                    CLIActionsExecutor.class.getDeclaredMethod("monk", String.class));
            effectMethodMappings.put("innkeeper",
                    CLIActionsExecutor.class.getDeclaredMethod("innkeeper", String.class));
            effectMethodMappings.put("herald",
                    CLIActionsExecutor.class.getDeclaredMethod("herald", String.class));
            effectMethodMappings.put("mailman",
                    CLIActionsExecutor.class.getDeclaredMethod("mailman", String.class));
            effectMethodMappings.put("herbalist",
                    CLIActionsExecutor.class.getDeclaredMethod("herbalist", String.class));
            effectMethodMappings.put("centaur",
                    CLIActionsExecutor.class.getDeclaredMethod("centaur", String.class));
            effectMethodMappings.put("jester",
                    CLIActionsExecutor.class.getDeclaredMethod("jester", String.class));
            effectMethodMappings.put("knight",
                    CLIActionsExecutor.class.getDeclaredMethod("knight", String.class));
            effectMethodMappings.put("mushroomer",
                    CLIActionsExecutor.class.getDeclaredMethod("mushroomer", String.class));
            effectMethodMappings.put("bard",
                    CLIActionsExecutor.class.getDeclaredMethod("bard", String.class));
            effectMethodMappings.put("queen",
                    CLIActionsExecutor.class.getDeclaredMethod("queen", String.class));
            effectMethodMappings.put("scoundrel",
                    CLIActionsExecutor.class.getDeclaredMethod("scoundrel", String.class));
        } catch (NoSuchMethodException e) {
            System.err.printf("Error while creating action/effect method mappings (%s)", e.getMessage());
            System.exit(1);
        }
        ACTION_METHOD_MAPPINGS = Collections.unmodifiableMap(actionMethodMappings);
        EFFECT_METHOD_MAPPINGS = Collections.unmodifiableMap(effectMethodMappings);
    }

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
     * Execute an action from the available ones for current phase.
     *
     * @param username player username
     * @throws CLIActionsExecutor.CLIActionsExecutorException if there was an error while retrieving game information from parser or executing action methods
     */
    public void executeAction(String username) throws CLIActionsExecutorException {
        List<String> availableActions;
        try {
            availableActions = this.directivesParser.getAvailableActions();
            if (!this.directivesParser.isExpertMode() ||
                    Boolean.FALSE.equals(this.directivesParser.getPlayersEnabledCharacterCard().get(username)))
                availableActions.remove("ActivateCharacterCard");
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new CLIActionsExecutorException(
                    String.format("Error while retrieving game information from parser (%s)", e.getMessage()), e);
        }
        List<String> actionNameArgs;
        int chosenAction = -1;
        String chosenActionString;
        System.out.printf("%n> Choose an action:%n");
        while (chosenAction < 0 || chosenAction >= availableActions.size()) {
            for (int i = 0; i < availableActions.size(); i++) {
                actionNameArgs = Arrays.stream(availableActions.get(i).split("(?=\\p{Lu})")).toList();
                System.out.printf("- %s%s [%d]%s%n", CLIConstants.ANSI_BRIGHT_WHITE,
                        actionNameArgs.stream().skip(1).map(s ->
                                        " " + s.substring(0, 1).toLowerCase() + s.substring(1))
                                .reduce(actionNameArgs.get(0), String::concat),
                        i + 1, CLIConstants.ANSI_RESET);
            }
            System.out.print("> ");
            try {
                chosenActionString = CLIActionsExecutor.input.nextLine().strip();
                chosenAction = Integer.parseInt(chosenActionString) - 1;
                if (chosenAction < 0 || chosenAction >= availableActions.size())
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("> Invalid input, choose an action:");
            }
        }
        try {
            ACTION_METHOD_MAPPINGS.get(availableActions.get(chosenAction)).invoke(this, username);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CLIActionsExecutorException(
                    String.format("Error while invoking action method (%s)", e.getMessage()), e);
        }
    }

    /**
     * Execute fill clouds action.
     *
     * @param username player username
     */
    private void fillClouds(String username) {
        this.directivesDispatcher.actionFillClouds(username);
    }

    /**
     * Execute play assistant card action.
     *
     * @param username player username
     */
    private void playAssistantCard(String username) {
        List<String> assistantCardNames = this.directivesParser.getDashboardAssistantCards(username).keySet().stream().toList();
        System.out.print("> Choose an assistant card to play (from the ones you have on your dashboard): ");
        String chosenAssistantCardName = "";
        while (chosenAssistantCardName.isEmpty()) {
            chosenAssistantCardName = CLIActionsExecutor.input.nextLine().strip().toLowerCase();
            if (!assistantCardNames.contains(chosenAssistantCardName)) {
                System.out.print("> Invalid input, choose an assistant card to play (from the ones you have on your dashboard): ");
                chosenAssistantCardName = "";
            }
        }
        this.directivesDispatcher.actionPlayAssistantCard(username, chosenAssistantCardName);
    }

    /**
     * Parse a tokens list from a string based on regex.
     *
     * @param tokensListString string representing tokens list
     * @return list of tokens
     */
    private List<String> parseTokensList(String tokensListString) {
        Pattern pattern = Pattern.compile(TOKENS_LIST_REGEX);
        Matcher matcher = pattern.matcher(tokensListString);
        List<String> tokens = new ArrayList<>();
        while (matcher.find())
            tokens.addAll(Collections.nCopies(
                    Integer.parseInt(matcher.group(2)), View.AVAILABLE_TOKEN_COLORS.get(matcher.group(1))));
        return tokens;
    }

    /**
     * Select students to move to different islands.
     *
     * @return map of island indexes and tokens lists
     */
    private Map<Integer, List<String>> selectStudentsToIslands() {
        System.out.printf("> Students from entrance to islands (press enter to stop).%n" +
                "> Select an island: ");
        Map<Integer, List<String>> studentsToIslands = new HashMap<>();
        int islandsSize = this.directivesParser.getIslandsSize();
        int islandIndex = -1;
        String choice = " ";
        while (!choice.isEmpty() && (islandIndex < 0 || islandIndex >= islandsSize)) {
            choice = CLIActionsExecutor.input.nextLine().strip();
            if (!choice.isEmpty()) {
                try {
                    islandIndex = Integer.parseInt(choice) - 1;
                    if (islandIndex < 0 || islandIndex >= islandsSize)
                        throw new NumberFormatException();
                    System.out.print(TOKENS_LIST_MESSAGE);
                    choice = CLIActionsExecutor.input.nextLine().toLowerCase();
                    studentsToIslands.put(islandIndex, this.parseTokensList(choice));
                    islandIndex = -1;
                    System.out.print("> Select another island: ");
                } catch (NumberFormatException e) {
                    System.out.print("> Invalid input, select an island: ");
                }
            }
        }
        return studentsToIslands;
    }

    /**
     * Execute move students action.
     *
     * @param username player username
     */
    private void moveStudents(String username) {
        System.out.printf("> Students from entrance to hall (press enter for none).%n%s", TOKENS_LIST_MESSAGE);
        List<String> studentsToHall = new ArrayList<>();
        Map<Integer, List<String>> studentsToIslands = new HashMap<>();
        String tokensListString;
        int studentsPerCloud = this.directivesParser.getStudentsPerCloud();
        while (studentsToHall.size() + studentsToIslands.values().stream()
                .mapToInt(List::size).sum() != studentsPerCloud) {
            tokensListString = CLIActionsExecutor.input.nextLine().toLowerCase();
            studentsToHall = this.parseTokensList(tokensListString);
            if (studentsToHall.size() < studentsPerCloud)
                studentsToIslands = this.selectStudentsToIslands();
            if (studentsToHall.size() + studentsToIslands.values().stream()
                    .mapToInt(List::size).sum() != studentsPerCloud) {
                System.out.printf("> Invalid input, you have to move exactly %d students from the entrance.%n" +
                                "> Students from entrance to hall (press enter for none).%n%s",
                        studentsPerCloud, TOKENS_LIST_MESSAGE);
                studentsToHall = new ArrayList<>();
                studentsToIslands = new HashMap<>();
            }
        }
        this.directivesDispatcher.actionMoveStudents(username, studentsToHall, studentsToIslands);
    }

    /**
     * Execute move mother nature action.
     *
     * @param username player username
     */
    private void moveMotherNature(String username) {
        System.out.print("> Choose the number of steps for mother nature: ");
        int baseSteps = this.directivesParser.getDashboardLastDiscardedAssistantCard(username).getValue().getValue();
        int additionalSteps = this.directivesParser.getAdditionalMotherNatureSteps();
        String motherNatureStepsString;
        int motherNatureSteps = 0;
        while (motherNatureSteps < 1 || motherNatureSteps > baseSteps + additionalSteps) {
            try {
                motherNatureStepsString = input.nextLine().strip();
                motherNatureSteps = Integer.parseInt(motherNatureStepsString);
                if (motherNatureSteps < 1 || motherNatureSteps > baseSteps + additionalSteps)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                motherNatureSteps = 0;
                System.out.print("> Invalid input, choose the number of steps for mother nature: ");
            }
        }
        this.directivesDispatcher.actionMoveMotherNature(username, motherNatureSteps);
    }

    /**
     * Execute choose cloud action.
     *
     * @param username player username
     */
    private void chooseCloud(String username) {
        System.out.println("> Choose a cloud:");
        int cloudSize = this.directivesParser.getCloudsSize();
        String cloudIndexString;
        int cloudIndex = -1;
        while (cloudIndex < 0 || cloudIndex >= cloudSize) {
            for (int i = 0; i < cloudSize; i++)
                System.out.printf("- %s%s cloud [%s]%s%n", CLIConstants.ANSI_BRIGHT_WHITE,
                        CLIGamePrinter.CARDINALITY_MAP.get(i), i + 1, CLIConstants.ANSI_RESET);
            System.out.print("> ");
            try {
                cloudIndexString = input.nextLine().strip();
                cloudIndex = Integer.parseInt(cloudIndexString) - 1;
                if (cloudIndex < 0 || cloudIndex >= cloudSize)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("> Invalid input, choose a cloud:");
            }
        }
        this.directivesDispatcher.actionChooseCloud(username, cloudIndex);
    }

    /**
     * Execute activate character card action.
     *
     * @param username player username
     */
    private void activateCharacterCard(String username) throws CLIActionsExecutorException {
        List<String> characterCardNames = this.directivesParser.getCharacterCardNames();
        System.out.println("> Choose a character card:");
        int chosenCard = -1;
        String chosenCardString;
        while (chosenCard < 0 || chosenCard >= characterCardNames.size()) {
            String cardName;
            for (int i = 0; i < characterCardNames.size(); i++) {
                cardName = characterCardNames.get(i);
                System.out.printf("- %s%s [%d]%s%n", CLIConstants.ANSI_BRIGHT_WHITE,
                        cardName.substring(0, 1).toUpperCase() + cardName.substring(1),
                        i + 1, CLIConstants.ANSI_RESET);
            }
            System.out.print("> ");
            try {
                chosenCardString = CLIActionsExecutor.input.nextLine().strip();
                chosenCard = Integer.parseInt(chosenCardString) - 1;
                if (chosenCard < 0 || chosenCard >= characterCardNames.size())
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("> Invalid input, choose a character card:");
            }
        }
        try {
            EFFECT_METHOD_MAPPINGS.get(characterCardNames.get(chosenCard)).invoke(this, username);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CLIActionsExecutorException(
                    String.format("Error while invoking effect method (%s)", e.getMessage()), e);
        }
    }

    /**
     * Activate innkeeper character card effect.
     *
     * @param username player username
     */
    private void innkeeper(String username) {
        this.directivesDispatcher.activateInnkeeper(username);
    }

    /**
     * Activate mailman character card effect.
     *
     * @param username player username
     */
    private void mailman(String username) {
        this.directivesDispatcher.activateMailman(username);
    }

    /**
     * Activate centaur character card effect.
     *
     * @param username player username
     */
    private void centaur(String username) {
        this.directivesDispatcher.activateCentaur(username);
    }

    /**
     * Activate knight character card effect.
     *
     * @param username player username
     */
    private void knight(String username) {
        this.directivesDispatcher.activateKnight(username);
    }

    /**
     * Activate monk character card effect.
     *
     * @param username player username
     */
    private void monk(String username) {

    }

    /**
     * Activate herald character card effect.
     *
     * @param username player username
     */
    private void herald(String username) {

    }

    /**
     * Activate herbalist character card effect.
     *
     * @param username player username
     */
    private void herbalist(String username) {

    }

    /**
     * Activate jester character card effect.
     *
     * @param username player username
     */
    private void jester(String username) {

    }

    /**
     * Activate mushroomer character card effect.
     *
     * @param username player username
     */
    private void mushroomer(String username) {

    }

    /**
     * Activate bard character card effect.
     *
     * @param username player username
     */
    private void bard(String username) {

    }

    /**
     * Activate queen character card effect.
     *
     * @param username player username
     */
    private void queen(String username) {

    }

    /**
     * Activate scoundrel character card effect.
     *
     * @param username player username
     */
    private void scoundrel(String username) {

    }

    /**
     * Exception for errors within cli action executor class.
     */
    public static class CLIActionsExecutorException extends Exception {
        /**
         * CLIActionsExecutorException constructor with message and cause.
         *
         * @param message message to be shown
         * @param cause   cause of the exception
         */
        public CLIActionsExecutorException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
