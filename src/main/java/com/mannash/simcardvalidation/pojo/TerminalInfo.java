package com.mannash.simcardvalidation.pojo;

import javax.smartcardio.CardTerminal;
@SuppressWarnings("restriction")
public class TerminalInfo {

	@Override
	public String toString() {
		return "TerminalInfo [terminalNumber=" + terminalNumber + ", terminalCardIccid=" + terminalCardIccid + "]";
	}
	
	private String woId;
	private CardTerminal ct;
	private ResponseFieldTestingCardPojo fieldTestingCard;
	private int terminalNumber;
	private String terminalCardIccid;
	private String userName;
	private String imsi;

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public ResponseFieldTestingCardPojo getFieldTestingCard() {
		return fieldTestingCard;
	}
	public void setFieldTestingCard(ResponseFieldTestingCardPojo fieldTestingCard) {
		this.fieldTestingCard = fieldTestingCard;
	}
	
	public CardTerminal getCt() {
		return ct;
	}
	
	public void setCt(CardTerminal ct) {
		this.ct = ct;
	}
	
	public int getTerminalNumber() {
		return terminalNumber;
	}
	
	public void setTerminalNumber(int terminalNumber) {
		this.terminalNumber = terminalNumber;
	}
	
	public String getTerminalCardIccid() {
		return terminalCardIccid;
	}
	public void setTerminalCardIccid(String terminalCardIccid) {
		this.terminalCardIccid = terminalCardIccid;
	}
	public String getWoId() {
		return woId;
	}
	public void setWoId(String woId) {
		this.woId = woId;
	}
	
	
	
}
