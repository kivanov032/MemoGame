package org.example.game_memo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private final ErrorBox errorBox = new ErrorBox(); // Класс для вывода ошибки

    @Override
    public void start(Stage primaryStage) {
        try {
            // Инициализация загрузчика
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frames/start_game.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Вход в игру Мемо");
            primaryStage.setResizable(false);

            // Получение и настройка контроллера
            GameController controller = loader.getController();
            controller.setPrimaryStage(primaryStage);

            primaryStage.show(); // Отображение окна
        } catch (IOException e) {
            e.printStackTrace();
            errorBox.show("Критическая ошибка запуска: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
