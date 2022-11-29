package cn.edu.njfu.zyf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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

    private static Set<String> nukeAcidDoors = new HashSet<String>();
    
    private static Set<String> studentTypes = new HashSet<String>();
    static {
        // TODO 还有教9楼、新开的某楼等
        nukeAcidDoors.add("大活103");
        nukeAcidDoors.add("大活104");
        nukeAcidDoors.add("大活111");
        nukeAcidDoors.add("大活一楼弱电室");
        
         // 只保留学生，不保留教工、社会人员、家属等
        studentTypes.add("本科生");
        studentTypes.add("博士生");
        studentTypes.add("留学生");
        studentTypes.add("统招硕士生");
        studentTypes.add("专业硕士生");

    }
    
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
    public String findUntestedDorm(MultipartFile dorms, MultipartFile testedPeople) throws IOException {
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
            CellType studentNumberType = row.getCell(0).getCellType();
            if (studentNumberType == CellType.NUMERIC) {
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
            CellType bedNumberType = row.getCell(4).getCellType();
            if (bedNumberType == CellType.NUMERIC) {
                student.setBedNumber((int) Math.round(row.getCell(4).getNumericCellValue()));
            } else {
                student.setBedNumber(Integer.parseInt(row.getCell(4).getStringCellValue()));
            }
            
            // 把宿舍楼和宿舍号存进宿舍对象
            String building = row.getCell(2).getStringCellValue();
            CellType dormNumberType = row.getCell(3).getCellType();
            String dormNumber;
            if (dormNumberType == CellType.NUMERIC) {
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
        
        
        
        // 根据核酸处刷卡记录，
        List<Student> nukeAcidTestedStudents = new ArrayList<Student>();
        HSSFWorkbook usedCardPeopleWorkbook = new HSSFWorkbook(testedPeople.getInputStream());
        Sheet usedCardPeopleSheet = usedCardPeopleWorkbook.getSheetAt(0);
        int usedCardPeopleMaxRow = usedCardPeopleSheet.getLastRowNum();
        for (int i = 2; i < usedCardPeopleMaxRow; i ++) {
            Row row = usedCardPeopleSheet.getRow(i);
            String doorName = row.getCell(13).getStringCellValue();
            String studentType = row.getCell(7).getStringCellValue();
            // 非核酸刷卡记录（在不相关的门的刷卡记录）需要排除在外
            if (!nukeAcidDoors.contains(doorName)) {
                continue;
            }
            // 非学生需要排除在外
            if (!studentTypes.contains(studentType)) {
                continue;
            }
            
            Student student = new Student();
            
            String name = row.getCell(1).getStringCellValue();
            String studentNumber = row.getCell(2).getStringCellValue();
            student.setName(name);
            student.setStudentNumber(studentNumber);
            
            nukeAcidTestedStudents.add(student);
        }
        
        // 将所有住宿生和当天做了核酸的学生对比一下
        for (Student s : studentList) {
            if (nukeAcidTestedStudents.contains(s)) {
                s.setNukeAcidTested(true);
            }
        }
        
        // 制作一个好看的excel表格，将【做了核酸的宿舍】和【没做核酸的宿舍】区分开
        
        
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
    
    private Dormitory findDormByStudentNumber(List<Dormitory> dormList, String studentNumber) {
        for (Dormitory dorm: dormList) {
            List<Student> studentList = dorm.getStudentListList();
            for (Student student: studentList) {
                if (student.getStudentNumber().equalsIgnoreCase(studentNumber)) {
                    return dorm;
                }
            }
        }
        return null;
    }
    
    private Student findStudentInDorm(Dormitory dorm, String studentNumber) {
        for (Student s: dorm.getStudentListList()) {
            if (s.getStudentNumber().equalsIgnoreCase(studentNumber)) {
                return s;
            }
        }
        return null;
    }
    
    private Student findStudentInDorm(Dormitory dorm, Student student) {
        for (Student s: dorm.getStudentListList()) {
            if (s.getStudentNumber().equalsIgnoreCase(student.getStudentNumber())) {
                return s;
            }
        }
        return null;
    }
}
