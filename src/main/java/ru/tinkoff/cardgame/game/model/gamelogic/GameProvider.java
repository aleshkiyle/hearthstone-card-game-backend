package ru.tinkoff.cardgame.game.model.gamelogic;

import java.util.concurrent.CopyOnWriteArrayList;

public enum GameProvider {
    INSTANCE;

    private final CopyOnWriteArrayList<Game> games = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Game> getGames() {
        return games;
    }

    public Game findGame(String id){
        return games.stream()
                .filter(game -> game.getId().equals(id))
                .findFirst()
                .get();
    }
}
