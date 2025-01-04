package cn.edu.njfu.zyf.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import cn.edu.njfu.zyf.model.StudentAttender;

public interface StudentAttenderService {
	List<StudentAttender> listAttendersByConferenceID(String conferenceID);
	int getSignedCount(String conferenceID);
	Workbook createAttenderWorkbookByConferenceID(String conferenceID);
}
