package org.example.ballsort.controller;
import com.almasb.fxgl.core.collection.Array;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    GameState gameState;
    String level;
    GameSession gameSession;
    User userLoggedIn;
    Boolean clickedStartFirstTime = false;
    Stage stage;

    public User getUserLoggedIn() {
        return userLoggedIn;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUserLoggedIn(User userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    @FXML
    private Label statusText;
    @FXML
    private Button startButton;
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
    @FXML
    public void initialize() {
        level = "Level 1";
        levelsChoiceBox.setOnAction(event -> {
            String levelSelected = levelsChoiceBox.getSelectionModel().getSelectedItem();
            level = levelSelected;
//            setGameUi(level);
        });
        hoverGrowTransition(startButton);
        hoverGrowTransition(undoButton);
        hoverGrowTransition(redoButton);
//        gameSound();
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

    public void onButtonClick() {
        closedWindowInstructions(); // set event handler when the user close the window we should insert the gameSession into DB
        if(clickedStartFirstTime){
            DAO.insertSession(gameSession);
            clickedStartFirstTime = false;
        }
        LocalDateTime now = LocalDateTime.now();
        int levelNum = (level.charAt(level.length() - 1)) - '0';
        System.out.println("level: " + levelNum);
        String[] color = {"orange", "blue", "red", "green"};

        //Create new GameSession and initilliaze it with 0 in score, endTime and false in won, so we can modify it later when the session ends.
        gameSession  = new GameSession(userLoggedIn.getId(), now,now.getSecond(), now.getSecond(), false, levelNum, 0);
//        DAO.insertSession(gameSession);
        gameState = new GameState(4, levelNum, color);
        clickedStartFirstTime = true;
        setGameUi(level);
    }

    public void closedWindowInstructions(){
        // set event handler when the user close the window we should insert the gameSession into DB
        stage.setOnCloseRequest(event -> {
           if(clickedStartFirstTime) DAO.insertSession(gameSession);
        });
    }

    public void setGameUi(String level) {
//        int levelNum = (level.charAt(level.length() - 1)) - '0';
//        String[] color = {"orange", "blue", "red", "green"};
//        gameState = new GameState(4, levelNum, color);

        if(gameGrid == null) {
            System.out.println("GameGrid is null");
        }
        if(gameState == null) {
            System.out.println("GameState is null");
        }
        gameGrid.getChildren().clear();

        if(gameState != null ) statusText.setText("Moves: " + String.valueOf(gameState.getMoveCounter()));
        int i = 0;
        for(Tube tube: gameState.getTubes()){
            final int index = i;
            VBox vbox = new VBox(30);
            vbox.setId("vbox");
            vbox.setAlignment(Pos.BOTTOM_CENTER);
            vbox.setSpacing(10);
            vbox.setPrefHeight(250);
            vbox.setPrefWidth(50);

            for(int j = tube.getBalls().size() - 1; j >= 0; j--){
                Ball ball = tube.getBalls().get(j);
                Circle circle = new Circle(15);
                circle.setId("circle");
                circle.setFill(Color.valueOf(ball.getColor()));
                Image circleImage = new Image(getClass().getResource("/images/balls/ball_" + ball.getColor() + ".png").toExternalForm());
                circle.setFill(new ImagePattern(circleImage));
                circle.setStyle("-fx-background-radius: 50; -fx-border-radius: 50;");

                if(j == tube.getBalls().size() - 1){
                    setUpDragStart(circle, index);
                    setupDragEnd(circle);
                }
                vbox.getChildren().add(circle);
            }

            setupDragOver(vbox, index);
            setupDragDropped(vbox, index);
            gameGrid.add(vbox, i++, 0);
        }
    }

    public Circle lastElementOfVbox(VBox vbox) {
        if(vbox == null) return null;
        ObservableList<Node> children = vbox.getChildren();
        if(children.isEmpty()) return null;
        return (Circle) children.getLast();
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
        System.out.println("drag end enter fuunc");
        vbox.setOnDragDropped(event -> {
            if(lastElementOfVbox(vbox) != null) lastElementOfVbox(vbox).setVisible(true);
            else System.out.println("lastElemet is null");
            System.out.println("Drag dropped");
            Dragboard db = event.getDragboard();
            if (db.hasString()) {
                int srcIndex = Integer.parseInt(db.getString());
                int destIndex = index;
                boolean moved = gameState.moveBall(srcIndex, destIndex);
                if(moved) {
                    LocalDateTime now = LocalDateTime.now();
                    int nowSeconds = now.getSecond();
                    gameSession.setDuration(abs(nowSeconds - gameSession.getStartTime()));
                    gameSession.setEndTime(nowSeconds);
                    // score = moves / 2  +  duration / 2
                    gameSession.setScore((gameState.getMoveCounter() / 2 ) + abs(gameSession.getDuration() / 2));
//                    DAO.modifySession(gameSession);
                    Move move = new Move(srcIndex, destIndex); // create the moves maked by the user
                    gameState.getHistory().add(move);   // I added the move in the history's moves of the GameState
//                    DAO.modifySession(gameSession);
                    System.out.println("Moved Ball");

                    setGameUi(level); // setup UI du jeu

                    if(gameState.isSolved()){  // instructions to do when the game is solved (won)
                        gameSession.setWon(true);// set the session as won.
//                        DAO.modifySession(gameSession);// insert the new state of the GameSession
                        gameState.winAnimation(gameGrid,stackPane, borderPane);

                        winSound();
                    }
                }
                else System.out.println("not Moved ball");
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

    public void winSound(){
        Media sound = new Media(getClass().getResource("/sounds/winningSound.wav").toExternalForm());
        MediaPlayer mp = new MediaPlayer(sound);
        mp.setCycleCount(MediaPlayer.INDEFINITE);
        mp.play();
    }
//    public void gameSound(){
//        Media sound = new Media(getClass().getResource("/sounds/gameSound.wav").toString());
//        MediaPlayer mp = new MediaPlayer(sound);
//        mp.play();
//    }
}