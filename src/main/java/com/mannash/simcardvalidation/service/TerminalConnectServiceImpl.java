package com.mannash.simcardvalidation.service;

import com.mannash.simcardvalidation.SimVerifyLoggerThread;
import com.mannash.simcardvalidation.SimVerifyMasterThread2;
import com.mannash.simcardvalidation.pojo.TerminalInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.smartcardio.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//import com.mannash.trakme.client.pojo.TerminalInfo;

@SuppressWarnings("restriction")
public class TerminalConnectServiceImpl implements TerminalConnectService {
	private final Logger logger = LoggerFactory.getLogger(TerminalConnectServiceImpl.class);
	private LoggerService loggerService;
//	private TestingController4 controller ;
	private SimVerifyMasterThread2 controller;

	private SimVerifyLoggerThread loggerThread;
	String AID;
	int counter = 0;
	CardChannel cardChannel = null;
	public String _terminal = "T";
	public String _card = "C";
	public String _device = "D";
	public String _ui = "UI";
	SimVerifyMasterThread2 simVerifyMasterThread2 ;

	public TerminalConnectServiceImpl(SimVerifyLoggerThread loggerThread, SimVerifyMasterThread2 masterThread) {
		this.loggerThread = loggerThread;
		this.loggerService = new LoggerServiceImpl();
		this.simVerifyMasterThread2 = masterThread;
	}

	public List<TerminalInfo> fetchTerminalInfo() {
		List<String> iccidList = new ArrayList<String>();
		List<TerminalInfo> terminalInfos = new ArrayList<TerminalInfo>();
		clearTerminal();
		TerminalFactory terminalFactory = null;
		try {
			terminalFactory = TerminalFactory.getInstance("PC/SC", null);

			// this.logger.debug("Terminal factory : " + terminalFactory);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		List<CardTerminal> list = null;
		try {
			list = terminalFactory.terminals().list();
			if (list == null){
				this.simVerifyMasterThread2.displayLogs(_terminal,"No device found",-1);
				

			}else {
				this.simVerifyMasterThread2.displayLogs(_terminal,"Devices available",-1);
				this.simVerifyMasterThread2.displayLogs(_terminal,_card,"Connecting to devices",-1);
			}
			for (CardTerminal cardTerminal : list) {
				try {

					System.out.println("Card Terminal : " + cardTerminal.toString());
					// cardTerminal.waitForCardPresent(10000);
					TerminalInfo terminalInfo = new TerminalInfo();
					terminalInfo.setCt(cardTerminal);
					for (int i = 0; i < cardTerminal.getName().length(); i++) {
						if (Character.isDigit(cardTerminal.getName().charAt(i))) {
							String n = "" + cardTerminal.getName().charAt(i);
							terminalInfo.setTerminalNumber(Integer.parseInt(n));
						}
					}
					terminalInfos.add(terminalInfo);
					if (cardTerminal.isCardPresent()) {

						try {
							Card card = cardTerminal.connect("T=0");
							// this.logger.debug("Card is present : " + card);
							cardChannel = card.getBasicChannel();
							// ATR atr = card.getATR();
						} catch (CardException e) {
							terminalInfo.setTerminalCardIccid(null);
							terminalInfo.setImsi(null);
							System.out.println("Card connect Exception");
//							this.simVerifyMasterThread2.displayLogs(_terminal,"Card not responding",-1);
							continue;

						}

						try {
							this.AID = getAID(cardTerminal);
						} catch (Exception e) {
							// this.logger.error("Exception in getAID");
							System.out.println("AID exception");
						}
						String iccid = null;
						String imsi = null;
						iccid = getICCID(cardTerminal);
						System.out.println("ICCID IMpl : " + iccid);
						imsi = getIMSI(cardTerminal);
						System.out.println("IMSI impl : " + imsi);
						if (iccid != null && !"".equalsIgnoreCase(iccid)) {
							terminalInfo.setTerminalCardIccid(iccid);
							terminalInfo.setImsi(imsi);
						}
					}
				} catch (CardException cardException) {
					System.out.println("Exception 1");

					cardException.printStackTrace();
				} catch (Exception cardException) {
					System.out.println("Exception 2");
					cardException.printStackTrace();

				}

			}
		} catch (Throwable e) {
			e.printStackTrace();
			this.simVerifyMasterThread2.displayLogs(_terminal,"No device found",-1);
			// this.logger.info("Terminal is not connected.");
		}
		return terminalInfos;
	}

	@Override
	public int fetchTerminalCount(){
		clearTerminal();
		TerminalFactory terminalFactory = null;
		try {
			terminalFactory = TerminalFactory.getInstance("PC/SC", null);

			// this.logger.debug("Terminal factory : " + terminalFactory);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		List<CardTerminal> list = null;
		try {
			list = terminalFactory.terminals().list();
		}catch (Exception e){
			e.printStackTrace();
		}
		if (list == null){
			return 0;
		}else {
			return list.size();
		}
	}

	public String getICCID(CardTerminal cardTerminal) {
		try {
			String response =  null;
//			cardTerminal = TerminalFactory.getDefault().terminals().list().get(0);
			// sendRawApduNoPrint(cardTerminal, "A0A4000002 3F00");

			sendRawApduNoPrint(cardTerminal, "00A4080402 2FE2");
			response = sendRawApduNoPrint(cardTerminal, "00B000000A");
			System.out.println("Response :  " + response);
			if (response != null) {
				return nibbleSwap(response);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("Exception from getICCID");
			e.printStackTrace();
			return null;
			// this.logger.error("Exception occurred while connecting with Card");
		}

	}

	public String getIMSI(CardTerminal cardTerminal) {
		try {
//			sendRawApduNoPrint(cardTerminal,"00A4000402 3F00");
			sendRawApduNoPrint(cardTerminal, "00A4040C10 " + this.AID);
			sendRawApduNoPrint(cardTerminal, "00A4090C02 6F07");
			String s1 = nibbleSwap(sendRawApduNoPrint(cardTerminal, "00B0000009"));
			s1 = s1.substring(3);
			return s1;
		} catch (Exception e) {
			// e.printStackTrace();
			return "0000000000000000";
		}
	}

	public String getAID(CardTerminal cardTerminal) {
		String str1 = "", str2 = "";
//			sendRawApduNoPrint(cardTerminal,"00A4000002 3F00");
		try {
			str1 = insertSpace(sendRawApduNoPrint(cardTerminal, "00A4080402 2F00"));
			str2 = fetchRecordSize(str1);
			str1 = sendRawApduNoPrint(cardTerminal, "00B20104" + str2);
			if (!str1.equals(null))
				str1 = str1.substring(8, 40);
		} catch (Exception e) {

			// this.logger.error("Null Pointer Exception : " + str1);
		}
		return str1.toUpperCase();
	}

	public String fetchRecordSize(String paramString) {
		StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ");
		int i = stringTokenizer.countTokens();
		byte b1 = 0, b2 = 0;
		int j = 0;
		String str = "";
		String[] arrayOfString = new String[i];
		try {
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
			if (str.length() == 1) {
				str = "0" + str;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			// this.logger.error("ArrayIndexOutOfBoundException at" + j);
		}
		return str;
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

	public String sendRawApduNoPrint(CardTerminal cardTerminal, String paramString) {
		try {
			return sendCmd(cardTerminal, paramString);
		} catch (Exception exception) {
			System.out.println("INSIDE THE EXCEPTION OF SENDRAWAPDUNOPRINT");
			// this.logger.error("Send APDU Error : " +paramString);
		}
		return null;
	}

	public String sendCmd(CardTerminal cardTerminal, String paramString) {
		paramString = paramString.toUpperCase();
		paramString = paramString.replaceAll(" ", "");
		if (paramString.length() % 2 != 0) {
			// this.logger.error("Command: " + paramString + " 1 nibble is missing in APDU.
			// Please Check");
		} else {
			byte[] arrayOfByte = new byte[paramString.length() / 2];
			boolean bool = true;
			for (int i = 0; i < paramString.length(); i += 2) {
				String str = paramString.substring(i, i + 2);
				try {
					int j = Integer.parseInt(str, 16);
					arrayOfByte[i / 2] = (byte) j;
				} catch (Exception exception) {
					// this.logger.error("Command: " + paramString + " Invalid Hex Value in the
					// APDU. Please Check");
					bool = false;
					break;
				}
			}
			if (bool) {
				CommandAPDU commandAPDU = null;
				try {
					commandAPDU = new CommandAPDU(arrayOfByte);
				} catch (Exception exception) {
					// this.logger.error("Command: " + paramString + "ERROR: " +
					// exception.getMessage());
				}
				try {
					ResponseAPDU responseAPDU = cardChannel.transmit(commandAPDU);
//					// this.logger.debug("SW : " + String.format("%4s", Integer.toHexString(responseAPDU.getSW()))
//							+ " SW1 : " + String.format("%2s", Integer.toHexString(responseAPDU.getSW1())) + " SW2 : "
//							+ String.format("%2s", Integer.toHexString(responseAPDU.getSW2())) + " response length: "
//							+ responseAPDU.getData().length);
//					if (responseAPDU.getSW1() == 110) {
//						this.counter++;
//						// this.logger.debug("counter  : " + this.counter);
//						if (this.counter >= 5) {
//							return null;
//						}
//						getICCID(cardTerminal);
//					}
					String fetchedIccid = byteArrayToString(responseAPDU.getData());
					return fetchedIccid;
				} catch (Exception exception) {
					// exception.printStackTrace();
					// this.logger.error("Command: " + paramString + " Invalid APDU. Please Check");
				}
			}
		}
		return null;
	}

	public String byteArrayToString(byte[] paramArrayOfbyte) {
		StringBuffer stringBuffer = new StringBuffer();
		for (byte b = 0; b < paramArrayOfbyte.length; b++) {
			String str = Integer.toHexString(paramArrayOfbyte[b] & 0xFF);
			if (str.length() == 1)
				stringBuffer.append(0);
			stringBuffer.append(str);
		}
		return stringBuffer.toString().toUpperCase();
	}

	public static String nibbleSwap(String paramString) {
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

	private void clearTerminal(){
		try {
			Class pcscterminal = Class.forName("sun.security.smartcardio.PCSCTerminals");
			Field contextId = pcscterminal.getDeclaredField("contextId");
			contextId.setAccessible(true);

			if (contextId.getLong(pcscterminal) != 0L) {
				// First get a new context value
				Class pcsc = Class.forName("sun.security.smartcardio.PCSC");
				Method SCardEstablishContext = pcsc.getDeclaredMethod(
						"SCardEstablishContext",
						new Class[]{Integer.TYPE}
				);
				SCardEstablishContext.setAccessible(true);

				Field SCARD_SCOPE_USER = pcsc.getDeclaredField("SCARD_SCOPE_USER");
				SCARD_SCOPE_USER.setAccessible(true);

				long newId = ((Long) SCardEstablishContext.invoke(pcsc,
						new Object[]{SCARD_SCOPE_USER.getInt(pcsc)}
				));
				contextId.setLong(pcscterminal, newId);


				// Then clear the terminals in cache
				TerminalFactory factory = TerminalFactory.getDefault();
				CardTerminals terminals = factory.terminals();
				Field fieldTerminals = pcscterminal.getDeclaredField("terminals");
				fieldTerminals.setAccessible(true);
				Class classMap = Class.forName("java.util.Map");
				Method clearMap = classMap.getDeclaredMethod("clear");

				clearMap.invoke(fieldTerminals.get(terminals));
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public String toString() {
		return "TerminalConnectServiceImpl [fetchTerminalInfo()=" + fetchTerminalInfo() + "]";
	}
}
