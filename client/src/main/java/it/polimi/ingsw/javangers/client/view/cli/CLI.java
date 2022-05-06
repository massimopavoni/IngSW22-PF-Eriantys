package it.polimi.ingsw.javangers.client.view.cli;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLI extends View {
    private static final Scanner input = new Scanner(System.in);

    /**
     * Constructor for cli, initializing directives dispatcher and parser.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    public CLI(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        super(directivesDispatcher, directivesParser);
    }

    private static boolean isValidUsername(String username) {
        Pattern pattern = Pattern.compile(USERNAME_REGEX);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

//    private static boolean confirm() {
//        String confirmString = null;
//        System.out.println(">Do you want to confirm? [" + constantsMap.get("GREEN") + "y" + constantsMap.get("RST") + "/" + constantsMap.get("RED") + "n" + constantsMap.get("RST") + "]:");
//        System.out.print(">");
//        while (true) {
//            confirmString = input.nextLine();=
//            if (confirmString != null) {
//                if (confirmString.equalsIgnoreCase("y") || confirmString.equalsIgnoreCase("n")) {
//                    return confirmString.equalsIgnoreCase("y");
//                } else {
//                    System.out.println("Please insert correct input [" + constantsMap.get("GREEN") + "y" + constantsMap.get("RST") + "/" + constantsMap.get("RED") + "n" + constantsMap.get("RST") + "]:");
//                    System.out.print(">");
//                }
//            }
//        }
//    }

//    private static String chooseUsername() {
//        String username = null;
//        System.out.println(">Insert your " + constantsMap.get("YELLOW_BRIGHT") + "username " + constantsMap.get("RST") + "(start with a letter, 3-30 characters):");
//        System.out.print(">");
//        while (username == null) {
//            username = input.nextLine();
//            if (!isValidUsername(username)) {
//                username = null;
//                System.out.println(">Please insert correct input (start with a letter, 3-30 characters):");
//                System.out.print(">");
//            }
//        }
//        return username;
//    }

    public static int chooseNumberOfPlayers() {
        String numberOfPlayersString = null;
        int numberOfPlayers = 0;
        System.out.println(">Insert if you want to play in 2 or 3 players:");
        System.out.print(">");
        while (numberOfPlayersString == null) {
            numberOfPlayersString = input.nextLine();
            if (numberOfPlayersString != null) {
                if (numberOfPlayersString.equalsIgnoreCase("2") || numberOfPlayersString.equalsIgnoreCase("3")) {
                    numberOfPlayers = Integer.parseInt(numberOfPlayersString);
                } else {
                    numberOfPlayersString = null;
                    System.out.println(">Please insert correct input [2/3]:");
                    System.out.print(">");
                }
            }
        }
        return numberOfPlayers;
    }

//    private static boolean chooseExpertMode() {
//        String expertModeString;
//        System.out.println(">Insert if you want to play in expert mode [" + constantsMap.get("GREEN") + "y" + constantsMap.get("RST") + "/" + constantsMap.get("RED") + "n" + constantsMap.get("RST") + "]:");
//        System.out.print(">");
//        while (true) {
//            expertModeString = null;
//            expertModeString = input.nextLine();
//            if (expertModeString != null) {
//                if (expertModeString.equalsIgnoreCase("y")) {
//                    return true;
//                } else if (expertModeString.equalsIgnoreCase("n")) {
//                    return false;
//                } else {
//                    System.out.println(">Please insert correct input [y/n]:");
//                    System.out.print(">");
//                }
//            }
//        }
//    }

    private static String chooseWizardType() {
        String wizardType = null;
        System.out.println(">Insert which type of wizard you want to be between:");
        System.out.println(">Druid [d]");
        System.out.println(">King [k]");
        System.out.println(">Sensei [s]");
        System.out.println(">Witch [w]");
        System.out.print(">");
        while (wizardType == null) {
            wizardType = input.nextLine();
            if (wizardType != null) {
                if (wizardType.equalsIgnoreCase("d")) wizardType = "DRUID";
                else if (wizardType.equalsIgnoreCase("k")) wizardType = "KING";
                else if (wizardType.equalsIgnoreCase("s")) wizardType = "SENSEI";
                else if (wizardType.equalsIgnoreCase("w")) wizardType = "WITCH";
                else {
                    wizardType = null;
                    System.out.println(">Please insert correct input [d/k/s/w]:");
                    System.out.print(">");
                }
            }
        }
        return wizardType;
    }

    private static String chooseTowerColor() {
        String towerColor = null;
        System.out.println(">Insert which color of tower you want to be between:");
        System.out.println(">White [w]");
        System.out.println(">Black [b]");
        System.out.println(">Gray [g]");
        System.out.print(">");
        while (towerColor == null) {
            towerColor = input.nextLine();
            if (towerColor != null) {
                if (towerColor.equalsIgnoreCase("w")) towerColor = "WHITE";
                else if (towerColor.equalsIgnoreCase("b")) towerColor = "BLACK";
                else if (towerColor.equalsIgnoreCase("g")) towerColor = "GRAY";
                else {
                    System.out.println(">Please insert correct input [w/b/g]:");
                    System.out.print(">");
                    towerColor = null;
                }
            }
        }
        return towerColor;
    }

    public static void main(String[] args) {
        System.out.printf("%s%s%s%n%n", CLIConstants.ANSI_BRIGHT_MAGENTA, CLIConstants.ERIANTYS_TITLE, CLIConstants.ANSI_RESET);
        String selection = null;
        String username;
        String numberOfPlayersString;
        String expertModeString;
        int numberOfPlayers = 0;
        boolean expertMode = false;
        boolean confirmation = false;
        String wizardType;
        String towerColor;


//        System.out.println(">Insert if you want to create a new game or join one [" + constantsMap.get("BLUE") + "c" + constantsMap.get("RST") + "/" + constantsMap.get("RED") + "j" + constantsMap.get("RST") + "]:");
//        System.out.print(">");
//        while (selection == null) {
//            selection = input.nextLine();
//            if (selection != null) {
//                if (selection.equalsIgnoreCase("c")) {
//                    do {
//                        username = chooseUsername();
//                        numberOfPlayers = chooseNumberOfPlayers();
//                        expertMode = chooseExpertMode();
//                        wizardType = chooseWizardType();
//                        towerColor = chooseTowerColor();
//                    } while (!confirm());
//                    directivesDispatcher.createGame(username, numberOfPlayers, expertMode, wizardType, towerColor);
//                    synchronized (newDataLock) {
//                        try {
//                            newDataLock.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    System.out.println(constantsMap.get("GREEN_BRIGHT") + ">Game created correctly!" + constantsMap.get("RST"));
//                    System.out.println(constantsMap.get("YELLOW_BRIGHT") + ">Waiting for other players to join..." + constantsMap.get("RST"));
//                } else if (selection.equalsIgnoreCase("j")) {
//                    do {
//                        username = chooseUsername();
//                        wizardType = chooseWizardType();
//                        towerColor = chooseTowerColor();
//                    } while (!confirm());
//                    System.out.println(constantsMap.get("GREEN_BRIGHT") + ">Game joined correctly!" + constantsMap.get("RST"));
//                    System.out.println(constantsMap.get("YELLOW_BRIGHT") + ">Waiting for starting the game..." + constantsMap.get("RST"));
//                } else {
//                    System.out.println(">Please insert correct input [c/j]:");
//                    System.out.print(">");
//                    selection = null;
//                }
//            }
//        }
//
//        System.out.println(constantsMap.get("GREEN_BRIGHT") + ">Let's play!" + constantsMap.get("RST"));
//
//
//        System.out.println(constantsMap.get("BLUE") + ">Now it's your turn!" + constantsMap.get("RST"));
    }

    @Override
    protected void createGame() {

    }

    @Override
    protected void joinGame() {

    }

    @Override
    protected void waitForStart() {

    }

    @Override
    protected void startGame() {

    }

    @Override
    protected void startShow() {

    }

    @Override
    protected void updateGame() {

    }

    @Override
    protected void showAbort(String message) {

    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void closeGame(List<String> winners) {

    }

    @Override
    protected void enableActions() {

    }

    @Override
    protected void waitTurn() {
        System.out.printf("%sWait for your turn.%s%n", CLIConstants.ANSI_BRIGHT_YELLOW, CLIConstants.ANSI_RESET);
    }
}

