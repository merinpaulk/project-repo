package util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import sftp.SFTPConnection;
import com.jscape.inet.sftp.Sftp;
import com.jscape.inet.sftp.SftpException;
import com.jscape.inet.sftp.SftpFile;

import lang.Universal;
import model.McOrders;
import model.McRate;

/**
 * Provides SFTP functionality which is specific to Moneycorp
 * @author andyc, merinp
 */
public class SftpAccess extends Universal {

	protected final String RATES_FOLDER_NAME = "write/temp_createdbyavidity_Rates"; // for DEV
	protected final String RATES_PROCESSED_FOLDER_NAME = RATES_FOLDER_NAME + "/Archived";
	
	protected final String PAYMENTS_FOLDER_NAME = "write/temp_createdbyavidity_Payments"; // for DEV
	protected final String PAYMENTS_PROCESSED_FOLDER_NAME = PAYMENTS_FOLDER_NAME + "/Processed";
	protected final String PAYMENTS_ERROR_FOLDER_NAME = PAYMENTS_FOLDER_NAME + "/FileError";
	
	protected final String ORDERS_FOLDER_NAME = "write/temp_createdbyavidity_Orders";

	//csv file variables
	private static final String COMMA_DELIMITER = ",";
		
	//Orders CSV File Header 
	private static final String FILE_HEADER = "P3SRef,USDtoEURrate,USDamount,EURamount,TransactionStartDate,TransactionTargetEndDate,Destination,Speed,Date";
	
	
	/**
	 * Get the latest Rate.
	 * Initially, return as a String. Soon (Jan16) we'll create a domain class to hold a rate 
	 * MP18082017 Created a model class and so replacing return type to McRate
	 * @return the rate
	 * @throws SftpException, IOException
	 * @throws com.jcraft.jsch.SftpException 
	 */
	public McRate getLatestRate() throws SftpException, IOException, MoneycorpFileFormatException, com.jcraft.jsch.SftpException {
		
		McRate todaysRate = new McRate();
		String latestRate = null;
		
		SftpFile latestRateFile = getLatestRateFile();
		if (latestRateFile==null) return todaysRate;
		//System.out.println("Latest Rates file is : "+latestRateFilename);
		
		SFTPConnection util = new SFTPConnection();
		util.connect();

		
		Map<Integer, String[]> asMap = util.readCsvSftpFile(RATES_FOLDER_NAME, latestRateFile);
		ArrayList<String> lines = util.assembleHashmapToArrlistStr(asMap);

		try {
			latestRate = parseAndExtractRate(lines);
		} catch (MoneycorpFileFormatException e) {
			log().error("", e);
			util.disconnect(); // Disconnect now, in case subsequent processing of exception required open/close
			throw e;
		}
		BigDecimal decimalRate = new BigDecimal(latestRate);
		todaysRate.setCurrentRate(decimalRate);
		util.disconnect(); // May already be disconnected
		return todaysRate;
	}
	
	
	
		/**
	 * Gets the name of the latest Rates file
	 * @param readFolder
	 * @return latest SftpFile
	 * @throws SftpException 
		 * @throws com.jcraft.jsch.SftpException 
	 */
	public SftpFile getLatestRateFile() throws SftpException, com.jcraft.jsch.SftpException {
		
		SFTPConnection util = new SFTPConnection();    
		util.connect();
		SftpFile latestRateFile = util.getLatestSftpFile(RATES_FOLDER_NAME);
		util.disconnect();
		return latestRateFile;
	}

	
	/**
	 * Identifies the latest CSV in the Rates file, & moves all other CSVs to the Processed subfolder
	 * 
	 * Doesn't throw any exception on failure. Client wont care. It works or it doesn't
	 * @throws com.jcraft.jsch.SftpException 
	 */
	public void archiveOlderRatefiles() throws com.jcraft.jsch.SftpException {
		// Make safe against race condition: i.e. new ratefile appears just after determining the latest file

		// This necessarily largely duplicates SFTPConnection:getLatestSftpFile() 

		final String ratesFolder = "/"+RATES_FOLDER_NAME;
		final String processedFolder = "/"+RATES_PROCESSED_FOLDER_NAME;

		try {
   		SFTPConnection util = new SFTPConnection();
   		util.connect();
   		
   		ArrayList<SftpFile> allRatefiles = util.listAllCsvSftpFiles(ratesFolder);
   		SftpFile latestFileSoFar = null;
   		for (SftpFile aFile : allRatefiles) {
   			if ( (latestFileSoFar==null)
   					|| (aFile.getModificationTime() > latestFileSoFar.getModificationTime())) {
   							latestFileSoFar = aFile; 
   			}
   		}
   		if (latestFileSoFar!=null) {
   			String latestRateFilename = latestFileSoFar.getFilename();
   			for (SftpFile bFile : allRatefiles) {
   				String bFilename = bFile.getFilename();
   				if ( ! bFilename.equals(latestRateFilename)) {
   					util.sftpMoveFileToDir(ratesFolder, processedFolder, bFile);
   				}
   			}
   		}
		}
		catch (IOException e) {
			log().error("archiveOlderRatefiles failed", e);
			// Don't re-throw. Client doesn't care
		}
	}

	
	/**
	 * Given a moneycorp Rates file, 
	 * Extracts the USD->EUR rate (line 2, field 6)
	 * after performing rudimentary parsing, to have confidence in file format
	 * @param lines
	 * @return
	 */
	protected String parseAndExtractRate(ArrayList<String> ratesFileContents) throws MoneycorpFileFormatException {
		
      //Files should conform to the following:
      //- Line 1, field 6 = 'ClientAsk'
      //- are 4 lines (or 5th line is empty)
      //- Line 2, field 6 is decimal numeric
      //- Line 2, field 6 value is between 0.7 and 2.2
   
		final BigDecimal lowWater  = new BigDecimal(0.7); 
		final BigDecimal highWater = new BigDecimal(2.2); 
		
   	String failMsg = null;
   	String rateStr = null;
   	BigDecimal rateBD = null;
   	
		String line1 = null;
		String line2 = null;
		String[] line1Elements = null;
		String[] line2Elements = null;

		if (failMsg==null) {
   		int numLines = ratesFileContents.size();
      	if (numLines<4 || numLines>5) failMsg = "Wrong number of lines ["+numLines+"]"; 
      	if (numLines==5) {
      		String line5 =  ratesFileContents.get(4);
      		if (notEmpty(line5)) failMsg = "Lines 5 is not empty ["+line5+"]";
      	}
   	}   
   	if (failMsg==null) {
   		line1 =  ratesFileContents.get(0);
   		if (line1==null) failMsg = "improbable1";
   	}
     	if (failMsg==null) {
   		line1Elements = line1.split(",");
   		int line1numElements = line1Elements.length; 
   		if (line1numElements != 7) failMsg = "Lines 1 num elements is incorrect ["+line1numElements+"]";
     	}
     	if (failMsg==null) {
     		String rateTitle = line1Elements[5];
     		if ( ! "ClientAsk".equals(rateTitle)) failMsg = "Rate title is not 'ClientAsk'. [is "+rateTitle+"]";
     	}
     	if (failMsg==null) {
   		line2 =  ratesFileContents.get(1);
   		line2Elements = line2.split(",");
   		int line2numElements = line2Elements.length; 
   		if (line2numElements != 7) failMsg = "Lines 2 num elements is incorrect ["+line2numElements+"]";
		}
     	if (failMsg==null) {
   		rateStr = line2Elements[5];
   		if (isEmpty(rateStr)) failMsg = "Rate field is empty";
   		try {
   			rateBD = new BigDecimal(rateStr);	
      		// If survice to here, rate is ok
   		} 
   		catch (NumberFormatException nfe) {
   			failMsg = "Rate field is not numeric["+rateStr+"]";
   		}
   		//System.out.println("acdebug  got rate of "+rateStr);
     	}
     	if (failMsg==null) {
     		if ((rateBD.compareTo(lowWater) != 1)
     				|| (rateBD.compareTo(highWater) != -1)) failMsg = "Rate value is impossible ["+rateBD+"]";
     	}
     	
     	
   	if (failMsg!=null) {
   		throw new MoneycorpFileFormatException(failMsg); 
   	}
   	return rateStr;
	}
	
	/**
     * The method gets all the Unprocessed SftpFiles
     * @return list of SftpFiles
     * @throws SftpException if failed to connect to SFTP
	 * @throws com.jcraft.jsch.SftpException 
     */
	public ArrayList<SftpFile> getAllPayFiles() throws SftpException, com.jcraft.jsch.SftpException {
		
		
		final String paymentsFolder = "/"+PAYMENTS_FOLDER_NAME;
		
		SFTPConnection util = new SFTPConnection();
		util.connect();
		ArrayList<SftpFile> fileList=util.listAllCsvSftpFiles(paymentsFolder);
		
		if(! (fileList == null)){
			log().debug(fileList.size()+" Unprocessed Payment files found ");
			/*for (SftpFile aFile : fileList) {
	   			if ( aFile.getFilename().contains(caseNumber)){
	   				payFiles.add(aFile);
	   			}
	   		}*/
   		}
    	
		util.disconnect();
		return fileList;
		
	}
	
	/**
     * The method gets the reference Id from the SftpFile
     * @param SftpFile
     * @return SftpFile which is the latest
     */
	public String getPayReferenceId(SftpFile aFile) throws MoneycorpFileFormatException{
		String payRefId = null;
		String failMsg = null;
		String latestPayName = aFile.getFilename();
		
	
		
		if(failMsg == null){
			if(!(latestPayName.endsWith(".csv")) ) failMsg = latestPayName+" file format error";
		}	
			
		if(failMsg == null){
			int offset_ = latestPayName.indexOf('_');      //assuming filename format to be xxxAvxxxxx_N.csv
			if(offset_ == 0) failMsg = latestPayName+" file format error"; 
			else{
				payRefId= latestPayName.substring(0, offset_);
			}
			
		}
		if (failMsg!=null) {
	   		throw new MoneycorpFileFormatException(failMsg); 
	   	}
		
		return payRefId;
	}
	
	/**
     * The method gets the statusId from the SftpFile
     * @param SftpFile
     * @return SftpFile which is the latest
     */
	public int getPayStatus(SftpFile aFile) throws MoneycorpFileFormatException{
		int payStatus = 0;
		String failMsg = null;
		String latestPayName = aFile.getFilename();
		int status = 1;
		int aStatus = 0;
	
		
		if(failMsg == null){
			if(!(latestPayName.endsWith(".csv")) ) failMsg = latestPayName+" file format error";
		}	
			
		if(failMsg == null){
			int offset_ = latestPayName.indexOf('_');      //assuming filename format to be xxxAvxxxxx_N.csv
			if(offset_ == 0) failMsg = latestPayName+" file format error"; 
			else{
				payStatus= Integer.parseInt(latestPayName.substring(offset_+1, latestPayName.length()-4));
				if(payStatus > 4) failMsg = latestPayName+" has an invalid status code";    // assuming that we have only 1,2,3 & 4 status codes
			}
			
		}
		if (failMsg!=null) {
	   		throw new MoneycorpFileFormatException(failMsg); 
	   	}
		
		return payStatus;
	}
	
	/**
     * The method archive the list of SftpFiles passed as parameter
     * @param SftpFile list 
     * @return void
	 * @throws com.jcraft.jsch.SftpException 
     * @throws IOException
     */
	public void archivePayFiles(ArrayList<SftpFile> payFiles) throws com.jcraft.jsch.SftpException{
		final String paymentsFolder = PAYMENTS_FOLDER_NAME;
		final String processedFolder = PAYMENTS_PROCESSED_FOLDER_NAME;
		try{
			SFTPConnection util= new SFTPConnection();
			util.connect();
	   		if(payFiles != null){
		   		for(SftpFile thisFile : payFiles)
		   			util.sftpMoveFileToDir(paymentsFolder, processedFolder, thisFile);
		   		log().debug("Old Payment Files archived to Processed subfolder");
	   		}
	   		util.disconnect();
		}catch(IOException e){
			log().error("archivePaymentsfiles failed", e);
		}
	}
	
	public void archiveIncorrectPayFiles(SftpFile payFile) throws com.jcraft.jsch.SftpException{
		final String paymentsFolder = PAYMENTS_FOLDER_NAME;
		final String processedFolder = PAYMENTS_ERROR_FOLDER_NAME;
		try{
			SFTPConnection util= new SFTPConnection();
			util.connect();
	   		if(payFile != null){
		   		util.sftpMoveFileToDir(paymentsFolder, processedFolder, payFile);
		   		log().debug("Old Payment Files with error archiving to Error subfolder");
	   		}
	   		util.disconnect();
		}catch(IOException e){
			log().error("archivePaymentsfiles failed", e);
			
		}
	}
		
	public void uploadAsCSVFile( ArrayList<String> contents, String filename) throws IOException, com.jcraft.jsch.SftpException{
		
		final String writeFolder = ORDERS_FOLDER_NAME;
		SFTPConnection util= new SFTPConnection();
		util.connect();
		try{
			String wholeFileAsString = "";
			for (String aLine : contents) {
				wholeFileAsString += (aLine + "\r\n");
			}
			
			byte[] wholeFileAsBytes = wholeFileAsString.getBytes();
		
			util.uploadCSVFile(wholeFileAsBytes,writeFolder,filename);
		}
		catch(IOException e){
			log().error("Exception inside uploadAsCSVFile() "+ e.getMessage());
		}
		
	}
	
	
	public ArrayList<String> writeOrdersFileContents(McOrders newOrder) throws IOException{
		
		ArrayList<String> lines = new ArrayList<String>();
		//Build the newOrder content as like a csv - HAND CRAFTING THE FILE CONTENTS. THE FORMAT SHOULD BE THE SAME EVERYTIME
		String oneLine = "";
		oneLine = newOrder.getP3sRef()+ COMMA_DELIMITER + newOrder.getUsdToEurRate()+ COMMA_DELIMITER + 
				newOrder.getUSDAmount() + COMMA_DELIMITER + newOrder.getEURAmount() + COMMA_DELIMITER +
				newOrder.getDestination() + COMMA_DELIMITER +newOrder.getSpeed() + COMMA_DELIMITER +
				newOrder.getNowDate().getTime();
		
		lines.add(FILE_HEADER);
		lines.add(oneLine);
		
		if(lines.size() == 2)
			log().debug(" writeOrdersFileContents() finished by generating 2 lines of data"); 
		return lines;
	}
		
}
