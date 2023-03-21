package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.pojo.LogType;
import com.mannash.simcardvalidation.pojo.RequestClientLogPojo;
//import com.mannash.trakme.client.pojo.LogType;
//import com.mannash.trakme.client.pojo.RequestClientLogPojo;

public interface LoggerService {

	void logInfo(String message);
	
	void logError(String message);

	void logDebug(String message);

	RequestClientLogPojo pollLogs();

	void log(String message, String iccid, String woId, LogType logType);

}
