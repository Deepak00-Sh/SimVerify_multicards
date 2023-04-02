package com.mannash.simcardvalidation;

//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.mannash.simcardvalidation.pojo.*;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class SimVerifyMasterThread2 implements Initializable {

    SimVerifyLoggerThread loggerThread;
    public static String loggedInUserName;

    //FXML elements
    @FXML
    private TextField user_input;
    @FXML
    private PasswordField password_input;
    @FXML
    private Label promptLabel;
    @FXML
    private Button loginButton;
    @FXML
    private GridPane mainGridPane;
    @FXML
    private VBox simCardVbox;

    @FXML
    private StackPane rootPane;

    @FXML
    private Label exportMessage;

    @FXML
    private ImageView startTestingButton;

    @FXML
    private static ListView cardsConnectedList;

    @FXML
    private ImageView exportIcon;

    Boolean isAnyCardConnected = false;

    //runtime elements
    ScrollPane pane;
    //Image Views
    Image cancelButton = new Image("/com/mannash/javafxapplication/fxml/images/TestingInProgressGif.gif");
    Image processingImage = new Image("/com/mannash/javafxapplication/fxml/images/loading14.gif");
    Image doneImage = new Image("/com/mannash/javafxapplication/fxml/images/done4.gif");
    Image questionMarkImage = new Image("/com/mannash/javafxapplication/fxml/images/question-mark.png");
    Image startButtonImage = new Image("/com/mannash/javafxapplication/fxml/images/button_Start_Testing.png");

    //Indicator Images to listView
//  Image redIndicatorImage = new Image("/com/mannash/javafxapplication/fxml/images/button_cancel.png");
    Image greenIndicatorImage = new Image("/com/mannash/javafxapplication/fxml/images/greenIndicator.gif");
    Image yellowIndicatorImage = new Image("/com/mannash/javafxapplication/fxml/images/yellow (1).gif");
    Image exportButtonImage = new Image("/com/mannash/javafxapplication/fxml/images/export.png");
    //LogtextArea
    TextArea logTextArea = new TextArea();
    UserName userNamePojo = new UserName();

    //Task running
    private Task<Boolean> task1;

    //global variables for widgets
    public static int numRows = 4;
    public static int numCols = 4;
    public static int elementRow = 0;
    public static int elementColumn = 0;
    public String _terminal = "T";
    public String _card = "C";
    public String _device = "D";
    public String _ui = "UI";
    PauseTransition pause = new PauseTransition(Duration.seconds(0));
    StackPane[] cardWidget = new StackPane[16];
    //private Stage primaryTestingStage = null;
    int cardConnectedCounter = 0;
    int finishedThreads = 0;
    int initializedThreads = 0;
    int terminalsConnected = 0;
    Boolean logTextAreaInitialize = false;
    //Thread for on start button Press
    Thread thread1;
    private List<Future<?>> futureList = new ArrayList<>();
    List<TestingController4> controller4ThreadList = new ArrayList<>();
    Thread[] threads = new Thread[Thread.activeCount()];
    private boolean headersPrinted = false;

    public static List<ExportTestingResultPojo> cardTestingPojosList = new ArrayList<ExportTestingResultPojo>();


    ExportTestingResultPojo exportTestingResultPojo = new ExportTestingResultPojo();
    private Object csvLock = new Object();

    private ExecutorService executorService;
    private static final String CACHE_FILE_PATH = "..\\reports\\cache\\";
    private Cache<String, Object> cache;

    int widgetID = 0;

    SimVerifyPopUpWindowController popUpController;

    int widgetIdSeq[];


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize function");
        try {
            File widgetSequence = new File("..\\config\\widgetSequence.ini");
            BufferedReader reader = new BufferedReader(new FileReader(widgetSequence));
            String line = reader.readLine();

            if (line != null) {
                System.out.println("ArrayLine : " + line);
                String widgetSequenceArray[] = line.split(",");
                widgetIdSeq = new int[widgetSequenceArray.length];
                for (int i = 0; i < widgetSequenceArray.length; i++) {
                    try {
                        widgetIdSeq[i] = Integer.parseInt(widgetSequenceArray[i].trim());
                    } catch (Exception e) {

                    }
                }
            } else {
                widgetIdSeq = new int[]{1, 2, 3, 4, 5, 6, 7};
            }

            for (int i = 0; i < widgetIdSeq.length; i++) {
                System.out.println("Printing array : ");
                System.out.println(widgetIdSeq[i]);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//
    }


    public SimVerifyMasterThread2() {
        // Initialize the cache with a maximum size of 1000 and a time-to-live of 10 minutes
        cache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES).build();
    }

    @FXML
    public void onLoginButtonPress() throws IOException {
//      TrakmeServerCommunicationService trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
        TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
        String userId = user_input.getText();
        String password = password_input.getText();
        String hardCodeUserId = "a";
        String hardCodePassword = "a";
        SimVerifyMasterThread2.loggedInUserName = userId;
        ResponseAuthenticationPojo responseAuthenticationPojo = trakmeServerCommunicationService.authenticateClient(userId, password);

        System.out.println("Response code  : " + responseAuthenticationPojo.getStatusCode());

        if (responseAuthenticationPojo.getStatusCode() != 200) {
            promptLabel.setTextFill(Color.rgb(255, 0, 0));
            promptLabel.setText("Invalid username or password!");
        } else {
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.close();
            try {
                writeConfigFile("8991526200009320025F", "1160", userId);
            } catch (Exception e) {
                System.out.println("Server not responding for writeing config");
//                e.printStackTrace();
            }

//            writeConfigFile("8991526200009320025F", "1160", userId);
            loadTestingWindowData();
            startTransferDataThread(SimVerifyMasterThread2.loggedInUserName);
        }

    }

    private void startTransferDataThread(String loggedInUserName) {
        SimVerifyTransferDataToServerThread transferDataThread = new SimVerifyTransferDataToServerThread(loggedInUserName);
        transferDataThread.start();
    }

    private void writeConfigFile(String terminalCardICCID, String woId2, String userName) throws IOException {
        TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();

        ResponseProfileTestingConfig serverResponse = trakmeServerCommunicationService.getProfileTestingConfig(terminalCardICCID, woId2, userName);

        ResponseStressTestingConfig serverResponse2 = trakmeServerCommunicationService.getStressTestingConfig(terminalCardICCID, woId2, userName);


//        System.out.println(serverResponse.getApduList().toString());
        System.out.println(serverResponse.toString());
        File config = new File("..\\config\\config.txt");
        if (!config.exists()) {
            config.createNewFile();
        }

//        JSONObject response = new JSONObject();
//        try {
//            try {
//                response.put("fileSystemConfig",serverResponse.getFileSystemConfig());
//                response.put("fileContentConfig",serverResponse.getFileContentConfig());
//                response.put("fileVerificationSystemConfig",serverResponse.getFileVerificationSystemConfig());
//                response.put("fileVerificationContentConfig",serverResponse.getFileVerificationContentConfig());
//                response.put("apduList",serverResponse.getApduList());
//                response.put("loopCount",serverResponse.getLoopCount());
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        try (FileWriter file = new FileWriter("..\\config\\config.txt")) {
//            file.write(response.toString());
//            file.flush();
//
//            System.out.println("Response written to response.txt");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            FileWriter fileWriter = new FileWriter(config);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
//        bufferedWriter.write("{\n\"fileSystemConfig\": " +serverResponse.getFileSystemConfig()+",\n\"fileContentConfig\": "+
//                        serverResponse.getFileContentConfig()+",\n\"fileVerificationSystemConfig\": "+
//                        serverResponse.getFileVerificationSystemConfig()+",\n\"fileVerificationContentConfig\": "+
//                        serverResponse.getFileVerificationContentConfig()+",\n\"apduList\": "+
//                        serverResponse.getApduList()+",\n\"loopCount\": "+
//                        serverResponse.getLoopCount()
//                +"\n}");

            bufferedWriter.write("{\n\"fileSystemConfig\": " + serverResponse.getFileSystemConfig() + ",\n\"fileContentConfig\": " + serverResponse.getFileContentConfig() + ",\n\"fileVerificationSystemConfig\": " + serverResponse2.getApduList() + ",\n\"loopCount\": " + serverResponse2.getLoopCount() + "\n}");

            bufferedWriter.close();
            System.out.println("Successfully wrote to file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    //keyboard handling of loginscreen
    @FXML
    private void handleKeyForPassword(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            loginButton.fire();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB && event.isShiftDown()) {
            event.consume();
            user_input.requestFocus();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
            event.consume();
            loginButton.requestFocus();
        }
    }

    @FXML
    private void handleKeyForUserInput(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            loginButton.fire();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB && event.isShiftDown()) {
            event.consume();
            loginButton.requestFocus();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
            event.consume();
            password_input.requestFocus();
        }
    }

    @FXML
    private void handleKeyForLoginButton(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
            loginButton.fire();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB && event.isShiftDown()) {
            event.consume();
            password_input.requestFocus();
        } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
            event.consume();
            user_input.requestFocus();
        }
    }

    public void loadTestingWindowData() throws IOException {


        System.out.println("#2 loading testing window data: " + loggedInUserName);
        SimVerifyMasterThread2.elementRow = 0;
        SimVerifyMasterThread2.elementColumn = 0;
        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));
        Stage primaryStage = new Stage();
        Scene scene = new Scene(mainPage);
        primaryStage.setTitle("SIM Verify!");
        primaryStage.getIcons().add(icon);
        primaryStage.setMaximized(true);
//        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        for (int i = 0; i < widgetIdSeq.length; i++) {
            double delay = 0.05;
            int id = widgetIdSeq[i] - 1;
            int childId = i;
            PauseTransition pause = new PauseTransition(Duration.seconds(delay * i));
            pause.setOnFinished(event -> {
                addCardWidget(id, childId);
//                this.cardsConnectedList.getItems().add(childId, "-");
            });
            pause.play();
        }
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        GridPane gridPane = (GridPane) scene.lookup("#mainGridPane");
        VBox root = (VBox) scene.lookup("#cardsConnectedVbox");
        this.mainGridPane = gridPane;
        mainGridPane.setHgap(10);
        mainGridPane.setVgap(10);
        ListView<String> cardsConnectedList = (ListView<String>) scene.lookup("#cardsConnectedList");
        cardsConnectedList.prefHeightProperty().bind(root.heightProperty());
        cardsConnectedList.maxHeight(Double.MAX_VALUE);
        cardsConnectedList.setMinHeight(primaryStage.getMinHeight());
        this.cardsConnectedList = cardsConnectedList;
        Image backgroundImage = new Image("/com/mannash/javafxapplication/fxml/images/airtel-logo-f.png");
        BackgroundSize backgroundSize = new BackgroundSize(700, 500, false, false, false, false);
        if (backgroundImage.isError()) {
            System.out.println("Error loading background image: " + backgroundImage.getException().getMessage());
        }
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        mainGridPane.setBackground(new Background(background));

        for (int i = 0; i < widgetIdSeq.length; i++) {
            this.cardsConnectedList.getItems().add(i, "");
        }
    }

    public void reLoadTestingWindowData() {
        logTextAreaInitialize = true;

        for (int i = 0; i < cardsConnectedList.getItems().size(); i++) {
            cardsConnectedList.getItems().set(i, "");
        }
        initializedThreads = 0;
        finishedThreads = 0;

        logTextArea.setText("");

        for (int i = 0; i < widgetIdSeq.length; i++) {
            int id = widgetIdSeq[i] - 1;
            reLoadCardWidget(id);
        }

        exportMessage.setVisible(false);
        exportIcon.setVisible(false);
        setStartButton();
    }

    public void onWidgetClick() {
        try {
            System.out.println("ON WIDGET CLICK !!!");
            FXMLLoader popUpWindowloader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/MultiCardTesting.fxml"));
            Parent popUproot = popUpWindowloader.load();
            this.popUpController = popUpWindowloader.getController();
            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(rootPane.getScene().getWindow());
            popupStage.initStyle(StageStyle.UNDECORATED);// remove the status bar;


            popupStage.setResizable(false); // prevent resizing
            System.out.println("WIDGET ID : " + rootPane.getId());


            // Make the parent screen behind this window blurry
            BoxBlur blur = new BoxBlur();
            blur.setWidth(5);
            blur.setHeight(5);
            blur.setIterations(3);
            Scene gridPane = rootPane.getScene();
            String widgetID = rootPane.getId();


            //main BorderPane
            BorderPane borderPane = (BorderPane) gridPane.lookup("#borderPane");
            Scene scene = borderPane.getScene();
            scene.setFill(Color.BLACK);


            //getting data from the widget
            StackPane widget = (StackPane) gridPane.lookup(widgetID);
            Label iccid = (Label) rootPane.lookup("#iccid_value_label");
            Label imisi = (Label) rootPane.lookup("#imsi_value_label");
            this.popUpController.setLabelColor();
            for (ExportTestingResultPojo testingResultPojo : cardTestingPojosList) {
                if (testingResultPojo.getTerminalICCID().equals(iccid.getText())) {
                    this.popUpController.setTestingStatusLabel(testingResultPojo.getCardStatus());
                    if (testingResultPojo.getSIMHeartbeat().equals("OK")) {
                        this.popUpController.setSIMHeartBeatStatusImage(true);
                    } else {
                        this.popUpController.setSIMHeartBeatStatusImage(false);
                        break;
                    }
                    if (testingResultPojo.getFileSystemVerification().equals("OK")) {
                        this.popUpController.setFileSystemVerificationStatusImage(true);
                    } else {
                        this.popUpController.setFileSystemVerificationStatusImage(false);
                        break;
                    }
                    if (testingResultPojo.getProfileTesting().equals("OK")) {
                        this.popUpController.setProfileVerificationStatusImage(true);
                    } else {
                        this.popUpController.setProfileVerificationStatusImage(false);
                        break;
                    }
                    if (testingResultPojo.getReadWrite().equals("OK")) {
                        this.popUpController.setReadWriteStatusImage(true);
                    } else {
                        this.popUpController.setReadWriteStatusImage(false);
                        break;
                    }
                }
            }
            borderPane.setEffect(blur);
            borderPane.setOpacity(0.6);
            //Setting data to the popUp
            this.popUpController.setPopup_iccid(iccid.getText());
            this.popUpController.setPopup_imsi(imisi.getText());
            Button closeButton = new Button("X");
            closeButton.setFocusTraversable(false);
            closeButton.setStyle("-fx-background-color: lightgrey;");
            closeButton.setCursor(Cursor.HAND);
            closeButton.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.EMPTY)));
            closeButton.setOnAction(event -> {
                ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.2), popUproot);
                scaleOut.setToX(0);
                scaleOut.setToY(0);
                scaleOut.setOnFinished(e -> {
                    popupStage.close();
                    borderPane.setEffect(null);
                    borderPane.setOpacity(1);
                    scene.setFill(null);
                });
                scaleOut.play();
            });
            closeButton.getStyleClass().add("close-button"); // add a style class to customize the appearance
            VBox header = new VBox();
            header.getChildren().add(closeButton);
            header.setAlignment(Pos.TOP_RIGHT);
            ((StackPane) popUproot).getChildren().add(header);
            popupStage.setScene(new Scene(popUproot));
            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onStartButtonPress() {
        System.out.println("#2 onstart button press : " + userNamePojo.getLoggedInUserName());

        if (startTestingButton.getImage().getUrl().contains("button_Start_Testing.png")) {
            cardTestingPojosList.clear();
            TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(this.loggerThread, this);
            int numberOfTerminal = terminalConnectService.fetchTerminalCount();

            System.out.println("Number of terminal connected : " + numberOfTerminal);

            startingLogTextArea();
            task1 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("From task1");
                    for (int i = 0; i < numberOfTerminal; i++) {
                        if (i == widgetIdSeq.length) {
                            break;
                        }
                        System.out.println("For loop count : " + i);
                        int a = i;
                        updateStackPaneData(a, "-", "-");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                    return true;
                }
            };
            task1.setOnSucceeded(event1 -> {
                System.out.println(task1.getValue());

                System.out.println("Task 1 Succeeded");
                initializeTestingThreads();
            });
            this.thread1 = new Thread(task1);
            this.thread1.start();
            System.out.println("Task 1 finished");
        } else if (startTestingButton.getImage().getUrl().contains("done")) {
            reLoadTestingWindowData();
        }
    }

    public void setDoneButton() {
        startTestingButton.setImage(doneImage);
    }

    public void setStartButton() {
        startTestingButton.setImage(startButtonImage);
    }

    public void setExportButton() {
        Platform.runLater(() -> {
            exportIcon.setVisible(true);
            exportIcon.setDisable(false);
            exportIcon.setImage(exportButtonImage);
        });
    }

    public void updateStackPaneData(int index, String iccid, String imsi) {
        try {

            Node node = mainGridPane.getChildren().get(widgetIdSeq[index] - 1);
            // index 1 because there are 2 cells in a row (column 0 and column 1)
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                // Find the Label inside the StackPane with fx:id "iccid_value_label"
                cardWidget[index] = (StackPane) stackPane.lookup("#cardWidget_" + index);
                Platform.runLater(() -> {
                    try {
                        cardWidget[index].setVisible(true);
                        Label iccidValue = (Label) cardWidget[index].lookup("#iccid_value_label");
                        Label imsiValue = (Label) cardWidget[index].lookup("#imsi_value_label");
                        Label statusLabel = (Label) cardWidget[index].lookup("#status_label");
                        ImageView statusImage = (ImageView) cardWidget[index].lookup("#status_image");
                        iccidValue.setText(iccid);
                        imsiValue.setText(imsi);

//                        this.cardsConnectedList.getItems().add(widgetIdSeq[index] - 1, iccid);
                        this.cardsConnectedList.getItems().set(widgetIdSeq[index] - 1, iccid);
                        System.out.println("Task1 runLater : " + index);
//                  statusImage.setImage(processingImage);

                        statusLabel.setText("Waiting for card");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateWidgetStatusLabel(String status, int index) {
        Node node = mainGridPane.getChildren().get(widgetIdSeq[index] - 1);

        if (node instanceof StackPane) {

            StackPane stackPane = (StackPane) node;
            stackPane.setStyle("-fx-background-color: #FFB562; -fx-background-radius: 15;");

            StackPane cardWidget = (StackPane) stackPane.lookup("#cardWidget_" + index);
            Label statusLabel = (Label) cardWidget.lookup("#status_label");
            pause.setOnFinished(event -> {
                Platform.runLater(() -> {
                    statusLabel.setText(status);
                });
            });
            pause.play();
        }
    }

    public void updateWidgetStatusImage(Image image, int index) {
        Node node = mainGridPane.getChildren().get(widgetIdSeq[index] - 1);
        if (node instanceof StackPane) {
            StackPane stackPane = (StackPane) node;
            StackPane cardWidget = (StackPane) stackPane.lookup("#cardWidget_" + index);
            ImageView statusImage = (ImageView) cardWidget.lookup("#status_image");
            Platform.runLater(() -> {
                statusImage.setImage(image);
            });

        }
    }

    public void updateWidgetStatusImage(Boolean testSuccessful, int index) {
        Node node = mainGridPane.getChildren().get(widgetIdSeq[index] - 1);
        if (node instanceof StackPane) {
            StackPane stackPane = (StackPane) node;
            // Find the Label inside the StackPane with fx:id "iccid_value_label"
            StackPane cardWidget = (StackPane) stackPane.lookup("#cardWidget_" + index);
            Label iccid = (Label) stackPane.lookup("#iccid_value_label");
            ImageView statusImage = (ImageView) cardWidget.lookup("#status_image");
            Image greenCheck = new Image("com/mannash/javafxapplication/fxml/images/greenRight.png");
            Image redCross = new Image("com/mannash/javafxapplication/fxml/images/redCross.png");
            pause.setOnFinished(event -> {
                Platform.runLater(() -> {
                    if (testSuccessful) {
                        statusImage.setImage(greenCheck);
                        stackPane.setStyle("-fx-background-color: #82CD47; -fx-background-radius: 15;");
//                        updateIndicatorImage(iccid.getText(), greenIndicatorImage, index);
                        updateListItemColor(index, testSuccessful);

                    } else {
                        statusImage.setImage(redCross);
                        stackPane.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 15;");
//                        updateIndicatorImage(iccid.getText(), yellowIndicatorImage, index);
                        updateListItemColor(index, testSuccessful);
                    }
                });
            });
            pause.play();
        }
    }

    public void updateWidgetIccidAndImsi(String iccid, String imsi, int index) {
//        Node node = mainGridPane.getChildren().get(index);
//        if (node instanceof StackPane) {
//            StackPane stackPane = (StackPane) node;
        // Find the Label inside the StackPane with fx:id "iccid_value_label"
//            StackPane cardWidget = (StackPane) stackPane.lookup("#cardWidget_" + index);

        Scene scene = (Scene) startTestingButton.getScene();
        StackPane cardWidget = (StackPane) scene.lookup("#cardWidget_" + index);


        Label iccidValue = (Label) cardWidget.lookup("#iccid_value_label");
        Label imsiValue = (Label) cardWidget.lookup("#imsi_value_label");
        Label widgetSlot = (Label) cardWidget.lookup("#widgetSlot");
//            if (iccid == null) {
//                Platform.runLater(() -> {
//                    this.cardsConnectedList.getItems().set(index,"-");
//                    widgetSlot.setText("" + (index + 1));
//                    widgetSlot.setStyle("");
//                });
//                return;
//            }
        pause.setOnFinished(event -> {
            Platform.runLater(() -> {
                iccidValue.setText(iccid);
                imsiValue.setText(imsi);
                this.cardsConnectedList.getItems().set(widgetIdSeq[index] - 1, iccid);
                widgetSlot.setText("" + widgetIdSeq[index]);
//                    setIndicatorToICCID(iccid, yellowIndicatorImage, index);
            });
        });
        pause.play();
//        }
    }

    public void updateListItemColor(int index, Boolean testSuccessful) {
        // Get the item at the specified index
        String item = (String) this.cardsConnectedList.getItems().get(widgetIdSeq[index] - 1);
        // Check if the item matches the specified iccid
        Text iccid = new Text(item);
        if (testSuccessful) {
            iccid.setFill(Color.GREEN);
            iccid.setFont(Font.font("System", FontWeight.BOLD, 12));
        } else if (!testSuccessful) {
            iccid.setFill(Color.RED);
            iccid.setFont(Font.font("System", FontWeight.BOLD, 12));
        }
        this.cardsConnectedList.getItems().set(widgetIdSeq[index] - 1, iccid);
    }

    private void startingLogTextArea() {
        if (!logTextAreaInitialize) {
            //Add Logs area
            logTextArea.setPrefSize(simCardVbox.getPrefWidth(), 550);
            logTextArea.positionCaret(logTextArea.getLength());
            logTextArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            logTextArea.setEditable(false);
            logTextArea.viewOrderProperty();

            logTextArea.getStyleClass().add("text-area-left-aligned");
            logTextArea.setStyle("-fx-background-color: #e3f1fe; -text-area-background: #e3f1fe; text-area-background: #e3f1fe;-fx-border-color: transparent; -fx-border-width: 0; -fx-border-image-width: 0; -fx-background-insets: 0; -fx-padding: 0; -fx-background-image: null; -fx-region-background: null;-fx-border-insets: 0; -fx-background-size:0; -fx-border-image-insets:0;");
            pane = new ScrollPane(logTextArea);
            Node horizontalScrollBar = pane.lookup(".scroll-bar:horizontal");
            if (horizontalScrollBar != null) {
                horizontalScrollBar.setStyle("-fx-pref-width: 1px;");
            }
            pane.setStyle("-fx-background-color: #e3f1fe; -fx-border-color: transparent; -fx-border-width: 0; -fx-border-image-width: 0; -fx-background-image: null; -fx-region-background: null;-fx-border-insets: 0; -fx-background-size:0; -fx-border-image-insets:0;");
            pane.setFitToWidth(true);
            pane.setFitToHeight(true);

            pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            simCardVbox.getChildren().add(pane);

            startLogging();

        }
    }

    public void changeCursor() {
        Platform.runLater(() -> {

            startTestingButton.setCursor(Cursor.HAND);
        });
    }


    public void startLogging() {
        this.loggerThread = new SimVerifyLoggerThread(logTextArea);
        this.loggerThread.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                displayLogs(newValue);
            }
        });
        Thread t = new Thread(this.loggerThread);
        t.setDaemon(true);
        t.setName("LoggerThread");
        t.start();
    }

    private void addCardWidget(int id, int childId) {
        StackPane root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/GridElement.fxml"));
            root = loader.load();
            StackPane cardWidget = (StackPane) root.lookup("#rootPane");
            Label iccidValue = (Label) cardWidget.lookup("#iccid_value_label");
            Label imsiValue = (Label) cardWidget.lookup("#imsi_value_label");
            cardWidget.setId("cardWidget_" + id);

            StackPane finalRoot1 = root;

            if (SimVerifyMasterThread2.elementColumn == SimVerifyMasterThread2.numCols) {
                SimVerifyMasterThread2.elementRow++;
                SimVerifyMasterThread2.elementColumn = 0;
            }

            if (SimVerifyMasterThread2.elementRow < SimVerifyMasterThread2.numRows && SimVerifyMasterThread2.elementColumn < SimVerifyMasterThread2.numCols) {
                int _elementColumn = elementColumn;
                int _elementRow = elementRow;
                pause.setOnFinished(event -> {
                    Platform.runLater(() -> {
                        iccidValue.setText("-");
                        imsiValue.setText("-");
                        mainGridPane.add(finalRoot1, _elementColumn, _elementRow);
                        mainGridPane.getChildren().get(childId).setVisible(false);
                    });
                });
                pause.play();
                SimVerifyMasterThread2.elementColumn++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enableCardWidget(int index) {
        Platform.runLater(() -> {
            mainGridPane.getChildren().get(widgetIdSeq[index] - 1).setDisable(false);
        });

    }

    private void reLoadCardWidget(int id) {
        try {
            Scene scene = (Scene) startTestingButton.getScene();
            StackPane cardWidget = (StackPane) scene.lookup("#cardWidget_" + id);
            Label iccidValue = (Label) cardWidget.lookup("#iccid_value_label");
            Label imsiValue = (Label) cardWidget.lookup("#imsi_value_label");
            ImageView statusImage = (ImageView) cardWidget.lookup("#status_image");
            Platform.runLater(() -> {
                iccidValue.setText("-");
                imsiValue.setText("-");
                statusImage.setImage(questionMarkImage);
                cardWidget.setStyle("-fx-background-color: #2F58CD; -fx-background-radius: 15;");
                mainGridPane.getChildren().get(id).setVisible(false);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logoutMainFunction() throws IOException {
        Parent logInPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/login-form.fxml"));
        Stage primaryStage = (Stage) startTestingButton.getScene().getWindow();
        this.cardsConnectedList.getItems().clear();
        primaryStage.close();
        Stage primaryStage2 = new Stage();
        Scene scene = new Scene(logInPage);
        primaryStage2.setScene(scene);
        primaryStage2.setResizable(false);
        Image icon = new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png");
        primaryStage2.getIcons().add(icon);
        primaryStage2.setTitle("SIM Verify!");
        primaryStage2.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage2.show();
    }


    public void logOut() {
//                      cancelAllThreads();
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Confirm logout");
        Stage stage = (Stage) logoutAlert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
        ButtonType logoutButton = new ButtonType("Logout");
        ButtonType cancel = new ButtonType("Cancel");
        logoutAlert.setContentText("Are you sure that you want to logout ?");
        logoutAlert.setHeaderText(null);
        logoutAlert.getButtonTypes().clear();
        logoutAlert.getButtonTypes().setAll(logoutButton, cancel);
        logoutAlert.showAndWait().ifPresent(button -> {
            if (button == logoutButton) {
                try {
                    logoutMainFunction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void checkUpdateOnStartingApplication(){
        CheckUpdate checkUpdate = new CheckUpdate();
        String latestVersion = null;
        String currentVersion = null;
        try {
            currentVersion = checkUpdate.getCurrentVersion();
            System.out.println("Current Version : " + currentVersion);

            latestVersion = checkUpdate.getLatestVersion();

            System.out.println("Latest Version : " + latestVersion);
//                                  latestVersion = "1.0";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(!currentVersion.equals(latestVersion)){
            checkUpdate.downloadJarFile();
        }

    }

    public void checkUpdate() {
        CheckUpdate checkUpdate = new CheckUpdate();
        String latestVersion = null;
        String currentVersion = null;
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle("Checking for updates ...");
        try {
            currentVersion = checkUpdate.getCurrentVersion();
            System.out.println("Current Version : " + currentVersion);

            latestVersion = checkUpdate.getLatestVersion();

            System.out.println("Latest Version : " + latestVersion);
//                                  latestVersion = "1.0";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (currentVersion.equals(latestVersion)) {
            Platform.runLater(() -> {
                Stage stage = (Stage) alert1.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                alert1.setContentText("You are running the latest version.");
                alert1.setHeaderText("No updates available");
                alert1.showAndWait();
            });
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Checking for updates ...");
                ButtonType downloadUpdate = new ButtonType("Download");
                ButtonType noThanks = new ButtonType("No, thanks",ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.setContentText("A newer version of application is available.");
                alert.setHeaderText(null);
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image("/com/mannash/javafxapplication/fxml/images/airtelair2.png"));
                alert.getButtonTypes().clear();
                alert.getButtonTypes().setAll(downloadUpdate);
                alert.showAndWait().ifPresent(button -> {
                    if (button == downloadUpdate) {
                        checkUpdate.downloadJarFile();
                    }
                });


            });
        }


    }


    public void initializeTestingThreads() {
        isAnyCardConnected = false;
        System.out.println("Calling initializeTestingThreads");
        System.out.println("inside the run");
        TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(this.loggerThread, this);
        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();

        System.out.println("TERMINAL INFOS : " + terminalInfos.toString());
        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
        int index = 0;
//        this.cardsConnectedList.setItems(FXCollections.observableArrayList());
        while (terminalInfo.hasNext()) {
            if (index == widgetIdSeq.length) {
                break;
            }
            System.out.println("index : " + index);
            TerminalInfo terminal = terminalInfo.next();
            String iccid = terminal.getTerminalCardIccid();
            System.out.println("ICCID : " + iccid);
            this.terminalsConnected++;

            if (iccid == null) {
                updateWidgetIccidAndImsi("-", "-", index);


            }
            if (iccid != null) {
                isAnyCardConnected = true;
                cardConnectedCounter++;
                int finalIndex = index;
                updateWidgetIccidAndImsi(terminal.getTerminalCardIccid(), terminal.getImsi(), finalIndex);
                updateWidgetStatusLabel("IN_PROGRESS", finalIndex);
                updateWidgetStatusImage(processingImage, finalIndex);
            }
            index++;

        }

        if (isAnyCardConnected) {
            Platform.runLater(() -> {
                startTestingButton.setImage(cancelButton);

            });
        } else {
            setDoneButton();
        }
        startTestingThreads(terminalInfos);
    }

    private void startTestingThreads(List<TerminalInfo> terminals) {
        int numThreads = Math.min(terminals.size(), Runtime.getRuntime().availableProcessors());
        executorService = Executors.newFixedThreadPool(numThreads);
//        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < terminals.size(); i++) {

            if (i == widgetIdSeq.length) {
                break;
            }
            System.out.println("#2 : " + loggedInUserName);
            TerminalInfo terminal = terminals.get(i);
            if (terminal.getTerminalCardIccid() != null) {
                int terminalNumber = terminal.getTerminalNumber();
                System.out.println("Terminal Number ~~~ : " + (terminalNumber));
                String threadName = terminal.getTerminalNumber() + "_" + terminal.getTerminalCardIccid();
                TestingController4 controller = new TestingController4(threadName, terminal, this, i, this.loggerThread);

                Thread thread = new Thread(controller);
                thread.setName(widgetID + "_mainThread");
                controller4ThreadList.add(controller);
                Future<?> future = executorService.submit(thread);
//              futureList.add(future);
                initializedThreads++;
            }
        }
//        for (Future<?> future : futureList) {
//            try {
//                future.get();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
        executorService.shutdown();
        System.out.println("testing done");
        this.loggerThread.displayLogs(_terminal, "Verification Completed", -1);
    }

    private void onStopButtonClicked() {
        stopThreads();
        System.out.println("testing stopped");
    }


    private void stopThreads() {
//        for (TestingController4 testingController4 : controller4ThreadList) {
//            testingController4.stopMainThread();
//        }
////        for (Thread thread : threadList) {
////            thread.stop();
////            thread.interrupt();
////            executorService.shutdownNow();
////            System.out.println("THread status : " + thread.isAlive());
////        }


    }

    public void displayLogs(String log) {
        Platform.runLater(() -> {
            logTextArea.appendText(log + "\n");
        });
    }

    public void displayLogs(String from, String to, String log, int widgetId) {
        displayLogs("[" + (widgetId + 1) + "] " + " [" + from + " -> " + to + "] : " + log);
    }

    public void displayLogs(String from, String log, int widgetId) {
        displayLogs("[" + (widgetId + 1) + "] " + " [" + from + "        ] : " + log);
    }

    public synchronized void updateTesting(int id) {

        finishedThreads++;
        if (finishedThreads == initializedThreads) {
            setDoneButton();
            setExportButton();
            loggerThread.displayLogs(_terminal, "Verification Completed", -1);
            System.out.println("testing done");
            System.out.println("sending reports to server");
            sendResultToServer();

        }
    }

    private void sendResultToServer() {
//        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
//        int response = service.sendReportsToServer(SimVerifyMasterThread2.loggedInUserName, cardTestingPojosList);
        int response = 100;
        if (response != 200) {
            serializeCacheToDisk();
//            loadCacheFromDisk();
        }
    }

    private void serializeCacheToDisk() {
        System.out.println("creating cache....");
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String dateTimeString = currentDateTime.format(formatter);


        try (FileOutputStream fileOutputStream = new FileOutputStream(CACHE_FILE_PATH + dateTimeString + ".ser"); ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(cardTestingPojosList);
            fileOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error serializing cache to disk.");
            e.printStackTrace();
        }
    }

//    private void loadCacheFromDisk() {
//       File cacheFile = new File(CACHE_FILE_PATH);
//       if (cacheFile.exists()) {
//           try (FileInputStream fileInputStream = new FileInputStream(cacheFile);
//                     ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
//                   cache = (Cache<String, Object>) objectInputStream.readObject();
//
//                   List<ExportTestingResultPojo> listFromCache = (List<ExportTestingResultPojo>) cache;
//
//               System.out.println("1 : "+listFromCache.get(0));
//               System.out.println("2 : "+listFromCache.get(1));
//               System.out.println(cache.toString());
//           } catch (IOException | ClassNotFoundException e) {
//                   System.out.println("Error loading cache from disk.");
//                   e.printStackTrace();
//           }
//       }
//    }

    private void loadCacheFromDisk() {
        try {
            // Create a FileInputStream to read the serialized data from the cache file
            File cacheFile = new File(CACHE_FILE_PATH);
            FileInputStream fis = new FileInputStream(cacheFile);

            // Create an ObjectInputStream to deserialize the data and read it from the FileInputStream
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read the list of data from the cache file and cast it to a List<String>
            List<ExportTestingResultPojo> data = (List<ExportTestingResultPojo>) ois.readObject();

            // Close the ObjectInputStream and FileInputStream
            ois.close();
            fis.close();

            // Print the list of data to the console
            System.out.println("Data read from cache file:");
            for (ExportTestingResultPojo s : data) {
                System.out.println(s.getTerminalICCID());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onExportButtonPress() {
        String csvFilePath = "..\\reports\\";
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String dateTimeString = currentDateTime.format(formatter);
        String csvFileName = "TestingResult_" + dateTimeString + ".csv";
        String fileName = csvFilePath + "TestingResult_" + dateTimeString + ".csv";
        File csvFile = new File(fileName);

        File path = new File(csvFilePath);
        if (!path.exists()) {
            path.mkdir();
        }
        if (csvFile.exists()) {
            this.headersPrinted = true;
        }
        if (!csvFile.exists()) {
            try {
                csvFile.createNewFile();
                this.headersPrinted = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter writer = new FileWriter(csvFile, true); CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            if (!this.headersPrinted) {
                csvPrinter.printRecord("Username", "Slot", "Date", "Time", "ICCID", "IMSI", "SIM Heartbeat", "File System Verification", "Profile Testing", "Read/Write", "Card status");
                this.headersPrinted = true;
            }
            synchronized (csvLock) {
                for (ExportTestingResultPojo value : cardTestingPojosList) {
//                    Integer key = result.getKey();
//                    ExportTestingResultPojo value = result.getValue();

                    csvPrinter.printRecord(SimVerifyMasterThread2.loggedInUserName, widgetIdSeq[value.getTerminalNumber() - 1], value.getDateOfTesting(), value.getTimeOfTesting(), value.getTerminalICCID(), value.getTerminalIMSI(), value.getSIMHeartbeat(), value.getFileSystemVerification(), value.getProfileTesting(), value.getReadWrite(), value.getCardStatus());
                }
                csvPrinter.flush();
            }

            exportIcon.setVisible(false);
            exportMessage.setText("Result Exported successfully to\n" + csvFileName + " in reports folder");
            exportMessage.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void startTestingEnable() {
        this.startTestingButton.setDisable(false);
    }
}



