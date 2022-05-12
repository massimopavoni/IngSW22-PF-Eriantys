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
    private TextField fxmlUsername;
    @FXML
    private CheckBox expertMode;
    @FXML
    private Label errorMessage;
    @FXML
    private Button confirmButton;
    @FXML
    private Label loadingInfo;
    Alert errorAlert;
    private final GUIGameDisplayer guiGameDisplayer;


    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUIController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser, Stage stage) {
        super(directivesDispatcher, directivesParser);
        this.stage = stage;
        this.application = new GUIApplication();
        this.exactPlayersNumber = new ChoiceBox<>();
        this.towerColor = new ChoiceBox<>();
        this.create_join_ChoiceBox = new ChoiceBox<>();
        this.errorAlert = new Alert(Alert.AlertType.ERROR);
        this.guiGameDisplayer = new GUIGameDisplayer(directivesParser, this.stage);
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
        if (this.fxmlUsername.getCharacters() == null || this.exactPlayersNumber.getValue() == null || this.wizardType == null || this.towerColor.getValue() == null)
            alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(fxmlUsername.getCharacters().toString())) {
                alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            } else {
                this.username = this.fxmlUsername.getCharacters().toString();
                this.directivesDispatcher.createGame(this.username, this.exactPlayersNumber.getValue(), this.expertMode.isSelected(), this.wizardType, this.towerColor.getValue());
                this.previousMessageType = MessageType.CREATE;
                //this.application.switchScene(this.application.getStage(), "loading-page.fxml");
            }
        }
    }

    @FXML
    @Override
    protected void joinGame() {
        if (this.fxmlUsername.getCharacters() == null || wizardType == null || towerColor.getValue() == null)
            alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(this.fxmlUsername.getCharacters().toString()))
                alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            else {
                this.username = this.fxmlUsername.getCharacters().toString();
                this.directivesDispatcher.addPlayer(username, this.wizardType, this.towerColor.getValue());
                this.previousMessageType = MessageType.PLAYER;

            }
        }
    }

    @Override
    @FXML
    protected void waitForStart() {
        Platform.runLater(() -> {
            this.stage.close();
            openNewStage("loading-page.fxml");
            this.loadingInfo.setText("Waiting start game");
        });

        //questa funzione viene chiamata dalla view che è sbloccata dal parser
        //visualizzare schermata attendo nuovi player
    }

    @Override
    protected void startGame() {
        this.directivesDispatcher.startGame(this.fxmlUsername.getCharacters().toString());
        this.previousMessageType = MessageType.START;
    }

    @Override
    protected void startShow() {
        Platform.runLater(() ->{
            this.stage.close();
            guiGameDisplayer.openNewStage();
        });
        try {
            this.guiGameDisplayer.displayGame(fxmlUsername.getCharacters().toString());
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateGame() {

    }

    @Override
    protected void showAbort(String message) {

    }

    @Override
    protected void showError(String message) {
        Platform.runLater(() -> {
            stage.close();
            Platform.runLater(() -> alertMessage(message, "Please retry"));
       });
    }

    @Override
    protected void closeGame(List<String> winners) {

    }

    @Override
    protected void enableActions() {


    }

    @Override
    protected void waitTurn() {
        Platform.runLater(() -> {
            openNewStage("loading-page.fxml");
            this.loadingInfo.setText("Waiting your turn");
        });
    }

    @Override
    protected void returnToMainMenu() {
        Platform.runLater(() -> {
            this.openNewStage("create-join.fxml");
       });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exactPlayersNumber.getItems().addAll(possibleNumberOfPlayer);
        towerColor.getItems().addAll(possibleTowerColor);
        create_join_ChoiceBox.getItems().addAll(possibleCreateJoin);
    }


}
