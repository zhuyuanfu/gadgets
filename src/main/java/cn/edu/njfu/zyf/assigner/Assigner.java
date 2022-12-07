package cn.edu.njfu.zyf.assigner;

import cn.edu.njfu.zyf.model.Dormitory;
import cn.edu.njfu.zyf.model.Student;

public interface Assigner {
    Student getAssignee(Dormitory dorm);
}
