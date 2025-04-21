package org.example.game_memo;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MusicPlayer {
    private final ErrorBox errorBox = new ErrorBox(); // Класс для отображения ошибок
    private static final String BASE_SOUND_PATH = "sounds/";
    private MediaPlayer currentPlayer;  // Текущий медиаплеер для управления воспроизведением


    // Метод для выбора и воспроизведения музыки на основе статистики игры.
    public void selectAndPlayMusic(Map<String, String> gameStats) {
        stopCurrentPlayback(); // Останавливаем текущее воспроизведение
        String musicFile = determineMusicFile(gameStats); // Определяем файл
        playMusicFile(musicFile); // Воспроизводим выбранный файл
    }

    // Останавливание текущего воспроизведения звукового файла и освобождения ресурсов.
    public void stopCurrentPlayback() {
        if (currentPlayer != null) {
            currentPlayer.stop(); // Останавливаем воспроизведение
            currentPlayer.dispose(); // Освобождаем ресурсы
        }
    }

    // Определение звукового файла для воспроизведения на основе статистики сыгранной игры.
    private String determineMusicFile(Map<String, String> stats) {
        int matchedPairs = Integer.parseInt(stats.getOrDefault("matchedPairs", "20"));
        int errors = Integer.parseInt(stats.getOrDefault("errors", "2"));
        int cardTurning = Integer.parseInt(stats.getOrDefault("cardTurning", "1"));

        // Логика выбора звукового эффекта
        if (errors <= 2 && cardTurning <= 1 && matchedPairs >= 10) {
            return "wow-sound-effect-great.mp3";
        }
        else if (errors <= 2 && cardTurning <= 2) {
            return "wow-sound-effect-good.mp3";
        }
        return "wow-sound-effect-notBad.mp3";
    }

    // Воспроизведение указанного звукового файла
    private void playMusicFile(String fileName) {
        try {
            // Создаем Media объект из файла (с проверкой на null)
            Media sound = new Media(requireNonNull(getClass().getResource(BASE_SOUND_PATH + fileName)).toString());
            currentPlayer = new MediaPlayer(sound);

            currentPlayer.setVolume(0.3); // Устанавливаем громкость 30%
            currentPlayer.setCycleCount(1); // Воспроизведение один раз
            currentPlayer.play(); // Запуск воспроизведения

            // Обработчик завершения воспроизведения
            currentPlayer.setOnEndOfMedia(() -> {
                currentPlayer.dispose(); // Освобождаем ресурсы
                currentPlayer = null; // Обнуляем ссылку
            });

        } catch (Exception e) {
            e.printStackTrace();
            errorBox.show("Ошибка воспроизведения звука:" + e.getMessage());

            // Освобождаем ресурсы при ошибке
            if (currentPlayer != null) {
                currentPlayer.dispose();
            }
        }
    }
}
