package ru.tinkoff.cardgame.game.model;

import lombok.Data;
import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.List;

@Data
public class WSRoundMessage {
    private final List<Card> playerCard;
    private final List<Card> opponentCard;
    private final int attackCardIndex;
    private final int defenceCardIndex;

    public WSRoundMessage(List<Card> playerCard, List<Card> opponentCard, int attackCardIndex, int defenceCardIndex) {
        this.playerCard = playerCard;
        this.opponentCard = opponentCard;
        this.attackCardIndex = attackCardIndex;
        this.defenceCardIndex = defenceCardIndex;
    }

    public WSRoundMessage(List<Card> playerCard, List<Card> opponentCard) {
        this.playerCard = playerCard;
        this.opponentCard = opponentCard;
        this.attackCardIndex = -1;
        this.defenceCardIndex = -1;
    }
}
