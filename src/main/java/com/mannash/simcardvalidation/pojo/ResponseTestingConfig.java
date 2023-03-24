package com.mannash.simcardvalidation.pojo;

import java.util.List;

public class ResponseTestingConfig {
    List<String> apduList;
    long loopCount;
    List<String> fileSystemConfig;
    List<String> fileContentConfig;
    List<String> fileVerificationSystemConfig;
    List<String> fileVerificationContentConfig;
    public List<String> getApduList() {
        return apduList;
    }

    public void setApduList(List<String> apduList) {
        this.apduList = apduList;
    }


    public long getLoopCount() {
        return loopCount;
    }

    public void setLoopCount(long loopCount) {
        this.loopCount = loopCount;
    }

    public List<String> getFileSystemConfig() {
        return fileSystemConfig;
    }

    public void setFileSystemConfig(List<String> fileSystemConfig) {
        this.fileSystemConfig = fileSystemConfig;
    }

    public List<String> getFileContentConfig() {
        return fileContentConfig;
    }

    public void setFileContentConfig(List<String> fileContentConfig) {
        this.fileContentConfig = fileContentConfig;
    }

    public List<String> getFileVerificationSystemConfig() {
        return fileVerificationSystemConfig;
    }

    public void setFileVerificationSystemConfig(List<String> fileVerificationSystemConfig) {
        this.fileVerificationSystemConfig = fileVerificationSystemConfig;
    }

    public List<String> getFileVerificationContentConfig() {
        return fileVerificationContentConfig;
    }

    public void setFileVerificationContentConfig(List<String> fileVerificationContentConfig) {
        this.fileVerificationContentConfig = fileVerificationContentConfig;
    }


}
