package org.example.game_memo;

import java.util.List;

public interface ICardFactory {
    List<CardGame> createCardPairs(int pairsCount);
    String getCategoryName();
}
