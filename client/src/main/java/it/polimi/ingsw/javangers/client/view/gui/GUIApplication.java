package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class GUIApplication extends Application {

    private static DirectivesDispatcher directivesDispatcher;
    private static DirectivesParser directivesParser;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("create-join.fxml"));
        GUIController controller = new GUIController(directivesDispatcher, directivesParser, stage);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 620, 640);
        try {
            stage.getIcons().add(new Image(GUIApplication.class.getResource("images/logo-cranio.png").toURI().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        stage.setWidth(400);
        stage.setHeight(300);
        stage.setResizable(false);
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
}