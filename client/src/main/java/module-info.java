module it.polimi.ingsw.eriantysclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports it.polimi.ingsw.javangers.client.gui.launcher;
    opens it.polimi.ingsw.javangers.client.gui.launcher to javafx.fxml;
    exports it.polimi.ingsw.javangers.client.controller;
    opens it.polimi.ingsw.javangers.client.controller to com.fasterxml.jackson.databind;
}