package org.example.ballsort;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    public static Stage window;
    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/login_view.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }


    public static void changeScene(String fxmlPath) throws IOException {

        FXMLLoader fxmlLoader1 = new FXMLLoader(MainApp.class.getResource(fxmlPath));
        Parent root = fxmlLoader1.load();
        Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add(MainApp.class.getResource("/css/style.css").toExternalForm());
        window.setTitle("Ball Sort Game!");
        window.setScene(scene);
        window.show();

    }
    public static void main(String[] args) {
        launch();
    }
}