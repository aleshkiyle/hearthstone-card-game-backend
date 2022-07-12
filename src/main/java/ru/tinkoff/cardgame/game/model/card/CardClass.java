package ru.tinkoff.cardgame.game.model.card;

public enum CardClass {

    Witch("Ведьма"),
    Dragon("Дракон"),
    Demon("Демон"),
    Giant("Великан"),
    Machine("Машина");

    private final String title;

    CardClass(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
