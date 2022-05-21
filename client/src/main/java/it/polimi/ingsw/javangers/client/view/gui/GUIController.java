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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GUIController extends View implements Initializable {
    private final Alert errorAlert;
    private final GUIGameDisplayer guiGameDisplayer;
    private boolean isInCreate;
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
    private TextField fxmlUsername;
    @FXML
    private CheckBox fxmlExpertMode;
    @FXML
    private Label errorMessage;
    @FXML
    private Label loadingInfo;
    @FXML
    private Label labelNumberOfPlayers;
    @FXML
    private GridPane wizardsGridPane;

    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUIController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser, Stage stage) {
        super(directivesDispatcher, directivesParser);
        this.stage = stage;
        this.fxmlExactPlayersNumber = new ChoiceBox<>();
        this.fxmlTowerColor = new ChoiceBox<>();
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


    private Background displayBackGround(String resource) {
        Image img = new Image(GUIGameDisplayer.class.getResource(resource).toString());
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        return new Background(bImg);
    }


    private void openNewStage(String resourceName, String backGroundResource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.sizeToScene();
            AnchorPane anchorPane = fxmlLoader.getRoot();
            anchorPane.setBackground(this.displayBackGround(backGroundResource));
            this.stage.hide();
            this.stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void selectWizard(MouseEvent event) {
        setInvisibleWizardFrames();
        ImageView imv = new ImageView();
        ImageView frame = new ImageView();
        this.wizardType = ((ImageView) event.getSource()).getId();
        frame = (ImageView) this.wizardsGridPane.lookup("#frame_"+this.wizardType);
        frame.setVisible(true);
    }

    private void alertMessage(String headerText, String contentText) {
        this.errorAlert.setHeaderText(headerText);
        this.errorAlert.setContentText(contentText);
        this.errorAlert.showAndWait();

    }

    private void setInvisibleWizardFrames(){
        List<String> cardNameList = new ArrayList<>(AVAILABLE_WIZARD_TYPES.values());
        for (String s : cardNameList) {
            ImageView imv = null;
            imv = (ImageView) this.wizardsGridPane.lookup("#frame_" + s);
            imv.setVisible(false);
        }
    }

    private void displayWizards() {
        Image frame = new Image(GUIController.class.getResource("images/cardSelectionFrame.png").toString());
        List<String> cardNameList = new ArrayList<>(AVAILABLE_WIZARD_TYPES.values());
        for (int i = 0; i < cardNameList.size(); i++) {
            Image image = new Image(GUIController.class.getResource("images/wizards/" + cardNameList.get(i).toLowerCase() + ".png").toString());
            ImageView imv = new ImageView();
            ImageView frameImv = new ImageView();
            imv.setImage(image);
            imv.setId(cardNameList.get(i));
            imv.setFitWidth(81);
            imv.setFitHeight(120);
            imv.setOnMouseClicked(this::selectWizard);
            frameImv.setImage(frame);
            frameImv.setId("frame_"+cardNameList.get(i));
            frameImv.setFitWidth(90);
            frameImv.setFitHeight(126);
            frameImv.setVisible(false);
            wizardsGridPane.add(imv, i % 2, i / 2);
            wizardsGridPane.add(frameImv, i % 2, i / 2);
        }
    }

    @FXML
    private void switchCreate() {
        this.isInCreate = true;
        openNewStage("start-menu.fxml", "images/startMenuBG.png");
        this.displayWizards();
    }

    @FXML
    private void switchJoin() {
        this.isInCreate = false;
        openNewStage("start-menu.fxml", "images/startMenuBG.png");
        this.displayWizards();
        fxmlExactPlayersNumber.setVisible(false);
        fxmlExpertMode.setVisible(false);
        labelNumberOfPlayers.setVisible(false);
    }

    @FXML
    private void redirectCreateJoin() {
        if (this.isInCreate)
            this.createGame();
        else
            this.joinGame();
    }

    @Override
    protected void createGame() {
        if (this.fxmlUsername.getCharacters() == null || this.fxmlExactPlayersNumber.getValue() == null || this.wizardType == null || this.fxmlTowerColor.getValue() == null)
            this.alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(fxmlUsername.getCharacters().toString())) {
                this.alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
            } else {
                this.username = this.fxmlUsername.getCharacters().toString();
                this.exactPlayersNumber = this.fxmlExactPlayersNumber.getValue();
                this.expertMode = this.fxmlExpertMode.isSelected();
                this.towerColor = this.fxmlTowerColor.getValue();
                this.directivesDispatcher.createGame(this.username, this.exactPlayersNumber, this.expertMode, this.wizardType, this.towerColor);
                this.previousMessageType = MessageType.CREATE;
            }
        }
    }

    @Override
    protected void joinGame() {
        if (this.fxmlUsername.getCharacters() == null || wizardType == null || fxmlTowerColor.getValue() == null)
            this.alertMessage("Empty field", "Please fill all the fields");
        else {
            if (!isValidUsername(this.fxmlUsername.getCharacters().toString()))
                this.alertMessage("Invalid username", "Please write a correct username\n(min4/max32 characters, alphanumeric + underscores)");
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
        openNewStage("loading-page.fxml", "images/loading.gif");
        this.loadingInfo.setText("Waiting start game");

        //questa funzione viene chiamata dalla view che è sbloccata dal parser
        //visualizzare schermata attendo nuovi player
    }

    @Override
    protected void startGame() {
        this.directivesDispatcher.startGame(this.username);
    }

    @Override
    protected void startShow() {
        guiGameDisplayer.openNewStage("game-view.fxml", "images/gameBoard.png" );
        try {
            this.guiGameDisplayer.displayGame(this.username);
            this.previousMessageType = MessageType.START;
        } catch (DirectivesParser.DirectivesParserException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void updateGame() {
        //forse da completare
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
        this.alertMessage(this.directivesParser.getCurrentPhase().getValue(), message);
    }

    @Override
    protected void closeGame(List<String> winners) {
        this.guiGameDisplayer.displayEndGame(winners);
    }

    @Override
    protected void enableActions() {
        this.disableAllButtons();
        this.enableActionButtons();
        this.guiGameDisplayer.setYourTurnMessage("It's your turn!");
        this.previousMessageType = MessageType.ACTION;
        //forse da continuare
        //aggiungere è il tuo turno
    }

    @Override
    protected void waitTurn() {
        this.disableAllButtons();
        this.guiGameDisplayer.setYourTurnMessage("Wait your turn!");
        //da aggiungere in una label il wait turn
    }

    private void enableActionButtons() {
        // da aggiungere controlli
        try {
            for (String action : directivesParser.getAvailableActions()) {
                switch (action) {
                    case "FillClouds" -> guiGameDisplayer.getFillCloudsButton().setDisable(false);
                    case "PlayAssistantCard" -> guiGameDisplayer.getPlayAssistantCardButton().setDisable(false);
                    case "MoveStudents" -> guiGameDisplayer.getMoveStudentsButton().setDisable(false);
                    case "MoveMotherNature" -> guiGameDisplayer.getMoveMotherNatureButton().setDisable(false);
                    case "ChooseCloud" -> guiGameDisplayer.getChooseCloudButton().setDisable(false);
                    case "ActivateCharacterCard" -> guiGameDisplayer.getActivateCharacterCardButton().setDisable(false);
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
        this.openNewStage("create-join.fxml", "images/startMenuBG.png");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxmlExactPlayersNumber.getItems().addAll(MIN_PLAYERS_NUMBER, MAX_PLAYERS_NUMBER);
        fxmlTowerColor.getItems().addAll(AVAILABLE_TOWER_COLORS.values());
    }
}
