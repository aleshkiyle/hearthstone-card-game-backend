package ru.tinkoff.cardgame.game.model.card;

import lombok.Data;

import java.io.Serializable;

@Data
public class Card implements Cloneable, Serializable {

    private String name;
    private int price;
    private int damage;
    private int hp;
    private Spell spell;
    private int lvl;
    private CardClass cardClass;
    private boolean isTriplet = false;

    public Card(String name, int price, int damage, int hp, Spell spell, int lvl, CardClass cardClass) {
        this.name = name;
        this.price = price;
        this.damage = damage;
        this.hp = hp;
        this.spell = spell;
        this.lvl = lvl;
        this.cardClass = cardClass;
    }

    @Override
    public Card clone() {
        try {
            Card clone = (Card) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
