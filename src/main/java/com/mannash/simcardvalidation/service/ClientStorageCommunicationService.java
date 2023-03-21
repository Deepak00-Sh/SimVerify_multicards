package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.pojo.ResponseWorkOrderFieldTestingPojo;

public interface ClientStorageCommunicationService {

	public String fetchLocalStoredWoId();

	public void setWoToNull();

	public boolean storeWo(ResponseWorkOrderFieldTestingPojo responseWorkOrderFieldTestingPojo);
	
}
