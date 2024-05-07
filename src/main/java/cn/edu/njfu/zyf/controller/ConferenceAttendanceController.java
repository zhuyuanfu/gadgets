package cn.edu.njfu.zyf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.service.ConferenceAttendanceService;
import io.swagger.annotations.ApiOperation;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class ConferenceAttendanceController {
    
    @Autowired
    private ConferenceAttendanceService cas;
    
    private final Path imageStoragePath = Paths.get(".\\static\\");
    //private final Path imageStoragePath = Paths.get("E:\\eclipseWorkspace\\gadgets\\static\\");
    
    @RequestMapping(value = "/qrCode", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> qrImage(HttpServletResponse res) {
    	try {
    		res.setContentType("image/apng");
            Path file = imageStoragePath.resolve("conferenceCheckinQRCode.png");
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
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndex(String conferenceName) {
        return getIndexPage(conferenceName);
    }
    
    @ApiOperation(value = "查询签到人数")
    @RequestMapping(value = "/numOfCheckins", method = RequestMethod.GET)
    public int getUncheckedNum(HttpServletRequest req, String conferenceName) {
        int checkins = cas.getCheckIns(conferenceName);
        System.out.println(req.getLocalAddr() + "会议" + conferenceName + "已签到人数：" + checkins);
        return checkins;
    }
    
    
    
    
    private String getIndexPage(String conferenceName) {
        return "<!DOCTYPE html>\r\n"
        		+ "<html lang=\"en\">\r\n"
        		+ "<head>\r\n"
        		+ "    <meta charset=\"UTF-8\">\r\n"
        		+ "    <title>会议签到情况</title>\r\n"
        		+ "    <style type=\"text/css\">\r\n"
        		+ "        body {background-color: blue}\r\n"
        		+ "        h1 {color: white; text-align:center}\r\n"
        		+ "        a {text-align:center; margin: auto}\r\n"
        		+ "        div {text-align:center}\r\n"
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
        		+ "	    function queryNum(){\r\n"
        		+ "	        httpRequest.open('GET', '/gadgets/numOfCheckins?conferenceName=" + conferenceName + "', true)\r\n"
        		+ "            httpRequest.send()  \r\n"
        		+ "            httpRequest.onreadystatechange = function(){\r\n"
        		+ "                if (httpRequest.readyState == 4 && httpRequest.status == 200) {\r\n"
        		+ "                    console.log(\"received numOfCheckins = \" + httpRequest.response)\r\n"
        		+ "                    document.getElementById('num').innerHTML = httpRequest.response\r\n"
        		+ "                }\r\n"
        		+ "	        }  \r\n"
        		+ "        }\r\n"
        		+ "        setInterval(queryNum, 1700)\r\n"
        		+ "    </script>\r\n"
        		+ "</head>\r\n"
        		+ "<body>\r\n"
        		+ "    <h1>网络安全和信息化工作会议</h1>\r\n"
        		+ "    <h1>签到二维码：</h1>\r\n"
        		+ "        <div>\r\n"
        		+ "            <img src=\"/gadgets/qrCode\" />\r\n"
        		+ "        </div>\r\n"
        		+ "    <h1>已签到人数：<a id='num'>0</a> </h1>\r\n"
        		+ "    <h1>WiFi7信号（NJFU-WiFi）已覆盖本会场，欢迎大家试用</h1>\r\n"
        		+ "</body>\r\n"
        		+ "</html>";
    }
}
