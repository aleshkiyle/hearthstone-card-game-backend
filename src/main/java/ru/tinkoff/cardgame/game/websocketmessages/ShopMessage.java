package ru.tinkoff.cardgame.game.websocketmessages;

import lombok.Data;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;

import java.util.List;

@Data
public class ShopMessage {
    private final Player player;
    private final List<Player> players;
}


