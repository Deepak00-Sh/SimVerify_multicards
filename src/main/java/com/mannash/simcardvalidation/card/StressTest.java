package com.mannash.simcardvalidation.card;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.SimVerifyLoggerThread;
import com.mannash.simcardvalidation.SimVerifyMasterThread2;
import com.mannash.simcardvalidation.TestingController4;
import com.mannash.simcardvalidation.pojo.LogType;
import com.mannash.simcardvalidation.pojo.ResponseStressTestingConfig;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.LoggerService;
import com.mannash.simcardvalidation.service.TrakmeServerCommunicationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.smartcardio.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
//import com.mannash.trakme.client.pojo.LogType;
//import com.mannash.trakme.client.pojo.ResponseStressTestingConfig;
//import com.mannash.trakme.client.pojo.TerminalInfo;
//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.TrakmeServerCommunicationServiceImpl;

@SuppressWarnings("restriction")
public class StressTest {
    private final Logger logger = LoggerFactory.getLogger(StressTest.class);
    LoggerService loggerService;
    SimVerifyLoggerThread loggerThread;


    boolean run = false;


    public String _terminal = "T";
    public String _card = "C";
    public String _device = "D";
    public String _ui = "UI";
    int widgetId;

    public StressTest(TerminalInfo terminal, Card c, LoggerService loggerService, SimVerifyLoggerThread loggerThread, int id) {
        this.loggerThread = loggerThread;
        this.terminalInfo = terminal;
        this.card = c;
        this.loggerService = loggerService;
        this.run = true;
        this.init();
        this.widgetId = id;
        System.out.println("Widget id of stress : " + this.widgetId);
        try (InputStream input = StressTest.class.getClassLoader().getResourceAsStream("trakmeClient.properties")) {
            this.properties = new Properties();

            if (input == null) {
                // //TODO // System.out.println("Sorry, unable to find config.properties");
                return;
            }
            this.properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//	public void stopStressTesting() {
//		this.logger.info("Stopping Stress Testing for ICCID " + this.ICCID + "WorkOrder " + this.woId);
//		this.run = false;
//	}

    public void runStressTesting() {
        this.run = true;
    }

//	private void updateStageCounter(String cardIccid, String woId2, int fieldTestingCardStageId, long counter) {
//		TrakmeServerCommunicationServiceImpl communicationServiceImpl = new TrakmeServerCommunicationServiceImpl();
//		communicationServiceImpl.updateCardStageCounter(woId2, cardIccid, fieldTestingCardStageId, counter,
//				this.terminalInfo.getUserName());
//
//	}

    public boolean startStressTest() {
        this.loggerThread.displayLogs(_terminal, _card, "Read/Write Test started", this.widgetId);
        try {
            this.aID = getAID();
            if (this.aID == null) {
                return false;
            }
            this.ICCID = getICCID();
            if (this.ICCID == null) {
                return false;
            }
            byte b = 0;
            if (getTerminal().isCardPresent()) {
                try {
                    Iterator<String> apdus = this.apduList.iterator();
                    String[] arrayOfString = new String[this.apduList.size()];

                    this.reverseStress = this.apduList;
                    while (apdus.hasNext()) {
                        arrayOfString[b++] = apdus.next();
                    }
                    int indexOfSms = 0;
                    try {
                        for (int i = 0; i < arrayOfString.length; i++) {
                            if (arrayOfString[i].equals("00A40004026F3C")) {
                                indexOfSms = i + 1;
                            }
                        }
                    } catch (Exception e) {
                        this.loggerService.log(e.toString(), this.ICCID, this.woId, LogType.ERROR);
                        e.printStackTrace();
                    }

                    // System.out.println("index of sms : " + indexOfSms);
//					String date;
//					String time;
//					String stressTimeStamp;
//					String updatedSmsContent;
                    for (double j = 1; j <= this.loopCount; j++) {
                        // System.out.println("Loop counts : " + j);
                        this.loggerThread.displayLogs(_terminal, _card, "Read/Write cycle : " + (int) j, this.widgetId);

//						// System.out.println("Stress loop counts : "+j);
//						//TODO // System.out.println("Stress loop count : " + (int) Math. round(j));
                        if (!this.run)
                            break;



                        if (j == this.loopCount) {
                            log("TERMINAL " + this.terminalNumber + " ## " + this.ICCID
                                            + " ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
                                    "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                    this.log_path + this.terminalNumber + "/");
                            log("TERMINAL " + this.terminalNumber + " ## " + this.ICCID
                                            + " ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
                                    "STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
                                    this.log_path + this.terminalNumber + "/");

                            // this.logger.debug("TERMINAL " + this.terminalNumber + " ## " + this.ICCID + "
                            // ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
                            // "STRESS_TEST_" + this.woId + "_" + this.ICCID);
                            // this.logger.debug("TERMINAL " + this.terminalNumber + " ## " + this.ICCID + "
                            // ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
                            // "STRESS_TEST_REPORT_T" + this.terminalNumber + "_" + this.woId + "_" +
                            // this.ICCID);
                        } else {
                            log("TERMINAL " + this.terminalNumber + " ## " + this.ICCID
                                            + " ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
                                    "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                    this.log_path + this.terminalNumber + "/");
                            // this.logger.debug("TERMINAL " + this.terminalNumber + " ## " + this.ICCID + "
                            // ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
                            // "STRESS_TEST_" + this.woId + "_" + this.ICCID);
                        }

                        // creating stress reverse APDU list
                        if (j == 1) {

                            for (int i = 0; i < this.reverseStress.size(); i++) {
                                try {
                                    // System.out.println(this.reverseStress.get(i));
                                    if (!this.reverseStress.get(i).substring(2, 4).equals("D6")) {
                                        if (this.reverseStress.get(i).equals("SETAID")) {
                                            if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
                                                return false;
                                            }
                                            continue;
                                        } else if (this.reverseStress.get(i).equals("RESET")) {
                                            try {
                                                if (!resetChannel()) {
                                                    return false;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            continue;
                                        }

                                        sendRawApduNoPrint(this.reverseStress.get(i));

                                        if (this.reverseStress.get(i).substring(2, 4).equals("B0")) {
                                            String st = this.reverseStress.get(i).substring(0, 2);
                                            String len = this.reverseStress.get(i).substring(8, 10);
                                            this.reverseStress.set(i + 1, st + "D60000" + len + getResponse());
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
//								createReverseStressConfig();
                            } catch (Exception e) {
                                this.logger.debug(e.toString(), this.ICCID, this.woId);
                                this.loggerService.log(e.toString(), this.ICCID, this.woId, LogType.ERROR);
                            }
                            // System.out.println("List : " + this.reverseStress);
                        }
//						// System.out.println("Array of string : "+arrayOfString.length);
                        for (int b1 = 0; b1 < arrayOfString.length; b1++) {
//							// System.out.println("B = "+b1 +" J = "+j);
                            if (arrayOfString[b1].equals("SETAID")) {
                                if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
                                    return false;
                                }
                                continue;
                            } else if (arrayOfString[b1].equals("RESET")) {
                                // System.out.println("calling reset");
                                this.loggerThread.displayLogs(_terminal, _card, "Calling reset", this.widgetId);
                                try {
                                    if (!resetChannel()) {
                                        return false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                continue;
                            }

                            if (!sendRawApduNoPrint(arrayOfString[b1])) {
                                // System.out.println("false 1");
                                return false;
                            }

//							if (!getSW1Text().equalsIgnoreCase("90") && !getSW1Text().equalsIgnoreCase("9F") && !getSW1Text().equalsIgnoreCase("94")){
//								return false;
//							}

                            String str1 = getResponse();

                            if (j < 2 && !str1.equals("")) {
                                log("COMMAND  : " + arrayOfString[b1] + " CONTENT :" + str1,
                                        "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
                                        "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");

                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " CONTENT :" + str1,
                                // "STRESS_TEST_" + this.woId + "_" + this.ICCID);
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
                                // getSW1Text() + getSW2Text(), "STRESS_TEST_" + this.woId + "_" + this.ICCID);

                            } else if (j == this.loopCount && !str1.equals("")) {
                                log("COMMAND  : " + arrayOfString[b1] + " CONTENT :" + str1,
                                        "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
                                        "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                log("COMMAND  : " + arrayOfString[b1] + " CONTENT :" + str1,
                                        "STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
                                        "STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");

                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " CONTENT :" + str1,
                                // "STRESS_TEST_" + this.woId + "_" + this.ICCID);
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
                                // getSW1Text() + getSW2Text(),"STRESS_TEST_" + this.woId + "_" + this.ICCID);
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " CONTENT :" +
                                // str1,"STRESS_TEST_REPORT_T" + this.terminalNumber + "_" + this.woId + "_"+
                                // this.ICCID);
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
                                // getSW1Text() + getSW2Text(),"STRESS_TEST_REPORT_T" + this.terminalNumber +
                                // "_" + this.woId + "_" + this.ICCID);

                            } else if (j == this.loopCount && str1.equals("")) {
                                log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
                                        "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
                                        "STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
                                // getSW1Text() + getSW2Text(),"STRESS_TEST_" + this.woId + "_" + this.ICCID);
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
                                // getSW1Text() + getSW2Text(),"STRESS_TEST_REPORT_T" + this.terminalNumber +
                                // "_" + this.woId + "_"+ this.ICCID);

                            } else {
                                log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
                                        "STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
                                        this.log_path + this.terminalNumber + "/");
                                // this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
                                // getSW1Text() + getSW2Text(),"STRESS_TEST_" + this.woId + "_" + this.ICCID);
                            }
                        }
                    }

                    try {
//						startReverseStressTest();
                    } catch (Exception e) {
                        this.logger.debug(e.toString(), this.ICCID, this.woId);
                        this.loggerService.log(e.toString(), this.ICCID, this.woId, LogType.ERROR);
                    }

                    try {
//						settleLogsIntoStorage();
                    } catch (Exception e) {
                        this.logger.debug("Error while sending Stress logs into storage " + e.toString(), this.ICCID,
                                this.woId);
                        this.loggerService.log("Error while sending logs into storage " + e.toString(), this.ICCID,
                                this.woId, LogType.ERROR);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else {
                this._RspDataString = "";
                this._Error = "Card Not Present";
            }
        } catch (CardException cardException) {
            cardException.printStackTrace();
        }
        return true;
    }

//	private void settleLogsIntoStorage() {
//		// TODO Auto-generated method stub
//
//		try {
//
//			File dir1 = new File(this.localStressLogPath);
//			if (dir1.isDirectory()) {
//				String[] content = dir1.list();
//				for (int i = 0; i < content.length; i++) {
//					if (content[i].contains(this.woId + "_" + this.ICCID)) {
//						Files.move(Paths.get(this.localStressLogPath + content[i]),
//								Paths.get(this.storageStressLogPath + content[i]));
//					}
//				}
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
////			e.printStackTrace();
//		}
//
//	}

//	private void createReverseStressConfig() {
//		// TODO Auto-generated method stub
//		File stressReverseConfig = new File("..\\config\\StressReverseConfig_"+ this.woId + "_" + this.ICCID + ".conf");
////		File stressReverseConfig = new File(
////				"C:\\logs\\config\\StressReverseConfig_" + this.woId + "_" + this.ICCID + ".conf");
//		FileWriter writer = null;
//
//		if (!stressReverseConfig.exists()) {
//			try {
//				stressReverseConfig.createNewFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		try {
//			writer = new FileWriter(stressReverseConfig, true);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		for (String str : this.reverseStress) {
//			try {
//				writer.write(str + System.lineSeparator());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		try {
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	// TODO Stress Reverse Code
//	public boolean startReverseStressTest() {
//		try {
//			this.aID = getAID();
//			if (this.aID == null) {
//				return false;
//			}
//			this.ICCID = getICCID();
//			if (this.ICCID == null) {
//				return false;
//			}
//			byte b = 0;
//			if (getTerminal().isCardPresent()) {
//				try {
//
//					// TODO
//					File stressReverseConfig = new File("..\\config\\StressReverseConfig_"+ this.woId + "_" + this.ICCID + ".conf");
////					File stressReverseConfig = new File(
////							"C:\\logs\\config\\StressReverseConfig_"+ this.woId + "_" + this.ICCID + ".conf");
//					Scanner s = null;
//					if (stressReverseConfig.exists()) {
//						s = new Scanner(new File("..\\config\\StressReverseConfig_"+ this.woId + "_" + this.ICCID + ".conf"));
////						s = new Scanner(new File("C:\\logs\\config\\StressReverseConfig_"+ this.woId + "_" + this.ICCID + ".conf"));
//					} else {
//						s = new Scanner(new File("..\\config\\DefaultStressReverseConfig.conf"));
////						s = new Scanner(new File("C:\\logs\\config\\DefaultStressReverseConfig.conf"));
//					}
//
//					ArrayList<String> reverseList = new ArrayList<String>();
//					while (s.hasNext()) {
//						reverseList.add(s.next());
//					}
//					s.close();
//
//					Iterator<String> apdus = reverseList.iterator();
//					String[] arrayOfString = new String[reverseList.size()];
//					while (apdus.hasNext()) {
//						arrayOfString[b++] = apdus.next();
//					}
//					for (double j = 1; j <= 5; j++) {
//						// System.out.println("Reverse Stress loop counts : " + j);
//						if (!this.run)
//							break;
//						log("TERMINAL " + this.terminalNumber + " ## " + this.ICCID
//								+ " ## REVERSE STRESS TEST ITERATION NUMBER ----------------------------####"
//								+ (long) j, "REVERSE_STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//								this.log_path + this.terminalNumber + "/");
//						// this.logger.debug("TERMINAL " + this.terminalNumber + " ## " + this.ICCID + "
//						// ## STRESS TEST ITERATION NUMBER ----------------------------####" + (long) j,
//						// "STRESS_TEST_" + this.woId + "_" + this.ICCID);
//
//						for (int b1 = 0; b1 < arrayOfString.length; b1++) {
//							if (arrayOfString[b1].equals("SETAID")) {
//								if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
//									return false;
//								}
//								continue;
//							} else if (arrayOfString[b1].equals("RESET")) {
//								// System.out.println("calling reset");
//								this.loggerThread.displayLogs(_terminal, _card,"Calling reset", this.widgetId);
//								try {
//									if (!resetChannel()) {
//										return false;
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								this.loggerThread.displayLogs(_card,_terminal,"Reset success", this.widgetId);
//								continue;
//							} else if (arrayOfString[b1].equals("00A40004026F3C")) {
//								String date = formatN("" + calendar.get(5), 2) + formatN("" + (calendar.get(2) + 1), 2)
//										+ formatN("" + calendar.get(1), 4);
//								String time = formatN("" + calendar.get(11), 2) + formatN("" + calendar.get(12), 2);
//								if (!sendRawApduNoPrint("00DC0404B0AB" + date + "AB" + time
//										+ "AB0000000"+j+"FFFFFFFFFFFFFFFFFFFFFFFFABABABABABABABABABABFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFABABABABABABABABABABFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFABABABABABABABABABAB")) {
//									return false;
//								}
//								continue;
//							}
//
//							if (!sendRawApduNoPrint(arrayOfString[b1])) {
//								return false;
//							}
//							String str1 = getResponse();
//
//							if (j < 2 && !str1.equals("")) {
//								log("COMMAND  : " + arrayOfString[b1] + " CONTENT :" + str1,
//										"REVERSE_STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
//										"REVERSE_STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " CONTENT :" + str1,
//								// "STRESS_TEST_" + this.woId + "_" + this.ICCID);
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
//								// getSW1Text() + getSW2Text(), "STRESS_TEST_" + this.woId + "_" + this.ICCID);
//
//							} else if (j == this.loopCount && !str1.equals("")) {
//								log("COMMAND  : " + arrayOfString[b1] + " CONTENT :" + str1,
//										"REVERSE_STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
//										"STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								log("COMMAND  : " + arrayOfString[b1] + " CONTENT :" + str1,
//										"REVERSE_STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
//										"STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " CONTENT :" + str1,
//								// "STRESS_TEST_" + this.woId + "_" + this.ICCID);
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
//								// getSW1Text() + getSW2Text(),"STRESS_TEST_" + this.woId + "_" + this.ICCID);
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " CONTENT :" +
//								// str1,"STRESS_TEST_REPORT_T" + this.terminalNumber + "_" + this.woId + "_"+
//								// this.ICCID);
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
//								// getSW1Text() + getSW2Text(),"STRESS_TEST_REPORT_T" + this.terminalNumber +
//								// "_" + this.woId + "_" + this.ICCID);
//
//							} else if (j == this.loopCount && str1.equals("")) {
//								log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
//										"REVERSE_STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
//										"REVERSE_STRESS_TEST_REPORT_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
//								// getSW1Text() + getSW2Text(),"STRESS_TEST_" + this.woId + "_" + this.ICCID);
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
//								// getSW1Text() + getSW2Text(),"STRESS_TEST_REPORT_T" + this.terminalNumber +
//								// "_" + this.woId + "_"+ this.ICCID);
//
//							} else {
//								log("COMMAND  : " + arrayOfString[b1] + " RESPONSE :" + getSW1Text() + getSW2Text(),
//										"REVERSE_STRESS_TEST_" + this.woId + "_" + this.ICCID + "_",
//										this.log_path + this.terminalNumber + "/");
//								// this.logger.debug("COMMAND : " + arrayOfString[b1] + " RESPONSE :" +
//								// getSW1Text() + getSW2Text(),"STRESS_TEST_" + this.woId + "_" + this.ICCID);
//							}
//						}
//					}
//
//				} catch (Exception exception) {
//					exception.printStackTrace();
//				}
//			} else {
//				this._RspDataString = "";
//				this._Error = "Card Not Present";
//			}
//		} catch (CardException cardException) {
//			cardException.printStackTrace();
//		}
//		return true;
//	}

    // TODO Finished here

//	private void updateLoopCounterLocal(String ICCID2, String woId2, long updatedCounter) throws IOException {
//		try {
//			FileWriter fileWrite = new FileWriter(this.counterFileName, false);
//			fileWrite.write("" + updatedCounter);
//			fileWrite.close();
//			// this.logger.info("File updated with value:" + updatedCounter);
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}

    public String getSW1Text() {
        StringBuffer stringBuffer = new StringBuffer();
        String str = Integer.toHexString(getSW1() & 0xFF);
        if (str.length() == 1)
            stringBuffer.append(0);
        stringBuffer.append(str);
        return stringBuffer.toString().toUpperCase();
    }

    public String getSW2Text() {
        StringBuffer stringBuffer = new StringBuffer();
        String str = Integer.toHexString(getSW2() & 0xFF);
        if (str.length() == 1)
            stringBuffer.append(0);
        stringBuffer.append(str);
        return stringBuffer.toString().toUpperCase();
    }

    public boolean sendRawApduNoPrint(String paramString) {
        try {
            if (!sendCmd(paramString)) {
                return false;
            }
        } catch (Exception exception) {
            this.logger.error("Send APDU Error : " + paramString);
            return false;
        }
        return true;
    }

    public boolean sendCmd(String paramString) {
        paramString = paramString.toUpperCase();
        paramString = paramString.replaceAll(" ", "");
        // this.logger.debug("Param String in send cmd :-" + paramString);
        this.loggerThread.displayLogs(_terminal, _card, paramString, this.widgetId);

        try {
            if (getTerminal().isCardPresent())
                if (paramString.length() % 2 != 0) {
                    this._CommandApduBytes = null;
                    this._RspDataString = "";
                    this._Error = "";
                    this._SW1 = 0;
                    this._SW2 = 0;
                    this._SW = "";
                    this._RspDataBytes = null;
                    this._Error = "Command: " + paramString + "\nERROR: 1 nibble is missing in APDU. Please Check.\n";
//					this.loggerThread.displayLogs(_card,_terminal,paramString+"ERROR: nibble is missing in APDU");
                } else {
                    byte[] arrayOfByte = new byte[paramString.length() / 2];
                    boolean bool = true;
                    for (int i = 0; i < paramString.length(); i += 2) {
                        String str = paramString.substring(i, i + 2);
                        try {
                            int j = Integer.parseInt(str, 16);
                            arrayOfByte[i / 2] = (byte) j;
                        } catch (Exception exception) {
                            this._CommandApduBytes = null;
                            this._RspDataString = "";
                            this._Error = "";
                            this._SW1 = 0;
                            this._SW2 = 0;
                            this._SW = "";
                            this._RspDataBytes = null;
                            this._Error = "Command: " + paramString
                                    + "\nERROR: Invalid Hex Value in the APDU. Please re-check.\n";
//							this.loggerThread.displayLogs(_card,_terminal,paramString+"ERROR: Invalid Hex Value in the APDU");
                            bool = false;
                            break;
                        }
                    }
                    if (bool) {
                        CommandAPDU commandAPDU = null;
                        try {
                            commandAPDU = new CommandAPDU(arrayOfByte);
                        } catch (Exception exception) {
                            this._CommandApduBytes = null;
                            this._RspDataString = "";
                            this._Error = "";
                            this._SW1 = 0;
                            this._SW2 = 0;
                            this._SW = "";
                            this._RspDataBytes = null;
                            this._Error = "Command: " + paramString + "\nERROR: " + exception.getMessage() + "\n";
//							this.loggerThread.displayLogs(_card,_terminal,paramString+"ERROR: " + exception.getMessage());
                        }
                        try {

                            this._CommandApduBytes = arrayOfByte;
                            this._Error = "";
                            ResponseAPDU responseAPDU = null;

                            try {
                                responseAPDU = this.cardChannel.transmit(commandAPDU);
                            } catch (Exception e) {
//								// System.out.println("e5");
//								 e.printStackTrace();

//								tryToReconnect(commandAPDU);

                                boolean reset = true;
                                forLoop:
                                for (int i = 0; i < 5; i++) {
                                    reset = resetChannel();
                                    if (reset) {
                                        responseAPDU = this.cardChannel.transmit(commandAPDU);
                                        break forLoop;
                                    }
                                }

                                if (!reset) {
                                    // System.out.println("Disconnecting card...");
                                    this.card.disconnect(false);
                                    this.loggerThread.displayLogs(_terminal, "Card disconnected, please connect again.", this.widgetId);
                                    return false;
                                }
                            }

                            // this.logger.debug("SW : " + responseAPDU.getSW() + " SW1 : " +
                            // responseAPDU.getSW1() + " SW2 : " + responseAPDU.getSW2());
                            this._RspDataBytes = responseAPDU.getData();
                            // this.logger.debug("this._RspDataBytes.length :-" +
                            // this._RspDataBytes.length);
                            // this.logger.debug("this is this._RspDataBytes:-" + this._RspDataBytes);
//							byte[] Bytes = this._RspDataBytes;
                            this._RspDataString = byteArrayToHex(this._RspDataBytes);

                            // this.logger.debug("this._RspDataString :-" + this._RspDataString);

                            this._SW = Integer.toHexString(responseAPDU.getSW());
                            this._SW1 = (byte) responseAPDU.getSW1();
                            this._SW2 = (byte) responseAPDU.getSW2();
                            this.loggerThread.displayLogs(_card, _terminal, this._SW, this.widgetId);
                        } catch (Exception exception) {

                            exception.printStackTrace();
                            this._CommandApduBytes = null;
                            this._RspDataString = "";
                            this._Error = "";
                            this._SW1 = 0;
                            this._SW2 = 0;
                            this._SW = "";
                            this._RspDataBytes = null;
                            this._Error = "Command: " + paramString + "\nERROR: Invalid APDU. Re-check it..\n";
//							this.loggerThread.displayLogs(_card,_terminal,"ERROR: Invalid APDU");
                        }
                    }
                }
        } catch (CardException cardException) {
            cardException.printStackTrace();
            return false;
        }
        return true;
    }

//	public boolean isStressTestingSuccessful() {
//		return this.stressTestingStatus;
//
//	}

    public String byteArrayToHex(byte[] a) {

        StringBuilder sb = new StringBuilder(a.length * 2);

        for (byte b : a) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();

    }

    public String getResponse() {
        return this._RspDataString.toUpperCase();
    }

    public String getICCID() {
        if (!sendRawApduNoPrint("00A4000402 3F00")) {
            return null;
        }
        if (!sendRawApduNoPrint("00A4000402 2FE2")) {
            return null;
        }
        if (!sendRawApduNoPrint("00B000000A")) {
            return null;
        }

        return nibbleSwap(getResponse());
    }

    public String fetchRecordSize(String paramString) {
        StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ");
        int i = stringTokenizer.countTokens();
        @SuppressWarnings("unused")
        byte b1 = 0, b2 = 0;
        int j = 0;
        String str = "";
        String[] arrayOfString = new String[i];
        if (stringTokenizer.hasMoreTokens())
            for (byte b = 0; b < i; b++) {
                arrayOfString[b] = stringTokenizer.nextToken();
                if (arrayOfString[b].equals("82") && b <= i - 4)
                    b1 = b;
                if (arrayOfString[b].equals("80") && b <= i - 4)
                    b2 = b;
            }
        j = Integer.parseInt(arrayOfString[b1 + 4] + arrayOfString[b1 + 5], 16);
        str = Integer.toHexString(j);
        if (str.length() == 1)
            str = "0" + str;
        return str;
    }

//	public String getAID() {
//		String str1 = "", str2 = "";
////		// this.logger.info("sending RawAPDU :-");
//		sendRawApduNoPrint("00A4000002 3F00");
//
//		sendRawApduNoPrint("00A4000002 2F00");
//		str1 = insertSpace(getResponse());
////		// this.logger.info("Str1 after 2F00 :-"+str1);
//		str2 = fetchRecordSize(str1);
//		sendRawApduNoPrint("00B20104" + str2);
//		str1 = getResponse();
//
////		// this.logger.info("Str1 response : " + str1);
//
//		if (!str1.equals(null))
//			str1 = getResponse().substring(8, 40);
//		return str1.toUpperCase();
//	}

    public String getAID() {
        boolean resetOfAID = resetChannel();
        if (!resetOfAID) {
            return null;
        }
        String str1 = "", str2 = "";
        // this.logger.debug("sending RawAPDU :-");
        // sendRawApduNoPrint("00A4000002 3F00");
        if (!sendRawApduNoPrint("00A4080402 2F00")) {
            return null;
        }
        str1 = insertSpace(getResponse());
        // this.logger.debug("Str1 after 2F00 :-" + str1);
        str2 = fetchRecordSize(str1);
        if (!sendRawApduNoPrint("00B20104" + str2)) {
            return null;
        }
        str1 = getResponse();
        // this.logger.debug("Str1 response : " + str1);
        if (!str1.equals(null))
            str1 = getResponse().substring(8, 40);
        return str1.toUpperCase();
    }

//	public String getSW() {
//		return this._SW.toUpperCase();
//	}
//
//	public byte[] getResponseBytes() {
//		return this._RspDataBytes;
//	}

    public String getError() {
        return this._Error;
    }
//
//	public byte[] getCommandBytes() {
//		return this._CommandApduBytes;
//	}
//
//	public String getCommand() {
//		return byteArrayToString(this._CommandApduBytes);
//	}

    public byte getSW1() {
        return this._SW1;
    }

    public byte getSW2() {
        return this._SW2;
    }

//	public String byteArrayToString(byte[] paramArrayOfbyte) {
//		StringBuffer stringBuffer = new StringBuffer();
//		for (byte b = 0; b < paramArrayOfbyte.length; b++) {
//			String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
//			if (str.length() == 1)
//				stringBuffer.append(0);
//			stringBuffer.append(str);
//		}
//		return stringBuffer.toString().toUpperCase();
//	}

    public String insertSpace(String paramString) {
        String str = "";
        for (int i = 0; i < paramString.length(); i += 2) {
            if (!str.equals("")) {
                str = str + " " + paramString.substring(i, i + 2);
            } else {
                str = paramString.substring(i, i + 2);
            }
        }
        return str.toUpperCase();
    }

    public String nibbleSwap(String paramString) {
        String str = "";
        for (int i = 0; i < paramString.length(); i += 2) {
            String str1 = paramString.substring(i, i + 2).substring(0, 1);
            String str2 = paramString.substring(i, i + 2).substring(1);
            String str3 = str1;
            str1 = str2;
            str2 = str3;
            str = str + str1 + str2;
        }
        return str;
    }

    public void log(String logText, String logFileName, String noInUse) {

//		try {
//			this.loggerService.log(logText, this.ICCID, this.woId, LogType.DEBUG);
//
////			Calendar calendar = Calendar.getInstance();
//			@SuppressWarnings("unused")
//			String str2 = formatN("" + calendar.get(1), 4) + formatN("" + (calendar.get(2) + 1), 2)
//					+ formatN("" + calendar.get(5), 2);
//			String str3 = formatN("" + calendar.get(11), 2) + formatN("" + calendar.get(12), 2)
//					+ formatN("" + calendar.get(13), 2);
//			String str4 = getdate(1);
//			String str5 = str4.substring(0, 6);
////			// System.out.println("str5 : "+str5);
//
////			File file = new File(this.properties.getProperty("stressLogPath"));
//			File file2 = new File(this.localStressLogPath + logFileName + str2 + ".txt");
//			if (!file2.exists()) {
//				file2.createNewFile();
//			}
//
////      if (!file.exists() || !file.isDirectory())
////        file.mkdir();
//			FileOutputStream fileOutputStream = new FileOutputStream(file2, true);
//			PrintStream printStream = new PrintStream(fileOutputStream);
//			if (logText.contains("STRESS TEST ITERATION NUMBER")) {
//				printStream.println("#" + str2 + "#" + str3 + " #" + logText);
//			} else {
//				printStream.println(logText);
//			}
//
//			printStream.close();
//			fileOutputStream.close();
//		} catch (Exception exception) {
//			// this.logger.error("GOT Exception in LOG method as:" + exception);
//		}
    }

//	private String formatN(String paramString, int paramInt) {
//		String str = "";
//		int i = paramString.length();
//		if (i >= paramInt) {
//			str = paramString;
//		} else {
//			for (byte b = 0; b < paramInt - i; b++)
//				str = str + "0";
//			str = str + paramString;
//		}
//		return str;
//	}

//	public String getdate(int paramInt) {
//		String str = "yyyyMMdd";
//		if (paramInt == 2) {
//			str = "yyMMdd";
//		} else if (paramInt == 3) {
//			str = "HH";
//		} else if (paramInt == 4) {
//			str = "dd-MM HH";
//		} else if (paramInt == 5) {
//			str = "yyyy-MM-dd";
//		}
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
//		Date date = new Date();
//		return simpleDateFormat.format(date);
//	}

    public CardTerminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(CardTerminal terminal) {
        this.terminal = terminal;
    }

    private void init() {
        try {
//			CardTerminal ct = TerminalFactory.getDefault().terminals().list().get(this.terminalId);

            this.setTerminal(this.terminalInfo.getCt());
            this.woId = this.terminalInfo.getWoId();
            // Card c = this.getTerminal().connect("T=0");
            this.cardChannel = this.card.getBasicChannel();

            setStressTestingConfig(this.terminalInfo.getTerminalCardIccid(), this.terminalInfo.getWoId());
            this.terminalNumber = this.terminalInfo.getTerminalNumber();

            this.localStressLogPath = "D:\\Work\\R&D\\SIMVerify\\logs\\StressLogs\\SimVerify_Stress\\";
//			this.storageStressLogPath = "Z:\\Offline Test Logs\\Stress Test\\" + this.woId + "\\";

//			createLogFolders();

        } catch (Exception e) {
            // System.out.println("inside the init function");
            e.printStackTrace();
        }
    }

//	private void createLogFolders() {
//		// TODO Auto-generated method stub
//		File logs = new File("C:\\logs\\");
//		if (!logs.exists()) {
//			logs.mkdir();
//		}
//
//		File stress = new File("C:\\logs\\Stress_Logs");
//		if (!stress.exists()) {
//			stress.mkdir();
//		}
//
//		File workOrder = new File("C:\\logs\\Stress_Logs\\" + this.woId);
//		if (!workOrder.exists()) {
//			workOrder.mkdir();
//		}
//
//		File storageStress = new File("Z:\\Offline Test Logs\\Stress Test");
//		if (!storageStress.exists()) {
//			storageStress.mkdir();
//		}
//
//		File storageStressWorkOrder = new File("Z:\\Offline Test Logs\\Stress Test\\" + this.woId);
//		if (!storageStressWorkOrder.exists()) {
//			storageStressWorkOrder.mkdir();
//		}
//
//	}

    private boolean checkFileStatus(File file) {

        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
//
//	private void createFileAndStartCounter(File file) throws IOException {
//		FileWriter fileWrite = new FileWriter(file, false);
//		fileWrite.write("1");
//		fileWrite.close();
//	}

//	private int getCounter(File file) throws NumberFormatException, IOException {
//		BufferedReader in = new BufferedReader(new FileReader(file));
//		String x = "";
//		int counter = 0;
//
//		while ((x = in.readLine()) != null) {
//			counter = Integer.parseInt(x.trim());
//			// this.logger.info("counter:" + counter);
//		}
//		in.close();
//		return counter;
//	}

    public boolean resetChannel() {
//		// System.out.println("Calling reset channel ...");
//		try {
//			this.cardChannel.close();
//		} catch (Exception e1) {
//			 e1.printStackTrace();
//		}
        try {
            this.card.disconnect(true);
            if (this.terminal.isCardPresent()) {
                this.card = this.terminal.connect("T=0");
            } else {
                return false;
            }
            this.cardChannel = this.card.getBasicChannel();
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
        return true;
    }

    private void setStressTestingConfig(String terminalCardICCID, String woId2) {
//		TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
//		ResponseStressTestingConfig serverResponse = trakmeServerCommunicationService
//				.getStressTestingConfig(terminalCardICCID, woId2, this.terminalInfo.getUserName());

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("..\\config\\testingConfig.conf"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            // handle the exception
        }
        String jsonString = stringBuilder.toString();

        Gson gson = new Gson();
        ResponseStressTestingConfig serverResponse = gson.fromJson(jsonString, ResponseStressTestingConfig.class);

        this.apduList = serverResponse.getApduList();
        this.loopCount = serverResponse.getLoopCount();

//		this.reverseApduList = serverResponse.getReverseApduList();

//		Collections.addAll(this.reverseApduList = new ArrayList<String>(), "RESET","A0A40000023F00","A0A40000027F20","A0A40000026F7E","A0B000000B","A0D600000B272F806804F401EE7BABAB","A0A40000026F20","A0B0000009","A0D60000096C6F636174696F6EAB","A0A40000026F7B","A0B000000C","A0D600000CABABFFFFFFFFFFFFFFFFABAB","A0A40000026F74","A0B0000010","A0D6000010ABABFFFFFFFFABABFFFFFFFFFFFFABAB","A0A40000023F00","A0A40000027F10","A0A40000026F3C","A0B20104B0","A0B20204B0","RESET","SETAID","00A40004027FFF","00A40004026F7E","00B000000B","00D600000BABABDB3804F4010209ABAB","00A40004026F7B","00B000000C","00D600000CABABFFFFFFFFFFFFFFFFABAB","00A40004026F3C","00DC0204B0","ABABABABABFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFABABABABABABABABABABFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFABABABABABABABABABABFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFABABABABABABABABABAB","00A40004025F3B","00A40004024F20","00B0000009","00D6000009ABABABFFFFFFABABAB");

        this.reverseLoopCount = serverResponse.getReverseLoopCount();

        this.counterFileName = this.woId + "_" + terminalCardICCID + "_cntr" + ".txt";
        File fileStressTestingCounter = new File(this.counterFileName);
        boolean fileAlreadyExist = checkFileStatus(fileStressTestingCounter);
        long startCounter = 1;
//		if (!fileAlreadyExist) {
//			try {
//				createFileAndStartCounter(fileStressTestingCounter);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} else if (fileAlreadyExist) {
//			try {
//				startCounter = getCounter(fileStressTestingCounter);
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
        serverResponse.setStartCounter(startCounter);
        // this.logger.info("Start Loop Count : " + serverResponse.getStartCounter());
        this.loopStrtCnt = serverResponse.getStartCounter();

    }

    List<String> apduList;
    List<String> reverseStress = new ArrayList<String>();
    //	List<String> reverseApduList;
//	int reverseStartLoopCount;
    long reverseLoopCount;
    String log_path = "";
    CardTerminal terminal = null;
    byte _SW1;
    byte _SW2;
    String _RspDataString;
    String _SW;
    String ICCID, aID;
    String woId;
    byte[] _RspDataBytes;
    String _Error;
    byte[] _CommandApduBytes;
    double loopCount = 0;
    int terminalNumber = 0;
    long loopStrtCnt = 0;
    private Card card;
    CardChannel cardChannel = null;
    String counterFileName = null;
//	boolean breakForLoop = false;
//	boolean stressTestingStatus = true;
//	int apduCounter = 0;
//	Calendar calendar = Calendar.getInstance();

    private TerminalInfo terminalInfo;
    private Properties properties;
    String localStressLogPath;
//	String storageStressLogPath;

}