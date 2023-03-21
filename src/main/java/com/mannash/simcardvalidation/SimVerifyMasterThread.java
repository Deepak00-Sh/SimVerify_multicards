package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class SimVerifyMasterThread extends Application implements Initializable {



    @FXML private GridPane mainGridPane;

    @FXML
    private VBox simCardVbox;

    @FXML
    private ImageView startTestingButton;

    @FXML
    private static ListView cardsConnectedList;

    ScrollPane pane;


    TextArea logTextArea = new TextArea();
    private Task<Boolean> task1;
    List<String> items;

   public static int numRows = 4;
    public static int numCols = 4;

    public static int elementRow =0;
    public static int elementColumn =0;
    Image greenIndicatorImage = new Image(getClass().getResourceAsStream("/com/mannash/javafxapplication/fxml/images/greenLight2.gif"));
    Image cancelButton = new Image("/com/mannash/javafxapplication/fxml/images/button_cancel.png");

    public static CountDownLatch latch = null;
    private final Object lock = new Object();


    static Map<String, Thread> testThreadMap = Collections.synchronizedMap(new HashMap<String, Thread>(20));


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    @Override
    public void start(Stage primaryStage) throws IOException, InterruptedException {
        Parent mainPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));
        // Create a scene with the GridPane as the root node
        Scene scene = new Scene(mainPage);
        // Set the stage title and show the stage
        primaryStage.setTitle("My Grid");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

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

    public void run(){
        while (true){
            TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(null);
            List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
            Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
            while (terminalInfo.hasNext()) {
                TerminalInfo terminal = terminalInfo.next();
                String localIccid = terminal.getTerminalCardIccid();
                int terminalId = terminal.getTerminalNumber();
//                startTestingThread(terminal);
            }

        }

    }

//    private void startTestingThread(TerminalInfo terminal) {
//        String threadName = terminal.getTerminalNumber() + "_" + terminal.getTerminalCardIccid();
//        Thread localTestExecutor = new Thread(new TestExecutor(threadName, terminal, this.loggerService), threadName);
//        localTestExecutor.start();
//
//        //TODO
////		this.threadMap.addEntry(terminal.getTerminalCardIccid(), localTestExecutor);
//        testThreadMap.put(terminal.getTerminalCardIccid(), localTestExecutor);
//
//    }

    public void onStartButtonPress() throws IOException, InterruptedException {
        if(startTestingButton.getImage().getUrl().contains("button_Start_Testing.png")) {
            TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(null);
            int numberOfTerminal = terminalConnectService.fetchTerminalCount();
            System.out.println("Number of terminal connected : "+numberOfTerminal);
//            List<String> iccidsValue = new ArrayList<String>();
//            Platform.runLater(() -> {
                System.out.println("inside platform");
                startTestingButton.setImage(cancelButton);
                startingLogTextArea();
//                    });


//            SimVerifyMasterThread.latch = new CountDownLatch(numberOfTerminal);
//            for (int i=0; i<numberOfTerminal; i++){
////                iccidsValue.add("--");
//                setDataToTestingWindow("-",i);
//            }
//
//            try {
//                // wait for all setDataToTestingWindow calls to finish
//                latch.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


//            Thread.sleep(5000);

//            System.out.println("outside platform");
            task1 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    System.out.println("From task1");
                    for (int i=0; i<numberOfTerminal; i++){
//                iccidsValue.add("--");
                        setDataToTestingWindow("-",i);
                    }
                    return true;
                }
            };

            task1.setOnSucceeded(event1 -> {
                Boolean result = task1.getValue();
                if (result){
                    System.out.println("from setOnSucceeded");
                    for (int i=0; i<numberOfTerminal; i++){
                        updateStackPaneData(0,"A"+i);
                    }
//                iccidsValue.add("--");

                }

            });

            Thread thread1 = new Thread(task1);
            thread1.start();

//            CountDownLatch latch = new CountDownLatch(1);
//            new Thread(() -> {
//                for (int i=0; i<numberOfTerminal; i++){
////                iccidsValue.add("--");
//                        setDataToTestingWindow("-",i);
//                    }
//                // Count down the latch to signal that fn1 is complete
//                latch.countDown();
//            }).start();
//
//            updateStackPaneData(0,"A"+0);

            for (int i=0;i<numberOfTerminal;i++){



//                Scene scene = startTestingButton.getScene();

//                Window owner = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
//                Scene scene = owner.getScene();
//                Label iccidLable = (Label) scene.lookup("#iccid_value_lable_"+i);
//                int cntr = 0;
//                while(iccidLable==null){
//                    cntr++;
//                    if(cntr==1000000){
//                        System.out.println("Breaking counter");
//                        break;
//                    }
//                    iccidLable = (Label) scene.lookup("#iccid_value_lable_"+i);
//                }
//                iccidLable.setText(""+i);

//                ImageView iccidLable2 = (ImageView) scene.lookup("#startTestingButton");
//
//                Image doneImage = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
//
//                iccidLable2.setImage(doneImage);
            }
//            iccidsValue.add("8991000904448610811F");
//            iccidsValue.add("8991000904448610812F");
//            iccidsValue.add("8991000904448610813F");
//            iccidsValue.add("8991000904448610814F");
//            iccidsValue.add("8991000904448610815F");
//            iccidsValue.add("8991000904448610816F");
//            SimVerifyMasterThread.numRows = iccidsValue.size() /4 + 1;

//            Iterator<String> iccidsValueIterator = iccidsValue.iterator();
//            int i =0;
//            while(iccidsValueIterator.hasNext()){
//                setDataToTestingWindow(iccidsValueIterator.next(),i);
//                i++;
//            }

//            Platform.runLater(() -> {
//                ObservableList<String> iccids = FXCollections.observableArrayList(
//                        "8991000904448610811F", "8991000904448610812F", "8991000904448610813F", "8991000904448610815F");
//                cardsConnectedList.getItems().clear();
////                cardsConnectedList.getItems().setAll(iccids);
////
////                addCardWidget(iccids);
////                startingLogTextArea();
//
//            });

        }else if(startTestingButton.getImage().getUrl().contains("button_cancel.png")) {
            Platform.runLater(() ->{
                try {
                    loadTestingPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }
    public synchronized void setDataToTestingWindow(String iccid , int id){
        System.out.println("setDataToTestingWindow");
//        double delay = 0.5;
//        PauseTransition pause = new PauseTransition(Duration.seconds(delay * id));
//        pause.setOnFinished(event -> {
//
//
////            SimVerifyMasterThread.latch.countDown();
//
//
//
//
//
//        });
//        pause.play();
        addCardWidget(iccid, id);
        cardsConnectedList.getItems().add(id,iccid);
        addIndicatorsToICCID(greenIndicatorImage,id);

        // Start the pause transition

    }

    public void updateStackPaneData(int index, String iccid) {
        System.out.println("from updateStackPaneData");
        synchronized(lock){
        Node node = mainGridPane.getChildren().get(index);
        // index 1 because there are 2 cells in a row (column 0 and column 1)
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                // Find the Label inside the StackPane with fx:id "iccid_value_label"
                Label iccidValueLabel = (Label) stackPane.lookup("#iccid_value_label_"+index);
                // Update the text of the Label
                Platform.runLater(() -> {
                    iccidValueLabel.setText(iccid);
                    cardsConnectedList.getItems().set(index, iccid);
        //            addIndicatorsToICCID(yellowIndicatorImage,index);
                });

            }
        }
    }


    private static void addIndicatorsToICCID(Image indicatorImage ,int id){
        cardsConnectedList.setCellFactory(lv -> new ListCell<String>() {
            ImageView imageView = new ImageView(indicatorImage);
            private final HBox hbox = new HBox(10);

            {
                hbox.setAlignment(Pos.CENTER_RIGHT);
                hbox.getChildren().addAll(new Label(), imageView);
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ((Label) hbox.getChildren().get(0)).setText(item);
                    imageView.setFitHeight(12);
                    imageView.setFitWidth(12);
                    setText(null);
                        setGraphic(hbox);
                }
            }
        });

    }

    private void addCardWidget(String iccid , int id){
//        int numRows = 4;
//        int numCols = 4;
//        int delay = 2; // delay in seconds
//        for (int i = 0; i < numRows; i++) {
//            for (int j = 0; j < numCols; j++) {
//                int index = i * numCols + j;
//                PauseTransition pause = new PauseTransition(Duration.seconds(delay));
//                if (index < cardsConnectedList.getItems().size()) {
//                    // Load the FXML file and create the root node
                    StackPane root = null;
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/GridElement.fxml"));
                        root = loader.load();
                        Label iccidValueLabel =  (Label) root.lookup("#iccid_value_label");
                        System.out.println("Id0 : "+iccidValueLabel.getId());
                        iccidValueLabel.setId("iccid_value_label_"+id);
                        System.out.println("ID : iccid_value_lable_"+id);

                        System.out.println("Id2 : "+iccidValueLabel.getId());
                        StackPane finalRoot1 = root;

                        if(SimVerifyMasterThread.elementColumn == SimVerifyMasterThread.numCols){
                            SimVerifyMasterThread.elementRow++;
                            SimVerifyMasterThread.elementColumn = 0;
                        }

                        if(SimVerifyMasterThread.elementRow < SimVerifyMasterThread.numRows && SimVerifyMasterThread.elementColumn < SimVerifyMasterThread.numCols) {
                            iccidValueLabel.setText(iccid);
//                            mainGridPane.add(finalRoot1, elementColumn, elementRow);
                            Platform.runLater(() -> {
                                        mainGridPane.add(finalRoot1, elementColumn, elementRow);
                                    });
                            SimVerifyMasterThread.elementColumn++;
                        }
//                        if(mainGridPane.getChildren().size() >= 4){
//                            updateStackPaneData(1,"1212121212121221");
//                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Add a PauseTransition to delay adding the root elemen
        }
    private void startingLogTextArea(){
        //Add Logs area
        logTextArea.setPrefSize(simCardVbox.getPrefWidth(), simCardVbox.getPrefHeight());
        logTextArea.positionCaret(logTextArea.getLength());
        logTextArea.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        logTextArea.setEditable(false);
        logTextArea.viewOrderProperty();

        logTextArea.getStyleClass().add("text-area-left-aligned");
        logTextArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0; -fx-border-image-width: 0; -fx-background-image: null; -fx-region-background: null;-fx-border-insets: 0; -fx-background-size:0; -fx-border-image-insets:0;");
        logTextArea.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");


        pane = new ScrollPane(logTextArea);
        Node horizontalScrollBar = pane.lookup(".scroll-bar:horizontal");
        if (horizontalScrollBar != null) {
            horizontalScrollBar.setStyle("-fx-pref-width: 1px;");
        }
        pane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 0; -fx-border-image-width: 0; -fx-background-image: null; -fx-region-background: null;-fx-border-insets: 0; -fx-background-size:0; -fx-border-image-insets:0;");
        pane.setFitToWidth(true);
        pane.setFitToHeight(true);

        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        simCardVbox.getChildren().add(pane);
    }

    private void loadTestingPage() throws IOException {
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));
        Stage primaryStage = (Stage) startTestingButton.getScene().getWindow();
        Scene scene = new Scene(testingPage);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
