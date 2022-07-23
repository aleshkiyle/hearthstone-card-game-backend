package ru.tinkoff.cardgame.lobby.model;

import lombok.Data;
import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Lobby {

    private final String id;
    private final int playerCount;

    private LobbyStatus status;

    private final CopyOnWriteArrayList<User> users;

    public Lobby(String id, int playerCount) {
        this.id = id;
        this.playerCount = playerCount;
        this.status = LobbyStatus.CREATED;
        this.users = new CopyOnWriteArrayList<>();
    }

    public void addUser(User user) throws LobbyException {
        if (users.size() < playerCount & !checkUserInList(user.getSessionId())) {
            users.add(user);
        } else {
            throw new LobbyException();
        }
    }

    private boolean checkUserInList(String sessionId) {
        return users.stream().anyMatch(p -> p.getSessionId().equals(sessionId));
    }

    public void removeUser(String sessionId) {
        users.removeIf(p -> p.getSessionId().equals(sessionId));
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

}
