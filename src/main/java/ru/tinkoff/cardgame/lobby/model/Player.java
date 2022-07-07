package ru.tinkoff.cardgame.lobby.model;

public class Player {
    private String username;
    private String sessionId;

    public Player(String username, String sessionId) {
        this.username = username;
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }

}
