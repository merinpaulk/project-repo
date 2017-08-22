package mcsimdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import enums.PaymentStatusEnum;
import enums.RenewalStatusEnum;
import model.McPatent;

/**
 * Transaction gets updated to Completed or Failed
 *  Which means is ceases to be an Active transaction
 *  Consequently the status of patent & renewal must change
 */
public class EndTheTransaction extends ChangeTransactionStatusOnly{

	public boolean endTheTransaction(String txnid, PaymentStatusEnum newstatus) {

		boolean isOk = false;
    	System.out.println("in EndTheTransaction endTheTransaction "+txnid+" : "+newstatus.toString());

		 try
		    {
			  Long txnIdLong = new Long(txnid);
			  String txnIdSafeStr = txnIdLong.toString(); 

			  if ( ( ! "Completed".equals(newstatus.toString())) && ( ! "Failed".equals(newstatus.toString())) )
				  	throw new RuntimeException("EndTheTransaction endTheTransaction  given invalid status of "+newstatus);
			  
			  isOk = changeTransactionStatus(txnid, newstatus);
			  if ( ! isOk) throw new RuntimeException("EndTheTransaction changeTransactionStatus reported problem! ABORT midway!!!");
			 
			  // update all renewals for this transaction
				GetRenewalsForTransaction renwls = new GetRenewalsForTransaction();
				List<McPatent> mcRenewals = renwls.getRenewalsForTransaction(txnIdLong);

				// Get status value for patent & renewal
				RenewalStatusEnum status = new RenewalStatusEnum(RenewalStatusEnum.RENEWAL_IN_PLACE);
				if ( "Failed".equals(newstatus.toString()) ) {
					status = new RenewalStatusEnum(RenewalStatusEnum.INVALID__NEEDS_SETTING);
				}
				
				for (McPatent combi : mcRenewals) {
					updateRenewal(combi.renewalId.toString(), status);
					updatePatent(combi.patentId, status);
				}
			  
		    
		    
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception! ");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }

		return isOk;
	}


	protected void updateRenewal(String renewalid, RenewalStatusEnum newstatus) throws Exception {
	
	    // create our mysql database connection
		  DbCredentials db = new DbCredentials(); 
		  String myDriver = "com.mysql.jdbc.Driver";
	    String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
	    
	    // our SQL SELECT query. 
	    // if you only need a few columns, specify them by name instead of using "*"
	    String cmd = "UPDATE renewal SET renewal_status = '"+newstatus.toString()+"'  WHERE id = "+renewalid ;
	    System.out.println("EndTheTransaction  updateRenewal("+renewalid+", "+newstatus+") created cmd of : "+cmd);
	
	    // create the java statement
	    Statement st = conn.createStatement();
	    
	    // execute the cmd
	    st.execute(cmd);
	
	    st.close();
	}
	protected void updatePatent(Long patentid, RenewalStatusEnum newstatus) throws Exception {
		
		String epoSays = "EPO says is renewed...";
		if ((RenewalStatusEnum.INVALID__NEEDS_SETTING.toString()).equalsIgnoreCase(newstatus.toString())) {
			epoSays = "Needs update - summat gone wrong our end";
		}
		
		
	    // create our mysql database connection
		DbCredentials db = new DbCredentials(); 
		String myDriver = "com.mysql.jdbc.Driver";
	    String myUrl = "jdbc:mysql://"+db.dbhost+":3306/"+db.db;
	    Class.forName(myDriver);
	    Connection conn = DriverManager.getConnection(myUrl, db.un, db.pwd);
	    
	    // our SQL SELECT query. 
	    // if you only need a few columns, specify them by name instead of using "*"
	    String strDate = "06/08/2017 00:00:00";
	    String cmd = "UPDATE patent SET renewal_status = '"+newstatus.toString()
	    	+"', epo_patent_status='"+epoSays+"'"
	    	+", last_renewed_date_ex_epo= CURDATE()  WHERE id = "+patentid ;
	    System.out.println("EndTheTransaction  updatePatent("+patentid+", "+newstatus+") created cmd of : "+cmd);
	
	    // create the java statement
	    Statement st = conn.createStatement();
	    
	    // execute the cmd
	    st.execute(cmd);
	
	    st.close();
	
	}
	
	
	
	
}
