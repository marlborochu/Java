package com.ma.utils;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Base64;

/**
 * @author marlboro.chu@gmail.com
 **/

public class DesEncrypter {
	
    
    javax.crypto.Cipher dcipher;
	private String defaultPrivateKey = "12345678900987654321qwertyuiopasdfghjklzxcvbnm";
	
	// desede is DESede    	
	private final String defaultAlgorithm = "DES";
	
	private String algorithmType ;
	
	public void setDefaultProvateKey(String defaultPrivateKey){
		this.defaultPrivateKey = defaultPrivateKey;
	}
	public String getDefaultProvateKey(){
		return this.defaultPrivateKey;
	}
    public DesEncrypter() {
       this.algorithmType = defaultAlgorithm;
    }
	
	public DesEncrypter(String algorithm) {
       this.algorithmType = algorithm;
    }
    
	private SecretKey getDESSecretKey() throws Exception{
		
		DESKeySpec desk = new DESKeySpec(defaultPrivateKey.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithmType);
        SecretKey sk = skf.generateSecret(desk);
        return sk;
        
	}
	private SecretKey getTripleDESSecretKey() throws Exception{
		
		DESedeKeySpec desk = new DESedeKeySpec(defaultPrivateKey.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithmType);
        SecretKey sk = skf.generateSecret(desk);
        return sk;
        
	}
    public String encrypt(String str) {
        try {
        	
        	javax.crypto.Cipher ecipher = javax.crypto.Cipher.getInstance(algorithmType);
        	if(defaultAlgorithm.equals(algorithmType)){
        		ecipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getDESSecretKey());
        	}else{
        		ecipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getTripleDESSecretKey());
        	}
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
//            return new sun.misc.BASE64Encoder().encode(enc);
            return Base64.encodeBase64String(enc);
           
        } catch (Exception e) {
        }
        return null;
    }

    public String decrypt(String str) {
        try {
            
            javax.crypto.Cipher dcipher = javax.crypto.Cipher.getInstance(algorithmType);
            
            if(defaultAlgorithm.equals(algorithmType)){
        		dcipher.init(javax.crypto.Cipher.DECRYPT_MODE, getDESSecretKey());
        	}else{
        		dcipher.init(javax.crypto.Cipher.DECRYPT_MODE, getTripleDESSecretKey());
        	}
            // Decode base64 to get bytes
//            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] dec = Base64.decodeBase64(str);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        } catch (Exception e) {
        }
        return null;
    }

}
