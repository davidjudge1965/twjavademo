/**
 * 
 */
package com.datadog.presales.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Properties;

import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
//import org.apache.http.client.fluent.Request;

import datadog.trace.api.Trace;
import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;



/**
 * @author djudge
 *
 */
public class TimeWasterUI {
	
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	// private static final String webServiceURI = "http://localhost:8080/JavaWebServicesExample/";
	
	// Table and field information
	private String webTargetPath="rest/hello/wastetime";
	private String webServiceURI="http://localhost:8080/JavaWebServicesExample/";
	private String requestType = "OKhttp";
	
	public TimeWasterUI() {
		logger.info("TimeWasterUI constructor called.");
		String tmpStr = "";
		Properties prop = new Properties();
		try {
			logger.debug("Loading properties file");
			//load a properties file
			// 			prop.load(new FileInputStream("TimeWasterUtility.properties"));
			prop.load(this.getClass().getClassLoader().getResourceAsStream("TimeWasterUI.properties"));
			logger.debug("Properties file has been loaded");
			//get the property value and print it out 
			tmpStr = prop.getProperty("webTargetPath");
			if (tmpStr != null) webTargetPath = tmpStr;
			tmpStr = prop.getProperty("webServiceURI");
			if (tmpStr != null) webServiceURI = tmpStr;
			tmpStr = prop.getProperty("requestType");
			if (tmpStr != null) requestType = tmpStr;
			
			logger.debug("Properties: webTargetPath has value: " + webTargetPath );
			logger.debug("Properties: webServiceURI has value: " + webServiceURI );
			logger.debug("Properties: requestType   has value: " + requestType );

		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("Exception while attempting to load or process the properties file.", ex);
		}
	}
	
	public String calculateNumber (String inputString) {
		logger.info("calculateNumber() Called with [" + inputString + "].");
		String resultString = "resultString Initialisation Value.";
		
		if ( requestType.equalsIgnoreCase("OKhttp") ) {
			logger.debug("Calling calculateNumberOKhttp");
			resultString = calculateNumberOKhttp (inputString);
		} else if ( requestType.equalsIgnoreCase("OKhttpTracing") ) {
			logger.debug("Calling calculateNumberOKhttpTracing");
			resultString = calculateNumberOKhttpWithTracing (inputString);
		}else if ( requestType.equalsIgnoreCase("ApacheHttp") ) {
			logger.debug("Calling calculateNumberClassic");
			resultString = calculateNumberClassic (inputString);
		} else {
			logger.debug("Calling calculateNumberClassic");
			resultString = calculateNumberClassic (inputString);
		}
		logger.debug("After the try-catch; Finished and the resultString is [" + resultString +"]."); 
		return resultString;
	}
	
	
	public String calculateNumberOKhttp (String inputString) {
		String resultString = "resultString Initialisation Value.";
		OkHttpClient client = new OkHttpClient.Builder()
		        .connectTimeout(30, TimeUnit.SECONDS)
		        .writeTimeout(30, TimeUnit.SECONDS)
		        .readTimeout(30, TimeUnit.SECONDS)
		        .build();

		logger.info("calculateNumberOKhttp() Called with [" + inputString + "].");
		String urlToRetrieve = webServiceURI + "/" + webTargetPath + "/" + inputString;
		logger.debug("Setting up the URL to retrieve: [" + urlToRetrieve + "].");
		
		logger.debug("Creating the request");
		Request request = new Request.Builder()
		      .url(urlToRetrieve)
		      .build();
		try {
			logger.debug("Calling client.newCall(request).execute()");
			Response response = client
				      .newCall(request).execute();
			logger.debug("Setting resultString");
			resultString =  response.body().string();
			response.body().close();
		} catch (IOException e) {
			logger.debug( "Caught exception" + e.toString() );
			e.printStackTrace();
		}
		logger.debug("After the try-catch; Finished and the resultString is [" + resultString +"]."); 
		return resultString;
	}	
	
	@Trace
	public String calculateNumberOKhttpWithTracing (String inputString) {
		String resultString = "resultString Initialisation Value.";
		OkHttpClient client = new OkHttpClient.Builder()
		        .connectTimeout(30, TimeUnit.SECONDS)
		        .writeTimeout(30, TimeUnit.SECONDS)
		        .readTimeout(30, TimeUnit.SECONDS)
		        .build();

		logger.info("calculateNumberOKhttp() Called with [" + inputString + "].");
		String urlToRetrieve = webServiceURI + "/" + webTargetPath + "/" + inputString;
		logger.debug("Setting up the URL to retrieve: [" + urlToRetrieve + "].");
		
		logger.debug("Creating the request");
		Request request = new Request.Builder()
		      .url(urlToRetrieve)
		      .build();
		try {
			logger.debug("Calling client.newCall(request).execute()");
			Response response = client
				      .newCall(request).execute();
			logger.debug("Setting resultString");
			resultString =  response.body().string();
			response.body().close();
		} catch (IOException e) {
			logger.debug( "Caught exception" + e.toString() );
			e.printStackTrace();
		}
		logger.debug("After the try-catch; Finished and the resultString is [" + resultString +"]."); 
		return resultString;
	}	
	
	public String calculateNumberApacheHttp (String inputString) {
		logger.info("calculateNumberApacheHttp() Called with [" + inputString + "].");
		String resultString = "resultString Initialisation Value.";

		String urlToRetrieve = webServiceURI + "/" + webTargetPath + "/" + inputString;
		logger.debug("Setting up the URL to retrieve: [" + urlToRetrieve + "].");

		logger.debug("Creating the request");

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(urlToRetrieve);

		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try (InputStream stream = entity.getContent()) {
					BufferedReader reader =
							new BufferedReader(new InputStreamReader(stream));

					resultString = reader.readLine();

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.debug("After the try-catch; Finished and the resultString is [" + resultString +"]."); 

		return resultString;
	}	
	
	
	@Trace
	public String calculateNumberClassic (String inputString) {
		logger.info("calculateNumberClassic() Called with [" + inputString + "].");
		
		String resultString = "Invalid number passed in.";
		Integer duration = null;
		
		logger.debug("Setting up the client objects");
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		
		// Client client = ClientBuilder.newClient();
		URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
		WebTarget webTarget = client.target(serviceURI);
		Tracer tracer = GlobalTracer.get();
		
		logger.debug("Start of try-catch");
		try (Scope scope = tracer.buildSpan("WScall.to.ConsumeCPU").startActive(true)) {
			duration = new Integer(inputString);
			logger.info("calculateNumberClassic() Calling consumeCPU() with [" + duration.toString() + "].");
			// resultString = consumeCPU(duration).toString();
			logger.debug("Making the call to the web service [" + webServiceURI + "/" + webTargetPath + "]");
			// resultString = webTarget.path("rest").path("wastetime").path("wastetime").path(inputString).request()
			resultString = webTarget.path(webTargetPath).path(inputString).request()
					.accept(MediaType.TEXT_XML).get(String.class);
		} finally {}
		logger.debug("After the try-catch; Finished and the resultString is [" + resultString +"]."); 
		
		return resultString;
		}
	

	public String calculateNumberClassicFullTracing (String inputString) {
		logger.info("calculateNumberClassicFullTracing() Called with [" + inputString + "].");
		
		String resultString = "Invalid number passed in.";
		Integer duration = null;
		
		logger.debug("Setting up the client objects");
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		
		// Client client = ClientBuilder.newClient();
		URI serviceURI = UriBuilder.fromUri(webServiceURI).build();
		WebTarget webTarget = client.target(serviceURI);
		
		logger.debug("Start of try-catch");
		try {
			duration = new Integer(inputString);
			logger.info("calculateNumberClassicFullTracing() Calling consumeCPU for [" + duration.toString() + "].");
			// resultString = consumeCPU(duration).toString();
			logger.debug("Invoking [" + webServiceURI + "/" + webTargetPath + "]");
			// resultString = webTarget.path("rest").path("wastetime").path("wastetime").path(inputString).request()
			resultString = webTarget.path(webTargetPath).path(inputString).request()
					.accept(MediaType.TEXT_XML).get(String.class);
		} finally {}
		logger.debug("Finished and the resultString is [" + resultString +"]."); 
		
		return resultString;
		}
	

	public static void main(String[] args) {
		TimeWasterUI twui  = new TimeWasterUI();
		System.out.println("main(): Result is [" + twui.calculateNumber("2") + "].");
	}

}
