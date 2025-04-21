package org.example.game_memo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardGame implements Card {
    private final Card card;
    private final FlipAnimation flipAnimation;
    private boolean isMatched = false;
    private ImageView imageView;

    public CardGame(Card card, FlipAnimation animation) {
        this.card = card;
        this.flipAnimation = animation;
    }

    // Делегируем вызовы к Card
    @Override public Image getImage() { return card.getImage(); }
    @Override public String getDescription() { return card.getDescription(); }
    public ImageView getImageView() {return imageView;}
    public FlipAnimation getFlipAnimation() {return flipAnimation;}

    public void bindImageView(ImageView imageView) {
        this.imageView = imageView;
        if (flipAnimation != null) {
            flipAnimation.setImageView(imageView);
        }
    }

    public void flip() {
        if (flipAnimation != null) {
            flipAnimation.play();
        }
    }

    public boolean matches(CardGame other) {
        return this.getDescription().equals(other.getDescription());
    }

    public void reset() {
        if (flipAnimation != null) {
            flipAnimation.reset();
        }
        isMatched = false;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        this.isMatched = matched;
    }

}

