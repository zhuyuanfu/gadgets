package cn.edu.njfu.zyf.model;

public class GeneralAttender {
	private String conferenceID;
	private String unchangeableCode;
	private String name;
	private String department;
	private String comment;
	private String hasSignedIn;
	
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getHasSignedIn() {
		return hasSignedIn;
	}
	public void setHasSignedIn(String hasCheckedIn) {
		this.hasSignedIn = hasCheckedIn;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof GeneralAttender) {
			GeneralAttender otherAttender = (GeneralAttender) other;
			return this.conferenceID.equals(otherAttender.getConferenceID()) && this.unchangeableCode.equals(otherAttender.getUnchangeableCode());
		} else {
			return false;
		}
		
	}
}
