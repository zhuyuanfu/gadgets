package cn.edu.njfu.zyf.model;

import java.util.ArrayList;
import java.util.List;

public class Dormitory {
    
    private String building;
    private String dormNumber;
    
    private List<Student> studentList = new ArrayList<Student>();
    
    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getDormNumber() {
        return dormNumber;
    }

    public void setDormNumber(String dormNumber) {
        this.dormNumber = dormNumber;
    }

    public List<Student> getStudentListList() {
        return this.studentList;
    }
    
    public void addStudent(Student s) {
        if (!containsStudent(s)) {
            studentList.add(s);
        }
    }
    
    
    public boolean containsStudent(Student s) {
        return studentList.contains(s);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Dormitory) {
            Dormitory otherDorm = (Dormitory) other;
            return this.dormNumber.equals(otherDorm.getDormNumber()) && 
                    this.building.equals(otherDorm.getBuilding());
        } else {
            return false;
        }
    }
}
