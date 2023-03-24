package com.mannash.simcardvalidation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class CheckUpdate {

    private static final String VERSION_FILE_URL = "https://trakme.mannash.com/version.txt"; // URL of version file on server
    private static final String JAR_FILE_URL = "https://trakme.mannash.com/app.jar"; // URL of jar file on server
    private static final String LOCAL_FILE_PATH = "C:\\path\\to\\app.jar"; // Local file path of jar file
    private String currentVersion;
    private String latestVersion;

    public String getLatestVersion() throws IOException {
        URL url = new URL(VERSION_FILE_URL);
        Scanner scanner = new Scanner(url.openStream());
        String latestVersion = scanner.nextLine();
        scanner.close();
        return latestVersion;
    }


    private void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Available");
        alert.setHeaderText("A new version of the app is available.");
        alert.setContentText("Do you want to download and install it now?");
        ButtonType downloadButton = new ButtonType("Download");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(downloadButton, cancelButton);
        alert.showAndWait().ifPresent(button -> {
            if (button == downloadButton) {
                downloadJarFile();
            }
        });
    }

    private void downloadJarFile() {
        try {
            URL url = new URL(JAR_FILE_URL);
            Path path = Paths.get(LOCAL_FILE_PATH);
            Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Complete");
            alert.setHeaderText("The update has been downloaded.");
            alert.setContentText("Please restart the application to apply the update.");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            alert.setHeaderText("Failed to download the update.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
}