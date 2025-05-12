package org.example.ballsort.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static int incre = 1;
    private int id;
    private String name;
    private String password;
    private String email;
    private List<GameSession> sessions;

    public User(String name, String password, String email) {
        this.id = incre++;
        this.name = name;
        this.password = password;
        this.email = email;
        this.sessions = new ArrayList<GameSession>();
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<GameSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<GameSession> sessions) {
        this.sessions = sessions;
    }
}
