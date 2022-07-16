package ru.tinkoff.cardgame.game.model;

import lombok.Data;
import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Player {

    private final String id;
    private int hp;
    private int gold;
    private CopyOnWriteArrayList<Card> activeCards;
    private CopyOnWriteArrayList<Card> invCards;
    private final Shop shop;

    public Player(String id) {
        this.id = id;
        this.hp = 100;
        this.gold = 0;
        this.activeCards = new CopyOnWriteArrayList<>();
        this.invCards = new CopyOnWriteArrayList<>();
        this.shop = new Shop();
    }

    public void decreaseGold(int value) {
        this.gold -= value;
    }
}
