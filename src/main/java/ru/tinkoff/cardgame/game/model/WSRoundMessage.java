package ru.tinkoff.cardgame.game.model;

import lombok.Data;
import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.List;

@Data
public class WSRoundMessage {
    private final List<Card> playerCard;
    private final List<Card> opponentCard;
    private final int playerCardIndex;
    private final int opponentCardIndex;
    private final boolean isPlayerAttack;

    public WSRoundMessage(List<Card> playerCard, List<Card> opponentCard, int playerCardIndex, int opponentCardIndex, boolean isPlayerAttack) {
        this.playerCard = playerCard;
        this.opponentCard = opponentCard;
        this.playerCardIndex = playerCardIndex;
        this.opponentCardIndex = opponentCardIndex;
        this.isPlayerAttack = isPlayerAttack;
    }

    public WSRoundMessage(List<Card> playerCard, List<Card> opponentCard) {
        this.playerCard = playerCard;
        this.opponentCard = opponentCard;
        this.playerCardIndex = -1;
        this.opponentCardIndex = -1;
        this.isPlayerAttack = false;
    }

}
