package ru.tinkoff.cardgame.lobby.model;

import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;

import java.util.concurrent.CopyOnWriteArrayList;

public class Lobby {
    private final String id;
    private final int playerCount;

    private LobbyStatus status;

    private final CopyOnWriteArrayList<Player> players;

    public Lobby(String id, int playerCount) {
        this.id = id;
        this.playerCount = playerCount;
        this.status = LobbyStatus.CREATED;
        this.players = new CopyOnWriteArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public void addUser(Player player) throws LobbyException {
        //synchronized (users) {
        if (players.size() < playerCount & !checkUserInList(player)) {
            players.add(player);
        } else {
            throw new LobbyException();
        }
        //  }
    }

    public void removeUser(Player player) throws LobbyException {
        //synchronized () {
        if (players.size() > 0 & checkUserInList(player)) {
            players.removeIf(p->p.getSessionId().equals(player.getSessionId()));
        }
        //}
    }

    private boolean checkUserInList(Player player) {
        return players.stream().anyMatch(p -> p.getSessionId().equals(player.getSessionId()));

    }

    public void start() throws LobbyException {
        if (this.status == LobbyStatus.CREATED) {
            this.status = LobbyStatus.STARTED;
        } else {
            throw new LobbyException();
        }
    }

    public void finish() throws LobbyException {
        if (this.status == LobbyStatus.STARTED) {
            this.status = LobbyStatus.FINISHED;
        } else {
            throw new LobbyException();
        }
    }

    @Override
    public String toString() {
        return "Lobby{" +
                "id='" + id + '\'' +
                ", playerCount=" + playerCount +
                ", status=" + status +
                ", users=" + players +
                '}';
    }

}
