package ru.tinkoff.cardgame.game.model;

import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {

    private final String id;
    private int hp;
    private int gold;
    private final CopyOnWriteArrayList<Card> activeCards;
    private final CopyOnWriteArrayList<Card> invCards;
    private final Shop shop;

    public Player(String id) {
        this.id = id;
        this.hp = 100;
        this.gold = 0;
        this.activeCards = new CopyOnWriteArrayList<>();
        this.invCards = new CopyOnWriteArrayList<>();
        this.shop = new Shop();
    }

    public String getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }

    public int getGold() {
        return gold;
    }

    public List<Card> getActiveCards() {
        return activeCards;
    }

    public List<Card> getInvCards() {
        return invCards;
    }

    public Shop getShop() {
        return shop;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", hp=" + hp +
                ", gold=" + gold +
                ", activeCards=" + activeCards +
                ", invCards=" + invCards +
                ", shop=" + shop +
                '}';
    }
}