package ru.tinkoff.cardgame.game.services;

import org.springframework.stereotype.Service;
import ru.tinkoff.cardgame.game.model.card.Card;
import ru.tinkoff.cardgame.game.model.gamelogic.Player;

import java.util.Comparator;
import java.util.Optional;

@Service
public class CardTripletService {

    private static final int CARD_REQUIRED_TRIPLET_COUNT = 2;
    private static final int INCREASE_CARD_NUMBER = 2;

    public void checkPlayerCardForTriplet(Player player, Card card) {
        int inventoryCardForTriplet = (int) player.getInvCards().stream().
                filter(c -> c.getName().equals(card.getName()) && !c.isTriplet()).count();
        int activeCardsForTriplet = (int) player.getActiveCards().stream()
                .filter(c -> c.getName().equals(card.getName()) && !c.isTriplet()).count();
        if (inventoryCardForTriplet + activeCardsForTriplet == CARD_REQUIRED_TRIPLET_COUNT) {
            updateStatsCard(player, card);
            player.getActiveCards().removeIf(c -> c.getName().equals(card.getName()) && !c.isTriplet());
            player.getInvCards().removeIf(c -> c.getName().equals(card.getName()) && !c.isTriplet());
        }
    }

    private void updateStatsCard(Player player, Card card) {
        card.setTriplet(true);
        Optional<Card> currentCard = player.getActiveCards().stream()
                .filter(c -> c.getName().equals(card.getName()) && !c.isTriplet())
                .max(Comparator.comparingInt(c -> c.getDamage() + c.getHp()));
        if (currentCard.isPresent()) {
            card.setHp(currentCard.get().getHp() * INCREASE_CARD_NUMBER);
            card.setDamage(currentCard.get().getDamage() * INCREASE_CARD_NUMBER);
        } else {
            card.setHp(card.getHp() * INCREASE_CARD_NUMBER);
            card.setDamage(card.getDamage() * INCREASE_CARD_NUMBER);
        }

    }
}
