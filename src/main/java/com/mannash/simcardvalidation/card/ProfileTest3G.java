package com.mannash.simcardvalidation.card;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.SimVerifyLoggerThread;
import com.mannash.simcardvalidation.SimVerifyMasterThread2;
import com.mannash.simcardvalidation.TestingController4;
import com.mannash.simcardvalidation.pojo.LogType;
import com.mannash.simcardvalidation.pojo.ResponseProfileTestingConfig;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import com.mannash.simcardvalidation.service.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.smartcardio.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
//import com.mannash.trakme.client.pojo.LogType;
//import com.mannash.trakme.client.pojo.ResponseProfileTestingConfig;
//import com.mannash.trakme.client.pojo.TerminalInfo;
//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.TrakmeServerCommunicationServiceImpl;

@SuppressWarnings("restriction")
public class ProfileTest3G {

	SimVerifyLoggerThread loggerThread;
	public String _terminal = "T";
	public String _card = "C";
	public String _device = "D";
	public String _ui = "UI";
	LoggerService loggerService;
	int widgetId;

	public ProfileTest3G() {
	}

	public ProfileTest3G(String aDM, TerminalInfo terminal, Card c, LoggerService loggerService, SimVerifyLoggerThread loggerThread,int id) {
		this.aDM = aDM;
		this.ICCID = terminal.getTerminalCardIccid();
		this.woId = terminal.getWoId();
		this.terminalInfo = terminal;
		this.card = c;
		this.loggerService = loggerService;
		this.loggerThread = loggerThread;
		widgetId = id;
		this.init();

		try (InputStream input = StressTest.class.getClassLoader().getResourceAsStream("trakmeClient.properties")) {
			this.properties = new Properties();

			if (input == null) {
//				// System.out.println("Sorry, unable to find config.properties"); 
				return;
			}
			this.properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean runProfileTesting() {
		loggerThread.displayLogs(_terminal,"Profile Verification started",widgetId);
		if (this.aDM.equals("0000000000000000")) {
			this.aID = getAID();
			if (this.aID == null) {
				loggerThread.displayLogs(_card,_terminal,"Card AID not found",widgetId);
				return false;
			}
			// System.out.println("\nAID OF THE CARD
			// ....................................................." + this.aID);
			// this.loggerService.log("AID OF THE CARD : " + this.aID, this.ICCID, this.woId, LogType.DEBUG);
			this.ICCID = getICCID();
			if (this.ICCID == null) {
				loggerThread.displayLogs(_card,_terminal,"Card ICCID not found",widgetId);
				return false;
			}
			if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
				// System.out.println("1");
				return false;
			}

			this.IMSI = getIMSI();
			if (this.IMSI == null) {
				loggerThread.displayLogs(_card,_terminal,"Card IMSI not found",widgetId);
				return false;
			}
			// System.out.println("\nREADING PROFILE FOR THE CARD WITHOUT VERIFYING
			// ADM........................................................");
			// this.loggerService.log("READING PROFILE FOR THE CARD WITHOUT VERIFYING ADM.", this.ICCID, this.woId,
//					LogType.DEBUG);
			// this.loggerService.log("ICCID OF THE CARD : " + this.ICCID, this.ICCID, this.woId, LogType.DEBUG);
			// System.out.println("\nIMSI OF THE CARD
			// ....................................................." + this.IMSI);
			// this.loggerService.log("IMSI OF THE CARD : " + this.IMSI, this.ICCID, this.woId, LogType.DEBUG);
			log("READING PROFILE FOR THE CARD WITHOUT VERIFYING ADM........................................................",
					"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
			log("ICCID OF THE CARD ....................................................." + this.ICCID,
					"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
			log("IMSI OF THE CARD ....................................................." + this.IMSI,
					"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
			// System.out.println("ICCID OF THE CARD : " + this.ICCID);
			// System.out.println("IMSI OF THE CARD : " + this.IMSI);
			log("AID OF THE CARD ....................................................." + this.aID,
					"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);

//			updateSmsContent();
			boolean config = readConfig();
			if (!config) {
				return false;
			}
			boolean configContent = readConfigContent();
			if (!configContent) {
				return false;
			}
			

		} else {
//			boolean srp2 = sendRawApduNoPrint("0020000A08" + this.aDM);
			if (!sendRawApduNoPrint("0020000A08" + this.aDM)) {
				// System.out.println("2");
				return false;
			}
			if (getSW1Text().equals("90")) {
				this.admFlag = 1;
				this.aID = getAID();
				// this.aID = "1234";
//				boolean srp3 = sendRawApduNoPrint("00A4040C10 " + this.aID);
				if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
					// System.out.println("3");
					return false;
				}
				this.ICCID = getICCID();
				this.IMSI = getIMSI();
				// System.out.println("\nREADING PROFILE FOR THE CARD
				// ..............................................................");
				this.logger
						.info("\nICCID OF THE CARD ....................................................." + this.ICCID);
				this.logger
						.info("\nIMSI OF THE CARD ....................................................." + this.IMSI);
				log("READING PROFILE FOR THE CARD WITHOUT VERIFYING ADM........................................................",
						"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
				log("ICCID OF THE CARD ....................................................." + this.ICCID,
						"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
				log("IMSI OF THE CARD ....................................................." + this.IMSI,
						"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
				boolean config = readConfig();
				if (!config) {
					return false;
				}
				boolean configContent = readConfigContent();
				if (!configContent) {
					return false;
				}
				updateSmsContent();
				
			} else {
				// System.out.println("ADM verify failed !! ............");
			}
		}
		try {
//			settleLogsIntoStorage();
		} catch (Exception e) {
			System.out.println("Error while sending logs into storage " + e.toString());
			// this.loggerService.log("Error while sending Profile logs into storage " + e.toString(), this.ICCID,
//					this.woId, LogType.ERROR);
		}
		return true;
	}

//	private void settleLogsIntoStorage() {
//
//		try {
//			File dir1 = new File(this.localProfileLogPath);
//			if (dir1.isDirectory()) {
//				String[] content = dir1.list();
//				for (int i = 0; i < content.length; i++) {
//					if (content[i].contains("PROFILE_TEST_" + this.woId + "_" + this.ICCID)) {
//						Path temp = Files.move(Paths.get(this.localProfileLogPath + content[i]),
//								Paths.get(this.storageProfileLogPath + content[i]));
//					}
//				}
//			}
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	public String getICCID() {
		// sendRawApduNoPrint("00A4000402 3F00");
		if (!sendRawApduNoPrint("00A4080402 2FE2")) {
			// System.out.println("4");
			return null;
		}
		if (!sendRawApduNoPrint("00B000000A")) {
			// System.out.println("5");
			return null;
		}
		return nibbleSwap(getResponse());
	}

	public String getIMSI() {
//		sendRawApduNoPrint("00A4000402 3F00");
		if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
			// System.out.println("5");
			return null;
		}
		if (!sendRawApduNoPrint("00A4090C02 6F07")) {
			// System.out.println("6");
			return null;
		}
		if (!sendRawApduNoPrint("00B0000009")) {
			// System.out.println("7");
			return null;
		}
		return nibbleSwap(getResponse());
	}

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

//	public void sendingRawApduNoPrint() {
//
//	}

	public boolean sendRawApduNoPrint(String paramString) {
		try {
			if (!sendCmd(paramString)) {
				// System.out.println("returning false from sendRawApduNoPrint");
				return false;
			}else {
				// System.out.println("returning true from sendRawApduNoPrint");
				return true;
			}
		} catch (Exception exception) {
			// System.out.println("Send APDU Error : "+paramString);
			return false;
		}
//		return true;
	}

//	public boolean sendRawApdu(String paramString) {
//		try {
//			boolean cmd = sendCmd(paramString);
//			if (!cmd) {
//				return false;
//			}
//		} catch (Exception exception) {
//			System.out.println("Send APDU Error!!" + exception.toString());
//			// this.loggerService.log("Send APDU Error!!" + exception.toString(), this.ICCID, this.woId, LogType.ERROR);
//		}
//		writeLogFile("CMD " + insertSpace(getCommand()) + "\tRESPONSE --->" + getSW());
//		return true;
//	}

	public boolean gotoFile(String paramString) {
		if (!paramString.equals("ADF")) {
			if (paramString.equals("ADF5FC0")) {
				String fiveg = "5FC0";
//				boolean srp4 = sendRawApduNoPrint("00A4040C10 " + this.aID);
				if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
					// System.out.println("8");
					return false;
				}
//				boolean srp5 = sendRawApduNoPrint("00A4000402 " + fiveg);
				if (!sendRawApduNoPrint("00A4000402 " + fiveg)) {
					// System.out.println("8");
					return false;
				}
			} else {
				int i = paramString.length();
				int j = i / 4;
				if (paramString.substring(0, 4).equals("7F01")) {
//				boolean srp6 = sendRawApduNoPrint("00A4040C10 " + this.aID);
					if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
						// System.out.println("10");
						return false;
					}
				}
				for (byte b = 0; b < j; b++) {
					int k = b * 4, m = k + 4;
					String str1 = paramString.substring(k, m);
//					boolean srp7 = sendRawApduNoPrint("00A4000402" + str1);
					if (!sendRawApduNoPrint("00A4000402" + str1)) {
						// System.out.println("11");
						return false;
					}
//					String str2 = getSW1Text() + getSW2Text();
				}
			}
		} else if (paramString.equals("ADF")) {
			if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
				// System.out.println("12");
				return false;
			}
		}
		return true;
	}

	public boolean gotoAMDO(String paramString) {
		if (!paramString.equals("ADF")) {
			if (paramString.equals("ADF5FC0")) {
//				boolean srp9 = sendRawApduNoPrint("00A4040C10 " + this.aID);
				if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
					// System.out.println("13");
					return false;
				}
			} else {
				int i = paramString.length();
				int j = i / 4;
				if (j == 3)
					j--;
				if (paramString.substring(0, 4).equals("7F01") && paramString.substring(4, 8).equals("5F3B")) {
//					boolean srp10 = sendRawApduNoPrint("00A4040C10 " + this.aID);
					if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
						// System.out.println("14");
						return false;
					}
				} else {
					for (byte b = 0; b < j; b++) {
						int k = b * 4, m = k + 4;
						String str1 = paramString.substring(k, m);
//						boolean srp11 = sendRawApduNoPrint("00A4000402" + str1);
						if (!sendRawApduNoPrint("00A4000402" + str1)) {
							// System.out.println("15");
							return false;
						}
//						String str2 = getSW1Text() + getSW2Text();
					}
				}
			}
		} else if (paramString.equals("ADF")) {
//			boolean srp12 = sendRawApduNoPrint("00A4040C10 " + this.aID);
			if (!sendRawApduNoPrint("00A4040C10 " + this.aID)) {
				// System.out.println("16");
				return false;
			}
		}
		return true;
	}

	public boolean compareFileAttributes(String[] paramArrayOfString) {
		// System.out.println("compare file atribute");
		try {
			String str1 = paramArrayOfString[0];
			String str2 = paramArrayOfString[1];

			if (!gotoFile(paramArrayOfString[1])){
				return false;
			}
			loggerThread.displayLogs(_terminal,_card,"00A4000402" + str1,widgetId);
			if (!sendRawApduNoPrint("00A4000402" + str1)) {
				// System.out.println("17");
				loggerThread.displayLogs(_card,_terminal,getSW1Text()+getSW2Text(),widgetId);
				return false;
			}
			loggerThread.displayLogs(_card,_terminal,getSW1Text()+getSW2Text(),widgetId);
			if (getSW1Text().equals("90")) {
				String str3 = insertSpace(getResponse());
				StringTokenizer stringTokenizer = new StringTokenizer(str3, " ");
				int i = stringTokenizer.countTokens();
				byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
				String str4 = "", str5 = "";
				String[] arrayOfString1 = new String[i];
				if (stringTokenizer.hasMoreTokens())
					for (byte b = 0; b < i; b++) {
						arrayOfString1[b] = stringTokenizer.nextToken();
						if (arrayOfString1[b].equals("82")) {
							if (b1 == 0)
								b1 = b; // value for the file descriptor
						}
						if (arrayOfString1[b].equals("83")) {
							if (b3 == 0)
								b3 = b; // value for the file ID
						}
						if (arrayOfString1[b].equals("80")) {
							// // System.out.println("Bvalue"+b);
							// // System.out.println("IValue"+i);
							if (b + 1 != i)
								b2 = b; // value for the length of record and file size
						}
						if (arrayOfString1[b].equals("8B")) {
							if (b4 == 0)
								b4 = b;// value for the access conditions
						}

					}
				String str6 = "", str8 = "", str9 = "", str10 = "", str11 = "", str12 = "";
				String[] arrayOfString2 = new String[20];
				String[] arrayOfString3 = { "FILE ID", "FILE PATH", "FILE TYPE", "READ CONDITION", "UPDATE CONDITION",
						"INVALIDATE CONDITION", "REHABILITATE CONDITION", "OTA UPDATABLE", "RECORD SIZE",
						"RECORD COUNT", "FILE SIZE" };
				String str13 = arrayOfString1[b1 + 2];
				String str14 = arrayOfString1[b3 + 2] + arrayOfString1[b3 + 3];
				int j = Integer.parseInt(arrayOfString1[b2 + 2] + arrayOfString1[b2 + 3], 16), k = 0, m = 0, n = 0,
						i1 = 0;
				if (b4 != 0) {
					String str21 = arrayOfString1[b4 + 1];
					if (str21.equals("03")) {
						str4 = arrayOfString1[b4 + 2] + arrayOfString1[b4 + 3];
						str5 = arrayOfString1[b4 + 4];
					} else if (str21.equals("06")) {
						str4 = arrayOfString1[b4 + 2] + arrayOfString1[b4 + 3];
						str5 = arrayOfString1[b4 + 5];
					} else {
						System.out.println(
								"AM_DO Condition not matching with the expended condition, may be compact access rules are defined");
					}
				} else {
					str4 = "2F06";
					str5 = "01";
				}
//				str4 = arrayOfString1[b4 + 2] + arrayOfString1[b4 + 3];
//				str5 = arrayOfString1[b4 + 4];
				String str15 = readAMDO(str4, str5, str2);
				if (str15.equals(str4)) {
				} else if (str15.equalsIgnoreCase("-1")) {
					return false;
				} else {
					str12 = getAccessRight(str15);
					i1 = str12.indexOf(",", 0);
					str8 = str12.substring(0, i1);
					n = i1 + 1;
					i1 = str12.indexOf(",", n + 1);
					str9 = str12.substring(n, i1);
					n = i1 + 1;
					i1 = str12.indexOf(",", n + 1);
					str11 = str12.substring(n, i1);
					n = i1 + 1;
					i1 = str12.length();
					str10 = str12.substring(n, i1);
				}
				if (!str13.equals("41")) {
					k = Integer.parseInt(arrayOfString1[b1 + 4] + arrayOfString1[b1 + 5], 16);
					m = Integer.parseInt(arrayOfString1[b1 + 6], 16);
				}
				switch (str13) {
				case "41":
					str6 = "T";
					break;
				case "42":
					str6 = "LF";
					break;
				case "43":
					str6 = "RFU";
					break;
				case "44":
					str6 = "RFU";
					break;
				case "45":
					str6 = "RFU";
					break;
				case "46":
					str6 = "C";
					break;
				}
				arrayOfString2[0] = str14;
				arrayOfString2[1] = "NA";
				paramArrayOfString[1] = "NA";
				arrayOfString2[2] = str6;
				arrayOfString2[3] = str8;
				arrayOfString2[4] = str9;
				arrayOfString2[5] = str11;
				arrayOfString2[6] = str10;
				arrayOfString2[7] = "NA";
				paramArrayOfString[7] = "NA";
				arrayOfString2[8] = Integer.toString(k);
				arrayOfString2[9] = Integer.toString(m);
				arrayOfString2[10] = Integer.toString(j);
				if (str6.equals("T")) {
					arrayOfString2[8] = "NA";
					arrayOfString2[9] = "NA";
				}
//				for (byte b5 = 0; b5 <= 10; b5++) {
//					if (!paramArrayOfString[b5].equals(arrayOfString2[b5])) {
//						// System.out.println("\n" + str1 + "\t" + str2 + "   NOTMATCHED  " + arrayOfString3[b5] + " EXPVAL : " + paramArrayOfString[b5] + "  CARDVAL : " + arrayOfString2[b5]);
//						if (!arrayOfString2[b5].equals(""))
//							log(str1 + "\t" + str2 + "   NOTMATCHED  " + arrayOfString3[b5] + " EXPVAL : " + paramArrayOfString[b5] + "  CARDVAL : " + arrayOfString2[b5], "PROFILE_TEST_"+this.woId+"_" + this.ICCID + "_", log_path);
//						
//					}
//				}
				for (byte b5 = 0; b5 <= 10; b5++) {
					if (!paramArrayOfString[b5].equals(arrayOfString2[b5])) {
						// System.out.println("\n" + str1 + "\t" + str2 + " NOTMATCHED " +
						// arrayOfString3[b5]+ " EXPVAL : " + paramArrayOfString[b5] + " CARDVAL : " +
						// arrayOfString2[b5]);
						log(str1 + "\t" + str2 + "   NOTMATCHED  " + arrayOfString3[b5] + " EXPVAL : "
								+ paramArrayOfString[b5] + "  CARDVAL : " + arrayOfString2[b5],
								"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
						if (!arrayOfString2[b5].equals(""))
							log(str1 + "\t" + str2 + "   NOTMATCHED  " + arrayOfString3[b5] + " EXPVAL : "
									+ paramArrayOfString[b5] + "  CARDVAL : " + arrayOfString2[b5],
									"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
						// System.out.println(str1 + "\t" + str2 + " NOTMATCHED " + arrayOfString3[b5] +
						// " EXPVAL : "+ paramArrayOfString[b5] + " CARDVAL : " + arrayOfString2[b5]);

					} else if (!arrayOfString2[b5].equals("NA")) {
						// System.out.println("\n" + str1 + "\t" + str2 + " MATCHED " +
						// arrayOfString3[b5] + " EXPVAL : "+ paramArrayOfString[b5] + " CARDVAL : " +
						// arrayOfString2[b5]);
						log(str1 + "\t" + str2 + "   MATCHED  " + arrayOfString3[b5] + " EXPVAL : "
								+ paramArrayOfString[b5] + "  CARDVAL : " + arrayOfString2[b5],
								"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
						if (!arrayOfString2[b5].equals(""))
							log(str1 + "\t" + str2 + "   MATCHED  " + arrayOfString3[b5] + " EXPVAL : "
									+ paramArrayOfString[b5] + "  CARDVAL : " + arrayOfString2[b5],
									"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
						// System.out.println(str1 + "\t" + str2 + " MATCHED " + arrayOfString3[b5] + "
						// EXPVAL : "+ paramArrayOfString[b5] + " CARDVAL : " + arrayOfString2[b5]);
					}
				}

			} else {
				log("File not found !!" + str1 + " at path " + paramArrayOfString[1],
						"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
				// this.loggerService.log("File not found !!" + str1 + " at path " + paramArrayOfString[1], this.ICCID,
//						this.woId, LogType.DEBUG);
				System.out.println("File not found !!" + str1 + " at path " + paramArrayOfString[1]);
				return false;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	public String readAMDO(String paramString1, String paramString2, String paramString3) {

		// System.out.println("readAMDO parmstr1" + paramString1 + " parmStr2 " +
		// paramString2 + " parmStr3" + paramString3);
		if (!gotoAMDO(paramString3)){
			return "-1";
		}


		if (paramString1.equals("2F06")){
			if (!sendRawApduNoPrint("00A4000402 3F00")){
				// System.out.println("18");
				return "-1";
			}
		}

		if (!sendRawApduNoPrint("00A4000402 " + paramString1)){
			// System.out.println("19");
			return "-1";
		}

		if (getSW1Text().equals("90")) {
			String str1 = insertSpace(getResponse());
			// System.out.println("readAMDo str1 : " + str1);
			StringTokenizer stringTokenizer = new StringTokenizer(str1, " ");
			int i = stringTokenizer.countTokens();
			byte b = 0;
			int j = 0;
			String str2 = "";

//			62 1A 82 05 42 21 00 2C 14 83 02 2F 06 8A 01 05 8B 03 2F 06 04 80 02 03 70 88 01 30
			String[] arrayOfString = new String[i];
			if (stringTokenizer.hasMoreTokens())
				for (byte b1 = 0; b1 < i; b1++) {
					arrayOfString[b1] = stringTokenizer.nextToken();
					if (arrayOfString[b1].equals("82"))
						b = b1;
				}
			str2 = arrayOfString[b + 5];
			j = Integer.parseInt(paramString2, 16);
			String str3 = Integer.toHexString(j);
			if (str3.length() == 1) {
				str3 = "0" + str3;
			}
			if(!sendRawApduNoPrint("00B2 " + str3 + "04" + str2)){
				// System.out.println("20");
				return "-1";
			}

			return getResponse();
		}
		// System.out.println("\t AMDO file not found !!" + paramString1);
		return paramString1;
	}

	public String getAccessRight(String paramString) {
		String str1 = "UNKNOWN";
		String str2 = "UNKNOWN";
		String str3 = "UNKNOWN";
		String str4 = "UNKNOWN";
		paramString = paramString.replaceAll(" ", "");
		// System.out.println("########## get accessRight param string " + paramString);
		int i;
		for (i = 0; i < paramString.length();) {
			String str = paramString.substring(i, i + 2);
			if (str.equals("80")) {
				i += 2;
				int j = Integer.parseInt(paramString.substring(i, i + 2));
				i += 2;
				String str5 = paramString.substring(i, i + j * 2);
				i += j * 2;
				String str6 = paramString.substring(i, i + 2);
				String str7 = "UNKNOWN";
				i += 2;
				if (str6.equals("A4")) {
					int m = Integer.parseInt(paramString.substring(i, i + 2));
					i += 2;
					String str10 = paramString.substring(i, i + m * 2);
					i += m * 2;
					str7 = getAccessCondition(str10);
				} else if (str6.equals("97")) {
					i += 2;
					str7 = "NEVER";
				} else if (str6.equals("90")) {
					i += 2;
					str7 = "ALWAYS";
				} else {
					i += 2;
				}
				int k = Integer.parseInt(str5, 16);
				String str8 = Integer.toBinaryString(k);
				String str9 = String.format("%8s", new Object[] { str8 }).replaceAll(" ", "0");
//				byte b = 3;
				if (str9.substring(3, 4).equalsIgnoreCase("1"))
					str4 = str7;
				if (str9.substring(4, 5).equalsIgnoreCase("1"))
					str3 = str7;
				if (str9.substring(5, 6).equalsIgnoreCase("1"))
					;
				if (str9.substring(6, 7).equalsIgnoreCase("1"))
					str2 = str7;
				if (str9.substring(7, 8).equalsIgnoreCase("1"))
					str1 = str7;
				continue;
			}
			i += 2;
		}
		return str1 + "," + str2 + "," + str3 + "," + str4;
	}

	private String getAccessCondition(String paramString) {
		if (paramString.equalsIgnoreCase("830101950108"))
			return "CHV1";
		if (paramString.equalsIgnoreCase("830102950108"))
			return "CHV2";
		if (paramString.equalsIgnoreCase("830103950108"))
			return "CHV3";
		if (paramString.equalsIgnoreCase("830104950108"))
			return "CHV4";
		if (paramString.equalsIgnoreCase("830105950108"))
			return "CHV5";
		if (paramString.equalsIgnoreCase("830106950108"))
			return "CHV6";
		if (paramString.equalsIgnoreCase("830107950108"))
			return "CHV7";
		if (paramString.equalsIgnoreCase("830108950108"))
			return "CHV8";
		if (paramString.equalsIgnoreCase("83010A950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83010B950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83010C950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83010D950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83010E950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("830111950108"))
			return "UCHV";
		if (paramString.equalsIgnoreCase("830181950108"))
			return "SCHV1";
		if (paramString.equalsIgnoreCase("830182950108"))
			return "SCHV2";
		if (paramString.equalsIgnoreCase("830183950108"))
			return "SCHV3";
		if (paramString.equalsIgnoreCase("830184950108"))
			return "SCHV4";
		if (paramString.equalsIgnoreCase("830185950108"))
			return "SCHV5";
		if (paramString.equalsIgnoreCase("830186950108"))
			return "SCHV6";
		if (paramString.equalsIgnoreCase("830187950108"))
			return "SCHV7";
		if (paramString.equalsIgnoreCase("830188950108"))
			return "SCHV8";
		if (paramString.equalsIgnoreCase("83018A950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83018B950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83018C950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83018D950108"))
			return "ADM";
		if (paramString.equalsIgnoreCase("83018E950108"))
			return "ADM";
		return "UNKNOWN";
	}

	public boolean sendCmd(String paramString) {
		// System.out.println("from sendCmd");

		paramString = paramString.toUpperCase();
		paramString = paramString.replaceAll(" ", "");
		// System.out.println("Param String in send cmd :-" + paramString);
//		loggerThread.displayLogs(_terminal,_card,paramString);
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
//							loggerThread.displayLogs(_card,_terminal,paramString +" "+exception.getMessage());
						}
						try {

							this._CommandApduBytes = arrayOfByte;
							this._Error = "";
							ResponseAPDU responseAPDU = null;

							try {
								responseAPDU = this.cardChannel.transmit(commandAPDU);
							} catch (Exception e) {
								boolean reset = true;
								forLoop: for (int i = 0; i < 3; i++) {
									reset = resetChannel();
									if (reset) {
										responseAPDU = this.cardChannel.transmit(commandAPDU);
										break forLoop;
									}
								}

								if (!reset) {
									// System.out.println("Disconnecting card for profile...");
									this.card.disconnect(false);
									loggerThread.displayLogs(_terminal, "Card disconnected, please connect again.",widgetId);
									return false;
								}

							}
							// System.out.println("SW : " + responseAPDU.getSW() + " SW1 : " +
							// responseAPDU.getSW1() + " SW2 : " + responseAPDU.getSW2());
							this._RspDataBytes = responseAPDU.getData();
							// System.out.println("this._RspDataBytes.length :-" +
							// this._RspDataBytes.length);
							// System.out.println("this is this._RspDataBytes:-" + this._RspDataBytes);
//							byte[] Bytes = this._RspDataBytes;
							this._RspDataString = byteArrayToHex(this._RspDataBytes);

							// System.out.println("this._RspDataString :-" + this._RspDataString);

							this._SW = Integer.toHexString(responseAPDU.getSW());
							this._SW1 = (byte) responseAPDU.getSW1();
							this._SW2 = (byte) responseAPDU.getSW2();
							// System.out.println("APDU : "+ paramString+"Response :"+this._SW);
//							loggerThread.displayLogs(_card,_terminal, this._SW);
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
//							loggerThread.displayLogs(_card,_terminal,paramString + " Invalid APDU");
						}
					}
				}
		} catch (CardException cardException) {
			cardException.printStackTrace();
			return false;
		}
		return true;
	}

//	public void close() {
//		try {
//			// getCard().disconnect(true);
//		} catch (Exception exception) {
//		}
//	}

	public boolean resetChannel() {
		try {
			this.cardChannel.close();
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
		try {
			this.card.disconnect(true);
			if (this.terminal.isCardPresent()) {
				this.card = this.terminal.connect("T=0");
			} else {
				return false;
			}
			this.cardChannel = this.card.getBasicChannel();
		} catch (Exception e2) {
//			e2.printStackTrace();
			return false;
		}
		return true;
	}

//	public void getATR() {
//		try {
//			if (getTerminal().isCardPresent()) {
//				this._Error = "";
//				this._RspDataString = "ATR: " + byteArrayToString(getCard().getATR().getBytes()) + "\n";
//			} else {
//				this._RspDataString = "";
//				this._Error = "Card Not Present";
//			}
//		} catch (CardException cardException) {
//			cardException.printStackTrace();
//		}
//	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean readConfig() {
		// System.out.println("from readConfig");
		try {

			Iterator<String> filesystemConfigs = this.fileSystemConfig.iterator();
			while (filesystemConfigs.hasNext()) {
				String str = filesystemConfigs.next();
				// System.out.println("str:" + str);
				str = str.replace(",,,,,", ",END,END,END,END,");
				str = str.replace(",,,,", ",END,END,END,");
				str = str.replace(",,,", ",END,END,");
				str = str.replace(",,", ",END,");
				StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
				int j = stringTokenizer.countTokens();
				String[] arrayOfString = new String[j];
				if (stringTokenizer.hasMoreTokens() && !stringTokenizer.equals("END"))
					for (byte b1 = 0; b1 < j; b1++)
						arrayOfString[b1] = stringTokenizer.nextToken();
				if (arrayOfString[0].equals("RESET")) {
					loggerThread.displayLogs(_terminal,_card,"RESET",widgetId);
					if (!resetChannel()) {
						return false;
					}
				}
				if (this.admFlag == 1) {
					if (!compareFileAttributes(arrayOfString)) {
						return false;
					}
				} else if (!arrayOfString[3].equals("ADM")) {
					if (!compareFileAttributes(arrayOfString)) {
						return false;
					}
				} else {
					System.out.println("\n Can not read file " + arrayOfString[0] + " at path " + arrayOfString[1]
							+ " ADM required");
					// this.loggerService.log(
//							"Can not read file " + arrayOfString[0] + " at path " + arrayOfString[1] + " ADM required",
//							this.ICCID, this.woId, LogType.DEBUG);
					log("Can not read file " + arrayOfString[0] + " at path " + arrayOfString[1] + " ADM required",
							"PROFILE_TEST_" + this.woId + "_" + this.ICCID + "_", log_path);
				}
			}

		} catch (Exception exception) {
			// System.out.println("Exception in readConfig method as " + exception);
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean readConfigContent() {

		try {
			// System.out.println("Reading content config : ");
			Iterator<String> fileContentConfigs = this.fileContentConfig.iterator();
			while (fileContentConfigs.hasNext()) {
				String str = fileContentConfigs.next();
				str = str.replace(",,,,,", ",END,END,END,END,");
				str = str.replace(",,,,", ",END,END,END,");
				str = str.replace(",,,", ",END,END,");
				str = str.replace(",,", ",END,");
				StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
				int j = stringTokenizer.countTokens();
				String[] arrayOfString = new String[j];
				if (stringTokenizer.hasMoreTokens() && !stringTokenizer.equals("END"))
					for (byte b1 = 0; b1 < j; b1++)
						arrayOfString[b1] = stringTokenizer.nextToken();
				if (this.admFlag == 1) {

				} else if (!arrayOfString[3].equals("ADM")) {

				} else {
					System.out.println("\n Can not read file " + arrayOfString[0] + " at path " + arrayOfString[1]
							+ " ADM required");
					// this.loggerService.log(
//							"Can not read file " + arrayOfString[0] + " at path " + arrayOfString[1] + " ADM required",
//							this.ICCID, this.woId, LogType.DEBUG);
				}
			}

		} catch (Exception exception) {
			// System.out.println("Exception in readConfig method as " + exception);
			exception.printStackTrace();
			return false;
		}
		return true;
	}

	public void updateSmsContent() {
		Calendar calendar = Calendar.getInstance();
		String date = formatN("" + calendar.get(5), 2) + formatN("" + (calendar.get(2) + 1), 2)
				+ formatN("" + calendar.get(1), 4);
		String time = formatN("" + calendar.get(11), 2) + formatN("" + calendar.get(12), 2);
		
		String updatedContent="00DC0304B0AB" +date+ "AB"+time+"AB50524F46494C45FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFAB";
		sendRawApduNoPrint("00A4000C023F00");
		sendRawApduNoPrint("00A4000C027F10");
		sendRawApduNoPrint("00A4000C026F3C");
		sendRawApduNoPrint(updatedContent);
		// System.out.println(updatedContent + getSW1Text() + getSW2Text());
	}


//	public byte[] getATRBytes() {
//		byte[] arrayOfByte = null;
//		try {
//			if (getTerminal().isCardPresent()) {
//				arrayOfByte = getCard().getATR().getBytes();
//			} else {
//				arrayOfByte = null;
//			}
//		} catch (CardException cardException) {
//			cardException.printStackTrace();
//		}
//		return arrayOfByte;
//	}

	public String getResponse() {
		return this._RspDataString.toUpperCase();
	}

//	public String getSW() {
//		return this._SW.toUpperCase();
//	}

//	public byte[] getResponseBytes() {
//		return this._RspDataBytes;
//	}

	public String getError() {
		return this._Error;
	}

//	public byte[] getCommandBytes() {
//		return this._CommandApduBytes;
//	}
//
//	public String getCommand() {
//		return byteArrayToString(this._CommandApduBytes);
//	}

//	public int openChannel() throws CardException {
//		CardChannel cardChannel = getCard().openLogicalChannel();
//		cardChannel.close();
//		return cardChannel.getChannelNumber();
//	}

	public byte getSW1() {
		return this._SW1;
	}

	public byte getSW2() {
		return this._SW2;
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

	public String getAID() {
		String str1 = "", str2 = "";
		// System.out.println("sending RawAPDU :-");
		// sendRawApduNoPrint("A0A4000002 3F00");

		if (!sendRawApduNoPrint("00A4080402 2F00")) {
			// System.out.println("21");
			return null;
		}
		str1 = insertSpace(getResponse());
		// System.out.println("Str1 after 2F00 :-" + str1);
		str2 = fetchRecordSize(str1);
		if (!sendRawApduNoPrint("00B20104" + str2)) {
			// System.out.println("22");
			return null;
		}
		str1 = getResponse();

		// System.out.println("Str1 response : " + str1);

		if (!str1.equals(null))
			str1 = getResponse().substring(8, 40);
		return str1.toUpperCase();
	}
//
//	public boolean setADM() {
////		boolean srp20 = sendRawApduNoPrint("A020000A08 " + this.admKEY);
//		if (!sendRawApduNoPrint("A020000A08 " + this.admKEY)) {
//			// System.out.println("23");
//			return false;
//		}
//		return true;
//	}

//	private String compareContent(String paramString1, String paramString2) {
//		paramString2 = paramString2.replaceAll(" ", "");
//		paramString1 = paramString1.replaceAll(" ", "");
//		String str = "";
//		String[] arrayOfString = paramString1.split(",");
//		if (arrayOfString.length == 2)
//			if (arrayOfString[0].equalsIgnoreCase(paramString2)) {
//				paramString1 = arrayOfString[0];
//			} else if (arrayOfString[1].equalsIgnoreCase(paramString2)) {
//				paramString1 = arrayOfString[1];
//			} else {
//				paramString1 = paramString1.replaceAll(",", "OR");
//			}
//
//		if (paramString2.equalsIgnoreCase(paramString1)) {
//			str = "MATCH";
//		} else {
//			str = "NOMATCH";
//		}
//		return str;
//	}

//	public String byteArrayToString(byte[] paramArrayOfbyte) {
//		StringBuffer stringBuffer = new StringBuffer();
//		for (int b = 0; b < paramArrayOfbyte.length; b++) {
//			String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
//			if (str.length() == 1)
//				stringBuffer.append(0);
//			stringBuffer.append(str);
//		}
//		return stringBuffer.toString().toUpperCase();
//	}

	public String byteArrayToHex(byte[] a) {

		StringBuilder sb = new StringBuilder(a.length * 2);

		for (byte b : a) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();

	}

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

//	public void writeLogFile(String paramString) {
////    try {
////      if (paramString != null)
////        out.write(paramString);
////      // System.out.println(paramString);
////    } catch (IOException iOException) {}
//	}

	public void log(String paramString1, String paramString2, String paramString3) {
//		try {
//
////			File directory = new File(this.properties.getProperty("profileLogPath"));
//
//			// this.loggerService.log(paramString1, this.ICCID, this.woId, LogType.DEBUG);
//			Calendar calendar = Calendar.getInstance();
//			@SuppressWarnings("unused")
//			String str1 = paramString2 + formatN("" + calendar.get(1), 4) + formatN("" + (calendar.get(2) + 1), 2)
//					+ formatN("" + calendar.get(5), 2);
//			String str2 = formatN("" + calendar.get(1), 4) + formatN("" + (calendar.get(2) + 1), 2)
//					+ formatN("" + calendar.get(5), 2);
//			String str3 = formatN("" + calendar.get(11), 2) + formatN("" + calendar.get(12), 2)
//					+ formatN("" + calendar.get(13), 2);
//			String str4 = getdate(1);
//			String str5 = str4.substring(0, 6);
//
////			File file2 = new File(this.properties.getProperty("profileLogPath") +"/"+ paramString2 +"_" + str5  + ".txt");
//			File file2 = new File(this.localProfileLogPath + paramString2 + str2 + ".txt");
//			if (!file2.exists()) {
//				file2.createNewFile();
//			}
//
////			FileOutputStream fileOutputStream = new FileOutputStream(paramString3 + "/" + str5 + "/" + str1 + ".txt",
////					true);
//
//			FileOutputStream fileOutputStream = new FileOutputStream(file2, true);
//			PrintStream printStream = new PrintStream(fileOutputStream);
//			printStream.println("#" + str2 + "#" + str3 + "#" + paramString1);
//			printStream.close();
//			fileOutputStream.close();
//		} catch (Exception exception) {
//			// System.out.println("GOT Exception in LOG method as:" + exception);
//		}
	}

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

	private String formatN(String paramString, int paramInt) {
		String str = "";
		int i = paramString.length();
		if (i >= paramInt) {
			str = paramString;
		} else {
			for (byte b = 0; b < paramInt - i; b++)
				str = str + "0";
			str = str + paramString;
		}
		return str;
	}

	public Card getCard() {
		return this.card;
	}

//	public void setCard(Card card) {
//		this.card = card;
//	}

	public CardTerminal getTerminal() {
		return this.terminal;
	}

	public void setTerminal(CardTerminal terminal) {
		this.terminal = terminal;
	}

	private void init() {
		try {

			// Get Profile config from server
			this.setProfileConfig(this.terminalInfo.getTerminalCardIccid(), this.terminalInfo.getWoId());

			this.setTerminal(this.terminalInfo.getCt());
			// Card c = this.getTerminal().connect("T=0");
			this.cardChannel = this.card.getBasicChannel();

			this.localProfileLogPath = "D:\\Work\\R&D\\SIMVerify\\logs\\ProfileLogs\\SimVerify_Profile\\";
			this.storageProfileLogPath = "Z:\\Offline Test Logs\\Profile Test\\" + this.woId + "\\";

//			createLogFolders();

		} catch (Exception e) {
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
//		File profile = new File("C:\\logs\\Profile_Logs");
//		if (!profile.exists()) {
//			profile.mkdir();
//		}
//
//		File workOrder = new File("C:\\logs\\Profile_Logs\\" + this.woId);
//		if (!workOrder.exists()) {
//			workOrder.mkdir();
//		}
//
//		File storageOffline = new File("Z:\\Offline Test Logs");
//		if (!storageOffline.exists()) {
//			storageOffline.mkdir();
//		}
//
//		File storageProfile = new File("Z:\\Offline Test Logs\\Profile Test");
//		if (!storageProfile.exists()) {
//			storageProfile.mkdir();
//		}
//
//		File storageProfileWorkOrder = new File("Z:\\Offline Test Logs\\Profile Test\\" + this.woId);
//		if (!storageProfileWorkOrder.exists()) {
//			storageProfileWorkOrder.mkdir();
//		}
//
//	}

	private void setProfileConfig(String terminalCardIccid, String woId2) {

//		TrakmeServerCommunicationServiceImpl trakmeServerCommunicationService = new TrakmeServerCommunicationServiceImpl();
//		ResponseProfileTestingConfig serverResponse = trakmeServerCommunicationService
//				.getProfileTestingConfig(terminalCardIccid, woId2, terminalInfo.getUserName());

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

		// System.out.println("jsonString : "+jsonString);

		Gson gson = new Gson();
		ResponseProfileTestingConfig serverResponse = gson.fromJson(jsonString, ResponseProfileTestingConfig.class);

		// System.out.println("ServerResponse : "+serverResponse.toString());
		// System.out.println("fileSystemConfig : "+serverResponse.getFileSystemConfig());


		this.fileSystemConfig = serverResponse.getFileSystemConfig();
		// System.out.println(this.fileSystemConfig);

		this.fileContentConfig = serverResponse.getFileContentConfig();
		// System.out.println(this.fileContentConfig);
		// System.out.println("fileSystemConfig" + this.fileSystemConfig);
	}

	CardChannel cardChannel = null;
	List<String> fileSystemConfig;
	List<String> fileContentConfig;
	String log_path = "";
	private Card card;
	private CardTerminal terminal = null;
	byte _SW1;
	byte _SW2;
	String _RspDataString;
	String _SW;
	byte[] _RspDataBytes;
	String _Error;
	byte[] _CommandApduBytes;
	private int admFlag = 0;
	String aID;
	String aDM;
	String admKEY;
	String ICCID;
	String IMSI;
	String woId;
	int terminalId;
	private TerminalInfo terminalInfo;
	private final Logger logger = LoggerFactory.getLogger(ProfileTest3G.class);
	private Properties properties;
	String localProfileLogPath;
	String storageProfileLogPath;

}