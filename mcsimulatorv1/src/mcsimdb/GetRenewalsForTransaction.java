package mcsimdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.McPatent;
import model.McTransaction;

public class GetRenewalsForTransaction {

	public List<McPatent> getRenewalsForTransaction(Long transactionId) {

    	System.out.println("in GetRenewalsForTransaction  getRenewalsForTransaction for id "+transactionId);
		List<McPatent>  result = new ArrayList<McPatent>();

		int qlimit = 50; //limit of renewals a txn can have
//
		int counter = 0;
//		String[] statii = new String[qlimit];
//		for (int i=0;i<qlimit;i++)
//			statii[i] = "empty";

		Long[] renewalsIds = new Long[qlimit];

		 try
		    {
		      // create our mysql database connection
			  DbCredentials db = new DbCredentials(); 
			  String myDriver = "com.mysql.jdbc.Driver";
		      String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
		      Class.forName(myDriver);
		      Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
		      
		      // our SQL SELECT query. 
		      String query = "SELECT renewals FROM payment_renewals WHERE payment = "+transactionId;

		      // create the java statement
		      Statement st = conn.createStatement();
		      
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      
		      // iterate through the java resultset
		      while (rs.next())
		      {
		    	Long renewalId = rs.getLong("renewals");
		    	renewalsIds[counter] = renewalId; 
		    	
		    	counter++;

//		    	McPatent combi = getMcPatent(renewalId);
		    	

		      }
				System.out.println("size is : "+counter);
		    	System.out.println("in GetRenewalsForTransaction  find Transaction "+transactionId+" has "+counter +" renewals: being:");
		    	for (int ii=0; ii<counter; ii++) {
		    		System.out.println("              "+renewalsIds[ii]);
		    	}

		      st.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }

		 
		    // Now that connection is closed, is safe to open another to get the required details
	    	for (int kk=0; kk<counter; kk++) {
	    		McPatent aMcPatent = getMcPatent(renewalsIds[kk]);
	    		result.add(aMcPatent);
	    	}
		 
		 
		return result;
	}

	
	public McPatent getMcPatent(Long renewalId) {
    	System.out.println("in GetRenewalsForTransaction  getMcPatent("+renewalId+")  invoked");
		McPatent combi = new McPatent();
		
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
		      String query = "SELECT * FROM renewal WHERE id = "+renewalId;

		      // create the java statement
		      Statement st = conn.createStatement();
		      
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query);
		      
		      // iterate through the java resultset
		      while (rs.next())
		      {
		    	  // is only 1
		    	combi.renewalId = rs.getLong("id");
		    	combi.patentId = rs.getLong("patent");
		    	combi.renewalStatus = "renewal_status"; 
		    	combi.epoPatentStatus = "NEED PATENT TO GET epoPatentStatus";
		    	combi.lastRenewedDateExEpo = new Date();  // avoif null maybe??  // "NEED PATENT TO GET lastRenewedDateExEpo";

		      }

		      st.close();
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }

		
		return combi;
	}
	
	
}
