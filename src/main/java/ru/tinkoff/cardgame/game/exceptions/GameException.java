package ru.tinkoff.cardgame.game.exceptions;

public class GameException extends Exception {
    public GameException() {
    }

    public GameException(String message) {
        super(message);
    }
}
