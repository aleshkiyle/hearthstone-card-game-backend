package ru.tinkoff.cardgame.game.model.gamelogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import ru.tinkoff.cardgame.game.GameProvider;
import ru.tinkoff.cardgame.game.Notificator;
import ru.tinkoff.cardgame.game.websocketmessages.GameOverMessage;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {

    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private static final int MAX_PLAYER_GOLD = 10;
    private static final int ROUND_TIME = 60 ;

    private final Notificator notificator;

    private final String id;
    private final List<Player> players;
    private int roundNumber = 0;
    private final List<Thread> rounds;

    public Game(String id, List<Player> players, Notificator notificator) {
        this.id = id;
        this.players = players;
        this.rounds = new LinkedList<>();
        this.notificator = notificator;
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @SendTo()
    public void startGame() {
        logger.info("START GAME");
        players.forEach(p -> p.getShop().updateShop());
        startTimer();
    }

    public Player findPlayer(String playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .get();
    }

    private void startTimer() {
        logger.info("START TIMER");
        this.players.forEach(p -> notificator.notifyShopStart(p.getId(), p, this.players));
        new Thread(new Timer(this, ROUND_TIME)).start();
    }

    public void startFight() {
        generateRounds();
        rounds.forEach(Thread::start);
        logger.info("START ROUND №" + this.roundNumber);
        rounds.forEach(r -> {
            try {
                r.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        finishRound();
    }

    private void generateRounds() {
        this.rounds.clear();
        List<Player> playerList = new CopyOnWriteArrayList<>(this.players);
        playerList.sort(Comparator.comparingInt(Player::getHp));
        for (int i = 0; i < playerList.size(); i += 2) {
            this.rounds.add(new Thread(new Round(notificator, playerList.get(i), playerList.get(i + 1))));
        }
    }

    private void finishRound() {
        logger.info("FINISH ROUND №" + this.roundNumber);
        this.roundNumber++;
        players.stream().filter(p -> p.getHp() <= 0).forEach(this::kickPlayer);
        if (isGameEnd()) {
            finishGame();
        } else {
            startNewRound();
        }
    }

    private void startNewRound() {
        players.forEach(p -> {
            if (!p.getShop().isFreezeStatus()) {
                p.getShop().updateShop();
            }
        });
        players.forEach(p -> p.getShop().decreaseUpgradePrice());
        players.forEach(p -> {
            if (p.getMaxGold() < MAX_PLAYER_GOLD) {
                p.setMaxGold(p.getMaxGold() + 1);
            }
        });
        players.forEach(p -> {
            if (p.getShop().isFreezeStatus()) {
                p.getShop().setFreezeStatus(false);
            }
        });
        players.forEach(p -> p.setGold(p.getMaxGold()));
        startTimer();
    }

    private boolean isGameEnd() {
        return this.players.stream().filter(p -> p.getHp() > 0).count() <= 1;
    }

    private void kickPlayer(Player player) {
        player.setHp(0);
        GameOverMessage gameOverMessage = new GameOverMessage(player.getUsername(), false);
        notificator.notifyGameOver(player.getId(), gameOverMessage);

    }

    private void finishGame() {
        logger.info("GAME OVER");
        Optional<Player> winner = this.players.stream().filter(p -> p.getHp() > 0).findFirst();
        if (winner.isPresent()) {
            logger.info("WINNER: " + winner.get());
            GameOverMessage gameOverMessage = new GameOverMessage(winner.get().getUsername(), true);
            notificator.notifyGameOver(winner.get().getId(), gameOverMessage);
        }

        GameProvider.INSTANCE.getGames().remove(this);
    }

    public void stopGame() {
        players.forEach(this::kickPlayer);
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