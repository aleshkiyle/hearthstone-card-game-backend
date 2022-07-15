package ru.tinkoff.cardgame.game.controller;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import ru.tinkoff.cardgame.game.model.Game;
import ru.tinkoff.cardgame.game.model.GameProvider;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;

@Controller
public class GameController {

    @MessageMapping("/game/updateShop")
    public void updateShop(SimpMessageHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId) {
        WSLobbyMessage lobbyMessage = (WSLobbyMessage) headerAccessor.getSessionAttributes().get("lobby");
        Game game = GameProvider.INSTANCE.findGame(lobbyMessage.getLobbyId());
        game.updateShop(sessionId);
    }
}
