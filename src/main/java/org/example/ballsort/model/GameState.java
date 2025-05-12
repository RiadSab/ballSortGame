package org.example.ballsort.model;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.AudioTrack;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.ballsort.controller.GameController;


import java.util.*;

import static javafx.geometry.Pos.CENTER;

public class GameState {
    private List<Tube> tubes;
    private Deque<Move> history;
    private Deque<Move> redoMoves;
    private final int maxBalls = 6;
    private final int maxTubes = 4;
    private int moveCounter = 0;


   public GameState(int maxBalls, int mTubes, String [] colors) {


       redoMoves = new ArrayDeque<>();
       tubes = new ArrayList<>();
       history = new ArrayDeque<>();
       for(int i = 0; i < 5; i++) {
           int randomIndex = (int) (Math.random() );
           Tube tube = new Tube();
           for(int j = 0; j < colors.length; j++) {
               Ball ball = new Ball(colors[(randomIndex + i + j) % colors.length]);
               tube.pushBall(ball);
           }
           tubes.add(tube);
       }
       int emptyTubes = 0;
       if(mTubes == 1) emptyTubes = 2;
       else if(mTubes == 2) emptyTubes = 1;

       for(int i = 0; i < emptyTubes; i++) {
           Tube tube = new Tube();
           tubes.add(tube);
       }
   }

    public boolean moveBall(int from, int to) {
        Tube fromTube = this.getTubes().get(from);
        Tube toTube = this.getTubes().get(to);
        if(isSolved()){

            return false;
        }
        else if(fromTube != null && toTube != null && fromTube != toTube) {
            if(toTube.isEmpty()) {
                moveCounter++;
                toTube.pushBall(fromTube.popBall());
                System.out.println("Moved " + fromTube + " to " + toTube);
                return true;
            }
            else if(!toTube.isEmpty() && fromTube.getBalls().peek().getColor() == toTube.getBalls().peek().getColor()){
                toTube.pushBall(fromTube.popBall());
                System.out.println("Moved " + fromTube + " to " + toTube);
                moveCounter++;
                return true;
            }
        }
        return false;

   }



    public boolean isSolved(){
        for(Tube tube : tubes){
            if(tube.isEmpty()) continue;
            if((tube.getBalls().size() < 5) ) return false;
            if(!tube.isUniColor()) return false;
        }
        return true;
    }

    public void recodMove(Move move){
        history.push(move);
    }

    public void reset(){
        history.clear();
        tubes.clear();
    }

    public int getMaxBalls(){
        return maxBalls;
    }
    public int getMaxTubes(){
        return maxTubes;
    }


    public List<Tube> getTubes() {
        return tubes;
    }

    public void setTubes(List<Tube> tubes) {
        this.tubes = tubes;
    }

    public Deque<Move> getHistory() {
        return history;
    }

    public void setHistory(Deque<Move> history) {
        this.history = history;
    }

    public int getMoveCounter() {
       return moveCounter;
    }


    public Deque<Move> getRedoMoves() {
        return redoMoves;
    }

    public void setRedoMoves(Deque<Move> redoMoves) {
        this.redoMoves = redoMoves;
    }



    public void winAnimation(GridPane gridPane, StackPane stackPane, BorderPane borderPane){

       for(Node vb : gridPane.getChildren()){
           if(vb instanceof VBox){
               for(Node circle : ((VBox) vb).getChildren()){
                   if(circle instanceof Circle){
                       ScaleTransition grow = new ScaleTransition(new Duration(300), circle);
                       grow.setToY(1.4);
                       grow.setToX(1.4);
                       grow.setAutoReverse(true);
                       grow.setCycleCount(Animation.INDEFINITE);
                       grow.play();

                       DropShadow dropShadow = new DropShadow();
                       dropShadow.setColor(Color.GOLD);
                       dropShadow.setWidth(20);
                       dropShadow.setHeight(20);
                       dropShadow.setRadius(20);
                       dropShadow.setSpread(0.5);
                       circle.setEffect(dropShadow);
                   }
               }
           }
       }
       winTextAnimation(gridPane, stackPane, borderPane);
    }

    public void winTextAnimation(GridPane pane, StackPane stackPane, BorderPane borderPane){
       GaussianBlur blur = new GaussianBlur(15);
       borderPane.setEffect(blur);

       Label label = new Label("YOU WON!");
       label.setId("winLabel");
       DropShadow dropShadow = new DropShadow();
       dropShadow.setColor(Color.BLACK);
       dropShadow.setWidth(20);
       dropShadow.setHeight(20);
       dropShadow.setRadius(20);
       dropShadow.setSpread(0.5);
       label.setEffect(dropShadow);
       label.setAlignment(CENTER);
       stackPane.getChildren().add(label);
       stackPane.setAlignment(label, CENTER);
       borderPane.setDisable(true);

        stackPane.setOnMouseClicked(mouseEvent -> {
            borderPane.setDisable(false);
            borderPane.setEffect(null);
            label.setVisible(false);

        });
    }

//    public void winSound(){
//       Media sound = new Media(GameController.class.getResource("/sounds/winningSound.wav").toExternalForm());
//       MediaPlayer mp = new MediaPlayer(sound);
//       mp.play();
//    }
//    public void gameSound(){
//       Media sound = new Media(GameController.class.getResource("/sounds/gameSound1.wav").toExternalForm());
//       MediaPlayer mp = new MediaPlayer(sound);
//       mp.play();
//    }
}
