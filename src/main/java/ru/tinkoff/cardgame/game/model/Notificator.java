package ru.tinkoff.cardgame.game.model;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;

public class Notificator {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public Notificator(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void notifyGameStart(String playerSessionId, Player player) {
        notifyFront(playerSessionId, "/queue/game/start", player);
    }

    public void notifyShopStart(String playerSessionId, Player player) {
        notifyFront(playerSessionId, "/queue/game/shop/start", player);
    }

    public void notifyShopUpdate(String playerSessionId, WSRoundMessage roundMessage) {
        notifyFront(playerSessionId, "/queue/game/shop/update", roundMessage);
    }

    public void notifyRoundStart(String playerSessionId, WSRoundMessage roundMessage) {
        notifyFront(playerSessionId, "/queue/game/round/start", roundMessage);
    }


    public void notifyRoundUpdate(String playerSessionId, WSRoundMessage roundMessage) {
        notifyFront(playerSessionId, "/queue/game/round/update", roundMessage);
    }

    public void notifyGameOver(String playerSessionId, WSGameOverMessage gameOverMessage) {
        notifyFront(playerSessionId, "/queue/game/end", gameOverMessage);
    }

    private void notifyFront(String playerSessionId, String path, Object payload) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(playerSessionId);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(playerSessionId, path, payload,
                headerAccessor.getMessageHeaders());
    }

}
