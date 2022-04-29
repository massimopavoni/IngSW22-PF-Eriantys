package it.polimi.ingsw.javangers.client.launcher;

import it.polimi.ingsw.javangers.client.cli.launcher.CLILauncher;
import it.polimi.ingsw.javangers.client.gui.launcher.GUILauncherApplication;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientLauncher {

    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out));
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    private static final String DEFAULT_SERVER_PORT = "50666";
    private static String ServerIP = null;
    private static int ServerPort= 0;


    public static String chooseInterfaceMode() {
        String interfaceMode = null;
        while (interfaceMode == null) {
            try {
                interfaceMode = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (interfaceMode != null) {
                if (interfaceMode.equalsIgnoreCase("cli")) interfaceMode = "CLI";
                else if (interfaceMode.equalsIgnoreCase("gui")) interfaceMode = "GUI";
                else {
                    System.out.println(">Please insert correct input [cli/gui]:");
                    System.out.print(">");
                    interfaceMode = null;
                }
            }
        }
        return interfaceMode;
    }

    private static boolean isValidIp(String ip) {
        String regex = "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\." +
                "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";
        ;
        Pattern p = Pattern.compile(regex);
        if (ip == null) {
            return false;
        }
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    public static final String ERIANTYS =
        "   ▄████████    ▄████████  ▄█     ▄████████ ███▄▄▄▄       ███     ▄██   ▄      ▄████████ \n"
          +  "  ███    ███   ███    ███ ███    ███    ███ ███▀▀▀██▄ ▀█████████▄ ███   ██▄   ███    ███ \n"
          +  "  ███    █▀    ███    ███ ███▌   ███    ███ ███   ███    ▀███▀▀██ ███▄▄▄███   ███    █▀  \n"
          +  " ▄███▄▄▄      ▄███▄▄▄▄██▀ ███▌   ███    ███ ███   ███     ███   ▀ ▀▀▀▀▀▀███   ███        \n"
          +  "▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ███▌ ▀███████████ ███   ███     ███     ▄██   ███ ▀███████████ \n"
          +  "  ███    █▄  ▀███████████ ███    ███    ███ ███   ███     ███     ███   ███          ███ \n"
          +  "  ███    ███   ███    ███ ███    ███    ███ ███   ███     ███     ███   ███    ▄█    ███ \n"
          +  "  ██████████   ███    ███ █▀     ███    █▀   ▀█   █▀     ▄████▀    ▀█████▀   ▄████████▀  \n"
          +  "               ███    ███                                                                ";

    private static boolean isValidPort(String port) {
        String regex = "^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
        ;
        Pattern p = Pattern.compile(regex);
        if (port == null) {
            return false;
        }
        Matcher m = p.matcher(port);
        return m.matches();
    }

    private static String chooseServerIp() {
        String serverIp = null;
        System.out.println(">Please insert the ip of server [default: 127.0.0.1]:");
        System.out.print(">");

        while (serverIp == null) {
            try {
                serverIp = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (serverIp != null && serverIp.isEmpty()){
                serverIp = DEFAULT_SERVER_ADDRESS;
            }
            if (!isValidIp(serverIp)) {
                serverIp = null;
                System.out.println(">Please insert correct input [default: 127.0.0.1]:");
                System.out.print(">");
            }
        }
        return serverIp;
    }

    public static int chooseServerPort() {
        String serverPortString = null;
        int serverPort = 0;
        System.out.println(">Please insert port of server [default: 50666]:");
        System.out.print(">");
        while (serverPortString == null) {
            try {
                serverPortString = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (serverPortString != null && serverPortString.isEmpty()){
                serverPortString = DEFAULT_SERVER_PORT;
            }
            if (!isValidPort(serverPortString)) {
                serverPortString = null;

                System.out.println(">Please insert correct input [default: 50666]:");
                System.out.print(">");
            }
        }
        serverPort = Integer.parseInt(serverPortString);
        return serverPort;
    }
    public static boolean Parser_wait(){
    return (ServerIP.equals("127.0.0.1") && ServerPort == 50666);
    };
    public static void Dispatcher_send(String serverIP, int serverPort){
        ServerIP = serverIP;
        ServerPort = serverPort;
    };
    public static void main(String[] args) throws IOException {
        String serverIP = null;
        int serverPort = 0;

        System.out.println(ERIANTYS);
        System.out.println(">Welcome to Eriantys");
        do {
        serverIP = chooseServerIp();
        serverPort = chooseServerPort();
        Dispatcher_send(serverIP, serverPort);
        if (!Parser_wait()){
        System.out.println(">ERROR: WRONG SERVER IP OR PORT");
        System.out.println(">Please try again");
        }
        }while(!Parser_wait());

        System.out.println(">Insert if you want to remain on CLI or play on GUI [cli/gui]:");
        System.out.print(">");
        if (Objects.equals(chooseInterfaceMode(), "GUI")) {
            new Thread(() -> GUILauncherApplication.main(args)).start();
            System.out.println("GUI started");
        } else {
            new Thread(() -> CLILauncher.main(args)).start();
        }
    }
}
