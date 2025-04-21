package org.example.game_memo;

import java.util.*;

public class CardContainer {
    private static final CardContainer instance = new CardContainer();
    private final List<CardFactory> cardFactories = new ArrayList<>();
    private int cardPairsCount = 10;

    private CardContainer() {}

    public static CardContainer getInstance() {
        return instance;
    }

    public void addFactory(CardFactory factory) {
        cardFactories.add(factory);
    }

    public void clearFactories() {
        cardFactories.clear();
    }

    public List<CardGame> prepareGameCards() {
        List<CardGame> gameCards = new ArrayList<>();

        if (cardFactories.isEmpty()) return gameCards;

        int pairsPerFactory = cardPairsCount / cardFactories.size();
        int remainder = cardPairsCount % cardFactories.size();

        for (int i = 0; i < cardFactories.size(); i++) {
            CardFactory factory = cardFactories.get(i);
            int pairsToCreate = pairsPerFactory + (i < remainder ? 1 : 0);

            List<CardGame> categoryCards = factory.createCardPairs(pairsToCreate);
            gameCards.addAll(categoryCards);
        }

        Collections.shuffle(gameCards);
        return gameCards;
    }

    // Геттеры/сеттеры
    public int getCountCard() {
        return cardPairsCount;
    }

    public void setCountCard(int cardPairsCount) {
        this.cardPairsCount = cardPairsCount;
    }
}

