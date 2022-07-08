package ru.tinkoff.cardgame.lobby.model;

/*
 * POJO (TO)
 */
public class WSLobbyMessage {
    private String username;
    private String lobbyId;


    public WSLobbyMessage(String username, String lobbyId) {
        this.username = username;
        this.lobbyId = lobbyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public String toString() {
        return "WSLobbyMessage{" +
                "username='" + username + '\'' +
                ", lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
