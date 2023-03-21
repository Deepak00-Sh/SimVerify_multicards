package com.mannash.simcardvalidation.threads;//package com.mannash.simcardvalidation.threads;
//
//import com.mannash.simcardvalidation.TestingController;
//import com.mannash.simcardvalidation.card.ProfileTest3G;
//import com.mannash.simcardvalidation.card.StressTest;
//import com.mannash.simcardvalidation.pojo.LogType;
//import com.mannash.simcardvalidation.pojo.TerminalInfo;
//import com.mannash.simcardvalidation.service.LoggerService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.smartcardio.Card;
//import javax.smartcardio.CardException;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class StartTestCase {
//
//    TestingController testingController = new TestingController();
//    private final Logger logger = LoggerFactory.getLogger(StartTestCase.class);
//    // String counterFilePath = "%TEMP%\\TrakMe\\client\\trace\\";
//    String counterFilePath = "";
//    private String localIccid;
//    private String woId;
//    private TerminalInfo terminal;
//    StressTest stressTest;
//    //private TestThreadMap threadMap = new TestThreadMap();
//    private Card card = null;
//    private AtomicBoolean running = new AtomicBoolean(false);
//    public boolean stressTestingStatus = true ;
//    LoggerService loggerService;
//    boolean stressTestingSuccessful = true;
//    boolean profileTestingSuccessful = true;
//    boolean fieldTestingStatus = true;
//
//    public void setStartTestCase(TerminalInfo terminal1, LoggerService loggerService1) {
//        System.out.println("inside the constructor");
//        this.loggerService = loggerService;
//        this.localIccid = terminal.getTerminalCardIccid();
//        this.woId = terminal.getWoId();
//        this.terminal = terminal;
//        try {
//            this.card = this.terminal.getCt().connect("T=0");
//            this.loggerService.log("Card conncted successfully. ", this.localIccid, this.woId, LogType.INFO);
//            this.logger.debug("Card conncted successfully : " + this.localIccid + "WorkOrderID : " + this.woId);
//        } catch (CardException e1) {
//            this.loggerService.log("Error while connecting to card. " + e1.toString(), this.localIccid, this.woId,
//                    LogType.ERROR);
//            this.logger.error("Error while connecting to card : " + e1.toString() + "ICCID : " + this.localIccid + "WorkOrderID : " + this.woId);
//            e1.printStackTrace();
//        }
//        this.stressTest = new StressTest(this.terminal, this.card, this.loggerService);
//
//    }
//
//    public StartTestCase(){
//
//    }
//
//    public void atrVerification(){
//        testingController.atrVerification(this.localIccid,true);
//        System.out.println("inside atrverification");
//    }
//
//    public void fileSystemVerification(){
//        testingController.fileSystemVerification(true);
//    }
//
////    public void profileValidation(){
////
////        ProfileTest3G profileTest3G = new ProfileTest3G("0000000000000000", this.terminal, this.card,
////                this.loggerService);
////        boolean runProfile = profileTest3G.runProfileTesting();
////        if (runProfile){
////            testingController.profileValidation(true);
////        }else {
////            testingController.profileValidation(false);
////        }
////
////    }
//
////    public void readWriteTest(){
////        this.stressTest.runStressTesting();
////
////        boolean stressTestingSuccessful = stressTest.startStressTest();
////        if (stressTestingSuccessful) {
////            testingController.readWriteTest(true);
////        }
////    }
//
//    public void resultCompilation(){
//        testingController.resultCompilation(true);
//    }
//
//
//}
