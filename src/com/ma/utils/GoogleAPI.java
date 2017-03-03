package com.ma.utils;
/**
 * @author marlboro.chu@gmail.com
 **/
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.dom4j.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GoogleAPI {

	public final static String GOOGLE_TRANSLATE_NAME = "Google Translate";
	public final static String GOOGLE_TRANSLATE_API = "http://translate.google.com.tw/translate_a/t?";
	public final static String GOOGLE_TRANSLATE_SAMPLE_API = "http://translate.google.com.tw/translate_a/single?";
	                                                       
	private String originalLanguage;

	private String transLanguage;
	private String transCountry;

	private static GoogleAPI google;

	public static synchronized GoogleAPI getInstance() {
		if (google == null) {
			google = new GoogleAPI();
		}
		return google;
	}

	public String translate(String text) {
		return translate(text, false);
	}

	public String translate(String text, boolean autoTestLang) {

		try {

			String oriLanguage = originalLanguage;
			if (oriLanguage == null)
				oriLanguage = "en";

			String transLang = transLanguage;
			if (transCountry != null && !transCountry.trim().equals(""))
				transLang = transLanguage + "-" + transCountry;

			HashMap hm = new HashMap();

			hm.put("client", "t");
			hm.put("text", URLEncoder.encode(text, "UTF-8"));
			hm.put("hl", transLang);
			hm.put("sl", oriLanguage);
			hm.put("tl", transLang);
			hm.put("ie", "UTF-8");
			hm.put("oe", "UTF-8");
			hm.put("multires", "1");
			hm.put("otf", "1");
			hm.put("ssel", "0");
			hm.put("tsel", "0");
			hm.put("sc", "1");

			String url = GOOGLE_TRANSLATE_API
					+ HttpConnection.getInstance().toParameter(hm);
			String result = HttpConnection.getInstance().request(url, "UTF-8",
					true);
			
			// test original language
			if (autoTestLang) {
				try {
					JSONArray jarray = new JSONArray(result);
					if (!jarray.isNull(8)) {
						String googleOriLang = jarray.getJSONArray(8)
								.getJSONArray(0).getString(0);
						if (!oriLanguage.equalsIgnoreCase(googleOriLang
								.substring(0, 2))) {
							originalLanguage = googleOriLang.substring(0, 2);
							if (originalLanguage.equals("zh")) {
								originalLanguage = "zh-TW";
							}
							return translate(text);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public String translateSample(String text, String utrans) {

		try {

			String oriLanguage = originalLanguage;
			if (oriLanguage == null)
				oriLanguage = "en";

			String transLang = transLanguage;
			if (transCountry != null && !transCountry.trim().equals(""))
				transLang = transLanguage + "-" + transCountry;

			HashMap hm = new HashMap();

			hm.put("client", "t");
			hm.put("sl", oriLanguage);
			hm.put("tl", transLang);
			hm.put("hl", transLang);
			hm.put("dt", "ex");
			hm.put("q", URLEncoder.encode(text, "UTF-8"));
			
			String url = GOOGLE_TRANSLATE_SAMPLE_API
					+ HttpConnection.getInstance().toParameter(hm);
//			System.out.println("================"+url);
			String result = HttpConnection.getInstance().request(url, "UTF-8",
					true);

//			System.out.println(result);

			return result;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

	public String getTransLanguage() {
		return transLanguage;
	}

	public void setTransLanguage(String transLanguage) {
		this.transLanguage = transLanguage;
	}

	public String getTransCountry() {
		return transCountry;
	}

	public void setTransCountry(String transCountry) {
		this.transCountry = transCountry;
	}

	public String getOriginalLanguage() {
		return originalLanguage;
	}

	public void setOriginalLanguage(String originalLanguage) {
		this.originalLanguage = originalLanguage;
	}

	public Document queryGoogleAddress(String address) throws Exception {

		String url = "http://maps.google.com/maps/api/geocode/xml?sensor=false&address="
				+ URLEncoder.encode(address, "UTF-8");
		HashMap header = new HashMap();
		header.put("Accept-Language", Locale.TRADITIONAL_CHINESE.toString());
		String result = HttpConnection.getInstance().request(url, "UTF-8",
				header);
		return XmlUtil.getInstance().toXml(result);
		// return null;
	}

	public static void main(String args[]) {

//		GoogleAPI.getInstance().setOriginalLanguage("en");
//		GoogleAPI.getInstance().setTransLanguage("zh");
//		GoogleAPI.getInstance().setTransCountry("TW");
//		String result = GoogleAPI.getInstance().translate("teacher", false);
		
		try {

			// System.out.println(new
			// JSONArray(result).getJSONArray(0).getJSONArray(0).get(0));
			// System.out.println(new
			// JSONArray(result).getJSONArray(1).getJSONArray(0).get(1));
			// System.out.println(result);
			// JSONArray jarray = new JSONArray(result);
			// for(int i = 0;i<jarray.length();i++){
			// if(!jarray.isNull(i))
			// System.out.println(i+":"+jarray.get(i));
			// }
			// System.out.println(jarray.getJSONArray(8).getJSONArray(0).getString(0));
			// String trans = "";
			// for(int i = 0;i<jarray.getJSONArray(0).length();i++){
			// trans +=
			// jarray.getJSONArray(0).getJSONArray(i).get(1).toString();
			// }
			//
			// System.out.println(trans);
			Document doc = GoogleAPI.getInstance().queryGoogleAddress("南投縣埔里鎮中華路30號");
			
			System.out.println(XmlUtil.getInstance().toString(doc));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
