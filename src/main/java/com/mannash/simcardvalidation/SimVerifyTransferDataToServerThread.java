package com.mannash.simcardvalidation;

import com.mannash.simcardvalidation.pojo.ExportTestingResultPojo;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimVerifyTransferDataToServerThread extends Service<Void> {

    TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
    String userName = null;

    private String directoryPath = "..\\reports\\cache\\";
    private boolean running;

    public SimVerifyTransferDataToServerThread(String loggedInUserName) {
        this.userName = loggedInUserName;
        running = true;
    }

//    @Override
//    public void run() {
//        File directory = new File(directoryPath);
//        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
//        while (running) {
//            File[] files = directory.listFiles();
//            if (files != null && files.length > 0) {
//                System.out.println("Files found in directory " + directoryPath + ":");
//                for (File file : files) {
//                    System.out.println(file.getName());
//                    int responseCode = loadCacheFromDisk(file);
//                    if (responseCode == 200){
//                        file.delete();
//                    }
//                }
//            } else {
//                System.out.println("No files found in directory " + directoryPath);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            try {
//                Thread.sleep(5000); // wait for 5 seconds before checking again
//            } catch (InterruptedException e) {
//                System.out.println("File checker thread interrupted");
//            }
//        }
//    }

    private int loadCacheFromDisk(File file){
        try {
            // Create a FileInputStream to read the serialized data from the cache file
            File cacheFile = new File(file.toURI());
            FileInputStream fis = new FileInputStream(cacheFile);

            // Create an ObjectInputStream to deserialize the data and read it from the FileInputStream
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Read the list of data from the cache file and cast it to a List<String>
            List<ExportTestingResultPojo> data = (List<ExportTestingResultPojo>) ois.readObject();

            // Close the ObjectInputStream and FileInputStream
            ois.close();
            fis.close();

//            int responseCode = service.sendReportsToServer(this.userName, data);


            File logs = new File("..\\logs");
            if(!logs.exists()){
                logs.mkdir();
            }

            File testFile = new File("..\\logs\\test.txt");

            if (!testFile.exists()){
                testFile.createNewFile();
            }
            for (ExportTestingResultPojo element: data) {
                System.out.println(element.getTerminalICCID());
                FileWriter writer = new FileWriter(testFile, true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.append("Date : "+element.getDateOfTesting()
                        + " Time : "+element.getTimeOfTesting()
                        +" Iccid : "+element.getTerminalICCID()
                        +" Status : "+element.getCardStatus()
                        +"\n");
                bufferedWriter.close();
            }



            return 200;

            // Print the list of data t o the console
//            System.out.println("Data read from cache file:");
//            for (ExportTestingResultPojo s : data) {
//                System.out.println(s.getTerminalICCID());
//            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        return 0;
    }

    public void stop() {
        running = false;
    }

//    @Override
//    protected String call() throws Exception {
//        File directory = new File(directoryPath);
//        TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
//        while (running) {
//            File[] files = directory.listFiles();
//            if (files != null && files.length > 0) {
//                System.out.println("Files found in directory " + directoryPath + ":");
//                for (File file : files) {
//                    System.out.println(file.getName());
//                    int responseCode = loadCacheFromDisk(file);
//                    if (responseCode == 200){
//                        file.delete();
//                    }
//                }
//            } else {
//                System.out.println("No files found in directory " + directoryPath);
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            try {
//                Thread.sleep(5000); // wait for 5 seconds before checking again
//            } catch (InterruptedException e) {
//                System.out.println("File checker thread interrupted");
//            }
//        }
//        return null;
//    }






    // #################################


    //        private final Path directoryPath;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

//        public DirectoryMonitorService(Path directoryPath) {
//            this.directoryPath = directoryPath;
//            this.executorService = ;
//        }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File directory = new File(directoryPath);
                TrakmeServerCommunicationServiceImpl service = new TrakmeServerCommunicationServiceImpl();
                while (running) {
                    File[] files = directory.listFiles();
                    if (files != null && files.length > 0) {
                        System.out.println("Files found in directory " + directoryPath + ":");
                        for (File file : files) {
                            System.out.println(file.getName());
                            int responseCode = loadCacheFromDisk(file);
                            if (responseCode == 200){
                                file.delete();
                            }
                        }
                    } else {
                        System.out.println("No files found in directory " + directoryPath);
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep(5000); // wait for 5 seconds before checking again
                    } catch (InterruptedException e) {
                        System.out.println("File checker thread interrupted");
                    }
                }
                return null;
            }
        };
    }

    @Override
    public boolean cancel() {
        super.cancel();
        executorService.shutdownNow();
        return false;
    }


}
