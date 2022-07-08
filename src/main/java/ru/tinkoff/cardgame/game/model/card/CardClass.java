package ru.tinkoff.cardgame.game.model.card;

public enum CardClass {

    //Beast("Зверь"),
    //Demon("Демон"),
    Dragon("Дракон"),
    //Elemental("Элементаль"),
    Mech ("Механизм");
    //Murloc ("Мурлок"),
    //Naga ("Нага"),
    //Pirate ("Пират"),
    //Quilboar ("Свинобраз");

    private final String title;

    CardClass(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
