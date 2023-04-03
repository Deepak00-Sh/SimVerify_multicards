package com.mannash.simcardvalidation;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import java.awt.*;

public class SimVerifyPopUpWindowController {

    public String getWidgetID() {
        return widgetID;
    }

    public void setWidgetID(String widgetID) {
        this.widgetID = widgetID;
    }

    @FXML
    private Label label_stage1;

    @FXML
    private Label label_stage2;

    @FXML
    private Label label_stage3;

    @FXML
    private Label label_stage4;


    @FXML
    private Label TestingStatusLabel;


    @FXML
    private Rectangle stage1_rectBox;

    @FXML
    private Rectangle stage2_rectBox;

    @FXML
    private Rectangle stage3_rectBox;

    @FXML
    private Rectangle stage4_rectBox;



    private String widgetID;
    @FXML
    private Label popup_iccid;

    @FXML
    private Label popup_imsi;

    @FXML
    private ImageView popUpWindowStatusImage;


    @FXML
    private ImageView stage1_image;

    @FXML
    private ImageView stage2_image;

    @FXML
    private ImageView stage3_image;

    @FXML
    private ImageView stage4_image;
    //right image
    Image rightImage = new Image("/com/mannash/javafxapplication/fxml/images/correct.png");
    Image crossImage = new Image("/com/mannash/javafxapplication/fxml/images/cross.png");
//    Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
    Image notOkImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");


    public Label getPopup_iccid() {
        return popup_iccid;
    }

    public void setPopup_iccid(String iccid) {
        Platform.runLater(() ->{
            this.popup_iccid.setText(iccid);
        });

    }

    public Label getPopup_imsi() {
        return popup_imsi;
    }

    public void setPopup_imsi(String imsi) {
        Platform.runLater(() ->{
            this.popup_imsi.setText(imsi);
        });

    }

    public ImageView getPopUpWindowStatusImage() {
        return popUpWindowStatusImage;
    }





    public void setLabelColor(){
        this.label_stage1.setTextFill(Color.BLACK);
        this.label_stage2.setTextFill(Color.BLACK);
        this.label_stage3.setTextFill(Color.BLACK);
        this.label_stage4.setTextFill(Color.BLACK);


    }

    public void setSIMHeartBeatStatusImage(Boolean isSuccessful){
        if (isSuccessful) {
            Platform.runLater(() ->{
                this.stage1_image.setImage(rightImage);
                this.stage1_rectBox.setStyle("-fx-fill: green;");
            });

        } else {
            Platform.runLater(() ->{
                this.stage1_rectBox.setStyle("-fx-fill: red;");
                this.stage2_rectBox.setStyle("-fx-fill: red;");
                this.stage3_rectBox.setStyle("-fx-fill: red;");
                this.stage4_rectBox.setStyle("-fx-fill: red;");
                this.stage1_image.setImage(crossImage);
                this.stage2_image.setImage(crossImage);
                this.stage3_image.setImage(crossImage);
                this.stage4_image.setImage(crossImage);

                this.popUpWindowStatusImage.setImage(notOkImage);

            });

        }
    }

    public void setTestingStatusLabel(String status){
        Platform.runLater(() ->{
            if(status.equals("OK")) {
                this.popUpWindowStatusImage.setImage(new Image("/com/mannash/javafxapplication/fxml/images/correct.png"));
                this.TestingStatusLabel.setText("Card is OK");
            }else{
                this.popUpWindowStatusImage.setImage(new Image("/com/mannash/javafxapplication/fxml/images/cross.png"));
                this.TestingStatusLabel.setText("Card is Faulty");

            }
        });

    }
    public void setFileSystemVerificationStatusImage(Boolean isSuccessful){
        if (isSuccessful) {
            Platform.runLater(() ->{
                this.stage2_image.setImage(rightImage);
                this.stage2_rectBox.setStyle("-fx-fill: green;");
            });

        } else {
            Platform.runLater(() ->{
                this.stage2_rectBox.setStyle("-fx-fill: red;");
                this.stage3_rectBox.setStyle("-fx-fill: red;");
                this.stage4_rectBox.setStyle("-fx-fill: red;");
                this.stage2_image.setImage(crossImage);
                this.stage3_image.setImage(crossImage);
                this.stage4_image.setImage(crossImage);
            });
        }
    }
    public void setProfileVerificationStatusImage(Boolean isSuccessful){
        if (isSuccessful) {
            Platform.runLater(() ->{
                this.stage3_rectBox.setStyle("-fx-fill: green;");
                this.stage3_image.setImage(rightImage);
            });

        } else {
            Platform.runLater(() ->{
                this.stage3_rectBox.setStyle("-fx-fill: red;");
                this.stage4_rectBox.setStyle("-fx-fill: red;");
                this.stage3_image.setImage(crossImage);
                this.stage4_image.setImage(crossImage);
            });

        }
    }
    public void setReadWriteStatusImage(Boolean isSuccessful){
        if (isSuccessful) {
            Platform.runLater(() ->{
                this.stage4_rectBox.setStyle("-fx-fill: green;");
                this.stage4_image.setImage(rightImage);

            });

        } else {
            Platform.runLater(() ->{
                this.stage4_rectBox.setStyle("-fx-fill: red;");
                this.stage4_image.setImage(crossImage);
            });

        }
    }

}
