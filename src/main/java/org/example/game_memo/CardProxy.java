package org.example.game_memo;

import javafx.scene.image.Image;

// Прокси для ленивой загрузки
public class CardProxy implements Card {
    private CardReal realCard;
    private final String imagePath;
    private final String description;

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
}


