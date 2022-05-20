package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GUIGameDisplayer {

    private final Alert errorAlert;
    private final Alert messageAlert;
    private final DirectivesParser directivesParser;
    private final DirectivesDispatcher directivesDispatcher;
    private final Stage stage;
    private int previousArchipelagoSize;
    private Scene scene;
    private Parent root;
    private Stage popUpStage;
    private AnchorPane anchorPane;
    private String assistantCardChosen;
    private String characterCardChosen;
    private Boolean firstDisplay;
    private String username;
    private String opponentUsername;
    private String leftUsername;
    @FXML
    private Label currentPhase;
    @FXML
    private Label playersOrder;
    @FXML
    private Button fillCloudsButton;
    @FXML
    private Button playAssistantCardButton;
    @FXML
    private Button moveStudentsButton;
    @FXML
    private Button moveMotherNatureButton;
    @FXML
    private Button chooseCloudButton;
    @FXML
    private Button activateCharacterCardButton;
    @FXML
    private GridPane assistantCardsGridPane;
    @FXML
    private GridPane characterCardsGridPane;
    @FXML
    private Label yourTurn;


    protected GUIGameDisplayer(DirectivesParser directivesParser, DirectivesDispatcher directivesDispatcher, Stage stage) {
        this.directivesParser = directivesParser;
        this.directivesDispatcher = directivesDispatcher;
        this.stage = stage;
        this.errorAlert = new Alert(Alert.AlertType.ERROR);
        this.messageAlert = new Alert(Alert.AlertType.INFORMATION);
        this.firstDisplay = true;
    }

    public void setYourTurnMessage(String message) {
        this.yourTurn.setText(message);
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

    protected void openNewStage(String fxmlFile, String backGroundResource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.sizeToScene();
            this.anchorPane = fxmlLoader.getRoot();
            this.anchorPane.setBackground(this.displayBackGround(backGroundResource));
            this.stage.hide();
            this.stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }


    public void openPopUp(String fxmlFile, int width, int height, String backGroundResource) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
        fxmlLoader.setController(this);
        Parent root;
        this.popUpStage = new Stage();
        try {
            root = fxmlLoader.load();
            this.anchorPane = fxmlLoader.getRoot();
            anchorPane.setBackground(this.displayBackGround(backGroundResource));
            this.popUpStage.setScene(new Scene(root));
            this.popUpStage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image(GUIGameDisplayer.class.getResource("images/logo-cranio.png").toString()));
            this.popUpStage.setWidth(width);
            this.popUpStage.setHeight(height);
            this.popUpStage.setResizable(false);
            this.popUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void displayCurrentPhase() {
        Pair<String, String> currentPhasePair = this.directivesParser.getCurrentPhase();
        this.currentPhase.setText("Current phase: " + currentPhasePair.getKey() + " => " + currentPhasePair.getValue());
    }

    private void displayPlayersOrder(String username) throws DirectivesParser.DirectivesParserException {

        playersOrder.setText("You are " + username + ", Player's order is: " + directivesParser.getPlayersOrder().toString());
    }

    private void setInvisibleCharacterCardsFrames() {
        List<String> cardNameList = new ArrayList<>(directivesParser.getCharacterCardNames());
        for (String s : cardNameList) {
            ImageView imv = null;
            imv = (ImageView) this.characterCardsGridPane.lookup("#frame_" + s);
            imv.setVisible(false);
        }
    }

    private void displayCharacterCards() {
        Image frame = new Image(GUIController.class.getResource("images/selection-frame.png").toString());
        for (int i = 0; i < directivesParser.getCharacterCardNames().size(); i++) {
            Image image = new Image(GUIGameDisplayer.class.getResource("images/characterCards/" + directivesParser.getCharacterCardNames().get(i) + ".png").toString());
            ImageView imv = new ImageView();
            ImageView frameImv = new ImageView();
            imv.setImage(image);
            imv.setId(directivesParser.getCharacterCardNames().get(i));
            imv.setFitWidth(109);
            imv.setFitHeight(160);
            imv.setOnMouseClicked(this::selectCharacterCard);
            frameImv.setImage(frame);
            frameImv.setId("frame_" + directivesParser.getCharacterCardNames().get(i));
            frameImv.setFitWidth(120);
            frameImv.setFitHeight(170);
            frameImv.setVisible(false);
            characterCardsGridPane.add(imv, i, 0);
            characterCardsGridPane.add(frameImv, i, 0);
        }
    }

    private void displayPlayerDashboard() {
        Image image = new Image((GUIGameDisplayer.class.getResource("images/player-dashboard.png")).toString());
        ImageView imv = new ImageView();
        imv.setImage(image);
        imv.setFitWidth(366);
        imv.setFitHeight(202);
        imv.setX(-90);
        imv.setY(200);
        imv.setRotate(-90);
        this.anchorPane.getChildren().add(imv);
    }

    private void displayArchipelago() {
        int archipelagoSize = this.directivesParser.getIslandsSize();
        if (this.firstDisplay) {
            this.previousArchipelagoSize = archipelagoSize;
            for (int i = 0; i < archipelagoSize; i++) {
                ImageView imv = new ImageView();
                imv.setFitWidth(80);
                imv.setFitHeight(80);
                imv.setOnMouseClicked(this::selectIsland);
                imv.setId(String.format("island%d", i));
                this.anchorPane.getChildren().add(imv);
            }
        } else {
            for (int i = archipelagoSize; i < previousArchipelagoSize; i++)
                this.anchorPane.getChildren().remove(this.scene.lookup(String.format("#island%d", i)));
        }
        double deltaRad = 2 * Math.PI / archipelagoSize;
        double currentRad = 0;
        ImageView currentIsland;
        for (int i = 0; i < archipelagoSize; i++) {
            currentIsland = (ImageView) this.scene.lookup(String.format("#island%d", i));
            currentIsland.setX(595 + 260 * Math.cos(currentRad));
            currentIsland.setY(280 + 130 * Math.sin(currentRad));
            currentRad += deltaRad;
            if (i == this.directivesParser.getMotherNaturePosition())
                currentIsland.setImage(new Image(GUIGameDisplayer.class.getResource("images/islands/island-motherNature.png").toString()));
            else
                currentIsland.setImage(new Image(GUIGameDisplayer.class.getResource("images/islands/island" + i % 3 + ".png").toString()));
        }
    }

    private void displayDashboardEntranceTokensLabel0() {
        for (String tokenColor : View.AVAILABLE_TOKEN_COLORS.values()) {
            Label label;
            try {
                label = (Label) this.scene.lookup("#" + tokenColor + "_D0");
                if (this.directivesParser.getDashboardEntranceTokens(this.username).get(tokenColor) != null)
                    label.setText(this.directivesParser.getDashboardEntranceTokens(this.username).get(tokenColor).toString());
                else {
                    label.setText("0");
                }
            } catch (DirectivesParser.DirectivesParserException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayDashboardHallTokensLabel0() {
        for (String tokenColor : View.AVAILABLE_TOKEN_COLORS.values()) {
            Label label;
            try {
                label = (Label) this.scene.lookup("#" + tokenColor + "_DH0");
                if (this.directivesParser.getDashboardHallTokens(this.username).get(tokenColor) != null)
                    label.setText(this.directivesParser.getDashboardEntranceTokens(this.username).get(tokenColor).toString());
                else {
                    label.setText("0");
                }
            } catch (DirectivesParser.DirectivesParserException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectIsland(MouseEvent mouseEvent) {
        int selectedIsland = Integer.parseInt(((ImageView) mouseEvent.getSource()).getId().split("island")[1]);
        switch (this.directivesParser.getCurrentPhase().getValue()) {
            case "Move mother nature" -> {
                int steps = (selectedIsland - this.directivesParser.getMotherNaturePosition()
                        + this.directivesParser.getIslandsSize()) % this.directivesParser.getIslandsSize();
                if (steps < 1 || steps > this.directivesParser.getDashboardLastDiscardedAssistantCard(this.username).getValue().getValue()
                        + this.directivesParser.getAdditionalMotherNatureSteps()) {
                    this.errorAlert.setHeaderText("Move mother nature");
                    this.errorAlert.setContentText("Invalid number of steps");
                    this.errorAlert.showAndWait();
                } else {
                    this.directivesDispatcher.actionMoveMotherNature(this.username, steps);
                }
            }
        }
    }

    private void displayCloudsTokensLabels() {
        Label label;
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            for (String tokenColor : View.AVAILABLE_TOKEN_COLORS.values()) {
                try {
                    label = (Label) this.scene.lookup("#" + tokenColor + "_C" + i);
                    label.setText(this.directivesParser.getCloudTokens(i).get(tokenColor) != null ?
                            this.directivesParser.getCloudTokens(i).get(tokenColor).toString() : "0");
                } catch (DirectivesParser.DirectivesParserException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void setOthersUsername(){
        List<String> othersUsernameList = directivesParser.getDashboardNames();
        othersUsernameList.remove(this.username);
        this.opponentUsername = othersUsernameList.get(0);
        if (this.directivesParser.getExactPlayersNumber() == 3){
            this.leftUsername = othersUsernameList.get(1);
        }
    }

    protected void displayGame(String username) throws DirectivesParser.DirectivesParserException {
        if (this.firstDisplay) {
            if (directivesParser.getExactPlayersNumber() == 3) {
                this.displayPlayerDashboard();
                this.displayCloud();
            }
            if (!directivesParser.isExpertMode())
                activateCharacterCardButton.setVisible(false);
            this.username = username;
            this.setOthersUsername();
        }
        this.displayCurrentPhase();
        this.displayPlayersOrder(username);
        this.displayArchipelago();
        this.displayDashboardEntranceTokensLabel0();
        this.displayDashboardHallTokensLabel0();
        this.displayCloudsTokensLabels();
        if (this.firstDisplay)
            this.firstDisplay = false;
    }

    private void disableCloudImageView() {
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            ImageView imageView;
            imageView = (ImageView) this.scene.lookup("#cloud" + i);
            imageView.setDisable(true);
        }
    }

    private void displayCloud() {
        Image image = new Image((GUIGameDisplayer.class.getResource("images/cloud.png")).toString());
        ImageView imv = new ImageView();
        imv.setImage(image);
        imv.setFitWidth(125);
        imv.setFitHeight(125);
        imv.setX(577);
        imv.setY(251);
        this.anchorPane.getChildren().add(imv);
    }

    private void setInvisibleAssistantCardsFrames() {
        List<String> cardNameList = new ArrayList<>(directivesParser.getDashboardAssistantCards(this.username).keySet());
        for (String s : cardNameList) {
            ImageView imv = null;
            imv = (ImageView) this.assistantCardsGridPane.lookup("#frame_" + s);
            imv.setVisible(false);
        }
    }

    private void displayAvailableAssistantCards() {
        Image image = null;
        Image frame = new Image(GUIController.class.getResource("images/selection-frame.png").toString());
        List<String> cardNameList = new ArrayList<>(directivesParser.getDashboardAssistantCards(this.username).keySet());
        for (int i = 0; i < cardNameList.size(); i++) {
            image = new Image(GUIGameDisplayer.class.getResource("images/assistantCards/" + cardNameList.get(i) + ".png").toString());
            ImageView imv = new ImageView();
            ImageView frameImv = new ImageView();
            imv.setImage(image);
            imv.setId(cardNameList.get(i));
            imv.setFitWidth(109);
            imv.setFitHeight(160);
            imv.setOnMouseClicked(this::selectAssistantCard);
            frameImv.setImage(frame);
            frameImv.setId("frame_" + cardNameList.get(i));
            frameImv.setFitWidth(120);
            frameImv.setFitHeight(170);
            frameImv.setVisible(false);
            assistantCardsGridPane.add(imv, i % 5, i / 5);
            assistantCardsGridPane.add(frameImv, i % 5, i / 5);
        }
    }

    @FXML
    private void selectAssistantCard(MouseEvent event) {
        setInvisibleAssistantCardsFrames();
        ImageView imv = new ImageView();
        ImageView frame = new ImageView();
        this.assistantCardChosen = ((ImageView) event.getSource()).getId();
        frame = (ImageView) this.assistantCardsGridPane.lookup("#frame_" + this.assistantCardChosen);
        frame.setVisible(true);
    }

    @FXML
    private void selectCharacterCard(MouseEvent event) {
        setInvisibleCharacterCardsFrames();
        ImageView imv = new ImageView();
        ImageView frame = new ImageView();
        this.characterCardChosen = ((ImageView) event.getSource()).getId();
        frame = (ImageView) this.characterCardsGridPane.lookup("#frame_" + this.characterCardChosen);
        frame.setVisible(true);
    }

    @FXML
    private void fillClouds() {
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void playAssistantCard() {
        //va cambiato il bg
        openPopUp("assistantCardsChoice.fxml", 645, 450, "images/assistantCardsChoiceBG.png");
        this.displayAvailableAssistantCards();
    }

    @FXML
    private void playCharacterCard() {
        openPopUp("characterCardsChoice.fxml", 400, 300, "images/characterCardsChoiceBG.png");
        this.displayCharacterCards();
    }

    @FXML
    private void confirmAssistantCard() {
        this.popUpStage.close();
        this.directivesDispatcher.actionPlayAssistantCard(this.username, this.assistantCardChosen);
    }


    @FXML
    private void moveStudents() {

    }

    @FXML
    private void moveMotherNature() {
        this.messageAlert.setHeaderText("Move mother nature");
        this.messageAlert.setContentText(String.format("Choose an island by clicking on it (max distance is %d)",
                this.directivesParser.getDashboardLastDiscardedAssistantCard(this.username).getValue().getValue()
                        + this.directivesParser.getAdditionalMotherNatureSteps()));
        this.messageAlert.showAndWait();
    }

    @FXML
    private void chooseCloud() {
        for (int i = 0; i < this.directivesParser.getExactPlayersNumber(); i++) {
            ImageView imageView;
            imageView = (ImageView) this.scene.lookup("#cloud" + i);
            imageView.setDisable(false);
        }
    }

    private void sendChoseCloud(MouseEvent event) {
        int cloudChosen = Integer.parseInt(((ImageView) event.getSource()).getId().split("cloud")[1]);
        this.directivesDispatcher.actionChooseCloud(this.username, cloudChosen);
    }

    @FXML
    private void activateCharacterCard() {
        this.popUpStage.close();
        //continuare ad implementare
    }


    protected Button getFillCloudsButton() {
        return fillCloudsButton;
    }

    protected Button getPlayAssistantCardButton() {
        return playAssistantCardButton;
    }

    protected Button getMoveStudentsButton() {
        return moveStudentsButton;
    }

    protected Button getMoveMotherNatureButton() {
        return moveMotherNatureButton;
    }

    protected Button getChooseCloudButton() {
        return chooseCloudButton;
    }

    protected Button getActivateCharacterCardButton() {
        return activateCharacterCardButton;
    }
}
