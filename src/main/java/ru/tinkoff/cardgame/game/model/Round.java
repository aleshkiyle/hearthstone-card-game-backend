package ru.tinkoff.cardgame.game.model;


import ru.tinkoff.cardgame.game.model.card.Card;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Round {
    private final Player firstPlayer;
    private final Player secondPlayer;
    private int attackIndex = 0;
    private int defenceIndex = 0;

    public Round(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }


    //
    //  11.07
    //
    public void startRound() throws IOException, ClassNotFoundException {
        List<Player> players = new ArrayList<>();
        players.add(firstPlayer);
        players.add(secondPlayer);
        Collections.shuffle(players);
        attack(players);
    }


    private int getRandom(int max) {
        int min = 0;
        return (int) ((Math.random() * ((max - min) + 1)) + min);
    }

    private List<Card> serialize(List<Card> cards) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);


        ous.writeObject(cards);
        ous.close();


        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        return new CopyOnWriteArrayList<>((List<Card>) ois.readObject());
    }

    private List<List<Card>> doMove(List<Card> cardsOfAttack, int attackIndexLocal, List<Card> cardsOfDefence, int defenceIndexLocal, boolean a1) {
        if (attackIndexLocal >= cardsOfAttack.size()) {
            attackIndexLocal = 0;
        }
        int index = getRandom(cardsOfDefence.size() - 1); //индекс атакуемой карты
        Card attackCard = cardsOfAttack.get(attackIndexLocal);  //карта наносящая урон
        Card attackedCard = cardsOfDefence.get(index); // карта получающая урон

        int hp = attackedCard.getHp();
        int damage = attackCard.getDamage();
        int hpA = attackCard.getHp();
        int damageD = attackedCard.getDamage();

        if (hp <= damage) { //если хп карты(например 1) <= чем урон ( например 2), то удаляем карту
            //TODO: Отправка на фронт события "уничтожение атакованной карты"
            cardsOfDefence.remove(index);
            if (index < defenceIndexLocal) {
                defenceIndexLocal--;
            }
        } else {
            //TODO: Отправка на фронт события "получение урона атакованной картой"

            attackedCard.setHp(hp - damage); //иначе наносим урон
            cardsOfDefence.set(index, attackedCard); // и заменяем старую карту, на новую с измененными характеристиками

        }
        if (hpA <= damageD) {
            //TODO: уничтожение атакующей карты
            cardsOfAttack.remove(attackIndexLocal);

        } else {
            //TODO: Отправка на фронт события "получение урона атакующей картой"
            attackCard.setHp(hpA - damageD);
            cardsOfAttack.set(attackIndexLocal, attackCard);
        }

        attackIndexLocal++;
        if (a1) {
            attackIndex = attackIndexLocal;
            defenceIndex = defenceIndexLocal;
        } else {
            defenceIndex = attackIndexLocal;
            attackIndex = defenceIndexLocal;
        }

        return new CopyOnWriteArrayList<>(Arrays.asList(cardsOfAttack, cardsOfDefence));
    }

    private void attackHero(List<Card> cardsOfAttack, Player playerA, Player playerD) {
        int damageForHero = 0;
        for (Card card : cardsOfAttack) {
            damageForHero += card.getLvl();
        }
        damageForHero += playerA.getShop().getLvl();// добавляем уровень магазина игрока Защиты к урону
        if (playerD.getHp() <= damageForHero) { //проверяем хватает ли урона на убийство
            //TODO: send to front death\lose
            playerD.setHp(0); // устанавливаем хп в 0
        } else {
            //TODO: send to front get damage
            playerD.setHp(playerD.getHp() - damageForHero); //наносим урон
        }
    }

    private void attack(List<Player> players) throws IOException, ClassNotFoundException {

        List<Card> cardsOfAttack = serialize(players.get(0).getActiveCards());
        List<Card> cardsOfDefence = serialize(players.get(1).getActiveCards());


        if (cardsOfAttack.size() == 0 && cardsOfDefence.size() != 0) {
            //Карты только у защиты(Атака защиты(1) по герою Атаки(0))
            attackHero(cardsOfDefence, players.get(1), players.get(0));

        } else if (cardsOfDefence.size() == 0 && cardsOfAttack.size() != 0) {
            //Карты только у атаки (Герой атаки(0) атакует героя защиты(1))
            attackHero(cardsOfAttack, players.get(0), players.get(1));
        } else {
            //TODO: У двоих есть карты
            while (cardsOfAttack.size() > 0 && cardsOfDefence.size() > 0) {

                List<List<Card>> cards;

                //Атака атаки
                cards = doMove(cardsOfAttack, attackIndex, cardsOfDefence, defenceIndex, true);
                cardsOfAttack = cards.get(0);
                cardsOfDefence = cards.get(1);

                if (cardsOfDefence.size() != 0 && cardsOfAttack.size() != 0) {
                    //Атака защиты
                    cards = doMove(cardsOfDefence, defenceIndex, cardsOfAttack, attackIndex, false);
                    cardsOfAttack = cards.get(1);
                    cardsOfDefence = cards.get(0);
                }

                if (cardsOfDefence.size() == 0 && cardsOfAttack.size() != 0) {

                    attackHero(cardsOfAttack, players.get(0), players.get(1));

                    //TODO: End round
                    break;

                } else if (cardsOfAttack.size() == 0 && cardsOfDefence.size() != 0) {

                    attackHero(cardsOfDefence, players.get(1), players.get(0));

                    //TODO: End round
                    break;
                }

                if (cardsOfAttack.size() == 0 && cardsOfDefence.size() == 0) {
                    //TODO: Ничья
                    Game.logger.info("Ничья");
                    break;
                }

                if (attackIndex >= cardsOfAttack.size()) {
                    attackIndex = 0;
                }
                if (defenceIndex >= cardsOfDefence.size()) {
                    defenceIndex = 0;
                }
                //последняя карта(сброс цикла)

                //TODO: вернуть на фронт конец раунда

            }
        }
    }


    //
    //  11.07
    //


    @Override
    public String toString() {
        return "\nRound{" +
                "\n\tfirstPlayer = " + firstPlayer +
                ", \n\tsecondPlayer = " + secondPlayer +
                '}';
    }
}
