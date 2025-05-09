package org.example.game_memo.ControllerHelpers;

import org.example.game_memo.Containers.CardGameLogic;

public interface GameObserver {
    void update(EventType type, CardGameLogic.CardGameContainer gameData);
}

