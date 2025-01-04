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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.njfu.zyf.dao.GeneralConferenceAttenderDao;
import cn.edu.njfu.zyf.model.GeneralAttender;

@Repository
public class GeneralConferenceAttenderDaoHttpImpl implements GeneralConferenceAttenderDao{

	// 缓存5秒内签到人数据，防止别人开许多个窗口，导致刷新太快
	private static final Long localCacheExpiryMilliseconds = 5000L; 
	// 记录了上一次查询时间
	private volatile Map<String, Long> conferenceQueryTimestampMap = new HashMap<>();
	// 记录了上一次查询结果
	private volatile Map<String, List<GeneralAttender>> conferenceQueryResultMap = new HashMap<>(); 
	
	private final String shareKey = "cbed0acd4bcd43faa7104358f4d575d0";
	private final String requestURL = "https://apaas.njfu.edu.cn/api/public/query/cbed0acd4bcd43faa7104358f4d575d0/data";
	
	private static Map<String, String> fieldCodeToFieldNameMap = new HashMap<>();
	
	static {
		fieldCodeToFieldNameMap.put("input_1713436053766", "comment");
		fieldCodeToFieldNameMap.put("select_1713434732376", "conferenceID");
		fieldCodeToFieldNameMap.put("input_1713436028287", "department");
		fieldCodeToFieldNameMap.put("input_1713435952863", "unchangeableCode");
		fieldCodeToFieldNameMap.put("radio_1713451244024", "hasSignedIn");
		fieldCodeToFieldNameMap.put("input_1713435978437", "name");
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
	    	int count = Integer.parseInt((String) dataMap.get("count"));
	    	List<Map<String, String>> datas = (List<Map<String, String>>) dataMap.get("datas");
	    	for(Map<String, String> datum: datas) {
	    		GeneralAttender ga = new GeneralAttender();
	    		ga.setConferenceID(conferenceID);
	    		ga.setUnchangeableCode(datum.get("unchangeableCode"));
	    		ga.setName(datum.get("name"));
	    		ga.setComment(datum.get("comment"));
	    		ga.setDepartment(datum.get("department"));
	    		ga.setHasSignedIn(datum.get("hasSignedIn"));
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
		String result = "{\"query\":\"{\\\"query\\\":{\\\"bool\\\":{\\\"must\\\":[{\\\"terms\\\":{\\\"select_1713434732376\\\":[\\\"{0}\\\"]}}],\\\"must_not\\\":[]}},\\\"_source\\\":[\\\"singlemember_1713435076343\\\",\\\"date_1713450008324\\\",\\\"select_1713434732376\\\",\\\"input_1713435952863\\\",\\\"input_1713435978437\\\",\\\"input_1713436028287\\\",\\\"input_1713436053766\\\",\\\"radio_1713451244024\\\",\\\"divider_1713450807261\\\",\\\"singlemember_1713450315127\\\",\\\"creator\\\",\\\"createTime\\\",\\\"updateTime\\\"],\\\"from\\\":0,\\\"size\\\":99999,\\\"sort\\\":[{\\\"createTime\\\":{\\\"order\\\":\\\"desc\\\"}}]}\",\"password\":null,\"appId\":\"99\",\"formId\":\"639\",\"formCode\":\"bbed0acd4bcd43faa7104358f4d575d0\",\"filterFields\":[\"select_1713434732376\"],\"queryFields\":[\"singlemember_1713435076343\",\"date_1713450008324\",\"select_1713434732376\",\"input_1713435952863\",\"input_1713435978437\",\"input_1713436028287\",\"input_1713436053766\",\"radio_1713451244024\",\"divider_1713450807261\",\"singlemember_1713450315127\",\"creator\",\"createTime\",\"updateTime\"]}";
		return result.replace("{0}", conferenceID);
	}
}
