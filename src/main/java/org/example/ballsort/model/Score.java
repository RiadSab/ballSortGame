package org.example.ballsort.model;

import javafx.beans.property.*;


// en general, cette classe est utilisée pour afficher le tableau du score.
public class Score {
    private int rank;
    private int value;
    private User user;

    public Score(int value, User user, int rank) {
        this.value = value;
        this.user = user;
        this.rank = rank;
    }


    public IntegerProperty rankProperty() {
        return new SimpleIntegerProperty(rank);
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
