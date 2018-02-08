/**
 * 
 */
package com.datadog.presales.load;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;

import datadog.trace.api.Trace;


/**
 * @author djudge
 *
 */
public class TimeWasterUtility {
	
	private Logger logger = Logger.getLogger(this.getClass());
	private String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";


	// JDBC driver name and database URL
	// static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private String databaseURI = "jdbc:oracle:thin:foglight/foglight@twdb:1521:xe";
	// jdbc:oracle:thin:@localhost:1521:xe
	
	private String databaseDriver = "oracle.jdbc.driver.OracleDriver";
	
	//  Database credentials
	private String dbuser = "foglight";
	private String dbpassword = "foglight";
	
	// Table and field information
	private String tablename="consumecpu";
	private String fieldname="result";
	
	

	/**
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	
	public TimeWasterUtility() throws IOException, ClassNotFoundException {
		logger.debug("Constructor called");
		
		String tmpStr = "";
		Properties prop = new Properties();
		try {
			logger.debug("Loading properties file");
			//load a properties file
			// 			prop.load(new FileInputStream("TimeWasterUtility.properties"));
			prop.load(this.getClass().getClassLoader().getResourceAsStream("TimeWasterUtility.properties"));
			logger.debug("Properties file has been loaded");
			//get the property value and print it out 
			tmpStr = prop.getProperty("databaseURI");
			if (tmpStr != null) databaseURI = tmpStr;
			tmpStr = prop.getProperty("databaseDriver");
			if (tmpStr != null) databaseDriver = tmpStr;
			tmpStr = prop.getProperty("tablename");
			if (tmpStr != null) tablename = tmpStr;
			tmpStr = prop.getProperty("fieldname");
			if (tmpStr != null) fieldname = tmpStr;
			tmpStr = prop.getProperty("dbuser");
			if (tmpStr != null) dbuser = tmpStr;
			tmpStr = prop.getProperty("dbpassword");
			if (tmpStr != null) dbpassword = tmpStr;
			
			logger.debug("databaseURI has value: " + databaseURI );
			logger.debug("databaseDriver has value: " + databaseDriver );
			logger.debug("tablename has value: " +  tablename );
			logger.debug("fieldname has value: " +  fieldname );
			logger.debug("dbuser has value: " + dbuser );
			logger.debug("dbpassword has value: " + dbpassword);

		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("Exception while attempting to load or process the properties file.", ex);
		}
		
		logger.debug("Constructor calling Call.forName with [" + databaseDriver + "]." );
		Class.forName(databaseDriver);
		logger.debug("Constructor processing complete." );
	}
	

	/**
	 * @param args
	 */
	@Trace
	public void persistData(String theString) {
		logger.info("persistData() called with [" + theString + "].");
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(databaseDriver);
			// Class.forName(databaseDriver);

			//STEP 3: Open a connection
			logger.debug("persistData(): Connecting to a selected database... with [" + databaseURI +"].");
			conn = DriverManager.getConnection(databaseURI);
			logger.debug("persistData(): Connected database successfully...");

			//STEP 4: Execute a query
			logger.debug("persistData(): Calling conn.createStatement()");
			stmt = conn.createStatement();

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
			
			String sql = "INSERT INTO test.consumecpu " +
					"VALUES ('" + sdf.format(cal.getTime()) + " - " + theString + "')";
			logger.debug("persistData(): About to execute [" + sql + "].");
			stmt.executeUpdate(sql);
			sql = "select count(*) from test.consumecpu, pg_sleep(0.1)" ;
			logger.debug("persistData(): is a bit quick so we're slowing things down here with a 100ms pause.");
			ResultSet rs = stmt.executeQuery(sql);
			logger.info("persistData(): Inserted record into the table...");

		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
			logger.error("persistData(): Exception [" + se.getClass().toString() + "] occurred.", se);
		}catch(Exception e){
			//Handle errors for Class.forName
			logger.error("persistData(): Exception [" + e.getClass().toString() + "] occurred.", e);
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					conn.close();
			}catch(SQLException se){
				logger.error("persistData(): Exception [" + se.getClass().toString() + "] occurred while closing the connection when stmt not null.", se);
			}// do nothing
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				logger.error("persistData(): Exception [" + se.getClass().toString() + "] occurred while closing the connection when conn not null.", se);
				se.printStackTrace();
			}//end finally try
		}//end try
		logger.info("persistData(): finished processing [" + theString + "].");
	}
		
	@Trace	
	public String wasteDBTime(Integer seconds) {
			logger.info("twu.wasteDBTime() called with [" + seconds + "].");
			Connection conn = null;
			Statement stmt = null;
			try{
				Class.forName(databaseDriver);
				logger.debug("twu.wasteDBTime(): Connecting to a selected database... with [" + databaseURI +"].");
				conn = DriverManager.getConnection(databaseURI);
				logger.debug("twu.wasteDBTime(): Connected database successfully...");
				logger.debug("twu.wasteDBTime(): Calling conn.createStatement()");
				stmt = conn.createStatement();
				
				String sql = "select count(*) from test.consumecpu, pg_sleep(10)" ;
				logger.debug("twu.wasteDBTime(): About to execute [" + sql + "].");
				
				ResultSet rs = stmt.executeQuery(sql);
				logger.info("twu.wasteDBTime(): Inserted record into the table...");

			}catch(SQLException se){
				//Handle errors for JDBC
				se.printStackTrace();
				logger.error("twu.wasteDBTime(): Exception [" + se.getClass().toString() + "] occurred.", se);
			}catch(Exception e){
				//Handle errors for Class.forName
				logger.error("twu.wasteDBTime(): Exception [" + e.getClass().toString() + "] occurred.", e);
				e.printStackTrace();
			}finally{
				//finally block used to close resources
				try{
					if(stmt!=null)
						conn.close();
				}catch(SQLException se){
					logger.error("twu.wasteDBTime(): Exception [" + se.getClass().toString() + "] occurred while closing the connection when stmt not null.", se);
				}// do nothing
				try{
					if(conn!=null)
						conn.close();
				}catch(SQLException se){
					logger.error("twu.wasteDBTime(): Exception [" + se.getClass().toString() + "] occurred while closing the connection when conn not null.", se);
					se.printStackTrace();
				}//end finally try
			}//end try
			logger.info("twu.wasteDBTime(): finished processing [" + seconds + "].");
			return "Done";

	}//end main


}
