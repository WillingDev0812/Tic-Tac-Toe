package com.iti.tictactoe;



import com.google.gson.JsonObject;

public class Player {

    private int id;
    private String username;
    private String email;
    private String password;
    private int score;
    private String status;

    public Player() {
        this.id = 0;
        this.username = null;
        this.email = null;
        this.password = null;
        this.score = 0;
        this.status = "offline";
    }

    public Player(String username, String email, String password) {
        this.id = 0;
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.status = "offline";
    }

    public Player(int id, String username, String email, int score, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.score = score;
        this.status = status;
    }

    public Player(int id, String username, String email, String password, int score, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // convert Player object to JSON
    public JsonObject getPlayerAsJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("score", score);
        jsonObject.addProperty("status", status);
        return jsonObject;
    }

}

