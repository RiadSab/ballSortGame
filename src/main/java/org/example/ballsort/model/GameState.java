package org.example.ballsort.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
            if(!tube.isEmpty() && !tube.isUniColor()) return false;
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
}
