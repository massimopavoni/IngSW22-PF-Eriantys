package it.polimi.ingsw.javangers.client.launcher;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientLauncherController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}