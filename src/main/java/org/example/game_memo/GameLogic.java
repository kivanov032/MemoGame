package org.example.game_memo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogic {
    private final List<CardGame> cards; // Массив игровых карт
    private final List<GameObserver> observers = new ArrayList<>(); // Массив наблюдателей
    private CardGame firstSelectedCard; // Первая выбранная карта
    private CardGame secondSelectedCard; // Вторая выбранная карта
    private boolean isLockedTap = true; // Блокировка нажатия карт
    private boolean isAnimationProcessing = false;

    private int matchedPairs = 0;
    private int countErrors = 0;
    private int countExtraTaps = 0;
    private int cardTurning = -1;
    private final long gameStartTime;


    public GameLogic(List<CardGame> cards) {
        this.cards = cards;
        this.gameStartTime = System.currentTimeMillis();
    }


    // Добавление наблюдателей
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    // Тап карт
    public void cardClicked(CardGame card) {
        if (card.isMatched()) return;

        notifyObservers(EventType.CARD_FLIPPED, card);

        if (card == firstSelectedCard){
            firstSelectedCard = null;
            countExtraTaps ++;
        }
        else if (firstSelectedCard == null) {
            firstSelectedCard = card;
        } else {
            secondSelectedCard = card;
            checkForMatch();
        }
    }


    // Проверка на совпадение карт
    private void checkForMatch() {
        if (firstSelectedCard.matches(secondSelectedCard)) {
            firstSelectedCard.setMatched(true);
            secondSelectedCard.setMatched(true);
            matchedPairs++;
            notifyObservers(EventType.PAIR_CARD_MATCHED, null);
        } else {
            countErrors ++;
            notifyObservers(EventType.PAIR_CARD_MISMATCHED, null);
        }
    }

    // Сброс выбор карт
    void resetSelection() {
        firstSelectedCard = null;
        secondSelectedCard = null;
    }

    // Вернуть массив выбранных карт
    public List<CardGame> getSelectedCards() {
        List<CardGame> selected = new ArrayList<>();
        if (firstSelectedCard != null) selected.add(firstSelectedCard);
        if (secondSelectedCard != null) selected.add(secondSelectedCard);
        return selected;
    }

    // Получить состояние блокировки нажатия
    public boolean isLockedTap() {
        return isLockedTap;
    }

    // Блокировка нажатия
    public void LockedTapBecauseAnimation() {
        isAnimationProcessing = true;
    }

    // Разблокирование нажатия
    public void UnLockedTapBecauseAnimation() {
        isAnimationProcessing = false;
        if (matchedPairs == cards.size()/2) {
            notifyObservers(EventType.GAME_OVER, null);
        }
    }

    public boolean isAnimationProcessing() {
        return !isAnimationProcessing;
    }

    // Изменить состояние игры: активная/неактивная
    public void changeStateGame() {
        resetSelection();
        for (CardGame card : cards) {
            if (isLockedTap && card.getFlipAnimation().isShowingFront()) {card.getFlipAnimation().play();}
            else if (!isLockedTap && !card.getFlipAnimation().isShowingFront()) {card.getFlipAnimation().play();}
        }
        cardTurning ++;
        isLockedTap = !isLockedTap;
    }

    // Возврат статистики игры
    public Map<String, String> getGameStats() {
        Map<String, String> stats = new HashMap<>();
        stats.put("matchedPairs", String.valueOf(matchedPairs));
        stats.put("errors", String.valueOf(countErrors));
        stats.put("cardTurning", String.valueOf(cardTurning/2));
        stats.put("extraTaps", String.valueOf(countExtraTaps));

        // Используем отдельный метод для форматирования времени
        stats.put("gameTime", formatGameTime(System.currentTimeMillis() - gameStartTime));

        return stats;
    }



    // Отдельный метод для форматирования времени
    private String formatGameTime(long totalMillis) {
        long totalSeconds = totalMillis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    // Оповещение наблюдателей об изменении исхода игры
    private void notifyObservers(EventType type, CardGame card) {
        for (GameObserver observer : observers) {
            observer.update(type, card);
        }
    }
}
