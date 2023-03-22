package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationService;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;


import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;


public class SimVerifyMasterThread2 {

    SimVerifyLoggerThread loggerThread;

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
    private Label exportMessage;

    @FXML
    private ImageView startTestingButton;

    @FXML
    private static ListView cardsConnectedList;

    @FXML
    private ImageView exportIcon;
    //runtime elements
    ScrollPane pane;
    //Image Views
    Image cancelButton = new Image("/com/mannash/javafxapplication/fxml/images/button_cancel.png");
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
    public List<Thread> threadList = new ArrayList<Thread>();
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

    ConcurrentHashMap<Integer, ExportTestingResultPojo> cardTestingResultMap = new ConcurrentHashMap<Integer, ExportTestingResultPojo>();

    ExportTestingResultPojo exportTestingResultPojo = new ExportTestingResultPojo();
    private Object csvLock = new Object();

    int numberOfAvailableCards =0;

    @FXML
    public void onLoginButtonPress() throws IOException {
      TrakmeServerCommunicationService trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl(loggerThread);
        String userId = user_input.getText();
        String password = password_input.getText();
        String hardCodeUserId = "a";
        String hardCodePassword = "a";
        if (userId.equalsIgnoreCase("store1@airtel.in") && password.equalsIgnoreCase(hardCodePassword)) {
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.close();
            loadTestingWindowData();
        } else if (userId.equalsIgnoreCase("simlab@airtel.in") && password.equalsIgnoreCase(hardCodePassword)) {
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.close();
            loadTestingWindowData();
        } else if (userId.equalsIgnoreCase(hardCodeUserId) && password.equalsIgnoreCase(hardCodePassword)) {
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.close();
            loadTestingWindowData();
        } else {
            promptLabel.setTextFill(Color.rgb(255, 0, 0));
            promptLabel.setText("Invalid username or password!");
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
        for (int i = 0; i < 16; i++) {
            double delay = 0.05;
            int id = i;
            PauseTransition pause = new PauseTransition(Duration.seconds(delay * i));
            pause.setOnFinished(event -> {
                addCardWidget(id);
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
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        mainGridPane.setBackground(new Background(background));
    }

    public void reLoadTestingWindowData() {
        logTextAreaInitialize = true;
        cardsConnectedList.getItems().clear();
        initializedThreads = 0;
        finishedThreads = 0;

        logTextArea.setText("");
        for (int i = 0; i < 16; i++) {
            int id = i;
            reLoadCardWidget(id);
        }
        exportMessage.setVisible(false);
        exportIcon.setVisible(false);
        setStartButton();
        threadList.clear();
    }

    public void onStartButtonPress() {
        if (startTestingButton.getImage().getUrl().contains("button_Start_Testing.png")) {
            TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(this.loggerThread,this);
            int numberOfTerminal = terminalConnectService.fetchTerminalCount();

            System.out.println("Number of terminal connected : " + numberOfTerminal);
            startTestingButton.setImage(cancelButton);

            startingLogTextArea();
            task1 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("From task1");
                    for (int i = 0; i < numberOfTerminal; i++) {
                        System.out.println("For loop count : " + i);
                        int a = i;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateStackPaneData(a, "-", "-");

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
        } else if (startTestingButton.getImage().getUrl().contains("button_cancel")) {
//          cancelAllThreads();
            onStopButtonClicked();
            for (int i = 0; i < this.terminalsConnected; i++) {
                updateWidgetStatusImage(false, i);
                updateWidgetStatusLabel("Testing Cancelled", i);
            }
//          this.loggerThread.interrupt();
            setDoneButton();
            cardsConnectedList.getItems().clear();
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
        Node node = mainGridPane.getChildren().get(index);
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
//                    cardsConnectedList.getItems().add(index, iccidValue.getText());
                    System.out.println("Task1 runLater : " + index);
//                            statusImage.setImage(processingImage);
                    statusLabel.setText("Waiting for card");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public void updateWidgetStatusLabel(String status, int index) {
        Node node = mainGridPane.getChildren().get(index);
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
        Node node = mainGridPane.getChildren().get(index);
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
        Node node = mainGridPane.getChildren().get(index);
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
        Node node = mainGridPane.getChildren().get(index);
        if (node instanceof StackPane) {
            StackPane stackPane = (StackPane) node;
            // Find the Label inside the StackPane with fx:id "iccid_value_label"
            StackPane cardWidget = (StackPane) stackPane.lookup("#cardWidget_" + index);
            Label iccidValue = (Label) cardWidget.lookup("#iccid_value_label");
            Label imsiValue = (Label) cardWidget.lookup("#imsi_value_label");
            Label widgetSlot = (Label) cardWidget.lookup("#widgetSlot");
            if (iccid == null) {
                Platform.runLater(() -> {
                    this.cardsConnectedList.getItems().add(index,"--");
                    widgetSlot.setText("" + (index + 1));
                    widgetSlot.setStyle("");
                });
                return;
            }
            pause.setOnFinished(event -> {
                Platform.runLater(() -> {
                    iccidValue.setText(iccid);
                    imsiValue.setText(imsi);
                    this.cardsConnectedList.getItems().add(index,iccid);
                    widgetSlot.setText("" + (index + 1));
//                    setIndicatorToICCID(iccid, yellowIndicatorImage, index);
                });
            });
            pause.play();
        }
    }

    public void updateListItemColor(int index , Boolean testSuccessful) {
        // Get the item at the specified index
        String item = (String) this.cardsConnectedList.getItems().get(index);
        // Check if the item matches the specified iccid
        Text iccid = new Text(item);
        if(testSuccessful){
           iccid.setFill(Color.GREEN);
           iccid.setFont(Font.font("System", FontWeight.BOLD, 12));
        }
        else if(!testSuccessful){
            iccid.setFill(Color.RED);
            iccid.setFont(Font.font("System", FontWeight.BOLD, 12));
        }
        this.cardsConnectedList.getItems().set(index, iccid);
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
        t.start();
    }

    private void addCardWidget(int id) {
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
                        mainGridPane.getChildren().get(id).setVisible(false);

                    });
                });
                pause.play();
                SimVerifyMasterThread2.elementColumn++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void logOut() throws IOException {
//        cancelAllThreads();
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

        primaryStage2.show();

    }

    public void initializeTestingThreads() {
        System.out.println("inside the run");
        TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(this.loggerThread,this);
        List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
        Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
        int index = 0;
//        this.cardsConnectedList.setItems(FXCollections.observableArrayList());
        while (terminalInfo.hasNext()) {
            System.out.println("index : " + index);
            TerminalInfo terminal = terminalInfo.next();
            System.out.println("Terminal Number : " + (index + 1));
            exportTestingResultPojo.setTerminalNumber((index + 1));
            String iccid = terminal.getTerminalCardIccid();
            System.out.println("ICCID : " + iccid);
            this.terminalsConnected++;
            if (iccid == null) {
                updateWidgetIccidAndImsi(null, null, index);
            }
            if (iccid != null) {
                cardConnectedCounter++;
                String imsi = terminal.getImsi();
                System.out.println("IMSI : " + imsi);
                int finalIndex = index;
                updateWidgetIccidAndImsi(iccid, imsi, finalIndex);
                updateWidgetStatusLabel("IN_PROGRESS", finalIndex);
                updateWidgetStatusImage(processingImage, finalIndex);
            }
            index++;
        }
        startTestingThreads(terminalInfos);
    }

    private void startTestingThreads(List<TerminalInfo> terminals) {
        int numThreads = Math.min(terminals.size(), Runtime.getRuntime().availableProcessors());
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
//        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < terminals.size(); i++) {
            TerminalInfo terminal = terminals.get(i);
            if (terminal.getTerminalCardIccid() != null) {
                int terminalNumber = terminal.getTerminalNumber();
                System.out.println("Terminal Number ~~~ : " + (terminalNumber));
                String threadName = terminal.getTerminalNumber() + "_" + terminal.getTerminalCardIccid();
                TestingController4 controller4 = new TestingController4(threadName, terminal, this, i, this.loggerThread);
                controller4ThreadList.add(controller4);
                Future<?> future = executor.submit(controller4);
                futureList.add(future);
                initializedThreads++;
            }
        }
        for (Future<?> future : futureList) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        System.out.println("testing done");
        this.loggerThread.displayLogs(_terminal, "Verification Completed", -1);
    }

    private void onStopButtonClicked() {
        stopThreads();
        System.out.println("testing stopped");
    }

    private void stopThreads() {
        Thread.enumerate(threads); // Iterate over each thread and interrupt if name matches
        for (Thread thread : threads) {
            if (thread != null) {
                if (thread.getName().equals("Testing thread : 0")) {
                    System.out.println("found!!");
                    thread.interrupt();
                }
            }
        }
    }

    public void displayLogs(String log) {
        Platform.runLater(() -> {
            logTextArea.appendText(log + "\n");
        });
    }

    public void displayLogs(String from, String to, String log, int widgetId) {
        displayLogs("["+(widgetId+1)+"] "+" ["+from+" -> "+to+"] : "+log);
    }

    public void displayLogs(String from, String log,int widgetId) {
        displayLogs("["+(widgetId+1)+"] "+" ["+from+"        ] : "+log);
    }

    public synchronized void updateTesting(int id) {
        finishedThreads++;
        if (finishedThreads == initializedThreads) {
            setDoneButton();
            setExportButton();
            loggerThread.displayLogs(_terminal, "Verification Completed", -1);
            System.out.println("testing done");
        }
    }

    @FXML
    public void onExportButtonPress() {
        String csvFilePath = "..\\reports\\";
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String dateTimeString = currentDateTime.format(formatter);
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
        try (FileWriter writer = new FileWriter(csvFile, true);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            if (!this.headersPrinted) {
                csvPrinter.printRecord(
                        "Date",
                        "Time",
                        "Terminal Number",
                        "ICCID",
                        "IMSI",
                        "SIM Heartbeat",
                        "File System Verification",
                        "Profile Testing",
                        "Read/Write",
                        "Card status"
                );
                this.headersPrinted = true;
            }
            synchronized (csvLock) {
                for (Map.Entry<Integer, ExportTestingResultPojo> result : cardTestingResultMap.entrySet()) {
                    Integer key = result.getKey();
                    ExportTestingResultPojo value = result.getValue();
                    csvPrinter.printRecord(
                            value.getDateOfTesting(),
                            value.getTimeOfTesting(),
                            value.getTerminalNumber(),
                            value.getTerminalICCID(),
                            value.getTerminalIMSI(),
                            value.getSIMHeartbeat(),
                            value.getFileSystemVerification(),
                            value.getProfileTesting(),
                            value.getReadWrite(),
                            value.getCardStatus()
                    );
                }
                csvPrinter.flush();
            }
            exportIcon.setVisible(false);
            exportMessage.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void testMethod(){
        System.out.println("this is test");
    }


}



