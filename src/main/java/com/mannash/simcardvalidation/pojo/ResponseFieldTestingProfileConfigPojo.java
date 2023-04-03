package com.mannash.simcardvalidation.pojo;

import java.util.List;


public class ResponseFieldTestingProfileConfigPojo extends ResponseGenericPojo {

    private int id;
    private String profileName;
    private ProfileType profileType;
    private List<String> fileSystemConfigs;
    private List<String> fileContentConfigs;
    private List<String> stressTestingApdus;
    private long stressTestingLoopCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        this.profileType = profileType;
    }

    public List<String> getFileSystemConfigs() {
        return fileSystemConfigs;
    }

    public void setFileSystemConfigs(List<String> fileSystemConfigs) {
        this.fileSystemConfigs = fileSystemConfigs;
    }

    public List<String> getFileContentConfigs() {
        return fileContentConfigs;
    }

    public void setFileContentConfigs(List<String> fileContentConfigs) {
        this.fileContentConfigs = fileContentConfigs;
    }

    public List<String> getStressTestingApdus() {
        return stressTestingApdus;
    }

    public void setStressTestingApdus(List<String> stressTestingApdus) {
        this.stressTestingApdus = stressTestingApdus;
    }

    public long getStressTestingLoopCount() {
        return stressTestingLoopCount;
    }

    public void setStressTestingLoopCount(long stressTestingLoopCount) {
        this.stressTestingLoopCount = stressTestingLoopCount;
    }

    @Override
    public String toString() {
        return "ResponseFieldTestingProfileConfigPojo [id=" + id + ", profileName=" + profileName + ", profileType="
                + profileType + ", fileSystemConfigs=" + fileSystemConfigs + ", fileContentConfigs="
                + fileContentConfigs + ", stressTestingApdus=" + stressTestingApdus + ", stressTestingLoopCount="
                + stressTestingLoopCount + "]";
    }

}
