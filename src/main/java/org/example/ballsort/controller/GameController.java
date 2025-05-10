package org.example.ballsort.controller;
import com.almasb.fxgl.core.collection.Array;
import javafx.animation.ScaleTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import org.example.ballsort.model.Ball;
import org.example.ballsort.model.GameState;
import org.example.ballsort.model.Move;
import org.example.ballsort.model.Tube;

import javax.swing.text.html.Option;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    GameState gameState;
    String level;

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
    public void initialize() {
        level = "Level 1";

        levelsChoiceBox.setOnAction(event -> {
            String levelSelected = levelsChoiceBox.getSelectionModel().getSelectedItem();
            level = levelSelected;
            setGameUi(level);
        });
        hoverGrowTransition(startButton);
        hoverGrowTransition(undoButton);
        hoverGrowTransition(redoButton);
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
        int levelNum = (level.charAt(level.length() - 1)) - '0';
        System.out.println("level: " + levelNum);
        String[] color = {"orange", "blue", "red", "green"};
        gameState = new GameState(4, levelNum, color);
        setGameUi(level);
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
                    Move move = new Move(srcIndex, destIndex);
                    gameState.getHistory().add(move);
                    System.out.println("Moved Ball");
                    setGameUi(level);
                }
                else System.out.println("not Moved ball");
                event.setDropCompleted(true);
            }
            else event.setDropCompleted(false);
            if(event.isDropCompleted()){
                if(gameState.isSolved()){
                    Label win = new Label("You won!!");
                    win.setStyle("-fx-font-size: 18;");
                    win.setAlignment(Pos.CENTER);
//                    gameGrid.getChildren().getLast().add(win);
                }
            }
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
}