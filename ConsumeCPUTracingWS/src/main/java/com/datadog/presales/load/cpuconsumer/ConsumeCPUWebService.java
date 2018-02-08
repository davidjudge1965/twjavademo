package com.datadog.presales.load.cpuconsumer;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.datadog.presales.load.TimeWaster;
import com.datadog.presales.load.TimeWasterUtility;

import datadog.trace.api.Trace;

import org.apache.log4j.Logger;

@Path("/consumecpu")
public class ConsumeCPUWebService {
	
	// Table and field information
	private String maxRandomStr="100";
	private String thresholdStr="2";
	private Integer maxRandom = new Integer(maxRandomStr);
	private Integer threshold = new Integer (thresholdStr);
	
	private Logger logger = Logger.getLogger(this.getClass());
	private TimeWasterUtility twu = null;
	
	public ConsumeCPUWebService() {
		
		logger.debug("ConsumeCPUWebService() Constructor called");
			
		String tmpStr = "";
		Properties prop = new Properties();
		try {
			logger.debug("ConsumeCPUWebService() Loading properties file");
			//load a properties file
			// 			prop.load(new FileInputStream("TimeWasterUtility.properties"));
			prop.load(this.getClass().getClassLoader().getResourceAsStream("ConsumeCPUWebService.properties"));
			logger.debug("ConsumeCPUWebService() Properties file has been loaded");
			//get the property value and print it out 
			tmpStr = prop.getProperty("maxRandom");
			if (tmpStr != null) {
				logger.debug("ConsumeCPUWebService() Properties file : maxRandom has string value [" + tmpStr + "]");
				maxRandom = new Integer (tmpStr);
			} else {
				logger.debug("ConsumeCPUWebService() Properties file : maxRandom Not found!");
			}
			tmpStr = prop.getProperty("threshold");
			if (tmpStr != null) {
				logger.debug("ConsumeCPUWebService() Properties file : threshold has string value [" + tmpStr + "]");
				threshold = new Integer (tmpStr);
			} else {
				logger.debug("ConsumeCPUWebService() Properties file : threshold Not found!");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("ConsumeCPUWebService() Exception while attempting to load or process the properties file.", ex);
		}
		
		try {
			twu = new TimeWasterUtility() ;
		} catch (ClassNotFoundException e) {
			logger.error("ConsumeCPUWebService() Exception while attempting to create a TimeWasterUtility object.", e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("ConsumeCPUWebService() Exception while attempting to create a TimeWasterUtility object.", e);
			e.printStackTrace();
		}
		
		logger.debug("maxRandom is ["+ maxRandom.toString() +"] and threshold is [" + threshold.toString() +"].");
	}

	@GET
	@Path("/hello/{name}")
	public Response sayHello(@PathParam("name") String name) {

		logger.debug("in getMsg");
		logger.debug("Processing name=[" + name + "]");
		String output = "<html> " + "<title>" + "Java WebService Example" + "</title>"  + "<body><h1><div style='font-size: larger;'>"
				+ "Hello <span style='text-transform: capitalize; color: green;'>" + name + "</span></div></h1></body>" + "</html>";
		return Response.status(200).entity(output).build();
	}
	
	@GET
	// @Produces(MediaType.TEXT_XML)
	// @Produces(MediaType.TEXT_HTML)
	@Path("/consumeit/{inputString}")
	// public String gwasteTime(@QueryParam("inputString")String inputString)
	@Trace
	public String wasteTime(@PathParam("inputString") String inputString)
	{
		//http://localhost:8080/ConsumeCPUWS/rest/consumecpu/consumeit/3
				logger.debug("in wasteTime of ConsumeCPUWebService");
			    //return calculateNumber(inputString);
				logger.debug("Calling new TimeWaster();");
				TimeWaster tw = new TimeWaster();
				logger.debug("Calling wasteDBTime just in case it's random time to wait...");
				String returnStr = wasteDBTime("DummyString");
				logger.debug("wasteDBTime has returned.");
				logger.debug("Calling calculateNumber()");
			    return "<?xml version=\"1.0\"?>" + "<hello> Time waster took (in ms): " + tw.calculateNumber(inputString)
						+ "</hello>";
	}
	@GET
	// @Produces(MediaType.TEXT_XML)
	// @Produces(MediaType.TEXT_HTML)
	@Path("/consumeDBTime/{inputString}")
	// public String gwasteTime(@QueryParam("inputString")String inputString)
	public String wasteDBTime(@PathParam("inputString") String inputString)
	{
		//http://localhost:8088/ConsumeCPUWS/rest/consumecpu/consumeit/3
				logger.debug("in wasteDBTime of ConsumeCPUWebService");
				logger.debug("maxRandom is ["+ maxRandom.toString() +"] and threshold is [" + threshold.toString() +"].");
				String returnStr = "Did not call the database with a pause";
			    //return calculateNumber(inputString);
				Integer randomNumber = (int) (Math.random() * maxRandom);
				logger.debug("Random number is ["+ randomNumber.toString() +"].");
				if ( randomNumber < threshold ) {
					logger.debug("Calling twu.wasteDBTime() as random number was below the threshold.");
					returnStr = "Utilities' DB Time waster took (in ms): " + twu.wasteDBTime(new Integer(4));
					logger.debug("twu.wasteDBTime() returned ["+returnStr +"[");
				} else {
					logger.debug("Carrying on as random number was above the threshold.");
				}
			    return returnStr;
	}


}
