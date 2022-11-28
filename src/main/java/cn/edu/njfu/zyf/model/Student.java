package cn.edu.njfu.zyf.model;

public class Student {
	private String name;
	private String identityNumber;
	private String studentNumber;
	
	private int bedNumber;
	private String phoneNumber;
	
	public Student() {	}
	
	public Student(String name, String identityNumber, String studentNumber) {
		this.name = name;
		this.identityNumber = identityNumber;
		this.studentNumber = studentNumber;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdentityNumber() {
		return identityNumber;
	}
	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String stundentNumber) {
		this.studentNumber = stundentNumber;
	}
	
	public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
	public boolean equals(Object other) {
		if (! (other instanceof Student)) {
			return false;
		}
		Student otherStudent = (Student) other;
		boolean result = false;
		if (otherStudent.getStudentNumber().equalsIgnoreCase(this.studentNumber)) {
			result = true;
		} else {
			if (!isBlank(this.studentNumber) && !isBlank(otherStudent.getStudentNumber())) {
				result = this.studentNumber.equals(otherStudent.getStudentNumber());
			}
		}
		
		return result;
	}
	
	public static boolean isBlank(CharSequence cs) {
		return cs == null || cs.toString().trim().length() == 0;
	}
	
	public String toString() {
		return "【姓名：" + name + "；学号：" + studentNumber + "；身份证号：" + identityNumber + "】";
	}
}
