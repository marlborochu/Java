package com.ma.utils;

/**
 * @author marlboro.chu@gmail.com
 **/
import java.security.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class MessageDigestUtil{
	
	private Log logger = LogFactory.getLog(MessageDigestUtil.class);
	private MessageDigestUtil(){}
	private static MessageDigestUtil mdu;
	
	public synchronized static MessageDigestUtil getInstance(){
		if(mdu == null){
			mdu = new MessageDigestUtil();
		}	
		return mdu;
	}
	
	public String getDisgest(String text) throws Exception {
		return getMD5Digest(text);
	}
	
	public String getMD5Digest(String text) throws Exception {
		
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(text.getBytes());
		byte messageDigest[] = algorithm.digest();
		
		
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<messageDigest.length;i++) {
			hexString.append( (0xff & messageDigest[i]) < 16 ? "0" : "");
			hexString.append(Integer.toHexString(0xff & messageDigest[i]));
		}
		return hexString.toString();
		
	}
	
	public String getMD5Base64(String text) throws Exception {
		
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
        //System.out.println(text);
		algorithm.reset();
		algorithm.update(text.getBytes());
		byte messageDigest[] = algorithm.digest();
		return org.apache.commons.codec.binary.Base64.encodeBase64String(messageDigest);
		//return hexString.toString();
		
	}
	
	public String getSHA256(String text) throws Exception {
		
		MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
		algorithm.reset();
		algorithm.update(text.getBytes());
		byte messageDigest[] = algorithm.digest();
		
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<messageDigest.length;i++) {
			hexString.append( (0xff & messageDigest[i]) < 16 ? "0" : "");
			hexString.append(Integer.toHexString(0xff & messageDigest[i]));
		}
		return hexString.toString();
		
		/*
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(messageDigest);
		*/
	}
	public String getSHA(String text) throws Exception {
		
		MessageDigest algorithm = MessageDigest.getInstance("SHA-1");
		algorithm.reset();
		algorithm.update(text.getBytes());
		byte messageDigest[] = algorithm.digest();
		
		StringBuffer hexString = new StringBuffer();
		for (int i=0;i<messageDigest.length;i++) {
			hexString.append( (0xff & messageDigest[i]) < 16 ? "0" : "");
			hexString.append(Integer.toHexString(0xff & messageDigest[i]));
		}
		return hexString.toString();
		
		/*
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(messageDigest);
		*/
	}
	
	
	
	public static void main(String args[]){
		try{
		    /*
            System.out.println(getInstance().getSHA256(args[0]));            
			System.out.println("key1 :"+ new com.infosys.bankaway.util.CryptManager().encrypt("KBL") );
			System.out.println("key2 :"+ "qlkmuihynmlkdfo" );
			//String mKey = new com.infosys.bankaway.util.CryptManager().encrypt("KBL")+"\n"+"qlkmuihynmlkdfo";
			*/
			String mKey = "A3<"+"\n"+"qlkmuihynmlkdfo";
			String[] passwrods = {"abn@12385732097.85732097","abn@12314542337.14542337","abn@12359216646.59216646","abn@12385884376.85884376","abn@12316315723.16315723","abn@12303563600.03563600","abn@12319741211KW.19741211KW","abn@12303043101.03043101","abn@12319791121AN.19791121AN","abn@12385760591.85760591"};
			/*
			String mypassword = "qwe$12";
			String myid = "1964082918";
			               
			String myMDPwd = mypassword+myid+"."+myid+mKey;
			
			String result = getInstance().getMD5Base64(myMDPwd);              
			System.out.println(result.substring(0,result.length()-2));
			*/
			/*
			for(int i=0;i<myMDPwd.length();i++){
				
				String testpwd = myMDPwd.substring(myMDPwd.length()-i,myMDPwd.length());				
				String result = getInstance().getMD5Base64(testpwd);              
				System.out.println(result.substring(0,result.length()-2));
							
			}
			*/
			//com.infosys.bankaway.util.MD5 myMD5 = new com.infosys.bankaway.util.MD5();
			//byte[] mdbytes = myMD5.GetMD5Bytes(args[0]+mKey);
			//System.out.println( com.infosys.bankaway.util.Base64EncoderDecoder.encode(mdbytes) );
			
			for(int i=0;i<passwrods.length;i++){
				
				//MessageDigest algorithm = MessageDigest.getInstance(Constant.MD5_MESSAGE_DIGEST_NAME);
				//algorithm.reset();
				//algorithm.update((passwrods[i]+mKey).getBytes());
				//byte messageDigest[] = algorithm.digest();
				
				//String result = new sun.misc.BASE64Encoder().encode(messageDigest);
				
               
				String result = getInstance().getMD5Base64(passwrods[i]+mKey);              
				System.out.println(result.substring(0,result.length()-2));
                //System.out.println( getInstance().getMD5Base64( passwrods[i]+mKey ) );
                
                
                //System.out.println(getInstance().getMD5Digest(passwrods[i]+mKey));
			}	
				
			
			
			/*
			DesEncrypter de = new DesEncrypter();
			de.setDefaultProvateKey("A210259143");
			*/
			
			//System.out.println("md5 :"+getInstance().getMD5Digest(args[0]));
			//System.out.println("md5+des :"+de.encrypt(getInstance().getMD5Digest(args[0])));
			System.out.println("sha256 :"+getInstance().getSHA256("SYSTEM"));
			//System.out.println("sha :"+getInstance().getSHA(args[0]));
			
		}catch(Exception e){e.printStackTrace();}
	}
	
	public int getRandom(int min,int max){
		 return min + (int) ((1 + max - min) * Math.random());
	}
}
