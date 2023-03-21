package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.pojo.TerminalInfo;

import java.util.List;

public interface TerminalConnectService {

	public List<TerminalInfo> fetchTerminalInfo() ;
	public int fetchTerminalCount();
}
