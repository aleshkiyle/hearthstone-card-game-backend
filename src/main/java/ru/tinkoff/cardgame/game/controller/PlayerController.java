package ru.tinkoff.cardgame.game.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.cardgame.game.services.PlayerService;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public void getCards() {
        // playerService.buyCardFromShop(new Player(), 1);
    }
}
