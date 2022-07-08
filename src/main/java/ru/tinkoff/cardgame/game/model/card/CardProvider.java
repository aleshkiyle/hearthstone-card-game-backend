package ru.tinkoff.cardgame.game.model.card;

import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.concurrent.CopyOnWriteArrayList;

public enum CardProvider {
    INSTANCE;

    private final CopyOnWriteArrayList<Card> cards = new CopyOnWriteArrayList<>();

    public CopyOnWriteArrayList<Card> getCards() {
        return cards;
    }
    
    public Card getRandomLvlCard(int lvl) {
        // TODO: 09.07.2022  
        return null;
    }

}
