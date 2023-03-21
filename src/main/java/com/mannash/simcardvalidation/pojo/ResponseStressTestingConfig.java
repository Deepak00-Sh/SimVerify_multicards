package com.mannash.simcardvalidation.pojo;

import java.util.List;

public class ResponseStressTestingConfig {
	
	List<String> apduList;
	long loopCount;
	long startCounter;
	
	List<String> reverseApduList;
	long reverseLoopCount;
	long reverseStartCounter;

	public List<String> getReverseApduList() {
		return reverseApduList;
	}

	public void setReverseApduList(List<String> reverseApduList) {
		this.reverseApduList = reverseApduList;
	}

	public long getReverseLoopCount() {
		return reverseLoopCount;
	}

	public void setReverseLoopCount(long reverseLoopCount) {
		this.reverseLoopCount = reverseLoopCount;
	}

	public long getReverseStartCounter() {
		return reverseStartCounter;
	}

	public void setReverseStartCounter(long reverseStartCounter) {
		this.reverseStartCounter = reverseStartCounter;
	}

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

	public long getStartCounter() {
		return startCounter;
	}

	public void setStartCounter(long startCounter) {
		this.startCounter = startCounter;
	}
	
	
}
