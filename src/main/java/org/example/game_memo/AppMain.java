package org.example.game_memo;

import javafx.application.Application;
import javafx.stage.Stage;

import org.example.game_memo.Controllers.StartGameController;

public class AppMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartGameController.showStartGame(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

