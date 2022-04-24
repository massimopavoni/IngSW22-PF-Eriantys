package it.polimi.ingsw.javangers.server.launcher;

import it.polimi.ingsw.javangers.server.controller.network.ConnectionsPool;
import it.polimi.ingsw.javangers.server.controller.network.PlayerConnection;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler implements Runnable {

    private static MessageHandler singleton = null;
    private final ConnectionsPool connectionsPool;
    private final Object waitLock;
    private final int pollingTime;
    private List<PlayerConnection> playerConnectionsList;

    private MessageHandler(ConnectionsPool connectionsPool, int pollingTime) {
        this.connectionsPool = connectionsPool;
        this.waitLock = new Object();
        this.pollingTime = pollingTime;

    }

    public static MessageHandler getInstance(ConnectionsPool connectionsPool, int pollingTime) {
        if (singleton == null) singleton = new MessageHandler(connectionsPool, pollingTime);
        return singleton;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            try {
                synchronized (this.waitLock) {
                    this.waitLock.wait(this.pollingTime);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.playerConnectionsList = this.connectionsPool.getPlayerConnectionsList();
            for (PlayerConnection playerConnection : this.playerConnectionsList) {
                if (playerConnection.getInputDirective() != null && playerConnection.getInputDirective().equalsIgnoreCase("\"buongiorno\"")) {
                    System.out.println("entro nel if buongiorno in message control");
                    playerConnection.setOutputDirective("buonasera");
                    changeListOrder(playerConnectionsList.indexOf(playerConnection));
                }
                if (playerConnection.getInputDirective() != null && playerConnection.getInputDirective().equalsIgnoreCase("\"cane\"")) {
                    playerConnection.setOutputDirective("gatto");
                    changeListOrder(playerConnectionsList.indexOf(playerConnection));
                } else if (playerConnection.getInputDirective() != null && !playerConnection.getInputDirective().equalsIgnoreCase("")) {
                    playerConnection.setOutputDirective("input non trovato");
                    changeListOrder(playerConnectionsList.indexOf(playerConnection));
                }
            }
        }
    }


    private void changeListOrder(int playerConnectionPosition) {
        List<PlayerConnection> modifiedPlayerConnectionsList = new ArrayList<>();
        for (int i = playerConnectionPosition + 1; i < playerConnectionsList.size(); i++) {
            modifiedPlayerConnectionsList.add(playerConnectionsList.get(i));
        }
        for (int i = 0; i <= playerConnectionPosition; i++) {
            modifiedPlayerConnectionsList.add(playerConnectionsList.get(i));
        }
        this.playerConnectionsList = modifiedPlayerConnectionsList;
        for (PlayerConnection playerConnection : playerConnectionsList) {
            System.out.println(playerConnection.id);
        }
    }

}

