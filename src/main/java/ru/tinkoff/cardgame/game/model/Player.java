package ru.tinkoff.cardgame.game.model;

import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.card.CardProvider;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {

    private final int id;
    private int hp;
    private int gold;
    private CopyOnWriteArrayList<Card> activeCards;
    private final CopyOnWriteArrayList<Card> invCards;
    private final Shop shop;
    public Player(int id) {
        this.id = id;
        this.hp = 100;
        this.gold = 0;
        this.activeCards = new CopyOnWriteArrayList<>();
        this.invCards = new CopyOnWriteArrayList<>();
        this.shop = new Shop();
    }

    public void setActiveCards(CopyOnWriteArrayList<Card> activeCards) {
        this.activeCards = activeCards;
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
        return "\nPlayer{" +
                "id=" + id +
                ", hp=" + hp +
//                ", gold=" + gold +
                ", activeCards=" + activeCards +
//                ", invCards=" + invCards +
//                ", shop=" + shop +
                '}';
    }
}
