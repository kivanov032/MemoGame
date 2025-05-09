package org.example.game_memo.Containers;

import org.example.game_memo.CardFactories.CardFactory;
import org.example.game_memo.CardThings.CardGame;

import java.util.*;

public class CardFactoriesContainer {
    private static CardFactoriesContainer instance;

    private final List<CardFactory> cardFactories = new ArrayList<>(); // Массив фабрик для создания карт
    private int cardPairsCount = 10; // Дефолтное кол-во карточек (без пар)

    private CardFactoriesContainer() {}

    // Метод для получения экземпляра
    public static synchronized CardFactoriesContainer getInstance() {
        if (instance == null) {
            instance = new CardFactoriesContainer();
        }
        return instance;
    }

    // Добавить фабрику
    public void addFactory(CardFactory factory) {
        cardFactories.add(factory);
    }

    // Очистить все фабрики
    public void clearFactories() {
        cardFactories.clear();
    }

    // Получить игровые карты (согласно их кол-ву)
    public List<CardGame> getGameCards() {
        List<CardGame> gameCards = new ArrayList<>();
        if (cardFactories.isEmpty()) return gameCards;

        int pairsPerFactory = cardPairsCount / cardFactories.size();
        int remainder = cardPairsCount % cardFactories.size();

        for (int i = 0; i < cardFactories.size(); i++) {
            CardFactory factory = cardFactories.get(i);
            int pairsToCreate = pairsPerFactory + (i < remainder ? 1 : 0);

            List<CardGame> categoryCards = factory.createGameCards(pairsToCreate);
            gameCards.addAll(categoryCards);
        }

        Collections.shuffle(gameCards);
        return gameCards;
    }

    public void setCountCard(int cardPairsCount) {
        this.cardPairsCount = cardPairsCount;
    }
}

