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

/**
 * Class representing the gui view, and acting as main controller for JavaFX application.
 */
public class GUIController extends View implements Initializable {
    /**
     * Main application stage.
     */
    private final Stage stage;
    /**
     * Secondary controller for main game scene.
     */
    private final GUIGameDisplayer guiGameDisplayer;
    /**
     * Alert dialog for errors.
     */
    private final Alert errorAlert;
    /**
     * Tower color ChoiceBox.
     */
    @FXML
    private final ChoiceBox<String> towerColorChoice;
    /**
     * Exact players number ChoiceBox.
     */
    @FXML
    private final ChoiceBox<Integer> exactPlayersNumberChoice;
    /**
     * Create vs join flag.
     */
    private boolean isInCreate;
    /**
     * Username TextField.
     */
    @FXML
    private TextField usernameField;
    /**
     * Exact players number Label.
     */
    @FXML
    private Label exactPlayersNumberLabel;
    /**
     * Expert mode CheckBox.
     */
    @FXML
    private CheckBox expertModeCheck;

    /**
     * Constructor for gui view, initializing directives dispatcher and parser, application stage, application controls and secondary controller.
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

    /**
     * Method for view start (not implemented as it is not used for gui).
     *
     * @param args arguments
     */
    @Override
    public void main(String[] args) {
        // Not used in gui view
    }

    /**
     * Method for game creation.
     */
    @Override
    protected void createGame() {
        if (this.usernameField.getCharacters() == null ||
                this.towerColorChoice.getValue() == null ||
                this.wizardType == null ||
                this.exactPlayersNumberChoice.getValue() == null)
            this.showErrorAlert("Empty field", "Please provide all required information.");
        else {
            if (!View.isValidUsername(this.usernameField.getCharacters().toString())) {
                this.showErrorAlert("Invalid username",
                        "Please use a valid username\n(min 4/max 32 characters, alphanumeric + underscores).");
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

    /**
     * Method for game joining.
     */
    @Override
    protected void joinGame() {
        if (this.usernameField.getCharacters() == null ||
                this.towerColorChoice.getValue() == null ||
                this.wizardType == null)
            this.showErrorAlert("Empty field", "Please provide all required information.");
        else {
            if (!View.isValidUsername(this.usernameField.getCharacters().toString()))
                this.showErrorAlert("Invalid username",
                        "Please write a correct username\n(min 4/max 32 characters, alphanumeric + underscores).");
            else {
                this.username = this.usernameField.getCharacters().toString();
                this.towerColor = this.towerColorChoice.getValue().toUpperCase();
                this.directivesDispatcher.addPlayer(this.username, this.wizardType, this.towerColor);
                this.previousMessageType = MessageType.PLAYER;
            }
        }
    }

    /**
     * Method for game start wait loading.
     */
    @Override
    protected void waitForStart() {
        this.openNewStage("loading.fxml");
    }

    /**
     * Method for game start call.
     */
    @Override
    protected void startGame() {
        this.directivesDispatcher.startGame(this.username);
    }

    /**
     * Method for first game show.
     */
    @Override
    protected void startShow() {
        this.guiGameDisplayer.open();
        try {
            this.guiGameDisplayer.displayGame(this.username);
            this.previousMessageType = MessageType.START;
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new ViewException(e.getMessage(), e);
        }
    }

    /**
     * Method for update game show.
     */
    @Override
    protected void updateGame() {
        try {
            this.guiGameDisplayer.displayGame(this.username);
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new ViewException(e.getMessage(), e);
        }
    }

    /**
     * Method for abort message show.
     *
     * @param message abort message
     */
    @Override
    protected void showAbort(String message) {
        this.showErrorAlert(message, "Please create a new game or wait to join a new game");
    }

    /**
     * Method for error message show.
     *
     * @param message error message
     */
    @Override
    protected void showError(String message) {
        this.showErrorAlert(message, "Please retry");
    }

    /**
     * Method for closing endgame show.
     *
     * @param winners winners list
     */
    @Override
    protected void closeGame(List<String> winners) {
        this.guiGameDisplayer.displayEndgame(winners);
    }

    /**
     * Method for enabling player actions.
     */
    @Override
    protected void enableActions() {
        this.disableAllButtons();
        this.enableActionButtons();
        this.guiGameDisplayer.setYourTurnMessage("It's your turn");
        this.previousMessageType = MessageType.ACTION;
    }

    /**
     * Method for waiting turn and not allowing player actions.
     */
    @Override
    protected void waitTurn() {
        this.disableAllButtons();
        this.guiGameDisplayer.setYourTurnMessage("Wait your turn");
    }

    /**
     * Method for returning to main menu.
     */
    @Override
    protected void returnToMainMenu() {
        this.openNewStage("createJoin.fxml");
    }

    /**
     * Main view method override for JavaFX main thread.
     */
    @Override
    public void updateView() {
        Platform.runLater(super::updateView);
    }

    /**
     * Method for opening new stage in application.
     *
     * @param resourceName fxml file resource to load
     */
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
            throw new ViewException(e.getMessage(), e);
        }
    }

    /**
     * Show error alert message.
     *
     * @param header  header text
     * @param content content text
     */
    private void showErrorAlert(String header, String content) {
        this.errorAlert.setHeaderText(header);
        this.errorAlert.setContentText(content);
        this.errorAlert.showAndWait();
    }

    /**
     * Enable action button for player, depending on available actions for current phase.
     */
    private void enableActionButtons() {
        try {
            for (String action : this.directivesParser.getAvailableActions()) {
                switch (action) {
                    case "FillClouds" -> this.guiGameDisplayer.setDisableFillClouds(false);
                    case "PlayAssistantCard" -> this.guiGameDisplayer.setDisablePlayAssistantCard(false);
                    case "MoveStudents" -> this.guiGameDisplayer.setDisableMoveStudents(false);
                    case "MoveMotherNature" -> this.guiGameDisplayer.setDisableMoveMotherNature(false);
                    case "ChooseCloud" -> this.guiGameDisplayer.setDisableChooseCloud(false);
                    case "ActivateCharacterCard" -> this.guiGameDisplayer.setDisableActivateCharacterCard(
                            !this.directivesParser.getPlayersEnabledCharacterCard().get(this.username));
                    default -> throw new ViewException(String.format("Unknown action button: %s", action));
                }
            }
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new ViewException(e.getMessage(), e);
        }
    }

    /**
     * Disable all action buttons.
     */
    private void disableAllButtons() {
        this.guiGameDisplayer.setDisableFillClouds(true);
        this.guiGameDisplayer.setDisablePlayAssistantCard(true);
        this.guiGameDisplayer.setDisableMoveStudents(true);
        this.guiGameDisplayer.setDisableMoveMotherNature(true);
        this.guiGameDisplayer.setDisableChooseCloud(true);
        this.guiGameDisplayer.setDisableActivateCharacterCard(true);
    }

    /**
     * Controller initialize method.
     *
     * @param url            url location
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.towerColorChoice.getItems().addAll(View.TOWER_COLORS_MAPPINGS.values());
        this.exactPlayersNumberChoice.getItems().addAll(MIN_PLAYERS_NUMBER, MAX_PLAYERS_NUMBER);
    }

    /**
     * Show game create menu event.
     */
    @FXML
    private void switchCreate() {
        this.wizardType = null;
        this.isInCreate = true;
        this.openNewStage("startMenu.fxml");
    }

    /**
     * Show game join menu event.
     */
    @FXML
    private void switchJoin() {
        this.wizardType = null;
        this.isInCreate = false;
        this.openNewStage("startMenu.fxml");
        this.exactPlayersNumberLabel.setVisible(false);
        this.exactPlayersNumberChoice.setVisible(false);
        this.expertModeCheck.setVisible(false);
    }

    /**
     * Select wizard event.
     *
     * @param event mouse click event
     */
    @FXML
    private void selectWizard(MouseEvent event) {
        if (this.wizardType != null)
            this.stage.getScene().lookup(String.format("#%sFrame",
                    this.wizardType.toLowerCase())).setVisible(false);
        this.wizardType = ((Node) event.getSource()).getId().toUpperCase();
        this.stage.getScene().lookup(String.format("#%sFrame",
                this.wizardType.toLowerCase())).setVisible(true);
    }

    /**
     * Confirm create/join event.
     */
    @FXML
    private void confirmCreateJoin() {
        if (this.isInCreate)
            this.createGame();
        else
            this.joinGame();
    }
}
