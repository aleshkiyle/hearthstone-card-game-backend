package ru.tinkoff.cardgame.game.model;


import ru.tinkoff.cardgame.game.model.card.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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


    private void Attack(List<Player> players){


        List<Card> cardsOfAttack = players.get(0).getActiveCards();
        List<Card> cardsOfDefence = players.get(1).getActiveCards();



        int damageForHero = 0;

        for (int i = 0; i < cardsOfAttack.size(); i++) {
                if(cardsOfDefence.size()==0){ //если карт у защиты 0, то подсчитаем урон получаемый героем игрока защиты

                        damageForHero += cardsOfAttack.get(i).getLvl();//если карт изначально было ноль суммируются все
                                                                        // если же карт не осталось в момент хода, то суммируется уровень только не атаковавших

                    if(++i == cardsOfAttack.size()){ // если атакующая карта последняя,
                        damageForHero += players.get(0).getShop().getLvl();// то добавляем уровень магазина игрока Атаки к урону
                        if (players.get(1).getHp() <= damageForHero){ //проверяем хватает ли урона на убийство
                            //TODO: send to front death\lose
                            players.get(1).setHp(0); // устанавливаем хп в 0
                        }
                        else{
                            //TODO: send to front get damage
                            players.get(1).setHp(players.get(1).getHp() - damageForHero); //наносим урон
                        }
                    }
                }


                int index = getRandom(cardsOfDefence.size()-1); //индекс атакуемой карты
                Card attackCard = cardsOfAttack.get(i);  //карта наносящая урон
                Card attackedCard = cardsOfDefence.get(index); // карта получающая урон
                int hp = attackedCard.getHp();
                int damage = attackCard.getDamage();
                if (hp <= damage){ //если хп карты(например 1) <= чем урон ( например 2), то удаляем карту
                    //TODO: Отправка на фронт события "уничтожение карты"
                    cardsOfDefence.remove(index);

                }else{
                    //TODO: Отправка на фронт события "получение урона"
                    attackedCard.setHp(hp-damage); //иначе наносим урон
                    cardsOfDefence.set(index, attackedCard); // и заменяем старую карту, на новую с измененными характеристиками
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
