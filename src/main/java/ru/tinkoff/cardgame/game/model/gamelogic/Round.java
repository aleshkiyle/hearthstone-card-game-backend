package ru.tinkoff.cardgame.game.model.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.cardgame.game.Notificator;
import ru.tinkoff.cardgame.game.websocketmessages.RoundMessage;
import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.card.Spell;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Round implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Round.class);
    private static final long ATTACK_TIME = 1;
    private final Notificator notificator;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private int attackIndex = 0;
    private int defenceIndex = 0;
    private boolean isFirstPlayerFirstAttack;

    public Round(Notificator notificator, Player firstPlayer, Player secondPlayer) {
        this.notificator = notificator;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
    }

    @Override
    public void run() {
        try {
            notifyFront();
            startRound();
            //Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startRound() throws IOException, ClassNotFoundException, InterruptedException {
        SpellController.spell22Realisation(firstPlayer.getActiveCards());
        SpellController.spell22Realisation(secondPlayer.getActiveCards());
        SpellController.spell10Realisation(firstPlayer, firstPlayer.getActiveCards());
        SpellController.spell10Realisation(secondPlayer, secondPlayer.getActiveCards());
        SpellController.spell16Realisation(firstPlayer.getActiveCards());
        SpellController.spell16Realisation(secondPlayer.getActiveCards());
        List<Player> players = new ArrayList<>();
        players.add(firstPlayer);
        players.add(secondPlayer);
        Collections.shuffle(players);
        this.isFirstPlayerFirstAttack = players.get(0).equals(firstPlayer);
        attack(players);
    }

    private void notifyFront() {
        RoundMessage roundMessage = new RoundMessage(firstPlayer, secondPlayer, firstPlayer.getActiveCards(), secondPlayer.getActiveCards());
        notificator.notifyRoundStart(firstPlayer.getId(), roundMessage);
        roundMessage = new RoundMessage(secondPlayer, firstPlayer, secondPlayer.getActiveCards(), firstPlayer.getActiveCards());
        notificator.notifyRoundStart(secondPlayer.getId(), roundMessage);
    }

    private void notifyUpdateFront(List<Card> firstPlayerCard, List<Card> secondPlayerCard) {
        RoundMessage roundMessage = new RoundMessage(firstPlayer, secondPlayer, firstPlayerCard, secondPlayerCard);
        notificator.notifyRoundStart(firstPlayer.getId(), roundMessage);
        roundMessage = new RoundMessage(secondPlayer, firstPlayer, secondPlayerCard, firstPlayerCard);
        notificator.notifyRoundStart(secondPlayer.getId(), roundMessage);
    }

    private void notifyAttackFront(List<Card> firstPlayerCard, List<Card> secondPlayerCard, int attackIndex, int defenceIndex, boolean isAttackMove) {
        boolean isFirstPlayerAttack = isAttackMove == this.isFirstPlayerFirstAttack;
        int firstPlayerCardIndex = isFirstPlayerAttack ? attackIndex : defenceIndex;
        int secondPlayerCardIndex = isFirstPlayerAttack ? defenceIndex : attackIndex;

        RoundMessage roundMessage = new RoundMessage(firstPlayer, secondPlayer, firstPlayerCard, secondPlayerCard, firstPlayerCardIndex, secondPlayerCardIndex, isFirstPlayerAttack);
        notificator.notifyRoundUpdate(firstPlayer.getId(), roundMessage);

        roundMessage = new RoundMessage(secondPlayer, firstPlayer, secondPlayerCard, firstPlayerCard, secondPlayerCardIndex, firstPlayerCardIndex, !isFirstPlayerAttack);
        notificator.notifyRoundUpdate(secondPlayer.getId(), roundMessage);
    }


    private int getRandom(int max) {
        int min = 0;
        return (int) ((Math.random() * ((max - min) + 1)) + min);
    }

    private List<Card> cloneCards(List<Card> cards) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream(baos);
        ous.writeObject(cards);
        ous.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        return new CopyOnWriteArrayList<>((List<Card>) ois.readObject());
    }

    private void activateSpell(Spell spell, List<Card> cards, Player player, Card card) {
        SpellController.spell7Realisation(cards, card);
        switch (spell) {
            case SPELL6 -> SpellController.spell6Realisation(cards);
            case SPELL11 -> SpellController.spell11Realisation(cards);
            case SPELL14 -> SpellController.spell14Realisation(cards);
            case SPELL17 -> SpellController.spell2Realisation(player);
            case SPELL21 -> SpellController.spell21Realisation(cards);
        }
    }

    private List<List<Card>> doMove(List<Card> cardsOfAttack, int attackIndexLocal, List<Card> cardsOfDefence, int defenceIndexLocal, boolean a1, Player attacker, Player defender) throws InterruptedException {
        if (attackIndexLocal >= cardsOfAttack.size()) {
            attackIndexLocal = 0;
        }
        int index = getRandom(cardsOfDefence.size() - 1); //индекс атакуемой карты
        //notifyUpdateFront()
        Card attackCard = cardsOfAttack.get(attackIndexLocal);  //карта наносящая урон
        Card attackedCard = cardsOfDefence.get(index); // карта получающая урон

        Spell spellAttackCard = attackCard.getSpell();
        Spell spellDefenceCard = attackedCard.getSpell();

        int hp = attackedCard.getHp();
        int damage = attackCard.getDamage();
        int hpA = attackCard.getHp();
        int damageD = attackedCard.getDamage();

        switch (spellAttackCard) {
            case SPELL5 -> damage += 1;
            case SPELL15 -> damage *= 2;
            case SPELL18 -> damage += 2;
        }

        if (this.isFirstPlayerFirstAttack == a1) {
            notifyAttackFront(cardsOfAttack, cardsOfDefence, attackIndexLocal, index, a1);
        } else {
            notifyAttackFront(cardsOfDefence, cardsOfAttack, index, attackIndexLocal, a1);
        }
        Thread.sleep(TimeUnit.SECONDS.toMillis(ATTACK_TIME));

        if (hp <= damage) { //если хп карты(например 1) <= чем урон ( например 2), то удаляем карту
            //TODO: Отправка на фронт события "уничтожение атакованной карты"
            cardsOfDefence.remove(index);
            activateSpell(spellDefenceCard, cardsOfDefence, defender, attackedCard);
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
            activateSpell(spellAttackCard, cardsOfAttack, attacker, attackCard);
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

        return new LinkedList<>(Arrays.asList(cardsOfAttack, cardsOfDefence));
    }

    private void attackHero(List<Card> cardsOfAttack, Player playerA, Player playerD) {
        int damageForHero = 0;
        for (Card card : cardsOfAttack) {
            damageForHero += card.getLvl();
        }
        damageForHero += playerA.getShop().getLevel();// добавляем уровень магазина игрока Защиты к урону
        if (playerD.getHp() <= damageForHero) { //проверяем хватает ли урона на убийство
            //TODO: send to front death\lose
            playerD.setHp(0); // устанавливаем хп в 0
        } else {
            //TODO: send to front get damage
            playerD.setHp(playerD.getHp() - damageForHero); //наносим урон
        }
    }

    private void attack(List<Player> players) throws IOException, ClassNotFoundException, InterruptedException {
        List<Card> cardsOfAttack = cloneCards(players.get(0).getActiveCards());
        List<Card> cardsOfDefence = cloneCards(players.get(1).getActiveCards());

        if (cardsOfAttack.size() == 0 && cardsOfDefence.size() != 0) {
            if (this.isFirstPlayerFirstAttack) {
                notifyUpdateFront(cardsOfAttack, cardsOfDefence);
            } else {
                notifyUpdateFront(cardsOfDefence, cardsOfAttack);
            }
            //Карты только у защиты(Атака защиты(1) по герою Атаки(0))
            attackHero(cardsOfDefence, players.get(1), players.get(0));
        } else if (cardsOfDefence.size() == 0 && cardsOfAttack.size() != 0) {
            if (this.isFirstPlayerFirstAttack) {
                notifyUpdateFront(cardsOfAttack, cardsOfDefence);
            } else {
                notifyUpdateFront(cardsOfDefence, cardsOfAttack);
            }
            //Карты только у атаки (Герой атаки(0) атакует героя защиты(1))
            attackHero(cardsOfAttack, players.get(0), players.get(1));
        } else {
            //TODO: У двоих есть карты
            while (cardsOfAttack.size() > 0 && cardsOfDefence.size() > 0) {

                if (this.isFirstPlayerFirstAttack) {
                    notifyUpdateFront(cardsOfAttack, cardsOfDefence);
                } else {
                    notifyUpdateFront(cardsOfDefence, cardsOfAttack);
                }
                Thread.sleep(TimeUnit.SECONDS.toMillis(ATTACK_TIME));

                List<List<Card>> cards;
                //Атака атаки
                cards = doMove(cardsOfAttack, attackIndex, cardsOfDefence, defenceIndex, true, players.get(0), players.get(1));
                cardsOfAttack = cards.get(0);
                cardsOfDefence = cards.get(1);

                if (cardsOfDefence.size() != 0 && cardsOfAttack.size() != 0) {

                    if (this.isFirstPlayerFirstAttack) {
                        notifyUpdateFront(cardsOfAttack, cardsOfDefence);
                    } else {
                        notifyUpdateFront(cardsOfDefence, cardsOfAttack);
                    }
                    Thread.sleep(TimeUnit.SECONDS.toMillis(ATTACK_TIME));


                    //Атака защиты
                    cards = doMove(cardsOfDefence, defenceIndex, cardsOfAttack, attackIndex, false, players.get(0), players.get(1));
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
                    //logger.info("Ничья");
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
                if (this.isFirstPlayerFirstAttack) {
                    notifyUpdateFront(cardsOfAttack, cardsOfDefence);
                } else {
                    notifyUpdateFront(cardsOfDefence, cardsOfAttack);
                }
                Thread.sleep(TimeUnit.SECONDS.toMillis(ATTACK_TIME));

            }

            if (this.isFirstPlayerFirstAttack) {
                notifyUpdateFront(cardsOfAttack, cardsOfDefence);
            } else {
                notifyUpdateFront(cardsOfDefence, cardsOfAttack);
            }
            Thread.sleep(TimeUnit.SECONDS.toMillis(ATTACK_TIME));
        }

    }

    @Override
    public String toString() {
        return "\nRound{" +
                "\n\tfirstPlayer = " + firstPlayer +
                ", \n\tsecondPlayer = " + secondPlayer +
                '}';
    }

}
