package mcsimdb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import lang.Universal;

public class UpdateTodaysRate extends Universal{
	
	protected String PREFIX = this.getClass().getName() + " : "; 
	
	public boolean updateRates(BigDecimal rate) throws SQLException{
		
		log().debug( PREFIX + "updateTodaysRate(" +rate+ ") invoked :::");
		
		boolean isRateUpdated = false;
		
		try {
			
			DbCredentials db = new DbCredentials(); 
			String myDriver = "com.mysql.jdbc.Driver";
			String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
	      	Class.forName(myDriver);
	      	Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
	      	
		    Statement st = conn.createStatement();
	      	String query = "SELECT * from GLOBAL_VARIABLE_SOLE";
	     
	      	ResultSet rs = st.executeQuery(query);
	      	
	      	BigDecimal oldRate = new BigDecimal(0.0);
	      	Date date1 = new Date();
	      	while (rs.next())
	      	{
	      		oldRate = rs.getBigDecimal("current_rate");
	      		date1  = rs.getTimestamp("current_rate_active_date");
	      		
		    }
	      	
	      	//String cmd1 = "INSERT INTO ARCHIVED_RATE  VALUES(" + oldRate + "," + date1 +")";
	      	String cmd1 = "INSERT INTO ARCHIVED_RATE (active_from_date,fx_rate) VALUES('" + date1 + "'," + oldRate + ")";
	      	st.execute(cmd1);
	      	
		    String cmd2 = "UPDATE GLOBAL_VARIABLE_SOLE SET current_rate = '" + rate + "' , current_rate_active_date = CURRENT_TIMESTAMP()"  ;
		    System.out.println("Updated todays rate as  : "+rate);

		    
		     // execute the cmd
		     st.execute(cmd2);
		     st.close();
		     isRateUpdated = true;
		      
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
	      
		return isRateUpdated;
	}

}
