package ru.tinkoff.cardgame.game.model.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.tinkoff.cardgame.game.model.Notificator;
import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.card.CardProvider;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private final Notificator notificator;

    private final String id;
    private final List<Player> players;
    private int roundNumber = 0;
    private final CopyOnWriteArrayList<Round> rounds;

    public Game(String id, List<Player> players, Notificator notificator) {
        this.id = id;
        this.players = players;
        this.rounds = new CopyOnWriteArrayList<>();
        this.notificator = notificator;
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
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

    @SendTo()
    public void startGame() {
        logger.info("START GAME");
        players.forEach(p -> p.getShop().updateShop());

        // test values for front
        players.forEach(p -> {
            CopyOnWriteArrayList<Card> testCards = new CopyOnWriteArrayList<>();
            for (int i = 0; i < 7; i++) {
                testCards.add(CardProvider.INSTANCE.getRandomLvlCard(p.getShop().getLevel()));
            }
            p.setInvCards(testCards);
            testCards.clear();
            for (int i = 0; i < 7; i++) {
                testCards.add(CardProvider.INSTANCE.getRandomLvlCard(p.getShop().getLevel()));
            }
            p.setActiveCards(testCards);
        });
        //

        startTimer();
        this.players.forEach(p -> notificator.notifyShop(p.getId(), p));

    }

    public Player findPlayer(String playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .get();
    }

    public void startTimer() {
        logger.info("START TIMER");
        new Thread(new Timer(this, 4)).start();
    }

    public void startRound() {
        generateRounds();
        this.rounds.forEach(Round::test);
        logger.info("START ROUND №" + this.roundNumber);
        logger.info(this.rounds.toString());
        // TODO: 09.07.2022
        // round controller
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //finishRound();
    }

    public void generateRounds() {
        // TODO: 16.07.2022
        this.rounds.clear();
        List<Player> playerList = new LinkedList<>(this.players);
        Collections.shuffle(playerList);
        for (int i = 0; i < playerList.size(); i += 2) {
            this.rounds.add(new Round(playerList.get(i), playerList.get(i + 1), notificator));
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
