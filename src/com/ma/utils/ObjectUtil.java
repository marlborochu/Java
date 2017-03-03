package com.ma.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @author marlboro.chu@gmail.com
 **/
public class ObjectUtil {
	
	private static ObjectUtil ou;
	
	public synchronized static ObjectUtil getInstance(){
		if(ou == null) ou = new ObjectUtil();
		return ou;
	}
	
	public void writeFile(String fileName, Serializable o) throws Exception{
		
		ObjectOutput output = null;
		try{
			File f = new File(fileName);
			if(!f.getParentFile().exists()){ f.getParentFile().mkdirs(); }
//			System.out.println("================="+fileName);
			OutputStream file = new FileOutputStream(fileName);
		    OutputStream buffer = new BufferedOutputStream(file);
		    output = new ObjectOutputStream(buffer);
		    output.writeObject(o);
		    
		}finally{
			if(output != null){
				output.flush();
				output.close();
			}
		}
	}
	
	public Serializable readFile(String fileName) throws Exception{
		
		ObjectInput input = null;
		try{
			
			InputStream file = new FileInputStream(fileName);
			InputStream buffer = new BufferedInputStream(file);
			input = new ObjectInputStream(buffer);
		    Object o = input.readObject();
		    return (Serializable)o;
		    
		}finally{
			input.close();
		}
	}
	public Serializable readFile(File f) throws Exception{
		
		ObjectInput input = null;
		try{
			
			InputStream file = new FileInputStream(f);
			InputStream buffer = new BufferedInputStream(file);
			input = new ObjectInputStream(buffer);
		    Object o = input.readObject();
		    return (Serializable)o;
		    
		}finally{
			input.close();
		}
	}
	
	public void clone(Object from,Object to){
		
		try{
			
			Method[] methods = from.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith("get")) {
					String methodName = methods[i].getName().replaceAll("get", "");
					if (methodName.equalsIgnoreCase("class")) {
						continue;
					}
					Object input = methods[i].invoke(from, null);
					if(input != null){
						Method toMethod = to.getClass().getMethod("set"+methodName, input.getClass());
						toMethod.invoke(to, input);
					}
				}
			}
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public Object getValue(Object from,String method){
		
		try{
			
			Method[] methods = from.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith("get")) {
					String methodName = methods[i].getName().replaceAll("get", "");
					if(methodName.toLowerCase().equals(method.toLowerCase())){
						Object input = methods[i].invoke(from, null);
						return input;
					}
					
				}
			}
		}catch(Exception e){e.printStackTrace();}
		return null;
	}
	
	public void toString(Object input){
		
		
		try{
			
			Method[] methods = input.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().startsWith("get")) {
					String methodName = methods[i].getName().replaceAll("get", "");
					if (methodName.equalsIgnoreCase("class")) {
						continue;
					}
					try {
						//System.out.println(methods[i].getReturnType().getSimpleName());
						if(methods[i].getReturnType().getSimpleName().equals("Date")){
							System.out.println(methodName.toLowerCase()+":"+DateUtil.getInstance().formatDate(
									(java.util.Date)methods[i].invoke(input, null),
									"yyyy/MM/dd"));
						}else{
							System.out.println(methodName.toLowerCase()+":"+methods[i].invoke(input, null));
						}	
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}catch(Exception e){e.printStackTrace();}
		
	}
	
	public void saveFTPFile(String remoteFileName,String localFileName,String encoding){
		
		try{
			
			ResourceBundle resource = 
	                ResourceBundle.getBundle("com/ma/utils/resource", Locale.ENGLISH);
			String infos = resource.getString("ftp.infos");
			String dInfos = new DesEncrypter().decrypt(infos);
			String[] dInfosa = dInfos.split("\\{DES\\}");
			FTPClient client = 
					FtpUtil.getInstance().getFTPClient(dInfosa[0],dInfosa[1],dInfosa[2]);
//		
			client.enterLocalPassiveMode();
	        client.setFileType(FTPClient.BINARY_FILE_TYPE);
	        
			client.setControlEncoding(encoding);
			String[] pathNames = null;
			
			if(remoteFileName.indexOf("\\\\") >= 0){
				pathNames = remoteFileName.split("\\\\");
			}else{
				pathNames = remoteFileName.split("/");
			}
			String pathName = "";
			for(int z = 0;z<pathNames.length-1;z++){
				pathName = pathName+pathNames[z]+"/";
				client.makeDirectory(pathName);
			}
			
			File ftpFile = new File(localFileName);
			FileInputStream fis = new FileInputStream(ftpFile);
			String ftpFileName = remoteFileName.replaceAll("\\\\", "/");
			ftpFileName = new String(ftpFileName.getBytes(encoding),"ISO-8859-1");
			
			client.storeFile(ftpFileName, fis);

			fis.close();
			client.disconnect();
			
		}catch(Exception e1){e1.printStackTrace();}
		
	}
	
	public String objectToString(Object o) throws Exception{
		
	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.flush();
        oos.close();
        String s =  org.apache.commons.codec.binary.Base64.encodeBase64String(baos.toByteArray()  );
        DesEncrypter des = new DesEncrypter();
        String defaultKey = des.getDefaultProvateKey();
        defaultKey += DateUtil.getInstance().formatDate(new Date(),"yyyyMMdd");
        des.setDefaultProvateKey(defaultKey);
        s = des.encrypt(s);
        return s;
        
	}
	public Object stringToObject(String s) throws Exception{
		
		DesEncrypter des = new DesEncrypter();
        String defaultKey = des.getDefaultProvateKey();
        defaultKey += DateUtil.getInstance().formatDate(new Date(),"yyyyMMdd");
        des.setDefaultProvateKey(defaultKey);
        s = des.decrypt(s);
		
		byte [] data = org.apache.commons.codec.binary.Base64.decodeBase64(s);
        ObjectInputStream ois = new ObjectInputStream( 
                                        new ByteArrayInputStream(  data ) );
        Object o = ois.readObject();
        ois.close();
        return o;
        
	}
}
