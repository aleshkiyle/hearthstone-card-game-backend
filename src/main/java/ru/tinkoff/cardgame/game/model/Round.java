package ru.tinkoff.cardgame.game.model;


import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Round {
    private final Player firstPlayer;
    private final Player secondPlayer;


    public Round(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }


    //
    //  11.07
    //
    public void startRound(){
        List<Player> players = new ArrayList<>();
        players.add(firstPlayer);
        players.add(secondPlayer);
        Collections.shuffle(players);
        Attack(players);
        //TODO: Смена сторон
        Collections.reverse(players);

        Attack(players);

    }



    private int getRandom(int max){
        int min = 0;
        return (int)((Math.random() * ((max - min) + 1)) + min);
    }

    private int getCountMoves(List<Card> c1, List<Card> c2 ){
        return (c1.size()+c2.size());
    }


    private List<Card> damageCounter(Card cardA, Card cardD ){
        List<Card> listCard = new CopyOnWriteArrayList<>();
        int hp = cardD.getHp();
        int damage = cardA.getDamage();


        listCard.add(cardA);
        listCard.add(cardD);
        return listCard;
    }

    private void Attack(List<Player> players){

        List<Card> cardsOfAttack = players.get(0).getActiveCards();
        List<Card> cardsOfDefence = players.get(1).getActiveCards();

        int countMoves = getCountMoves(cardsOfAttack, cardsOfDefence);
        int movesMade = 0;
        int playedCards = 0;


        if (cardsOfAttack.size()==0){

            //Карты только у защиты(Атака защиты(1) по герою Атаки(0))

            int damageForHero = 0;
            for (Card card : cardsOfDefence) {
                damageForHero += card.getLvl();
            }
            damageForHero += players.get(1).getShop().getLvl();// добавляем уровень магазина игрока Защиты к урону
            if (players.get(0).getHp() <= damageForHero){ //проверяем хватает ли урона на убийство
                //TODO: send to front death\lose
                players.get(0).setHp(0); // устанавливаем хп в 0
            }
            else{
                //TODO: send to front get damage
                players.get(0).setHp(players.get(0).getHp() - damageForHero); //наносим урон
            }

        }else if(cardsOfDefence.size()==0){

            //Карты только у атаки (Герой атаки(0) атакует героя защиты(1))

            int damageForHero = 0;
            for (Card card : cardsOfAttack) {
                damageForHero += card.getLvl();
            }
            damageForHero += players.get(0).getShop().getLvl();// добавляем уровень магазина игрока Защиты к урону
            if (players.get(1).getHp() <= damageForHero){ //проверяем хватает ли урона на убийство
                //TODO: send to front death\lose
                players.get(1).setHp(0); // устанавливаем хп в 0
            }
            else{
                //TODO: send to front get damage
                players.get(1).setHp(players.get(1).getHp() - damageForHero); //наносим урон
            }




        }else{
            //TODO: У двоих есть карты

            int attackIndex = 0;
            int defenceIndex = 0;

            int countOfAllCards = getCountMoves(cardsOfAttack, cardsOfDefence);
            while(playedCards<=countOfAllCards){

                //Атака атаки

                int index = getRandom(cardsOfDefence.size()-1); //индекс атакуемой карты

                Card attackCard = cardsOfAttack.get(attackIndex);  //карта наносящая урон
                Card attackedCard = cardsOfDefence.get(index); // карта получающая урон

                int hp = attackedCard.getHp();
                int damage = attackCard.getDamage();

                if (hp <= damage){ //если хп карты(например 1) <= чем урон ( например 2), то удаляем карту
                    //TODO: Отправка на фронт события "уничтожение карты"
                    playedCards+=2;
                    cardsOfDefence.remove(index);


                }else{
                    //TODO: Отправка на фронт события "получение урона"
                    attackedCard.setHp(hp-damage); //иначе наносим урон
                    cardsOfDefence.set(index, attackedCard); // и заменяем старую карту, на новую с измененными характеристиками
                    playedCards++;
                }

                attackIndex++;

                //Атака защиты

                index = getRandom(cardsOfAttack.size()-1); //индекс атакуемой карты

                attackCard = cardsOfDefence.get(defenceIndex);  //карта наносящая урон
                attackedCard = cardsOfAttack.get(index); // карта получающая урон

                hp = attackedCard.getHp();
                damage = attackCard.getDamage();

                if (hp <= damage){ //если хп карты(например 1) <= чем урон ( например 2), то удаляем карту
                    //TODO: Отправка на фронт события "уничтожение карты"
                    playedCards+=2;
                    cardsOfAttack.remove(index);


                }else{
                    //TODO: Отправка на фронт события "получение урона"
                    attackedCard.setHp(hp-damage); //иначе наносим урон
                    cardsOfAttack.set(index, attackedCard); // и заменяем старую карту, на новую с измененными характеристиками
                    playedCards++;
                }

                defenceIndex++;




            }
        }

        //TODO: вернуть на фронт конец раунда

    }






    //
    //  11.07
    //


    @Override
    public String toString() {
        return "Round{" +
                "firstPlayer=" + firstPlayer +
                ", secondPlayer=" + secondPlayer +
                '}';
    }
}
