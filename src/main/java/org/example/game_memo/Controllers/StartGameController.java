package org.example.game_memo.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGameController implements IController {
    private static final String FXML_PATH = "/org/example/game_memo/frames/start_game.fxml";

    private Stage stage;

    // FXML элементы
    @FXML private Button goToMenuButton;

    @FXML
    @Override
    public void initialize() {
        goToMenuButton.setOnMouseClicked(e -> MenuController.showMenu(stage));
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void showStartGame(Stage parentStage) {
        try {
            Stage startStage = new Stage();
            FXMLLoader loader = new FXMLLoader(StartGameController.class.getResource(FXML_PATH));
            Parent root = loader.load();

            StartGameController controller = loader.getController();
            controller.setStage(startStage);

            startStage.setScene(new Scene(root));
            startStage.setTitle("Добро пожаловать в Мемо!");
            startStage.setResizable(false);

            if (parentStage != null) {
                parentStage.close();
            }

            startStage.show();
            startStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        } catch (IOException e) {
            ErrorBoxController.showError("Ошибка загрузки стартового окна: " + e.getMessage(), parentStage);
            e.printStackTrace();
            //throw new RuntimeException("Ошибка показа статистики", e);
        }
    }

}




