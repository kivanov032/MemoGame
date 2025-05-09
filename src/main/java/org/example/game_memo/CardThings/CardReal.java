package org.example.game_memo.CardThings;

import javafx.scene.image.Image;

import static java.util.Objects.requireNonNull;

// Реальная карта (загружает изображение)
public class CardReal implements Card {
    private final Image image; // Путь к изображению карты (png)
    private final String description; // Описание карты

    public CardReal(String imagePath, String description) {
        this.image = new Image(requireNonNull(getClass().getResourceAsStream(imagePath)));
        this.description = description;
    }

    @Override public Image getImage() { return image; }
    @Override public String getDescription() { return description; }
}


