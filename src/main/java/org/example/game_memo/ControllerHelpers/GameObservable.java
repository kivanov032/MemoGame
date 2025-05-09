package org.example.game_memo.ControllerHelpers;

import org.example.game_memo.Containers.CardGameLogic;

public interface GameObservable {
    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);
    void removeAllObservers();
    void notifyObservers(EventType type, CardGameLogic.CardGameContainer gameData);
}
