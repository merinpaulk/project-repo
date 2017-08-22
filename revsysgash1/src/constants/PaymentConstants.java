package constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PaymentConstants {
	
	public static final Map<Integer , String> STATUS =Collections.unmodifiableMap( new HashMap<Integer , String>() {{
	    put(1,    "Pending");
	    put(2, "Funds Received");
	    put(3,   "Funds Sent");
	    put(4, "EPO Received");
	    put(5, "Failed");
	   //EPO Instructed and EPO Received status values not from MC
	}});

}
