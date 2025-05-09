package org.example.ballsort.controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.ballsort.MainApp;
import org.example.ballsort.Database.DAO;


import java.awt.*;
import java.io.IOException;

public class LoginController {
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

    public  void loginAction() throws IOException {
        String user =  userName.getText();
        String pass = userPassword.getText();
        System.out.println(user);
        System.out.println(pass);
        if(user == null || pass == null){
            System.out.println("Username or Password are empty");
        }

        if(user.isEmpty() || pass.isEmpty() || !DAO.checkLogin(user, pass)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username or Password");
            alert.setContentText("Invalid Username or Password");
            alert.showAndWait();
        }
        else {
            MainApp.changeScene("/fxml/game_view.fxml");
        }
    }

    public void toSignUp() throws IOException {
        MainApp.changeScene("/fxml/signup_view.fxml");
    }

    @FXML
    private void initialize() {
        hoverGrowTransition(loginButton);
        hoverGrowTransition(toSignUp);
    }


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
