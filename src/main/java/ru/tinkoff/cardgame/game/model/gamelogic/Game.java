package ru.tinkoff.cardgame.game.model.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import ru.tinkoff.cardgame.game.model.Notificator;
import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.card.CardProvider;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

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
//        players.forEach(p -> {
//            CopyOnWriteArrayList<Card> testCards = new CopyOnWriteArrayList<>();
//            for (int i = 0; i < 7; i++) {
//                testCards.add(CardProvider.INSTANCE.getRandomLvlCard(p.getShop().getLevel()));
//            }
//            p.setInvCards(testCards);
//            testCards.clear();
//            for (int i = 0; i < 7; i++) {
//                testCards.add(CardProvider.INSTANCE.getRandomLvlCard(p.getShop().getLevel()));
//            }
//            p.setActiveCards(testCards);
//        });
//        //

        startTimer();
    }

    public Player findPlayer(String playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .get();
    }

    public void startTimer() {
        logger.info("START TIMER");
        this.players.forEach(p -> notificator.notifyShopStart(p.getId(), p));
        new Thread(new Timer(this, 15)).start();
    }

    public void startRound() {
        generateRounds();
        rounds.forEach(r-> new Thread(r).start());
        logger.info("START ROUND №" + this.roundNumber);
        logger.info(this.rounds.toString());
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // TODO: 17.07.2022
        // sync
        finishRound();
    }

    //    private static final Comparator<Player> PLAYER_COMPARATOR_BY_HP = new Comparator<Player>() {
//        @Override
//        public int compare(Player o1, Player o2) {
//            return o1.getHp()-o2.getHp();
//        }
//    };
    public void generateRounds() {
        this.rounds.clear();
        List<Player> playerList = new CopyOnWriteArrayList<>(this.players);
        //logger.info("orig: " + this.players);
        //Collections.shuffle(playerList);
        // Collections.sort(playerList, PLAYER_COMPARATOR_BY_HP);
        playerList.sort(Comparator.comparingInt(Player::getHp));
        logger.info("copy: " + playerList);
        for (int i = 0; i < playerList.size(); i += 2) {
            this.rounds.add(new Round(notificator, playerList.get(i), playerList.get(i + 1)));
        }
    }


    public void finishRound() {
        logger.info("FINISH ROUND №" + this.roundNumber);
        this.roundNumber++;
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