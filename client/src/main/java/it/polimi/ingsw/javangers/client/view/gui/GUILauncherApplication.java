package it.polimi.ingsw.javangers.client.view.gui;

import it.polimi.ingsw.javangers.client.controller.directives.DirectivesDispatcher;
import it.polimi.ingsw.javangers.client.controller.directives.DirectivesParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUILauncherApplication extends Application {

    private static DirectivesDispatcher directivesDispatcher;
    private static DirectivesParser directivesParser;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUILauncherApplication.class.getResource("create-join.fxml"));
        GUILauncherController controller = new GUILauncherController(directivesDispatcher, directivesParser);
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

    public static void setDirectives(DirectivesDispatcher newDirectivesDispatcher, DirectivesParser newDirectivesParser ){
        directivesDispatcher = newDirectivesDispatcher;
        directivesParser = newDirectivesParser;
    }

    /*
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(GUILauncherApplication.class.getResource("/it/polimi/ingsw/javangers/client/view/gui/gui-launcher-view.fxml")));
        Scene scene = new Scene(root, 620, 620, Color.BLACK);
        Image icon = new Image(Objects.requireNonNull(GUILauncherApplication.class.getResource("/it/polimi/ingsw/javangers/client/view/gui/images/logo-cranio.png")).toString());
        stage.getIcons().addAll(icon);
        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.show();
    }

     */




    /*
    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 620, 620, Color.BLACK);
        Image icon = new Image(Objects.requireNonNull(GUILauncherApplication.class.getResource("/it/polimi/ingsw/javangers/client/view/gui/logo-cranio.png")).toString());
        stage.getIcons().addAll(icon);
        stage.setTitle("Eriantys");

        Text text = new Text();
        text.setText("Eriantys");
        text.setX(50);
        text.setY(50);
        text.setFont(Font.font("Impact", 50));
        text.setFill(Color.PURPLE);


        root.getChildren().add(text);
        stage.setScene(scene);
        stage.show();
    }

     */

}