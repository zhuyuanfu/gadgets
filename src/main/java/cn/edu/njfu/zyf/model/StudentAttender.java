package cn.edu.njfu.zyf.model;
/**
 * Student attenders don't have to be arranged beforehand.
 * @author zyf
 *
 */
public class StudentAttender {
	private String conferenceID;
	private String unchangeableCode;
	private String name;
	private String department;
	private String major;
	
	public String getConferenceID() {
		return conferenceID;
	}
	public void setConferenceID(String conferenceID) {
		this.conferenceID = conferenceID;
	}
	public String getUnchangeableCode() {
		return unchangeableCode;
	}
	public void setUnchangeableCode(String unchangeableCode) {
		this.unchangeableCode = unchangeableCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
}
