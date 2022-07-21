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
import ru.tinkoff.cardgame.game.model.WSShopMessage;
import ru.tinkoff.cardgame.game.model.gamelogic.Game;
import ru.tinkoff.cardgame.game.model.gamelogic.GameProvider;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;
import ru.tinkoff.cardgame.game.services.PlayerService;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;

import java.util.List;

@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final PlayerService playerService;

    public GameController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @MessageMapping("/game.buyCard")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage buyCard(SimpMessageHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId,
                          @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.buyCardFromShop(player, cardIndex);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.changeFreezeShop")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage changeFreezeShop(SimpMessageHeaderAccessor headerAccessor,
                                   @Header("simpSessionId") String sessionId) {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.changeFreezeShop(player);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.updateShop")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage updateShop(SimpMessageHeaderAccessor headerAccessor,
                             @Header("simpSessionId") String sessionId) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.updateShop(player);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.upgradeShop")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage upgradeShop(SimpMessageHeaderAccessor headerAccessor,
                              @Header("simpSessionId") String sessionId) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.upgradeLevelShop(player);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.putCardToTable")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage putCardToTable(SimpMessageHeaderAccessor headerAccessor, @Header("simpSessionId") String sessionId,
                                 @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.putCardToTable(player, cardIndex);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.sellInventoryCard")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage sellInventoryCard(SimpMessageHeaderAccessor headerAccessor,
                                    @Header("simpSessionId") String sessionId, @Payload int cardIndex) {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.sellInventoryCard(player, cardIndex);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.sellActiveCard")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage sellActiveCard(SimpMessageHeaderAccessor headerAccessor,
                                 @Header("simpSessionId") String sessionId, @Payload int cardIndex) {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.sellActiveCard(player, cardIndex);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.moveCardLeft")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage moveCardToLeftOnTable(SimpMessageHeaderAccessor headerAccessor,
                                               @Header("simpSessionId") String sessionId,
                                               @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.moveCardToLeftOnTable(player, cardIndex);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
    }

    @MessageMapping("/game.moveCardRight")
    @SendToUser("/queue/game/shop/update")
    public WSShopMessage moveCardToRightOnTable(SimpMessageHeaderAccessor headerAccessor,
                                         @Header("simpSessionId") String sessionId,
                                         @Payload int cardIndex) throws IncorrectPlayerActionException {
        Player player = findPlayer(headerAccessor, sessionId);
        this.playerService.moveCardToRightOnTable(player, cardIndex);
        List<Player> players = findGame(headerAccessor).getPlayers();
        return new WSShopMessage(player, players);
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

    private Game findGame(SimpMessageHeaderAccessor headerAccessor) {
        WSLobbyMessage lobbyMessage = (WSLobbyMessage) headerAccessor.getSessionAttributes().get("lobby");
        return GameProvider.INSTANCE.findGame(lobbyMessage.getLobbyId());
    }
}
