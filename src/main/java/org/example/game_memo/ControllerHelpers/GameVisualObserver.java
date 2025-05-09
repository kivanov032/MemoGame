package org.example.game_memo.ControllerHelpers;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.game_memo.CardThings.CardGame;
import org.example.game_memo.Containers.CardGameLogic;

import java.util.List;

public class GameVisualObserver implements GameObserver {

    @Override
    public void update(EventType type, CardGameLogic.CardGameContainer gameData) {
        switch (type) {
            case CARD_FLIPPED -> Platform.runLater(gameData.getSelectedCard()::flip);
            case PAIR_CARD_MATCHED -> handlePair(gameData, true);
            case PAIR_CARD_MISMATCHED -> handlePair(gameData, false);
        }
    }

    //Обработка результатов сравнения пары карт (совпадение/несовпадение)
    private void handlePair(CardGameLogic.CardGameContainer cardGameContainer, boolean isMatched) {
        cardGameContainer.LockedAllTap();
        List<CardGame> cards = cardGameContainer.getSelectedCards();

        // Выбор цвета подсветки в зависимости от результата сравнения
        Color highlightColor = isMatched ? Color.LIGHTGREEN : Color.RED;

        // Создание параллельной анимации, которая будет выполняться для всех карт одновременно
        ParallelTransition mainAnimation = new ParallelTransition();

        // Для каждой выбранной карты создаем последовательную анимацию
        cards.forEach(card -> {
            SequentialTransition sequence = new SequentialTransition(
                    // Анимация подсветки карты (выбранным цветом)
                    createHighlightAnimation(card, highlightColor),

                    // Определение поведения для совпавших и несовпавших карт:
                    isMatched ?
                            // Для совпавших карт:
                            // - пауза 0.5 секунды
                            // - анимация уменьшения (исчезновения) карты
                            new SequentialTransition(
                                    new PauseTransition(Duration.seconds(0.5)),
                                    createShrinkAnimation(card)) // Анимация "сжатия" (исчезновения) карты
                            :
                            // Для несовпавших карт:
                            // - пауза 1 секунда
                            // - анимация переворота карты
                            new SequentialTransition(
                                    new PauseTransition(Duration.seconds(1)),
                                    createFlipAnimation(card)) // Анимация переворота карты
            );

            // Добавляем анимацию для текущей карты в общий набор анимаций
            mainAnimation.getChildren().add(sequence);
        });

        // Сброс выбранных карт в игровой логике
        mainAnimation.setOnFinished(e -> {
            cardGameContainer.resetSelection();
            cardGameContainer.UnLockedAllTap();
        });

        // Запуск анимаций
        mainAnimation.play();
    }

    // Анимация переворота карты
    private ParallelTransition createFlipAnimation(CardGame card) {
        return new ParallelTransition(
                new Timeline(
                        new KeyFrame(Duration.ZERO,
                                e -> card.getFlipAnimation().getImageView().setEffect(null)),
                        new KeyFrame(Duration.millis(300),
                                e -> card.flip())
                )
        );
    }


    // Анимация "сжатия" (исчезновения) карты
    private ParallelTransition createShrinkAnimation(CardGame card) {
        ImageView view = card.getFlipAnimation().getImageView();

        // Анимация масштабирования до 0 за 500 мс
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), view);
        scale.setToX(0); // Сжатие по X
        scale.setToY(0); // Сжатие по Y

        // Параллельная анимация исчезновения
        FadeTransition fade = new FadeTransition(Duration.millis(500), view);
        fade.setToValue(0); // Полная прозрачность

        // Комбинация обоих эффектов
        return new ParallelTransition(scale, fade);
    }

    // Анимация подсветки карты
    private ParallelTransition createHighlightAnimation(CardGame card, Color color) {
        ImageView view = card.getFlipAnimation().getImageView();;

        // Эффект свечения с указанным цветом и радиусом
        DropShadow glow = new DropShadow(20, color);

        // Анимация плавного появления свечения за 300 мс
        return new ParallelTransition(
                new Timeline(
                        new KeyFrame(Duration.ZERO,    // Начальное состояние - без эффекта
                                new KeyValue(view.effectProperty(), null)),
                        new KeyFrame(Duration.millis(300), // Конечное состояние - полное свечение
                                new KeyValue(view.effectProperty(), glow))
                )
        );
    }

}
