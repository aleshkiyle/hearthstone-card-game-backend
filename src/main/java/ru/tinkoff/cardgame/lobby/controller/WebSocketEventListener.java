package ru.tinkoff.cardgame.lobby.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.tinkoff.cardgame.lobby.exceptions.LobbyException;
import ru.tinkoff.cardgame.lobby.model.LobbiesSingleton;
import ru.tinkoff.cardgame.lobby.model.Lobby;
import ru.tinkoff.cardgame.lobby.model.Player;
import ru.tinkoff.cardgame.lobby.model.WSLobbyMessage;

import java.util.Optional;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    // FIXME: 07.07.2022
    // rewrite this code
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        WSLobbyMessage lobbyMessage = (WSLobbyMessage) headerAccessor.getSessionAttributes().get("lobby");
        String sessionId = (String) headerAccessor.getSessionAttributes().get("sessionId");
        if (lobbyMessage.getUsername() != null) {
            Optional<Lobby>  lobby = LobbiesSingleton.INSTANCE.findLobby(lobbyMessage.getLobbyId());
            if (LobbiesSingleton.INSTANCE.findLobby(lobbyMessage.getLobbyId()).isPresent()) {
                try {
                    lobby.get().removeUser(new Player(lobbyMessage.getUsername(), sessionId));
                } catch (LobbyException e) {
                    throw new RuntimeException(e);
                }
                logger.info("LEAVE " + lobby);

                if (lobby.get().getPlayers().size() == 0) {
                    LobbiesSingleton.INSTANCE.getLobbies().remove(lobby.get());
                    logger.info("REMOVE lobby " + lobby);
                }
                messagingTemplate.convertAndSend("/topic/public/" + lobby.get().getId(), lobby);
            }
        }
    }

}
