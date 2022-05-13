package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.MessageType;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.application.Platform;
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

    // si puo rendere piu generica
    private final Integer[] possibleNumberOfPlayer = {MIN_PLAYERS_NUMBER, MAX_PLAYERS_NUMBER};
    // si puo rendere piu generica
    private final String[] possibleTowerColor = {AVAILABLE_TOWER_COLORS.get("b"), AVAILABLE_TOWER_COLORS.get("w"), AVAILABLE_TOWER_COLORS.get("g")};
    private final String[] possibleCreateJoin = {"CREATE", "JOIN"};
    private final Alert errorAlert;
    private final GUIGameDisplayer guiGameDisplayer;
    //private GUIApplication application;
    @FXML
    //non deve essere final
    private ChoiceBox<Integer> fxmlExactPlayersNumber;
    @FXML
    //non deve essere final
    private ChoiceBox<String> fxmlTowerColor;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    //non deve essere final
    private ChoiceBox<String> create_join_ChoiceBox;
    @FXML
    private TextField fxmlUsername;
    @FXML
    private CheckBox fxmlExpertMode;
    @FXML
    private Label errorMessage;
    @FXML
    private Button confirmButton;
    @FXML
    private Label loadingInfo;


    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUIController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser, Stage stage) {
        super(directivesDispatcher, directivesParser);
        this.stage = stage;
        //this.application = new GUIApplication();
        this.fxmlExactPlayersNumber = new ChoiceBox<>();
        this.fxmlTowerColor = new ChoiceBox<>();
        this.create_join_ChoiceBox = new ChoiceBox<>();
        this.errorAlert = new Alert(Alert.AlertType.ERROR);
        this.guiGameDisplayer = new GUIGameDisplayer(directivesParser, this.stage);
    }

    @Override
    public void main(String[] args) {

    }

    @Override
    public void updateView() {
        Platform.runLater(super::updateView);
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


    protected void openNewStage(String resourceName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            //stage = (Stage) this.application.getStage().getScene().getWindow();
            scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void selectWizard(MouseEvent event) {
        this.wizardType = ((ImageView) event.getSource()).getId();
    }

    protected void alertMessage(String headerText, String contentText) {
        this.errorAlert.setHeaderText(headerText);
        this.errorAlert.setContentText(contentText);
        this.errorAlert.showAndWait();

    }

    @FXML
    protected void switchCreateJoin() {
        if (this.create_join_ChoiceBox.getValue() != null) {
            if (this.create_join_ChoiceBox.getValue().equals("CREATE"))
                openNewStage(this.confirmButton, "createGame-menu.fxml");
            else
                openNewStage(this.confirmButton, "joinGame-menu.fxml");
        } else {
            alertMessage("Empty choice", "Please select one option");
        }

    }

    @FXML
    @Override
    protected void createGame() {
        if (this.fxmlUsername.getCharacters() == null || this.fxmlExactPlayersNumber.getValue() == null || this.wizardType == null || this.fxmlTowerColor.getValue() == null)
            alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(fxmlUsername.getCharacters().toString())) {
                alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            } else {
                this.username = this.fxmlUsername.getCharacters().toString();
                this.exactPlayersNumber = this.fxmlExactPlayersNumber.getValue();
                this.expertMode = this.fxmlExpertMode.isSelected();
                this.towerColor = this.fxmlTowerColor.getValue();
                this.directivesDispatcher.createGame(this.username, this.exactPlayersNumber, this.expertMode, this.wizardType, this.towerColor);
                this.previousMessageType = MessageType.CREATE;
                //this.application.switchScene(this.application.getStage(), "loading-page.fxml");
            }
        }
    }

    @FXML
    @Override
    protected void joinGame() {
        if (this.fxmlUsername.getCharacters() == null || wizardType == null || fxmlTowerColor.getValue() == null)
            alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(this.fxmlUsername.getCharacters().toString()))
                alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            else {
                this.username = this.fxmlUsername.getCharacters().toString();
                this.towerColor = this.fxmlTowerColor.getValue();
                this.directivesDispatcher.addPlayer(this.username, this.wizardType, this.towerColor);
                this.previousMessageType = MessageType.PLAYER;

            }
        }
    }

    @Override
    @FXML
    protected void waitForStart() {
        //this.stage.close();
        openNewStage("loading-page.fxml");
        this.loadingInfo.setText("Waiting start game");

        //questa funzione viene chiamata dalla view che è sbloccata dal parser
        //visualizzare schermata attendo nuovi player
    }

    @Override
    protected void startGame() {
        this.directivesDispatcher.startGame(this.username);
        this.previousMessageType = MessageType.START;
    }

    @Override
    protected void startShow() {
        //this.stage.close();
        guiGameDisplayer.openNewStage();
        try {
            this.guiGameDisplayer.displayGame(this.username);
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateGame() {

    }

    @Override
    protected void showAbort(String message) {
        alertMessage(message, "Please create a new game or wait to join a new game");
    }

    @Override
    protected void showError(String message) {
        //stage.close();
        alertMessage(message, "Please retry");

    }

    @Override
    protected void closeGame(List<String> winners) {

    }

    @Override
    protected void enableActions() {
        // preso in considerazione da thom da continuare

    }

    @Override
    protected void waitTurn() {

        openNewStage("loading-page.fxml");
        this.loadingInfo.setText("Waiting your turn");
    }

    @Override
    protected void returnToMainMenu() {
        this.openNewStage("create-join.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxmlExactPlayersNumber.getItems().addAll(possibleNumberOfPlayer);
        fxmlTowerColor.getItems().addAll(possibleTowerColor);
        create_join_ChoiceBox.getItems().addAll(possibleCreateJoin);
    }


}
