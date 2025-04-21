package org.example.game_memo;

public interface GameObserver {
    void update(EventType type, CardGame card);
}

// Варианты игровых событий
enum EventType {
    CARD_FLIPPED, PAIR_CARD_MATCHED, PAIR_CARD_MISMATCHED, GAME_OVER
}
