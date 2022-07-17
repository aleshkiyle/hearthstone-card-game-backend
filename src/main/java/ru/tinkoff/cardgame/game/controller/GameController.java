package ru.tinkoff.cardgame.game.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import ru.tinkoff.cardgame.game.exceptions.GameException;
import ru.tinkoff.cardgame.game.exceptions.IncorrectPlayerActionException;
import ru.tinkoff.cardgame.game.model.gamelogic.Game;
import ru.tinkoff.cardgame.game.model.gamelogic.GameProvider;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;
import ru.tinkoff.cardgame.game.services.PlayerService;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;

@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final PlayerService playerService;

    public GameController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @MessageMapping("/game.buyCard")
    @SendToUser("/queue/game/shop/update")
    public Player buyCard(SimpMessageHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId,
                          @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.buyCardFromShop(player, cardIndex);
        return player;
    }

    @MessageMapping("/game.changeFreezeShop")
    @SendToUser("/queue/game/shop/update")
    public Player changeFreezeShop(SimpMessageHeaderAccessor headerAccessor,
                                   @Header("simpSessionId") String sessionId) {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.changeFreezeShop(player);
        return player;
    }

    @MessageMapping("/game.updateShop")
    @SendToUser("/queue/game/shop/update")
    public Player updateShop(SimpMessageHeaderAccessor headerAccessor,
                             @Header("simpSessionId") String sessionId) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.updateShop(player);
        return player;
    }

    @MessageMapping("/game.upgradeShop")
    @SendToUser("/queue/game/shop/update")
    public Player upgradeShop(SimpMessageHeaderAccessor headerAccessor,
                              @Header("simpSessionId") String sessionId) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.upgradeLevelShop(player);
        return player;
    }

    @MessageMapping("/game.putCardToTable")
    @SendToUser("/queue/game/shop/update")
    public Player putCardToTable(SimpMessageHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId,
                                 @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.putCardToTable(player, cardIndex);
        return player;
    }

    @MessageMapping("/game.sellInventoryCard")
    @SendToUser("/queue/game/shop/update")
    public Player sellInventoryCard(SimpMessageHeaderAccessor headerAccessor,
                                    @Header("simpSessionId") String sessionId, @Payload int cardIndex) {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.sellInventoryCard(player, cardIndex);
        return player;
    }

    @MessageMapping("/game.sellActiveCard")
    @SendToUser("/queue/game/shop/update")
    public Player sellActiveCard(SimpMessageHeaderAccessor headerAccessor,
                                 @Header("simpSessionId") String sessionId, @Payload int cardIndex) {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.sellActiveCard(player, cardIndex);
        return player;
    }

    @MessageMapping("/game.moveCardLeft")
    @SendToUser("/queue/game/shop/update")
    public Player moveCardToLeftOnTable(SimpMessageHeaderAccessor headerAccessor,
                                        @Header("simpSessionId") String sessionId,
                                        @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.moveCardToLeftOnTable(player, cardIndex);
        return player;
    }

    @MessageMapping("/game.moveCardRight")
    @SendToUser("/queue/game/shop/update")
    public Player moveCardToRightOnTable(SimpMessageHeaderAccessor headerAccessor,
                                         @Header("simpSessionId") String sessionId,
                                         @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.moveCardToRightOnTable(player, cardIndex);
        return player;
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

    private Player findPlayer(SimpMessageHeaderAccessor headerAccessor, String sessionId) {
        WSLobbyMessage lobbyMessage = (WSLobbyMessage) headerAccessor.getSessionAttributes().get("lobby");
        Game game = GameProvider.INSTANCE.findGame(lobbyMessage.getLobbyId());
        return game.findPlayer(sessionId);
    }
}
