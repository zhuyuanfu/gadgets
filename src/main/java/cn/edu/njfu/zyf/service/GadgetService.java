package cn.edu.njfu.zyf.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

public interface GadgetService {

	String hello();
	
	String findUntestedStudents(MultipartFile testedPeopleFile,
			int testedNameColIndex,
			int testedIdentityColIndex,
			MultipartFile allStudentsFile,
			int allNameColIndex,
			int allIdentityColIndex,
			int allStudentNumberIndex);
	
	Workbook findUntestedDorm(MultipartFile dorms, MultipartFile testedStudents)  throws IOException;
}
