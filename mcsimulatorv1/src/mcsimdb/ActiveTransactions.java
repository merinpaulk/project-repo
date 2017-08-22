package mcsimdb;

import java.util.ArrayList;
import java.util.List;

import lang.Universal;
//import dbFunctions.dbQuery;
import model.McTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class ActiveTransactions extends Universal{
	
	protected String PREFIX = this.getClass().getName() + " : "; 

	public List<McTransaction> getActiveTransactions() {

    	System.out.println("in mcsimdbActiveTransactions  getActiveTransactions");
		List<McTransaction>  result = new ArrayList<McTransaction>();

		int qlimit = 2; //limit of questions db can handle

		int counter = 0;
		String[] statii = new String[qlimit];
		for (int i=0;i<qlimit;i++)
			statii[i] = "empty";
		
    	System.out.println("in mcsimdbActiveTransactions  getActiveTransactions  A ");

    	
    	
		 try
		    {
		      // create our mysql database connection
			  DbCredentials db = new DbCredentials(); 
			  String myDriver = "com.mysql.jdbc.Driver";
		      String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
		      Class.forName(myDriver);
		      Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
		      
		      // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String query = "SELECT * FROM payment";

		      // create the java statement
		      Statement st = conn.createStatement();
		      
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      
		      // iterate through the java resultset
		      while (rs.next())
		      {
		    	counter++;

		    	McTransaction txn = new McTransaction();
		    	txn.transactionId = rs.getLong("id");
		    	txn.P3S_TransRef = rs.getString("p3s_trans_ref"); 
		    	txn.lastUpdatedDate = rs.getDate("last_updated_date");
		    	txn.latestTransStatus = rs.getString("latest_trans_status");
		    	txn.transStartDate = rs.getDate("trans_start_Date");
		    	txn.transTargetEndDate = rs.getDate("trans_target_end_date");
				txn.transAmount_USD = rs.getBigDecimal("trans_amount_usd");
// now patents
				GetRenewalsForTransaction renwls = new GetRenewalsForTransaction();
		    	txn.patents = renwls.getRenewalsForTransaction(txn.transactionId);
		    	txn.numPatents = txn.patents.size(); 
		    			
		    	result.add(txn);
				System.out.println("latest_trans_status is : "+txn.latestTransStatus );
				System.out.println("latest_trans_status is : "+txn.P3S_TransRef  );

		      }
				System.out.println("size is : "+counter);

		      st.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }

		return result;
	}
	
	
	public McTransaction getTransaction(String refId) {
		
		McTransaction txn = new McTransaction();
		
		
		log().debug(PREFIX + " getTransaction(" + refId + ") invoked");
		
		try
	    {
	      // create our mysql database connection
		  DbCredentials db = new DbCredentials(); 
		  String myDriver = "com.mysql.jdbc.Driver";
	      String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
	      Class.forName(myDriver);
	      Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
	      
	      // our SQL SELECT query. 
	      // if you only need a few columns, specify them by name instead of using "*"
	      String query = "SELECT * FROM payment where p3s_trans_ref = '" + refId + "' AND NOT (latest_trans_status = 'Completed' or 'Failed') ";

	      // create the java statement
	      Statement st = conn.createStatement();
	      
	      // execute the query, and get a java resultset
	      ResultSet rs = st.executeQuery(query);
	      
	      if(rs.next()){
	    	  log().debug("getTransaction(" + refId + ") returned payment details with id as " + rs.getLong("id") );
	    	  txn = new McTransaction();
			  txn.transactionId = rs.getLong("id");
			  txn.P3S_TransRef = rs.getString("p3s_trans_ref"); 
			  txn.lastUpdatedDate = rs.getDate("last_updated_date");
			  txn.latestTransStatus = rs.getString("latest_trans_status");
			  txn.transStartDate = rs.getDate("trans_start_Date");
			  txn.transTargetEndDate = rs.getDate("trans_target_end_date");
			  txn.transAmount_USD = rs.getBigDecimal("trans_amount_usd");
		
			  GetRenewalsForTransaction renwls = new GetRenewalsForTransaction();
			  txn.patents = renwls.getRenewalsForTransaction(txn.transactionId);
			  txn.numPatents = txn.patents.size(); 
	      }		
	      else
	    	  txn = null;
	    	
	      st.close();
	    }
	    catch (Exception e)
	    {
	      System.err.println("Got an exception! ");
	      System.err.println(e.getMessage());
	      e.printStackTrace();
	    }
		
		return txn;
	}
	
}
