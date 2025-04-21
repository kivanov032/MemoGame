package org.example.game_memo;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FlipAnimation {
    private ImageView imageView;
    private final Image frontImage;
    private final Image backImage;
    private final RotateTransition rotateTransition;
    private boolean isShowingFront = true;

    public boolean isShowingFront() {
        return isShowingFront;
    }

    public FlipAnimation(ImageView imageView, Image frontImage, Image backImage) {
        this.rotateTransition = new RotateTransition(Duration.millis(500));
        this.frontImage = frontImage;
        this.backImage = backImage;
        setImageView(imageView);
        configureRotation();
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
        if (imageView != null) {
            rotateTransition.setNode(imageView);
            imageView.setImage(frontImage);
            imageView.setRotate(0);
        }
    }

    public void play() {
        if (imageView == null || rotateTransition.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        // Определяем направление поворота
        rotateTransition.setFromAngle(isShowingFront ? 0 : 180);
        rotateTransition.setToAngle(isShowingFront ? 180 : 0);
        rotateTransition.playFromStart();
    }

    private void configureRotation() {
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setOnFinished(e -> {
            isShowingFront = !isShowingFront;
            imageView.setImage(isShowingFront ? frontImage : backImage);
            imageView.setRotate(0);
        });
    }

    public void reset() {
        rotateTransition.stop();
        imageView.setRotate(0);
        imageView.setImage(frontImage);
        isShowingFront = true;
    }
}

