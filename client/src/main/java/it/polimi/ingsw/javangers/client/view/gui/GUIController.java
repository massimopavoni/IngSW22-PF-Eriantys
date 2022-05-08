package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.MessageType;
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

public class GUIController extends View implements Initializable {

    private final Integer[] possibleNumberOfPlayer = {2, 3};
    private final String[] possibleTowerColor = {"BLACK", "WHITE", "GRAY"};
    private final String[] possibleCreateJoin = {"CREATE", "JOIN"};
    private GUIApplication application;
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
    //non deve essere final
    private ChoiceBox<String> create_join_ChoiceBox;
    @FXML
    private TextField username;
    @FXML
    private CheckBox expertMode;
    @FXML
    private Label errorMessage;
    @FXML
    private Button confirmButton;
    Alert errorAlert;


    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUIController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        super(directivesDispatcher, directivesParser);
        application = new GUIApplication();
        exactPlayersNumber = new ChoiceBox<>();
        towerColor = new ChoiceBox<>();
        create_join_ChoiceBox = new ChoiceBox<>();
        errorAlert = new Alert(Alert.AlertType.ERROR);
    }

    @Override
    public void main(String[] args) {

    }


    protected void openNewStage(Button button, String resourceName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(resourceName));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            stage = (Stage) button.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }


    @FXML
    @Override
    protected void createGame() {
        if (username.getCharacters() == null || exactPlayersNumber.getValue() == null || wizardType == null || towerColor.getValue() == null)
            alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(username.getCharacters().toString())) {
                alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            } else {
                directivesDispatcher.createGame(username.getCharacters().toString(), exactPlayersNumber.getValue(), expertMode.isSelected(), wizardType, towerColor.getValue());
                this.previousMessageType = MessageType.CREATE;
                application.switchScene("loading-page.fxml");
            }
        }
    }

    protected void alertMessage(String headerText, String contentText) {
        errorAlert.setHeaderText(headerText);
        errorAlert.setContentText(contentText);
        errorAlert.showAndWait();
    }

    @FXML
    protected void switchCreateJoin() {
        if (create_join_ChoiceBox.getValue() != null) {
            if (create_join_ChoiceBox.getValue().equals("CREATE"))
                openNewStage(confirmButton, "createGame-menu.fxml");
            else
                openNewStage(confirmButton, "joinGame-menu.fxml");
        } else {
            alertMessage("Empty choice", "Please select one option");
        }

    }

    @FXML
    protected void selectWizard(MouseEvent event) {
        wizardType = ((ImageView) event.getSource()).getId();
    }

    @FXML
    @Override
    protected void joinGame() {
        if (username.getCharacters() == null || wizardType == null || towerColor.getValue() == null)
            alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(username.getCharacters().toString()))
                alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            else {
                directivesDispatcher.addPlayer(username.getCharacters().toString(), wizardType, towerColor.getValue());
                this.previousMessageType = MessageType.PLAYER;
                application.switchScene("loading-page.fxml");
            }
        }
    }

    @Override
    @FXML
    protected void waitForStart() {
        //questa funzione viene chiamata dalla view che è sbloccata dal parser
        //visualizzare schermata attendo nuovi player
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
    protected void returnToMainMenu() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exactPlayersNumber.getItems().addAll(possibleNumberOfPlayer);
        towerColor.getItems().addAll(possibleTowerColor);
        create_join_ChoiceBox.getItems().addAll(possibleCreateJoin);
    }
}
