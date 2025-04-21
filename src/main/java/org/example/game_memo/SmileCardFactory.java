package org.example.game_memo;

import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class SmileCardFactory extends CardFactory {
    public SmileCardFactory() {
        super("Эмоции");
    }

    @Override
    protected List<CardProxy> initProxies() {
        List<CardProxy> proxies = new ArrayList<>();
        Map<String, String> images = Pictures.SMILE_IMAGES;
        String imageDirectory = Pictures.DIRECTORY_TO_SMILE_IMAGES;

        for (Map.Entry<String, String> entry : images.entrySet()) {
            proxies.add(new CardProxy(imageDirectory + entry.getKey(), entry.getValue()));
        }
        return proxies;
    }

    @Override
    protected FlipAnimation createFlipAnimation(Image frontImage) {
        return new FlipAnimation(null, frontImage, getBackImage());
    }

    private Image getBackImage() {
        return new Image(requireNonNull(getClass().getResourceAsStream(Pictures.CARD_BACK_PATH)));
    }
}
