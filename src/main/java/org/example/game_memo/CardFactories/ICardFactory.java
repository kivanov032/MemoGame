package org.example.game_memo.CardFactories;

import javafx.scene.image.Image;
import org.example.game_memo.CardThings.CardGame;
import org.example.game_memo.CardThings.CardProxy;
import org.example.game_memo.CardThings.FlipAnimation;

import java.util.List;

// Интерфейс для абстрактной Фабрики карт
public interface ICardFactory {
    List<CardProxy> initProxies();
    FlipAnimation createFlipAnimation(Image frontImage);
    Image getBackImage();
    List<CardGame> createGameCards(int pairsCount);
    String getCategoryName();
}
