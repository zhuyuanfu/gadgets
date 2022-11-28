package cn.edu.njfu.zyf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.njfu.zyf.model.Dormitory;
import cn.edu.njfu.zyf.model.Student;
import cn.edu.njfu.zyf.service.GadgetService;

@Service
public class GadgetServiceImpl implements GadgetService{

	public String hello() {
		return "Hello Springboot! 你好世界！";
	}

	@Override
	public String findUntestedStudents(MultipartFile testedPeopleFile,
			int testedNameColIndex,
			int testedIdentityColIndex,
			MultipartFile allStudentsFile,
			int allNameColIndex,
			int allIdentityColIndex,
			int allStudentNumberIndex) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String findUntestedDorm(MultipartFile dorms, MultipartFile testedStudents) throws IOException {
        // 将宿舍从excel表中提取出来，也把学生提取出来
        List<Dormitory> dormList = new ArrayList<Dormitory>();
        List<Student> studentList = new ArrayList<Student>();
        
        InputStream dormsInputStream = dorms.getInputStream();
        HSSFWorkbook dormsWorkbook = new HSSFWorkbook(dormsInputStream);
        Sheet dormsSheet = dormsWorkbook.getSheetAt(0);
        int numOfDormsSheetRows = dormsSheet.getLastRowNum();
        for (int i = 1; i < numOfDormsSheetRows; i ++) {
            // 原始数据实在是太脏了屮
            System.out.println("processing " + i + "th row"); 
            Row row = dormsSheet.getRow(i);
            Student student = new Student();
            
            // 把学号存进学生对象
            int studentNumberType = row.getCell(0).getCellType();
            if (studentNumberType == Cell.CELL_TYPE_NUMERIC) {
                student.setStudentNumber(row.getCell(0).getNumericCellValue() + "");
            } else {
                student.setStudentNumber(row.getCell(0).getStringCellValue());
            }
            
            // 把姓名存进学生对象
            student.setName(row.getCell(1).getStringCellValue());
            
            // 把手机号存进学生对象
            String phoneNumber = "";
            try {
                phoneNumber = row.getCell(5).getStringCellValue();
            } catch(Exception e) {
                
            }
            student.setPhoneNumber(phoneNumber);
            
            // 把床号存进学生对象
            int bedNumberType = row.getCell(4).getCellType();
            if (bedNumberType == Cell.CELL_TYPE_NUMERIC) {
                student.setBedNumber((int) Math.round(row.getCell(4).getNumericCellValue()));
            } else {
                student.setBedNumber(Integer.parseInt(row.getCell(4).getStringCellValue()));
            }
            
            // 把宿舍楼和宿舍号存进宿舍对象
            String building = row.getCell(2).getStringCellValue();
            int dormNumberType = row.getCell(3).getCellType();
            String dormNumber;
            if (dormNumberType == Cell.CELL_TYPE_NUMERIC) {
                dormNumber = row.getCell(3).getNumericCellValue() + "";
            } else {
                dormNumber = row.getCell(3).getStringCellValue();
            }
            
            Dormitory dorm = findDorm(dormList, building, dormNumber);
            if (dorm == null) {
                dorm = new Dormitory();
                dorm.setBuilding(building);
                dorm.setDormNumber(dormNumber);
                dormList.add(dorm);
            }
            dorm.addStudent(student);
            studentList.add(student);
        }
        System.out.println(dormList.size());
        System.out.println(studentList.size());
        return null;
    } 
    
    private Dormitory findDorm(List<Dormitory> dormList, String building, String dormNumber) {
        for (Dormitory dorm: dormList) {
            if (dorm.getBuilding().contentEquals(building) && dorm.getDormNumber().contentEquals(dormNumber)) {
                return dorm;
            } 
        }
        return null;
    }
}
