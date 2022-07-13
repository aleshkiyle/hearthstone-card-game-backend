package ru.tinkoff.cardgame.game.model;

import java.util.concurrent.CopyOnWriteArrayList;

public enum GameProvider {
    INSTANCE;

    private final CopyOnWriteArrayList<Game> games = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Game> getGames() {
        return games;
    }
}
