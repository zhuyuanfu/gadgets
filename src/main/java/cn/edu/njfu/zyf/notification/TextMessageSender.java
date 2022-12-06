package cn.edu.njfu.zyf.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.edu.njfu.zyf.model.Student;
import cn.edu.njfu.zyf.util.JsonUtils;
import cn.edu.njfu.zyf.util.Md5Utils;

@Component
public class TextMessageSender {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String textMessageServiceUrl = "http://apis.njfu.edu.cn/mp_message_pocket_web-mp-restful-message-send/ProxyService/message_pocket_web-mp-restful-message-sendProxyService";
    
    // 懒了
    private String appId = "05c4c92ebf232547";
    private String accessToken = "1792a85e3cef2b666d50d493efc2dc0b";
    private String schoolCode = "njlydx";
    
    public void sendMessage(String text, Student student) {
        HttpURLConnection con = null;
        try {
            con = createConnection();
            
            OutputStream os = con.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            
            String requestBody = JsonUtils.toJson(createRequestBodyMap(text, student));
            logger.info("request body is: " + requestBody);
            
            writer.write(requestBody);
            writer.flush();
            
            con.connect();
            
            logResponse(con);
            
        } catch (IOException e) {
            logger.error("Encountered exception. ", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    
    public void batchSendMessage(String text, List<Student> receivers) {
        
        HttpURLConnection con = null;
        
        try {
            URL url = new URL(textMessageServiceUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("appId", appId);
            con.setRequestProperty("accessToken", accessToken);
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);

            writer.write(JsonUtils.toJson(createRequestBodyMap(text, receivers)));
            writer.flush();
            
            con.connect();
            
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = con.getInputStream();
                StringBuffer sb = new StringBuffer();
                String line = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                logger.info("response: " + sb.toString());
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    
    private HttpURLConnection createConnection() throws IOException {
        URL url = new URL(textMessageServiceUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("appId", "05c4c92ebf232547");
        con.setRequestProperty("accessToken", "1792a85e3cef2b666d50d493efc2dc0b");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        return con;
    }
    
    
    
    private List<Map> toReceivers(List<Student> students) {
        List<Map> result = new ArrayList<>();
        for(int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            Map map = new HashMap();
            map.put("userId", s.getStudentNumber());
            map.put("mobile", s.getPhoneNumber());
            map.put("email", "");
            map.put("flag", 0);
            result.add(map);
        }
        return result;
    }
//    private List<Map> toReceivers(Student student) {
//        List<Student> studentList = new ArrayList<>();
//        studentList.add(student);
//        return toReceivers(studentList);
//    }
    
    private Map createRequestBodyMap(String text, List<Student> receivers) {
        Map result = new HashMap();
        result.put("appId", "zyf-gadgets");
        result.put("subject", "nukeAcidNotification");
        result.put("content", text);
        result.put("sendType", 4);
        result.put("tagId", 1012);
        result.put("wxSendType", "text");
        result.put("sendNow", true);
        result.put("receivers", toReceivers(receivers));
        result.put("schoolCode", schoolCode);
        result.put("sign", Md5Utils.toMd5(accessToken + schoolCode + receivers.get(0).getStudentNumber()).toLowerCase());
        return result;
    }
    
    private Map createRequestBodyMap(String text, Student student) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        return createRequestBodyMap(text, studentList);
    }
    
    
    private void logResponse(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream is = con.getInputStream();
            StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            logger.info("response: " + sb.toString());
        } else {
            InputStream is = con.getErrorStream();
            StringBuffer sb = new StringBuffer();
            String line = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }
            logger.info("response: " + sb.toString());
        }
    }
}
