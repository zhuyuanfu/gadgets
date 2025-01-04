package cn.edu.njfu.zyf.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.edu.njfu.zyf.dao.StudentAttenderDao;
import cn.edu.njfu.zyf.model.StudentAttender;
import cn.edu.njfu.zyf.util.HttpRequestUtils;

@Repository
public class StudentAttenderDaoImpl implements StudentAttenderDao {
	
	private static Logger logger = LoggerFactory.getLogger(StudentAttenderDaoImpl.class);

	// 缓存5秒内签到人数据，防止别人开许多个窗口，导致刷新太快
	private static final Long localCacheExpiryMilliseconds = 5000L; 
	// 记录了上一次查询时间
	private volatile Map<String, Long> conferenceQueryTimestampMap = new HashMap<>();
	// 记录了上一次查询结果
	private volatile Map<String, List<StudentAttender>> conferenceQueryResultMap = new HashMap<>(); 
	
	private final String shareKey = "2cb139f20a3f45c1a1bee84b2dad2857";
	private final String requestURL = "https://apaas.njfu.edu.cn/api/public/query/2cb139f20a3f45c1a1bee84b2dad2857/data";
	
	private static Map<String, String> fieldCodeToFieldNameMap = new HashMap<>();
	
	static {
		fieldCodeToFieldNameMap.put("input_1716524158019", "major");
		fieldCodeToFieldNameMap.put("select_1718070672408", "conferenceID");
		fieldCodeToFieldNameMap.put("input_1716524139165", "department");
		fieldCodeToFieldNameMap.put("input_1716523965124", "unchangeableCode");
		fieldCodeToFieldNameMap.put("input_1716524085527", "name");
	}
	@Override
	public List<StudentAttender> listGeneralAttendersByConferenceID(String conferenceID) {
		Long now = System.currentTimeMillis();
		if (conferenceQueryTimestampMap.containsKey(conferenceID) && conferenceQueryResultMap.containsKey(conferenceID)) {
			Long lastTimeHttpQuery = conferenceQueryTimestampMap.get(conferenceID);
			if (now - lastTimeHttpQuery < localCacheExpiryMilliseconds) {
				return conferenceQueryResultMap.get(conferenceID);
			}
		}		
		List<StudentAttender> result = new ArrayList<>();
		try {
			Map<String, String> requestPropertyMap = new HashMap<>();
			requestPropertyMap.put("Content-Type", "application/json");
			requestPropertyMap.put("Sharekey", shareKey);
			
	    	
	    	String responseText = HttpRequestUtils.request(requestURL, "POST", requestPropertyMap, makePayload(conferenceID));
	    	logger.info(responseText);
	    	String parsedResponseText = convertFieldCode2FieldName(responseText);
	    	ObjectMapper om = new ObjectMapper();
	    	Map<String, Object> responseMap = om.readValue(parsedResponseText, new TypeReference<Map<String, Object>>() {});
	    	Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
	    	List<Map<String, String>> datas = (List<Map<String, String>>) dataMap.get("datas");
	    	for(Map<String, String> datum: datas) {
	    		StudentAttender sa = new StudentAttender();
	    		sa.setConferenceID(conferenceID);
	    		sa.setUnchangeableCode(datum.get("unchangeableCode"));
	    		sa.setName(datum.get("name"));
	    		sa.setMajor(datum.get("major"));
	    		sa.setDepartment(datum.get("department"));
	    		result.add(sa);
	    	}
	    	conferenceQueryTimestampMap.put(conferenceID, now);
			conferenceQueryResultMap.put(conferenceID, result);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return result;
	}
	
	/**
	 * 写重复了，得想办法抽出来
	 * @param inputJson
	 * @return
	 */
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
		String result = "{\"query\":\"{\\\"query\\\":{\\\"bool\\\":{\\\"must\\\":[{\\\"terms\\\":{\\\"select_1718070672408\\\":[\\\"{0}\\\"]}}],\\\"must_not\\\":[]}},\\\"_source\\\":[\\\"singlemember_1716524263135\\\",\\\"input_1716523965124\\\",\\\"input_1716524085527\\\",\\\"input_1716524139165\\\",\\\"input_1716524158019\\\",\\\"creator\\\",\\\"createTime\\\",\\\"updateTime\\\"],\\\"from\\\":0,\\\"size\\\":9999999,\\\"sort\\\":[{\\\"createTime\\\":{\\\"order\\\":\\\"desc\\\"}}]}\",\"password\":null,\"appId\":\"124\",\"formId\":\"869\",\"formCode\":\"1cb139f20a3f45c1a1bee84b2dad2857\",\"filterFields\":[\"select_1718070672408\"],\"queryFields\":[\"singlemember_1716524263135\",\"input_1716523965124\",\"input_1716524085527\",\"input_1716524139165\",\"input_1716524158019\",\"creator\",\"createTime\",\"updateTime\"]}";
		return result.replace("{0}", conferenceID);
	}

}
