package com.mannash.simcardvalidation.service;

//import com.mannash.trakme.client.pojo.ResponseAuthenticationPojo;
//import com.mannash.trakme.client.pojo.ResponseFieldTestingCardInfos;
//import com.mannash.trakme.client.pojo.ResponseProfileTestingConfig;
//import com.mannash.trakme.client.pojo.ResponseStressTestingConfig;

import com.mannash.simcardvalidation.pojo.ResponseAuthenticationPojo;
import com.mannash.simcardvalidation.pojo.ResponseFieldTestingCardInfos;
import com.mannash.simcardvalidation.pojo.ResponseProfileTestingConfig;
import com.mannash.simcardvalidation.pojo.ResponseStressTestingConfig;

public interface TrakmeServerCommunicationService {

	public ResponseAuthenticationPojo authenticateClient(String userName, String password);
	
	public ResponseFieldTestingCardInfos fetchWorkOrderInfo(String userName) ;
	
	public ResponseProfileTestingConfig getProfileTestingConfig(String iccid, String woId, String userName);
	
	public ResponseStressTestingConfig getStressTestingConfig(String iccid, String woId, String userName);
	
	public void updateWOStatus(String woID, String iccid, String status, String userID);
	

}
