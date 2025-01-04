package cn.edu.njfu.zyf.dao;

import java.util.List;

import cn.edu.njfu.zyf.model.GeneralAttender;

public interface GeneralConferenceAttenderDao {	
	List<GeneralAttender> listGeneralAttendersByConferenceID(String conferenceID);
}
