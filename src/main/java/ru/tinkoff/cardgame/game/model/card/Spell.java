package ru.tinkoff.cardgame.game.model.card;

public enum Spell {
    ADDHP1("\bЧародейство","Получаете +2 к атаке до следующего хода");
    private String type;
    private String description;

    Spell(String type, String description){
        this.type = type;
        this.description = description;
    }
    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
