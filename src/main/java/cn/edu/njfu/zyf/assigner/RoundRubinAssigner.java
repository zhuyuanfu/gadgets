package cn.edu.njfu.zyf.assigner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.edu.njfu.zyf.model.Dormitory;
import cn.edu.njfu.zyf.model.Student;

/**
 * 从宿舍里依次指定一个学生去做核酸。
 * 
 * @author Administrator
 *
 */
@Component
public class RoundRubinAssigner implements Assigner{

    private static final LocalDate pivot = LocalDate.of(1970, 1, 1);
    
    @Override
    public Student getAssignee(Dormitory dorm) {
        LocalDate now = LocalDate.now();
        long days = pivot.until(now, ChronoUnit.DAYS);
        List<Student> studentList = dorm.getStudentListList();
        int numOfStudents = studentList.size();
        if (numOfStudents == 0) return null;
        int indexOfAssignee = (int) (days % numOfStudents);
        return studentList.get(indexOfAssignee);
    }
}
