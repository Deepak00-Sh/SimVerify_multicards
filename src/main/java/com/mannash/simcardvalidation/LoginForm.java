package com.mannash.simcardvalidation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class LoginForm extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
        FXMLLoader splashLoader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/splash-screen.fxml"));
        Parent splashRoot = splashLoader.load();
        Scene splashScene = new Scene(splashRoot);
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/login-form.fxml"));
        Parent loginRoot = loginLoader.load();
        Scene loginScene = new Scene(loginRoot);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);
        stage.setScene(splashScene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            stage.setScene(splashScene);
            stage.close();
            Stage loginStage = new Stage();
            loginStage.getIcons().add(icon);
            loginStage.setScene(loginScene);
            loginStage.setResizable(false);
            loginStage.setTitle("SIM Verify!");
            loginStage.show();

            loginStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
        }));
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
