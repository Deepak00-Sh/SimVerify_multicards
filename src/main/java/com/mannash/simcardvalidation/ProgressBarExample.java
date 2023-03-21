package com.mannash.simcardvalidation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ProgressBarExample extends Application {

    @FXML
    private ProgressBar ATR_progressBar;

    @FXML
    private ProgressBar FIleVerifyProgressBar;

    @FXML
    private ProgressBar ProfileVerifyProgressBar;

    @FXML
    private ProgressBar testResultCompilation;

    @FXML
    private ProgressBar readWriteProgressBar;

    @FXML
    private ImageView ATRVerificationSucessImage;
    @FXML
    private ImageView FileVerifySuccessImage;

    @FXML
    private ImageView ReadWriteSucessImage;
    @FXML
    private ImageView ProfileVerifySuccessImage;
    @FXML
    private ImageView ok_image;


    @FXML
    private Label compilation_result;
    private Timeline startingTimeline;
    private Timeline timeline1;
    private Timeline timeline2;
    private Timeline timeline3;
    private Timeline timeline4;

    private Timeline timeline5;
    @Override
        public void start(Stage primaryStage) throws IOException {
        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/progress.fxml"));
        Scene scene = new Scene(mainPage);
        primaryStage.setScene(scene);
        primaryStage.show();
        }

        @FXML
        private  void onPressingStart() throws InterruptedException {

            Image greenCheck = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
            Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
            Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");

            Thread.sleep(5000);

            timeline1 = createTimeline(ATR_progressBar, 5.0);
            timeline1.setOnFinished(event -> {
                // start the timeline for progressBar2 after progressBar1 finishes
                ATRVerificationSucessImage.setImage(greenCheck);
                timeline2.play();
            });
            timeline1.play();

            // create a timeline that fills progressBar2 in 10 seconds
            timeline2 = createTimeline(FIleVerifyProgressBar, 10.0);
            timeline2.setOnFinished(event -> {
                // start the timeline for progressBar3 after progressBar2 finishes
                FileVerifySuccessImage.setImage(greenCheck);
                timeline3.play();
            });


            // create a timeline that fills progressBar3 in 12 seconds
            timeline3 = createTimeline(ProfileVerifyProgressBar, 12.0);
            timeline3.setOnFinished(event -> {
                // start the timeline for progressBar4 after progressBar3 finishes
                ProfileVerifySuccessImage.setImage(greenCheck);
                timeline4.play();


            });

//            // create a timeline that fills progressBar4 in 30 seconds

            timeline4 = createTimeline(readWriteProgressBar, 30.0);
            timeline4.setOnFinished(event -> {
                // start the timeline for progressBar4 after progressBar3 finishes
                ReadWriteSucessImage.setImage(greenCheck);
                timeline5.play();

            });
            timeline5= createTimeline(testResultCompilation, 10.0);

            timeline5.setOnFinished(event -> {
                compilation_result.setText("Test Compilation Result");
                ok_image.setImage(okImage);

            });

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
    public static void main(String[] args) {
        launch(args);
    }
    }


