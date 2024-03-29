package cn.edu.njfu.zyf.service;

import java.util.List;

import cn.edu.njfu.zyf.model.ConferenceAttender;

public interface ConferenceAttendanceService {
    List<ConferenceAttender> listAttenders();
    int getCheckIns(String conferenceName);
}
