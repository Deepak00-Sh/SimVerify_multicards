package com.mannash.simcardvalidation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SimVerifyLoggerThread extends Task<String> {

    //    private final ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
    private final TextArea logTextArea;
    private final BlockingQueue <String> logQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    //    private final List<String> logBatch = new ArrayList<>();
    private ObservableList<String> logBatch = FXCollections.observableArrayList();
    private final int maxBatchSize = 50;
    private final long batchIntervalMillis = 100;

    public SimVerifyLoggerThread(TextArea logTextArea) {
        this.logTextArea = logTextArea;
        executorService.scheduleAtFixedRate(this::writeLogBatch, batchIntervalMillis, batchIntervalMillis, TimeUnit.MILLISECONDS);
//        writeLogs();
    }

    public void displayLogs(String from, String to, String log, int widgetId) {
        log("["+(widgetId+1)+"] "+" ["+from+" -> "+to+"] : "+log);
    }

    public void displayLogs(String from, String log,int widgetId) {
        log("["+(widgetId+1)+"] "+" ["+from+"        ] : "+log);
    }

    public void log(String logMessage) {
        logQueue.offer(logMessage);
    }

    private void writeLogBatch() {
//        if (!logBatch.isEmpty()) {
//            Platform.runLater(() -> {
//                for (String logMessage : logBatch) {
//                    logTextArea.appendText(logMessage + "\n");
//                }
//            });
//            logBatch.clear();
//        }
    }

//    @Override
//    public void run() {
//        while (true) {
//            try {
//                String logMessage = logQueue.poll();
//                logBatch.add(logMessage);
//                if (logBatch.size() >= maxBatchSize) {
//                    writeLogBatch();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

//    @Override
//    public void run() {
//        writeLogs();
//        System.out.println("void run");
//        while (true) {
//            String message = logQueue.poll();
//            if (message != null){
//                writeLogs(message);
//            }


//            Task<Void> task = new Task<Void>() {
//                String message = logQueue.poll();
//                @Override
//                protected Void call() throws Exception {
//                    for (int i = 0; i < 5000; i++) {
//
//                    }
//                    return null;
//                }
//            };

//            new Thread(task).start();

//            logBatch.add(message);
//            if (logBatch.size() >= maxBatchSize) {
//
//                Task<Void> task = new Task<Void>() {
//                    //            String message = logQueue.poll();
//                    @Override
//                    protected Void call() throws Exception {
//                        System.out.println("from task");
//
//                        Platform.runLater(() -> {
//                                    for (String logMessage : logBatch) {
//                                        logTextArea.appendText(logMessage + "\n");
//                                    }
//                        });logBatch.clear();
//                      return null;
//                    }
//
//
//            }

//        }
//    }

//    public void writeLogs(){
//
//
//        System.out.println("write logs");
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                StringBuilder logsBuilder = new StringBuilder();
//                int logCounter = 0;
//                String log;
//                while (true) {
//                    logCounter++;
////                String message = logQueue.take();
////                if (message != null){
////                    if(logCounter % 100 == 0){
////                        try {
////                            Thread.sleep(10);
////                        } catch (InterruptedException e) {
////                            throw new RuntimeException(e);
////                        }
////                        logCounter = 0;
////                    }
//
//                    for (int i = 0; i < 10; i++){
////                        log = logQueue.poll();
//                        log = logQueue.take();
//                        System.out.println(log);
//                        logsBuilder.append(log).append("\n");
//                    }
//                    String message = logsBuilder.toString();
//                    Platform.runLater(() -> {
//                        logTextArea.appendText(message + "\n");
//                    });
////                }
//
//            }
//            }
//        };
//        new Thread(task).start();
//    }

//    @Override
//    public void interrupt() {
//        executorService.shutdownNow();
//        super.interrupt();
//    }

    @Override
    protected String call() throws Exception {
        while (true){
            String message = logQueue.take();
            if (message != null){

                updateValue(logQueue.take());

            }
        }
//        return null;
    }
}
