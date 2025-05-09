package org.example.ballsort.model;

public class Move {
    private int fromIndex;
    private int toIndex;

    Move(int fromIndex, int toIndex) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }
    public int getFromIndex() {
        return fromIndex;
    }
    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }
    public int getToIndex() {
        return toIndex;
    }
    public void setToIndex(int toIndex) {
        this.toIndex = toIndex;
    }

}
