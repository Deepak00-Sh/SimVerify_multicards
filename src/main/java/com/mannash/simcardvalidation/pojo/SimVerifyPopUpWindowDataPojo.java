package com.mannash.simcardvalidation.pojo;

import java.io.Serializable;

public class SimVerifyPopUpWindowDataPojo implements Serializable {
    private String cardWidgetID;
    private Boolean isSIMHeartBeatSuccessful;
    private Boolean isFileSystemVerificationSuccessful;
    private Boolean isProfileTestingSuccessful;
    private Boolean isReadWriteTestingSuccessful;
    private Boolean isCardOK;

    public String getCardWidgetID() {
        return cardWidgetID;
    }

    public void setCardWidgetID(String cardWidgetID) {
        this.cardWidgetID = cardWidgetID;
    }

    public Boolean getSIMHeartBeatSuccessful() {
        return isSIMHeartBeatSuccessful;
    }

    public void setSIMHeartBeatSuccessful(Boolean SIMHeartBeatSuccessful) {
        isSIMHeartBeatSuccessful = SIMHeartBeatSuccessful;
    }

    public Boolean getFileSystemVerificationSuccessful() {
        return isFileSystemVerificationSuccessful;
    }

    public void setFileSystemVerificationSuccessful(Boolean fileSystemVerificationSuccessful) {
        isFileSystemVerificationSuccessful = fileSystemVerificationSuccessful;
    }

    public Boolean getProfileTestingSuccessful() {
        return isProfileTestingSuccessful;
    }

    public void setProfileTestingSuccessful(Boolean profileTestingSuccessful) {
        isProfileTestingSuccessful = profileTestingSuccessful;
    }

    public Boolean getReadWriteTestingSuccessful() {
        return isReadWriteTestingSuccessful;
    }

    public void setReadWriteTestingSuccessful(Boolean readWriteTestingSuccessful) {
        isReadWriteTestingSuccessful = readWriteTestingSuccessful;
    }

    public Boolean getCardOK() {
        return isCardOK;
    }

    public void setCardOK(Boolean cardOK) {
        isCardOK = cardOK;
    }
}
