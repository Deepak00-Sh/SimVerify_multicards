package com.mannash.simcardvalidation;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class DemoMain extends Application {

    @FXML
    private Rectangle  myRectangle;

    @FXML
    private ImageView startButton;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader testingLoader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing_multiCard.fxml"));
        Parent testLoader = testingLoader.load();
        Scene testScene = new Scene(testLoader, 800,600);
        stage.setScene(testScene);
        stage.setMaximized(true);
        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
