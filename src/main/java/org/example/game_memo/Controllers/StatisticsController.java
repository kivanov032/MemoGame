package org.example.game_memo.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Map;

public class StatisticsController implements IController{
    private static final String FXML_PATH = "/org/example/game_memo/frames/statistics.fxml";

    private Stage stage;

    // FXML элементы
    @FXML private Label
            matchedPairsLabel,
            errorsLabel,
            extraTapsLabel,
            timeLabel,
            cardTurningLabel;

    @FXML public Button goToMenuButton;

    @FXML
    @Override
    public void initialize() {
        goToMenuButton.setOnMouseClicked(e -> MenuController.showMenu(stage));
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Фабричный метод с улучшенной логикой
    public static void showStatistics(Stage parentStage, Map<String, String> stats) {
        try {
            Stage statsStage = new Stage();
            FXMLLoader loader = new FXMLLoader(StatisticsController.class.getResource(FXML_PATH));
            Parent root = loader.load();

            StatisticsController controller = loader.getController();
            controller.setStage(statsStage);

            statsStage.setScene(new Scene(root));
            statsStage.setTitle("Статистика игры");
            statsStage.setResizable(false);

            controller.initializeStats(stats);

            if (parentStage != null) {
                parentStage.close();
            }

            statsStage.show();
            statsStage.setOnCloseRequest(e -> {
                System.exit(0);
            });
        } catch (Exception e) {
            ErrorBoxController.showError("Ошибка показа статистики: " + e.getMessage(), parentStage);
            e.printStackTrace();
            //throw new RuntimeException("Ошибка показа статистики", e);
        }
    }

    // Инициализация статистики
    public void initializeStats(Map<String, String> stats) {
        matchedPairsLabel.setText(stats.getOrDefault("matchedPairs", "0"));
        errorsLabel.setText(stats.getOrDefault("errors", "0"));
        extraTapsLabel.setText(stats.getOrDefault("extraTaps", "0"));
        cardTurningLabel.setText(stats.getOrDefault("cardTurning", "0"));
        timeLabel.setText(stats.getOrDefault("gameTime", "00:00"));
    }
}
