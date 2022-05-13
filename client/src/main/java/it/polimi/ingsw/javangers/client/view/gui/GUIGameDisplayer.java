package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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


    protected GUIGameDisplayer(DirectivesParser directivesParser, Stage stage) {
        this.directivesParser = directivesParser;
        this.stage = stage;
        //this.application = new GUIApplication();
    }

    protected void openNewStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("game-view.fxml"));
            fxmlLoader.setController(this);
            this.root = fxmlLoader.load();
            //this.stage = this.application.getStage();
            this.scene = new Scene(root);
            this.stage.setScene(scene);
            this.stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }

    //bisogna togliere il run later ma non funziona senza
    private void displayCurrentPhase() {
        Pair<String, String> currentPhasePair = this.directivesParser.getCurrentPhase();
        Platform.runLater(() -> this.currentPhase.setText("Current phase: " + currentPhasePair.getKey() + " => " + currentPhasePair.getValue()));
    }

    //bisogna togliere il run later ma non funziona senza
    private void displayPlayersOrder(String username) throws DirectivesParser.DirectivesParserException {
        Platform.runLater(() -> {
            //non va fatto in un try catch
            try {
                playersOrder.setText("You are " + username + ", Player's order is: " + directivesParser.getPlayersOrder().toString());
            } catch (DirectivesParser.DirectivesParserException e) {
                throw new RuntimeException(e);
            }
            /* da sistemare non visualizzate in modo corretto
            fxmlUsername1.setText(username);
            fxmlUsername2.setText(directivesParser.getDashboardNames().get(1));
            if(directivesParser.getExactPlayersNumber() == 3)
                fxmlUsername3.setText(directivesParser.getDashboardNames().get(2));

             */
        });
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
        if(directivesParser.getExactPlayersNumber() == 3)
            playerDashboard3.setImage(image);
    }


    protected void displayGame(String username) throws DirectivesParser.DirectivesParserException {
        this.displayCurrentPhase();
        this.displayPlayersOrder(username);
        this.displayCharacterCards();
        this.printPlayersDashboard();
    }


}
