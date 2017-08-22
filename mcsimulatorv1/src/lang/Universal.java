package lang;




import log.BcsLogger;
import log.Loggable;

/**
 * Many (all?) classes will extend this. Provides:
 * - easy-to-use logging capability
 * - DIY java language extension
 *  
 *  Dependencies:
 *   Logging 
 *   
 **/
public class Universal extends BcsLogger implements Loggable {

	
	public boolean isEmpty(String val) {
		if (val==null || val.trim().length()==0) return true;
		return false;
	}
	public boolean notEmpty(String val) {
		return ! isEmpty(val);
	}

    public void fail(String message) {
    	logInternalError().fatal(message);
    	throw new P3SRuntimeException(message);
    }

    
    public void logAttention(String msg) {
    	log().fatal("                                                               ");
    	log().fatal("                                                               ");
    	log().fatal("  ***************************************************************************");
    	log().fatal("  ***************************************************************************");
    	log().fatal("  ****  "+msg+"  ****");
    	log().fatal("  ***************************************************************************");
    	log().fatal("  ***************************************************************************");
    	log().fatal("                                                               ");
    	log().fatal("                                                               ");
    }

    // Temporary - for Development:
    public void notYet(String msg) {
    	String message = "    *************  NOT YET IMPLEMENTED - So this will not work  ************* ";
    	if (notEmpty(msg)) message += msg;
    	log().fatal(message);
    }
    public void notYet() {
    	notYet("Default");
    }
    
}
