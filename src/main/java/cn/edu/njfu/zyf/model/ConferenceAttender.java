package cn.edu.njfu.zyf.model;

public class ConferenceAttender {
    private String conferenceName;
    private String staffUnchangeableCode;
    private String staffName;
    private String attendanceStatus;
    private String assistanceField;
    
    
    public String getConferenceName() {
        return conferenceName;
    }
    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }
    public String getStaffUnchangeableCode() {
        return staffUnchangeableCode;
    }
    public void setStaffUnchangeableCode(String staffUnchangeableCode) {
        this.staffUnchangeableCode = staffUnchangeableCode;
    }
    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
    public String getAttendanceStatus() {
        return attendanceStatus;
    }
    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    public String getAssistanceField() {
        return assistanceField;
    }
    public void setAssistanceField(String assistanceField) {
        this.assistanceField = assistanceField;
    }
}
