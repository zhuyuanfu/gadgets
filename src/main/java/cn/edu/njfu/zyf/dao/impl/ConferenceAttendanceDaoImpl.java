package cn.edu.njfu.zyf.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.edu.njfu.zyf.dao.ConferenceAttendanceDao;
import cn.edu.njfu.zyf.model.ConferenceAttender;

@Repository
public class ConferenceAttendanceDaoImpl implements ConferenceAttendanceDao{
    @Override
    public List<ConferenceAttender> listAttenders() {
        Connection con = null;
        List<ConferenceAttender> result = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con= DriverManager.getConnection("jdbc:oracle:thin:@223.2.96.170:1521:KFPTDB",
                    "usr_zsj_zjk",
                    "usr_zsj_zjk#2022");  
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from T_CONFERENCE_ATTENDANCE_1");
            while(rs.next()) {
                String conferenceName = rs.getString("CONFERENCE_NAME");
                String staffUnchangeableCode = rs.getString("STAFF_UNCHANGEABLE_CODE");
                String staffName = rs.getString("STAFF_NAME");
                String assistanceField = rs.getString("ASSISTANCE_FIELD");
                String attendanceStatus = rs.getString("ATTENDANCE_STATUS");
                
                ConferenceAttender ca = new ConferenceAttender();
                ca.setConferenceName(conferenceName);
                ca.setStaffUnchangeableCode(staffUnchangeableCode);
                ca.setStaffName(staffName);
                ca.setAssistanceField(assistanceField);
                ca.setAttendanceStatus(attendanceStatus);
                result.add(ca);
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (con != null ) {
                try {
                    con.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
