package cn.edu.njfu.zyf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.service.ConferenceAttendanceService;
import io.swagger.annotations.ApiOperation;

@RestController
public class ConferenceAttendanceController {
    
    @Autowired
    private ConferenceAttendanceService cas;
    
    @ApiOperation(value = "展示未签到人数页面")
    @RequestMapping(value = "/numOfCheckins", method = RequestMethod.GET)
    public String getUncheckedNum(String conferenceName) {
        int checkins = cas.getCheckIns(conferenceName);
        return getTemplateIndex().replace("{0}", conferenceName).replace("{1}", checkins + "");
    }
    
    
    private String getTemplateIndex() {
        return "<!DOCTYPE html>\r\n" + 
                "<html lang=\"en\">\r\n" + 
                "<head>\r\n" + 
                "    <meta charset=\"UTF-8\">\r\n" + 
                "    <meta http-equiv=\"refresh\" content=\"10\">\r\n" + 
                "    <title>会议签到情况</title>\r\n" + 
                "    <style type=\"text/css\">\r\n" + 
                "        body {background-color: blue}\r\n" + 
                "        h1 {color: white; text-align:center}\r\n" + 
                "        a {text-align:center; margin: auto}\r\n" + 
                "        div {text-align:center}\r\n" + 
                "    </style>\r\n" + 
                "</head>\r\n" + 
                "<body>\r\n" + 
                "    <h1>{0}</h1>\r\n" + 
                "    <h1>签到二维码：</h1>\r\n" + 
                "        <div>\r\n" + 
                "            <img src=\"https://private-user-images.githubusercontent.com/8939983/317766016-e9a925c2-35eb-4995-8f2e-622e0cf2a510.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTE2MzY2MzQsIm5iZiI6MTcxMTYzNjMzNCwicGF0aCI6Ii84OTM5OTgzLzMxNzc2NjAxNi1lOWE5MjVjMi0zNWViLTQ5OTUtOGYyZS02MjJlMGNmMmE1MTAucG5nP1gtQW16LUFsZ29yaXRobT1BV1M0LUhNQUMtU0hBMjU2JlgtQW16LUNyZWRlbnRpYWw9QUtJQVZDT0RZTFNBNTNQUUs0WkElMkYyMDI0MDMyOCUyRnVzLWVhc3QtMSUyRnMzJTJGYXdzNF9yZXF1ZXN0JlgtQW16LURhdGU9MjAyNDAzMjhUMTQzMjE0WiZYLUFtei1FeHBpcmVzPTMwMCZYLUFtei1TaWduYXR1cmU9ZTE5ODZjYWJmNDRiYzFjZTAwNGU4ZmY5NjhhYzNiZGY1MmI5ZmMzYTIxMWUxZDFmYjQwZWE0OGIxMjQ2MGYzZSZYLUFtei1TaWduZWRIZWFkZXJzPWhvc3QmYWN0b3JfaWQ9MCZrZXlfaWQ9MCZyZXBvX2lkPTAifQ.nc-VXkKCgf-mXpqzq8xYBfxgqAqvKms1_xEMlm4sArY\" />\r\n" + 
                "        </div>\r\n" + 
                "    <h1>实际签到人数：{1}</h1>\r\n" + 
                "</body>\r\n" + 
                "</html>";
    }
}
