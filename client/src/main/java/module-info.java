module it.polimi.ingsw.eriantysclient {
    requires javafx.controls;
    requires javafx.fxml;

    opens it.polimi.ingsw.javangers.client.launcher to javafx.fxml;
    exports it.polimi.ingsw.javangers.client.launcher;
    exports it.polimi.ingsw.javangers.client.controller;
    opens it.polimi.ingsw.javangers.client.controller to javafx.fxml;
}