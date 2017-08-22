package sftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jcraft.jsch.SftpException;
import com.jscape.inet.sftp.Sftp;
import com.jscape.inet.sftp.SftpConfiguration;
import com.jscape.inet.sftp.SftpFile;
import com.jscape.inet.ssh.SshConfiguration;

import com.jscape.inet.ssh.util.SshParameters;

import lang.Universal;
import model.McOrders;
import util.SftpAccess;


public class SFTPConnection extends Universal{

	
	protected final String RATES_FOLDER_NAME = "write/temp_createdbyavidity_Rates"; // for DEV
	protected final String RATES_PROCESSED_FOLDER_NAME = RATES_FOLDER_NAME + "/Archived";
	
	protected final String PAYMENTS_FOLDER_NAME = "write/temp_createdbyavidity_Payments"; // for DEV
	protected final String PAYMENTS_PROCESSED_FOLDER_NAME = PAYMENTS_FOLDER_NAME + "/Processed";
	protected final String PAYMENTS_ERROR_FOLDER_NAME = PAYMENTS_FOLDER_NAME + "/FileError";
	
	protected final String ORDERS_FOLDER_NAME = "write/temp_createdbyavidity_Orders";

	//csv file variables
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//Orders CSV File Header 
	private static final String FILE_HEADER = "P3SRef,USDtoEURrate,USDamount,EURamount,Destination,Speed,Date";
	
	/** connecting to our SFTP server **/
	protected final String sftpURL = "162.13.14.125";
	protected final String username = "moneycorp";
	protected final String password = "P4t3ntFX79";

	/** connecting to moneycorp SFTP **/
	/*protected final String sftpURL = "securefile.moneycorp.com";
	protected final String username = "avidity-cfx";
	protected final String password = "2Gc9jheJ";*/

	protected Sftp sftpConnection = null;

	
	
	// The demo method
	
	/**
	 * Sequence of tests: 
	 * - Connect 
	 * - List contents of 'Rates' folder
	 * - Read & display contents of each file
	 * - Write a file
	 * - Move that file 
	 * @throws SftpException 
	 */
	public void connectToSftp() throws IOException, SftpException {

		System.out.println("SFTP Connecting ... \n");
		connect();
		System.out.println("Post connect attempt \n");
		

	}

	
	
	// Utility methods start here
	
	
	/**
	 * Makes a SFTP connection to the host.
	 * Once connected, all methods below may be invoked.
	 * It is client responsibility to ensure:
	 * - are connected before invoking any such method
	 * - call disconnect() upon completion
	 * @throws SftpException 
	 */
	public void connect() throws SftpException {

		if (sftpConnection != null && sftpConnection.isConnected()) { // Just in case
			log().warn("connect() invoked when already connected");
			sftpConnection.disconnect();
		}

		SshParameters params = new SshParameters(sftpURL, username, password);
		sftpConnection = new Sftp(params);

		try {
			log().debug("SFTPConnection: SFTP about to connect:");
			sftpConnection.connect();
		} catch (com.jscape.inet.sftp.SftpException e1) {
			log().error("SFTPConnection: connect: Failed to connect",e1);
			
		}
	}

	
	public void disconnect() {
		log().debug("SFTPConnection: SFTP about to disconnect:");
		if (sftpConnection != null && sftpConnection.isConnected()) {
			log().debug("   Connection can be disconnected");
			sftpConnection.disconnect();
		}
		else log().warn("disconnect() invoked when non-connected");
	}

	/** method lists only those files with .csv extension 
	 * @throws SftpException ***/
	public ArrayList<SftpFile> listAllCsvSftpFiles(String readFolder) throws SftpException {

		ArrayList<SftpFile> sftpFiles = new ArrayList<SftpFile>();

		String tgtDir = "/";
		if (readFolder != null)
			tgtDir += readFolder;
		try {
			System.out.println("Present directory" + sftpConnection.getDir());
			sftpConnection.setDir(tgtDir);
			Enumeration list = sftpConnection.getDirListing();
			while (list.hasMoreElements()) {
				SftpFile name = (SftpFile) list.nextElement();

				if (!name.isDirectory()) {
					if (name.getFilename().toLowerCase().endsWith(".csv"))
						sftpFiles.add(name);
				}
			}

		} catch (com.jscape.inet.sftp.SftpException e) {
			log().error("SFTP getDirListing() failed", e);
			//e.printStackTrace();
			//throw e;
		}
		return sftpFiles;
	}

	/** as listAllCsvSftpFiles(), but returns ArrayList<String> **/
	public ArrayList<String> listAllCsvFilenames(String readFolder) throws SftpException {
		ArrayList<String> fileNames = new ArrayList<String>();
		for (SftpFile sftpFile : listAllCsvSftpFiles(readFolder)) {
			fileNames.add(sftpFile.getFilename());
		}
		return fileNames;
	}

	/** method reads a CSV file 
	 * @throws IOException **/
	public Map<Integer, String[]> readCsvSftpFile(String readFolder, SftpFile readFile) throws IOException {

		String tgtDir = "/";
		if (readFolder != null) tgtDir += (readFolder + "/");

		String[] dataArray = null;
		HashMap<Integer, String[]> contents = new HashMap<Integer, String[]>();
		try {

			int numLines = 0;
			InputStream in = sftpConnection.getInputStream(tgtDir + readFile.getFilename(), 0L);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = br.readLine();
			while (data != null) {
				dataArray = data.split(",");
				for (String item : dataArray) {
					contents.put(numLines, dataArray);
				}
				numLines++;
				data = br.readLine();
			}

			br.close();
			in.close();

		} catch (IOException e) {
			log().error("SFTP read file contents failed", e);
			//e.printStackTrace();
			throw e;
		}

		return contents;
	}

	

	/** method to delete a csv file 
	 * @throws SftpException **/
	public void deleteSftpFile(String readFolder, String deleteFileName) throws SftpException {

		String tgtDir = "/";
		if (readFolder != null)
			tgtDir += (readFolder + "/");
		//System.out.println("delete tgt = " + tgtDir + deleteFileName);
		try {
			sftpConnection.deleteFile(tgtDir + deleteFileName);
		} catch (com.jscape.inet.sftp.SftpException e) {
			// com.jscape.inet.sftp.SftpFileNotFoundException
			log().error("SFTP delete failed", e);
			//e.printStackTrace();
			
		}
	}

	

	/**
	 * Convenience method NOT Null-proofed
	 * 
	 * @param list
	 *           And Array of Arrays of strings. Type CSV file contents. Many
	 *           lines. Each line may have many elements
	 * @return Array of strings. e.g. same CSV file, with each line being one
	 *         string. Adds comma beteen elements.
	 * **/
	public ArrayList<String> assembleArrArrStrIntoArrStr(ArrayList<String[]> list) {
		ArrayList<String> lines = new ArrayList<String>();
		for (String[] arr : list) {
			String thisLine = "";
			String separator = "";
			for (String element : arr) {
				thisLine += (separator + element);
				separator = ",";
			}
			lines.add(thisLine);
		}
		return lines;
	}

	
	// convert HashMap<Integer, String[]> to ArrayList<String>
	public ArrayList<String> assembleHashmapToArrlistStr(Map<Integer, String[]> contents) {
   	ArrayList<String> lines = new ArrayList<String>();
   	for (int ii = 0 ; ii < contents.size() ; ii++) {
   		String[] elements = contents.get(ii);
   		String thisLine = "";
   		String separator = "";
   		for (String element : elements) {
   			thisLine += (separator + element);
   			separator = ",";
   		}
   		lines.add(thisLine);
   	}
   	return lines;
	}
	

	
	
	
	/**
	 * method that sftp-writes a new csv file
	 * 
	 * @param lines
	 *           An array of arrays. Outer is each line. Inner is each csv field
	 *           for that line
	 **/
	
	/** method that uploads a new file with contents as a ArrayList<String[]>  **/
	public void createCsvSftpFile(String writeFolder, String writeFileName, ArrayList<String[]> list) throws IOException {

		ArrayList<String> lines = assembleArrArrStrIntoArrStr(list);

		createCsvSftpFileFromLines(writeFolder, writeFileName, lines);
		
	}

			
	/** method that uploads a new file with contents as a ArrayList<String[]>  
	 * @throws IOException **/
	public void createCsvSftpFileFromLines(String writeFolder, String writeFileName, ArrayList<String> lines) throws IOException {

		try {

			String wholeFileAsString = "";
			for (String aLine : lines) {
				wholeFileAsString += (aLine + "\r\n");
			}
			byte[] wholeFileAsBytes = wholeFileAsString.getBytes();

			String tgtDir = "/";
			if (writeFolder != null)
				tgtDir += writeFolder;

			sftpConnection.setDir(tgtDir);
			sftpConnection.upload(wholeFileAsBytes, writeFileName);

		} catch (IOException e) {
			int asst = (lines==null) ? -1 : lines.size();
			log().error("createCsvFileFromLines failed ["+asst+"]", e);
			//e.printStackTrace();
			throw e;
		}
	}

	
	
	
	
	
	
	
	/** Effectively a move command. i.e. move directory (but not rename filename)
	 * @throws IOException 
	 * @throws SftpException 
	 **/
	public void sftpMoveFileToDir(String srcFolder, String dstFolder, SftpFile file) throws IOException, SftpException {
		boolean fileWritten = false;
		try {

			HashMap<Integer, String[]> fileContents = new HashMap<Integer, String[]>();
			fileContents = (HashMap<Integer, String[]>) readCsvSftpFile(srcFolder, file);

			// convert HashMap<Integer, String[]> to ArrayList<String>
			ArrayList<String> lines = assembleHashmapToArrlistStr(fileContents);
			
					// acdebug ac todo - consistency : String[] , ArrayList<String[], ArrayList<String>
					
			// Write file to the new location
			createCsvSftpFileFromLines(dstFolder, file.getFilename(), lines);
			fileWritten = true;
			
			// If above write was successful (i.e. no exception thrown) then is now safe to delete the original
			deleteSftpFile(srcFolder, file.getFilename());
		
		} catch (IOException e) {
			String alarm = (fileWritten) ? " [BEWARE: file is duplicated]" : "";
			log().error("SFTP 'move' operation failed"+alarm, e);
			//e.printStackTrace();
			throw e;
		}
	}
	

	/** Identifies that newest CSV file in the folder, using modification date
	 * @return Returns null if no csv file found. Else returns filename 
	 * @throws SftpException 
	 * 
	 **/
	public SftpFile getLatestSftpFile(String readFolder) throws SftpException {

		String tgtDir = "/";
		if (readFolder != null) tgtDir += readFolder;
		int numFiles = 0;
		SftpFile latestFileSoFar = null;
		try {
			sftpConnection.setDir(tgtDir);
			Enumeration list = sftpConnection.getDirListing();
			while (list.hasMoreElements()) {
				SftpFile thisFile = (SftpFile) list.nextElement();
				if ( ! thisFile.isDirectory()) {
					numFiles++;
					String thisFilename = thisFile.getFilename();
					if (thisFilename.toLowerCase().endsWith(".csv")) {
						if ( (latestFileSoFar==null)
							|| (thisFile.getModificationTime() > latestFileSoFar.getModificationTime())) {
									latestFileSoFar = thisFile; 
						}
					}
				}
			}
			
			/** If numfiles == 1 , no new file arrived today . So no archiving needed. 
			 *  If numfiles == 2 , new Rates file arrived today. So archive the old file
			 */
			if(numFiles == 2){
				new SftpAccess().archiveOlderRatefiles();
			}
			else{
				log().fatal("Unexepected number of files in Rates folder : rataes folder has " + numFiles + " present");
			}
		} catch (com.jscape.inet.sftp.SftpException e) {
			log().error("SFTP getLatestSftpFile() failed", e);
			//throw e;
		}
		return (latestFileSoFar==null) ? null : latestFileSoFar;
	}
	
	
	public void uploadCSVFile(byte[] data, String writeFolder, String filename) throws IOException{
		
		try{
			
			
			String tgtDir = "/";
			if (writeFolder != null)
				tgtDir += writeFolder;

			sftpConnection.setDir(tgtDir);
			
			sftpConnection.upload(data,filename);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	
	
}
