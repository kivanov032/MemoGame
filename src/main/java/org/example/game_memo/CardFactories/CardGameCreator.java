package org.example.game_memo.CardFactories;

import org.example.game_memo.CardThings.CardGame;
import org.example.game_memo.CardThings.CardProxy;

import java.util.*;
import java.util.function.Function;

public class CardGameCreator {

    // Создание игровых карт
    public static List<CardGame> createGameCards(List<CardProxy> proxies, int pairsCount, Function<CardProxy, CardGame> cardCreator) {
        List<CardGame> pairs = new ArrayList<>();
        List<CardProxy> selected = selectRandomProxies(proxies, pairsCount);

        for (CardProxy proxy : selected) {
            CardGame card = cardCreator.apply(proxy);
            pairs.add(card);
            CardGame cardCopy = cardCreator.apply(proxy);
            pairs.add(cardCopy);
        }
        return pairs;
    }

    // Рандомный выбор прокси-карт для создания игровых карт
    private static List<CardProxy> selectRandomProxies(List<CardProxy> proxies, int count) {
        if (count > proxies.size()) {
            throw new IllegalArgumentException(
                    String.format("Запрошено %d карт, но доступно только %d", count, proxies.size())
            );
        }
        List<CardProxy> shuffled = new ArrayList<>(proxies);
        Collections.shuffle(shuffled); // Перемешивание карт
        return shuffled.subList(0, count);
    }
}
