package org.example.game_memo.ControllerHelpers;

import org.example.game_memo.Containers.CardFactoriesContainer;
import org.example.game_memo.CardFactories.*;

public class GameConfigurationFacade {

    private final CardFactoriesContainer cardFactoriesContainer;

    public GameConfigurationFacade(CardFactoriesContainer cardFactoriesContainer) {
        this.cardFactoriesContainer = cardFactoriesContainer;
    }

    // Установка сложности
    public void setDifficulty(String complexity) {
        switch (complexity) {
            case "EASY" -> cardFactoriesContainer.setCountCard(6); // 6 пар = 12 карт
            case "MEDIUM" -> cardFactoriesContainer.setCountCard(10); // 10 пар = 20 карт
            case "HARD" -> cardFactoriesContainer.setCountCard(15); // 15 пар = 30 карт
        }
    }

    // Установка категорий
    public void selectCategories(boolean smiles, boolean animals, boolean food,
                                 boolean sport, boolean flags) {
        cardFactoriesContainer.clearFactories();
        if (smiles) cardFactoriesContainer.addFactory(new SmileCardFactory());
        if (animals) cardFactoriesContainer.addFactory(new AnimalCardFactory());
        if (food) cardFactoriesContainer.addFactory(new FoodCardFactory());
        if (sport) cardFactoriesContainer.addFactory(new SportCardFactory());
        if (flags) cardFactoriesContainer.addFactory(new FlagCardFactory());
    }

    public CardFactoriesContainer getCardFactoriesContainer() {
        return cardFactoriesContainer;
    }
}
