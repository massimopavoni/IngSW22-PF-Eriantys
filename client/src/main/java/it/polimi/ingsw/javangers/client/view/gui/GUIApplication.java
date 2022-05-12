package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {

    private static DirectivesDispatcher directivesDispatcher;
    private static DirectivesParser directivesParser;
    private Stage stage = new Stage();
    private static GUIController controller;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("create-join.fxml"));
        controller = new GUIController(directivesDispatcher, directivesParser, stage);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 620, 640);
        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void setDirectives(DirectivesDispatcher newDirectivesDispatcher, DirectivesParser newDirectivesParser) {
        directivesDispatcher = newDirectivesDispatcher;
        directivesParser = newDirectivesParser;
    }

    public void switchScene(String fxmlFile) {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(fxmlFile));
        fxmlLoader.setController(controller);
        Parent root;
        try {
            root = fxmlLoader.load();
            this.stage.setScene(new Scene(root));
            this.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage(){
        return this.stage;
    }

}