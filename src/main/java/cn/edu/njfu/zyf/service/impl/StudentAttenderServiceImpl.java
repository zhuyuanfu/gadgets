package cn.edu.njfu.zyf.service.impl;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.njfu.zyf.dao.StudentAttenderDao;
import cn.edu.njfu.zyf.model.StudentAttender;
import cn.edu.njfu.zyf.service.StudentAttenderService;

@Service
public class StudentAttenderServiceImpl implements StudentAttenderService {

	@Autowired
	private StudentAttenderDao dao;
	
	@Override
	public List<StudentAttender> listAttendersByConferenceID(String conferenceID) {
		return dao.listGeneralAttendersByConferenceID(conferenceID);
	}

	@Override
	public int getSignedCount(String conferenceID) {
		return dao.listGeneralAttendersByConferenceID(conferenceID).size();
	}

	@Override
	public Workbook createAttenderWorkbookByConferenceID(String conferenceID) {
		List<StudentAttender> generalAttenderList = dao.listGeneralAttendersByConferenceID(conferenceID);
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		
		Row row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("会议唯一标识");
		row0.createCell(1).setCellValue("学号");
		row0.createCell(2).setCellValue("姓名");
		row0.createCell(3).setCellValue("院系");
		row0.createCell(4).setCellValue("专业");
		
		for(int i = 0; i < generalAttenderList.size(); i++) {
			StudentAttender ga = generalAttenderList.get(i);
			Row dataRow = sheet.createRow(i + 1);
			dataRow.createCell(0).setCellValue(ga.getConferenceID());;
			dataRow.createCell(1).setCellValue(ga.getUnchangeableCode());;
			dataRow.createCell(2).setCellValue(ga.getName());;
			dataRow.createCell(3).setCellValue(ga.getDepartment());;
			dataRow.createCell(4).setCellValue(ga.getMajor());;
		}
		
		return wb;
	}

}
