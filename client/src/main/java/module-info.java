module it.polimi.ingsw.eriantysclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.fasterxml.jackson.databind;

    opens it.polimi.ingsw.javangers.client.launcher to javafx.fxml;
    exports it.polimi.ingsw.javangers.client.launcher;
    opens it.polimi.ingsw.javangers.client.controller to javafx.fxml;
    exports it.polimi.ingsw.javangers.client.controller;
}