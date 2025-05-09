package org.example.game_memo.CardFactories;

import org.example.game_memo.CardThings.CardProxy;
import org.example.game_memo.Containers.PicturesContainer;

import java.util.*;

public class FlagCardFactory extends CardFactory {
    public FlagCardFactory() {
        super("Флаги");
    }

    @Override
    public List<CardProxy> initProxies() {
        List<CardProxy> proxies = new ArrayList<>();
        Map<String, String> images = PicturesContainer.FLAG_IMAGES;
        String imageDirectory = PicturesContainer.DIRECTORY_TO_FLAG_IMAGES;

        for (Map.Entry<String, String> entry : images.entrySet()) {
            proxies.add(new CardProxy(imageDirectory + entry.getKey(), entry.getValue()));
        }
        return proxies;
    }

    //    @Override
//    public FlipAnimation createFlipAnimation(Image frontImage) {
//        return new FlipAnimation(null, frontImage, getBackImage());
//    }
//
//    @Override
//    public Image getBackImage() {
//        return new Image(requireNonNull(getClass().getResourceAsStream(PicturesContainer.CARD_BACK_PATH)));
//    }
}
