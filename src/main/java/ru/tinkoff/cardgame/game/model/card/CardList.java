package ru.tinkoff.cardgame.game.model.card;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CardList extends CopyOnWriteArrayList<Card> implements Serializable {
    private CopyOnWriteArrayList<Card> cardList;


    public CardList() {
    }

    public CardList(List<Card> cards) {
        this.cardList = (CopyOnWriteArrayList<Card>) cards;
    }

    public CopyOnWriteArrayList<Card> getCardList() {
        return this.cardList;
    }
}
