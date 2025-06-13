package org.example.ballsort.controller;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.ballsort.Database.DAO;
import org.example.ballsort.MainApp;
import org.example.ballsort.model.*;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

public class GameController {
    private GameState gameState;
    private int level;
    private GameSession gameSession;
    private User userLoggedIn;
    private Boolean clickedStartFirstTime = false;
    private Stage stage;

    @FXML
    private Button scoresButton;
    @FXML
    private Label statusText;
    @FXML
    private Button startButton;
    @FXML
    private Button toLogin;
    @FXML
    private GridPane gameGrid;
    @FXML
    private ComboBox<String> levelsChoiceBox;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private StackPane stackPane;
    @FXML
    private BorderPane borderPane;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUserLoggedIn(User userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    @FXML
    public void initialize() {
        level = 1;
        levelsChoiceBox.setOnAction(event -> {
            String levelSelected = levelsChoiceBox.getSelectionModel().getSelectedItem();
            if(levelSelected.equals("Level 1")){
                level = 1;
            }
            else if(levelSelected.equals("Level 2")){
                level = 2;
            }
            else {
                level = 3;
            }
        });
        hoverGrowTransition(startButton);
        hoverGrowTransition(undoButton);
        hoverGrowTransition(redoButton);
        hoverGrowTransition(scoresButton);
        hoverGrowTransition(toLogin);
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

    public void onButtonClick() {
        closedWindowInstructions(); // set event handler when the user close the window we should insert the gameSession into DB
        if(clickedStartFirstTime && !gameState.isSolved()){
            DAO.insertSession(gameSession);
            clickedStartFirstTime = false;
        }
        LocalDateTime now = LocalDateTime.now();
        System.out.println("level: " + level);
        String[] color = {"orange", "blue", "red", "green"};


        // intialiser le jeu et la session actuelle
        gameSession  = new GameSession(userLoggedIn.getId(), now,now.getSecond(), now.getSecond(), false, level, 0);
        gameState = new GameState( level, color);

        // user a clické le button Start (s'il clicke une deusieme fois,
        // c'est-à-dire la session est termiée et on doit la inserer dans DB)
        clickedStartFirstTime = true;
        setGameUi(level);
    }

    public void closedWindowInstructions(){
        // set event handler when the user close the window we should insert the gameSession into DB
        stage.setOnCloseRequest(event -> {
            // Avant d'insere la session, il faut assurer qu'elle contient des informations,
            // c'est-à-dire que l'utilsateur doit jouer avant d'inserer la session.
            // Donc, il faut assurer qu'il a clické start pour une fois.
           if(clickedStartFirstTime) DAO.insertSession(gameSession);
        });
    }

    public void setGameUi(int level) {

        if(gameGrid == null) {
            System.out.println("GameGrid is null");
        }
        if(gameState == null) {
            System.out.println("GameState is null");
        }
        gameGrid.getChildren().clear();

        if(gameState != null ) statusText.setText("Moves: " + String.valueOf(gameState.getMoveCounter()));
        int i = 0;

        // On récupère les DATA  du gameState qu'on a deja créé
        // on crée les Circles en se basant sur le couleur de l'objet Ball on a deja dans le gameState

        for(Tube tube: gameState.getTubes()){
            final int index = i;
            VBox vbox = new VBox(30);
            vbox.setId("vbox");
            vbox.setAlignment(Pos.BOTTOM_CENTER);
            vbox.setSpacing(10);
            vbox.setPrefHeight(250);
            vbox.setPrefWidth(55);


            int ballCount = tube.getBalls().size();
            for(int j = ballCount - 1; j >= 0; j--){
                Ball ball = tube.getBalls().get(j);
                Circle circle = new Circle(15);
                circle.setId("circle");
                circle.setFill(Color.valueOf(ball.getColor()));
                Image circleImage = new Image(getClass().getResource("/images/balls/ball_" + ball.getColor() + ".png").toExternalForm());
                circle.setFill(new ImagePattern(circleImage));
                circle.setStyle("-fx-background-radius: 50; -fx-border-radius: 50;");

                if(j == tube.getBalls().size() - 1){
                    // On fait le setup du Drag start et le Drag End pour la derniere Ball du tube
                    setUpDragStart(circle, index);
                    setupDragEnd(circle);
                }
                vbox.getChildren().add(circle);
            }

            //On fait le setup drag Over et drag dropped pour toutes les balles.
            setupDragOver(vbox, index);
            setupDragDropped(vbox, index);

            // on ajoute le tube crée dans le bord du jeu (GameGrid)
            gameGrid.add(vbox, i++, 0);
        }
    }

    // permet de retouner le dernier Circle  du VBox
    public Circle lastElementOfVbox(VBox vbox) {
        if(vbox == null) return null;
        ObservableList<Node> children = vbox.getChildren();
        if(children.isEmpty()) return null;
        return (Circle) children.get(children.size() - 1);
    }

    public void setUpDragStart(Circle circle, int index) {
        if(circle == null) return;
        circle.setOnDragDetected(event -> {
            System.out.println("Drag started");

            Dragboard db = circle.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(index));
            db.setContent(content);

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            params.setTransform(Transform.scale(2, 2));
            WritableImage snapshot = circle.snapshot(params, null);
            db.setDragView(snapshot);

            circle.setVisible(false);
            event.consume();
        });
    }

    public void setupDragOver(VBox vbox, int index) {
        if(vbox == null) return;
        vbox.setOnDragOver(event -> {
            System.out.println("drag over");
            if(event.getDragboard().hasString() && event.getGestureSource() != vbox) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    public void setupDragDropped(VBox vbox, int index) {
        if(vbox == null) {
            System.out.println("vbox is null");
            return;
        }
        vbox.setOnDragDropped(event -> {
            if(lastElementOfVbox(vbox) != null) lastElementOfVbox(vbox).setVisible(true);
            else System.out.println("lastElemet is null");
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                // recuperer l'index du tube de depart d'après le Dragboard
                int srcIndex = Integer.parseInt(db.getString());
                int destIndex = index;

                // verifier si la balle peut etre deplacée (si elle respecte les règles)
                boolean moved = gameState.moveBall(srcIndex, destIndex);
                if(moved) {
                    LocalDateTime now = LocalDateTime.now();
                    int nowSeconds = now.getSecond();
                    gameSession.setDuration(abs(nowSeconds - gameSession.getStartTime()));
                    gameSession.setEndTime(nowSeconds);
                    gameSession.setScore(500 - (gameState.getMoveCounter() / 2 ) + abs(gameSession.getDuration() / 2));
                    Move move = new Move(srcIndex, destIndex); // create the moves maked by the user
                    gameState.getHistory().add(move);   // I added the move in the history's moves of the GameState

                    System.out.println("Moved Ball");

                    // après le mouvement du balle :
                    // on refait le setup de l'interface graphique selon la nouvelle etat des balles
                    setGameUi(level); // setup UI du jeu

                    if(gameState.isSolved()){// instructions to do when the game is solved (won)
                        gameSession.setWon(true);// set the session as won.
                        gameState.winAnimation(gameGrid,stackPane, borderPane);

                        // un son de victoire
                        winSound();
                    }
                }
                else System.out.println("ball didn't move");
                event.setDropCompleted(true);
            }
            else event.setDropCompleted(false);

            event.consume();
        });
    }

    public void setupDragEnd(Circle circle){
        circle.setOnDragDone(event -> {
            System.out.println("drag end");
            circle.setVisible(true);

        });
    }

    public void undoClicked(){
        if(gameState != null && gameState.getHistory().size() > 0){
            Move lastMove = gameState.getHistory().peekLast();
            gameState.getRedoMoves().addLast(lastMove);
            int srcIndex = gameState.getHistory().peekLast().getToIndex();
            int toIndex = gameState.getHistory().peekLast().getFromIndex();
            gameState.getTubes().get(toIndex).pushBall(gameState.getTubes().get(srcIndex).getBalls().pop());
            gameState.getHistory().removeLast();
            setGameUi(level);
        }
    }

    public void redoClicked(){
        if(gameState != null && gameState.getRedoMoves().size() > 0){
            Move lastMove = gameState.getRedoMoves().removeLast();
            gameState.moveBall(lastMove.getFromIndex(), lastMove.getToIndex());
            gameState.getHistory().addLast(lastMove);
            setGameUi(level);
        }
    }

    // son de victoire
    public void winSound(){
        Media sound = new Media(getClass().getResource("/sounds/winningSound.wav").toExternalForm());
        MediaPlayer mp = new MediaPlayer(sound);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();

    }

    // methode pour nous rendre dans une autre fenetre de scores
    public void showBestScores() throws IOException {
                FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/scores_view.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root, 800, 500);
                stage.setTitle("Best Scores");
                stage.setScene(scene);
                stage.show();
    }

    // methode pour retourner a la page du log in
    public void toLogin() throws IOException {
        MainApp.changeScene("/fxml/login_view.fxml");
    }


}

