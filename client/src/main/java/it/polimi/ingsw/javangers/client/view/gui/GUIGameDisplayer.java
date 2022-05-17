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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIGameDisplayer {

    private final DirectivesParser directivesParser;
    private final DirectivesDispatcher directivesDispatcher;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stage popUpStage;
    private String assistantCardChosen;
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
    @FXML
    private GridPane assistantCardsGridPane;


    protected GUIGameDisplayer(DirectivesParser directivesParser,DirectivesDispatcher directivesDispatcher, Stage stage) {
        this.directivesParser = directivesParser;
        this.directivesDispatcher = directivesDispatcher;
        this.stage = stage;
    }

    protected void openNewStage(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
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


    public void openPopUp(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
        fxmlLoader.setController(this);
        Parent root;
        popUpStage = new Stage();
        try {
            root = fxmlLoader.load();
            popUpStage.setScene(new Scene(root));
            popUpStage.initModality(Modality.APPLICATION_MODAL);
            popUpStage.show();
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

    private void displayPlayersDashboard() {
        Image image = null;
        try {
            image = new Image((GUIGameDisplayer.class.getResource("images/PLANCIA_GIOCO_2.png")).toURI().toString());
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
        this.displayPlayersDashboard();
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
    private void fillClouds(){
        directivesDispatcher.actionFillClouds(this.username);
    }

    @FXML
    private void playAssistantCard(){
        openPopUp("assistantCardsChoice.fxml");
        try {
            this.displayAvailableAssistantCards();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void confirmAssistantCard(){
        popUpStage.close();
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
