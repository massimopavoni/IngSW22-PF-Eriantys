package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class GUIGameDisplayer {

    private final DirectivesParser directivesParser;
    private final DirectivesDispatcher directivesDispatcher;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stage popUpStage;
    private AnchorPane anchorPane;
    private String assistantCardChosen;
    private String characterCardChosen;
    @FXML
    private Label currentPhase;
    @FXML
    private Label playersOrder;
    @FXML
    private Label fxmlUsername1;
    @FXML
    private Label fxmlUsername2;
    @FXML
    private Label fxmlUsername3;
    private String username;
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



    private Background displayBackGround(String resource) throws URISyntaxException {
        Image img = new Image(GUIGameDisplayer.class.getResource(resource).toURI().toString());
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        return new Background(bImg);
    }

    protected GUIGameDisplayer(DirectivesParser directivesParser,DirectivesDispatcher directivesDispatcher, Stage stage) {
        this.directivesParser = directivesParser;
        this.directivesDispatcher = directivesDispatcher;
        this.stage = stage;
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
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public void openPopUp(String fxmlFile,int width, int height, String backGroundResource) {
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
            try {
                stage.getIcons().add(new Image(GUIGameDisplayer.class.getResource("images/logo-cranio.png").toURI().toString()));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            this.popUpStage.setWidth(width);
            this.popUpStage.setHeight(height);
            this.popUpStage.setResizable(false);
            this.popUpStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }





    private void displayCurrentPhase() {
        Pair<String, String> currentPhasePair = this.directivesParser.getCurrentPhase();
        this.currentPhase.setText("Current phase: " + currentPhasePair.getKey() + " => " + currentPhasePair.getValue());
    }

    private void displayPlayersOrder(String username) throws DirectivesParser.DirectivesParserException {

        playersOrder.setText("You are " + username + ", Player's order is: " + directivesParser.getPlayersOrder().toString());

        /* da sistemare non visualizzate in modo corretto
        fxmlUsername1.setText(username);
        fxmlUsername2.setText(directivesParser.getDashboardNames().get(1));
        if(directivesParser.getExactPlayersNumber() == 3)
            fxmlUsername3.setText(directivesParser.getDashboardNames().get(2));

         */
    }

    private void displayCharacterCards() {
        Image image = null;
        for (int i = 0; i < directivesParser.getCharacterCardNames().size(); i++) {
            try {
                image = new Image(GUIGameDisplayer.class.getResource("images/characterCards/" +directivesParser.getCharacterCardNames().get(i)+".png").toURI().toString());
                ImageView imv = new ImageView();
                imv.setImage(image);
                imv.setId(directivesParser.getCharacterCardNames().get(i));
                imv.setFitWidth(109);
                imv.setFitHeight(160);
                imv.setOnMouseClicked(this::selectCharacterCard);
                characterCardsGridPane.add(imv,i,0);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void displayPlayerDashboard() {
        Image image = null;
        try {
            image = new Image((GUIGameDisplayer.class.getResource("images/plancia aggiuntiva.png")).toURI().toString());
            ImageView imv = new ImageView();
            imv.setImage(image);
            imv.setFitWidth(366);
            imv.setFitHeight(202);
            imv.setX(-90);
            imv.setY(200);
            imv.setRotate(-90);
            this.anchorPane.getChildren().add(imv);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayArchipelago(){
        int archipelagoSize = this.directivesParser.getIslandsSize();
        double deltaDeg = 2*Math.PI/archipelagoSize;
        double currentRad = 0;
        Image image = null;
        for(int i=0; i<archipelagoSize; i++){
            try {
                image = new Image(GUIGameDisplayer.class.getResource("images/islands/island"+Integer.toString(i%3)+".png").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            ImageView imv = new ImageView();
            imv.setImage(image);
            imv.setFitWidth(80);
            imv.setFitHeight(80);
            imv.setX(600+260*Math.cos(currentRad));
            imv.setY(270+130*Math.sin(currentRad));
            currentRad += deltaDeg;
            this.anchorPane.getChildren().add(imv);
        }
    }

    protected void displayGame(String username) throws DirectivesParser.DirectivesParserException {
        this.username = username;
        this.displayCurrentPhase();
        this.displayPlayersOrder(username);
        if (!directivesParser.isExpertMode())
            activateCharacterCardButton.setVisible(false);
        if (directivesParser.getExactPlayersNumber() == 3)
            this.displayPlayerDashboard();
        this.displayArchipelago();
    }



    private void displayAvailableAssistantCards() throws URISyntaxException {
        Image image = null;
        List<String> cardNameList = new ArrayList<> (directivesParser.getDashboardAssistantCards(this.username).keySet());
        for (int i = 0; i < cardNameList.size(); i++) {
            try {
                image = new Image(GUIGameDisplayer.class.getResource("images/assistantCards/"+cardNameList.get(i)+".png").toURI().toString());
                ImageView imv = new ImageView();
                imv.setImage(image);
                imv.setId(cardNameList.get(i));
                imv.setFitWidth(109);
                imv.setFitHeight(160);
                imv.setOnMouseClicked(this::selectAssistantCard);
                assistantCardsGridPane.add(imv,i % 5,i/5);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void selectAssistantCard(MouseEvent event){
        assistantCardChosen = ((ImageView) event.getSource()).getId();
    }

    @FXML
    private void selectCharacterCard(MouseEvent event){
        characterCardChosen = ((ImageView) event.getSource()).getId();
    }

    @FXML
    private void fillClouds(){
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void playAssistantCard(){
        //va cambiato il bg
        openPopUp("assistantCardsChoice.fxml", 645, 450, "images/assistantCardsChoiceBG.png");
        try {
            this.displayAvailableAssistantCards();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void playCharacterCard(){
        openPopUp("characterCardsChoice.fxml", 400, 300,"images/characterCardsChoiceBG.png" );
        this.displayCharacterCards();
    }

    @FXML
    private void confirmAssistantCard(){
        this.popUpStage.close();
        this.directivesDispatcher.actionPlayAssistantCard(this.username, this.assistantCardChosen);
    }



    @FXML
    private void moveStudents(){

    }

    @FXML
    private void moveMotherNature(){

    }

    @FXML
    private void chooseCloud(){

    }

    @FXML
    private void activateCharacterCard(){
        this.popUpStage.close();
        //continuare ad implementare
    }



   protected Button getFillCloudsButton(){
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
