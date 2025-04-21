package org.example.game_memo;

import javafx.scene.image.Image;

import java.util.*;

public abstract class CardFactory implements ICardFactory {
    protected final List<CardProxy> proxies;
    protected final String categoryName;
    private final CardPairCreator pairCreator;

    protected CardFactory(String categoryName) {
        this.categoryName = categoryName;
        this.proxies = initProxies();
        this.pairCreator = new CardPairCreator();
    }

    protected abstract List<CardProxy> initProxies();
    protected abstract FlipAnimation createFlipAnimation(Image image);

    @Override
    public final List<CardGame> createCardPairs(int pairsCount) {
        return pairCreator.createPairs(proxies, pairsCount, this::createCard);
    }

    @Override
    public final String getCategoryName() {
        return categoryName;
    }

    protected CardGame createCard(CardProxy proxy) {
        return new CardGame(proxy, createFlipAnimation(proxy.getImage()));
    }
}
