package org.example.game_memo.CardThings;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FlipAnimation {
    private ImageView imageView;
    private final Image frontImage; // Изображение карты (лицевое)
    private final Image backImage; // Рубашка карты
    private boolean isShowingFront = true; // Флаг текущего состояния (какая сторона видна)
    private final RotateTransition rotateTransition = new RotateTransition(Duration.millis(500));

    public FlipAnimation(ImageView imageView, Image frontImage, Image backImage) {
        this.frontImage = frontImage;
        this.backImage = backImage;
        setImageView(imageView);
        configureRotation();
    }


    // Привязка ImageView к анимации
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
        if (imageView != null) {
            rotateTransition.setNode(imageView); // Привязка анимации вращения к узлу
            imageView.setImage(frontImage); // Установка лицевой стороны карты
            imageView.setRotate(0); // Сброс угла поворота в 0°
        }
    }

    // Поворот карты
    public void play() {
        if (imageView == null || rotateTransition.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        // Определяем направление поворота
        rotateTransition.setFromAngle(isShowingFront ? 0 : 180); // При лицевой стороне: вращение 0° → 180°
        rotateTransition.setToAngle(isShowingFront ? 180 : 0); // При обратной стороне: вращение 180° → 0°
        rotateTransition.playFromStart();
    }

    // Конфигурирует параметры анимации вращения:
    private void configureRotation() {
        rotateTransition.setAxis(Rotate.Y_AXIS); // Ось вращения: Y (вертикальная ось для 3D-эффекта)
        rotateTransition.setOnFinished(e -> {
            isShowingFront = !isShowingFront; // Инвертирует текущее состояние стороны карты
            imageView.setImage(isShowingFront ? frontImage : backImage); // Меняет изображение на противоположное
            imageView.setRotate(0); // Сбрасывает угол поворота
        });
    }

    public boolean isShowingFront() {
        return isShowingFront;
    }

    public ImageView getImageView() {
        return imageView;
    }
}

