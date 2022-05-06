module it.polimi.ingsw.javangers.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens it.polimi.ingsw.javangers.client.view.gui to javafx.fxml;
    opens it.polimi.ingsw.javangers.client.controller to com.fasterxml.jackson.databind;

    exports it.polimi.ingsw.javangers.client.view.gui;
    exports it.polimi.ingsw.javangers.client.controller;
    exports it.polimi.ingsw.javangers.client.controller.directives;
    exports it.polimi.ingsw.javangers.client.view;
}