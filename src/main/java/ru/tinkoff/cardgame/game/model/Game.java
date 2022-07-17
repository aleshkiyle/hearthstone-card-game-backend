package ru.tinkoff.cardgame.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.cardgame.game.model.card.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    public static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final CopyOnWriteArrayList<Player> players;
    private int roundNumber = 0;
    private final CopyOnWriteArrayList<Round> rounds;
    private static CardList card1 = new CardList();
    private static CardList card2 = new CardList();
    private static CardList card3 = new CardList();
    private static CardList card4 = new CardList();
    private static Map<Player, CardList> cardListMap;

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        CopyOnWriteArrayList<Player> playersList = new CopyOnWriteArrayList<>();
        playersList.add(new Player(1));
        playersList.add(new Player(2));
        playersList.add(new Player(3));
        playersList.add(new Player(4));
        CopyOnWriteArrayList<Card> cards = new CopyOnWriteArrayList<>();
        cards.add(new Card("Ингрид Ильва", 3, "Белая ведьма, способная овладеть магией.", 2, 1, Spell.SPELL1, 1, CardClass.Witch));
        cards.add(new Card("Лаума", 3, "Ведьма, управляет силами природы, ночами душит спящих, насылает кошмары, подменяет детей.", 2, 1, Spell.SPELL4, 2, CardClass.Witch));
        cards.add(new Card("Геката", 3, "Королева ведьм, олицетворяющая исключительно силы мрака.", 3, 5, Spell.SPELL10, 3, CardClass.Witch));

        card1.add(new Card("Ингрид Ильва", 3, "Белая ведьма, способная овладеть магией.", 6, 1, Spell.SPELL1, 1, CardClass.Witch));
        card1.add(new Card("Лаума", 3, "Ведьма, управляет силами природы, ночами душит спящих, насылает кошмары, подменяет детей.", 2, 8, Spell.SPELL4, 2, CardClass.Witch));
        card1.add(new Card("Геката", 3, "Королева ведьм, олицетворяющая исключительно силы мрака.", 3, 5, Spell.SPELL10, 3, CardClass.Witch));

        card2.add(new Card("Ингрид Ильва", 3, "Белая ведьма, способная овладеть магией.", 1, 5, Spell.SPELL1, 1, CardClass.Witch));
        card2.add(new Card("Лаума", 3, "Ведьма, управляет силами природы, ночами душит спящих, насылает кошмары, подменяет детей.", 2, 4, Spell.SPELL4, 2, CardClass.Witch));
        card2.add(new Card("Геката", 3, "Королева ведьм, олицетворяющая исключительно силы мрака.", 3, 5, Spell.SPELL10, 3, CardClass.Witch));

        card3.add(new Card("Ингрид Ильва", 3, "Белая ведьма, способная овладеть магией.", 8, 6, Spell.SPELL1, 1, CardClass.Witch));
        card3.add(new Card("Лаума", 3, "Ведьма, управляет силами природы, ночами душит спящих, насылает кошмары, подменяет детей.", 6, 4, Spell.SPELL4, 2, CardClass.Witch));
        card3.add(new Card("Геката", 3, "Королева ведьм, олицетворяющая исключительно силы мрака.", 3, 5, Spell.SPELL10, 3, CardClass.Witch));

        card4.add(new Card("Ингрид Ильва", 3, "Белая ведьма, способная овладеть магией.", 1, 1, Spell.SPELL1, 1, CardClass.Witch));
        card4.add(new Card("Лаума", 3, "Ведьма, управляет силами природы, ночами душит спящих, насылает кошмары, подменяет детей.", 2, 4, Spell.SPELL4, 2, CardClass.Witch));
        card4.add(new Card("Геката", 3, "Королева ведьм, олицетворяющая исключительно силы мрака.", 3, 5, Spell.SPELL10, 3, CardClass.Witch));


        playersList.get(0).setActiveCards(card1);
        playersList.get(1).setActiveCards(card2);
        playersList.get(2).setActiveCards(card3);
        playersList.get(3).setActiveCards(card4);
//


        new Game(playersList).startGame();
    }




    public Game(CopyOnWriteArrayList<Player> players) {
        this.players = players;
        this.rounds = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<Player> getPlayers() {
        return players;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void startGame() {
        logger.info("START GAME");
        players.forEach(p -> p.getShop().updateShop());
        startTimer();

    }

    public void startTimer() {
        logger.info("START TIMER");
        new Timer(this,1).run();
    }

    public void startRound() throws IOException, ClassNotFoundException {
        generateRounds();
        logger.info("START ROUND №" + this.roundNumber);
        this.roundNumber++;
        logger.info(this.rounds.toString());
        // TODO: 09.07.2022
        // round controller




       this.rounds.get(0).startRound();
       this.rounds.get(1).startRound();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        finishRound();

    }
    private static final Comparator<Player> PLAYER_COMPARATOR_BY_HP = new Comparator<Player>() {
        @Override
        public int compare(Player o1, Player o2) {
            return o1.getHp()-o2.getHp();
        }
    };
    public void generateRounds() throws IOException, ClassNotFoundException{
        this.rounds.clear();
        List<Player> playerList = new CopyOnWriteArrayList<>(this.players);
        //logger.info("orig: " + this.players);
        //Collections.shuffle(playerList);
        Collections.sort(playerList, PLAYER_COMPARATOR_BY_HP);
        logger.info("copy: " + playerList);
        for (int i = 0; i < playerList.size(); i+=2) {
            this.rounds.add(new Round(playerList.get(i), playerList.get(i + 1)));
        }
    }

    public void finishRound() {
        logger.info("FINISH ROUND №" + this.roundNumber);
        this.roundNumber++;
        // TODO: 09.07.2022
        // now for test work
//        this.players.forEach(p -> p.setHp(p.getHp() - 40));
//        this.players.get(0).setHp(100);
        if (isGameEnd()) {
            finishGame();
        } else {
            startTimer();
        }
    }

    public boolean isGameEnd() {
        return this.players.stream().filter(p -> p.getHp() > 0).count() == 1;
    }

    public void finishGame() {
        logger.info("GAME OVER");
        logger.info("WINNER: " + this.players.stream().filter(p -> p.getHp() > 0).findFirst());
        logger.info("-------------------");
        logger.info("OTHERS: "+this.players);

    }

    @Override
    public String toString() {
        return "Game{" +
                "players=" + players +
                ", roundNumber=" + roundNumber +
                ", rounds=" + rounds +
                '}';
    }
}
