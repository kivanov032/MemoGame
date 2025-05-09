package org.example.game_memo.Containers;

import org.example.game_memo.CardThings.CardGame;
import org.example.game_memo.ControllerHelpers.EventType;
import org.example.game_memo.ControllerHelpers.GameObserver;
import org.example.game_memo.ControllerHelpers.GameObservable;

import java.util.*;

public class CardGameLogic implements GameObservable {
    private static CardGameLogic instance;
    private final CardGameContainer gameData = new CardGameContainer(); // Вложенный класс-контейнер
    private final List<GameObserver> observers = Collections.synchronizedList(new ArrayList<>()); // Потокобезопасный список наблюдателей

    public CardGameLogic() {}

    // Метод для получения единственного экземпляра
    public static synchronized CardGameLogic getInstance() {
        if (instance == null) {
            instance = new CardGameLogic();
        }
        return instance;
    }

    // Улучшенный destroyInstance()
    public static synchronized void destroyInstance() {
        if (instance != null) {
            instance.removeAllObservers(); // Сначала очищаем наблюдателей
            instance.getGameData().resetGameState();
            instance = null;
        }
    }

    // Метод для инициализации с картами
    public void initialize(List<CardGame> cards) {
        if(instance == null)notifyObservers(EventType.UPDATE_CARDS, gameData);
        gameData.cards = new ArrayList<>(cards);
        gameData.gameStartTime = System.currentTimeMillis();
        gameData.resetGameState();
    }

    // Добавление наблюдателей
    @Override
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    // Удаление всех наблюдателей
    @Override
    public void removeObserver(GameObserver observer) {
        synchronized(observers) {
            observers.remove(observer);
        }
    }

    // Безопасное удаление наблюдателей
    @Override
    public void removeAllObservers() {
        synchronized(observers) {
            observers.clear();
        }
    }

    // Обработка нажатия карты
    public void cardClicked(CardGame card) {

        gameData.selectedCard = card;
        notifyObservers(EventType.CARD_FLIPPED, gameData);
        gameData.selectedCard = null;

        // Если карту нажимаю повторно
        if (card == gameData.firstSelectedCard){
            gameData.firstSelectedCard = null;
            gameData.countExtraTaps ++;
        }
        // Если карту нажимают первый раз
        else if (gameData.firstSelectedCard == null) {
            gameData.firstSelectedCard = card;
        }
        // Если нажимают карту, отличную от предыдущей
        else {
            gameData.secondSelectedCard = card;
            checkForMatch();
        }
    }

    // Проверка на совпадение карт
    private void checkForMatch() {
        if (gameData.firstSelectedCard.matches(gameData.secondSelectedCard)) {
            gameData.matchedPairs++;
            notifyObservers(EventType.PAIR_CARD_MATCHED, gameData);
        } else {
            gameData.countErrors ++;
            notifyObservers(EventType.PAIR_CARD_MISMATCHED, gameData);
        }
    }

    // Оповещение Наблюдателей об окончании игры
    public void notifyAboutGameOver(){
        notifyObservers(EventType.GAME_OVER, gameData);
    }

    // Потокобезопасное оповещение наблюдателей об изменении исхода игры
    @Override
    public void notifyObservers(EventType type, CardGameContainer gameData) {
        List<GameObserver> observersCopy;
        synchronized(observers) {
            observersCopy = new ArrayList<>(observers);
        }

        for (GameObserver observer : observersCopy) {
            try {
                observer.update(type, gameData);
            } catch (Exception e) {
                System.err.println("Observer error: " + e.getMessage());
            }
        }
    }
    public CardGameContainer getGameData() {return gameData;}


    // Вложенный класс для хранения метрик состояния игры и необходимых методов
    public class CardGameContainer {

        private List<CardGame> cards; // Массив игровых карт
        private CardGame firstSelectedCard; // Первая выбранная карта
        private CardGame secondSelectedCard; // Вторая выбранная карта
        private CardGame selectedCard = null;
        private boolean isLockedTap = true; // Блокировка нажатия карт
        private boolean isLockedAllTap = false; // Блокировка на всё нажатие

        private int matchedPairs = 0; // Кол-во найденных пар карточек
        private int countErrors = 0; // Кол-во ошибок
        private int countExtraTaps = 0; // Кол-во лишних переворотов карточек
        private int cardTurning = -1; // Кол-во переворотов карточек
        private long gameStartTime; // Начало игры

        // Вернуть массив выбранных карт
        public List<CardGame> getSelectedCards() {
            List<CardGame> selected = new ArrayList<>();
            if (gameData.firstSelectedCard != null){
                selected.add(gameData.firstSelectedCard);
            }
            if (gameData.secondSelectedCard != null){
                selected.add(gameData.secondSelectedCard);
            }
            return selected;
        }

        // Возврат статистики игры
        public Map<String, String> getGameStats() {
            Map<String, String> stats = new HashMap<>();
            stats.put("matchedPairs", String.valueOf(gameData.matchedPairs));
            stats.put("errors", String.valueOf(gameData.countErrors));
            stats.put("cardTurning", String.valueOf(gameData.cardTurning/2));
            stats.put("extraTaps", String.valueOf(gameData.countExtraTaps));

            // Используем отдельный метод для форматирования времени
            stats.put("gameTime", formatGameTime(
                    System.currentTimeMillis() - gameData.gameStartTime));

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

        // Изменить состояние игры: активная/неактивная
        public void changeStateGame() {
            notifyObservers(EventType.TOGGLE_CARDS, gameData);
            gameData.resetSelection();
            for (CardGame card : gameData.cards) {
                if (gameData.isLockedTap && card.getFlipAnimation().isShowingFront()) {
                    card.getFlipAnimation().play();
                }
                else if (!gameData.isLockedTap && !card.getFlipAnimation().isShowingFront()) {
                    card.getFlipAnimation().play();
                }
            }
            gameData.cardTurning ++;
            gameData.isLockedTap = !gameData.isLockedTap;
        }

        // Получить выбранную карту
        public CardGame getSelectedCard(){
            return gameData.selectedCard;
        }

        // Метод для сброса состояния данных
        public void resetGameState() {
            resetSelection();
            matchedPairs = 0;
            countErrors = 0;
            countExtraTaps = 0;
            cardTurning = -1;
            isLockedTap = true;
            isLockedAllTap = false;
        }

        // Сброс выбор карт
        public void resetSelection() {
            firstSelectedCard = null;
            secondSelectedCard = null;
        }

        // Получить состояние карточной блокировки нажатия
        public boolean isLockedTap() {
            return gameData.isLockedTap;
        }

        // Получить состояние общей блокировки
        public boolean isLockedAllTap() {
            return gameData.isLockedAllTap;
        }

        // Блокировка нажатия
        public void LockedAllTap() {
            gameData.isLockedAllTap = true;
        }

        // Разблокирование нажатия
        public void UnLockedAllTap() {
            gameData.isLockedAllTap = false;
            if (gameData.matchedPairs == gameData.cards.size()/2) {
                notifyObservers(EventType.GAME_OVER, gameData);
            }
        }
    }
}
