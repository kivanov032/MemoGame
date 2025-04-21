package org.example.game_memo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Pictures {

    public static final String CARD_BACK_PATH = "/CardBack/card_back.jpg";

    public static final String IMAGE_MAIN_PATH = "/Emodji/";

    public static final Map<String, String> SMILE_IMAGES;
    public static final String DIRECTORY_TO_SMILE_IMAGES = IMAGE_MAIN_PATH + "SmileEmodji/";
    public static final Map<String, String> ANIMAL_IMAGES;
    public static final String DIRECTORY_TO_ANIMAL_IMAGES = IMAGE_MAIN_PATH + "AnimalEmodji/";
    public static final Map<String, String> SPORT_IMAGES;
    public static final String DIRECTORY_TO_SPORT_IMAGES = IMAGE_MAIN_PATH + "SportEmodji/";
    public static final Map<String, String> FOOD_IMAGES;
    public static final String DIRECTORY_TO_FOOD_IMAGES = IMAGE_MAIN_PATH + "FoodEmodji/";
    public static final Map<String, String> FLAG_IMAGES;
    public static String DIRECTORY_TO_FLAG_IMAGES = IMAGE_MAIN_PATH + "FlagEmodji/";

    static {
        // Инициализация словаря с изображениями эмоций
        Map<String, String> smileImages = new HashMap<>();
        smileImages.put("exploding_head.png", "Взрыв головы (эмоджи)");
        smileImages.put("face_with_cowboy_hat.png", "Лицо в ковбойской шляпе (эмоджи)");
        smileImages.put("face_with_monocle.png", "Лицо с моноклем (эмоджи)");
        smileImages.put("grin.png", "Улыбка (эмоджи)");
        smileImages.put("joy.png", "Радость (эмоджи)");
        smileImages.put("nerd_face.png", "Умное лицо (эмоджи)");
        smileImages.put("scream.png", "Кричащее лицо (эмоджи)");
        smileImages.put("star-struck.png", "Звёздные глаза (эмоджи)");
        smileImages.put("sunglasses.png", "Солнцезащитные очки (эмоджи)");
        smileImages.put("upside_down_face.png", "Лицо вверх дном (эмоджи)");
        smileImages.put("face_with_rolling_eyes.png", "Лицо с поднятыми глазами (эмоджи)");
        smileImages.put("breathable_face.png", "Дышащее лицо (эмоджи)");
        smileImages.put("stuck_out_tongue_winking_eye.png", "Лицо с высунутым языком и подмигивающим глазом (эмоджи)");
        smileImages.put("innocent.png", "Невиновный (эмоджи)");
        smileImages.put("money_mouth_face.png", "Денежное лицо (эмоджи)");
        SMILE_IMAGES = Collections.unmodifiableMap(smileImages);

        // Инициализация словаря с изображениями животных
        Map<String, String> animalImages = new HashMap<>();
        animalImages.put("bear.png", "Медведь (эмоджи)");
        animalImages.put("dog.png", "Собака (эмоджи)");
        animalImages.put("frog.png", "Лягушка (эмоджи)");
        animalImages.put("lion.png", "Лев (эмоджи)");
        animalImages.put("monkey.png", "Обезьяна (эмоджи)");
        animalImages.put("panda.png", "Панда (эмоджи)");
        animalImages.put("penguin.png", "Пингвин (эмоджи)");
        animalImages.put("pig.png", "Свинья (эмоджи)");
        animalImages.put("rabbit.png", "Кролик (эмоджи)");
        animalImages.put("tiger.png", "Тигр (эмоджи)");
        animalImages.put("dinosaur.png", "Динозавр (эмоджи)");
        animalImages.put("fox.png", "Лиса (эмоджи)");
        animalImages.put("wolf.png", "Волк (эмоджи)");
        animalImages.put("fish.png", "Рыба (эмоджи)");
        animalImages.put("unicorn.png", "Единорог (эмоджи)");
        ANIMAL_IMAGES = Collections.unmodifiableMap(animalImages);

        // Инициализация словаря с изображениями еды
        Map<String, String> foodImages = new HashMap<>();
        foodImages.put("apple.png", "Яблоко (эмоджи)");
        foodImages.put("avocado.png", "Авокадо (эмоджи)");
        foodImages.put("banana.png", "Банан (эмоджи)");
        foodImages.put("cherries.png", "Вишня (эмоджи)");
        foodImages.put("chocolate_bar.png", "Шоколадный батончик (эмоджи)");
        foodImages.put("coconut.png", "Кокос (эмоджи)");
        foodImages.put("croissant.png", "Круассан (эмоджи)");
        foodImages.put("hamburger.png", "Гамбургер (эмоджи)");
        foodImages.put("peach.png", "Персик (эмоджи)");
        foodImages.put("watermelon.png", "Арбуз (эмоджи)");
        foodImages.put("broccoli.png", "Брокколи (эмоджи)");
        foodImages.put("popcorn.png", "Попкорн (эмоджи)");
        foodImages.put("pepper.png", "Перец (эмоджи)");
        foodImages.put("corn.png", "Кукуруза (эмоджи)");
        foodImages.put("cake.png", "Пирог (эмоджи)");
        FOOD_IMAGES = Collections.unmodifiableMap(foodImages);

        // Инициализация словаря с изображениями спорта
        Map<String, String> sportImages = new HashMap<>();
        sportImages.put("badminton_racquet_and_shuttlecock.png", "Бадминтон (эмоджи)");
        sportImages.put("baseball.png", "Бейсбол (эмоджи)");
        sportImages.put("basketball.png", "Баскетбол (эмоджи)");
        sportImages.put("dart.png", "Дартс (эмоджи)");
        sportImages.put("football.png", "Футбол (эмоджи)");
        sportImages.put("ice_skate.png", "Коньки (эмоджи)");
        sportImages.put("soccer.png", "Соккер (эмоджи)");
        sportImages.put("table_tennis_paddle_and_ball.png", "Настольный теннис (эмоджи)");
        sportImages.put("tennis.png", "Теннис (эмоджи)");
        sportImages.put("volleyball.png", "Волейбол (эмоджи)");
        sportImages.put("ski.png", "Лыжный спорт (эмоджи)");
        sportImages.put("ice_hockey_stick_and_puck.png", "Хоккей на траве (эмоджи)");
        sportImages.put("boxing.png", "Бокс (эмоджи)");
        sportImages.put("fishing.png", "Рыбалка (эмоджи)");
        sportImages.put("golf.png", "Гольф (эмоджи)");
        SPORT_IMAGES = Collections.unmodifiableMap(sportImages);

        // Инициализация словаря с изображениями флагов
        Map<String, String> flagImages = new HashMap<>();
        flagImages.put("Brazil_flag.png", "Флаг Бразилии");
        flagImages.put("Germany_flag.png", "Флаг Германии");
        flagImages.put("Greece_flag.png", "Флаг Греции");
        flagImages.put("Italy_flag.png", "Флаг Италии");
        flagImages.put("Japan_flag.png", "Флаг Японии");
        flagImages.put("Russia_flag.png", "Флаг России");
        flagImages.put("Sweden_flag.png", "Флаг Швеции");
        flagImages.put("Turkey_flag.png", "Флаг Турции");
        flagImages.put("United_Kingdom_flag.png", "Флаг Великобритании");
        flagImages.put("United_States_flag.png", "Флаг Соединённых Штатов");
        flagImages.put("India_flag.png", "Флаг Индии");
        flagImages.put("Nigeria_flag.png", "Флаг Нигерии");
        flagImages.put("France_flag.png", "Флаг Франции");
        flagImages.put("Canada_flag.png", "Флаг Канады");
        flagImages.put("Spain_flag.png", "Флаг Испании");
        FLAG_IMAGES = Collections.unmodifiableMap(flagImages);
    }
}
