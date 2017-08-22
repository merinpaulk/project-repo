package AppLaunch;

import com.jscape.inet.sftp.Sftp;
import com.jscape.inet.sftp.SftpException;
import com.jscape.inet.ssh.util.SshParameters;

public class Test {

	public static void main(String[] args) throws SftpException {
		
		final String sftpURL = "162.13.14.125";
		final String username = "moneycorp";
		final String password = "P4t3ntFX79";
		
		/*final String sftpURL = "securefile.moneycorp.com";
		final String username = "avidity-cfx";
		final String password = "2Gc9jheJ";*/
		
		SshParameters params = new SshParameters(sftpURL,username,password);    		
		Sftp ftp = new Sftp(params);
                
        // Connect, upload PNG images and release the connection.
        ftp.connect();        
        ftp.mupload(".*\\.png");
        ftp.disconnect();

	}

}
