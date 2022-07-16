package ru.tinkoff.cardgame.game.model.gamelogic;

import java.util.concurrent.TimeUnit;

public class Timer implements Runnable {

    private final Game game;
    private final int time;

    public Timer(Game game, int time) {
        this.game = game;
        this.time = time;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(time));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        game.startRound();
    }
}
