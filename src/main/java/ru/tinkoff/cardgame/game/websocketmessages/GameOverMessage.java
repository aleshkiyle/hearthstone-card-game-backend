package ru.tinkoff.cardgame.game.websocketmessages;

import lombok.Data;

@Data
public class GameOverMessage {
    String username;
    boolean isWinner;

    public GameOverMessage(String username, boolean isWinner) {
        this.username = username;
        this.isWinner = isWinner;
    }
}
