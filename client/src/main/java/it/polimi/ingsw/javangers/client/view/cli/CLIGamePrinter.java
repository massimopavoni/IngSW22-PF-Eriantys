package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the cli printer for game information.
 */
public class CLIGamePrinter {
    /**
     * Map for cardinal numbers strings.
     */
    private static final Map<Integer, String> CARDINALITY_MAP = Map.ofEntries(
            Map.entry(0, "1st"),
            Map.entry(1, "2nd"),
            Map.entry(2, "3rd"),
            Map.entry(3, "4th"),
            Map.entry(4, "5th"),
            Map.entry(5, "6th"),
            Map.entry(6, "7th"),
            Map.entry(7, "8th"),
            Map.entry(8, "9th"),
            Map.entry(9, "10th"),
            Map.entry(10, "11th"),
            Map.entry(11, "12th"));
    /**
     * CLI game printer singleton instance.
     */
    private static CLIGamePrinter singleton = null;
    /**
     * Client directives parser instance.
     */
    private final DirectivesParser directivesParser;

    /**
     * Private constructor for utility class.
     *
     * @param directivesParser client directives parser instance
     */
    private CLIGamePrinter(DirectivesParser directivesParser) {
        this.directivesParser = directivesParser;
    }

    /**
     * Get cli game printer singleton instance.
     *
     * @param directivesParser client directives parser instance
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
        this.printSeparator();

        this.printStudentsBag();
        this.printClouds();
        this.printSeparator();

        List<String> playerDashboards = this.directivesParser.getDashboardNames();
        playerDashboards.remove(username);
        playerDashboards.add(0, username);
        for (String player : playerDashboards)
            this.printPlayerDashboard(player, username);
        this.printSeparator();

        this.printTeachers(username);
        this.printSeparator();

        this.printArchipelago();
        this.printSeparator();

        this.printCharacterCardsEffects();
        this.printCharacterCards();
        this.printSeparator();
    }

    private void printSeparator() {
        System.out.printf("%n%s%s%n", CLIConstants.ANSI_BRIGHT_WHITE, "-".repeat(64));
    }

    /**
     * Print current game phase information.
     */
    private void printGamePhase() {
        Pair<String, String> currentPhase = this.directivesParser.getCurrentPhase();
        System.out.printf("%sCurrent phase: %s%s => %s%s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_BRIGHT_WHITE,
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
        this.directivesParser.getPlayersOrder().forEach(player -> {
            String notCurrentPlayerColor = player.equals(username) ? CLIConstants.ANSI_BRIGHT_BLUE : CLIConstants.ANSI_BRIGHT_WHITE;
            coloredPlayersOrder.add(
                    String.format("%s%s%s", player.equals(currentPlayer) ? CLIConstants.ANSI_BRIGHT_GREEN
                            : notCurrentPlayerColor, player, CLIConstants.ANSI_RESET));
        });
        System.out.printf("%s (You are %s%s%s)%n", String.join(", ", coloredPlayersOrder),
                username.equals(currentPlayer) ? CLIConstants.ANSI_BRIGHT_GREEN
                        : CLIConstants.ANSI_BRIGHT_BLUE, username, CLIConstants.ANSI_RESET);
    }

    /**
     * Format a specific color counts map.
     *
     * @param colorCounts color counts map
     * @return formatted string for color counts
     */
    private String formatColorCounts(Map<String, Integer> colorCounts) {
        return View.TOKEN_COLORS_MAPPINGS.entrySet().stream().map(entry -> String.format("%s%s %s%d%s",
                        CLIConstants.TOKEN_COLORS_CLI_COLORS.get(entry.getKey()), entry.getValue(), CLIConstants.ANSI_BRIGHT_WHITE,
                        colorCounts.getOrDefault(View.AVAILABLE_TOKEN_COLORS.get(entry.getKey()), 0), CLIConstants.ANSI_RESET))
                .collect(Collectors.joining(", "));
    }

    /**
     * Print specific towers' information.
     *
     * @param towers  towers pair
     * @param tabbing tabbing string for different sections
     */
    private void printTowers(Pair<String, Integer> towers, String tabbing) {
        Optional<Map.Entry<String, String>> towerColorEntry = View.AVAILABLE_TOWER_COLORS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(towers.getKey())).findFirst();
        System.out.printf("%n%s%sTowers: %d %s%s", tabbing, CLIConstants.ANSI_BRIGHT_WHITE, towers.getValue(),
                towerColorEntry.isPresent() ? View.TOWER_COLORS_MAPPINGS.get(towerColorEntry.get().getKey())
                        : "none", CLIConstants.ANSI_RESET);
    }

    /**
     * Format a specific assistant card's information.
     *
     * @param assistantCard assistant card entry
     * @return formatted string for assistant card
     */
    private String formatAssistantCard(Map.Entry<String, Pair<Integer, Integer>> assistantCard) {
        String cardName = assistantCard.getKey();
        return String.format("%s%s%s (%s%d%s/%s%d%s)", CLIConstants.ANSI_BRIGHT_WHITE,
                cardName.substring(0, 1).toUpperCase() + cardName.substring(1), CLIConstants.ANSI_RESET,
                CLIConstants.ANSI_BRIGHT_GREEN, assistantCard.getValue().getKey(), CLIConstants.ANSI_RESET,
                CLIConstants.ANSI_BRIGHT_RED, assistantCard.getValue().getValue(), CLIConstants.ANSI_RESET);
    }

    /**
     * Print students bag information.
     *
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printStudentsBag() throws DirectivesParser.DirectivesParserException {
        System.out.printf("%n%sStudents bag:%s %s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET,
                this.formatColorCounts(this.directivesParser.getStudentsBagTokens()));
    }

    /**
     * Print clouds information.
     *
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printClouds() throws DirectivesParser.DirectivesParserException {
        System.out.printf("%n%sClouds:%s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
        for (int i = 0; i < this.directivesParser.getCloudsSize(); i++)
            System.out.printf("  %s%d => %s%n", CLIConstants.ANSI_BRIGHT_WHITE, i + 1,
                    this.formatColorCounts(this.directivesParser.getCloudTokens(i)));
    }

    /**
     * Print dashboard title depending on client player and current player.
     *
     * @param username       player username
     * @param playerUsername client player username
     */
    private void printDashboardTitle(String username, String playerUsername) {
        String currentPlayer = this.directivesParser.getCurrentPlayer();
        if (username.equals(playerUsername))
            System.out.printf("%n%sYour dashboard:%s", username.equals(currentPlayer) ? CLIConstants.ANSI_BRIGHT_GREEN
                    : CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
        else
            System.out.printf("%n%s%s's dashboard:%s", username.equals(currentPlayer) ? CLIConstants.ANSI_BRIGHT_GREEN
                    : CLIConstants.ANSI_BRIGHT_WHITE, username, CLIConstants.ANSI_RESET);
    }

    /**
     * Print dashboard coins number.
     *
     * @param username client player username
     */
    private void printDashboardCoins(String username) {
        if (this.directivesParser.isExpertMode()) {
            System.out.printf("%n  %sCoins: %s%d%s", CLIConstants.ANSI_BRIGHT_WHITE,
                    CLIConstants.ANSI_BRIGHT_YELLOW, this.directivesParser.getDashboardCoins(username), CLIConstants.ANSI_RESET);
        }
    }

    /**
     * Print dashboard cards back wizard type.
     *
     * @param username client player username
     */
    private void printDashboardCardsBack(String username) {
        String wizardType = this.directivesParser.getDashboardCardsBack(username);
        String wizardTypeID = View.AVAILABLE_WIZARD_TYPES.entrySet().stream()
                .filter(entry -> entry.getValue().equals(wizardType)).findFirst().orElseThrow().getKey();
        System.out.printf("%n  %sWizard: %s%s%s", CLIConstants.ANSI_BRIGHT_WHITE, CLIConstants.WIZARD_TYPES_CLI_COLORS.get(wizardTypeID),
                View.WIZARD_TYPES_MAPPINGS.get(wizardTypeID), CLIConstants.ANSI_RESET);
    }

    /**
     * Print dashboard assistant cards' information.
     *
     * @param username client player username
     */
    private void printDashboardAssistantCards(String username) {
        Map.Entry<String, Pair<Integer, Integer>> assistantCard = this.directivesParser.getDashboardLastDiscardedAssistantCard(username);
        if (assistantCard != null) {
            System.out.printf("%n  %sLast discarded assistant card:%s %s", CLIConstants.ANSI_BRIGHT_WHITE,
                    CLIConstants.ANSI_RESET, this.formatAssistantCard(assistantCard));
        }
        Map<String, Pair<Integer, Integer>> assistantCards = this.directivesParser.getDashboardAssistantCards(username);
        if (!assistantCards.isEmpty()) {
            List<String> assistantCardNames = assistantCards.keySet().stream().toList();
            List<Map<String, Pair<Integer, Integer>>> assistantCardsGroups = new ArrayList<>();
            for (int i = 0; i < assistantCardNames.size(); i++) {
                if (i % 5 == 0)
                    assistantCardsGroups.add(new LinkedHashMap<>());
                assistantCardsGroups.get(i / 5).put(assistantCardNames.get(i), assistantCards.get(assistantCardNames.get(i)));
            }
            System.out.printf("%n  %sAssistant cards:%s %s", CLIConstants.ANSI_BRIGHT_WHITE, CLIConstants.ANSI_RESET,
                    assistantCardsGroups.get(0).entrySet().stream().map(this::formatAssistantCard)
                            .collect(Collectors.joining(", ")));
            for (int i = 1; i < assistantCardsGroups.size(); i++)
                System.out.printf("%n  %s %s", " ".repeat(16), assistantCardsGroups.get(i).entrySet().stream()
                        .map(this::formatAssistantCard).collect(Collectors.joining(", ")));
        }
    }

    /**
     * Print dashboard information.
     *
     * @param username       player username
     * @param playerUsername client player username
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printPlayerDashboard(String username, String playerUsername) throws DirectivesParser.DirectivesParserException {
        this.printDashboardTitle(username, playerUsername);
        this.printDashboardCoins(username);
        this.printTowers(this.directivesParser.getDashboardTowers(username), "  ");
        this.printDashboardCardsBack(username);
        this.printDashboardAssistantCards(username);
        System.out.printf("%n  %sEntrance:%s %s", CLIConstants.ANSI_BRIGHT_WHITE, CLIConstants.ANSI_RESET,
                this.formatColorCounts(this.directivesParser.getDashboardEntranceTokens(username)));
        System.out.printf("%n  %sHall:%s%s %s%n", CLIConstants.ANSI_BRIGHT_WHITE, CLIConstants.ANSI_RESET, " ".repeat(4),
                this.formatColorCounts(this.directivesParser.getDashboardHallTokens(username)));
    }

    /**
     * Print teachers' information.
     *
     * @param username player username
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printTeachers(String username) throws DirectivesParser.DirectivesParserException {
        String currentPlayer = this.directivesParser.getCurrentPlayer();
        Map<String, String> teachers = this.directivesParser.getTeachers();
        System.out.printf("%n%sTeachers' owners:%n", CLIConstants.ANSI_BRIGHT_BLUE);
        teachers.forEach((teacher, owner) -> {
            String teacherColorID = View.AVAILABLE_TOKEN_COLORS.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(teacher)).findFirst().orElseThrow().getKey();
            String ownerColor = owner.equals(username) ? CLIConstants.ANSI_BRIGHT_BLUE : CLIConstants.ANSI_BRIGHT_WHITE;
            String ownerName = owner.equals(username) ? "You" : owner;
            if (ownerName.isEmpty())
                ownerName = "none";
            String formattedOwner = String.format("%s%s", owner.equals(currentPlayer) ? CLIConstants.ANSI_BRIGHT_GREEN
                    : ownerColor, ownerName);
            System.out.printf("  %s%-12s%s => %s%n", CLIConstants.TOKEN_COLORS_CLI_COLORS.get(teacherColorID),
                    View.TOKEN_COLORS_MAPPINGS.get(teacherColorID), CLIConstants.ANSI_BRIGHT_WHITE, formattedOwner);
        });
    }

    /**
     * Print archipelago's islands information.
     *
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printArchipelago() throws DirectivesParser.DirectivesParserException {
        System.out.printf("%n%sArchipelago:%n", CLIConstants.ANSI_BRIGHT_BLUE);
        int motherNaturePosition = this.directivesParser.getMotherNaturePosition();
        System.out.printf("  %sMother nature position: %s%s island%s%n",
                CLIConstants.ANSI_BRIGHT_WHITE, CLIConstants.ANSI_BRIGHT_GREEN,
                CARDINALITY_MAP.get(motherNaturePosition), CLIConstants.ANSI_RESET);
        System.out.printf("  %sIslands:%n", CLIConstants.ANSI_BRIGHT_WHITE);
        for (int i = 0; i < this.directivesParser.getIslandsSize(); i++) {
            int enabled = this.directivesParser.getIslandEnabled(i);
            System.out.printf("%s%s%s" +
                            "%n%s%sEnabled: %s%d", " ".repeat(4), motherNaturePosition == i
                            ? CLIConstants.ANSI_BRIGHT_GREEN : CLIConstants.ANSI_BRIGHT_WHITE, CARDINALITY_MAP.get(i),
                    " ".repeat(6), CLIConstants.ANSI_BRIGHT_WHITE, enabled > 0 ? CLIConstants.ANSI_BRIGHT_RED
                            : CLIConstants.ANSI_BRIGHT_GREEN, enabled);
            this.printTowers(this.directivesParser.getIslandTowers(i), " ".repeat(6));
            System.out.printf("%n%s%sTokens: %s%n", " ".repeat(6), CLIConstants.ANSI_BRIGHT_WHITE,
                    this.formatColorCounts(this.directivesParser.getIslandTokens(i)));
        }
    }

    /**
     * Print character cards effects status.
     */
    private void printCharacterCardsEffects() {
        boolean teachersEqualCount = this.directivesParser.isTeachersEqualCount();
        int additionalMotherNatureSteps = this.directivesParser.getAdditionalMotherNatureSteps();
        boolean enabledIslandTowers = this.directivesParser.isEnabledIslandTowers();
        int additionalPower = this.directivesParser.getAdditionalPower();
        String forbiddenColor = this.directivesParser.getForbiddenColor();
        String forbiddenColorID = forbiddenColor != null ? View.AVAILABLE_TOKEN_COLORS.entrySet().stream()
                .filter(entry -> entry.getValue().equals(forbiddenColor)).findFirst().orElseThrow().getKey() : null;
        System.out.printf("%n%sCharacter cards' effects:" +
                        "%n  %sTeachers equal count: %s%s" +
                        "%n  %sAdditional mother nature steps: %s%d" +
                        "%n  %sEnabled island towers: %s%s" +
                        "%n  %sAdditional power: %s%d" +
                        "%n  %sForbidden color: %s%s%s%n",
                CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_BRIGHT_WHITE, teachersEqualCount
                        ? CLIConstants.ANSI_BRIGHT_GREEN : CLIConstants.ANSI_BRIGHT_RED, teachersEqualCount,
                CLIConstants.ANSI_BRIGHT_WHITE, additionalMotherNatureSteps > 0
                        ? CLIConstants.ANSI_BRIGHT_GREEN : CLIConstants.ANSI_BRIGHT_RED, additionalMotherNatureSteps,
                CLIConstants.ANSI_BRIGHT_WHITE, enabledIslandTowers
                        ? CLIConstants.ANSI_BRIGHT_GREEN : CLIConstants.ANSI_BRIGHT_RED, enabledIslandTowers,
                CLIConstants.ANSI_BRIGHT_WHITE, additionalPower > 0
                        ? CLIConstants.ANSI_BRIGHT_GREEN : CLIConstants.ANSI_BRIGHT_RED, additionalPower,
                CLIConstants.ANSI_BRIGHT_WHITE, forbiddenColor != null
                        ? CLIConstants.TOKEN_COLORS_CLI_COLORS.get(forbiddenColorID) : CLIConstants.ANSI_BRIGHT_WHITE,
                forbiddenColor != null ? View.TOKEN_COLORS_MAPPINGS.get(forbiddenColorID) : "none", CLIConstants.ANSI_RESET);
    }

    /**
     * Print character cards' information.
     *
     * @throws DirectivesParser.DirectivesParserException if there was an error while retrieving game information from parser
     */
    private void printCharacterCards() throws DirectivesParser.DirectivesParserException {
        if (this.directivesParser.isExpertMode()) {
            System.out.printf("%n%sCharacter cards:%s%n", CLIConstants.ANSI_BRIGHT_BLUE, CLIConstants.ANSI_RESET);
            for (String cardName : this.directivesParser.getCharacterCardNames()) {
                Pair<Integer, Integer> cardCost = this.directivesParser.getCharacterCardCost(cardName);
                Map<String, Integer> cardTokens = this.directivesParser.getCharacterCardTokens(cardName);
                System.out.printf(
                        "  %s%s%n%sCost: %s%d+%d%s%n%sCounter: %s%d%s%n", CLIConstants.ANSI_BRIGHT_WHITE,
                        cardName.substring(0, 1).toUpperCase() + cardName.substring(1), " ".repeat(4),
                        CLIConstants.ANSI_BRIGHT_YELLOW, cardCost.getKey(), cardCost.getValue(), CLIConstants.ANSI_BRIGHT_WHITE,
                        " ".repeat(4), CLIConstants.ANSI_BRIGHT_CYAN,
                        this.directivesParser.getCharacterCardMultipurposeCounter(cardName),
                        cardTokens.size() < 1 ? CLIConstants.ANSI_RESET : String.format("%n%s%sTokens: %s%s", " ".repeat(4),
                                CLIConstants.ANSI_BRIGHT_WHITE, this.formatColorCounts(cardTokens), CLIConstants.ANSI_RESET));
            }
        }
    }
}

