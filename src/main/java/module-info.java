module org.example.game_memo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.game_memo to javafx.fxml;
    exports org.example.game_memo;
}