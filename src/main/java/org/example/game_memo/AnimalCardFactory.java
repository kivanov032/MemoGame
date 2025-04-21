package org.example.game_memo;

import javafx.scene.image.Image;
import static java.util.Objects.requireNonNull;

import java.util.*;

public class AnimalCardFactory extends CardFactory {
    public AnimalCardFactory() {
        super("Животные");
    }

    @Override
    protected List<CardProxy> initProxies() {
        List<CardProxy> proxies = new ArrayList<>();
        Map<String, String> animalImages = Pictures.ANIMAL_IMAGES;
        String imageDirectory = Pictures.DIRECTORY_TO_ANIMAL_IMAGES;

        for (Map.Entry<String, String> entry : animalImages.entrySet()) {
            proxies.add(new CardProxy(imageDirectory + entry.getKey(), entry.getValue()));
        }
        return proxies;
    }

    @Override
    protected FlipAnimation createFlipAnimation(Image frontImage) {
        return new FlipAnimation(null, frontImage, getBackImage()
        );
    }


    private Image getBackImage() {
        return new Image(requireNonNull(getClass().getResourceAsStream(Pictures.CARD_BACK_PATH)));
    }
}
