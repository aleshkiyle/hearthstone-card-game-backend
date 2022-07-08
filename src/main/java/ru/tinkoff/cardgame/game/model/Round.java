package ru.tinkoff.cardgame.game.model;


public class Round {
    private final Player firstPlayer;
    private final Player secondPlayer;

    public Round(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    @Override
    public String toString() {
        return "Round{" +
                "firstPlayer=" + firstPlayer +
                ", secondPlayer=" + secondPlayer +
                '}';
    }
}
