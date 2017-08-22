package properties;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 * Simple Property-File reader
 * 
 */
public class PropertyReader  {

	protected Properties safeProperties = null;  // either null or reliable
	

	
	private PropertyReader () { ; /* Inhibit Default constructor */ } 

	/**
	 * Instantiate & Attempt to open specified property file
	 * @param path Optional path. Preferably without trailing forwardslash. 
	 * Note: Forwardslashes, NOT backslashes. Even on windows.
	 * @param filename of property file to read
	 * @throws IOException 
	 */
	public PropertyReader(String path, String filename) throws IOException {
		readProperties(path, filename);
	}

	
	/**
	 * Attempts to open specified property file. Stores properties to safeProperties member. 
	 * @param path Optional path. Preferably without trailing forwardslash. Note: Forwardslashes, NOT backslashes. Even on windows.
	 * @param filename of property file to read
	 * @return null indicates problem reading file
	 * @throws IOException 
	 */
	protected Properties readProperties(String path, String filename) throws IOException {
		if (notEmpty(path)) path+="/"; else path = "";
		if (filename==null) return null;

		safeProperties = null;
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(path+filename);
			prop.load(input);
			safeProperties = prop;
	 
		} catch (IOException io) {
			System.out.println("Failed read Propfile at "+path+" : "+filename);
			throw io;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					;
				}
			}
		}
		return safeProperties;
	  }

	
	 

	/**
	 * Read a named property from the currently loaded Properties  
	 * @param propertyname
	 * @return The property value for key = propertyname
	 */
	public String getProperty(String propertyname) {
		if (safeProperties==null || isEmpty(propertyname)) {
			System.out.println("PropertyReader:getProperty Invoked with null"); // Nice if could log stacktrace
			return null;
		}
		return safeProperties.getProperty(propertyname);
	}

	
	protected Properties getAllProperties() {
		return safeProperties;
	}


	
	public boolean isEmpty(String val) {
		if (val==null || val.trim().length()==0) return true;
		return false;
	}
	public boolean notEmpty(String val) {
		return ! isEmpty(val);
	}

}
