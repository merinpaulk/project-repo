package formHandlers;

import enums.PaymentStatusEnum;
import mcsimdb.ActiveTransactions;
import mcsimdb.ChangeTransactionStatusOnly;
import mcsimdb.EndTheTransaction;

// import dbFunctions.dbUpdate;


public class P3sTransaction
{
	boolean validate = false;
    
    public void runSave(String txnid, String newstatus)
    {	
    	//System.out.println("in P3sTransaction.java runsave : txnid is "+txnid+"   NewStatus="+newstatus);
    	if (true) // NPE protection
    	{
	    	try
	    	{
	    		PaymentStatusEnum txnStatusEnum = new PaymentStatusEnum(newstatus);  
		    	System.out.println("P3sTransaction.java runSave given txnid of "+txnid+"  and new status of "+txnStatusEnum.toString());

		    	// is Transaction being ended by this change
		    	boolean transactionIsBeingEnded = false;
		    	if ( "Completed".equals(txnStatusEnum.toString()) || "Failed".equals(txnStatusEnum.toString())) {
		    		transactionIsBeingEnded = true;
		    	}

		    	if (transactionIsBeingEnded) {
		    		EndTheTransaction db = new EndTheTransaction();
			    	validate = db.endTheTransaction(txnid, txnStatusEnum);
		    		
		    	} else {
			    	ChangeTransactionStatusOnly db = new ChangeTransactionStatusOnly();
			    	validate = db.changeTransactionStatus(txnid, txnStatusEnum);
		    	}
		    		
		    	
		    	
		    	
		    	
		    	
		    	
	    		
	    	}
	    	catch (Exception e)
	    	{System.out.println("Whoops, error -"+e);}
    	}

    	validate = true;
    }

    
    public void populate()
    {	
	    	try
	    	{
	    		ActiveTransactions db1 = new ActiveTransactions();
	    		System.out.println("mcsimmain.jsp top");
	    		db1.getActiveTransactions();
	    		

				validate = true;
	    	}
	    	catch (Exception e)
	    	{
	    		System.out.println("Whoops, P3sTransaction.java  populate error -"+e);
	    	}

    	System.out.println("completed P3sTransaction.java populate");
    }

    	
    public void empty ()
    {
//     	harry = "";
    	 validate = false;
    }

	//    public void setHarry( String value ) { 
	//    	System.out.println("in P3sTransaction.java harry SETTER invoked with "+value);
	//    	harry = value; 
	//    }
//  gette rnot needed ????????????  ac
// even setter not needed if pass in params!
    
    public boolean validate ()
    {
    	return validate;
    }
}
