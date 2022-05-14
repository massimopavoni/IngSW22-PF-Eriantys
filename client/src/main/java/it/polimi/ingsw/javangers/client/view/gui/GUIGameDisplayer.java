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
    //private GUIApplication application;
    @FXML
    private ImageView characterCard1;
    @FXML
    private ImageView characterCard2;
    @FXML
    private ImageView characterCard3;
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
    @FXML
    private ImageView playerDashboard1;
    @FXML
    private ImageView playerDashboard2;
    @FXML
    private ImageView playerDashboard3;
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


    protected GUIGameDisplayer(DirectivesParser directivesParser,DirectivesDispatcher directivesDispatcher, Stage stage) {
        this.directivesParser = directivesParser;
        this.directivesDispatcher = directivesDispatcher;
        this.stage = stage;
    }

    protected void openNewStage(String resource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(resource));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            this.scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.show();
        } catch (IOException e) {
            //va cambiato
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
        if (directivesParser.isExpertMode()) {
            List<Image> imageList = new ArrayList<>();
            for (int i = 0; i < directivesParser.getCharacterCardNames().size(); i++) {
                Image image = null;
                String cardName = directivesParser.getCharacterCardNames().get(i).toLowerCase();
                try {
                    image = new Image((GUIGameDisplayer.class.getResource("images/characterCards/" + cardName + ".jpg")).toURI().toString());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                imageList.add(image);
            }
            characterCard1.setImage(imageList.get(0));
            characterCard2.setImage(imageList.get(1));
            characterCard3.setImage(imageList.get(2));
        }
    }

    private void printPlayersDashboard() {
        Image image = null;
        try {
            image = new Image((GUIGameDisplayer.class.getResource("images/PLANCIA GIOCO_2.png")).toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        playerDashboard1.setImage(image);
        playerDashboard2.setImage(image);
        if (directivesParser.getExactPlayersNumber() == 3)
            playerDashboard3.setImage(image);
    }


    protected void displayGame(String username) throws DirectivesParser.DirectivesParserException {
        this.username = username;
        this.displayCurrentPhase();
        this.displayPlayersOrder(username);
        this.displayCharacterCards();
        this.printPlayersDashboard();
    }

    @FXML
    private void fillClouds(){
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void playAssistantCard(){
        openNewStage("");
        String cardChosen="";
        directivesDispatcher.actionPlayAssistantCard(this.username, cardChosen);
    }

    @FXML
    private void moveStudents(){
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void moveMotherNature(){
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void chooseCloud(){
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void activateCharacterCard(){
        directivesDispatcher.actionFillClouds(this.username);
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
