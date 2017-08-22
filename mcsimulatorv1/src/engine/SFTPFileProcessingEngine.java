package engine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.jscape.inet.sftp.SftpException;
import com.jscape.inet.sftp.SftpFile;

import constants.PaymentConstants;
import formHandlers.P3sTransaction;
import lang.Universal;
import mcsimdb.ActiveTransactions;
import mcsimdb.PopulateOrdersData;
import mcsimdb.UpdateTodaysRate;
import model.McOrders;
import model.McRate;
import model.McTransaction;
import util.MoneycorpFileFormatException;
import util.SftpAccess;

public class SFTPFileProcessingEngine extends Universal {
	
	protected String PREFIX = this.getClass().getName() + " : "; 
	
	
	/**
	 * Method to process Payment files
	 * 		Get all unprocessed payment files
	 * 		Read the status values and payment reference numbers on these files
	 * 		Update db with the status for the reference numbers
	 * 		Archive the processed files
	 * @throws com.jcraft.jsch.SftpException 
	 * @throws SftpException 
	 * @throws MoneycorpFileFormatException 
	 */
	public void processPaymentFiles() throws SftpException, com.jcraft.jsch.SftpException, MoneycorpFileFormatException{
		
		String msg = PREFIX + "processPaymentFiles()";
		SftpAccess sftpAccess = new SftpAccess();
		P3sTransaction p3s = new P3sTransaction();
		ActiveTransactions trans = new ActiveTransactions();
		McTransaction transaction = null;
		
		String statusStr = null;
		log().debug( msg + " invoked on " + Calendar.getInstance());
		
		ArrayList<SftpFile> payFiles = sftpAccess.getAllPayFiles();
		
		if(payFiles.size() == 0){
			log().debug( msg + " returned no new payment files. Exit the method");
			return;
		}
		else{
			log().debug( msg + " returned "+  payFiles.size() + " new payment files");
			for (SftpFile aFile : payFiles) {
				
				sftpAccess.getPayStatus(aFile);
				String refId = sftpAccess.getPayReferenceId(aFile);
				int status = sftpAccess.getPayStatus(aFile);
				statusStr = PaymentConstants.STATUS.get(status);
				
				log().debug("Read the new file " + aFile.getFilename() + " and obtained Reference Id : " + refId + " and latest status as " + statusStr);
				
				transaction = trans.getTransaction(refId); 
				
				if(transaction == null){
					log().debug("getTransaction(" +refId +") returned null from db. Email triggered");
					log().fatal("getTransaction(" +refId +") returned null from db. Error in filename "+ aFile.getFilename());
					//archive the current file to FileError folder
					sftpAccess.archiveIncorrectPayFiles(aFile);
					payFiles.remove(aFile);
					//trigger an email here
					
				}
				else{
					p3s.runSave(transaction.getTransactionId(), statusStr);;
				}
				
			}
			
			sftpAccess.archivePayFiles(payFiles);
		}
		
		log().debug( msg + " Completed");
	}

	
	public void processRatesFile() throws com.jcraft.jsch.SftpException, IOException, MoneycorpFileFormatException, SQLException{

		
		String msg = PREFIX + " processRatesFile() ";
		boolean isUpdated = false;
		log().debug(msg +"invoked :::");
		
		SftpAccess sftpAccess = new SftpAccess();
		
		McRate latestRate = sftpAccess.getLatestRate();
		System.out.println("Latest rate as " + latestRate);
		
		if(! (latestRate == null))
			isUpdated = new UpdateTodaysRate().updateRates(latestRate.getCurrentRate());
				
	}

	public void generateOrdersFile(Long payId) throws IOException, com.jcraft.jsch.SftpException{
		
		String msg = PREFIX + " generateOrdersFile() ";
		log().debug(msg +" invoked::::::");
		ArrayList<String> lines = new ArrayList<String>();
		
		SftpAccess sftpAccess = new SftpAccess();
		//GET THE DATA FOR THE CSV FILE IN AN ATTAYLIST
		McOrders newOrder = getOrdersFileData(payId);
		
		if(newOrder == null){
			log().debug(msg +" Order file content creation failed. Exit the function now.");
			log().fatal(msg +" Order file content creation failed. Exit the function now.");
			return;
		}
		
		log().debug(msg +" Order file content created successfully.");
		lines = sftpAccess.writeOrdersFileContents(newOrder);
		
		if(lines != null){
			
			sftpAccess.uploadAsCSVFile(lines,newOrder.getP3sRef()+".csv");
		}
		
	}
	
	public McOrders getOrdersFileData(Long id){
		
		String msg = PREFIX + " getOrdersFileData() ";
		
		McOrders fromDB = new McOrders();
		McOrders newOrder = new McOrders();
		PopulateOrdersData fetchData = new PopulateOrdersData();
		log().debug(msg +" invoked::::::");
		
		fromDB = fetchData.getOrdersData(id);
		
		//hard coding values here. To be replaced with actual values form DB?????
		//McOrders newOrder = new McOrders("CQ 123 456 789 QC", "1.143", "465.00", "531.495", "EPO" , "SEPA" , Calendar.getInstance() );
		
		if ( fromDB != null ) {
			
			newOrder = new McOrders(fromDB.getP3sRef(), fromDB.getUsdToEurRate(), fromDB.getUSDAmount(), fromDB.getEURAmount(),
				fromDB.getTransStartDate(), fromDB.getTransTargetEndDate(), fromDB.getDestination(), fromDB.getSpeed(), 
				Calendar.getInstance().getTime());
			
		}
		
		return newOrder;
	}
	
	
	
	
}
