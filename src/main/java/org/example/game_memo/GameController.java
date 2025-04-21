package org.example.game_memo;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameController implements GameObserver{
    private final CardContainer cardContainer = CardContainer.getInstance(); // Класс-Singelton для хранения и подгрузки карточек для будущей игры
    private GameLogic gameLogic = null; // Класс для логики игры и хранения игровых карточек

    private Stage currentStage; // Текущая сцена
    private final ErrorBox errorBox = new ErrorBox(); // Класс для вывода ошибки

    // Элементы UI
    @FXML public Button goToMenuButton = new Button(), startGameButton = new Button(), changeStateCard = new Button(), updateCards = new Button();
    @FXML public CheckBox selectAllCheckBox, checkBoxSmiles, checkBoxAnimal, checkBoxFood, checkBoxSport, checkBoxFlags;
    @FXML private RadioButton checkBoxEasy = new RadioButton(), checkBoxMedium = new RadioButton(), checkBoxHard = new RadioButton();
    @FXML private ToggleGroup difficultyToggleGroup = new ToggleGroup(); // Группа для Radio типов
    @FXML private Label matchedPairsLabel = new Label(), errorsLabel = new Label(), extraTapsLabel = new Label(), timeLabel = new Label(), cardTurningLabel = new Label();
    @FXML private GridPane gridPane;
    @FXML private HBox bottomHBox;

    @FXML
    public void initialize() {
        // Заполнение Группа для Radio всеми вариациями Radio
        checkBoxEasy.setToggleGroup(difficultyToggleGroup);
        checkBoxMedium.setToggleGroup(difficultyToggleGroup);
        checkBoxHard.setToggleGroup(difficultyToggleGroup);
        checkBoxMedium.setSelected(true); // Дефолтный выбор Radio

        goToMenuButton.setOnMouseClicked(this::switchToMenu); // Кнопка "МЕНЮ" для перехода на меню
        startGameButton.setOnMouseClicked(this::startGame); // Кнопка "Начать игру! " в меню игры

        updateCards.setOnMouseClicked(e -> { // Кнопка "Обновить карточки" перезагрузки карточек через фабрики
            if (gameLogic != null){
                startGame(e);
            }
        });

        changeStateCard.setOnMouseClicked(e -> { // Кнопка изменения состояния ВСЕХ карточек
            if (gameLogic != null) {
                if (gameLogic.isAnimationProcessing()) {
                    gameLogic.changeStateGame();
                    changeStateCard.setText(gameLogic.isLockedTap() ? "Перевернуть карточки" : "Посмотреть карточки");
                }
            }
        });
    }

    // Удаление текущей сцены
    private void closeCurrentStage() {
        if (currentStage != null) {
            currentStage.close();
            errorBox.close();
        }
    }

    // Установка сцены
    public void setPrimaryStage(Stage stage) {
        this.currentStage = stage;
    }

    // Переход на экран меню
    private void switchToMenu(MouseEvent event) {
        try {
            // Инициализация загрузчика
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frames/menu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Меню выбора категорий карточек");
            stage.setResizable(false);

            // Получение и настройка контроллера
            GameController controller = loader.getController();
            controller.setPrimaryStage(stage);

            closeCurrentStage(); // Закрытие предыдущего окна
            stage.show(); // Отображение окна
        } catch (IOException e) {
            e.printStackTrace();
            errorBox.show("Ошибка при переходе в Меню: " + e.getMessage());
        }
    }

    // Переход на экран меню
    private void switchToGameStatistics() {
        try {
//            //Воспроизведение звука
//            MediaPlayer mediaPlayer = new MediaPlayer(new Media(requireNonNull(getClass().getResource("sounds/wow-sound-effect-great.mp3")).toString()));
//            mediaPlayer.setVolume(0.2);
//            mediaPlayer.play();

            Map<String, String> stats = gameLogic.getGameStats();

            // Инициализация загрузчика
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frames/game_statistics.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Статистика по пройденной игре");
            stage.setResizable(false);

            // Получение и настройка контроллера
            GameController controller = loader.getController();
            controller.setPrimaryStage(stage);
            controller.initializeStats(stats);
            (new MusicPlayer()).selectAndPlayMusic(stats);

            //Управление звуком при закрытии
            //stage.setOnHidden(e -> mediaPlayer.stop());

            closeCurrentStage(); // Закрытие предыдущего окна
            stage.show(); // Отображение окна
        } catch (IOException e) {
            e.printStackTrace();
            errorBox.show("Ошибка перехода: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            errorBox.show("Ошибка воспроизведения звука: " + e.getMessage());
        }
    }




    // Подготовка к игре и начало игры
    private void startGame(MouseEvent event) {
        if (gameLogic == null) {
            setupSelectedCardFactories(); // Выбор категорий карточек
            setupSelectedCardComplexity(); // Выбор сложности игры
        }
        List<CardGame> cards = cardContainer.prepareGameCards(); // Загрузка игровых карточек
        if (cards.isEmpty()){
            errorBox.show("Должна быть выбрана хотя бы одна категория карточек!");
            return;
        }
        openGameWindow(cards);
    }

    // Выбор категорий карточек
    private void setupSelectedCardFactories() {
        cardContainer.clearFactories();
        if (checkBoxSmiles.isSelected()) cardContainer.addFactory(new SmileCardFactory());
        if (checkBoxAnimal.isSelected()) cardContainer.addFactory(new AnimalCardFactory());
        if (checkBoxFood.isSelected()) cardContainer.addFactory(new FoodCardFactory());
        if (checkBoxSport.isSelected()) cardContainer.addFactory(new SportCardFactory());
        if (checkBoxFlags.isSelected()) cardContainer.addFactory(new FlagCardFactory());
    }

    // Выбор сложности игры
    private void setupSelectedCardComplexity() {
        String complexity = (String) difficultyToggleGroup.getSelectedToggle().getUserData();

        switch (complexity) {
            case "EASY": cardContainer.setCountCard(6); break; // 6 пар (4x3)
            case "MEDIUM": cardContainer.setCountCard(10); break; // 10 пар (5x4)
            case "HARD": cardContainer.setCountCard(15); break; // // 15 пар (6x5)
            default: errorBox.show("Выберите уровень сложности!");
        }
    }


    //=== Создание графических элементов для игрового поля и определение их размеров ===//

    // Переход на экран игры
    private void openGameWindow(List<CardGame> cards) {
        try {
            // Инициализация загрузчика
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frames/game_scene.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Игровой процесс");
            stage.setResizable(false); // Запрет изменения размера окна

            // Получение и настройка контроллера
            GameController controller = loader.getController();
            controller.setPrimaryStage(stage); // Передача текущего Stage

            // Инициализация игровой логики
            controller.gameLogic = new GameLogic(cards); // Создание экземпляра логики игры
            controller.gameLogic.addObserver(controller); // Регистрация контроллера как наблюдателя
            controller.setDifficultyLevel(cards.size()); // Задание промежутка между кнопками на фрейме игры
            controller.setupGameGrid(cards); // Заполнение игрового поля карточками
            setupWindowSize(stage, cards.size()); // Задание размера игрового окна


            closeCurrentStage(); // Закрытие предыдущего окна
            stage.show(); // Отображение окна
        } catch (IOException e) {
            e.printStackTrace();
            errorBox.show("Ошибка при запуске игры: " + e.getMessage());
        }
    }


    // Заполнение игрового поля карточками
    public void setupGameGrid(List<CardGame> cards) {
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
        if (gameLogic != null && !gameLogic.isLockedTap() && gameLogic.isAnimationProcessing()) {
            gameLogic.cardClicked(card);
        }
    }

    // Задание размера игрового окна
    private void setupWindowSize(Stage stage, int cardsCount) {
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
    public void setDifficultyLevel(int cardsCount) {
        double baseSpacing = 20; // Стандартный промежуток

        double extraSpacing = switch(cardsCount) {
            case 20 -> 30;  // Средний уровень
            case 30 -> 70; // Сложный уровень
            default -> 0;   // Легкий уровень
        };

        bottomHBox.setSpacing(baseSpacing + extraSpacing);
    }


    //=== Логика игры ===//
    //Варианты игровых событий
    @Override
    public void update(EventType type, CardGame card) {
        switch (type) {
            case CARD_FLIPPED: // Просто перевернуть карту
                Platform.runLater(card::flip);
                break;
            case PAIR_CARD_MATCHED: // Карты совпали
                handlePairResult(true);
                break;
            case PAIR_CARD_MISMATCHED: // Карты не совпали
                handlePairResult(false);
                break;
            case GAME_OVER: // Конец игры
                switchToGameStatistics();
                break;
        }
    }

    //Обработка результатов сравнения пары карт (совпадение/несовпадение)
    private void handlePairResult(boolean isMatched) {
        gameLogic.LockedTapBecauseAnimation();
        List<CardGame> cards = gameLogic.getSelectedCards();

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
            gameLogic.resetSelection();
            gameLogic.UnLockedTapBecauseAnimation();
        });

        // Запуск анимаций
        mainAnimation.play();
    }

    // Анимация переворота карты
    private ParallelTransition createFlipAnimation(CardGame card) {
        return new ParallelTransition(
                new Timeline(
                        new KeyFrame(Duration.ZERO,
                                e -> card.getImageView().setEffect(null)),
                        new KeyFrame(Duration.millis(300),
                                e -> card.flip())
                )
        );
    }


    // Анимация "сжатия" (исчезновения) карты
    private ParallelTransition createShrinkAnimation(CardGame card) {
        ImageView view = card.getImageView();

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
        ImageView view = card.getImageView();

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

    public void initializeStats(Map<String, String> stats) {
        matchedPairsLabel.setText(stats.get("matchedPairs"));
        errorsLabel.setText(stats.get("errors"));
        extraTapsLabel.setText(stats.get("extraTaps"));
        cardTurningLabel.setText(stats.get("cardTurning"));
        timeLabel.setText(stats.get("gameTime"));

//        System.out.println(stats.get("matchedPairs"));
//        System.out.println(stats.get("errors"));
//        System.out.println(stats.get("extraTaps"));
//        System.out.println(stats.get("gameTime"));
    }

    // Обработчик для чекбокса "Выбрать все"
    @FXML
    private void handleSelectAllCategories() {
        boolean selectAll = selectAllCheckBox.isSelected();
        checkBoxSmiles.setSelected(selectAll);
        checkBoxAnimal.setSelected(selectAll);
        checkBoxFood.setSelected(selectAll);
        checkBoxSport.setSelected(selectAll);
        checkBoxFlags.setSelected(selectAll);
    }

}


