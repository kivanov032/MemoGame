package org.example.game_memo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ErrorBox {
    @FXML private Label errorMessageLabel;
    @FXML private Button closeButton;

    private Stage stage;

    public ErrorBox() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("frames/error_box.fxml"));
            loader.setController(this);

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setScene(new Scene(loader.load()));
            stage.setResizable(false);

            closeButton.setOnAction(e -> stage.close());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show(String errorMessage) {
        errorMessageLabel.setText(errorMessage);
        stage.sizeToScene();
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
