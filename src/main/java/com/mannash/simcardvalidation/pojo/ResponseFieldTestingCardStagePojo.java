package com.mannash.simcardvalidation.pojo;

//import com.mannash.trakmeserver.schema.common.FieldTestingStageStatus;

public class ResponseFieldTestingCardStagePojo {
	
	private int fieldTestingStageId;
//	private String fieldTestingStageName;
	private int fieldTestingCardStageId;
	private FieldTestingStageStatus fieldTestingStageStatus;
	private long fieldTestingStageMaxCounter;
//	private long fieldTestingStageCurrentCounter;

	public long getFieldTestingStageMaxCounter() {
		return fieldTestingStageMaxCounter;
	}
	public void setFieldTestingStageMaxCounter(long fieldTestingStageMaxCounter) {
		this.fieldTestingStageMaxCounter = fieldTestingStageMaxCounter;
	}
//	public long getFieldTestingStageCurrentCounter() {
//		return fieldTestingStageCurrentCounter;
//	}
//	public void setFieldTestingStageCurrentCounter(long fieldTestingStageCurrentCounter) {
//		this.fieldTestingStageCurrentCounter = fieldTestingStageCurrentCounter;
//	}
	public int getFieldTestingCardStageId() {
		return fieldTestingCardStageId;
	}
	public void setFieldTestingCardStageId(int fieldTestingCardStageId) {
		this.fieldTestingCardStageId = fieldTestingCardStageId;
	}
	
	public int getFieldTestingStageId() {
		return fieldTestingStageId;
	}
	public void setFieldTestingStageId(int fieldTestingStageId) {
		this.fieldTestingStageId = fieldTestingStageId;
	}
//	public String getFieldTestingStageName() {
//		return fieldTestingStageName;
//	}
//	public void setFieldTestingStageName(String fieldTestingStageName) {
//		this.fieldTestingStageName = fieldTestingStageName;
//	}
	public FieldTestingStageStatus getFieldTestingStageStatus() {
		return fieldTestingStageStatus; 
	}
	public void setFieldTestingStageStatus(FieldTestingStageStatus fieldTestingStageStatus) {
		this.fieldTestingStageStatus = fieldTestingStageStatus;
	
	}
	
	

}
