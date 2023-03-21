package com.mannash.simcardvalidation.threads;//package com.mannash.simcardvalidation.threads;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//
//import com.mannash.simcardvalidation.pojo.TerminalInfo;
//import com.mannash.simcardvalidation.service.*;
////import com.mannash.trakme.client.pojo.ResponseFieldTestingCardInfos;
////import com.mannash.trakme.client.pojo.ResponseFieldTestingCardPojo;
////import com.mannash.trakme.client.pojo.TerminalInfo;
////import com.mannash.trakme.client.service.ClientStorageCommunicationService;
////import com.mannash.trakme.client.service.FieldTestingControllerService;
////import com.mannash.trakme.client.service.LoggerService;
////import com.mannash.trakme.client.service.LoggerServiceImpl;
////import com.mannash.trakme.client.service.TerminalConnectService;
////import com.mannash.trakme.client.service.TerminalConnectServiceImpl;
////import com.mannash.trakme.client.service.TrakmeServerCommunicationService;
////import com.mannash.trakme.client.service.TrakmeServerCommunicationServiceImpl;
////import com.mannash.trakmeserver.rest.service.FieldTestingClientLoggerServiceImpl;
////import com.mannash.trakme.client.threads.TestExecutor;
//
//public class FieldTestingMasterThread {
//
//	private long sleepInterval = 10000;
//	private LoggerService loggerService;
//	private TrakmeServerCommunicationService trakmeServerCommunicationService;
//	private ClientStorageCommunicationService clientStorageCommunicationService;
//	private FieldTestingControllerService fieldTestingControllerService;
//	private TerminalConnectService terminalConnectService;
//	private String loggedinUsername;
//
//	public StartTestCase getStartTestCase() {
//		return startTestCase;
//	}
//
//	public void setStartTestCase(StartTestCase startTestCase) {
//		this.startTestCase = startTestCase;
//	}
//
//	private StartTestCase startTestCase;
//
//
//
////	private final Logger logger = LoggerFactory.getLogger(FieldTestingClientLoggerServiceImpl.class);
//	private boolean testingStatus = true ;
//	@SuppressWarnings("unused")
//	private String currentRunningWoId = null;
//
//	public FieldTestingMasterThread() {
//		this.loggerService = new LoggerServiceImpl();
//		terminalConnectService = new TerminalConnectServiceImpl();
//		trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
//
//	}
//
//
//	static Map<String, Thread> testThreadMap = Collections.synchronizedMap(new HashMap<String, Thread>(20));
//
//	public void start() {
//
//			try {
//				// // this.logger.info("Running FieldTestingMasterThread");
//				setLoggedinUsername("mudit.sharma@mannash.com");
////				ResponseFieldTestingCardInfos serverResponse = this.trakmeServerCommunicationService
////						.fetchWorkOrderInfo(this.getLoggedinUsername());
////				if (serverResponse == null) {
//////					  this.logger.info("No WorkOrder to process");
////					System.out.println("No workOrder to process");
////					Thread.sleep(10000);
////					continue;
////				}
//				// Stop the pausing or cancelling WO
////				Iterator<ResponseFieldTestingCardPojo> fieldTestingCards = serverResponse
////						.getResponseFieldTestingCardPojos().iterator();
////				while (fieldTestingCards.hasNext()) {
//////					System.out.println("Check point 2");
//////					System.out.println("Iterator checking cards inside the main while loop...");
////					ResponseFieldTestingCardPojo fieldTestingCardPojo = fieldTestingCards.next();
////					String serverStatus = fieldTestingCardPojo.getFieldTestingStatus();
////					String woID = "" + fieldTestingCardPojo.getWoId();
////					String serverIccid = fieldTestingCardPojo.getCardIccid();
////					switch (serverStatus) {
////					case "PAUSING":
////						// Stop any running thread
////						stopAnyLocalRunningThread(serverIccid);
////						// Send PAUSED status to server
////						updateWOStatus(woID, serverIccid, "PAUSED");
////						break;
////					case "CANCELLING":
////						// Stop any running thread
////						stopAnyLocalRunningThread(serverIccid);
////						// Send CANCELLED status to server
////						updateWOStatus(woID, serverIccid, "CANCELLED");
////						break;
////					}
////				}
//
//				List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
////				System.out.println("Terminal infos : "+ terminalInfos);
//				if(terminalInfos.size()==0) {
////					System.out.println("Check point 3");
////					System.out.println("Terminal iterator has no data.");
//					terminalInfos = terminalConnectService.fetchTerminalInfo();
////					System.out.println("Terminal infos : "+ terminalInfos);
//				}
//
////				List<ResponseFieldTestingCardPojo> fieldTestingCardPojos = serverResponse
////						.getResponseFieldTestingCardPojos();
//
//				Iterator<TerminalInfo> terminalInfo = terminalInfos.iterator();
//
//				while (terminalInfo.hasNext()) {
//					System.out.println("Terminal iterator inside the sub-while loop...");
//					TerminalInfo terminal = terminalInfo.next();
//					terminal.setUserName(this.getLoggedinUsername());
//					String localIccid = terminal.getTerminalCardIccid();
//					int terminalId = terminal.getTerminalNumber();
//					terminal.setWoId("1184");
//					System.out.println("localIccid : "+localIccid);
////					startTestingThread(terminal);
//
//				}
//			} catch (Exception e) {
////				 this.logger.error("Exception occured in FieldTestingMasterThread" + e.getMessage());
////				e.printStackTrace();
//			}
//	}
//
//
////	private void startTestingThread(TerminalInfo terminal) {
////		System.out.println("inside the start testing");
//////		StartTestCase startTestCase = new StartTestCase(terminal,this.loggerService);
////		startTestCase.setStartTestCase(terminal,this.loggerService);
////
////		System.out.println("after object ");
//////		StartTestCase startTestCase = new StartTestCase();
////		startTestCase.atrVerification();
////		System.out.println("1");
////		startTestCase.fileSystemVerification();
////		System.out.println("2");
////		startTestCase.profileValidation();
////		System.out.println("3");
////		startTestCase.readWriteTest();
////		startTestCase.resultCompilation();
////
////	}
//
//	private boolean checkThreadRunningStatus(String localIccid) {
//		if (testThreadMap.get(localIccid) == null) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	@SuppressWarnings("deprecation")
//	public void stopAnyLocalRunningThread(String localIccid) {
//		System.out.println("Got Request to stop local running thread for ICCID : " + localIccid);
////		 this.logger.debug("Got Request to stop local running thread for ICCID : " + localIccid);
//		if (testThreadMap.get(localIccid) != null) {
//			// this.logger.debug("Stopping local running thread for ICCID : " + localIccid);
//			System.out.println("Stopping local running thread for ICCID : " + localIccid);
//			Thread t = this.testThreadMap.remove(localIccid);
//			// this.logger.debug("Stopping thread : " + t.getName());
//			System.out.println("thread map after removing ICCID : "+this.testThreadMap);
////			this.logger.debug("thread map after removing ICCID : "+this.testThreadMap);
////			t.interrupt();
//			t.stop();
////			interrupt();
//		} else {
////			  this.logger.info("No any local running thread found to stop for ICCID" + localIccid);
//		}
//	}
//
//	public void removeThread(String localIccid) {
//
//	}
//
//	private void updateWOStatus(String woID, String iccid, String status) {
//		this.trakmeServerCommunicationService.updateWOStatus(woID, iccid, status, this.getLoggedinUsername());
//	}
//
//	@SuppressWarnings("unused")
//	private void startWoTesting() {
////		  this.logger.info("Fetching Terminal Info to start WO testing");
//		List<TerminalInfo> terminalInfos = terminalConnectService.fetchTerminalInfo();
//		if (terminalInfos.size() > 0) {
////			  this.logger.info("Number of terminals fetched with card " + terminalInfos.size());
//		} else {
////			  this.logger.info("No terminals fetched connected card");
//		}
//	}
//
//	public LoggerService getLoggerService() {
//		return this.loggerService;
//	}
//
//	public void setLoggerService(LoggerService loggerService) {
//		this.loggerService = loggerService;
//	}
//
//	public TrakmeServerCommunicationService getTrakmeServerCommunicationService() {
//		return this.trakmeServerCommunicationService;
//	}
//
//	public void setTrakmeServerCommunicationService(TrakmeServerCommunicationService trakmeServerCommunicationService) {
//		this.trakmeServerCommunicationService = trakmeServerCommunicationService;
//	}
//
//	public ClientStorageCommunicationService getClientStorageCommunicationService() {
//		return clientStorageCommunicationService;
//	}
//
//	public void setClientStorageCommunicationService(
//			ClientStorageCommunicationService clientStorageCommunicationService) {
//		this.clientStorageCommunicationService = clientStorageCommunicationService;
//	}
//
//	public String getLoggedinUsername() {
//		return this.loggedinUsername;
//	}
//
//	public void setLoggedinUsername(String loggedinUsername) {
//		this.loggedinUsername = loggedinUsername;
//	}
//
//	public FieldTestingControllerService getFieldTestingControllerService() {
//		return this.fieldTestingControllerService;
//	}
//
//	public void setFieldTestingControllerService(FieldTestingControllerService fieldTestingControllerService) {
//		this.fieldTestingControllerService = fieldTestingControllerService;
//	}
//
//	public static void main(String[] args) {
//
//		FieldTestingMasterThread thread = new FieldTestingMasterThread();
//		// thread.setLoggerService(loggerService);
////		thread.start();
//		thread.start();
//	}
//
//	public boolean isTestingStatus() {
//		return testingStatus;
//	}
//
//	public void setTestingStatus(boolean testingStatus) {
//		this.testingStatus = testingStatus;
//	}
//}