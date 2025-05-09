package org.example.game_memo.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorBoxController implements IController {
    private static final String FXML_PATH = "/org/example/game_memo/frames/error_box.fxml";

    private Stage stage;

    // FXML элементы
    @FXML private Label errorMessageLabel;
    @FXML private Button closeButton;

    @FXML
    @Override
    public void initialize() {
        closeButton.setOnMouseClicked(e -> stage.close());
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Основной метод отображения окна ошибки
    public static void showError(String message, Stage parentStage) {
        try {
            Stage errorStage = new Stage();
            FXMLLoader loader = new FXMLLoader(ErrorBoxController.class.getResource(FXML_PATH));
            Parent root = loader.load();

            ErrorBoxController controller = loader.getController();
            controller.setStage(errorStage);

            errorStage.setScene(new Scene(root));
            errorStage.setTitle(parentStage != null ? "Ошибка" : "Критическая ошибка");
            errorStage.setResizable(false);

            controller.errorMessageLabel.setText(message);

            if (parentStage != null) {
                errorStage.initModality(Modality.WINDOW_MODAL);
                errorStage.initOwner(parentStage);
            }

            errorStage.show();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить диалоговое окно ошибки: " + e.getMessage());
            e.printStackTrace();
            //throw new RuntimeException("Ошибка показа статистики", e);
        }
    }

}
