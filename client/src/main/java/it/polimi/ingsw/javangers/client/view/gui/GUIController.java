package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.MessageType;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GUIController extends View implements Initializable {
    private final Stage stage;
    private final GUIGameDisplayer guiGameDisplayer;
    private final Alert errorAlert;
    @FXML
    private ChoiceBox<String> towerColorChoice;
    @FXML
    private ChoiceBox<Integer> exactPlayersNumberChoice;
    private boolean isInCreate;
    @FXML
    private TextField usernameField;
    @FXML
    private CheckBox expertModeCheck;
    @FXML
    private Label exactPlayersNumberLabel;

    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUIController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser, Stage stage) {
        super(directivesDispatcher, directivesParser);
        this.stage = stage;
        this.towerColorChoice = new ChoiceBox<>();
        this.exactPlayersNumberChoice = new ChoiceBox<>();
        this.errorAlert = new Alert(Alert.AlertType.ERROR);
        this.guiGameDisplayer = new GUIGameDisplayer(directivesParser, directivesDispatcher, this.stage);
    }

    @Override
    public void main(String[] args) {

    }

    @Override
    public void updateView() {
        Platform.runLater(super::updateView);
    }


    private void openNewStage(String resourceName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
            fxmlLoader.setController(this);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.sizeToScene();
            this.stage.hide();
            this.stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void selectWizard(MouseEvent event) {
        if (this.wizardType != null)
            this.stage.getScene().lookup(String.format("#%sFrame",
                    this.wizardType.toLowerCase())).setVisible(false);
        this.wizardType = ((Node) event.getSource()).getId().toUpperCase();
        this.stage.getScene().lookup(String.format("#%sFrame",
                this.wizardType.toLowerCase())).setVisible(true);
    }

    private void alertMessage(String headerText, String contentText) {
        this.errorAlert.setHeaderText(headerText);
        this.errorAlert.setContentText(contentText);
        this.errorAlert.showAndWait();
    }

    @FXML
    private void switchCreate() {
        this.wizardType = null;
        this.isInCreate = true;
        this.openNewStage("startMenu.fxml");
    }

    @FXML
    private void switchJoin() {
        this.wizardType = null;
        this.isInCreate = false;
        this.openNewStage("startMenu.fxml");
        this.exactPlayersNumberLabel.setVisible(false);
        this.exactPlayersNumberChoice.setVisible(false);
        this.expertModeCheck.setVisible(false);
    }

    @FXML
    private void confirmCreateJoin() {
        if (this.isInCreate)
            this.createGame();
        else
            this.joinGame();
    }

    @Override
    protected void createGame() {
        if (this.usernameField.getCharacters() == null ||
                this.towerColorChoice.getValue() == null ||
                this.wizardType == null ||
                this.exactPlayersNumberChoice.getValue() == null)
            this.alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!View.isValidUsername(this.usernameField.getCharacters().toString())) {
                this.alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            } else {
                this.username = this.usernameField.getCharacters().toString();
                this.towerColor = this.towerColorChoice.getValue().toUpperCase();
                this.exactPlayersNumber = this.exactPlayersNumberChoice.getValue();
                this.expertMode = this.expertModeCheck.isSelected();
                this.directivesDispatcher.createGame(this.username, this.exactPlayersNumber,
                        this.expertMode, this.wizardType, this.towerColor);
                this.previousMessageType = MessageType.CREATE;
            }
        }
    }

    @Override
    protected void joinGame() {
        if (this.usernameField.getCharacters() == null ||
                this.towerColorChoice.getValue() == null ||
                this.wizardType == null)
            this.alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!View.isValidUsername(this.usernameField.getCharacters().toString()))
                this.alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            else {
                this.username = this.usernameField.getCharacters().toString();
                this.towerColor = this.towerColorChoice.getValue().toUpperCase();
                this.directivesDispatcher.addPlayer(this.username, this.wizardType, this.towerColor);
                this.previousMessageType = MessageType.PLAYER;
            }
        }
    }

    @Override
    @FXML
    protected void waitForStart() {
        openNewStage("loading.fxml");
    }

    @Override
    protected void startGame() {
        this.directivesDispatcher.startGame(this.username);
    }

    @Override
    protected void startShow() {
        guiGameDisplayer.open();
        try {
            this.guiGameDisplayer.displayGame(this.username);
            this.previousMessageType = MessageType.START;
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void updateGame() {
        try {
            this.guiGameDisplayer.displayGame(this.username);
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void showAbort(String message) {
        this.alertMessage(message, "Please create a new game or wait to join a new game");
    }

    @Override
    protected void showError(String message) {
        this.alertMessage(message, "Please retry");
    }

    @Override
    protected void closeGame(List<String> winners) {
        this.guiGameDisplayer.displayEndgame(winners);
    }

    @Override
    protected void enableActions() {
        this.disableAllButtons();
        this.enableActionButtons();
        this.guiGameDisplayer.setYourTurnMessage("It's your turn!");
        this.previousMessageType = MessageType.ACTION;
    }

    @Override
    protected void waitTurn() {
        this.disableAllButtons();
        this.guiGameDisplayer.setYourTurnMessage("Wait your turn!");
    }

    private void enableActionButtons() {
        try {
            for (String action : directivesParser.getAvailableActions()) {
                switch (action) {
                    case "FillClouds" -> guiGameDisplayer.getFillCloudsButton().setDisable(false);
                    case "PlayAssistantCard" -> guiGameDisplayer.getPlayAssistantCardButton().setDisable(false);
                    case "MoveStudents" -> guiGameDisplayer.getMoveStudentsButton().setDisable(false);
                    case "MoveMotherNature" -> guiGameDisplayer.getMoveMotherNatureButton().setDisable(false);
                    case "ChooseCloud" -> guiGameDisplayer.getChooseCloudButton().setDisable(false);
                    case "ActivateCharacterCard" -> guiGameDisplayer.getActivateCharacterCardButton()
                            .setDisable(!this.directivesParser.getPlayersEnabledCharacterCard().get(this.username));
                }
            }
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new ViewException(e.getMessage());
        }
    }

    private void disableAllButtons() {
        guiGameDisplayer.getFillCloudsButton().setDisable(true);
        guiGameDisplayer.getPlayAssistantCardButton().setDisable(true);
        guiGameDisplayer.getMoveStudentsButton().setDisable(true);
        guiGameDisplayer.getMoveMotherNatureButton().setDisable(true);
        guiGameDisplayer.getChooseCloudButton().setDisable(true);
        guiGameDisplayer.getActivateCharacterCardButton().setDisable(true);
    }

    @Override
    protected void returnToMainMenu() {
        this.guiGameDisplayer.closePopUp();
        this.openNewStage("createJoin.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.towerColorChoice.getItems().addAll(View.TOWER_COLORS_MAPPINGS.values());
        this.exactPlayersNumberChoice.getItems().addAll(MIN_PLAYERS_NUMBER, MAX_PLAYERS_NUMBER);
    }
}
