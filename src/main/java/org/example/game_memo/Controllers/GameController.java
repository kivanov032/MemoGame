package org.example.game_memo.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.game_memo.Containers.CardFactoriesContainer;
import org.example.game_memo.ControllerHelpers.*;

public class GameController implements IController{
    private static final String FXML_PATH = "/org/example/game_memo/frames/game_scene.fxml";

    private Stage stage;
    private final GameLogicFacadeObserver gameFacade;
    private final CardFactoriesContainer cardFactoriesContainer;

    // FXML элементы
    @FXML private GridPane gridPane;
    @FXML private HBox bottomHBox;
    @FXML private Button changeStateCard, goToMenuButton, updateCards;

    private GameController(CardFactoriesContainer cardFactoriesContainer) {
        this.cardFactoriesContainer = cardFactoriesContainer;
        this.gameFacade = new GameLogicFacadeObserver();
    }

    @FXML
    @Override
    public void initialize() {
        updateCards.setOnMouseClicked(e -> gameFacade.updateCards(cardFactoriesContainer.getGameCards()));
        goToMenuButton.setOnMouseClicked(e -> gameFacade.goToMenu());
        changeStateCard.setOnMouseClicked(e -> gameFacade.toggleCardsState());
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void showGame(Stage parentStage, CardFactoriesContainer cardFactoriesContainer) {
        try {
            Stage gameStage = new Stage();

            FXMLLoader loader = new FXMLLoader(GameController.class.getResource(FXML_PATH));
            loader.setControllerFactory(clazz -> new GameController(cardFactoriesContainer));

            Parent root = loader.load();
            GameController controller = loader.getController();
            controller.setStage(gameStage);

            // Инициализация игрового поля
            controller.initializeGame();

            gameStage.setScene(new Scene(root));
            gameStage.setTitle("Игра Мемо");
            gameStage.setResizable(false);

            if (parentStage != null) {
                parentStage.close();
            }

            gameStage.show();
            gameStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        } catch (Exception e) {
            ErrorBoxController.showError("Ошибка запуска игры: " + e.getMessage(), parentStage);
            e.printStackTrace();
            //throw new RuntimeException("Ошибка показа статистики", e);
        }
    }

    // Инициализация gameFacade
    private void initializeGame() {
        gameFacade.initializeGame(
                cardFactoriesContainer.getGameCards(),
                gridPane,
                stage,
                bottomHBox,
                changeStateCard
        );
    }
}
