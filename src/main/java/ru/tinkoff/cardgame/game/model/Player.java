package ru.tinkoff.cardgame.game.model;

import java.util.List;

public class Player {
    private int hp;
    private int gold;
    private List<Card> activeCards;
    private List<Card> invCards;
    private Shop shop;

    public Player(int hp, int gold, List<Card> activeCards, List<Card> invCards, Shop shop) {
        this.hp = hp;
        this.gold = gold;
        this.activeCards = activeCards;
        this.invCards = invCards;
        this.shop = shop;
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

    public void setActiveCards(List<Card> activeCards) {
        this.activeCards = activeCards;
    }

    public void setInvCards(List<Card> invCards) {
        this.invCards = invCards;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
