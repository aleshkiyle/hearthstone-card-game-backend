package ru.tinkoff.cardgame.game.model;

import java.util.List;

public class Game {
    private List<Player> players;
    private int roundNumber;
    private List<Round> rounds;

    public Game(List<Player> players, int roundNumber, List<Round> rounds) {
        this.players = players;
        this.roundNumber = roundNumber;
        this.rounds = rounds;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<Round> getRounds() {
        return rounds;
    }
}
