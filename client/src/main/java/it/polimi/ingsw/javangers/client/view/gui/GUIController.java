package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.MessageType;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import it.polimi.ingsw.javangers.client.view.View;
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
    private MenuButton create_join_MenuButton;
    @FXML
    private Button finishButton;
    @FXML
    private TextField username;
    @FXML
    private CheckBox expertMode;


    /**
     * Constructor for view, initializing directives dispatcher and parser, view and starting main thread.
     *
     * @param directivesDispatcher directives dispatcher instance
     * @param directivesParser     directives parser instance
     */
    protected GUIController(DirectivesDispatcher directivesDispatcher, DirectivesParser directivesParser) {
        super(directivesDispatcher, directivesParser);
        exactPlayersNumber = new ChoiceBox<>();
        towerColor = new ChoiceBox<>();
    }

    @Override
    public void main(String[] args) {

    }


    @FXML
    @Override
    protected void createGame() {
        //da continuare
        /*System.out.println(username.getCharacters().toString());
        System.out.println(exactPlayersNumber.getValue());
        System.out.println(expertMode.isSelected());
        System.out.println(wizardType);
        System.out.println(towerColor.getValue());*/
        directivesDispatcher.createGame(username.getCharacters().toString(),exactPlayersNumber.getValue(),expertMode.isSelected(),wizardType,towerColor.getValue());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("loading-page.fxml"));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            stage = (Stage) finishButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
       // MessageType createType = directivesParser.getMessageType();
       // if(createType==CREATE)
    }

    @FXML
    protected void switchToCreateMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("createGame-menu.fxml"));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            stage = (Stage) create_join_MenuButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void switchToJoinMenu() {
        try {
            //CANE;
            //DA CONTROLLARE CHE ESISTA UNA PARTITA GIA` CREATA

            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("joinGame-menu.fxml"));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            stage = (Stage) create_join_MenuButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            //va cambiato
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void selectWizard(MouseEvent event) {
        wizardType = ((ImageView)event.getSource()).getId();
    }

    @FXML
    @Override
    protected void joinGame() {
        //da continuare
        System.out.println(username.getCharacters().toString());
        System.out.println(wizardType);
        System.out.println(towerColor.getValue());
        directivesDispatcher.addPlayer(username.getCharacters().toString(),wizardType,towerColor.getValue());
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("loading-page.fxml"));
            fxmlLoader.setController(this);
            root = fxmlLoader.load();
            stage = (Stage) finishButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e){
            throw new RuntimeException(e);
        }
        // MessageType createType = directivesParser.getMessageType();
        // if(createType==CREATE)
    }

    @Override
    protected void waitForStart() {

    }

    @Override
    protected void startGame() {

    }

    @Override
    protected void startShow() {

    }

    @Override
    protected void updateGame() {

    }

    @Override
    protected void showAbort(String message) {

    }

    @Override
    protected void showError(String message) {

    }

    @Override
    protected void closeGame(List<String> winners) {

    }

    @Override
    protected void enableActions() {

    }

    @Override
    protected void waitTurn() {

    }

    @Override
    protected void returnToMainMenu() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        exactPlayersNumber.getItems().addAll(possibleNumberOfPlayer);
        towerColor.getItems().addAll(possibleTowerColor);
    }
}