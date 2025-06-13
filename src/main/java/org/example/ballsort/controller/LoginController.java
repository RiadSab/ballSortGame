package org.example.ballsort.controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.ballsort.MainApp;
import org.example.ballsort.Database.DAO;
import org.example.ballsort.model.User;
import java.io.IOException;


public class LoginController {

    public Stage stage;
    public User userLoggedIn;
    @FXML
    private TextField userName;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button loginButton;

    @FXML
    private Button toSignUp;

    @FXML
    private GridPane gridPane;

    @FXML
    private void initialize() {
        hoverGrowTransition(loginButton);
        hoverGrowTransition(toSignUp);
    }

    public  void loginAction() throws IOException {

        // on récupère les informations entrées par l'utilsateur pour confirmer le login
        String user =  userName.getText();
        String pass = userPassword.getText();


        System.out.println(user);
        System.out.println(pass);


        // dans le cas où le mot de passe ou l'userName est vide
        // aussi dans le cos où le mot de passe est incorrecte
        if(user.isEmpty() || pass.isEmpty() || !DAO.checkLogin(user, pass)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username or Password");
            alert.setContentText("Invalid Username or Password");
            alert.showAndWait();
        }

        // le login est validé.
        else {

            // on se rend vers le jeu
            // on change la scene
            userLoggedIn = DAO.selectUser(user);
            FXMLLoader fxmlLoader1 = new FXMLLoader(MainApp.class.getResource("/fxml/game_view.fxml"));
            Parent root = fxmlLoader1.load();
            GameController gameController1 = fxmlLoader1.getController();
            gameController1.setUserLoggedIn(userLoggedIn);
            stage = MainApp.window;
            gameController1.setStage(stage);
            Scene scene = new Scene(root, 800, 500);
            scene.getStylesheets().add(MainApp.class.getResource("/css/style.css").toExternalForm());
            MainApp.window.setTitle("Ball Sort Game!");
            MainApp.window.setScene(scene);
            MainApp.window.show();
        }
    }

    public void toSignUp() throws IOException {
        MainApp.changeScene("/fxml/signup_view.fxml");
    }


    // methode pour ajouter des effets visuel sur la taille des bouttons lors du survole
    public void hoverGrowTransition(Button button) {
        ScaleTransition grow = new ScaleTransition(Duration.millis(200), button);
        grow.setToX(1.2);
        grow.setToY(1.2);

        ScaleTransition shrink = new ScaleTransition(Duration.millis(200), button);
        shrink.setToX(1);
        shrink.setToY(1);
        button.setOnMouseEntered(event -> {
            grow.play();
        });
        button.setOnMouseExited(event -> {
            shrink.play();
        });
    }



}
