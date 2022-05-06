package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GUILauncherController extends View implements Initializable {

    private final Integer[] possibleNumberOfPlayer = {2, 3};
    private final String[] possibleTowerColor = {"BLACK", "WHITE", "GRAY"};
    @FXML
    //non deve essere final
    private ChoiceBox<Integer> exactPlayersNumber;
    @FXML
    //non deve essere final
    private ChoiceBox<String> towerColor;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private MenuButton create_join_MenuBotton;
    @FXML
    private TextField username;
    @FXML
    private ToggleButton expertMode;


    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUILauncherController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        super(directivesDispatcher, directivesParser);
        exactPlayersNumber = new ChoiceBox<>();
        towerColor = new ChoiceBox<>();
    }


    @FXML
    @Override
    protected void createGame() {
        //da continuare
        directivesDispatcher.createGame(username.getCharacters().toString(),exactPlayersNumber.getValue(),expertMode.isSelected(),wizardType,towerColor.getValue());
    }

    @FXML
    protected void switchToCreateMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUILauncherApplication.class.getResource("createGame-menu.fxml"));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            stage = (Stage) create_join_MenuBotton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void selectWizard(MouseEvent event) {
        wizardType = ((ImageView)event.getSource()).getId();
    }

    @FXML
    @Override
    protected void joinGame() {
    }

    @Override
    protected void waitForStart() {

    }

    @Override
    protected void startGame() {

    }

    @Override
    protected void startShow() {

    }

    @Override
    protected void updateGame() {

    }

    @Override
    protected void showAbort(String message) {

    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void closeGame(List<String> winners) {

    }

    @Override
    protected void enableActions() {

    }

    @Override
    protected void waitTurn() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exactPlayersNumber.getItems().addAll(possibleNumberOfPlayer);
        towerColor.getItems().addAll(possibleTowerColor);
    }
}