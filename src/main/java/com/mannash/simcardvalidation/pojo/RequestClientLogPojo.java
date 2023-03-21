package com.mannash.simcardvalidation.pojo;

//import com.mannash.trakme.client.pojo.LogType;

public class RequestClientLogPojo {

//	private com.mannash.trakme.client.pojo.LogType logType;

	private LogType logType;
	private String logMessage;
	private String iccid;
	private String woId;
	@Override
	public String toString() {
		return "RequestClientLogPojo [logType=" + logType + ", logMessage=" + logMessage + ", iccid=" + iccid
				+ ", woId=" + woId + ", timeStamp=" + timeStamp + "]";
	}
	private String timeStamp;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
	public String getWoId() {
		return woId;
	}
	public void setWoId(String woId) {
		this.woId = woId;
	}
	
	
	public LogType getLogType() {
		return logType;
	}
	public void setLogType(LogType logType) {
		this.logType = logType;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	
}
