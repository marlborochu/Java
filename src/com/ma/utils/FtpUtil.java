package com.ma.utils;
/**
 * @author marlboro.chu@gmail.com
 **/
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;


public class FtpUtil {

	Log log = LogFactory.getLog(FtpUtil.class);
	
	private static FtpUtil instance;
	public synchronized static FtpUtil getInstance(){
		if(instance == null) { instance = new FtpUtil();}
		return instance;
	}
	public FTPClient getFTPClient(String ip,String userName,String password) throws Exception {
		
		FTPClient ftpClient = new FTPClient();
	
		ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
			public void protocolCommandSent(ProtocolCommandEvent protocolCommandEvent) {
			}
			public void protocolReplyReceived(ProtocolCommandEvent protocolCommandEvent) {
			}
		});
		log.debug("connect to ftp server :"+ip);
		log.debug("connect to ftp server :"+userName);
		log.debug("connect to ftp server :"+password);
		ftpClient.connect(ip);
		
		if (ftpClient.login(userName, password)) {
			log.debug("login ftp server success!!!");
			return ftpClient;
		} else {
			log.error(ftpClient.getReplyString());
			log.debug("login ftp server failed!!!");
			ftpClient.disconnect();
			throw new Exception(ftpClient.getReplyString());
			//return null;
		}
	}
	
	public FTPClient getFTPClient(String ip,int port,String userName,String password) throws Exception {
		
		FTPClient ftpClient = new FTPClient();
	
		ftpClient.addProtocolCommandListener(new ProtocolCommandListener() {
			public void protocolCommandSent(ProtocolCommandEvent protocolCommandEvent) {
			}
			public void protocolReplyReceived(ProtocolCommandEvent protocolCommandEvent) {
			}
		});
		log.debug("connect to ftp server :"+ip);
		log.debug("connect to ftp server :"+userName);
		log.debug("connect to ftp server :"+password);
		ftpClient.connect(ip,port);
		
		if (ftpClient.login(userName, password)) {
			log.debug("login ftp server success!!!");
			return ftpClient;
		} else {
			log.error(ftpClient.getReplyString());
			log.debug("login ftp server failed!!!");
			ftpClient.disconnect();
			throw new Exception(ftpClient.getReplyString());
			//return null;
		}
	}
	
	public void returnFtpClient(FTPClient ftpClient) {
		try {
			if (ftpClient != null) {
				ftpClient.disconnect();
			}
		} catch (FTPConnectionClosedException e) {
			log.error("returnFtpClient error ", e);
		} catch (IOException e) {
			log.error("returnFtpClient error ", e);
		}
	}
	
	
}
