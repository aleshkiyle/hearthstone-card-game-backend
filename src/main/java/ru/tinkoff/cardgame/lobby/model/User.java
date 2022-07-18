package ru.tinkoff.cardgame.lobby.model;

import lombok.Data;

@Data
public class User {
    private String username;
    private String sessionId;

    public User(String username, String sessionId) {
        this.username = username;
        this.sessionId = sessionId;
    }
}
