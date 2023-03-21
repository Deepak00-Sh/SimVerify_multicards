package com.mannash.simcardvalidation.pojo;

import java.util.List;

public class ResponseProfileTestingConfig {
	
	List<String> fileSystemConfig;
	List<String> fileContentConfig;

	public List<String> getFileVerificationSystemConfig() {
		return fileVerificationSystemConfig;
	}

	public void setFileVerificationSystemConfig(List<String> fileVerificationSystemConfig) {
		this.fileVerificationSystemConfig = fileVerificationSystemConfig;
	}

	List<String> fileVerificationSystemConfig;

	public List<String> getFileVerificationContentConfig() {
		return fileVerificationContentConfig;
	}

	public void setFileVerificationContentConfig(List<String> fileVerificationContentConfig) {
		this.fileVerificationContentConfig = fileVerificationContentConfig;
	}

	List<String> fileVerificationContentConfig;
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
}
