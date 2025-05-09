package org.example.game_memo.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.game_memo.Containers.CardFactoriesContainer;
import org.example.game_memo.ControllerHelpers.GameConfigurationFacade;

public class MenuController implements IController{
    private static final String FXML_PATH = "/org/example/game_memo/frames/menu.fxml";

    private Stage stage;
    private final GameConfigurationFacade gameConfigurationFacade;

    // FXML элементы
    @FXML private CheckBox
            selectAllCheckBox,
            checkBoxSmiles,
            checkBoxAnimal,
            checkBoxFood,
            checkBoxSport,
            checkBoxFlags;

    @FXML private RadioButton
            checkBoxEasy,
            checkBoxMedium,
            checkBoxHard;

    @FXML private ToggleGroup difficultyToggleGroup;
    @FXML private Button startGameButton;

    @FXML
    @Override
    public void initialize() {
        configureDifficultyToggleGroup();
        startGameButton.setOnMouseClicked(e -> startGame());
        selectAllCheckBox.setOnMouseClicked(e -> handleSelectAllCategories());
    }

    // Инициализация ToggleGroup (чекбоксы с единственным выбором)
    private void configureDifficultyToggleGroup() {
        checkBoxEasy.setToggleGroup(difficultyToggleGroup);
        checkBoxEasy.setUserData("EASY");

        checkBoxMedium.setToggleGroup(difficultyToggleGroup);
        checkBoxMedium.setUserData("MEDIUM");
        checkBoxMedium.setSelected(true);

        checkBoxHard.setToggleGroup(difficultyToggleGroup);
        checkBoxHard.setUserData("HARD");
    }

    private MenuController(GameConfigurationFacade gameConfigurationFacade) {
        this.gameConfigurationFacade = gameConfigurationFacade;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void showMenu(Stage parentStage) {
        try {
            Stage menuStage = new Stage();

            CardFactoriesContainer container = CardFactoriesContainer.getInstance();
            GameConfigurationFacade facade = new GameConfigurationFacade(container);

            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource(FXML_PATH));
            loader.setControllerFactory(clazz -> new MenuController(facade));
            Parent root = loader.load();

            MenuController controller = loader.getController();
            controller.setStage(menuStage);

            // Настройка окна
            menuStage.setScene(new Scene(root));
            menuStage.setTitle("Меню игры");
            menuStage.setResizable(false);

            // Закрытие родительского окна
            if (parentStage != null) {
                parentStage.close();
            }
            menuStage.show();
            menuStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        } catch (Exception e) {
            ErrorBoxController.showError("Ошибка загрузки меню: " + e.getMessage(), parentStage);
            e.printStackTrace();
            //throw new RuntimeException("Ошибка показа статистики", e);
        }
    }

    // Начало игры (инициализация карт)
    private void startGame() {
        // Проверка на выбор категории
        if (validateCategorySelection()) {
            ErrorBoxController.showError("Выберите хотя бы одну категорию!", stage);
            return;
        }

        // Установка сложности
        gameConfigurationFacade.selectCategories(
                checkBoxSmiles.isSelected(),
                checkBoxAnimal.isSelected(),
                checkBoxFood.isSelected(),
                checkBoxSport.isSelected(),
                checkBoxFlags.isSelected()
        );

        // Установка категорий
        String complexity = (String) difficultyToggleGroup.getSelectedToggle().getUserData();
        gameConfigurationFacade.setDifficulty(complexity);

        // Переход в контроллер игры
        GameController.showGame(stage, gameConfigurationFacade.getCardFactoriesContainer());
    }

    // Проверка на выбор категории
    private Boolean validateCategorySelection() {
        return  !(checkBoxSmiles.isSelected() ||
                checkBoxAnimal.isSelected() ||
                checkBoxFood.isSelected() ||
                checkBoxSport.isSelected() ||
                checkBoxFlags.isSelected());
    }

    // Кнопка (чекбокс) выбора всех категорий
    private void handleSelectAllCategories() {
        boolean selectAll = selectAllCheckBox.isSelected();
        checkBoxSmiles.setSelected(selectAll);
        checkBoxAnimal.setSelected(selectAll);
        checkBoxFood.setSelected(selectAll);
        checkBoxSport.setSelected(selectAll);
        checkBoxFlags.setSelected(selectAll);
    }

}
