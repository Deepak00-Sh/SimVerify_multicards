package com.mannash.simcardvalidation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class CheckUpdate {

    private static final String VERSION_FILE_URL = "https://example.com/version.txt"; // URL of version file on server
    private static final String JAR_FILE_URL = "https://example.com/app.jar"; // URL of jar file on server
    private static final String LOCAL_FILE_PATH = "..\\lib\\simverify_multicards-1.0.jar"; // Local file path of jar file
    private String currentVersion;
    private String latestVersion;

    public String getLatestVersion() throws IOException {
        URL url = new URL(VERSION_FILE_URL);
        Scanner scanner = new Scanner(url.openStream());
        String latestVersion = scanner.nextLine();
        scanner.close();
        return latestVersion;
    }

    public String getCurrentVersion(){
        JarFile jarFile = null;
        Manifest manifest = null;
        try {
            jarFile = new JarFile(LOCAL_FILE_PATH);
            manifest = jarFile.getManifest();
        } catch (IOException e) {
            System.err.println("NO APPLICATION JAR FILE FOUND!!");
//            e.printStackTrace();
        }

        Attributes attributes = manifest.getMainAttributes();
        String version = attributes.getValue("Manifest-Version");
        System.out.println("Version: " + version);

        try {
            jarFile.close();

        } catch (IOException e) {
            System.err.println("NO JAR FILE TO CLOSE!!");
//            e.printStackTrace();
        }


        return version;
    }


    public void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Available");
        ButtonType downloadButton = new ButtonType("Download");
        ButtonType noThanks = new ButtonType("No, thanks",ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.setContentText("A new version of the app is available.\n" +
                "Do you want to download and install it now?");
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().setAll(downloadButton,noThanks);
        alert.showAndWait().ifPresent(button -> {
            if (button == downloadButton) {
                downloadJarFile();
            }
        });
    }

    public void downloadJarFile() {
        try {
            URL url = new URL(JAR_FILE_URL);
            Path path = Paths.get(LOCAL_FILE_PATH);
            Files.copy(url.openStream(), path, StandardCopyOption.REPLACE_EXISTING);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Complete");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("The update has been downloaded.");
            alert.setContentText("Please restart the application to apply the update.");
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("Failed to download the update.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
}