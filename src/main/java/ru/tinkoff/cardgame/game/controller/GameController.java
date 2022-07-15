package ru.tinkoff.cardgame.game.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.tinkoff.cardgame.game.exceptions.GameException;
import ru.tinkoff.cardgame.game.exceptions.IncorrectPlayerActionException;
import ru.tinkoff.cardgame.game.model.Game;
import ru.tinkoff.cardgame.game.model.GameProvider;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    @MessageMapping("/game/updateShop")
    public void updateShop(SimpMessageHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId) {
        WSLobbyMessage lobbyMessage = (WSLobbyMessage) headerAccessor.getSessionAttributes().get("lobby");
        Game game = GameProvider.INSTANCE.findGame(lobbyMessage.getLobbyId());
        game.updateShop(sessionId);
    }

    @MessageExceptionHandler(GameException.class)
    @SendToUser("/queue/game/error")
    public String handleGameException(SimpMessageHeaderAccessor headerAccessor, Throwable exception) {
        exception.printStackTrace();
        // TODO: 15.07.2022
        // critical exception in game

        //WSLobbyMessage lobbyMessage = (WSLobbyMessage) headerAccessor.getSessionAttributes().get("lobby");
        //GameProvider.INSTANCE.findGame(lobbyMessage.getLobbyId()).stopGame();

        return exception.getMessage();
    }

    @MessageExceptionHandler(IncorrectPlayerActionException.class)
    @SendToUser("/queue/game/incorrect")
    public String handlePlayerActionException(Throwable exception) {
        return exception.getMessage();
    }
}
