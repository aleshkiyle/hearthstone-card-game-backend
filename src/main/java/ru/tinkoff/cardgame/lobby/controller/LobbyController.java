package ru.tinkoff.cardgame.lobby.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import ru.tinkoff.cardgame.game.model.Notificator;
import ru.tinkoff.cardgame.game.model.gamelogic.Game;
import ru.tinkoff.cardgame.game.model.gamelogic.GameProvider;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;
import ru.tinkoff.cardgame.lobby.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LobbyController {

   // @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Notificator notificator;

    private static final Logger logger = LoggerFactory.getLogger(LobbyController.class);

    public LobbyController(SimpMessagingTemplate simpMessagingTemplate) {
        System.out.println(simpMessagingTemplate);
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificator = new Notificator(simpMessagingTemplate);
    }

    @GetMapping("/lobby.check")
    public ResponseEntity<String> checkLobby(@RequestParam("id") String id) {
        Optional<Lobby> lobby = LobbiesProvider.INSTANCE.getLobbies().stream()
                .filter(x -> x.getId().equals(id))
                .findAny();
        if (lobby.isPresent()) {
            return ResponseEntity.ok("OK");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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
            lobby = new Lobby(lobbyId, 2);
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
        synchronized (lobby) {
            if (lobby.getUsers().size() < lobby.getPlayerCount() && lobby.getStatus() == LobbyStatus.CREATED) {

                lobby.addUser(new User(lobbyMessage.getUsername(), sessionId));
                logger.info("JOIN lobby" + lobby);
                simpMessagingTemplate.convertAndSend("/topic/public/" + lobby.getId(), lobby);

                if (lobby.getUsers().size() == lobby.getPlayerCount()) {
                    List<Player> playerList = new ArrayList<>();
                    lobby.getUsers().forEach(u -> playerList.add(new Player(u.getSessionId())));
                    Game game = new Game(lobby.getId(), playerList, notificator);
                    GameProvider.INSTANCE.getGames().add(game);
                    game.startGame();
                    lobby.setStatus(LobbyStatus.STARTED);
                }
            }
        }
    }


    @MessageMapping("/lobby.start")
    public void startGame(@Payload WSLobbyMessage lobbyMessage, SimpMessageHeaderAccessor headerAccessor) {
        // TODO: 07.07.2022
    }

    @MessageExceptionHandler(LobbyException.class)
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        logger.info("Error: " + exception.getMessage());
        exception.printStackTrace();
        return exception.getMessage();
    }

}
