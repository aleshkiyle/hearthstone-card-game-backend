package ru.tinkoff.cardgame.lobby.model;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public enum LobbiesProvider {
    INSTANCE;
    private final CopyOnWriteArrayList<Lobby> lobbies = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public  Optional<Lobby> findLobby(String id) {
        return lobbies.stream().filter(l -> l.getId().equals(id)).findFirst();
    }

}
