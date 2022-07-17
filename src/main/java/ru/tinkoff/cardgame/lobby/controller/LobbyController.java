package ru.tinkoff.cardgame.lobby.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tinkoff.cardgame.game.model.Notificator;
import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;
import ru.tinkoff.cardgame.lobby.model.LobbiesProvider;
import ru.tinkoff.cardgame.lobby.model.Lobby;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;
import ru.tinkoff.cardgame.lobby.service.LobbyService;

import java.util.Optional;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LobbyController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final LobbyService lobbyService;

    private final Notificator notificator;

    private static final Logger logger = LoggerFactory.getLogger(LobbyController.class);

    public LobbyController(SimpMessagingTemplate simpMessagingTemplate, LobbyService lobbyService) {
        this.lobbyService = lobbyService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.notificator = new Notificator(simpMessagingTemplate);
    }

    @GetMapping("/lobby.check")
    public ResponseEntity<String> checkLobby(@RequestParam("id") String id) {
        return this.lobbyService.checkLobby(id);
    }

    @MessageMapping("/lobby.create")
    @SendToUser("/queue/create")
    public Lobby createLobby(@Payload WSLobbyMessage lobbyMessage, SimpMessageHeaderAccessor headerAccessor,
                             @Header("simpSessionId") String sessionId) throws LobbyException {

        headerAccessor.getSessionAttributes().put("sessionId", sessionId);

        Lobby lobby = this.lobbyService.createLobby(lobbyMessage.getUsername(), sessionId);
        lobbyMessage.setLobbyId(lobby.getId());
        headerAccessor.getSessionAttributes().put("lobby", lobbyMessage);
        logger.info("CREATE lobby" + lobby);
        return lobby;
    }

    @MessageMapping("/lobby.join")
    public void joinLobby(@Payload WSLobbyMessage lobbyMessage, SimpMessageHeaderAccessor headerAccessor,
                          @Header("simpSessionId") String sessionId) throws LobbyException {
        headerAccessor.getSessionAttributes().put("lobby", lobbyMessage);
        headerAccessor.getSessionAttributes().put("sessionId", sessionId);
        Optional<Lobby> lobby = LobbiesProvider.INSTANCE.findLobby(lobbyMessage.getLobbyId());
        if (lobby.isPresent()) {
            this.lobbyService.joinLobby(lobby.get(), lobbyMessage.getUsername(), sessionId,
                    (java.util.logging.Logger) logger, simpMessagingTemplate, notificator);
        } else {
            throw new LobbyException();
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
