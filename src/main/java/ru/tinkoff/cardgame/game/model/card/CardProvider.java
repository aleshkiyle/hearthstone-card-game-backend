package ru.tinkoff.cardgame.game.model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum CardProvider {
    INSTANCE;

    private final ArrayList<Card> cards = new ArrayList<>();

    {
        cards.add(new Card("Ingrid", 3, 2, 1, Spell.SPELL1, 1, CardClass.Witch));
        cards.add(new Card("Lauma", 3, 2, 4, Spell.SPELL4, 2, CardClass.Witch));
        cards.add(new Card("Hecate", 3, 3, 5, Spell.SPELL10, 3, CardClass.Witch));
        cards.add(new Card("Erichto", 3, 5, 6, Spell.SPELL11, 4, CardClass.Witch));
        cards.add(new Card("Medea", 3, 4, 4, Spell.SPELL17, 5, CardClass.Witch));
        cards.add(new Card("Shenron", 3, 1, 2, Spell.SPELL15, 1, CardClass.Dragon));
        cards.add(new Card("Smaug", 3, 3, 5, Spell.SPELL18, 2, CardClass.Dragon));
        cards.add(new Card("Futsanlong", 3, 2, 1, Spell.SPELL19, 3, CardClass.Dragon));
        cards.add(new Card("Ladon", 3, 6, 5, Spell.SPELL14, 4, CardClass.Dragon));
        cards.add(new Card("Ankalagon", 3, 7, 5, Spell.SPELL3, 5, CardClass.Dragon));
        cards.add(new Card("Barbatos", 3, 2, 2, Spell.SPELL13, 1, CardClass.Demon));
        cards.add(new Card("Parki", 3, 2, 4, Spell.SPELL22, 2, CardClass.Demon));
        cards.add(new Card("Belial", 3, 5, 4, Spell.SPELL5, 3, CardClass.Demon));
        cards.add(new Card("Mulciber", 3, 6, 4, Spell.SPELL20, 4, CardClass.Demon));
        cards.add(new Card("Lucifer", 3, 4, 6, Spell.SPELL12, 5, CardClass.Demon));
        cards.add(new Card("Etin", 3, 3, 2, Spell.SPELL2, 1, CardClass.Giant));
        cards.add(new Card("Kalevipoeg", 3, 2, 4, Spell.SPELL8, 2, CardClass.Giant));
        cards.add(new Card("Gundyr", 3, 4, 4, Spell.SPELL9, 3, CardClass.Giant));
        cards.add(new Card("Hrungnir", 3, 5, 6, Spell.SPELL16, 4, CardClass.Giant));
        cards.add(new Card("Atlant", 3, 5, 8, Spell.SPELL7, 5, CardClass.Giant));
        cards.add(new Card("Golem", 3, 1, 4, Spell.SPELL21, 1, CardClass.Machine));
        cards.add(new Card("Tank", 3, 2, 3, Spell.SPELL2, 2, CardClass.Machine));
        cards.add(new Card("Jumper", 3, 4, 3, Spell.SPELL6, 3, CardClass.Machine));
        cards.add(new Card("Bot", 3, 3, 5, Spell.SPELL19, 4, CardClass.Machine));
        cards.add(new Card("Amalgam", 3, 6, 9, Spell.SPELL16, 5, CardClass.Machine));
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card getRandomLvlCard(int maxLvl) {
        List<Card> cards = this.cards.stream().filter(c->c.getLvl()<=maxLvl).toList();
        // FIXME: 12.07.2022
        // random
        return cards.get(new Random().nextInt(cards.size())).clone();
    }

}
