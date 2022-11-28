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

import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.njfu.zyf.service.GadgetService;
import io.swagger.annotations.ApiOperation;

@RestController
public class GadgetController {

	@Autowired
	private GadgetService gadgetService;
	
	@ApiOperation(value = "说声hi")
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		return gadgetService.hello();
	}
	
	@ApiOperation(value = "上传两个excel文件，指定姓名、身份证、学号列的位置，下载谁没做核酸的信息")
	@RequestMapping(value = "/untested/students", method = RequestMethod.GET)
	public String findUntestedStudents(HttpServletResponse response) throws IOException {
		
		File file = new File("C:\\Users\\Administrator\\Desktop\\排查没做核酸的学生workspace\\20221004\\未做核酸的学生（日期：20221004）.xlsx");
		InputStream is = new FileInputStream(file);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();
		
		response.reset();
		response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("未做核酸的学生（日期：20221004）.xlsx"));
		response.setContentType("multipart/form-data");
		
		OutputStream os = new BufferedOutputStream(response.getOutputStream());
		os.write(buffer);
		os.flush();
		os.close();
		
		return "hi";
//		return gadgetService.findUntestedStudents(testedPeopleFile, testedNameColIndex, testedIdentityColIndex, allStudentsFile, allNameColIndex, allIdentityColIndex, allStudentNumberIndex);
	}
	
	
	@ApiOperation(value = "上传两个excel文件，一个文件是学生-宿舍关系，另一个文件是刷卡记录。下载谁没做核酸的信息")
	@RequestMapping(value = "/untested/dorm", method = RequestMethod.POST)
	public String findUntestedDorm(MultipartFile dorms, MultipartFile testedStudents) throws IOException {
	    return gadgetService.findUntestedDorm(dorms, testedStudents);
	}
}
