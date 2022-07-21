package ru.tinkoff.cardgame.game.model;

import lombok.Data;

@Data
public class WSGameOverMessage {
    String username;
    boolean isWinner;

    public WSGameOverMessage(String username, boolean isWinner) {
        this.username = username;
        this.isWinner = isWinner;
    }
}
