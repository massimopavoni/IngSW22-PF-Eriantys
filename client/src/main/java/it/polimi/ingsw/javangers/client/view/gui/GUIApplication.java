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

/**
 * Class representing the JavaFX application.
 */
public class GUIApplication extends Application {
    /**
     * Client directives dispatcher instance.
     */
    private static DirectivesDispatcher directivesDispatcher;
    /**
     * Client directives parser instance.
     */
    private static DirectivesParser directivesParser;

    /**
     * Main method for JavaFX application.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialize directives dispatcher and parser instances.
     *
     * @param newDirectivesDispatcher directives dispatcher instance
     * @param newDirectivesParser     directives parser instance
     */
    public static void setDirectives(DirectivesDispatcher newDirectivesDispatcher, DirectivesParser newDirectivesParser) {
        GUIApplication.directivesDispatcher = newDirectivesDispatcher;
        GUIApplication.directivesParser = newDirectivesParser;
    }

    /**
     * Start method for JavaFX application.
     *
     * @param stage application stage
     * @throws IOException if FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource("createJoin.fxml"));
        GUIController controller = new GUIController(directivesDispatcher, directivesParser, stage);
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 620, 640);
        stage.getIcons().add(new Image(String.valueOf(GUIApplication.class.getResource("images/cranioLogo.png"))));
        stage.setWidth(400);
        stage.setHeight(300);
        stage.setResizable(false);
        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Stop method for JavaFX application.
     */
    @Override
    public void stop() {
        Thread.currentThread().interrupt();
        System.exit(1);
    }
}