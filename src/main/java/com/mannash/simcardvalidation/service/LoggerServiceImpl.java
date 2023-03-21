package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.pojo.LogType;
import com.mannash.simcardvalidation.pojo.RequestClientLogPojo;
import com.mannash.simcardvalidation.threads.FieldTestingLoggerThread;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//import com.mannash.trakme.client.card.StressTest;
//import com.mannash.trakme.client.pojo.LogType;
//import com.mannash.trakme.client.pojo.RequestClientLogPojo;
//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.TrakmeServerCommunicationServiceImpl;
//import com.mannash.trakme.client.threads.FieldTestingLoggerThread;

public class LoggerServiceImpl implements LoggerService {

	private BlockingQueue<RequestClientLogPojo> blockingQueue = new ArrayBlockingQueue<RequestClientLogPojo>(10000);
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private Properties properties = new Properties();

	public LoggerServiceImpl()
	{
		FieldTestingLoggerThread fieldTestingLoggerThread = new FieldTestingLoggerThread(this,new TrakmeServerCommunicationServiceImpl());
		fieldTestingLoggerThread.start();
	}
	
	public RequestClientLogPojo pollLogs() {
		return blockingQueue.poll();
	}
	
	public void createFieldTestLogFolder() {
		File directory = new File(this.properties.getProperty("fieldTestingLogsPath"));
		if(!directory.exists()) {
			directory.mkdir();
 		}
	}
	
	public void log(String message, String iccid, String woId, LogType logType) {

		RequestClientLogPojo requestLogPojo =  new RequestClientLogPojo();
		requestLogPojo.setLogType(logType);
		requestLogPojo.setLogMessage(message);
		requestLogPojo.setIccid(iccid);
		requestLogPojo.setWoId(woId);
		requestLogPojo.setTimeStamp(sdf.format(new Date()));
		blockingQueue.offer(requestLogPojo );		
	}
	
	public void logInfo(String message) {

		RequestClientLogPojo requestLogPojo =  new RequestClientLogPojo();
		requestLogPojo.setLogType(LogType.INFO);
		requestLogPojo.setLogMessage(message);
		blockingQueue.offer(requestLogPojo );		
	}

	public void logError(String message) {
		RequestClientLogPojo requestLogPojo =  new RequestClientLogPojo();
		requestLogPojo.setLogType(LogType.ERROR);
		requestLogPojo.setLogMessage(message);
		blockingQueue.add(requestLogPojo );		
	}
	
	public void logDebug(String message) {
		RequestClientLogPojo requestLogPojo =  new RequestClientLogPojo();
		requestLogPojo.setLogType(LogType.DEBUG);
		requestLogPojo.setLogMessage(message);
		blockingQueue.add(requestLogPojo );		
	}


}
