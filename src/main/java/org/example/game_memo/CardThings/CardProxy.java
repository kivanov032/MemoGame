package org.example.game_memo.CardThings;

import javafx.scene.image.Image;

// Прокси для ленивой загрузки
public class CardProxy implements Card {
    private CardReal realCard; // Реальное изображение
    private final String imagePath; // Путь к изображению карты (png)
    private final String description; // Описание карты

    public CardProxy(String imagePath, String description) {
        this.imagePath = imagePath;
        this.description = description;
    }

    @Override
    public synchronized Image getImage() {
        if (realCard == null) {
            realCard = new CardReal(imagePath, description);
        }
        return realCard.getImage();
    }

    public String getDescription() { return description; }

    public CardReal getRealCard() {
        return realCard;
    }
}


