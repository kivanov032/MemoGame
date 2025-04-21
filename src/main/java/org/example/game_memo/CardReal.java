package org.example.game_memo;

import javafx.scene.image.Image;
import static java.util.Objects.requireNonNull;

// Реальная карта (загружает изображение)
public class CardReal implements Card {
    private final Image image;
    private final String description;

    public CardReal(String imagePath, String description) {
        this.image = new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));
        this.description = description;
    }

    @Override public Image getImage() { return image; }
    @Override public String getDescription() { return description; }
}


