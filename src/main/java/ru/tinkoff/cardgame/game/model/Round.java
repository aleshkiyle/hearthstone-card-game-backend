package ru.tinkoff.cardgame.game.model;


import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class Round {
    private final Player firstPlayer;
    private final Player secondPlayer;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public Round(Player firstPlayer, Player secondPlayer, SimpMessagingTemplate simpMessagingTemplate) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void test() {
        System.out.println("test");
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(firstPlayer.getId());
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(firstPlayer.getId(), "/queue/game/round/start", this,
                headerAccessor.getMessageHeaders());

        headerAccessor = SimpMessageHeaderAccessor
                .create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(secondPlayer.getId());
        headerAccessor.setLeaveMutable(true);

        simpMessagingTemplate.convertAndSendToUser(secondPlayer.getId(), "/queue/game/round/start", this,
                headerAccessor.getMessageHeaders());
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    @Override
    public String toString() {
        return "Round{" +
                "firstPlayer=" + firstPlayer +
                ", secondPlayer=" + secondPlayer +
                '}';
    }
}
