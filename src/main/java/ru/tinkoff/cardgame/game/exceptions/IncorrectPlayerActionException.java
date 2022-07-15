package ru.tinkoff.cardgame.game.exceptions;

public class IncorrectPlayerActionException extends Exception {
    public IncorrectPlayerActionException() {
    }

    public IncorrectPlayerActionException(String message) {
        super(message);
    }
}
