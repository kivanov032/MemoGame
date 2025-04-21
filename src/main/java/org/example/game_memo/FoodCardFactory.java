package org.example.game_memo;


import javafx.scene.image.Image;
import java.util.*;
import static java.util.Objects.requireNonNull;

public class FoodCardFactory extends CardFactory {
    public FoodCardFactory() {
        super("Еда");
    }

    @Override
    protected List<CardProxy> initProxies() {
        List<CardProxy> proxies = new ArrayList<>();
        Map<String, String> images = Pictures.FOOD_IMAGES;
        String imageDirectory = Pictures.DIRECTORY_TO_FOOD_IMAGES;

        for (Map.Entry<String, String> entry : images.entrySet()) {
            proxies.add(new CardProxy(imageDirectory + entry.getKey(), entry.getValue()));
        }
        return proxies;
    }

    @Override
    protected FlipAnimation createFlipAnimation(Image frontImage) {
        return new FlipAnimation(
                null, // ImageView будет установлен позже
                frontImage, // Из CardProxy
                getBackImage()   // Общее для всех рубашка
        );
    }


    private Image getBackImage() {
        return new Image(requireNonNull(getClass().getResourceAsStream(Pictures.CARD_BACK_PATH)));
    }
}
