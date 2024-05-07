package cn.edu.njfu.zyf.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ConferenceAttendanceController2 {
    
    private final Path imageStoragePath = Paths.get(".\\static\\");
    
    @ApiOperation(value = "展示二维码")
    @RequestMapping(value = "/qrCode2", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> qrImage(HttpServletResponse res, String fileName) {
    	try {
    		res.setContentType("image/apng");
            Path file = imageStoragePath.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
 
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    	
    }
    
    @ApiOperation(value = "展示大屏页面")
    @RequestMapping(value = "/index2", method = RequestMethod.GET)
    public String getIndex(String conferenceName) {
        return getIndexPage(conferenceName);
    }
    
    @ApiOperation(value = "查询未签到人数")
    @RequestMapping(value = "/numOfSignIns", method = RequestMethod.GET)
    public int getUncheckedNum(HttpServletRequest req, String conferenceName) throws IOException {
        int checkins = getCount(conferenceName);
        System.out.println(req.getLocalAddr() + "会议" + conferenceName + "已签到人数：" + checkins);
        return checkins;
    }
    
    private int getCount(String conferenceName) throws IOException {
    	URL url = new URL("https://apaas.njfu.edu.cn/api/public/query/3e9898a7fc1640ed8eee731620abd5f2/data");
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setRequestMethod("POST");
    	con.setDoOutput(true);
    	con.setRequestProperty("Content-Type", "application/json");
    	con.setRequestProperty("Sharekey", "3e9898a7fc1640ed8eee731620abd5f2");

    	OutputStream os = con.getOutputStream();
    	OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
    	osw.write("{\"query\":\"{\\\"query\\\":{\\\"bool\\\":{\\\"must\\\":[],\\\"must_not\\\":[]}},\\\"_source\\\":[\\\"input_1712669129869\\\",\\\"input_1712668847672\\\",\\\"input_1712668869583\\\",\\\"input_1712668874272\\\",\\\"input_1712715303839\\\"],\\\"from\\\":0,\\\"size\\\":10,\\\"sort\\\":[{\\\"createTime\\\":{\\\"order\\\":\\\"desc\\\"}}]}\",\"password\":null,\"appId\":\"96\",\"formId\":\"612\",\"formCode\":\"2e9898a7fc1640ed8eee731620abd5f2\",\"filterFields\":[],\"queryFields\":[\"input_1712669129869\",\"input_1712668847672\",\"input_1712668869583\",\"input_1712668874272\",\"input_1712715303839\"]}");
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
    	System.out.println(responseText);
    	
    	ObjectMapper om = new ObjectMapper();
    	Map<String, Object> responseMap = om.readValue(responseText, new TypeReference<Map<String, Object>>() {});
    	Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
    	int count = Integer.parseInt((String) dataMap.get("count"));
    	return count;
    }
    
    
    private String getIndexPage(String conferenceName) {
        return "<!DOCTYPE html>\r\n"
        		+ "<html lang=\"en\">\r\n"
        		+ "\r\n"
        		+ "<head>\r\n"
        		+ "    <meta charset=\"UTF-8\">\r\n"
        		+ "    <title>" + conferenceName + "</title>\r\n"
        		+ "    <style type=\"text/css\">\r\n"
        		+ "        body {\r\n"
        		+ "            background-color: blue\r\n"
        		+ "        }\r\n"
        		+ "\r\n"
        		+ "        h1 {\r\n"
        		+ "            color: white;\r\n"
        		+ "            text-align: center\r\n"
        		+ "        }\r\n"
        		+ "\r\n"
        		+ "        a {\r\n"
        		+ "            text-align: center;\r\n"
        		+ "            margin: auto\r\n"
        		+ "        }\r\n"
        		+ "\r\n"
        		+ "        div {\r\n"
        		+ "            text-align: center\r\n"
        		+ "        }\r\n"
        		+ "    </style>\r\n"
        		+ "    <script>\r\n"
        		+ "        console.log(\"hahahahaha\")\r\n"
        		+ "        if (window.XMLHttpRequest) { // Mozilla, Safari, IE7+ ...\r\n"
        		+ "            httpRequest = new XMLHttpRequest();\r\n"
        		+ "        } else if (window.ActiveXObject) { // IE 6 及以下\r\n"
        		+ "            httpRequest = new ActiveXObject(\"Microsoft.XMLHTTP\");\r\n"
        		+ "        }\r\n"
        		+ "\r\n"
        		+ "\r\n"
        		+ "        function queryNum() {\r\n"
        		+ "            httpRequest.open('GET', '/gadgets/numOfSignIns?conferenceName=" + conferenceName + "', true)\r\n"
        		+ "            httpRequest.send()\r\n"
        		+ "            httpRequest.onreadystatechange = function () {\r\n"
        		+ "                if (httpRequest.readyState == 4 && httpRequest.status == 200) {\r\n"
        		+ "                    console.log(\"received numOfSignIns = \" + httpRequest.response)\r\n"
        		+ "                    document.getElementById('num').innerHTML = httpRequest.response\r\n"
        		+ "                }\r\n"
        		+ "            }\r\n"
        		+ "        }\r\n"
        		+ "        setInterval(queryNum, 1700)\r\n"
        		+ "    </script>\r\n"
        		+ "</head>\r\n"
        		+ "\r\n"
        		+ "<body>\r\n"
        		+ "    <h1>"+conferenceName+"</h1>\r\n"
        		+ "    <h1>签到二维码：</h1>\r\n"
        		+ "    <div>\r\n"
        		+ "        <img src=\"/gadgets/qrCode2?fileName=20240410.png\" />\r\n"
        		+ "    </div>\r\n"
        		+ "    <h1>已签到人数：<a id='num'>0</a> </h1>\r\n"
        		+ "</body>\r\n"
        		+ "\r\n"
        		+ "</html>";
    }
}
