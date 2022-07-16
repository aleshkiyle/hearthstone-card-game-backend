package ru.tinkoff.cardgame.game.model;

import lombok.Data;
import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.card.CardProvider;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Shop {

    private static final int START_SHOP_LVL = 1;
    private static final int MAX_SHOP_LVL = 5;
    private static final int SHOP_CARD_NUMBER = 3;
    private final CopyOnWriteArrayList<Card> cardList;
    private boolean freezeStatus;
    private int lvl;

    public Shop() {
        this.cardList = new CopyOnWriteArrayList<>();
        this.freezeStatus = false;
        this.lvl = START_SHOP_LVL;
    }

    public void upLvl() {
        if (this.lvl < MAX_SHOP_LVL) {
            this.lvl++;
        }
    }

    public void updateShop() {
        if (!isFreezeStatus()) {
            this.cardList.clear();
            for (int i = 0; i < SHOP_CARD_NUMBER; i++) {
                this.cardList.add(CardProvider.INSTANCE.getRandomLvlCard(this.lvl));
            }
        }
    }

    public Card buyCard(int index) {
        return this.cardList.remove(index);
    }
}
