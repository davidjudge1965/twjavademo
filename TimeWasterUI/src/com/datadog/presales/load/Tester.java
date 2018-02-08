package com.datadog.presales.load;

import java.io.IOException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Tester {
	private Logger logger = Logger.getLogger(this.getClass());
	private String webTargetPath="rest/consumecpu/consumeit/2";
	// http://twws:8080/ConsumeCPUWS/rest/consumecpu/consumeit/2
	private String webServiceURI="http://192.168.56.102:8080/ConsumeCPUWS/";
	private String requestType = "OKhttp";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tester tester = new Tester();
		tester.doTest();
	}

	public void doTest() {
		OkHttpClient client = new OkHttpClient();
		String resultString = "empty result string";
		logger.info("calculateNumberOKhttp() Called.");
		String urlToRetrieve = webServiceURI + "/" + webTargetPath;
		logger.debug("Setting up the URL to retrieve: [" + urlToRetrieve + "].");
		
		logger.debug("Creating the request");
		Request request = new Request.Builder()
		      .url(urlToRetrieve)
		      .build();
		try {
			logger.debug("Calling client.newCall(request).execute()");
			Response response = client.newCall(request).execute();
			logger.debug("Setting resultString");
			resultString =  response.body().string();
			response.body().close();
		} catch (IOException e) {
			logger.debug( "Caught exception" + e.toString() );
			e.printStackTrace();
		}
		logger.debug("After the try-catch; Finished and the resultString is [" + resultString +"]."); 
	}
	
}
