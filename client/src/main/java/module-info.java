module it.polimi.ingsw.eriantysclient {
    requires javafx.controls;
    requires javafx.fxml;
    exports it.polimi.ingsw.javangers.client.gui.launcher;
    opens it.polimi.ingsw.javangers.client.gui.launcher to javafx.fxml;
}