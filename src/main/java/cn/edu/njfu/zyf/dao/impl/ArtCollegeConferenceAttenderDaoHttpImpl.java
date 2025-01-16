package cn.edu.njfu.zyf.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.njfu.zyf.dao.GeneralConferenceAttenderDao;
import cn.edu.njfu.zyf.model.GeneralAttender;

@Repository(value = "artCollegeConferenceAttenderDao")
public class ArtCollegeConferenceAttenderDaoHttpImpl implements GeneralConferenceAttenderDao{

	// 缓存5秒内签到人数据，防止别人开许多个窗口，导致刷新太快
	private static final Long localCacheExpiryMilliseconds = 5000L; 
	// 记录了上一次查询时间
	private volatile Map<String, Long> conferenceQueryTimestampMap = new HashMap<>();
	// 记录了上一次查询结果
	private volatile Map<String, List<GeneralAttender>> conferenceQueryResultMap = new HashMap<>(); 
	
	private final String shareKey = "125e70bfd19c4d1fb7a72af7c0959504";
	private final String requestURL = "https://apaas.njfu.edu.cn/api/public/query/125e70bfd19c4d1fb7a72af7c0959504/data";
	
	private static Map<String, String> fieldCodeToFieldNameMap = new HashMap<>();
	
	static {
		fieldCodeToFieldNameMap.put("select_1718070672408", "conferenceID");
		fieldCodeToFieldNameMap.put("input_1716523965124", "unchangeableCode");
		fieldCodeToFieldNameMap.put("input_1716524085527", "name");
	}
	
	@Override
	public List<GeneralAttender> listGeneralAttendersByConferenceID(String conferenceID) {
		Long now = System.currentTimeMillis();
		if (conferenceQueryTimestampMap.containsKey(conferenceID) && conferenceQueryResultMap.containsKey(conferenceID)) {
			Long lastTimeHttpQuery = conferenceQueryTimestampMap.get(conferenceID);
			if (now - lastTimeHttpQuery < localCacheExpiryMilliseconds) {
				return conferenceQueryResultMap.get(conferenceID);
			}
		}
		
		List<GeneralAttender> result = new ArrayList<>();
		try {
			URL url = new URL(requestURL);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	con.setRequestMethod("POST");
	    	con.setDoOutput(true);
	    	con.setRequestProperty("Content-Type", "application/json");
	    	con.setRequestProperty("Sharekey", shareKey);

	    	OutputStream os = con.getOutputStream();
	    	OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
	    	osw.write(makePayload(conferenceID));
	    	osw.close();
	    	os.close();
	    	
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
	    	
	    	String responseText = responseBuilder.toString();
	    	String parsedResponseText = convertFieldCode2FieldName(responseText);
	    	ObjectMapper om = new ObjectMapper();
	    	Map<String, Object> responseMap = om.readValue(parsedResponseText, new TypeReference<Map<String, Object>>() {});
	    	Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
	    	List<Map<String, String>> datas = (List<Map<String, String>>) dataMap.get("datas");
	    	for(Map<String, String> datum: datas) {
	    		GeneralAttender ga = new GeneralAttender();
	    		ga.setConferenceID(conferenceID);
	    		ga.setUnchangeableCode(datum.get("unchangeableCode"));
	    		ga.setName(datum.get("name"));
	    		result.add(ga);
	    	}
	    	conferenceQueryTimestampMap.put(conferenceID, now);
			conferenceQueryResultMap.put(conferenceID, result);
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
	
	private String convertFieldCode2FieldName(String inputJson) {
		String result = inputJson;
		Set<Entry<String, String>> fieldCode2FieldNameEntrySet = fieldCodeToFieldNameMap.entrySet();
		for(Entry<String, String> entry: fieldCode2FieldNameEntrySet) {
			String fieldCode = entry.getKey();
			String fieldName = entry.getValue();
			result = result.replace(fieldCode, fieldName);
		}
		return result;
	}
	
	private String makePayload(String conferenceID) {
		String result = "{\"query\":\"{\\\"query\\\":{\\\"bool\\\":{\\\"must\\\":[{\\\"terms\\\":{\\\"select_1718070672408\\\":[\\\"{0}\\\"]}}],\\\"must_not\\\":[]}},\\\"_source\\\":[\\\"singlemember_1716524263135\\\",\\\"date_1718070440816\\\",\\\"format_number_1718070747506\\\",\\\"select_1718070672408\\\",\\\"input_1716523965124\\\",\\\"input_1716524085527\\\",\\\"coordinates_1735877356713\\\",\\\"input_1732153593976\\\",\\\"creator\\\",\\\"createTime\\\",\\\"updateTime\\\"],\\\"from\\\":0,\\\"size\\\":999999,\\\"sort\\\":[{\\\"createTime\\\":{\\\"order\\\":\\\"desc\\\"}}]}\",\"password\":null,\"appId\":\"182\",\"formId\":\"1244\",\"formCode\":\"025e70bfd19c4d1fb7a72af7c0959504\",\"filterFields\":[\"select_1718070672408\"],\"queryFields\":[\"singlemember_1716524263135\",\"date_1718070440816\",\"format_number_1718070747506\",\"select_1718070672408\",\"input_1716523965124\",\"input_1716524085527\",\"coordinates_1735877356713\",\"input_1732153593976\",\"creator\",\"createTime\",\"updateTime\"]}";
		return result.replace("{0}", conferenceID);
	}
}
