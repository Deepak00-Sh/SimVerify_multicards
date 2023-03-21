package com.mannash.simcardvalidation;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TestingController {

    @FXML private ImageView img_test_status_1;
    @FXML private ImageView img_test_status_2;
    @FXML private ImageView img_test_status_3;
    @FXML private ImageView img_test_status_4;
    @FXML private ImageView img_test_status_5;
    @FXML private ImageView img_test_status;
    @FXML private ImageView img_test_button;
    @FXML
    private TextField input_imsi;
    @FXML private ImageView img_reset_button;
    @FXML private TextField input_iccid;

    @FXML
    private Label status_label;
    @FXML private ImageView img_status;

    @FXML
    private TextArea logTextArea;

    @FXML
    private AnchorPane mainAnchorPane;

    private LocalDateTime dateTime ;
    public int counter = 0;

    @FXML
    private VBox simCardVbox;


    private void removeImageAndAddlogArea(){
        mainAnchorPane.getChildren().remove(img_status);
        TextArea logsArea = new TextArea();
        logsArea.setPrefSize(simCardVbox.getPrefWidth(), simCardVbox.getPrefHeight());
        ScrollPane pane = new ScrollPane(logTextArea);
        pane.setFitToWidth(true);
        pane.setFitToHeight(true);
        simCardVbox.getChildren().add(pane);
    }
    public void changeImage() throws InterruptedException, IOException {

        if(img_test_button.getImage().getUrl().contains("startTest.gif")) {
                Image newImage = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
                Image cancelButton = new Image("/com/mannash/javafxapplication/fxml/images/cancel Image.jpg");
                status_label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                status_label.setTextFill(Color.web("#16FF00"));

                img_test_button.setImage(cancelButton);
                dateTime = LocalDateTime.now();
                TextArea logTextArea = new TextArea();
                logTextArea.setPrefSize(simCardVbox.getPrefWidth(), simCardVbox.getPrefHeight());
                logTextArea.positionCaret(logTextArea.getLength());
                logTextArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                logTextArea.viewOrderProperty();
                logTextArea.getStyleClass().add("text-area-left-aligned");
                ScrollPane pane = new ScrollPane(logTextArea);
                pane.setFitToWidth(true);
                pane.setFitToHeight(true);
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
                    simCardVbox.getChildren().remove(img_status)),new KeyFrame(Duration.seconds(5), event ->
                    simCardVbox.getChildren().add(pane)),new KeyFrame(Duration.seconds(5), event ->
                        status_label.setText("TESTING CARD...")),
                        //setting testing logs
                        new KeyFrame(Duration.seconds(5), event ->
                                logTextArea.appendText(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " : reading ICCID and IMSI from the card \n")),
                    new KeyFrame(Duration.seconds(10), event ->
                        dateTime =  LocalDateTime.now()),
                        new KeyFrame(Duration.seconds(10), event ->
                                logTextArea.appendText(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " :ICCID : 8991000904448610811F \n IMSI : 404104448610811 \n")),
                        new KeyFrame(Duration.seconds(10), event ->
                        input_iccid.setText("8991000904448610811F")),new KeyFrame(Duration.seconds(10), event ->
                        input_iccid.setEditable(false)),new KeyFrame(Duration.seconds(10), event ->
                        input_iccid.setEditable(false)),new KeyFrame(Duration.seconds(10), event ->
                        input_imsi.setText("404104448610811")), new KeyFrame(Duration.seconds(15), event ->
                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_status_2.setImage(newImage)), new KeyFrame(Duration.seconds(37), event ->
                        img_test_status_3.setImage(newImage)), new KeyFrame(Duration.seconds(67), event ->
                        img_test_status_4.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
                        img_test_status_5.setImage(newImage)),new KeyFrame(Duration.seconds(78), event ->
                        simCardVbox.getChildren().add(img_status)),new KeyFrame(Duration.seconds(78), event ->
                        simCardVbox.getChildren().remove(pane)),new KeyFrame(Duration.seconds(78), event ->
                        status_label.setText("")),new KeyFrame(Duration.seconds(78), event ->
                        img_status.setImage(okImage)),new KeyFrame(Duration.seconds(78), event ->
                        img_test_button.setImage(doneButton)));
                timeline.play();
        } else if (img_test_button.getImage().getUrl().contains("cancel Image.jpg")) {
            status_label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            status_label.setTextFill(Color.web("#FF0303"));
            status_label.setText("CANCELLED!");
            loadTestingWindow();


        } else{
            loadTestingWindow();
        }
    }
    public void loadTestingWindow() throws IOException {
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(testingPage,850,550);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
