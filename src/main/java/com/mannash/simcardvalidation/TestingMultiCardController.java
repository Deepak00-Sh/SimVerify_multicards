package com.mannash.simcardvalidation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class TestingMultiCardController {

    @FXML
    private ProgressBar pb_0_0;
    @FXML
    private ImageView iv_0_0;
    @FXML
    private ImageView iv2_0_0;
    @FXML
    private ImageView iv3_0_0;
    @FXML
    private ImageView iv4_0_0;
    @FXML
    private ImageView iv5_0_0;
    private Timeline timeline;

    private Timeline timeline1;

    private Timeline timeline2;

    private Timeline timeline3;
    private Timeline timeline4;

    public ImageView greenCheck ;
    public Image okImage;
    @FXML
    private void onPressingStartButton() throws InterruptedException {
        greenCheck = new ImageView("/com/mannash/javafxapplication/fxml/images/green_check.png");
        okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
        Thread.sleep(5000);

        timeline = createTimeline(pb_0_0, 67);
        timeline.play();
        timeline1 = new Timeline(5);
        timeline1.setOnFinished(event -> {
            // start the timeline for progressBar2 after progressBar1 finishes

            iv_0_0.setImage(greenCheck.getImage());
        });
        timeline1.play();
    }

    private Timeline createTimeline(ProgressBar progressBar, double durationInSeconds) {
        double updateIntervalInSeconds = 0.1;
        double progressPerInterval = 1.0 / (durationInSeconds / updateIntervalInSeconds);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(updateIntervalInSeconds), event -> {
                    double newProgress = progressBar.getProgress() + progressPerInterval;
                    if (newProgress > 1.0) {
                        newProgress = 1.0;
                    }
                    progressBar.setProgress(newProgress);
                })
        );
        timeline.setCycleCount((int) Math.ceil(durationInSeconds / updateIntervalInSeconds));
        return timeline;
    }
}
