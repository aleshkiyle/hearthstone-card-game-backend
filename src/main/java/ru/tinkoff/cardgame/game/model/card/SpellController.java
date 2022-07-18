package ru.tinkoff.cardgame.game.model.card;

import ru.tinkoff.cardgame.game.model.Player;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpellController {

    public static void spell2Realisation(Player player) {
        player.setGold(player.getGold() + 1);
    }

    public static void spell3Realisation(CopyOnWriteArrayList<Card> cards, Card activeCard) {
        CardClass cardClass = activeCard.getCardClass();
        for (Card card : cards) if (card.getCardClass().equals(cardClass)) card.setDamage(card.getDamage() + 2);
    }

    public static void spell4Realisation(CopyOnWriteArrayList<Card> cards, Card activeCard) {
        CardClass cardClass = activeCard.getCardClass();
        for (Card card : cards) if (card.getCardClass().equals(cardClass)) card.setHp(card.getHp() + 2);
    }

    public static void spell6Realisation(CopyOnWriteArrayList<Card> cards) {
        for (Card card : cards) {
            card.setDamage(card.getDamage() + 1);
            card.setHp(card.getHp() + 1);
        }
    }

    public static void spell7Realisation(CopyOnWriteArrayList<Card> cards, Card dyingCard) {
        CardClass cardClass = dyingCard.getCardClass();
        for (Card card : cards) if (card.getSpell().equals(Spell.SPELL7) && card.getCardClass().equals(cardClass)) card.setHp(card.getHp() + 1);
    }

    public static void spell10Realisation(Player player, Card activeCard) {
        for (int i = 0; i < player.getGold(); i++) {
            activeCard.setDamage(activeCard.getDamage() + 1);
            activeCard.setHp(activeCard.getHp() + 1);
        }
    }

    public static void spell12Realisation(CopyOnWriteArrayList<Card> cards, Card activeCard) {
        CardClass cardClass = activeCard.getCardClass();
        for (Card card : cards) if (card.getCardClass().equals(cardClass)) {
            card.setDamage(card.getDamage() + 1);
            card.setHp(card.getHp() + 3);
        }
    }

    public static void spell14Realisation(CopyOnWriteArrayList<Card> cards) {
        for (Card card : cards) card.setHp(card.getHp() + 1);
    }

    public static void spell16Realisation(CopyOnWriteArrayList<Card> cards) {
        Random random = new Random();
        Card card = cards.get(random.nextInt(cards.size()));
        card.setDamage(card.getDamage() + 2);
        card.setHp(card.getHp() + 2);
    }

    public static void spell19Realisation(CopyOnWriteArrayList<Card> cards) {
        Random random = new Random();
        Card card = cards.get(random.nextInt(cards.size()));
        card.setDamage(card.getDamage() + 5);
        card.setHp(card.getHp() + 7);
    }

    public static void spell20Realisation(CopyOnWriteArrayList<Card> cards) {
        Random random = new Random();
        Card card1 = cards.get(random.nextInt(cards.size()));
        card1.setDamage(card1.getDamage() + 3);
        card1.setHp(card1.getHp() + 3);
        if (cards.size() > 1) {
            Card card2 = cards.get(random.nextInt(cards.size()));
            while (card1 == card2) card2 = cards.get(random.nextInt(cards.size()));
            card2.setDamage(card2.getDamage() + 3);
            card2.setHp(card2.getHp() + 3);
        }
    }

    public static void spell21Realisation(CopyOnWriteArrayList<Card> cards) {
        Random random = new Random();
        Card card = cards.get(random.nextInt(cards.size()));
        card.setDamage(card.getDamage() + 3);
        card.setHp(card.getHp() + 2);
    }

    public static void spell22Realisation(CopyOnWriteArrayList<Card> cards) {
        for (Card card : cards) {
            if (card.getSpell().equals(Spell.SPELL22)) {
                CardClass cardClass = card.getCardClass();
                for (Card cardType : cards) {
                    if (cardType != card && cardType.getCardClass().equals(cardClass)) {
                        card.setDamage(card.getDamage() + 1);
                        card.setHp(card.getHp() + 1);
                    }
                }
            }
        }
    }
}
