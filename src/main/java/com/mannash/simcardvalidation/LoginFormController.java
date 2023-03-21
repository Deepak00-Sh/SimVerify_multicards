package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.UserInfo;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationService;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class LoginFormController {
    @FXML
    private Button loginButton;

    @FXML
    private TextField user_input;

    @FXML
    private PasswordField password_input;
    @FXML
    private TextField testTextField;
    @FXML
    private Label promptLabel;
    String userId = null;
    UserInfo userInfo ;

    @FXML
    private void onLoginPress() throws IOException {
//        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
////        Scene primaryScene = loginButton.getScene();
//        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
//        primaryStage.close();
////        primaryStage.setScene(primaryScene);
//        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
//        Stage stage = new Stage();
//        Scene scene = new Scene(mainPage);
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
//        stage.setTitle("SIM Verify!");
//        stage.show();


        TrakmeServerCommunicationService trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
        String userId = user_input.getText();
        String password = password_input.getText();
//        String userId="piyush.srivastava@mannash.com";
//        String password="TMe#2020";

        String hardCodeUserId = "store_user@mannash.com";
        String hardCodePassword = "12345";



        if (userId.equalsIgnoreCase("store1@airtel.in") && password.equalsIgnoreCase(hardCodePassword)){

            Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));
//        Scene primaryScene = loginButton.getScene();
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.close();
//        primaryStage.setScene(primaryScene);
            Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
            Stage stage = new Stage();
            Scene scene = new Scene(mainPage);
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("SIM Verify!");
//            stage.setResizable(false);

//            MenuButton logOut = (MenuButton) scene.lookup("#logOut");
//            logOut.setText(userId);

            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
        }

        else if (userId.equalsIgnoreCase("simlab@airtel.in") && password.equalsIgnoreCase(hardCodePassword)){
            Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));
//        Scene primaryScene = loginButton.getScene();
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();

            primaryStage.close();
//        primaryStage.setScene(primaryScene);
            Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
            Stage stage = new Stage();
            Scene scene = new Scene(mainPage);
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.setTitle("SIM Verify!");
//            stage.setResizable(false);

//            MenuButton logOut = (MenuButton) scene.lookup("#logOut");
//            logOut.setText(userId);
            stage.show();
        }



        else if (userId.equalsIgnoreCase(hardCodeUserId) && password.equalsIgnoreCase(hardCodePassword)){
            Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));
//        Scene primaryScene = loginButton.getScene();
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.close();
//        primaryStage.setScene(primaryScene);
            Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
            Stage stage = new Stage();
            Scene scene = new Scene(mainPage);
            stage.getIcons().add(icon);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("SIM Verify!");
//            stage.setResizable(false);

//            MenuButton logOut = (MenuButton) scene.lookup("#logOut");
//            logOut.setText(userId);
            stage.show();

        } else{
            promptLabel.setTextFill(Color.rgb(255,0,0));
            promptLabel.setText("Invalid username or password!");
        }
        }



//        ResponseAuthenticationPojo responseAuthenticationPojo = trakmeServerCommunicationService.authenticateClient(userId,password);
////        testTextField.setText("response code : "+responseAuthenticationPojo.getStatusCode()+"\n");
////        testTextField.setText("response message : "+responseAuthenticationPojo.getMessage());
//        responseAuthenticationPojo.setUserName(userId);
//
//        if (responseAuthenticationPojo.getStatusCode() != 200) {
//            promptLabel.setTextFill(Color.rgb(255,0,0));
//            promptLabel.setText("Invalid username or password!");
//        } else if(responseAuthenticationPojo.getStatusCode()==200){
//
//            Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
////        Scene primaryScene = loginButton.getScene();
//            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
//            primaryStage.close();
////        primaryStage.setScene(primaryScene);
//            Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
//            Stage stage = new Stage();
//            Scene scene = new Scene(mainPage);
//            stage.getIcons().add(icon);
//            stage.setScene(scene);
//            stage.setTitle("SIM Verify!");
//
////            MenuButton logOut = (MenuButton) scene.lookup("#logOut");
////            logOut.setText(userId);
//            stage.show();

//            TestingController4 testingController4 = new TestingController4(this.userInfo);

//
//        }

//        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
////        Scene primaryScene = loginButton.getScene();
//        Stage primaryStage = (Stage) loginButton.getScene().getWindow();
//        primaryStage.close();
////        primaryStage.setScene(primaryScene);
//        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtel_logo.png");
//        Stage stage = new Stage();
//        Scene scene = new Scene(mainPage);
//        stage.getIcons().add(icon);
//        stage.setScene(scene);
//        stage.setTitle("SIM Verify!");
//        stage.show();

//    }




    public static void main(String[] args) throws IOException {
        LoginFormController controller = new LoginFormController();
        controller.onLoginPress();
    }
}
