package org.example.ballsort.model;

import java.sql.Date;

public class gameSession {
    private User user;
    private int idSession;
    private int idUser;
    private Date playedAt;
    private int duration;
    private boolean won;
    private int level;
    private int score;
    private int movesNb;

    public gameSession(int idUser, int idSession, Date playedAt, int duration, boolean won, int level, int score) {
        this.idUser = idUser;
        this.idSession = idSession;
        this.playedAt = playedAt;
        this.duration = duration;
        this.won = won;
        this.level = level;
        this.score = score;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getIdSession() {
        return idSession;
    }

    public void setIdSession(int idSession) {
        this.idSession = idSession;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public java.sql.Date getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(Date playedAt) {
        this.playedAt = playedAt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMovesNb() {
        return movesNb;
    }

    public void setMovesNb(int movesNb) {
        this.movesNb = movesNb;
    }
}
