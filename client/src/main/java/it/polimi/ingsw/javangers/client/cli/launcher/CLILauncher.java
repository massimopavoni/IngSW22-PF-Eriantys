package it.polimi.ingsw.javangers.client.cli.launcher;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLILauncher {

    private static final String GAME_COLOR_RESOURCE_LOCATION = "/it/polimi/ingsw/javangers/client/cli/launcher/constants.json";
    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out));
    private static Map<String, String> constantsMap;

    private static DirectivesDispatcher dispatcher;
    private static DirectivesParser parser;
    private static final Object newDataLock = new Object();

    static {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            InputStream jsonInputStream = CLILauncher.class.getResourceAsStream(GAME_COLOR_RESOURCE_LOCATION);
            constantsMap = jsonMapper.readValue(jsonInputStream, new TypeReference<HashMap<String, String>>() {
            });
        } catch (IOException e) {
            System.out.println("Error while reading color json file");
        }
    }

    public static void init(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        dispatcher = directivesDispatcher;
        parser = directivesParser;
        parser.setLock(newDataLock);
    }

    private static boolean isValidUsername(String username) {
        String regex = "^[A-Za-z]\\w{2,29}$";
        Pattern p = Pattern.compile(regex);
        if (username == null) {
            return false;
        }
        Matcher m = p.matcher(username);
        return m.matches();
    }

    private static boolean confirm() {
        String confirmString = null;
        System.out.println(">Do you want to confirm? ["+ constantsMap.get("GREEN")+ "y"+ constantsMap.get("RST") + "/"+ constantsMap.get("RED")+ "n"+constantsMap.get("RST")+ "]:");
        System.out.print(">");
        while (true) {
            try {
                confirmString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (confirmString != null) {
                if (confirmString.equalsIgnoreCase("y") || confirmString.equalsIgnoreCase("n")) {
                    return confirmString.equalsIgnoreCase("y");
                } else {
                    System.out.println("Please insert correct input ["+ constantsMap.get("GREEN")+ "y"+ constantsMap.get("RST") + "/"+ constantsMap.get("RED")+ "n"+constantsMap.get("RST")+ "]:");
                    System.out.print(">");
                }
            }
        }
    }

    private static String chooseUsername() {
        String username = null;
        System.out.println(">Insert your "+ constantsMap.get("YELLOW_BRIGHT") + "username " + constantsMap.get("RST") + "(start with a letter, 3-30 characters):");
        System.out.print(">");
        while (username == null) {
            try {
                username = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!isValidUsername(username)) {
                username = null;
                System.out.println(">Please insert correct input (start with a letter, 3-30 characters):");
                System.out.print(">");
            }
        }
        return username;
    }

    public static int chooseNumberOfPlayers() {
        String numberOfPlayersString = null;
        int numberOfPlayers = 0;
        System.out.println(">Insert if you want to play in 2 or 3 players:");
        System.out.print(">");
        while (numberOfPlayersString == null) {
            try {
                numberOfPlayersString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private static boolean chooseExpertMode() {
        String expertModeString;
        System.out.println(">Insert if you want to play in expert mode ["+ constantsMap.get("GREEN")+ "y"+ constantsMap.get("RST") + "/"+ constantsMap.get("RED")+ "n"+constantsMap.get("RST")+ "]:");
        System.out.print(">");
        while (true) {
            expertModeString = null;
            try {
                expertModeString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (expertModeString != null) {
                if (expertModeString.equalsIgnoreCase("y")) {
                    return true;
                } else if (expertModeString.equalsIgnoreCase("n")) {
                    return false;
                } else {
                    System.out.println(">Please insert correct input [y/n]:");
                    System.out.print(">");
                }
            }
        }
    }

    private static String chooseWizardType() {
        String wizardType = null;
        System.out.println(">Insert which type of wizard you want to be between:");
        System.out.println(">Druid [d]");
        System.out.println(">King [k]");
        System.out.println(">Sensei [s]");
        System.out.println(">Witch [w]");
        System.out.print(">");
        while (wizardType == null) {
            try {
                wizardType = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            try {
                towerColor = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private static void waitTurn() {
        System.out.println(constantsMap.get("YELLOW") + "Wait your turn... " + constantsMap.get("RST"));
    }

    public static void main(String[] args) {
        System.out.println(constantsMap.get("PURPLE") + constantsMap.get("ERIANTYS") + constantsMap.get("RST"));
        String selection = null;
        String username;
        String numberOfPlayersString;
        String expertModeString;
        int numberOfPlayers = 0;
        boolean expertMode = false;
        boolean confirmation = false;
        String wizardType;
        String towerColor;


        System.out.println(">Insert if you want to create a new game or join one [" + constantsMap.get("BLUE") + "c" + constantsMap.get("RST") +"/"+ constantsMap.get("RED") + "j"+ constantsMap.get("RST") + "]:");
        System.out.print(">");
        while (selection == null) {
            try {
                selection = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (selection != null) {
                if (selection.equalsIgnoreCase("c")) {
                    DirectivesParser.setNewData(false);
                    do {
                        username = chooseUsername();
                        numberOfPlayers = chooseNumberOfPlayers();
                        expertMode = chooseExpertMode();
                        wizardType = chooseWizardType();
                        towerColor = chooseTowerColor();
                    } while (!confirm());
                    dispatcher.createGame(username, numberOfPlayers, expertMode, wizardType, towerColor);
                    synchronized (newDataLock) {
                        try {
                            newDataLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(constantsMap.get("GREEN_BRIGHT") +">Game created correctly!" + constantsMap.get("RST"));
                    System.out.println(constantsMap.get("YELLOW_BRIGHT") +">Waiting for other players to join..."+ constantsMap.get("RST"));
                } else if (selection.equalsIgnoreCase("j")) {
                    do {
                        username = chooseUsername();
                        wizardType = chooseWizardType();
                        towerColor = chooseTowerColor();
                    } while (!confirm());
                    System.out.println(constantsMap.get("GREEN_BRIGHT") +">Game joined correctly!" + constantsMap.get("RST"));
                    System.out.println(constantsMap.get("YELLOW_BRIGHT") + ">Waiting for starting the game..." + constantsMap.get("RST"));
                } else {
                    System.out.println(">Please insert correct input [c/j]:");
                    System.out.print(">");
                    selection = null;
                }
            }
        }

        System.out.println(constantsMap.get("GREEN_BRIGHT") + ">Let's play!"+ constantsMap.get("RST"));
        waitTurn();


        System.out.println(constantsMap.get("BLUE") + ">Now it's your turn!"+ constantsMap.get("RST"));
    }
}

