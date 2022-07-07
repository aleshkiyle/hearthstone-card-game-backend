package ru.tinkoff.cardgame.game.model;

import java.util.List;

public class Shop {
    private List<Card> cardList;
    private boolean freezeStatus;
    private int lvl;

    public Shop(List<Card> cardList, boolean freezeStatus, int lvl) {

        this.cardList = cardList;
        this.freezeStatus = freezeStatus;
        this.lvl = lvl;
    }




    public boolean isFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(boolean freezeStatus) {
        this.freezeStatus = freezeStatus;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }






    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}
