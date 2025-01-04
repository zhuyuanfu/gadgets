package cn.edu.njfu.zyf.dao;

import java.util.List;

import cn.edu.njfu.zyf.model.StudentAttender;

public interface StudentAttenderDao {
	List<StudentAttender> listGeneralAttendersByConferenceID(String conferenceID);

}
