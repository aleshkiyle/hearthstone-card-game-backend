package ru.tinkoff.cardgame.game.model;

import java.io.IOException;
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
        try {
            game.startRound();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
