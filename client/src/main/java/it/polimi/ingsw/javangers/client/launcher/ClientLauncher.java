package it.polimi.ingsw.javangers.client.launcher;

import it.polimi.ingsw.javangers.client.cli.launcher.CLILauncher;
import it.polimi.ingsw.javangers.client.gui.launcher.GUILauncherApplication;

import java.io.*;
import java.util.Objects;

public class ClientLauncher {

    private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static final BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out));

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

    public static void main(String[] args) {
        System.out.println(">Welcome to Eriantys");
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
