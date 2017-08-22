package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import sftp.SFTPConnection;
import com.jscape.inet.sftp.Sftp;
import com.jscape.inet.sftp.SftpFile;
import com.jscape.inet.ssh.util.SshParameters;

public class FileTransfer {
	
//	    SFTPConnection connection=new SFTPConnection();
//	    HashMap<Integer, String[]> contents=new HashMap<Integer, String[]>();
//	    
//		public void RENAMEME(){
//		
//		String ratesFolder="temp_createdbyavidity_Rates";
//		String readFileName="AvidityRatesFile_20160118.csv";
//		
//		String paymentsFolder="temp_creatdebyavidity_Payments";
//		
//		String writeFolder="temp_createdbyavidity_Orders";
//		String writeFileName="AvidityRatesFile_written.csv";
//		
//		
//		
//		
//		
//		/*** connecting to moneycorp SFTP **/
//		String ftpURL = "securefile.moneycorp.com";
//		String username = "avidity-cfx";
//		String password = "2Gc9jheJ";
//		
//		Sftp sftp= null;
//		try {
//				SshParameters params = new SshParameters(ftpURL,username,password);
//				sftp = new Sftp(params);
//				
//				try {
//					System.out.println("SFTPConnection: SFTP about to connect:");
//					sftp.connect();
//				} catch (com.jscape.inet.sftp.SftpException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} 
//				System.out.println("Connected");
//				
//			    /*** STEP1 -- DEALING WITH RATES FOLDER STARTS-- ***/ 
//			    System.out.println("STEP1 -- DEALING WITH RATES FOLDER STARTS-- ");
//			    System.out.println("Call to find recent Rate from MoneyCorp -- calling first method << call to local method >>");
//			    String recentRate=findLatestRate(sftp,ratesFolder);
//			   
//			   
//				System.out.println("STEP1 -- DEALING WITH RATES FOLDER ENDS--");
//			    /*** STEP1 -- DEALING WITH RATES FOLDER ENDS-- ***/ 
//				
//				/*** STEP2 -- DEALING WITH PAYMENTS FOLDER STARTS-- ***/ 
//				System.out.println("STEP2 -- DEALING WITH PAYMENTS FOLDER STARTS--");
//				
//				
//				
//			   System.out.println("Finding file from Payments for the given case if any and reading contents from same  << call to local method >> \n");
//			   String searchName="Av000005";
//			   String status = findLatestStatus(sftp,paymentsFolder,searchName);
//			   System.out.println("STEP2 -- DEALING WITH PAYMENTS FOLDER ENDS-- ");
//			   /*** STEP2 -- DEALING WITH PAYMENTS FOLDER ENDS-- ***/ 
//			   //ArrayList<String[]> list = new ArrayList<String[]>();
//			   //list.add(new String[] {"EP12345678901.6","535.49","465.00","465.00","0","1.1516","1338.73","1162.5","Av000006","Av000006"});
//			    
//			   //connection.createCSVFile(sftp,writeFolder,writeFileName,list);
//			   
//			   /*** call to delete file -- call to generic method **/
//			   System.out.println("call to delete file << call to generic method >>");
//			   //connection.deleteCSVFile(sftp,writeFolder,writeFileName);*/
//
//
//			   System.out.println("");
//				
//		  } finally {
//			  sftp.disconnect();
//		  }
//		  
//		  
//	}
//		
//		/** below method finds the latest file from Rates for reading the Rate and archive all other files to Processed folder**/
//		public SftpFile findLatestFile(Sftp sftp, String ratesFolder){
//			SftpFile latestFile=null;
//			
//			/*** call to list all files  -- call to generic list method **/
//			System.out.println("Call to list all files in Rates Folder  << call to generic method >> \n");
//			ArrayList<SftpFile> fileList=connection.listAllCSVFile(sftp,ratesFolder);
//			   
//			/*** finding latest file and reading contents from same --call to local method **/ 
//			   
//			System.out.println("Finding latest file from Rates << call to local method >> \n");
//		    
//		    try {
//		    	latestFile=fileList.get(0);
//		    	Iterator it=fileList.iterator();
//		    	while(it.hasNext()){
//		    		SftpFile temp = (SftpFile) it.next();
//		    		if(temp.getModificationTime()>latestFile.getModificationTime()){    //need to check whether we can use the access time
//		    			latestFile=temp;
//		    		}
//		    	}
//		    	
//		    	Iterator itr=fileList.iterator();
//		    	while(itr.hasNext()){
//		    		SftpFile temp = (SftpFile) itr.next();
//		    		if(!(temp.getFilename().equals(latestFile.getFilename()))){
//		    			File file=sftp.download(temp.getFilename());
//		        		sftp.setDir("Processed");
//		        		sftp.upload(file,temp.getFilename());
//		                sftp.setDir("/"+ratesFolder);
//		              //add the line to delete the moved file
//		    		}
//		    	}
//		    }
//		    catch (Exception ex) {ex.printStackTrace();}
//		    
//		    /*** calling method to read the contents of returned File -- call to generic method **/
//			System.out.println("Calling method to read the contents of returned File << call to generic method >>");
//			contents=(HashMap<Integer, String[]>) connection.readCSVFile(sftp,ratesFolder,latestFile.getFilename());
//			System.out.println("Reading contents of file "+latestFile);
//			Iterator i = contents.entrySet().iterator();
//
//			while(i.hasNext())
//			{
//				 Entry e = (Entry) i.next();
//				 int key = (Integer) e.getKey();  
//				 System.out.println(contents.get(key)[0]+" "+contents.get(key)[1]+" "+contents.get(key)[2]+" "+contents.get(key)[3]+" "+contents.get(key)[4]+" "+contents.get(key)[5]
//				    		+" "+contents.get(key)[6]);
//			}
//			
//		    return latestFile;
//		}	
//		
//		
//		/** below method finds a file from Payments folder based on a name and archive all other files to Processed folder**/
//		public SftpFile findRecentCSVFilebyName(Sftp sftp,String paymentsFolder, String name){
//			SftpFile requestedFile=null;
//		    
//		    try {
//		    	
//		    	/*** Call to list all files  in Payments  << call to generic list method >> **/
//		    	System.out.println("Call to list all files in Payments Folder  << call to generic method >> \n");
//		    	ArrayList<SftpFile> fileList=connection.listAllCSVFile(sftp,paymentsFolder);
//				
//		    	//requestedFile=newList.get(0);
//		    	Iterator it=fileList.iterator();
//		    	while(it.hasNext()){
//		    		SftpFile temp = (SftpFile) it.next();
//		    		if(temp.getFilename().contains(name)){
//		    			requestedFile=temp;
//		    			File file=sftp.download(temp.getFilename());
//		    			sftp.setDir("Processed");
//		        		sftp.upload(file,temp.getFilename());
//		                sftp.setDir("/"+paymentsFolder);
//		    			break;
//		    		}
//		    	}
//		    }
//		    catch (Exception ex) {ex.printStackTrace();}
//		    return requestedFile;
//		}	
//		
//		
//		public String findLatestRate(Sftp sftp,String ratesFolder){
//			
//			System.out.println("findLatestRate method starts");
//			System.out.println("Call to get the recent file by name if any << call to local method >>");
//			SftpFile file= findLatestFile(sftp,ratesFolder);
//			if(file!=null){
//				   System.out.println("Call to read the latest file  << call to generic method >>");
//				   contents=(HashMap<Integer, String[]>) connection.readCSVFile(sftp, ratesFolder, file.getFilename());
//				   System.out.println("*****************************");
//				   System.out.println("Rate:£"+  contents.get(1)[5]);
//			   }
//			System.out.println("findLatestRate method ends");
//			return contents.get(1)[2];
//		}
//		
//		public String findLatestStatus(Sftp sftp,String paymentsFolder, String name){
//			
//			System.out.println("findLatestStatus method starts");
//			System.out.println("Call to get the recent file by name if any << call to local method >>");
//			SftpFile file= findRecentCSVFilebyName(sftp,paymentsFolder,name);
//			if(file!=null){
//				   System.out.println("New file added for the case "+name);
//				   System.out.println("Call to read the latest file  << call to generic method >>");
//				   contents=(HashMap<Integer, String[]>) connection.readCSVFile(sftp, paymentsFolder, file.getFilename());
//				   System.out.println("*****************************");
//				   System.out.println("Status:"+  contents.get(1)[2]);
//			   }
//			System.out.println("findLatestStatus method ends");
//			return contents.get(1)[2];
//		}
//	

	// archive all other files to Processed folder**/- EXCEPT NAMED (null)
	
}
