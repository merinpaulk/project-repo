package mcsimdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import enums.PaymentStatusEnum;
import model.McTransaction;

/**
 * Transaction gets updated - but Transaction NOT completed
 *  so no effect on renewal & patent
 *  @return true is all ok
 */
public class ChangeTransactionStatusOnly {

	public boolean changeTransactionStatus(String txnid, PaymentStatusEnum newstatus) {

		boolean isOk = false;
    	System.out.println("in ChangeTransactionStatusOnly  changeTransactionStatus "+txnid+" : "+newstatus.toString());
    	
		 try
		    {
			  Long txnIdLong = new Long(txnid);
			  String txnIdSafeStr = txnIdLong.toString(); 
			 
			 
		      // create our mysql database connection
			  DbCredentials db = new DbCredentials(); 
			  String myDriver = "com.mysql.jdbc.Driver";
		      String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
		      Class.forName(myDriver);
		      Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
		      
		      // our SQL SELECT query. 
		      // if you only need a few columns, specify them by name instead of using "*"
		      String cmd = "UPDATE payment SET latest_trans_status = '"+newstatus.toString()+"'  WHERE id = "+txnIdSafeStr ;
		      System.out.println("ChangeTransactionStatusOnly  changeTransactionStatus created cmd of : "+cmd);

		      // create the java statement
		      Statement st = conn.createStatement();
		      
		      // execute the cmd
		      st.execute(cmd);

		      st.close();

		      isOk = true;
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }

		return isOk;
	}

	
}
