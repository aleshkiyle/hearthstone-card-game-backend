package ru.tinkoff.cardgame.lobby.model;

import lombok.Data;

@Data
public class WSLobbyMessage {
    private String username;
    private String lobbyId;


    public WSLobbyMessage(String username, String lobbyId) {
        this.username = username;
        this.lobbyId = lobbyId;
    }

}
