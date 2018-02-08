/**
 * 
 */
package com.datadog.presales.load;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import datadog.trace.api.Trace;

/**
 * @author djudge
 *
 */
// @Path("/wastetimeold")
public class TimeWaster {
	
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(this.getClass());
	
	public TimeWaster() {
		logger.info("Constructor called.");
	}
	
	@Trace
	public String calculateNumber (String inputString) {
		logger.info("calculateNumber() Called with [" + inputString + "].");
		TimeWasterUtility twu = null;

		String resultString = "Invalid number passed in.";
		Integer duration = null;
		
		try {
			duration = new Integer(inputString);
			logger.info("calculateNumber() Calling consumeCPU() with [" + duration.toString() + "].");
			resultString = consumeCPU(duration).toString();
		} finally {}
		
		try {
			logger.info("calculateNumber() instantiating TimeWasterUtility - Calling new TimeWasterUtility().");
			twu = new TimeWasterUtility();
			logger.info("calculateNumber() Calling persistData() with [" + resultString + "].");
			twu.persistData(resultString);
		} catch (IOException e) {
			logger.error("calculateNumber() Caught exception [" + e.toString() + "].", e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) { 
			logger.error("calculateNumber() Caught exception [" + e.toString() + "].", e);
			e.printStackTrace();
		}		
				
		logger.info("calculateNumber() finished and returning [" + resultString + "].");
		return resultString;
	}
	
@Trace
	private Integer consumeCPU ( Integer duration ) {
		logger.info("consumeCPU called with [" + duration + "].");
		// System.out.println("consumeCPU called with [" + duration + "].");
		Integer theValue = new Integer("1000");
		long startTime = System.currentTimeMillis();
		long estimatedTime = 0L;
		long durationMiliseconds = duration.longValue() * 1000;
		int count = 0;
		while ( estimatedTime < durationMiliseconds ) {
			logger.debug("In the loop... iteration [" + count++ + "].");
			// System.out.print(".");
			estimatedTime = System.currentTimeMillis() - startTime;
			for (int i=0;i<10000000;i++) {
				theValue = theValue + i;
				theValue = theValue + i;
				theValue = theValue + i;
				theValue = theValue - i;
				theValue = theValue - i;
				theValue = theValue - i;
				theValue = theValue + i;
				theValue = theValue + i;
				theValue = theValue - i;
				theValue = theValue - i;
			}
			theValue = theValue / 10;
		}
		logger.info("consumeCPU done.  estimatedTime is  [" + estimatedTime + "].");
		return new Integer(new Integer((int)estimatedTime));
	}
	
	public static void main(String[] args) {
		TimeWaster tw  = new TimeWaster();
		System.out.println("main(): Result is [" + tw.calculateNumber("2") + "].");
	}

}
