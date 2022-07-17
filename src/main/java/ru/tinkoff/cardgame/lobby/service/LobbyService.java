package ru.tinkoff.cardgame.lobby.service;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.cardgame.game.model.Notificator;
import ru.tinkoff.cardgame.game.model.gamelogic.Game;
import ru.tinkoff.cardgame.game.model.gamelogic.GameProvider;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;
import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;
import ru.tinkoff.cardgame.lobby.model.LobbiesProvider;
import ru.tinkoff.cardgame.lobby.model.Lobby;
import ru.tinkoff.cardgame.lobby.model.LobbyStatus;
import ru.tinkoff.cardgame.lobby.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LobbyService {

    public ResponseEntity<String> checkLobby(String id) {
        Optional<Lobby> lobby = LobbiesProvider.INSTANCE.getLobbies().stream()
                .filter(x -> x.getId().equals(id))
                .findAny();
        return lobby.isPresent() ? ResponseEntity.ok("OK") : ResponseEntity.notFound().build();
    }
    
    public Lobby createLobby(String userName, String sessionId) throws LobbyException {
        Lobby lobby;
        String lobbyId;

        // FIXME: 08.07.2022
        // refactor random
        synchronized (LobbiesProvider.INSTANCE.getLobbies()) {

            if (LobbiesProvider.INSTANCE.getLobbies().size() == 0) {
                lobbyId = "0";
            } else {
                lobbyId = String.valueOf(Integer.parseInt(LobbiesProvider.INSTANCE.getLobbies()
                        .get(LobbiesProvider.INSTANCE.getLobbies().size() - 1).getId()) + 1);
            }
            lobby = new Lobby(lobbyId, 2);
            LobbiesProvider.INSTANCE.getLobbies().add(lobby);
        }

        lobby.addUser(new User(userName, sessionId));
        return lobby;
    }

    public void joinLobby(final Lobby lobby, String userName, String sessionId, Logger logger,
                          SimpMessagingTemplate simpMessagingTemplate, Notificator notificator) throws LobbyException {
        synchronized (lobby) {
            if (lobby.getUsers().size() < lobby.getPlayerCount() && lobby.getStatus() == LobbyStatus.CREATED) {

                lobby.addUser(new User(userName, sessionId));
                logger.info("JOIN lobby" + lobby);
                simpMessagingTemplate.convertAndSend("/topic/public/" + lobby.getId(), lobby);

                if (lobby.getUsers().size() == lobby.getPlayerCount()) {
                    createGame(lobby, notificator);
                }
            }
        }
    }

    private void createGame(Lobby lobby, Notificator notificator) {
        List<Player> playerList = new ArrayList<>();
        lobby.getUsers().forEach(u -> playerList.add(new Player(u.getSessionId())));
        Game game = new Game(lobby.getId(), playerList, notificator);
        GameProvider.INSTANCE.getGames().add(game);
        game.startGame();
        game.getPlayers().forEach(p -> notificator.notifyGameStart(p.getId(), p));
        lobby.setStatus(LobbyStatus.STARTED);
    }
}
