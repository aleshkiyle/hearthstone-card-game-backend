package ru.tinkoff.cardgame.lobby.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import ru.tinkoff.cardgame.game.model.Game;
import ru.tinkoff.cardgame.game.model.GameProvider;
import ru.tinkoff.cardgame.game.model.Player;
import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;
import ru.tinkoff.cardgame.lobby.model.LobbiesProvider;
import ru.tinkoff.cardgame.lobby.model.Lobby;
import ru.tinkoff.cardgame.lobby.model.User;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LobbyController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(LobbyController.class);

    @MessageMapping("/lobby.create")
    @SendToUser("/queue/create")
    public Lobby createLobby(@Payload WSLobbyMessage lobbyMessage, SimpMessageHeaderAccessor headerAccessor,
                             @Header("simpSessionId") String sessionId) throws LobbyException {

        headerAccessor.getSessionAttributes().put("sessionId", sessionId);
        Lobby lobby;
        String lobbyId;
        // FIXME: 08.07.2022
        // refactor random
        synchronized (LobbiesProvider.INSTANCE.getLobbies()) {

            if (LobbiesProvider.INSTANCE.getLobbies().size() == 0) {
                lobbyId = "0";
            } else {
                lobbyId = String.valueOf(Integer.parseInt(LobbiesProvider.INSTANCE.getLobbies().get(LobbiesProvider.INSTANCE.getLobbies().size() - 1).getId()) + 1);
            }
            lobby = new Lobby(lobbyId, 4);
            LobbiesProvider.INSTANCE.getLobbies().add(lobby);
        }
        lobbyMessage.setLobbyId(lobbyId);
        headerAccessor.getSessionAttributes().put("lobby", lobbyMessage);

        lobby.addUser(new User(lobbyMessage.getUsername(), sessionId));
        logger.info("CREATE lobby" + lobby);
        return lobby;
    }

    @MessageMapping("/lobby.join")
    public void joinLobby(@Payload WSLobbyMessage lobbyMessage, SimpMessageHeaderAccessor headerAccessor,
                          @Header("simpSessionId") String sessionId) throws LobbyException {

        headerAccessor.getSessionAttributes().put("lobby", lobbyMessage);
        headerAccessor.getSessionAttributes().put("sessionId", sessionId);
        Lobby lobby = LobbiesProvider.INSTANCE.findLobby(lobbyMessage.getLobbyId()).get();
        lobby.addUser(new User(lobbyMessage.getUsername(), sessionId));
        logger.info("JOIN lobby" + lobby);
        simpMessagingTemplate.convertAndSend("/topic/public/" + lobby.getId(), lobby);

        if (lobby.getUsers().size() == lobby.getPlayerCount()) {
            List<Player> playerList = new ArrayList<>();
            lobby.getUsers().forEach(u-> playerList.add(new Player(u.getSessionId())));
            Game game = new Game(lobby.getId(), playerList, simpMessagingTemplate);
            GameProvider.INSTANCE.getGames().add(game);
            game.startGame();
        }
    }



    @MessageMapping("/lobby.start")
    public void startGame(@Payload WSLobbyMessage lobbyMessage, SimpMessageHeaderAccessor headerAccessor) {
        // TODO: 07.07.2022
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        logger.info("Error: " + exception.getMessage());
        return exception.getMessage();
    }

}