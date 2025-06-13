package org.example.ballsort.model;

public class Move {
    private int fromIndex;
    private int toIndex;

    public Move(int fromIndex, int toIndex) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public int getFromIndex() {
        return fromIndex;
    }

    public int getToIndex() {
        return toIndex;
    }
}
