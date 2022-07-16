package ru.tinkoff.cardgame.game.model.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.cardgame.game.model.Notificator;
import ru.tinkoff.cardgame.game.model.WSRoundMessage;

public class Round {

    private static final Logger logger = LoggerFactory.getLogger(Round.class);
    private final Player firstPlayer;
    private final Player secondPlayer;

    private final Notificator notificator;

    public Round(Player firstPlayer, Player secondPlayer, Notificator notificator) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.notificator = notificator;
    }

    public void test() {
        WSRoundMessage roundMessage = new WSRoundMessage(firstPlayer.getActiveCards(), secondPlayer.getActiveCards());
        notificator.notifyRound(firstPlayer.getId(), roundMessage);
        roundMessage = new WSRoundMessage(secondPlayer.getActiveCards(), firstPlayer.getActiveCards());
        notificator.notifyRound(secondPlayer.getId(), roundMessage);
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
