package org.example.ballsort.model;

import java.util.Stack;

public class Tube {
    private final int maxBallsNb = 5;
    private Stack<Ball> balls ;

    public Tube() {
        this.balls = new Stack<>();
    }

    public Stack<Ball> getBalls() {
        return balls;
    }


    public boolean pushBall(Ball ball) {
        if(ball != null) {
            this.getBalls().add(ball);
            return true;
        }
        return false;
    }
    public Ball popBall() {
        if(!this.getBalls().isEmpty()) {
            return this.getBalls().pop();

        }
        return null;
    }

    public Ball peekBall() {
        if(!this.getBalls().isEmpty()) {
            return this.getBalls().peek();
        }
        return null;
    }

    public boolean isEmpty() {
        if (this.getBalls().isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        if(balls.size() == maxBallsNb) return true;
        return false;
    }
    public boolean isUniColor(){
        String firstColor = this.getBalls().peek().getColor();
        return balls.stream().allMatch(b -> b.getColor().equals(firstColor));

    }
}
