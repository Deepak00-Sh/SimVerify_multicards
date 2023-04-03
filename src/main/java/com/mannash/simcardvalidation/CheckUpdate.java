package com.mannash.simcardvalidation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import java.util.Scanner;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUpdate {

    private static final String VERSION_FILE_URL = "http://trakme.mannash.com/trakmeserver/simverify/multiCards/version.txt"; // URL of version file on server
    private static final String JAR_FILE_URL = "http://trakme.mannash.com/trakmeserver/simverify/multiCards/simverify_multicards-1.0-SNAPSHOT.jar";
    private static final String LOCAL_FILE_PATH = "..\\lib\\simverify_multicards-1.0-SNAPSHOT.jar";
    private static final String LOCAL_JAR_DOWNLOAD_PATH = "..\\updates\\simverify_multicards-1.0-SNAPSHOT.jar";
    private String currentVersion;
    private String latestVersion;
    String versionFilePath = "..\\config\\version.txt";

    public String getLatestVersion() throws IOException {
        URL url = new URL(VERSION_FILE_URL);
        Scanner scanner = new Scanner(url.openStream());
        String latestVersion = scanner.nextLine();
        scanner.close();
        return latestVersion;
    }

    public String getCurrentVersion() {
        InputStream inputStream = CheckUpdate.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);

        } catch (IOException e) {
            System.out.println("Application.properties file not found!!");
            e.printStackTrace();
        }
        System.out.println("Version is : " + properties.getProperty("version"));
        return properties.getProperty("version");

    }

    public void showUpdateAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Available");
        ButtonType downloadButton = new ButtonType("Download");
        ButtonType noThanks = new ButtonType("No, thanks", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.setContentText("A new version of the app is available.\n" +
                "Do you want to download and install it now?");
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().setAll(downloadButton, noThanks);
        alert.showAndWait().ifPresent(button -> {
            if (button == downloadButton) {
                downloadJarFile();
            }
        });
    }

    public void downloadUpdatedJarFileOnStart() {
        try {
            // create updates directory if it doesn't exist
            Path updateDir = Paths.get("..\\updates");
            if (!Files.exists(updateDir)) {
                Files.createDirectories(updateDir);
            }
            // download JAR file to updates directory
            Path updateFilePath = Paths.get(LOCAL_JAR_DOWNLOAD_PATH);
            URL url = new URL(JAR_FILE_URL);

            // show download progress dialog
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                Stage stage1 = (Stage) alert.getDialogPane().getScene().getWindow();
                stage1.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                alert.setTitle("Downloading Update");
                alert.setHeaderText("Downloading Update");
                ProgressBar progressBar = new ProgressBar();
                progressBar.setPrefWidth(400);
                progressBar.setProgress(0);
                alert.getDialogPane().setContent(progressBar);
                alert.getButtonTypes().clear();
                DialogPane dialogPane = alert.getDialogPane();
                alert.show();
                // download file in background thread
                Task<Void> downloadTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        URLConnection connection = url.openConnection();
                        int fileSize = connection.getContentLength();
                        InputStream input = connection.getInputStream();
                        OutputStream output = new FileOutputStream(updateFilePath.toFile());
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        long totalBytesRead = 0;
                        Thread.sleep(2000);
                        while ((bytesRead = input.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                            totalBytesRead += bytesRead;
                            updateProgress(totalBytesRead, fileSize);
                        }
                        input.close();
                        output.close();
                        Thread.sleep(1000);
                        return null;
                    }
                };
                downloadTask.setOnSucceeded(event -> {
                    // close progress dialog and show completion message
                    alert.setResult(ButtonType.OK);
                    alert.close();
                    Alert completionAlert = new Alert(Alert.AlertType.INFORMATION);
                    completionAlert.setTitle("Download Complete");
                    Stage stage = (Stage) completionAlert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                    completionAlert.setHeaderText("The update has been downloaded successfully.");
                    completionAlert.setContentText("Please restart the application to apply the update.");
                    completionAlert.showAndWait();
                    // updating new version in config
                    // updateNewVersionInConfig("1.2");
                    // closing application
                    System.exit(0);
                });
                downloadTask.setOnFailed(event -> {
                    // close progress dialog and show error message
                    alert.setResult(ButtonType.CANCEL);
                    alert.close();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Download Failed, try again later!");
                    Stage stage = (Stage) errorAlert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                    errorAlert.setHeaderText("Failed to download the update.");
                    errorAlert.setContentText("Please try again later.");
                    errorAlert.showAndWait();
                });
                progressBar.progressProperty().bind(downloadTask.progressProperty());
                new Thread(downloadTask).start();
            });
        } catch (Exception e) {
            System.out.println("NO UPDATES FOUND ON THE SERVER");
            e.printStackTrace();
        }
    }
    public void downloadOnStart(){
        try {
            // create updates directory if it doesn't exist
            Path updateDir = Paths.get("..\\updates");
            if (!Files.exists(updateDir)) {
                Files.createDirectories(updateDir);
            }
            // download JAR file to updates directory
            Path updateFilePath = Paths.get(LOCAL_JAR_DOWNLOAD_PATH);
            URL url = new URL(JAR_FILE_URL);
            Files.copy(url.openStream(), updateFilePath, StandardCopyOption.REPLACE_EXISTING);
            // show download complete message and close the application
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Download Complete");
//            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
//            alert.setHeaderText("The update has been downloaded.");
//            alert.setContentText("Please restart the application to apply the update.");
//            alert.showAndWait();
//
//            //updating new version in config
////            updateNewVersionInConfig("1.1");
//            //closing application
//            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Download Failed");
//            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
//            alert.setHeaderText("Failed to download the update.");
//            alert.setContentText("Please try again later.");
//            alert.showAndWait();
        }
    }

    public void downloadJarFile() {
        try {
            // create updates directory if it doesn't exist
            Path updateDir = Paths.get("..\\updates");
            if (!Files.exists(updateDir)) {
                Files.createDirectories(updateDir);
            }
            // download JAR file to updates directory
            Path updateFilePath = Paths.get(LOCAL_JAR_DOWNLOAD_PATH);
            URL url = new URL(JAR_FILE_URL);
            Files.copy(url.openStream(), updateFilePath, StandardCopyOption.REPLACE_EXISTING);
            // show download complete message and close the application
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Download Complete");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("The update has been downloaded.");
            alert.setContentText("Please restart the application to apply the update.");
            alert.showAndWait();

            //updating new version in config
//            updateNewVersionInConfig("1.1");
            //closing application
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Download Failed");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
            alert.setHeaderText("Failed to download the update.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }

    private void updateNewVersionInConfig(String newVersion) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(versionFilePath));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                Pattern pattern = Pattern.compile("version:\\s+(\\S+)");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    line = line.replace(matcher.group(1), newVersion); // Replace the version number with the updated version
                }
                builder.append(line).append("\n"); // Append the updated line to the StringBuilder
                line = reader.readLine();
            }
            reader.close();

            // Write the updated content back to the file
            FileWriter writer = new FileWriter(versionFilePath);
            writer.write(builder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}