package com.ma.utils;

import java.io.*;
import java.util.*;
import java.net.URLEncoder;
import org.dom4j.io.*;
import org.dom4j.*;

/**
 * @author marlboro.chu@gmail.com
 **/
public class XmlUtil{
	
	//private org.apache.log4j.Logger logger = Log4jUtil.getInstance().getLogger(com.bull.prj.Constant.getInstance().getApp_log_name());
	private static XmlUtil xu;
	private XmlUtil(){}
	
	public synchronized static XmlUtil getInstance(){
		if(xu == null){
			xu = new XmlUtil();
		}
		return xu;
	}
	/**
	 * �̸��|��� classloader �o xml file
	 **/
	public Document getDoc(String resource_path) throws Exception{
		return getDoc(resource_path,false);	
	}
	/**
	 * �̸��|��� classloader �o xml file
	 * @param resource_path ClassLoader ���|�W��
	 * @param reload true : �� url ��o ; false : �� classloader ��o
	 **/
	public synchronized Document getDoc(String resource_path,boolean reload) throws Exception{
		
		SAXReader saxReader = new SAXReader();
		
		Document doc  = null;
		
		if(reload){
			java.net.URL res_url = Thread.currentThread().getContextClassLoader().getResource(resource_path);
			doc = saxReader.read(res_url);
		}else{
			java.io.InputStream is = Thread.currentThread().
	                getContextClassLoader().getResourceAsStream(resource_path);
	        doc = saxReader.read(is);	  
	        is.close();
	    }
	    return doc;
		
	}
	
	public synchronized Document getDoc(File f) throws Exception{
		
		SAXReader saxReader = new SAXReader();
		
		return saxReader.read(f); 
		
	}
	public synchronized Document getDoc(InputStream is) throws Exception{
		
		SAXReader saxReader = new SAXReader();
		return saxReader.read(is); 
		
	}
	public void writeToFile(Document doc,String resource_path) throws Exception{
		
		java.net.URL res_url = Thread.currentThread().getContextClassLoader().getResource(resource_path);
		
		//System.out.println(res_url.toString());
		
		File f = null;
		try{
			f = new File(new java.net.URI(res_url.toString()));
		}catch(IllegalArgumentException iae){
			f = new File(res_url.toString().replaceAll("file:",""));
		}
		
		
		FileWriter fw = new FileWriter(f, false) ;
		
		try{
			fw.write(toString(doc));
			fw.flush();
		}finally{
			fw.close();
		}
		
	}
	
	public void writeToFile(Document doc,String encodeing,String resource_path) throws Exception{
		
		java.net.URL res_url = Thread.currentThread().getContextClassLoader().getResource(resource_path);
		
		//System.out.println(res_url.toString());
		
		File f = null;
		try{
			f = new File(new java.net.URI(res_url.toString()));
		}catch(IllegalArgumentException iae){
			f = new File(res_url.toString().replaceAll("file:",""));
		}
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter
				  (new FileOutputStream(f),encodeing));
				  
		try{
			out.write(XmlUtil.getInstance().toStringNoFormat(doc));
		}finally{
			out.flush();
			out.close();
		}
		
	}
	
	
	public Hashtable AttToHash(Element ele) throws Exception {
		
		Hashtable ht = new Hashtable();
		List atts = ele.attributes();
		for(int j=0;j<atts.size();j++){
			Attribute att = (Attribute)atts.get(j);
			ht.put(att.getName(),att.getValue());
		}
		return ht;
	}
	/**
	 * �N xml �����ର String
	 **/
	public String toString(Node ele) throws Exception{
		
		OutputFormat format = OutputFormat.createPrettyPrint() ;
		format.setTrimText(false);
		StringWriter sw = new StringWriter();
		XMLWriter writer = new XMLWriter( sw, format );
        writer.write( ele );
        return sw.toString();
	}
	public String toStringNoFormat(Node ele) throws Exception{
		
		OutputFormat format = OutputFormat.createPrettyPrint() ;
		format.setNewlines(false);
		//format.setEncoding("UTF-8");
		format.setXHTML(true);
		StringWriter sw = new StringWriter();
		XMLWriter writer = new XMLWriter( sw, format );
        writer.write( ele );
        return (sw.toString());
	}
	/**
	 * �N String �ର xml ����
	 **/
	public Document toXml(String str) throws Exception{
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("UTF-8");
		StringReader sr = new StringReader(str);
		return saxReader.read(sr);
	}
	/**
	 * �N xml �����ର html string
	 */
	private static String default_encoding = "UTF-8" ;
	public String toHTML(Node ele,String encoding) throws Exception{
		
		if(encoding == null) encoding = default_encoding;
		String urlEncode = URLEncoder.encode(toString(ele),encoding);
		return urlEncode.replaceAll("\\+", "%20");	
	
	}
	/**
	 * ����ƻs��� xml ����
	 **/
	public Document AllClone(Node n) throws Exception{
		return toXml(toString(n));
	}
	
	public Document formatHtml(String html) throws Exception{
		
		int fromIndex = 0;
		do{
			
			fromIndex = html.indexOf("<",fromIndex);
			int endIndex = html.indexOf(" ", fromIndex);
			String tagName = html.substring(fromIndex+1, endIndex);
			
			if(html.indexOf("</"+tagName+">") < 0 ){
				//html.r
			}
			
		}while(fromIndex < html.length());
		return toXml(html);
	}
}