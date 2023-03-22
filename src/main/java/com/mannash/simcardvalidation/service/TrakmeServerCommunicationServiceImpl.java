package com.mannash.simcardvalidation.service;

import com.google.gson.Gson;
import com.mannash.simcardvalidation.SimVerifyLoggerThread;
import com.mannash.simcardvalidation.pojo.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
//import com.mannash.trakme.client.pojo.LogType;
//import com.mannash.trakme.client.pojo.RequestClientLogPojo;
//import com.mannash.trakme.client.pojo.ResponseAuthenticationPojo;
//import com.mannash.trakme.client.pojo.ResponseFieldTestingCardInfos;
//import com.mannash.trakme.client.pojo.ResponseFieldTestingProfileConfigPojo;
//import com.mannash.trakme.client.pojo.ResponseProfileTestingConfig;
//import com.mannash.trakme.client.pojo.ResponseStressTestingConfig;
//import com.mannash.trakme.client.pojo.RequestClientLogsPojo;
//import com.mannash.trakme.client.pojo.ServerResponseLogPojo;
//import com.mannash.trakme.client.service.LoggerService;
//import com.mannash.trakme.client.service.LoggerServiceImpl;
//import com.mannash.trakmeserver.rest.service.FieldTestingClientLoggerServiceImpl;

public class TrakmeServerCommunicationServiceImpl implements TrakmeServerCommunicationService {

	private LoggerService loggerService;
	private ResponseAuthenticationPojo authenticationPojo;
	private final Logger logger = LoggerFactory.getLogger(TrakmeServerCommunicationServiceImpl.class);
	public String hostIP = "";

	SimVerifyLoggerThread simVerifyLoggerThread;
	public TrakmeServerCommunicationServiceImpl(SimVerifyLoggerThread simVerifyLoggerThread) {
		this.simVerifyLoggerThread = simVerifyLoggerThread;

//		 this.loggerService = new LoggerServiceImpl();

//		File file = new File("config/config.conf");
//		if(!file.exists()) {
//			JOptionPane.showMessageDialog(null, "Config file does not exist");
//			return ;
//		}
//		BufferedReader br = null;
//		try {
//			br = new BufferedReader(new FileReader(file));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String st;
//		try {
//			while ((st = br.readLine()) != null) {
//				this.hostIP = st;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	Gson gson;

	ResponseAuthenticationPojo responseAuthenticationPojo = new ResponseAuthenticationPojo();

	@SuppressWarnings("deprecation")
	public ResponseAuthenticationPojo authenticateClient(String userName, String password) {

		ResponseAuthenticationPojo responseAuthenticationPojo = new ResponseAuthenticationPojo();
		if (userName == null || password == null || userName.isEmpty() || password.isEmpty()) {
			responseAuthenticationPojo.setMessage("Username or Password can not be empty");
			return responseAuthenticationPojo;
		}

		else {
			try {
				org.apache.http.client.HttpClient client = new DefaultHttpClient();
				String completeUrl = "http://localhost:8080/trakmeserver/api/external/auth/validateUser?userId="
						+ userName + "&password=" + URLEncoder.encode(password);

				HttpPost httpPost = new HttpPost(completeUrl);
				httpPost.setHeader("Content-type", "application/json");
				// this.logger.info("Sending request to TrakmeServer for authenticating user : " + userName);
				HttpResponse response = client.execute(httpPost);
				if (response != null) {
					String responseString = EntityUtils.toString((HttpEntity) response.getEntity());
					if (response.getStatusLine().getStatusCode() != 200) {
						if (responseString != null) {
							if (response.getStatusLine().getStatusCode() == 401) {
								responseAuthenticationPojo.setMessage("Invalid username/password");
								// this.logger.error("Invalid Username & Password , Status code : " + 401);
								responseAuthenticationPojo.setStatusCode(401);
							} else {
								responseAuthenticationPojo.setMessage("Unable to authenticate user");
								// this.logger.debug("Unable to authenticate user with Status code : " + response.getStatusLine().getStatusCode());
								responseAuthenticationPojo.setStatusCode(response.getStatusLine().getStatusCode());
							}
						} else {
							responseAuthenticationPojo.setMessage("Unable to authenticate user");
							responseAuthenticationPojo.setStatusCode(500);
							// this.logger.debug("Unable to authenticate user with Status code : " + 500);
						}
					} else {
						responseAuthenticationPojo.setStatusCode(200);
						// this.logger.info("TrakmeServer authenticated successfully with user" + userName);
						// System.out.println("Status Code : " + 200);
					}
				} else {
					responseAuthenticationPojo.setMessage("Unable to authenticate user");
					responseAuthenticationPojo.setStatusCode(500);
					// this.logger.debug("Unable to authenticate user, response is null");
				}
			} catch (Exception e) {
				e.printStackTrace();
				responseAuthenticationPojo.setMessage("Unable to authenticate user");
				responseAuthenticationPojo.setStatusCode(500);
				// this.logger.info("Unable to authentcate user");
			}
			return responseAuthenticationPojo;
		}

	}

	public ResponseFieldTestingCardInfos fetchWorkOrderInfo(String userName) {

		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/getftIccid?usrId="
					+ userName;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpGet get = new HttpGet(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(get);
			// String responseString = "{ \"responseFieldTestingCardPojos\": [ {
			// \"cardIccid\": \"8991000905506201104F\", \"cardTestingPercentage\": 0.0,
			// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 29,
			// \"woId\": 908, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
			// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
			// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 55,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
			// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
			// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 56,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
			// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
			// \"OTA_TESTING\", \"fieldTestingCardStageId\": 57,
			// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
			// \"8991000905506200510F\", \"cardTestingPercentage\": 0.0,
			// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
			// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
			// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
			// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
			// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
			// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
			// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
			// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
			// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
			// \"fieldTestingStageStatus\": null } ] } ] }";
			if (response.getEntity() != null) {
				String responseString = EntityUtils.toString(response.getEntity());

				// String responseString = "{ \"responseFieldTestingCardPojos\": [ {
				// \"cardIccid\": \"8991000905506201104F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 29,
				// \"woId\": 908, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 55,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 56,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"COMPLETED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 57,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905506200510F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905506200387F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700780F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700764F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700566F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905508700749F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] }, { \"cardIccid\":
				// \"8991000905506200585F\", \"cardTestingPercentage\": 0.0,
				// \"fieldTestingStatus\": \"TESTING_IN_PROGRESS\", \"fieldTestingCardId\": 37,
				// \"woId\": 919, \"fieldTestingCardStagePojos\": [ { \"fieldTestingStageId\":
				// 1, \"fieldTestingCardStageStatus\": \"ERROR\", \"fieldTestingStageName\":
				// \"PROFILE_TESTING\", \"fieldTestingCardStageId\": 79,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 2,
				// \"fieldTestingCardStageStatus\": \"ONGOING\", \"fieldTestingStageName\":
				// \"STRESS_TESTING\", \"fieldTestingCardStageId\": 80,
				// \"fieldTestingStageStatus\": null }, { \"fieldTestingStageId\": 3,
				// \"fieldTestingCardStageStatus\": \"NOT_STARTED\", \"fieldTestingStageName\":
				// \"OTA_TESTING\", \"fieldTestingCardStageId\": 81,
				// \"fieldTestingStageStatus\": null } ] } ] }";
				ResponseFieldTestingCardInfos serverResponse = (ResponseFieldTestingCardInfos) gson
						.fromJson(responseString, ResponseFieldTestingCardInfos.class);

				return serverResponse;
			} else {
				return null;
			}

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

		finally {

			try {

				client.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	public void updateWOStatus(String woID, String iccid, String status, String userName) {
		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/client/wo/status?usrId="
					+ userName + "&woId=" + woID + "&iccid=" + iccid + "&status=" + status;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpPost post = new HttpPost(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateCardStageStatus(String woID, String iccid, int cardStageId, String status, String userName) {
		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/client/card/status?usrId="
					+ userName + "&woId=" + woID + "&iccid=" + iccid + "&status=" + status + "&cardStageId="
					+ cardStageId;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpPost post = new HttpPost(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateCardStageCounter(String woID, String iccid, int cardStageId, long counter, String userName) {
		System.out.println("### current counter which is being sent to server : " + counter);
		CloseableHttpClient client = HttpClients.createDefault();

		try {

			String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/client/card/counter?usrId="
					+ userName + "&woId=" + woID + "&iccid=" + iccid + "&counter=" + counter + "&cardStageId="
					+ cardStageId;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpPost post = new HttpPost(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(post);
			String responseString = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ResponseProfileTestingConfig getProfileTestingConfig(String iccid, String woId, String userName) {

		CloseableHttpClient client = HttpClients.createDefault();
		ResponseProfileTestingConfig responseProfileTestingConfig = new ResponseProfileTestingConfig();
		try {

			String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
					+ userName + "&woId=" + woId;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpGet get = new HttpGet(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(get);
//			String responseString = "{\"fileSystemConfig\":[\"2F05,3F00,T,ALWAYS,CHV1,ADM,ADM,YES,NA,NA,8\",\"2FE2,3F00,T,ALWAYS,NEVER,ADM,ADM,NO,NA,NA,10\",\"2F00,3F00,LF,ALWAYS,ADM,ADM,ADM,YES,53,1,53\"],\"fileContentConfig\":[\"2F05,3F00,T,ALWAYS,1,1,FFFFFFFFFFFFFFFF\",\"2FE2,3F00,T,ALWAYS,1,1,ICCIDI\",\"2F00,3F00,LF,ALWAYS,1,1,41697274656C203447\"]}";
			// this.System.out.println("Sever Response : " + responseString);
			String responseString = EntityUtils.toString(response.getEntity());

			ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = (ResponseFieldTestingProfileConfigPojo) gson
					.fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
			responseProfileTestingConfig
					.setFileContentConfig(responseFieldTestingProfileConfigPojo.getFileContentConfigs());
			responseProfileTestingConfig
					.setFileSystemConfig(responseFieldTestingProfileConfigPojo.getFileSystemConfigs());
			return responseProfileTestingConfig;

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

		finally {

			try {

				client.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

	public String sendLogsToServer(List<RequestClientLogPojo> requestLogPojos) {

//
//		CloseableHttpClient client = HttpClients.createDefault();
//		try {
//
//			String logPushUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/logs/post";
//			HttpPost post = new HttpPost(logPushUrl);
//			RequestClientLogsPojo pushRequest = new RequestClientLogsPojo();
//			pushRequest.setRequestClientLogPojos(requestLogPojos);
//
//			Gson gson = new Gson();
//
//			StringEntity input = new StringEntity(gson.toJson(pushRequest));
//			input.setContentType("application/json");
//			post.setEntity(input);
//
//			// this.logger.info("Sending logs to server");
//
//			CloseableHttpResponse response = client.execute(post);
//
//			String responseString = EntityUtils.toString(response.getEntity());
//
////			  ServerResponseLogPojo serverResponse = (ServerResponseLogPojo) gson.fromJson(responseString, ServerResponseLogPojo.class);
//			String status = "OK";
//			return status;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		finally {
//			try {
//				client.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}

	public ResponseStressTestingConfig getStressTestingConfig(String iccid, String woId, String userName) {

		CloseableHttpClient client = HttpClients.createDefault();
		ResponseStressTestingConfig responseStressTestingConfig = new ResponseStressTestingConfig();

		try {
			String completeUrl = "http://localhost:8080/trakmeserver/api/external/fieldtest/profileconfig/get?usrId="
					+ userName + "&woId=" + woId;

			// this.logger.debug("Calling  Server : " + completeUrl);

			HttpGet get = new HttpGet(completeUrl);

			Gson gson = new Gson();
			CloseableHttpResponse response = client.execute(get);
			// String responseString =
			// "{\"apduList\":[\"RESET\",\"A0A40000023F00\",\"A0A40000027F20\",\"A0A40000026F7E\"],\"loopCount\":10000,\"startCounter\":1}";
			// this.System.out.println("Sever Response : " + responseString);
			String responseString = EntityUtils.toString(response.getEntity());
			ResponseFieldTestingProfileConfigPojo responseFieldTestingProfileConfigPojo = (ResponseFieldTestingProfileConfigPojo) gson
					.fromJson(responseString, ResponseFieldTestingProfileConfigPojo.class);
			responseStressTestingConfig.setApduList(responseFieldTestingProfileConfigPojo.getStressTestingApdus());
			responseStressTestingConfig.setLoopCount(responseFieldTestingProfileConfigPojo.getStressTestingLoopCount());
			responseStressTestingConfig.setStartCounter(1);
			
			return responseStressTestingConfig;

		} catch (Exception e) {

			e.printStackTrace();

			return null;

		}

		finally {

			try {

				client.close();

			} catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

}
