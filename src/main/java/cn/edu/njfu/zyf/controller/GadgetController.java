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
}
