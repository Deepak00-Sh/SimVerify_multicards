package com.mannash.simcardvalidation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginForm extends Application implements Initializable {

    CheckUpdate checkUpdate = new CheckUpdate();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public static String getVersion(){
        InputStream inputStream = LoginForm.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);

        } catch (IOException e) {
            System.out.println("Application.properties file not found!!");
            e.printStackTrace();
        }
        return properties.getProperty("version");
    }

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
            loginStage.setTitle("SIM Verify-v"+getVersion());
            loginStage.show();

            try {
                if (!checkUpdate.getCurrentVersion().equals(checkUpdate.getLatestVersion())) {
                    System.out.println("Updated ");
                    System.out.println("CUrrent version : " + checkUpdate.getCurrentVersion());
                    System.out.println("New Version : " + checkUpdate.getLatestVersion());
//                    checkUpdate.downloadUpdatedJarFileOnStart();
                    checkUpdate.downloadOnStart();
                }

            }catch (Exception e){
                System.out.println("Unable to fetch version from the server , so skipping update on start!!");
            }
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
