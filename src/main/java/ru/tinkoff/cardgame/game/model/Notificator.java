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

    public void notifyShop(String playerSessionId, Player player) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(playerSessionId);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(playerSessionId, "/queue/game/shop", player,
                headerAccessor.getMessageHeaders());
    }

    public void notifyRound(String playerSessionId, WSRoundMessage roundMessage) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(playerSessionId);
        headerAccessor.setLeaveMutable(true);
        simpMessagingTemplate.convertAndSendToUser(playerSessionId, "/queue/game/round", roundMessage,
                headerAccessor.getMessageHeaders());
    }

}
