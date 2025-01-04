package cn.edu.njfu.zyf.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class HttpRequestUtils {

	public static String request(
			String requestURL,
			String method,
			Map<String, String> requestProperty,
			String requestBody) {
		String result = "";
		try {
			URL url = new URL(requestURL);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	con.setRequestMethod(method);
	    	con.setDoOutput(true);
	    	Iterator<Map.Entry<String, String>> itr = requestProperty.entrySet().iterator();
	    	while (itr.hasNext()) {
	    		Map.Entry<String, String> entry = itr.next();
	    		con.setRequestProperty(entry.getKey(), entry.getValue());
	    	}
	    	
	    	if (!method.toLowerCase().equals("get")) {
		    	OutputStream os = con.getOutputStream();
		    	OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		    	osw.write(requestBody);
		    	osw.close();
		    	os.close();
	    	}
	    	
	    	con.connect();
	    	
	    	InputStream is = con.getInputStream();
	    	InputStreamReader isr = new InputStreamReader(is);
	    	BufferedReader br = new BufferedReader(isr);
	    	StringBuilder responseBuilder = new StringBuilder();
	    	String responseLine = null;
	    	while((responseLine = br.readLine()) != null) {
	    		responseBuilder.append(responseLine).append("\n");
	    	}
	    	br.close();
	    	isr.close();
	    	is.close();
	    	
	    	result = responseBuilder.toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
		}
		return result;
	}
}
