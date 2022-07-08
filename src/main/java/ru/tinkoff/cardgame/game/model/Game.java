package ru.tinkoff.cardgame.game.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Player> playersList = new CopyOnWriteArrayList<>();
        playersList.add(new Player(1));
        playersList.add(new Player(2));
        playersList.add(new Player(3));
        playersList.add(new Player(4));
        new Game(playersList).startGame();
    }

    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final CopyOnWriteArrayList<Player> players;
    private int roundNumber = 0;
    private final CopyOnWriteArrayList<Round> rounds;


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
        new Timer(this,4).run();
    }

    public void startRound() {
        generateRounds();
        logger.info("START ROUND №" + this.roundNumber);
        logger.info(this.rounds.toString());
        // TODO: 09.07.2022
        // round controller
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        finishRound();

    }

    public void generateRounds() {
        this.rounds.clear();
        List<Player> playerList = new LinkedList<>(this.players);
        logger.info("orig: " + this.players);
        Collections.shuffle(playerList);
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
        this.players.forEach(p -> p.setHp(p.getHp() - 40));
        this.players.get(0).setHp(100);
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
