package cn.edu.njfu.zyf.notification;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.edu.njfu.zyf.model.Student;

@Component
public class TextMessageSender {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String textMessageServiceUrl = "";
    
    public void batchSendMessage(String text, List<Student> receivers) {
        
        HttpURLConnection con = null;
        
        try {
            URL url = new URL(textMessageServiceUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("appId", "05c4c92ebf232547");
            con.setRequestProperty("accessToken", "1792a85e3cef2b666d50d493efc2dc0b");
            con.setRequestProperty("Content-Type", "application/json");

            OutputStream os = con.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write("{}");
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
            
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
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
           
        }
        return result;
    }
    
    
}
