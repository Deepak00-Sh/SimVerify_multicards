package com.mannash.simcardvalidation.pojo;

import java.io.Serializable;

public class ExportTestingResultPojo implements Serializable {

    private String dateOfTesting;
    private String timeOfTesting;
    private String userName;
    private int terminalNumber;
    private String terminalICCID;
    private String terminalIMSI;
    private String simHeartbeat;
    private String fileSystemVerification;
    private String profileTesting;
    private String readWrite;
    private String testCompilation;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDateOfTesting() {
        return dateOfTesting;
    }

    public void setDateOfTesting(String dateOfTesting) {
        this.dateOfTesting = dateOfTesting;
    }

    public String getTimeOfTesting() {
        return timeOfTesting;
    }

    public void setTimeOfTesting(String timeOfTesting) {
        this.timeOfTesting = timeOfTesting;
    }


    public String getCardStatus() {
        return cardStatus;
    }

    public String getTerminalIMSI() {
        return terminalIMSI;
    }

    public void setTerminalIMSI(String terminalIMSI) {
        this.terminalIMSI = terminalIMSI;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    private String cardStatus;


    public int getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(int terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getTerminalICCID() {
        return terminalICCID;
    }

    public void setTerminalICCID(String terminalICCID) {
        this.terminalICCID = terminalICCID;
    }

    public String getSimHeartbeat() {
        return simHeartbeat;
    }

    public void setSimHeartbeat(String simHeartbeat) {
        this.simHeartbeat = simHeartbeat;
    }

    public String getFileSystemVerification() {
        return fileSystemVerification;
    }

    public void setFileSystemVerification(String fileSystemVerification) {
        this.fileSystemVerification = fileSystemVerification;
    }

    public String getProfileTesting() {
        return profileTesting;
    }

    public void setProfileTesting(String profileTesting) {
        this.profileTesting = profileTesting;
    }

    public String getReadWrite() {
        return readWrite;
    }

    public void setReadWrite(String readWrite) {
        this.readWrite = readWrite;
    }

    public String getTestCompilation() {
        return testCompilation;
    }

    public void setTestCompilation(String testCompilation) {
        this.testCompilation = testCompilation;
    }
}
