package ru.tinkoff.cardgame.game.model.gamelogic;

import lombok.Data;
import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class  Player {

    private final String id;
    private int hp;
    private int gold;
    private  CopyOnWriteArrayList<Card> activeCards;
    private  CopyOnWriteArrayList<Card> invCards;
    private final Shop shop;
    private int maxGold;

    public Player(String id) {
        this.id = id;
        this.hp = 10;
        this.gold = 100;
        this.activeCards = new CopyOnWriteArrayList<>();
        this.invCards = new CopyOnWriteArrayList<>();
        this.shop = new Shop();
        this.maxGold = 3;
    }

    public void increaseGold(int value) {
        if (gold + value <= maxGold) {
            this.gold += value;
        } else {
            this.gold = this.maxGold;
        }
    }

    public void decreaseGold(int value) {
        this.gold -= value;
    }
}
