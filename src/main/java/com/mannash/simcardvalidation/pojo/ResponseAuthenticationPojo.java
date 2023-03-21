package com.mannash.simcardvalidation.pojo;

public class ResponseAuthenticationPojo {

	private int statusCode;
	private String message;
	private String userName;
	private String usrDisplayName;

	public String getUsrDisplayName() {
		return usrDisplayName;
	}

	public void setUsrDisplayName(String usrDisplayName) {
		this.usrDisplayName = usrDisplayName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
