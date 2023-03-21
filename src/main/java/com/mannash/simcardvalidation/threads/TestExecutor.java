package com.mannash.simcardvalidation.threads;//package com.mannash.simcardvalidation.threads;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.*;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//import javax.smartcardio.Card;
//import javax.smartcardio.CardException;
//
////import com.mannash.trakme.client.threads.FieldTestingMasterThread;
//import com.mannash.simcardvalidation.TestingController;
//import com.mannash.simcardvalidation.card.ProfileTest3G;
//import com.mannash.simcardvalidation.card.StressTest;
//import com.mannash.simcardvalidation.pojo.LogType;
//import com.mannash.simcardvalidation.pojo.ResponseFieldTestingCardStagePojo;
//import com.mannash.simcardvalidation.pojo.TerminalInfo;
//import com.mannash.simcardvalidation.service.LoggerService;
//import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
////import com.mannash.trakme.client.card.ProfileTest3G;
////import com.mannash.trakme.client.card.StressTest;
////import com.mannash.trakme.client.pojo.LogType;
////import com.mannash.trakme.client.pojo.ResponseFieldTestingCardStagePojo;
////import com.mannash.trakme.client.pojo.TerminalInfo;
////import com.mannash.trakme.client.service.LoggerService;
////import com.mannash.trakme.client.service.TrakmeServerCommunicationServiceImpl;
//
//@SuppressWarnings("restriction")
//public class TestExecutor {
//
//	TestingController testingController = new TestingController();
//	private final Logger logger = LoggerFactory.getLogger(TestExecutor.class);
//	// String counterFilePath = "%TEMP%\\TrakMe\\client\\trace\\";
//	String counterFilePath = "";
//	private String localIccid;
//	private String woId;
//	private TerminalInfo terminal;
//	StressTest stressTest;
//	//private TestThreadMap threadMap = new TestThreadMap();
//	private Card card = null;
//	private AtomicBoolean running = new AtomicBoolean(false);
//	public boolean stressTestingStatus = true ;
//	LoggerService loggerService;
//	boolean stressTestingSuccessful = true;
//	boolean profileTestingSuccessful = true;
//	boolean fieldTestingStatus = true;
//
//
//
//	public TestExecutor() {
//
//	}
//	public TestExecutor(String threadName, TerminalInfo terminal, LoggerService loggerService) {
//		super();
//		this.loggerService = loggerService;
//		this.localIccid = terminal.getTerminalCardIccid();
//		this.woId = terminal.getWoId();
//		this.terminal = terminal;
//		try {
//			this.card = this.terminal.getCt().connect("T=0");
//			this.loggerService.log("Card conncted successfully. ", this.localIccid, this.woId, LogType.INFO);
//			this.logger.debug("Card conncted successfully : " + this.localIccid + "WorkOrderID : " + this.woId);
//		} catch (CardException e1) {
//			this.loggerService.log("Error while connecting to card. " + e1.toString(), this.localIccid, this.woId,
//					LogType.ERROR);
//			 this.logger.error("Error while connecting to card : " + e1.toString() + "ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//			e1.printStackTrace();
//			updateCardStatus(this.terminal, "ERROR");
//			running.set(false);
//		}
//		this.stressTest = new StressTest(this.terminal, this.card, this.loggerService);
//
//	}
//
//	public void interrupt() {
//		try {
//			// this.logger.debug("Got interrupt");
//			this.stressTest.stopStressTesting();
//			this.card.disconnect(true);
//		} catch (CardException e) {
//			e.printStackTrace();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		running.set(false);
//	}
//
//	boolean isRunning() {
//		return running.get();
//	}
//
//	public void run() {
//
//		this.loggerService.log("Testing started for ICCID " + this.localIccid, this.localIccid, this.woId,
//				LogType.INFO);
//		 this.logger.debug("Testing started for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//		running.set(true);
////		String testingStageFileName = this.woId + "_" + this.localIccid + "_ts" + ".txt";
////
////		File fileTestingStage = new File(this.counterFilePath + testingStageFileName);
////		boolean fileAlreadyExist = checkFileStatus(fileTestingStage);
////		// this.logger.info("fileAlreadyExist:" + fileAlreadyExist);
////		if (!fileAlreadyExist) {
////			try {
////				createFileAndStartCounter(fileTestingStage);
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		}
////
////		// this.logger.info("file testing stage : " + fileTestingStage.getAbsolutePath());
//// 		setTestingStartStage(this.terminal.getFieldTestingCard());
//
//		while (running.get()) {
//			try {
////				List<ResponseFieldTestingCardStagePojo> a = this.terminal.getFieldTestingCard()
////						.getFieldTestingCardStagePojos();
////				Iterator<ResponseFieldTestingCardStagePojo> fieldTestingCardStagesIterator = a.iterator();
////				while (fieldTestingCardStagesIterator.hasNext()) {
////					ResponseFieldTestingCardStagePojo fieldTestingCardStage = fieldTestingCardStagesIterator.next();
////					int stageId = fieldTestingCardStage.getFieldTestingStageId();
////					String stageTstingStatus = fieldTestingCardStage.getFieldTestingStageStatus().toString();
////
////
////					// check if this stage needs to be started
////					if (!stageTstingStatus.equalsIgnoreCase("COMPLETED")) {
////						// if yes then send the ongoing status for stage and retun the stageid and break
////						// the while loop
////						updateStageStatus(this.localIccid, this.woId,
////								fieldTestingCardStage.getFieldTestingCardStageId(), "ONGOING");
//				List<Integer> stageNumber = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
//				Iterator<Integer> stageNumberIterator = stageNumber.iterator();
//				while (stageNumberIterator.hasNext()){
//					int stageId = stageNumberIterator.next();
//						switch (stageId) {
//
//							case 1:
////								AtrVerication();
//								testingController.atrVerification(this.localIccid,true);
//
//							case 2:
////								fileSystemVerificationI();
//								testingController.fileSystemVerification(true);
//							case 3:
//							//TODO System.out.println("Starting Profile testing");
//							this.loggerService.log("STARTING PROFILE TESTING ", this.localIccid, this.woId,
//									LogType.INFO);
//							 this.logger.debug("Starting Profile Testing for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//							this.profileTestingSuccessful = this.startProfilevalidationTesting();
//
//							//TODO System.out.println("Profile test over");
//							this.loggerService.log("PROFILE TESTING OVER. ", this.localIccid, this.woId, LogType.INFO);
//							 this.logger.debug("Profile Testing Over for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//
////							this.updateStageCounter(this.localIccid, this.woId,
////									fieldTestingCardStage.getFieldTestingCardStageId(),
////									fieldTestingCardStage.getFieldTestingStageMaxCounter());
//
//							if(this.profileTestingSuccessful) {
//								testingController.profileValidation(true);
//							}else {
//								testingController.profileValidation(false);
//								stopAnyLocalRunningThread(this.localIccid);
//							}
//							break;
//
//						case 4:
//							//TODO System.out.println("Starting Stress testing");
//							this.loggerService.log("STARTING STRESS TESTING ", this.localIccid, this.woId,
//									LogType.INFO);
//							 this.logger.debug("Starting Stress Testing for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//							this.stressTestingSuccessful = this.startStressTesting();
//							//TODO System.out.println("Stress testing over");
//							this.loggerService.log("STRESS TESTING OVER. ", this.localIccid, this.woId, LogType.INFO);
//							 this.logger.debug("Stress Testing Over for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//
////							updateStageCounter(this.localIccid, this.woId,
////									fieldTestingCardStage.getFieldTestingCardStageId(),
////									fieldTestingCardStage.getFieldTestingStageMaxCounter());
//
//							if(this.stressTestingSuccessful) {
//								testingController.readWriteTest(true);
//							}else {
//								//TODO System.out.println("Setting status #ERROR for stress");
//								this.logger.debug("Setting status ERROR for stress for ICCID : "+this.localIccid+" WorkOrderID : "+this.woId);
//								testingController.readWriteTest(false);
//								this.stopAnyLocalRunningThread(this.localIccid);
//							}
//							break;
//
//						case 5:
//							//TODO System.out.println("OTA testing starting");
//							this.loggerService.log("STARTING OTA TESTING  ", this.localIccid, this.woId, LogType.INFO);
//							 this.logger.debug("Starting OTA Testing for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
////							 testResultCompilation();
//							this.startOTATesting();
//							//TODO System.out.println("OTA testing over");
//							this.loggerService.log("OTA TESTING OVER. ", this.localIccid, this.woId, LogType.INFO);
//							 this.logger.debug("OTA Testing Over for ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//
////							updateStageCounter(this.localIccid, this.woId,
////									fieldTestingCardStage.getFieldTestingCardStageId(),
////									fieldTestingCardStage.getFieldTestingStageMaxCounter());
//
//							//TODO System.out.println("Setting status #COMPLETED for stress");
////							updateStageStatus(this.localIccid, this.woId,
////									fieldTestingCardStage.getFieldTestingCardStageId(), "COMPLETED");
//							testingController.resultCompilation(true);
//							break;
//
//						default:
//							interrupt();
//						}
//					}
//
//				interrupt();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//
//
//	private boolean startStressTesting() throws NumberFormatException, IOException {
//		String counterFileName = this.woId + "_" + this.localIccid + "_cntr" + ".txt";
//		File fileStressTestingCounter = new File(this.counterFilePath + counterFileName);
//		boolean fileAlreadyExist = checkFileStatus(fileStressTestingCounter);
//		if (!fileAlreadyExist) {
//			createFileAndStartCounter(fileStressTestingCounter);
//		}
//		this.stressTest.runStressTesting();
////		this.stressTest.setStageId(stageId);
//
//		if(!this.stressTest.startStressTest()) {
//			return false;
//		}
//
//		return true ;
//	}
//
//	private void createFileAndStartCounter(File file) throws IOException {
//		FileWriter fileWrite = new FileWriter(file, false);
//		fileWrite.write("1");
//		fileWrite.close();
//	}
//
//	private boolean checkFileStatus(File file) {
//
//		if (file.exists()) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	private void updateStageStatus(String cardIccid, String woId2, int fieldTestingCardStageId, String status) {
//		TrakmeServerCommunicationServiceImpl communicationServiceImpl = new TrakmeServerCommunicationServiceImpl();
//		communicationServiceImpl.updateCardStageStatus(woId2, cardIccid, fieldTestingCardStageId, status,
//				this.terminal.getUserName());
//	}
//
//	private void updateStageCounter(String cardIccid, String woId2, int fieldTestingCardStageId, long counter) {
//		TrakmeServerCommunicationServiceImpl communicationServiceImpl = new TrakmeServerCommunicationServiceImpl();
//		communicationServiceImpl.updateCardStageCounter(woId2, cardIccid, fieldTestingCardStageId, counter,
//				this.terminal.getUserName());
//	}
//
//	private void updateCardStatus(TerminalInfo terminal2, String status) {
//		// TODO Auto-generated method stub
//	}
//
//	private void startOTATesting() throws NumberFormatException, IOException {
//		// TODO
//	}
//
//	private boolean startProfilevalidationTesting() throws IOException {
//		ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", this.terminal, this.card,
//				this.loggerService);
//		boolean runPrifile = profileTest3G.runProfileTesting();
//		if (!runPrifile) {
//			return false;
//		}
//		return true;
//	}
//
//	public boolean isStressTestingStatus() {
//		synchronized (this) {
//		return stressTestingStatus;
//		}
//	}
//
//
//
//	public void setStressTestingStatus(boolean stressTestingStatus) {
//		 synchronized (this) {
//			 this.stressTestingStatus = stressTestingStatus;
//		   }
//	}
//}