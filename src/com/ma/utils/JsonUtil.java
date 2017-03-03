package com.ma.utils;
/**
 * @author marlboro.chu@gmail.com
 **/
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	static Log log = LogFactory.getLog(JsonUtil.class);
	private static JsonUtil instance;

	private JsonUtil() {
	}

	public synchronized static JsonUtil getInstance() {
		
		if (instance == null){
			instance = new JsonUtil();
		}
		
		return instance;
	}

	public JSONArray sortArrayByNameReverse(JSONArray in, String propertyName) {

		JSONArray out = sortArrayByName(in, propertyName);
		JSONArray rjarray = new JSONArray();
		for (int i = out.length() - 1; i >= 0; i--) {
			try {
				rjarray.put(out.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return out;
			}
		}
		return rjarray;
	}

	public JSONArray sortArrayByName(JSONArray in, String propertyName) {

		JSONArray out = new JSONArray();

		TreeMap tm = new TreeMap();
		int maxLen = 0;
		for (int i = 0; i < in.length(); i++) {
			try {
				JSONObject obj = in.getJSONObject(i);
				String objValue = obj.getString(propertyName);
				if (objValue.length() > maxLen) {
					maxLen = objValue.length();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String temp = "";
		if (maxLen == 0)
			temp = "00000";
		else {
			for (int i = 0; i < maxLen + 5; i++)
				temp += "0";
		}

		for (int i = 0; i < in.length(); i++) {
			try {
				JSONObject obj = in.getJSONObject(i);
				String objValue = obj.getString(propertyName);

				objValue = temp + objValue;
				// System.out.println(objValue);
				objValue = objValue
						.substring(objValue.length() - temp.length());
				// System.out.println(objValue);
				// System.out.println(objValue);
				if (tm.containsKey(objValue)) {
					ArrayList objs = (ArrayList) tm.get(objValue);
					objs.add(obj);
					tm.put(objValue, objs);
				} else {
					ArrayList objs = new ArrayList();
					objs.add(obj);
					tm.put(objValue, objs);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return in;
			}
		}

		Iterator<String> ite = tm.keySet().iterator();
		while (ite.hasNext()) {
			String objKey = ite.next();
			ArrayList objValues = (ArrayList) tm.get(objKey);
			for (int i = 0; i < objValues.size(); i++) {
				out.put((JSONObject) objValues.get(i));
			}
		}

		return out;

	}

	public JSONArray changeFirstObject(JSONArray in, String propertyName,
			String byValue) {

		JSONArray out = new JSONArray();

		try {

			ArrayList temp = new ArrayList();
			JSONObject firstObj = null;
			for (int i = 0; i < in.length(); i++) {
				JSONObject jobject = in.getJSONObject(i);
				if (!jobject.isNull(propertyName)
						&& jobject.getString(propertyName).equals(byValue)
						&& firstObj == null) {
					firstObj = jobject;
				} else {
					temp.add(jobject);
				}
			}
			if (firstObj != null)
				out.put(firstObj);
			for (int i = 0; i < temp.size(); i++) {
				out.put(temp.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;

	}

	// public JSONObject convert(HashMap input){
	// return convert((Map)input);
	// }
	public JSONObject convert(Map input) throws Exception {
		
		
		JSONObject jobject = new JSONObject();

		Iterator ite = input.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			Object value = input.get(key);
			// System.out.println(value.getClass());
			if (value instanceof List) {
				// System.out.println("List");
				jobject.put(key.toLowerCase(), convert((List) value));
			} else if (value instanceof java.util.Date) {
				// System.out.println("Date");
				// jobject.put(key.toLowerCase(),
				// DateUtil.getInstance().formatDate((java.util.Date)value,Constant.KEY_DEFAULT_DATETIME_FORMATE));
				jobject.put(
						key.toLowerCase(),
						DateUtil.getInstance().formatDate(
								(java.util.Date) value,
								"yyyy/MM/dd'\n'HH:mm:ss"));
				jobject.put(
						key.toLowerCase() + "_normal",
						DateUtil.getInstance().formatDate(
								(java.util.Date) value, "yyyy/MM/dd HH:mm:ss"));
			} else if (value == null) {
				// System.out.println("null");
				jobject.put(key.toLowerCase(), "");
			} else if (value instanceof java.lang.String) {
				// System.out.println("String");
				jobject.put(key.toLowerCase(), value.toString());
			} else if (value instanceof java.math.BigDecimal) {
				// System.out.println("String");
				jobject.put(key.toLowerCase(), value.toString());
			} else {
				jobject.put(key.toLowerCase(), convert(value));
			}
		}

		return jobject;

	}

	public JSONObject convert(Object input) throws Exception {
		
		
		JSONObject jobject = new JSONObject();

		Method[] methods = input.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get")) {
				String methodName = methods[i].getName().replaceAll("get", "");
				if (methodName.equalsIgnoreCase("class")) {
					continue;
				}
				Object o = methods[i].invoke(input, null);
				if (methods[i].getReturnType().getSimpleName().equals("Date")) {
					jobject.put(
							methodName.toLowerCase() + "_normal",
							DateUtil.getInstance().formatDate(
									(java.util.Date) o, "yyyy/MM/dd HH:mm:ss"));
					jobject.put(
							methodName.toLowerCase(),
							DateUtil.getInstance().formatDate(
									(java.util.Date) methods[i].invoke(input,
											null), "yyyy/MM/dd'\n'HH:mm:ss"));
				} else if (o instanceof List) {
					//
					jobject.put(methodName.toLowerCase(), convert((List) o));

				} else if (o instanceof Map) {
					jobject.put(methodName.toLowerCase(), convert((Map) o));
				} else {
					jobject.put(methodName.toLowerCase(), o);
				}

			}
		}

		return jobject;
	}

	public JSONArray convert(List o) throws Exception {

		
		JSONArray jarray = new JSONArray();
		List detail = (List) o;
		for (int x = 0; x < detail.size(); x++) {
			if (detail.get(x) instanceof Map)
				jarray.put(convert((Map) detail.get(x)));
			else if (detail.get(x) instanceof List)
				jarray.put(convert((List) detail.get(x)));
			else
				jarray.put(convert(detail.get(x)));
		}
		return jarray;
	}

	public String convert(JSONObject jobject, String html) {

		try {
			if (html == null)
				return null;
			Document doc = XmlUtil.getInstance().toXml(html);
			List nodes = doc.selectNodes("//json");
			for (int i = 0; i < nodes.size(); i++) {

				Element e = (Element) nodes.get(i);
				String jsonKey = e.attributeValue("key");
				e.setName("span");
				e.remove(e.attribute("key"));

				if (!jobject.isNull(jsonKey)) {
					e.setText(jobject.getString(jsonKey));
				}
			}
			String retStr = XmlUtil.getInstance().toString(doc);
			retStr.replaceAll("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
			return retStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public JSONObject convertJJtoJA(JSONObject job) {

		try {
			Iterator ite = job.keys();
			while (ite.hasNext()) {

				String key = (String) ite.next();
				Object o = job.get(key);
				if (o instanceof JSONArray) {
					convertJJtoJA((JSONArray) o);
				} else if (o instanceof JSONObject) {
					JSONArray nja = new JSONArray();
					nja.put(convertJJtoJA((JSONObject) o));
					job.put(key, nja);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return job;
	}

	public JSONArray convertJJtoJA(JSONArray jarray) {

		try {
			for (int i = 0; i < jarray.length(); i++) {

				Object o = jarray.get(i);
				if (o instanceof JSONArray) {
					convertJJtoJA((JSONArray) o);
				} else if (o instanceof JSONObject) {
					convertJJtoJA((JSONObject) o);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jarray;
	}
}
