package ru.tinkoff.cardgame.game.model;

public class Card {
    private int id;
    private String name;
    private int price;
    private String description;
    private int damage;
    private int hp;
    private Spell spell;
    private int lvl;
    private CardClass cardClass;

    public Card(String name, int price, String description, int damage, int hp, Spell spell, int lvl, CardClass cardClass) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.damage = damage;
        this.hp = hp;
        this.spell = spell;
        this.lvl = lvl;
        this.cardClass = cardClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Spell getSpell() {
        return spell;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public CardClass getCardClass() {
        return cardClass;
    }

    public void setCardClass(CardClass cardClass) {
        this.cardClass = cardClass;
    }
}
