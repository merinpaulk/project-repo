package mcsimdb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import lang.Universal;
import model.McOrders;
import model.McPatent;

public class PopulateOrdersData extends Universal{
	
	protected String PREFIX = this.getClass().getName() + " : "; 
	
	public McOrders getOrdersData(Long paymentId){
		
		String msg = PREFIX + " getOrdersData(" + paymentId +")";
		log().debug(msg + " invoked ::: ");
		
		McOrders orderData = new McOrders();
		
		
		try
	    {
	      // create our mysql database connection
		  DbCredentials db = new DbCredentials(); 
		  String myDriver = "com.mysql.jdbc.Driver";
	      String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
	      Class.forName(myDriver);
	      Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
	      
	      String query = "SELECT * FROM payment where id = '" + paymentId + "'";
	      
	      Statement st = conn.createStatement();
	      
	      ResultSet rs = st.executeQuery(query);
	      
	      while(rs.next()){
	    	  
	    	  //BELOW FROM PAYMENT TABLE
	    	  orderData.p3sRef = rs.getString("p3s_trans_ref"); 
	    	  orderData.transStartDate = rs.getDate("trans_start_Date");
	    	  orderData.transTargetEndDate = rs.getDate("trans_target_end_date");
	    	  orderData.USDAmount = rs.getBigDecimal("trans_amount_usd");
	    	  
	    	  //GetRenewalsForTransaction renwls = new GetRenewalsForTransaction();
	    	  //orderData.patents = renwls.getRenewalsForTransaction(paymentId);
	    	  
	    	  //get the current FXRATE from GLOBAL_VARIABLE_SOLE DB
	    	  orderData.destination = "EPO";
	      }
	      
	      //NOW GET CURRENT RATE
	      String rate_query = "SELECT * from GLOBAL_VARIABLE_SOLE";
		     
	      ResultSet rs_rate = st.executeQuery(rate_query);
	      	
	      BigDecimal currentRate = new BigDecimal(0.0);
	      while (rs_rate.next())
	      {
	    	  currentRate = rs.getBigDecimal("current_rate");
		  }
	      
	      orderData.UsdToEurRate = currentRate;
	      orderData.EURAmount = orderData.USDAmount.divide(currentRate);
	      
	      //NOW HARDCODING THE SPEED PROPERTY
	      orderData.speed = "SEPA";
	      	
	    }
		
		catch(Exception e){
			e.printStackTrace();
			log().error("Exception inside " + msg + " ::: " + e.getMessage());
		}
		
		return orderData;
		
	}

}
