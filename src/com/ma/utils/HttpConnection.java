/**
 * @author marlboro.chu@gmail.com
 * */
package com.ma.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

//import android.util.Log;

public class HttpConnection {

	private static HttpConnection myHttp;
	private String method = "GET";
	private String encoding = "UTF-8";

	public synchronized static HttpConnection getInstance() {
		if (myHttp == null) {
			myHttp = new HttpConnection();
			return myHttp;
		}
		return myHttp;
	}

	public String request(String url) throws Exception {
		return request(url, "UTF-8", false);
	}

	public String request(String url, boolean addHeader) throws Exception {
		return request(url, "UTF-8", addHeader);
	}

	public String request(String url, String encoding, boolean addHeader)
			throws Exception {

		HttpParams httpparams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, 1000 * 30);
		HttpConnectionParams.setSoTimeout(httpparams, 1000 * 30);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);
		HttpGet method = null;
		HttpResponse httpResponse = null;

		try {

			// HttpPost method = new HttpPost(url);;
			method = new HttpGet(url);
			if (addHeader) {
				method.setHeader("Content-Type", "text/html;charset=UTF-8");
				method.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
				method.setHeader("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				method.setHeader("Accept-Charset", "" + encoding
						+ ",utf-8;q=0.7,*;q=0.3");
			}
			httpResponse = httpclient.execute(method);

			// System.out.println(httpResponse.getStatusLine().toString());

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();
				try {
					// System.out.println(EntityUtils.toString(resentity,
					// encoding));
					return EntityUtils.toString(resentity, encoding);
					// sb.append(EntityUtils.toString(resentity, encoding));
				} finally {
					// resentity.getContent().close();
				}
			} else {
				// Log.e("Marlboro", httpResponse.getStatusLine().toString());
			}

		} finally {

			try {
				method.abort();
			} catch (Exception e) {
			}
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
			// httpclient = null;
		}
		return null;
		// return sb.toString();
	}

	public String requestHttps(String url) throws Exception {
		HttpClient httpclient = this.getHttpsClient();
		try {
			return request(httpclient, url, "UTF-8", true);
		} finally {

			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
			// httpclient = null;
		}
	}

	public String request(org.apache.http.client.HttpClient httpclient,
			String url, String encoding, boolean addHeader) throws Exception {

		try {
			HttpResponse httpResponse = null;
			HttpGet method = new HttpGet(url);
			if (addHeader) {
				method.setHeader("Content-Type", "text/html;charset=UTF-8");
				method.setHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
				method.setHeader("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				method.setHeader("Accept-Charset", "Big5,utf-8;q=0.7,*;q=0.3");
			}
			httpResponse = httpclient.execute(method);

			// System.out.println(httpResponse.getStatusLine().toString());

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();
				return EntityUtils.toString(resentity, encoding);

			}

		} finally {

		}
		return null;
		// return sb.toString();
	}

	public String request(String url, String encoding) throws Exception {
		HashMap header = new HashMap();
		header.put("Content-Type", "text/html;charset=UTF-8");
		header.put(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
		header.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		header.put("Accept-Charset", "Big5,utf-8;q=0.7,*;q=0.3");
		return request(url, encoding, header);
	}

	public String requestUseProxy(String url, String encoding) throws Exception {

		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 1000 * 5;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 1000 * 10;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		ArrayList proxys = getProxyList();
		String tempProxy = "";
		if (new Random().nextInt(10) % 2 == 0) {
			do {
				tempProxy = (String) proxys.get(new Random().nextInt(proxys
						.size() - 1));
			} while (exceptionProxy.contains(tempProxy));
		}

		if (!tempProxy.equals("")) {
			System.out.println("proxy:" + tempProxy);
			HttpHost proxy = new HttpHost(tempProxy.split(":")[0],
					Integer.valueOf(tempProxy.split(":")[1]), "http");
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		try {
			try {
				HttpResponse httpResponse = null;

				HttpGet method = new HttpGet(url);
				httpResponse = httpclient.execute(method);

				if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

					HttpEntity resentity = httpResponse.getEntity();
					return EntityUtils.toString(resentity, encoding);

				}

			} finally {
				try {
					httpclient.getConnectionManager().shutdown();
				} catch (Exception e) {
				}
			}
		} catch (SocketException se) {
			exceptionProxy.add(tempProxy);
			return requestUseProxy(url, encoding);
		} catch (NoHttpResponseException nre) {
			exceptionProxy.add(tempProxy);
			return requestUseProxy(url, encoding);
		} catch (org.apache.http.conn.ConnectTimeoutException nre) {
			exceptionProxy.add(tempProxy);
			return requestUseProxy(url, encoding);
		} catch (SocketTimeoutException ste) {
			exceptionProxy.add(tempProxy);
			return requestUseProxy(url, encoding);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public String request(String url, String encoding, HashMap header)
			throws Exception {

		HttpParams httpparams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpparams, 15000);
		HttpConnectionParams.setSoTimeout(httpparams, 20000);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);

		try {
			HttpResponse httpResponse = null;

			HttpGet method = new HttpGet(url);
			if (header != null) {
				Iterator ite = header.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					String value = (String) header.get(key);
					method.setHeader(key, value);
				}
			}
			httpResponse = httpclient.execute(method);

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();
				return EntityUtils.toString(resentity, encoding);

			}

		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
		return null;
		// return sb.toString();
	}

	public String request(String[] urls) throws Exception {
		HttpParams httpparams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
		HttpConnectionParams.setSoTimeout(httpparams, 10000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);

		try {
			return request(httpclient, urls, "UTF-8", true);
		} finally {
			// httpclient = null;
		}
	}

	public String request(DefaultHttpClient httpclient, String[] urls,
			String encoding, boolean addHeader) throws Exception {

		try {
			HttpResponse httpResponse = null;
			String result = null;

			for (int i = 0; i < urls.length; i++) {
				// System.out.println(urls[i]);
				HttpGet method = new HttpGet(urls[i]);

				if (addHeader) {
					method.setHeader("Content-Type", "text/html;charset=UTF-8");
					method.setHeader(
							"User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4");
					method.setHeader("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					method.setHeader("Accept-Charset",
							"Big5,utf-8;q=0.7,*;q=0.3");
					method.setHeader("Referer", urls[0]);

				}

				httpResponse = httpclient.execute(method);

				// System.out.println(httpResponse.getStatusLine().toString());

				if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

					HttpEntity resentity = httpResponse.getEntity();
					result = EntityUtils.toString(resentity, encoding);
					// System.out.println(result);
					// sb.append(EntityUtils.toString(resentity, encoding));

				} else {
					// Log.e("Marlboro",
					// httpResponse.getStatusLine().toString());
				}

			}
			// System.out.println(result);
			// System.out.println("====================");
			return result;
		} finally {

		}
		// return sb.toString();
	}

	public String requestPost(String url, Map parameter) throws Exception {

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			return requestPost(httpclient, url, parameter, encoding, null);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
	}

	private ArrayList exceptionProxy = new ArrayList();

	public String requestPostUseProxy(String url, Map parameter)
			throws Exception {

		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 1000 * 5;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 1000 * 10;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		ArrayList proxys = getProxyList();
		String tempProxy = "";
		if (new Random().nextInt(10) % 2 == 0) {
			do {
				tempProxy = (String) proxys.get(new Random().nextInt(proxys
						.size() - 1));
			} while (exceptionProxy.contains(tempProxy));
		}

		if (!tempProxy.equals("")) {
			System.out.println("proxy:" + tempProxy);
			HttpHost proxy = new HttpHost(tempProxy.split(":")[0],
					Integer.valueOf(tempProxy.split(":")[1]), "http");
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}
		try {
			return requestPost(httpclient, url, parameter, encoding, null);
		} catch (SocketException se) {
			exceptionProxy.add(tempProxy);
			return requestPostUseProxy(url, parameter);
		} catch (NoHttpResponseException nre) {
			exceptionProxy.add(tempProxy);
			return requestPostUseProxy(url, parameter);
		} catch (org.apache.http.conn.ConnectTimeoutException nre) {
			exceptionProxy.add(tempProxy);
			return requestPostUseProxy(url, parameter);
		} catch (SocketTimeoutException ste) {
			exceptionProxy.add(tempProxy);
			return requestPostUseProxy(url, parameter);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}

	}

	private ArrayList getProxyList() {
		ArrayList result = new ArrayList();
		try {
			String[] proxyList = { "12.179.129.205:8080", "119.18.145.69:8080",
					"122.96.59.102:81", "221.7.11.102:83",
					"218.18.39.111:9000", "211.142.236.137:81",
					"221.210.40.150:8080", "178.169.169.50:8080",
					"221.130.29.184:8888", "201.73.245.178:3128",
					"46.248.58.62:8000", "216.235.15.24:80",
					"218.108.85.59:80", "218.108.232.190:843",
					"122.96.59.106:82", "190.216.26.194:8080",
					"94.200.108.10:3128", "217.12.113.67:443",
					"124.129.30.74:8080", "218.108.232.189:843",
					"190.24.10.122:8080", "91.214.84.110:3128",
					"202.137.21.196:8080", "60.210.10.59:80",
					"111.119.226.129:8080", "188.168.82.131:3128",
					"200.75.51.148:8080", "203.146.82.253:80",
					"200.195.33.3:3128", "123.183.217.135:80",
					"114.141.49.181:3128", "109.175.8.61:8080",
					"202.62.75.170:8080", "94.154.31.16:8090",
					"146.185.62.38:8080", "221.7.11.11:82",
					"218.108.192.205:843", "61.55.141.10:81",
					"89.110.41.165:8080", "194.141.96.11:8080",
					"218.108.85.59:81", "193.19.145.195:3128",
					"221.207.254.11:18186", "202.47.72.203:8080",
					"201.49.209.148:3128", "58.20.127.100:3128",
					"218.60.6.107:8080", "58.20.127.106:3128",
					"60.235.27.59:81", "221.10.40.232:83"

			};

			for (int i = 0; i < proxyList.length; i++) {
				if (!exceptionProxy.contains(proxyList[i]))
					result.add(proxyList[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String requestPost(String[] url, Map parameter) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();

		try {
			for (int i = 0; i < url.length - 1; i++) {
				requestPost(httpclient, url[i], parameter, encoding, null);
			}
			return requestPost(httpclient, url[url.length - 1], parameter,
					encoding, null);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
	}

	public String requestPost(String url, Map parameter, String encoding)
			throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			return requestPost(httpclient, url, parameter, encoding, null);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
	}

	public String requestPost(String url, Map parameter, String encoding,
			HashMap header) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			return requestPost(httpclient, url, parameter, encoding, header);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
	}

	public String requestPost(String[] url, Map parameter, String encoding,
			HashMap header) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			for (int i = 0; i < url.length - 1; i++) {
				requestPost(httpclient, url[i], parameter, encoding, header);

			}
			return requestPost(httpclient, url[url.length - 1], parameter,
					encoding, header);
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
	}

	public String requestPost(DefaultHttpClient httpclient, String url,
			Map parameter, String encoding, HashMap header) throws Exception {

		try {

			HttpResponse httpResponse = null;
			// DefaultHttpClient httpclient = new DefaultHttpClient();

			HttpPost method = new HttpPost(url);
			if (header != null) {
				Iterator ite = header.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					String value = (String) header.get(key);
					method.setHeader(key, value);
				}
			}

			ArrayList<NameValuePair> pairList = new ArrayList<NameValuePair>();
			if (parameter != null) {
				Iterator ite = parameter.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					String value = (String) parameter.get(key);
					pairList.add(new BasicNameValuePair(key, value));
				}
			}
			method.setEntity(new UrlEncodedFormEntity(pairList, encoding));

			httpResponse = httpclient.execute(method);

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();

				return (new String(EntityUtils.toByteArray(resentity), encoding));

			}

		} finally {

		}

		return null;
	}

	public String getCookie(String url) throws Exception {

		try {

			HttpResponse httpResponse = null;
			HttpParams httpparams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
			HttpConnectionParams.setSoTimeout(httpparams, 10000);

			DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);

			HttpGet method = new HttpGet(url);

			httpResponse = httpclient.execute(method);

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {
				return httpclient.getCookieStore().getCookies().toString();
			}

		} finally {

		}
		return null;
		// return sb.toString();
	}

	public synchronized String requestSocket(String url) throws Exception {

		URL u = new URL(url);
		try {
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			if (this.method != null) {
				conn.setRequestMethod(this.method.toUpperCase());
			}

			conn.setUseCaches(false);

			// conn.connect();
			InputStream in = conn.getInputStream();
			BufferedReader d = new BufferedReader(new InputStreamReader(in,
					this.encoding));
			try {
				String contextLine = "";
				StringBuffer context = new StringBuffer();
				while ((contextLine = d.readLine()) != null) {
					context.append(contextLine);
				}

				return context.toString();
			} finally {
				d.close();
				in.close();
				conn.disconnect();
			}
			// }catch(java.net.SocketTimeoutException e){
		} catch (Exception e) {
			e.printStackTrace();
			return requestSocket(url);
			// e.printStackTrace();

		}
		// return null;
	}

	public Map parseParameter(String url) {

		try {
			HashMap map = new HashMap();
			String parameter = url.split("\\?")[1];
			String[] parameters = parameter.split("&");
			for (int i = 0; i < parameters.length; i++) {
				String[] keyValue = parameters[i].split("=");
				if (keyValue.length > 1)
					map.put(keyValue[0], keyValue[1]);
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String toParameter(HashMap<String, String> hm) {

		StringBuffer sb = new StringBuffer();
		Iterator ite = hm.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			String value = hm.get(key);
			sb.append(key + "=" + value + "&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public String beginHttpXmlAction(
			org.apache.http.client.HttpClient httpclient, String url,
			HashMap header, String context) throws Exception {

		HttpResponse httpResponse = null;

		HttpPost httppost = new HttpPost(url);

		Iterator ite = header.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			String value = (String) header.get(key);
			httppost.setHeader(key, value);
		}

		HttpEntity entity = new StringEntity(context, "UTF-8");
		httppost.setEntity(entity);

		httpResponse = httpclient.execute(httppost);

		if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {
			HttpEntity result = httpResponse.getEntity();
			String strResult = EntityUtils.toString(result);

			return strResult;
		}

		return null;
	}
	
	public String beginHttpXmlAction( String url,
			HashMap header, String context) throws Exception {

		HttpResponse httpResponse = null;

		HttpPost httppost = new HttpPost(url);

		Iterator ite = header.keySet().iterator();
		while (ite.hasNext()) {
			String key = (String) ite.next();
			String value = (String) header.get(key);
			httppost.setHeader(key, value);
		}

		HttpEntity entity = new StringEntity(context, "UTF-8");
		httppost.setEntity(entity);
		
		HttpParams httpparams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
		HttpConnectionParams.setSoTimeout(httpparams, 10000);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);
		
		httpResponse = httpclient.execute(httppost);

		if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {
	
			HttpEntity result = httpResponse.getEntity();
			System.out.println(result.getContentLength());
			String strResult = EntityUtils.toString(result);

			return strResult;
		}

		return null;
	}
	
	public String request(String url, String encoding, HashMap header,
			Boolean redirectHandler) throws Exception {

		HttpParams httpparams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
		HttpConnectionParams.setSoTimeout(httpparams, 10000);

		DefaultHttpClient httpclient = new DefaultHttpClient(httpparams);
		httpclient.setRedirectHandler(new MyRedirectHandler());

		try {
			HttpResponse httpResponse = null;

			HttpGet method = new HttpGet(url);
			if (header != null) {
				Iterator ite = header.keySet().iterator();
				while (ite.hasNext()) {
					String key = (String) ite.next();
					String value = (String) header.get(key);
					method.setHeader(key, value);
				}
			}
			httpResponse = httpclient.execute(method);

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();
				return EntityUtils.toString(resentity, encoding);

			}

		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}
		return null;
		// return sb.toString();
	}

	public static void main(String args[]) {

		try {
			/*
			 * System.out.println( "=============="+
			 * HttpConnection.getInstance()
			 * .requestSocket("http://edition.cnn.com/WORLD/asiapcf/archive/"));
			 */

			// String result = HttpConnection.getInstance().requestSocket(
			// "http://edition.cnn.com/WORLD/asiapcf/archive/");
			// System.out.println(result);

			// System.out.println(HttpConnection.getInstance().parseParameter(
			// "http://test?123=345678"));
			System.out.println(new HttpConnection().getProxyList());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class MyRedirectHandler implements RedirectHandler {

		public URI getLocationURI(HttpResponse res, HttpContext arg1)
				throws ProtocolException {
			System.out.println(res.getAllHeaders());
			return null;
		}

		public boolean isRedirectRequested(HttpResponse res, HttpContext arg1) {
			// TODO Auto-generated method stub
			System.out.println(res.getAllHeaders());
			org.apache.http.Header[] header = res.getAllHeaders();
			for (int i = 0; i < header.length; i++) {
				System.out.println(header[i].getName() + ":"
						+ header[i].getValue());
			}

			return false;
		}

	}

	public String requestPDF(String url) throws Exception {

		HttpClient httpclient = getHttpsClient();

		try {

			HttpGet method = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(method);

			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();

				PDFParser parser = new PDFParser(resentity.getContent());
				parser.parse();
				PDFTextStripper stripper = new PDFTextStripper();
				PDDocument doc = parser.getPDDocument();
				try {
					String text = stripper.getText(doc);
					return text;
				} finally {
					doc.close();
				}
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}

		return null;
	}
	
	public Workbook requestXls(String url) throws Exception {

		HttpClient httpclient = getHttpsClient();

		try {

			HttpGet method = new HttpGet(url);
			HttpResponse httpResponse = httpclient.execute(method);
			
			if (httpResponse.getStatusLine().toString().indexOf("200") != -1) {

				HttpEntity resentity = httpResponse.getEntity();

				Workbook workb = WorkbookFactory.create(resentity.getContent());
				return workb;
				
			}
		} finally {
			try {
				httpclient.getConnectionManager().shutdown();
			} catch (Exception e) {
			}
		}

		return null;
	}
	
	public HttpClient getHttpsClient() {

		HttpClient httpclient = new DefaultHttpClient();
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpclient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, httpclient.getParams());

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
