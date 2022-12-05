package cn.edu.njfu.zyf.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.njfu.zyf.model.Student;
import cn.edu.njfu.zyf.notification.TextMessageSender;
import cn.edu.njfu.zyf.service.GadgetService;
import io.swagger.annotations.ApiOperation;

@RestController
public class GadgetController {

	@Autowired
	private GadgetService gadgetService;
	
	@Autowired
	private TextMessageSender sender;
	
	@ApiOperation(value = "说声hi")
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		return gadgetService.hello();
	}
	
	@ApiOperation(value = "给手机发一条短信")
	@RequestMapping(value = "/text/mobile", method = RequestMethod.GET)
	public String sendText(String text, String mobile) throws IOException {
		
	    Student targetPerson = new Student();
	    targetPerson.setPhoneNumber(mobile);
	    targetPerson.setStudentNumber("2022097");
		
	    sender.sendMessage(text, targetPerson);
	    
		return "hi";
	}
	
	
	@ApiOperation(value = "上传两个excel文件，一个文件是学生-宿舍关系，另一个文件是刷卡记录。下载谁没做核酸的信息")
	@RequestMapping(value = "/untested/dorm", method = RequestMethod.POST)
	public String findUntestedDorm(
	        MultipartFile dorms, 
	        MultipartFile testedStudents,
	        HttpServletResponse response) throws IOException {
	    Workbook wb = gadgetService.findUntestedDorm(dorms, testedStudents);
	    
	    response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("untested-dorm.xls"));
        response.setContentType("multipart/form-data");
	    OutputStream os = response.getOutputStream();
	    wb.write(os);
	    os.flush();
	    os.close();
	    return "hi";
	}
}
