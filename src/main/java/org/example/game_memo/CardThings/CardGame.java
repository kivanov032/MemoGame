package org.example.game_memo.CardThings;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardGame implements Card {
    private final Card card; // Абстрактная карта
    private final FlipAnimation flipAnimation; // Анимация Переворота карты

    public CardGame(Card card, FlipAnimation animation) {
        this.card = card;
        this.flipAnimation = animation;
    }

    @Override
    public Image getImage() {
        return card.getImage();
    }

    @Override
    public String getDescription() {
        return card.getDescription();
    }

    public FlipAnimation getFlipAnimation() {return flipAnimation;}

    // Привязка imageView к flipAnimation
    public void bindImageView(ImageView imageView) {
        if (flipAnimation != null) {
            flipAnimation.setImageView(imageView);
        }
    }

    // Поворот карты
    public void flip() {
        if (flipAnimation != null) {
            flipAnimation.play();
        }
    }

    // Сравнение 2-х карт (их описаний)
    public boolean matches(CardGame other) {
        return this.getDescription().equals(other.getDescription());
    }
}

