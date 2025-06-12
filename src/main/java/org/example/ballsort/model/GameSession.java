package org.example.ballsort.model;
import java.time.LocalDateTime;
import static java.lang.Math.abs;

public class GameSession {
    private static int idIncre = 0;
    private User user;
    private int idSession;
    private int idUser;
    private LocalDateTime playedAt;
    private int duration;
    private boolean won;
    private int level;
    private int score;
    private int movesNb;
    private int startTime;
    private int endTime;


    public GameSession(int idUser, LocalDateTime playedAt, int startTime, int endTime, boolean won, int level, int score) {
        this.startTime = LocalDateTime.now().getSecond();
        this.endTime = endTime;
        this.idUser = idUser;
        idIncre++;
        this.idSession = idIncre;
        this.playedAt = playedAt;
        this.duration = abs(endTime - startTime);
        this.won = won;
        this.level = level;
        this.score = score;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
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

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
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
