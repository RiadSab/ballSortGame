package org.example.ballsort.model;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


import java.util.*;

import static javafx.geometry.Pos.CENTER;

public class GameState {
    private List<Tube> tubes;
    private Deque<Move> history;
    private Deque<Move> redoMoves;
    private int moveCounter = 0;


   public GameState(int nbTubesBasedOnLevel, String [] colors) {
       //quand la classe est instanciée, nbTubesBasedOnLevel represente le niveau (level)
       // et apres on traite cet attribut pour creer les tubes vides correspendants
       redoMoves = new ArrayDeque<>();
       tubes = new ArrayList<>();
       history = new ArrayDeque<>();
       for(int i = 0; i < 5; i++) {
           Tube tube = new Tube();

           // le nombre de balles est le meme nombre de couleurs
           // l'ordre des balles se fait selon une variable random
           // pour que chaque sessoin eu un ordre special des couleurs
           int randomIndex = new Random().nextInt(colors.length);
           for(int j = 0; j < colors.length; j++) {
               Ball ball = new Ball(colors[(randomIndex + j ) % colors.length]);
               tube.pushBall(ball);
           }
           tubes.add(tube);
       }

       // on définit le niveau du jeu d'après le nombre de tubes vide
       // 2 tubes vides pour niveau 1,
       // 1 tube vide pour niveau 2,
       // 0 tube vide pour niveau 3.
       int emptyTubes = 0;
       if(nbTubesBasedOnLevel == 1) emptyTubes = 2;
       else if(nbTubesBasedOnLevel == 2) emptyTubes = 1;

       // ou crée les tubes vides selon le niveau
       for(int i = 0; i < emptyTubes; i++) {
           Tube tube = new Tube();
           tubes.add(tube);
       }
   }

   // methode qui prend en charge le mouvement et son validation
    public boolean moveBall(int from, int to) {
        Tube fromTube = this.getTubes().get(from);
        Tube toTube = this.getTubes().get(to);

        if(isSolved()){
            return false;
        }
        else if(fromTube != null && toTube != null && fromTube != toTube && toTube.getBalls().size() < 6) {
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
            // si le tube est vide, on ne peut pas connaitre si le jeu est gagné ou non
            if(tube.isEmpty()) continue;
            // si le tube n'est pas vide et contient mois de 5 boules alors le jeu nèst pas encore gagné
            if((tube.getBalls().size() < 5) ) return false;
            // si les tubes pleins ont des boules des couleurs differents, le jeu n'est pas encore gagné
            if(!tube.isUniColor()) return false;
        }

        // et enfine si les tubes pleins, remplient par des boules de couleur identique, le jeu est gagné
        // et retourn true

        return true;
    }


    public List<Tube> getTubes() {
        return tubes;
    }

    public Deque<Move> getHistory() {
        return history;
    }

    public int getMoveCounter() {
       return moveCounter;
    }


    public Deque<Move> getRedoMoves() {
        return redoMoves;
    }


    public void winAnimation(GridPane gridPane, StackPane stackPane, BorderPane borderPane){

       // quand la session est gagnée, on fait des animations,
        // les balles se grandissent, et inversement.
        // et il y a aussi du Drop shadow
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

       // on fait l'appel a la methode qui fait une animation pour le text (YOU WON!)
       winTextAnimation(gridPane, stackPane, borderPane);
    }


    //fait une animation pour le text (YOU WON!)
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


       // si on clicke sur n'import chose, les effets sont desactivés,
        // pour qu'on puisse faire des autres actions, comme jouer une autre partie...
        stackPane.setOnMouseClicked(mouseEvent -> {
            borderPane.setDisable(false);
            borderPane.setEffect(null);
            label.setVisible(false);
        });
    }

}
