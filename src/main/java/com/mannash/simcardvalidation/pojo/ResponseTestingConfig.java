package com.mannash.simcardvalidation.pojo;

import java.util.List;

public class ResponseTestingConfig {
    //    List<String> apduList;
//    long loopCount;
//    List<String> fileSystemConfig;
//    List<String> fileContentConfig;
    List<String> fileVerificationSystemConfigs;

    public List<String> getFileVerificationSystemConfigs() {
        return fileVerificationSystemConfigs;
    }

    public void setFileVerificationSystemConfigs(List<String> fileVerificationSystemConfigs) {
        this.fileVerificationSystemConfigs = fileVerificationSystemConfigs;
    }

    public List<String> getFileVerificationContentConfigs() {
        return fileVerificationContentConfigs;
    }

    public void setFileVerificationContentConfigs(List<String> fileVerificationContentConfigs) {
        this.fileVerificationContentConfigs = fileVerificationContentConfigs;
    }

    List<String> fileVerificationContentConfigs;


}
