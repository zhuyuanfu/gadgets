package cn.edu.njfu.zyf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
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
    public Workbook findUntestedDorm(MultipartFile dorms, MultipartFile testedPeople) throws IOException {
        // 将宿舍从excel表中提取出来，也把学生提取出来
        List<Dormitory> dormList = new ArrayList<Dormitory>();
        List<Student> studentList = new ArrayList<Student>();
        
        InputStream dormsInputStream = dorms.getInputStream();
        HSSFWorkbook dormsWorkbook = new HSSFWorkbook(dormsInputStream);
        Sheet dormsSheet = dormsWorkbook.getSheetAt(0);
        int numOfDormsSheetRows = dormsSheet.getLastRowNum();
        for (int i = 1; i < numOfDormsSheetRows; i ++) {
            // 原始数据实在是太脏了屮
            Row row = dormsSheet.getRow(i);
            Student student = new Student();
            
            // 把学号存进学生对象
            CellType studentNumberType = row.getCell(0).getCellType();
            if (studentNumberType == CellType.NUMERIC) {
                student.setStudentNumber(NumberToTextConverter.toText(row.getCell(0).getNumericCellValue()));
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
                dormNumber = NumberToTextConverter.toText(row.getCell(3).getNumericCellValue());
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
        System.out.println(studentList.get(3));
        
        
        
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
            student.setName(name.trim());
            student.setStudentNumber(studentNumber.trim());
            
            nukeAcidTestedStudents.add(student);
        }
        
//        System.out.println(nukeAcidTestedStudents.get(3));
//        System.out.println(nukeAcidTestedStudents.get(33));
//        System.out.println(nukeAcidTestedStudents.get(333));
//        System.out.println(nukeAcidTestedStudents.get(34));
//        System.out.println(nukeAcidTestedStudents.get(344));
//        System.out.println(nukeAcidTestedStudents.get(3444));
        
        // 将所有住宿生和当天做了核酸的学生对比一下
        for (Student s : studentList) {
            if (nukeAcidTestedStudents.contains(s)) {
                s.setNukeAcidTested(true);
            }
        }
        
        dormList.sort(new Comparator<Dormitory>() {
            @Override
            public int compare(Dormitory o1, Dormitory o2) {
                boolean o1Tested = o1.atLeast1StudentTested();
                boolean o2Tested = o2.atLeast1StudentTested();
                
                if (o1Tested && !o2Tested) {
                    return -1;
                } else if (o2Tested && !o1Tested) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        
        // 制作一个好看的excel表格，将【做了核酸的宿舍】和【没做核酸的宿舍】区分开
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("学号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("楼栋");
        row.createCell(3).setCellValue("宿舍");
        row.createCell(4).setCellValue("床号");
        row.createCell(5).setCellValue("联系方式");
        row.createCell(6).setCellValue("学生做了核酸");
        row.createCell(7).setCellValue("宿舍做了核酸（只要有一个学生做了核酸即可认为整个宿舍做了核酸）");
        
        // 制作一个蓝色背景色，用于乖学生
        byte paletteIndex = 0x8;
        HSSFCellStyle cellStyle1 = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        HSSFPalette customPalette = wb.getCustomPalette();
        int blueBackgroundColor = 0x94ddff;
        byte[] blueBackgroundColorByteArray = hexColorToBytes(blueBackgroundColor);
        customPalette.setColorAtIndex(paletteIndex, blueBackgroundColorByteArray[0], blueBackgroundColorByteArray[1], blueBackgroundColorByteArray[2]);
        cellStyle1.setFillForegroundColor(paletteIndex);
        cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        int outputRowNum = 1;
        for (Dormitory dorm: dormList) {
            boolean atLeast1StudentTested = dorm.atLeast1StudentTested();
            List<Student> students = dorm.getStudentListList();
            for (Student s: students) {
                Row studentRow = sheet.createRow(outputRowNum);
                Cell cell0 = studentRow.createCell(0);
                cell0.setCellValue(s.getStudentNumber());
                
                Cell cell1 = studentRow.createCell(1);
                cell1.setCellValue(s.getName());
                
                Cell cell2 = studentRow.createCell(2);
                cell2.setCellValue(dorm.getBuilding());
                
                Cell cell3 = studentRow.createCell(3);
                cell3.setCellValue(dorm.getDormNumber());
                
                Cell cell4 = studentRow.createCell(4);
                cell4.setCellValue(s.getBedNumber());
                
                Cell cell5 = studentRow.createCell(5);
                cell5.setCellValue(s.getPhoneNumber());
                
                Cell cell6 = studentRow.createCell(6);
                cell6.setCellValue(s.isNukeAcidTested());
                
                Cell cell7 = studentRow.createCell(7);
                cell7.setCellValue(atLeast1StudentTested);
                
                if (atLeast1StudentTested) {
//                    cell0.setCellStyle(cellStyle1);
//                    cell1.setCellStyle(cellStyle1);
//                    cell2.setCellStyle(cellStyle1);
//                    cell3.setCellStyle(cellStyle1);
//                    cell4.setCellStyle(cellStyle1);
//                    cell5.setCellStyle(cellStyle1);
//                    cell6.setCellStyle(cellStyle1);
                    cell7.setCellStyle(cellStyle1);
                }
                
                outputRowNum++;
            }
        }
        System.out.println("written " + outputRowNum + " rows");
        return wb;
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
    
    private byte[] hexColorToBytes(int hexColor) {
        byte[] rgb = new byte[3];
        int red = (hexColor & 0xff0000) >> 16;
        int green = (hexColor & 0x00ff00) >> 8;
        int blue = hexColor & 0x0000ff;
        rgb[0] = (byte) (red);
        rgb[1] = (byte) (green);
        rgb[2] = (byte) (blue);
        return rgb;
    }  
}
