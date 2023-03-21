package com.mannash.simcardvalidation.threads;

import com.mannash.simcardvalidation.service.LoggerService;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationService;


//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.TrakmeServerCommunicationService;

public class FieldTestingRunnerThread extends Thread{
	private TrakmeServerCommunicationService communicationService;
	LoggerService loggerService;
	
	public FieldTestingRunnerThread(LoggerService loggerService) {
		super();
		this.loggerService = loggerService;
	}

	public void run() {
		try {
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
