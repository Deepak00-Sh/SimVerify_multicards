package com.mannash.simcardvalidation.pojo;

//import com.mannash.trakme.client.pojo.ResponseFieldTestingCardStagePojo;

import java.util.List;

public class ResponseFieldTestingCardPojo {
	
	
	private String cardIccid;
	private double cardTestingPercentage;
	private String fieldTestingStatus;
//	private int fieldTestingCardId;
	private int woId;
	private List<ResponseFieldTestingCardStagePojo> fieldTestingCardStagePojos;
	
	public int getWoId() {
		return woId;
	}

	public void setWoId(int woId) {
		this.woId = woId;
	}

//	public int getFieldTestingCardId() {
//		return fieldTestingCardId;
//	}
//	
//	public void setFieldTestingCardId(int fieldTestingCardId) {
//		this.fieldTestingCardId = fieldTestingCardId;
//	}
	
	public String getFieldTestingStatus() {
		return fieldTestingStatus;
	}

	public void setFieldTestingStatus(String fieldTestingStatus) {
		this.fieldTestingStatus = fieldTestingStatus;
	}

	

	public String getCardIccid() {
		return cardIccid;
	}

	public void setCardIccid(String cardIccid) {
		this.cardIccid = cardIccid;
	}

	public double getCardTestingPercentage() {
		return cardTestingPercentage;
	}

	public void setCardTestingPercentage(double cardTestingPercentage) {
		this.cardTestingPercentage = cardTestingPercentage;
	}

	public List<ResponseFieldTestingCardStagePojo> getFieldTestingCardStagePojos() {
		return fieldTestingCardStagePojos;
	}

	public void setFieldTestingCardStagePojos(List<ResponseFieldTestingCardStagePojo> fieldTestingCardStagePojos) {
		this.fieldTestingCardStagePojos = fieldTestingCardStagePojos;
	}

//	public void setFieldTestingStatus(String tmWoftStatus) {
//		// TODO Auto-generated method stub
//		
//	}

}
