module org.example.game_memo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.game_memo to javafx.fxml;
    exports org.example.game_memo;
    exports org.example.game_memo.CardFactories;
    opens org.example.game_memo.CardFactories to javafx.fxml;
    exports org.example.game_memo.Controllers;
    opens org.example.game_memo.Controllers to javafx.fxml;
    exports org.example.game_memo.ControllerHelpers;
    opens org.example.game_memo.ControllerHelpers to javafx.fxml;
    exports org.example.game_memo.Containers;
    opens org.example.game_memo.Containers to javafx.fxml;
    exports org.example.game_memo.CardThings;
    opens org.example.game_memo.CardThings to javafx.fxml;
}