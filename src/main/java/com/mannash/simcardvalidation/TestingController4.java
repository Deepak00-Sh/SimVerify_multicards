package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.card.FileSystemVerification;
import com.mannash.simcardvalidation.card.ProfileTest3G;
import com.mannash.simcardvalidation.card.StressTest;
import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.LoggerService;
import com.mannash.simcardvalidation.service.TerminalConnectService;
import com.mannash.simcardvalidation.service.TerminalConnectServiceImpl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.smartcardio.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.atomic.AtomicBoolean;


public class TestingController4  implements Initializable,Runnable{

    int widgetId;
    String AID;
    String threadName;

    ExportTestingResultPojo testingResultPojo = new ExportTestingResultPojo();

    SimVerifyMasterThread2 simVerifyMasterThread2;

    SimVerifyLoggerThread loggerThread;
    private volatile boolean stopRequested = true;

    public TestingController4(String threadName, TerminalInfo terminal1, SimVerifyMasterThread2 thread, int index, SimVerifyLoggerThread loggerThread) {
        simVerifyMasterThread2 = thread;
        this.threadName = threadName;
        this.terminal = terminal1;
        this.widgetId = index;
        this.loggerThread = loggerThread;
    }

    @FXML
    private ImageView img_test_button;
    private ToggleGroup toggleGroup;

    Boolean isTesting = true;
    private Task<Void> mainTaskThread;
    private Task<Boolean> task1;
    private Task<Boolean> task2;
    private Task<Boolean> task3;
    private Task<Boolean> task4;
    private Task<Boolean> task5;

    //Threads


    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        System.out.println("[" + widgetId + "] " + "inside the setCard");
        this.card = card;
    }

    public String getLocalIccid() {
        return localIccid;
    }

    public void setLocalIccid(String localIccid) {
        this.localIccid = localIccid;
    }

    public int getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(int terminalId) {
        this.terminalId = terminalId;
    }

    public TerminalInfo getTerminal() {
        return terminal;
    }

    public void setTerminal(TerminalInfo _terminal) {
        terminal = _terminal;
    }

    public boolean isAtr() {
        return isAtr;
    }

    public void setAtr(boolean atr) {
        isAtr = atr;
    }

    public boolean isFileSystemVerification() {
        return fileSystemVerification;
    }

    public void setFileSystemVerification(boolean _fileSystemVerification) {
        fileSystemVerification = _fileSystemVerification;
    }

    public boolean isProfileTesting() {
        return profileTesting;
    }

    public void setProfileTesting(boolean profileTesting) {
        this.profileTesting = profileTesting;
    }

    public boolean isReadWriteTesting() {
        return readWriteTesting;
    }

    public void setReadWriteTesting(boolean readWriteTesting) {
        this.readWriteTesting = readWriteTesting;
    }

    public boolean isCardConnected() {
        return cardConnected;
    }

    public void setCardConnected(boolean cardConnected) {
        System.out.println("[" + widgetId + "] " + "From setCardConnected");
        this.cardConnected = cardConnected;
    }

    public boolean isResultCompilation() {
        return resultCompilation;
    }

    public void setResultCompilation(boolean resultCompilation) {
        this.resultCompilation = resultCompilation;
    }

    public boolean isTestingRunning() {
        return testingRunning;
    }

    public void setTestingRunning(boolean testingRunning) {
        this.testingRunning = testingRunning;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    private Card card;
    private String localIccid;
    private int terminalId;
    private TerminalInfo terminal;
    private boolean isAtr = false;
    private boolean fileSystemVerification = false;
    private boolean profileTesting = false;
    private boolean readWriteTesting = false;
    private boolean cardConnected = false;
    private boolean resultCompilation = false;
    private boolean testingRunning = false;
    private LoggerService loggerService;
    private String imsi;
    public String _terminal = "T";
    public String _card = "C";
    public String _device = "D";
    public String _ui = "UI";
    CardChannel cardChannel;

    List<Thread> threadList = new ArrayList<>();
    private AtomicBoolean running = new AtomicBoolean(true);


    //Tasks threads
    Thread thread1;
    Thread thread2;
    Thread thread3;
    Thread thread4;
    Thread thread5;


    Map<String, Thread> threadMap = new HashMap<>();

    public AtomicBoolean stopped = new AtomicBoolean(false);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.toggleGroup = new ToggleGroup();
    }

    public void interrupt() {
        running.set(false);
    }

    boolean isRunning() {
        return running.get();
    }


//    public Runnable start(){
//        mainThread = new Thread(this);
//        mainThread.start();
//        return null;
//    }
    public void stopThread(Boolean flag){
        if(!stopRequested){
            stopRequested = flag;
            Thread.currentThread().interrupt();
        }
    }
    public void stopMainThread() {
        for (Map.Entry<String, Thread> entry : this.threadMap.entrySet()) {
            String key = entry.getKey();
            Thread thread = entry.getValue();
            if(thread.isAlive()){
                Boolean threadStatus = thread.isAlive();
                System.out.println("Before stopping  : " + thread + " : " + threadStatus );
                thread.stop();
                threadStatus = thread.isAlive();
                System.out.println("After stopping  : " + thread + " : " + threadStatus );
            }
//            if(thread.isDaemon())
            System.out.println(Thread.currentThread().isAlive());
        }
    }

    public void run() {

            TerminalInfo localTerminal = terminal;
            task1 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws Exception {
                    //connect to terminal
                    System.out.println("[" + widgetId + "] " + "Inside the task 1");
//                    boolean b1 = initializeTerminal();
                    CardTerminal cardTerminal = localTerminal.getCt();
                    if (cardTerminal.isCardPresent()) {
                        loggerThread.displayLogs(_terminal, "Device connected", widgetId);
                        try {
                            Card card = cardTerminal.connect("T=0");
                            setCard(card);
                            cardChannel = card.getBasicChannel();
                        } catch (CardException e) {
                            e.printStackTrace();
                            setCardConnected(false);
                            System.out.println("Terminal: " + cardTerminal.getName() + " Card Not Present");
                            loggerThread.displayLogs(_terminal, "Card is not present", widgetId);

                            Thread.currentThread().interrupt();
                            return false;
                        }
                        System.out.println("Card is present on : " + cardTerminal.getName());
                        loggerThread.displayLogs(_terminal, "Card connected", widgetId);

                        try {
                            AID = getAID(cardTerminal);
                        } catch (Exception e) {
                            // this.logger.error("Exception in getAID");
                        }
                        loggerThread.displayLogs(_terminal, _card, "Reading ICCID", widgetId);
//                        int terminalNumber =
                        String iccid = getICCID(cardTerminal);
                        testingResultPojo.setDateOfTesting(LocalDate.now());
                        testingResultPojo.setTimeOfTesting(LocalTime.now());

                        testingResultPojo.setTerminalNumber(widgetId+1);
                        testingResultPojo.setTerminalICCID(iccid);
                        loggerThread.displayLogs(_terminal, "ICCID Value " + iccid, widgetId);
                        loggerThread.displayLogs(_terminal, _card, "Reading IMSI", widgetId);
                        String imsi = getIMSI(cardTerminal);
                        testingResultPojo.setTerminalIMSI(imsi);
                        loggerThread.displayLogs(_terminal, "IMSI Value " + imsi, widgetId);

                        if (iccid != null && !"".equalsIgnoreCase(iccid)) {
                            localTerminal.setTerminalCardIccid(iccid);
                            localTerminal.setImsi(imsi);
                            // terminalInfo.setTerminalNumber(Integer.parseInt(cardTerminal.getName()));
                        } else {

                            System.out.println("Terminal: " + cardTerminal.getName() + " Failed to fetch Card information");
                            loggerThread.displayLogs(_terminal, "Card is not responding", widgetId);
                            return false;
                        }
                        setCardConnected(true);
                    } else {
                        setCardConnected(false);
                        System.out.println("Terminal: " + cardTerminal.getName() + " Card Not Present");
                        loggerThread.displayLogs(_terminal, "Card is not present", widgetId);
                        return false;
                    }
                    return true;

                }
            };
            this.thread1 = new Thread(task1);
            this.threadMap.put("t1", this.thread1);
            thread1.start();

            task2 = new Task<Boolean>() {

                @Override
                protected Boolean call() {
                    loggerThread.displayLogs(_terminal, _card, "Starting File System Verification", widgetId);
                    System.out.println("Inside the task 2");
                    boolean b2 = fileSystemVerification();
                    setFileSystemVerification(b2);
                    System.out.println("AFTER FILE VERIFICATION!!");
                    return b2;
                }


            };

            task3 = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    //connect to terminal
                    System.out.println("Inside the task 3");
                    loggerThread.displayLogs(_terminal, _card, "Starting Profile Verification", widgetId);
                    boolean b3 = profileValidation();
                    // System.out.println("profile test status : "+b3);
                    setProfileTesting(b3);

                    System.out.println("AFTER PROFILE VERIFICATION!!");
                    return b3;

                }
            };

            task4 = new Task<Boolean>() {
                @Override
                protected Boolean call() throws InterruptedException {
                    //connect to terminal
                    System.out.println("Inside the task 4");
                    System.out.println("widgetId : " + widgetId);
                    loggerThread.displayLogs(_terminal, _card, "Starting Read/Write Test", widgetId);
                    boolean b4 = readWriteTest();

                    setReadWriteTesting(b4);

                    System.out.println("AFTER STRESS TESTING");
                    return b4;
                }
            };

            task5 = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    //connect to terminal
                    System.out.println("Inside the task 5");
                    boolean b5 = resultCompilation();
                    setResultCompilation(b5);
                    return b5;
                }
            };

            Platform.runLater(() -> {
                task1.setOnSucceeded(event1 -> {
                    Boolean result = task1.getValue();
                    System.out.println("result of task 1 : " + result);
                    Platform.runLater(() -> {
                        if (result) {

                        } else {
                            simVerifyMasterThread2.updateWidgetStatusImage(false, widgetId);
                            simVerifyMasterThread2.updateWidgetStatusLabel("Failed", widgetId);
                            loggerThread.displayLogs(_terminal, "SIM Heartbeat failed", widgetId);
                            loggerThread.displayLogs(_terminal, "Skipping File System Verification", widgetId);
                            loggerThread.displayLogs(_terminal, "Skipping Profile Verification", widgetId);
                            loggerThread.displayLogs(_terminal, "Skipping Read/Write Test", widgetId);
                        }

                    });
                    if (result) {
                        this.testingResultPojo.setSIMHeartbeat("OK");

                        this.thread2 = new Thread(task2);
                        this.threadMap.put("t2", this.thread2);
                        this.thread2.start();
                        this.thread1.stop();
                    } else {
                        task2.cancel();
                        task3.cancel();
                        task4.cancel();
                        task5.cancel();
                        simVerifyMasterThread2.updateTesting(widgetId);
                        return;
                    }

                });
                task2.setOnSucceeded(event2 -> {
                    Boolean result = task2.getValue();
                    if (result) {
                        loggerThread.displayLogs(_terminal, _card, "File Verification done", widgetId);
                    } else {
                        Platform.runLater(() -> {
                            simVerifyMasterThread2.updateWidgetStatusImage(false, widgetId);
                            simVerifyMasterThread2.updateWidgetStatusLabel("Failed", widgetId);
                            loggerThread.displayLogs(_terminal, "File Verification failed", widgetId);
                            loggerThread.displayLogs(_terminal, "Skipping Profile Verification", widgetId);
                            loggerThread.displayLogs(_terminal, "Skipping Read/Write Test", widgetId);
                        });
                    }

                    if (result) {
                        this.testingResultPojo.setFileSystemVerification("OK");
                        this.thread3 = new Thread(task3);
                        this.threadMap.put("t3", this.thread3);
                        this.thread3.start();
                        this.thread2.stop();

                    } else {
                        task3.cancel();
                        task4.cancel();
                        task5.cancel();
                        this.testingResultPojo.setFileSystemVerification("NOT OK");
                        this.testingResultPojo.setProfileTesting("NOT OK");
                        this.testingResultPojo.setReadWrite("NOT OK");
                        this.testingResultPojo.setCardStatus("FAULTY");
                        simVerifyMasterThread2.updateTesting(widgetId);
                        return;
                    }

                });

                task3.setOnSucceeded(event3 -> {
                    Boolean result = task3.getValue();

                    if (result) {
                        loggerThread.displayLogs(_terminal, "Profile Verification done", widgetId);
                    } else {
                        Platform.runLater(() -> {
                            simVerifyMasterThread2.updateWidgetStatusImage(false, widgetId);
                            simVerifyMasterThread2.updateWidgetStatusLabel("Failed", widgetId);
                            loggerThread.displayLogs(_terminal, "Profile Verification failed", widgetId);
                            loggerThread.displayLogs(_terminal, "Skipping Read/Write Test", widgetId);
                        });
                    }

                    if (result) {
                        this.testingResultPojo.setProfileTesting("OK");
                        this.thread4 = new Thread(task4);
                        this.threadMap.put("t3", this.thread4);
                        this.thread4.start();
                        this.thread3.stop();
                    } else {
                        task4.cancel();
                        task5.cancel();
                        this.testingResultPojo.setProfileTesting("NOT OK");
                        this.testingResultPojo.setReadWrite("NOT OK");
                        this.testingResultPojo.setCardStatus("FAULTY");
                        simVerifyMasterThread2.updateTesting(widgetId);
                        return;
                    }

                });

                task4.setOnSucceeded(event4 -> {
                    Boolean result = task4.getValue();
                    Platform.runLater(() -> {
                        if (result) {
                            loggerThread.displayLogs(_terminal, "Read/Write Test Passed", widgetId);
                            loggerThread.displayLogs(_terminal, "Card is OK.", widgetId);
                        } else {
                            simVerifyMasterThread2.updateWidgetStatusImage(false, widgetId);
                            simVerifyMasterThread2.updateWidgetStatusLabel("Failed", widgetId);
                            loggerThread.displayLogs(_terminal, "Read/Write Test failed", widgetId);
                        }
                    });


                    if (result) {
                        this.testingResultPojo.setReadWrite("OK");
                        this.thread5 = new Thread(task5);
                        this.threadMap.put("t5", this.thread5);
                        this.thread5.start();
                        this.thread4.stop();
                    } else {
                        task5.cancel();

                        this.testingResultPojo.setReadWrite("NOT OK");
                        this.testingResultPojo.setCardStatus("FAULTY");
                        simVerifyMasterThread2.updateTesting(widgetId);
                        return;
                    }

                });

                task5.setOnSucceeded(event5 -> {
                    Boolean result = task5.getValue();
                    Platform.runLater(() -> {
                        if (result) {
                            // simVerifyMasterThread2.setDoneButton();
                            simVerifyMasterThread2.updateWidgetStatusImage(true, widgetId);
                            simVerifyMasterThread2.updateWidgetStatusLabel("Ok", widgetId);
                        } else {
                            simVerifyMasterThread2.updateWidgetStatusImage(false, widgetId);
                            simVerifyMasterThread2.updateWidgetStatusLabel("Failed", widgetId);
                        }
                    });
                    this.testingResultPojo.setCardStatus("OK");
                    simVerifyMasterThread2.cardTestingResultMap.put(this.widgetId,this.testingResultPojo);
                    System.out.println("Testing completed");
                    simVerifyMasterThread2.updateTesting(widgetId);
                });

            });


        }




    public String getIMSI(CardTerminal cardTerminal) {
        try {
            sendRawApduNoPrint(cardTerminal, "00A4040C10 " + this.AID);
            sendRawApduNoPrint(cardTerminal, "00A4090C02 6F07");
            String s1 = nibbleSwap(sendRawApduNoPrint(cardTerminal, "00B0000009"));
            s1 = s1.substring(3);
            return s1;
        } catch (Exception e) {
            return "0000000000000000";
        }
    }

    public String byteArrayToString(byte[] paramArrayOfbyte) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b = 0; b < paramArrayOfbyte.length; b++) {
            String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
            if (str.length() == 1)
                stringBuffer.append(0);
            stringBuffer.append(str);
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static String nibbleSwap(String paramString) {
        String str = "";
        for (int i = 0; i < paramString.length(); i += 2) {
            String str1 = paramString.substring(i, i + 2).substring(0, 1);
            String str2 = paramString.substring(i, i + 2).substring(1);
            String str3 = str1;
            str1 = str2;
            str2 = str3;
            str = str + str1 + str2;
        }
        return str;
    }


    public String insertSpace(String paramString) {
        String str = "";
        for (int i = 0; i < paramString.length(); i += 2) {
            if (!str.equals("")) {
                str = str + " " + paramString.substring(i, i + 2);
            } else {
                str = paramString.substring(i, i + 2);
            }
        }
        return str.toUpperCase();
    }

    public String sendRawApduNoPrint(CardTerminal cardTerminal, String paramString) {
        try {
            return sendCmd(cardTerminal, paramString);
        } catch (Exception exception) {
            // this.logger.error("Send APDU Error : " +paramString);
        }
        return null;
    }

    public String sendCmd(CardTerminal cardTerminal, String paramString) {
        paramString = paramString.toUpperCase();
        paramString = paramString.replaceAll(" ", "");
        if (paramString.length() % 2 != 0) {

        } else {
            byte[] arrayOfByte = new byte[paramString.length() / 2];
            boolean bool = true;
            for (int i = 0; i < paramString.length(); i += 2) {
                String str = paramString.substring(i, i + 2);
                try {
                    int j = Integer.parseInt(str, 16);
                    arrayOfByte[i / 2] = (byte) j;
                } catch (Exception exception) {

                    bool = false;
                    break;
                }
            }
            if (bool) {
                CommandAPDU commandAPDU = null;
                try {
                    commandAPDU = new CommandAPDU(arrayOfByte);
                } catch (Exception exception) {
                    // this.logger.error("Command: " + paramString + "ERROR: " +
                    // exception.getMessage());
                }
                try {
                    ResponseAPDU responseAPDU = cardChannel.transmit(commandAPDU);
                    String fetchedIccid = byteArrayToString(responseAPDU.getData());
                    return fetchedIccid;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        return null;
    }

    public String getICCID(CardTerminal cardTerminal) {
        try {
            sendRawApduNoPrint(cardTerminal, "00A4080402 2FE2");
            String response = sendRawApduNoPrint(cardTerminal, "00B000000A");
            if (response != null) {
                return nibbleSwap(response);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAID(CardTerminal cardTerminal) {
        String str1 = "", str2 = "";
        try {
            str1 = insertSpace(sendRawApduNoPrint(cardTerminal, "00A4080402 2F00"));
            str2 = fetchRecordSize(str1);
            str1 = sendRawApduNoPrint(cardTerminal, "00B20104" + str2);
            if (!str1.equals(null))
                str1 = str1.substring(8, 40);
        } catch (Exception e) {
            // this.logger.error("Null Pointer Exception : " + str1);
        }
        return str1.toUpperCase();
    }

    public String fetchRecordSize(String paramString) {
        StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ");
        int i = stringTokenizer.countTokens();
        byte b1 = 0, b2 = 0;
        int j = 0;
        String str = "";
        String[] arrayOfString = new String[i];
        try {
            if (stringTokenizer.hasMoreTokens())
                for (byte b = 0; b < i; b++) {
                    arrayOfString[b] = stringTokenizer.nextToken();
                    if (arrayOfString[b].equals("82") && b <= i - 4)
                        b1 = b;
                    if (arrayOfString[b].equals("80") && b <= i - 4)
                        b2 = b;
                }
            j = Integer.parseInt(arrayOfString[b1 + 4] + arrayOfString[b1 + 5], 16);
            str = Integer.toHexString(j);
            if (str.length() == 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // this.logger.error("ArrayIndexOutOfBoundException at" + j);
        }
        return str;
    }

    public void logOut() throws IOException {
        Parent logInPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/login-form.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(logInPage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//    private boolean initializeTerminal() throws CardException {
//        loggerThread.displayLogs(_terminal, _card, "Calling fetchterminal", widgetId);
//        TerminalConnectService terminalConnectService = new TerminalConnectServiceImpl(null);
//        try {
//            List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
//            if (terminalInfos.size() == 0) {
////            displayLogs(_terminal,_card,"No card found");
//                return false;
//            } else {
//                TerminalInfo terminal1 = terminalInfos.get(0);
//                setTerminal(terminal1);
//
////            this.woId = "1184";
//                int terminalId1 = terminal.getTerminalNumber();
//                setTerminalId(terminalId1);
//                return true;
//            }
//        } catch (Exception e) {
//            // System.out.println("Testing controller exception");
//        }
//        return false;
//    }

    private boolean fileSystemVerification() {
//        loggerThread.displayLogs(_terminal,"File System Verification started",widgetId);
        FileSystemVerification fileSystemVerification = new FileSystemVerification("0000000000000000", terminal, getCard(), this.loggerService, this.loggerThread, widgetId);
        return fileSystemVerification.runFileSystemVerification();
    }

    private boolean profileValidation() {
        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", this.terminal, getCard(), this.loggerService, this.loggerThread, widgetId);
//        boolean runProfile = profileTest3G.runProfileTesting();
        return profileTest3G.runProfileTesting();
    }

    public boolean readWriteTest() {
        StressTest stressTest = new StressTest(this.terminal, getCard(), this.loggerService, this.loggerThread, widgetId);
        stressTest.runStressTesting();
        boolean stressTestingSuccessful = stressTest.startStressTest();
        return stressTestingSuccessful;
    }

    public boolean resultCompilation() {
        return true;
    }

    private void clearTerminal() {
        try {
            Class pcscterminal = Class.forName("sun.security.smartcardio.PCSCTerminals");
            Field contextId = pcscterminal.getDeclaredField("contextId");
            contextId.setAccessible(true);

            if (contextId.getLong(pcscterminal) != 0L) {
                // First get a new context value
                Class pcsc = Class.forName("sun.security.smartcardio.PCSC");
                Method SCardEstablishContext = pcsc.getDeclaredMethod(
                        "SCardEstablishContext",
                        new Class[]{Integer.TYPE}
                );
                SCardEstablishContext.setAccessible(true);

                Field SCARD_SCOPE_USER = pcsc.getDeclaredField("SCARD_SCOPE_USER");
                SCARD_SCOPE_USER.setAccessible(true);

                long newId = ((Long) SCardEstablishContext.invoke(pcsc,
                        new Object[]{SCARD_SCOPE_USER.getInt(pcsc)}
                ));
                contextId.setLong(pcscterminal, newId);


                // Then clear the terminals in cache
                TerminalFactory factory = TerminalFactory.getDefault();
                CardTerminals terminals = factory.terminals();
                Field fieldTerminals = pcscterminal.getDeclaredField("terminals");
                fieldTerminals.setAccessible(true);
                Class classMap = Class.forName("java.util.Map");
                Method clearMap = classMap.getDeclaredMethod("clear");

                clearMap.invoke(fieldTerminals.get(terminals));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}