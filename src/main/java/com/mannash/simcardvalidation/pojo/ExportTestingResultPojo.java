package com.mannash.simcardvalidation.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ExportTestingResultPojo implements Serializable {

    private String dateOfTesting;


    private String timeOfTesting;

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

    private int terminalNumber;
    private String terminalICCID;
    private String terminalIMSI;
    private String SIMHeartbeat;
    private String fileSystemVerification;
    private String profileTesting;
    private String readWrite;
    private String testCompilation;

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

    public String getSIMHeartbeat() {
        return SIMHeartbeat;
    }

    public void setSIMHeartbeat(String SIMHeartbeat) {
        this.SIMHeartbeat = SIMHeartbeat;
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
