package ru.tinkoff.cardgame.game.services;

import org.springframework.stereotype.Service;
import ru.tinkoff.cardgame.game.exceptions.IncorrectPlayerActionException;
import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.card.Spell;
import ru.tinkoff.cardgame.game.model.card.SpellController;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;
import ru.tinkoff.cardgame.game.model.gamelogic.Shop;

import java.util.Collections;

@Service
public class PlayerService {

    private static final int SHOP_UPDATE_PRICE = 1;
    private static final int SELL_CARD_PRICE = 1;
    private static final int MAX_CARD_COUNT = 7;

    /*
    Buy a card from the store
     */
    public void buyCardFromShop(Player player, int cardIndex) throws IncorrectPlayerActionException {
        int price = player.getShop().getCardList().get(cardIndex).getPrice();
        if (player.getInvCards().size() < MAX_CARD_COUNT && price <= player.getGold()) {
            player.getInvCards().add(player.getShop().buyCard(cardIndex));
            player.decreaseGold(price);
        } else {
            throw new IncorrectPlayerActionException();
        }
    }

    /*
    Freeze/Unfreeze Store
     */
    public void changeFreezeShop(Player player) {
        player.getShop().setFreezeStatus(!player.getShop().isFreezeStatus());
    }

    /*
    Update store
     */
    public void updateShop(Player player) throws IncorrectPlayerActionException {
        if (player.getGold() >= SHOP_UPDATE_PRICE) {
            player.getShop().updateShop();
            player.decreaseGold(SHOP_UPDATE_PRICE);
        } else {
            throw new IncorrectPlayerActionException();
        }
    }

    /*
    Upgrade the level of the tavern
     */
    public void upgradeLevelShop(Player player) throws IncorrectPlayerActionException {
        if (player.getGold() >= player.getShop().getUpgradePrice() && player.getShop().getLevel() < Shop.MAX_SHOP_LEVEL) {
            player.decreaseGold(player.getShop().getUpgradePrice());
            player.getShop().upgradeLevel();
        } else {
            throw new IncorrectPlayerActionException();
        }
    }

    /*
    Put the card on the table from inventory
     */
    public void putCardToTable(Player player, int cardIndex) throws IncorrectPlayerActionException {
        if (player.getActiveCards().size() < MAX_CARD_COUNT) {
            Card card = player.getInvCards().get(cardIndex);
            Spell spell = card.getSpell();
            switch (spell) {
                case SPELL1 -> SpellController.spell1Realisation(player.getActiveCards());
                case SPELL2 -> SpellController.spell2Realisation(player);
                case SPELL3 -> SpellController.spell3Realisation(player.getActiveCards(), card);
                case SPELL4 -> SpellController.spell4Realisation(player.getActiveCards(), card);
                case SPELL8 -> player.getShop().decreaseUpgradePrice();
                case SPELL12 -> SpellController.spell12Realisation(player.getActiveCards(), card);
                case SPELL19 -> SpellController.spell19Realisation(player.getActiveCards());
                case SPELL20 -> SpellController.spell20Realisation(player.getActiveCards());
            }
            player.getActiveCards().add(player.getInvCards().remove(cardIndex));
        } else {
            throw new IncorrectPlayerActionException();
        }
    }

    /*
    Sell card from inventory
     */
    public void sellInventoryCard(Player player, int cardIndex) {
        Spell spell = player.getInvCards().get(cardIndex).getSpell();
        player.getInvCards().remove(cardIndex);
        switch (spell) {
            case SPELL9 -> player.increaseGold(SELL_CARD_PRICE + 1);
            case SPELL13 -> player.increaseGold(SELL_CARD_PRICE + 2);
            default -> player.increaseGold(SELL_CARD_PRICE);
        }
    }

    /*
    Sell a card from the field
     */
    public void sellActiveCard(Player player, int cardIndex) {
        Spell spell = player.getActiveCards().get(cardIndex).getSpell();
        player.getActiveCards().remove(cardIndex);
        switch (spell) {
            case SPELL9 -> player.increaseGold(SELL_CARD_PRICE + 1);
            case SPELL13 -> player.increaseGold(SELL_CARD_PRICE + 2);
            default -> player.increaseGold(SELL_CARD_PRICE);
        }
    }

    /*
    Move the card to the left on the field
     */
    public void moveCardToLeftOnTable(Player player, int cardIndex) throws IncorrectPlayerActionException {
        if (cardIndex > 0) {
            Collections.swap(player.getActiveCards(), cardIndex, cardIndex - 1);
        } else {
            throw new IncorrectPlayerActionException();
        }
    }

    /*
   Move the card to the right on the field
    */
    public void moveCardToRightOnTable(Player player, int cardIndex) throws IncorrectPlayerActionException {
        if (cardIndex < MAX_CARD_COUNT - 1) {
            Collections.swap(player.getActiveCards(), cardIndex, cardIndex + 1);
        } else {
            throw new IncorrectPlayerActionException();
        }
    }

}
