package com.mannash.simcardvalidation.pojo;

import java.util.List;

public class ResponseWorkOrderFieldTestingInfoPojo {

	private List<ResponseWorkOrderFieldTestingPojo> responseWorkOrderFieldTestingPojo;

	public List<ResponseWorkOrderFieldTestingPojo> getResponseWorkOrderFieldTestingPojo() {
		return responseWorkOrderFieldTestingPojo;
	}

	public void setResponseWorkOrderFieldTestingPojo(List<ResponseWorkOrderFieldTestingPojo> responseWorkOrderFieldTestingPojo) {
		this.responseWorkOrderFieldTestingPojo = responseWorkOrderFieldTestingPojo;
	}

	@Override
	public String toString() {
		return responseWorkOrderFieldTestingPojo.toString();
	}
	
}
