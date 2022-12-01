package cn.edu.njfu.zyf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static String toJson(Object obj) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Parsing object threw exception. ", e);
            e.printStackTrace();
        }
        return result;
    }
    
    public static <T> T toObject(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<T>() {});
        } catch (JsonMappingException e) {
            logger.error("Parsing object threw exception. ", e);
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            logger.error("Parsing object threw exception. ", e);
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        
        List<Map> receivers = new ArrayList<Map>();
        
        Map m1 = new HashMap();
        receivers.add(m1);
        m1.put("name", "zyf");
        m1.put("age", 11);
        m1.put("phone", "18888888888");
        
        System.out.println(toJson(receivers));
    }
}
