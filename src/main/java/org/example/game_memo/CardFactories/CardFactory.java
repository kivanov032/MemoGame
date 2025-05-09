package org.example.game_memo.CardFactories;

import javafx.scene.image.Image;
import org.example.game_memo.CardThings.CardGame;
import org.example.game_memo.CardThings.CardProxy;
import org.example.game_memo.CardThings.FlipAnimation;
import org.example.game_memo.Containers.PicturesContainer;

import java.util.*;

import static java.util.Objects.requireNonNull;

public abstract class CardFactory implements ICardFactory {
    protected final List<CardProxy> proxies; // Массив прокси-карт
    protected final String categoryName; // Название категории карт

    protected CardFactory(String categoryName) {
        this.categoryName = categoryName;
        this.proxies = initProxies();
    }

    // Инизиализация прокси-карт (виртуальная функция)
    @Override
    public abstract List<CardProxy> initProxies();

    // Создание игровых карт
    @Override
    public final List<CardGame> createGameCards(int pairsCount) {
        return CardGameCreator.createGameCards(proxies, pairsCount, this::createGameCard);
    }

    // Создание одной игровой карты
    protected CardGame createGameCard(CardProxy proxy) {
        FlipAnimation flipAnimation = createFlipAnimation(proxy.getImage());
        return new CardGame(proxy.getRealCard(), flipAnimation);
    }

    // Создание сущности "Анимация переворота" (карты)
    @Override
    public FlipAnimation createFlipAnimation(Image frontImage){
        return new FlipAnimation(null, frontImage, getBackImage());
    }

    // Создание рубашки карты
    @Override
    public Image getBackImage() {
        return new Image(requireNonNull(getClass().getResourceAsStream(PicturesContainer.CARD_BACK_PATH)));
    }

    // Вывести категорию карт
    @Override
    public final String getCategoryName() {
        return categoryName;
    }

}
