package cn.edu.njfu.zyf.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import cn.edu.njfu.zyf.model.GeneralAttender;

public interface GeneralAttenderService {
	List<GeneralAttender> listAttendersByConferenceID(String conferenceID);
	int getSignedCount(String conferenceID);
	int getTotalCount(String conferenceID);
	Workbook createAttenderWorkbookByConferenceID(String conferenceID);
}
