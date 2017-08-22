package AppLaunch;

import util.SftpAccess;

import java.io.IOException;

import com.jcraft.jsch.SftpException;

import engine.SFTPFileProcessingEngine;
import sftp.SFTPConnection;


public class Launch {

	
	
	protected void doEverything() throws SftpException {

		System.out.println("Now the moneycorp specific utilities");
		SFTPFileProcessingEngine processEngine = new SFTPFileProcessingEngine();

		SFTPConnection sftp = new SFTPConnection();
		
		/*** 
		 * TASK 1 : PROCESS PAYMENTS FILE
		 */
		
		/*try{
			sftp.connectToSftp();
			processEngine.processPaymentFiles();
		}
		catch (Exception e) {
			//log().error("Failure within moneycorp specific utilities - 3", e);
		}
		finally{
			sftp.disconnect();
		}*/
		
		/*** 
		 * TASK 2 : PROCESS RATES FILE
		 */
		
		/*try{
			sftp.connectToSftp();
			processEngine.processRatesFile();
		}
		catch (Exception e) {
			//log().error("Failure within moneycorp specific utilities - 3", e);
		}
		finally{
			sftp.disconnect();
		}*/
		
		/*** 
		 * TASK 3 : GENERATE ORDERS FILE
		 */
		
		try{
			sftp.connectToSftp();
			processEngine.generateOrdersFile();
		}
		catch (Exception e) {
			//log().error("Failure within moneycorp specific utilities - 3", e);
		}
		finally{
			sftp.disconnect();
		}
		
	}

	
	
	public static void main(String[] args) throws SftpException {
		System.out.println("Moneycorp main() starting ...");

		Launch me = new Launch();
		me.doEverything();

		System.out.println("Moneycorp main() completing.");
	}
}
