package org.example.ballsort.controller;

import com.sun.tools.javac.Main;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.controlsfx.control.spreadsheet.Grid;
import org.example.ballsort.Database.DAO;
import org.example.ballsort.MainApp;
import org.example.ballsort.model.User;
import javafx.scene.control.Alert;
import java.awt.*;
import java.io.IOException;

import static org.example.ballsort.Database.DAO.insertUser;
import static org.example.ballsort.Database.DAO.userExists;

public class SignUpController {
    @FXML
    private TextField userName;
    @FXML
    private PasswordField userPassword;
    @FXML
    private TextField userEmail;
    @FXML
    private Button signButton;
    @FXML
    private Button loginButton;
    @FXML
    private void initialize() {
        // ajoute l'effect de grandir lors du survole, pour les boutons
        hoverGrowTransition(loginButton);
        hoverGrowTransition(signButton);
    }


    // retoune a la page du login
    public void toLogin () throws IOException {
        MainApp.changeScene("/fxml/login_view.fxml");
    }

    // s'active lors du click dans le bouton sign in
    // verifie les informations entrées pour créer un compte
    public void signInAction() throws IOException {

        // verifie si le userName ou le mot de passe ou l'email sont vide.
        if(userName.getText().length() == 0 ||  userEmail.getText().length() == 0 || userPassword.getText().length() == 0 ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter all the fields");
            alert.showAndWait();
        }

        // verifie si l'userName est deja reservé
        else if(userExists(userName.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username already exists");
            alert.showAndWait();
        }
        else {

            // la creation du compte est reussie.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully");
            alert.showAndWait();
            User newUser = new User(userName.getText(), userPassword.getText(), userEmail.getText());
            insertUser(newUser);
            MainApp.changeScene("/fxml/login_view.fxml");
        }
    }



    // ajoute l'effect de grandir lors du survole, pour les boutons
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
