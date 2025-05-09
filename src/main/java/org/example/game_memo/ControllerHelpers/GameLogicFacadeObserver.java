package org.example.game_memo.ControllerHelpers;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.game_memo.CardThings.CardGame;
import org.example.game_memo.Containers.CardGameLogic;
import org.example.game_memo.Controllers.MenuController;
import org.example.game_memo.Controllers.StatisticsController;

import java.util.List;
import java.util.Map;

public class GameLogicFacadeObserver implements GameObserver{
    private final CardGameLogic cardGameLogic;

    private GridPane gridPane;
    private HBox bottomHBox;
    private Button changeStateCard;

    private Stage stage;

    public GameLogicFacadeObserver() {
        this.cardGameLogic = CardGameLogic.getInstance();
    }

    public void initializeGame(List<CardGame> cards, GridPane gridPane,
                               Stage stage, HBox bottomHBox, Button changeStateCard) {

        this.stage = stage;

        this.gridPane = gridPane;
        this.bottomHBox = bottomHBox;
        this.changeStateCard = changeStateCard;

        this.cardGameLogic.initialize(cards);

        MusicPlayerObserver musicObserver = new MusicPlayerObserver();
        musicObserver.preloadSounds(); // Предзагрузка звуков

        this.cardGameLogic.addObserver(new GameVisualObserver()); // Визуальный Наблюдатель
        this.cardGameLogic.addObserver(musicObserver); // Музыкальный Наблюдатель
        this.cardGameLogic.addObserver(this); // Сам класс является Наблюдателем

        setupGameGrid(cards); // Заполнение игрового поля карточками
        setupWindowSize(cards.size()); // Задание размера игрового окна
        setDifficultyLevel(cards.size()); // Задание промежутка между кнопками на фрейме игры
    }


    @Override
    public void update(EventType type, CardGameLogic.CardGameContainer container) {
        if (type == EventType.GAME_OVER) {
            // Откладываем уничтожение до завершения FX-анимаций
            Platform.runLater(() -> {
                // Сохраняем статистику перед уничтожением
                Map<String, String> stats = cardGameLogic.getGameData().getGameStats();
                CardGameLogic.destroyInstance();
                StatisticsController.showStatistics(stage, stats);
            });
        }
    }

    // Обновление карточек
    public void updateCards(List<CardGame> newCards) {
        cardGameLogic.getGameData().LockedAllTap(); // Блокировка нажатия на кнопки
        Platform.runLater(() -> {
            gridPane.getChildren().clear();

            for (int i = 0; i < newCards.size(); i++) {
                CardGame card = newCards.get(i);
                ImageView cardView = createCardImageView(card);

                int columns = gridPane.getColumnConstraints().size();
                gridPane.add(cardView, i % columns, i / columns);
            }
        });
        cardGameLogic.getGameData().UnLockedAllTap(); // Разблокирование нажатия на кнопки
        // Инициализация cardGameLogic новыми карточками (с обнулением состояния игры)
        cardGameLogic.initialize(newCards);

        // Если карточки были повёрнуты рубашкой, то переворачиваем их лицевой стороной
        if (!cardGameLogic.getGameData().isLockedTap())toggleCardsState();
    }

    // Переворот всех карточек
    public void toggleCardsState() {
        if (cardGameLogic != null && !cardGameLogic.getGameData().isLockedAllTap()) {
            cardGameLogic.getGameData().changeStateGame(); // Переворот всех карточек
            changeStateCard.setText(cardGameLogic.getGameData().isLockedTap() ? "Перевернуть карточки" : "Посмотреть карточки");
        }
    }

    // Переход в МЕНЮ
    public void goToMenu(){
        Platform.runLater(() -> {
            CardGameLogic.destroyInstance();
            MenuController.showMenu(stage);
        });
    }

    // Заполнение игрового поля карточками
    private void setupGameGrid(List<CardGame> cards) {
        // Очистка предыдущего состояния gridPane
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        // Настройка отступов и выравнивания
        gridPane.setHgap(20); // Горизонтальный промежуток между карточками
        gridPane.setVgap(20); // Вертикальный промежуток
        gridPane.setAlignment(Pos.CENTER); // Центрирование содержимого

        // Определение структуры сетки в зависимости от сложности
        int columns = switch(cards.size()) {
            case 30 -> 6; // Сложный режим (6x5)
            case 20 -> 5; // Средний режим (5x4)
            default -> 4;  // Легкий режим (4x3)
        };
        int rows = (int) Math.ceil((double) cards.size() / columns); // Расчет строк

        // Создание динамических ограничений для столбцов
        for (int i = 0; i < columns; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(100); // Фиксированная ширина столбца
            col.setHalignment(HPos.CENTER); // Центрирование по горизонтали
            gridPane.getColumnConstraints().add(col);
        }

        // Создание динамических ограничений для строк
        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(100); // Фиксированная высота строки
            row.setValignment(VPos.CENTER); // Центрирование по вертикали
            gridPane.getRowConstraints().add(row);
        }

        // Размещение карточек в сетке
        for (int i = 0; i < cards.size(); i++) {
            ImageView cardView = createCardImageView(cards.get(i));
            gridPane.add(cardView, i % columns, i / columns);
        }
    }

    // Создание ImageView для отображения карточки
    private ImageView createCardImageView(CardGame card) {
        ImageView imageView = new ImageView();
        // Фиксированные размеры карточки:
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true); // Сохранение пропорций

        card.bindImageView(imageView); // Привязка изображения к карточке
        imageView.setOnMouseClicked(e -> handleCardClick(card)); // Обработчик клика
        return imageView;
    }

    // Обработка клика по карточке
    private void handleCardClick(CardGame card) {
        if (cardGameLogic != null && !cardGameLogic.getGameData().isLockedTap()
                && !cardGameLogic.getGameData().isLockedAllTap()) {
            cardGameLogic.cardClicked(card);
        }
    }

    // Задание размера игрового окна
    private void setupWindowSize(int cardsCount) {
        // Определение базовой структуры сетки
        double[] baseSizes = switch (cardsCount) {
            case 30 -> new double[]{6, 5}; // 6x5
            case 20 -> new double[]{5, 4}; // 5x4
            default -> new double[]{4, 3};  // 4x3
        };

        // Расчет размеров с учетом:
        // - Размеров карточек (100x100)
        // - Отступов между ними (20px)
        // - Дополнительных элементов интерфейса
        double width = baseSizes[0] * 100 + (baseSizes[0] - 1) * 20 + (cardsCount == 12 ? 140 : 80);
        double height = baseSizes[1] * 100 + (baseSizes[1] - 1) * 20 + 240;

        // Проверка на максимальные размеры экрана
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(Math.min(width, screenBounds.getWidth() * 0.9)); // 90% ширины экрана
        stage.setHeight(Math.min(height, screenBounds.getHeight() * 0.9)); // 90% высоты
        stage.centerOnScreen(); // Центрирование окна
    }


    // Задание промежутка между кнопками на фрейме игры
    private void setDifficultyLevel(int cardsCount) {
        double baseSpacing = 20; // Стандартный промежуток

        double extraSpacing = switch(cardsCount) {
            case 20 -> 30;  // Средний уровень
            case 30 -> 70; // Сложный уровень
            default -> 0;   // Легкий уровень
        };

        bottomHBox.setSpacing(baseSpacing + extraSpacing);
    }
}
