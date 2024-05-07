package cn.edu.njfu.zyf.service.impl;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.njfu.zyf.dao.GeneralConferenceAttenderDao;
import cn.edu.njfu.zyf.model.GeneralAttender;
import cn.edu.njfu.zyf.service.GeneralAttenderService;

@Service
public class GeneralAttenderServiceImpl implements GeneralAttenderService {

	@Autowired
	private GeneralConferenceAttenderDao dao;
	
	@Override
	public List<GeneralAttender> listAttendersByConferenceID(String conferenceID) {
		return dao.listGeneralAttendersByConferenceID(conferenceID);
	}

	@Override
	public int getSignedCount(String conferenceID) {
		List<GeneralAttender> generalAttenderList = dao.listGeneralAttendersByConferenceID(conferenceID);
		int signedCount = 0;
		for(GeneralAttender ga: generalAttenderList) {
			if (ga.getHasSignedIn().equals("是")) {
				signedCount++;
			}
		}
		return signedCount;
	}

	@Override
	public int getTotalCount(String conferenceID) {
		return dao.listGeneralAttendersByConferenceID(conferenceID).size();
	}

	@Override
	public Workbook createAttenderWorkbookByConferenceID(String conferenceID) {
		List<GeneralAttender> generalAttenderList = dao.listGeneralAttendersByConferenceID(conferenceID);
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet();
		
		Row row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("会议名");
		row0.createCell(1).setCellValue("不变号");
		row0.createCell(2).setCellValue("姓名");
		row0.createCell(3).setCellValue("部门");
		row0.createCell(4).setCellValue("备注");
		row0.createCell(5).setCellValue("是否已签到");
		
		for(int i = 0; i < generalAttenderList.size(); i++) {
			GeneralAttender ga = generalAttenderList.get(i);
			Row dataRow = sheet.createRow(i + 1);
			dataRow.createCell(0).setCellValue(ga.getConferenceID());;
			dataRow.createCell(1).setCellValue(ga.getUnchangeableCode());;
			dataRow.createCell(2).setCellValue(ga.getName());;
			dataRow.createCell(3).setCellValue(ga.getDepartment());;
			dataRow.createCell(4).setCellValue(ga.getComment());;
			dataRow.createCell(5).setCellValue(ga.getHasSignedIn());;
		}
		
		return wb;
	}

}
