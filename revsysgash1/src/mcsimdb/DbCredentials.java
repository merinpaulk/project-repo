package mcsimdb;

import properties.PropertyReader;

public class DbCredentials {
	
	public String dbhost = "cc-scrape";
	public String db = "p3s";
	public String un = "p3suser";
	public String pwd = "11111";
	
	
	public DbCredentials() {
		System.out.println("Attempting to open property file");

		// chack for host = TomcatA or Andy's pc. Owt else uses cc-scrape
		
		PropertyReader pr = null;
		try {
			pr = new PropertyReader("C:/MERIN/Documents/PatentPlace/MC Simulator/revsysgash1/revsysgash1/", "db.properties");
		} catch (Exception e) {
			System.out.println("DbCredentials suffered exception whilst reading properties. Is NOT Andy's Lenovo");
			// Not Andy's Lenovo PC. Try tomcatA
			try {
				pr = new PropertyReader("/opt/tomcat8/webapps/", "mcsimulatorv1.properties");
			} catch (Exception f) {
				System.out.println("DbCredentials suffered AGAIN exception whilst reading properties. Is not TomcatA");
				//f.printStackTrace();
			}
		}

		if (pr != null) {
			dbhost = pr.getProperty("dbhost");
			db = pr.getProperty("db");
			un = pr.getProperty("un");
			pwd = pr.getProperty("pwd");
		} else System.out.println("unable to read properties");
	

		System.out.println("at end: creds are to dbhost : "+dbhost+":"+db);
	}
	

	
}
