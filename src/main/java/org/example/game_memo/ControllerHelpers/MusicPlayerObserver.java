package org.example.game_memo.ControllerHelpers;

import javafx.scene.media.AudioClip;
import org.example.game_memo.Containers.CardGameLogic;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.requireNonNull;

public class MusicPlayerObserver implements GameObserver {
    private static final String BASE_SOUND_PATH = "/Sounds/";

    private final ExecutorService soundExecutor = Executors.newSingleThreadExecutor();
    private final Map<String, AudioClip> soundCache = new ConcurrentHashMap<>();
    private final double volume = 0.3;

    // Предзагрузка звуков (асинхронно)
    public void preloadSounds() {
        Arrays.asList(
                "card-flip.mp3",
                "pair-matched.mp3",
                "pair-mismatched.mp3",
                "update-cards.mp3",
                "toggle-cards.mp3",
                "wow-sound-effect-great.mp3",
                "wow-sound-effect-good.mp3",
                "wow-sound-effect-notBad.mp3"
        ).forEach(file -> soundExecutor.execute(() -> {
            try {
                AudioClip clip = new AudioClip(
                        requireNonNull(getClass().
                                getResource(BASE_SOUND_PATH + file)).toExternalForm()
                );
                soundCache.put(file, clip);
            } catch (Exception e) {
                System.err.println("Ошибка предзагрузки звука: " + file);
            }
        }));
    }

    @Override
    public void update(EventType type, CardGameLogic.CardGameContainer gameData) {
        switch (type) {
            case CARD_FLIPPED -> playSound("card-flip.mp3");
            case PAIR_CARD_MATCHED -> playSound("pair-matched.mp3");
            case PAIR_CARD_MISMATCHED -> playSound("pair-mismatched.mp3");
            case UPDATE_CARDS -> playSound("update-cards.mp3");
            case TOGGLE_CARDS -> playSound("toggle-cards.mp3");
            case GAME_OVER -> playSound(determineMusicFile(gameData.getGameStats()));
        }
    }

    // Асинхронное воспроизведение (в потоке отличном от UI потока)
    private void playSound(String fileName) {
        soundExecutor.execute(() -> {
            AudioClip clip = soundCache.get(fileName);
            if (clip == null) {
                clip = loadSound(fileName);
                if (clip == null) return;
            }
            clip.setVolume(volume);
            clip.play();
        });
    }

    // Загрузка звука
    private AudioClip loadSound(String fileName) {
        try {
            AudioClip clip = new AudioClip(
                    requireNonNull(getClass().getResource(BASE_SOUND_PATH + fileName)).toExternalForm()
            );
            soundCache.put(fileName, clip);
            return clip;
        } catch (Exception e) {
            System.err.println("Ошибка загрузки звука: " + fileName);
            return null;
        }
    }

    // Выбор звука относительно статистики прохождения игры
    private String determineMusicFile(Map<String, String> stats) {
        int matchedPairs = Integer.parseInt(stats.getOrDefault("matchedPairs", "20"));
        int errors = Integer.parseInt(stats.getOrDefault("errors", "2"));
        int cardTurning = Integer.parseInt(stats.getOrDefault("cardTurning", "1"));

        if (errors <= 2 && cardTurning <= 1 && matchedPairs >= 10) {
            return "wow-sound-effect-great.mp3";
        } else if (errors <= 3 && cardTurning <= 2) {
            return "wow-sound-effect-good.mp3";
        }
        return "wow-sound-effect-notBad.mp3";
    }
}
