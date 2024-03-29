package cn.edu.njfu.zyf.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.njfu.zyf.dao.ConferenceAttendanceDao;
import cn.edu.njfu.zyf.model.ConferenceAttender;
import cn.edu.njfu.zyf.service.ConferenceAttendanceService;

@Service
public class ConferenceAttendanceServiceImpl implements ConferenceAttendanceService{

    @Autowired
    private ConferenceAttendanceDao cad;
    
    @Override
    public List<ConferenceAttender> listAttenders(){
        return cad.listAttenders();
    }

    @Override
    public int getCheckIns(String conferenceName) {
        List<ConferenceAttender> allAttendances = cad.listAttenders();
        int count = 0;
        for(ConferenceAttender ca: allAttendances) {
            if (ca.getConferenceName().equals(conferenceName) && ca.getAttendanceStatus().equals("已签到")) {
                count ++;
            }
        }
        return count;
    }

}
