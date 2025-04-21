package org.example.game_memo;

import java.util.*;
import java.util.function.Function;

public class CardPairCreator {
    public List<CardGame> createPairs(List<CardProxy> proxies, int pairsCount, Function<CardProxy, CardGame> cardCreator) {
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

    private List<CardProxy> selectRandomProxies(List<CardProxy> proxies, int count) {
        if (count > proxies.size()) {
            throw new IllegalArgumentException(
                    String.format("Запрошено %d карт, но доступно только %d", count, proxies.size())
            );
        }
        List<CardProxy> shuffled = new ArrayList<>(proxies);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }
}
