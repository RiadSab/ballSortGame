package org.example.ballsort.model;

import javafx.beans.property.*;

public class Score {
    private int rank;
    private int value;
    private User user;

    public Score(int value, User user, int rank) {
        this.value = value;
        this.user = user;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public IntegerProperty rankProperty() {
        return new SimpleIntegerProperty(rank);
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public IntegerProperty valueProperty() {
        return new SimpleIntegerProperty(value);
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public StringProperty userProperty() {
        return new SimpleStringProperty(user.getName());
    }
}
