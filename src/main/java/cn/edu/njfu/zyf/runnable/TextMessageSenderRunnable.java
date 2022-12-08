package cn.edu.njfu.zyf.runnable;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.edu.njfu.zyf.assigner.Assigner;
import cn.edu.njfu.zyf.model.Dormitory;
import cn.edu.njfu.zyf.model.Student;
import cn.edu.njfu.zyf.notification.TextMessageSender;

public class TextMessageSenderRunnable implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(TextMessageSenderRunnable.class);
    
    private TextMessageSender sender;
    private Assigner assigner;
    private List<Dormitory> untestedDorms;
    
    private String template = "齐心协力，共抗疫情。宿舍%s今天没有人接受核酸检测，请被选中的你明天抽时间去大学生活动中心接受一次核酸检测，为全宿舍挣一份安心。";
    
    public TextMessageSenderRunnable(TextMessageSender sender, 
            Assigner assigner,
            List<Dormitory> untestedDorms) {
        this.sender = sender;
        this.assigner = assigner;
        this.untestedDorms = untestedDorms;
    }
    @Override
    public void run() {
        int untestedDormNumber = untestedDorms.size();
        logger.info("Starting to notify "+ untestedDormNumber + " untested dorms.");
        for (Dormitory dorm: untestedDorms) {
            Student student = assigner.getAssignee(dorm);
            String text = String.format(template, dorm.getDormNumber());
            
            // sender.sendMessage(text, student);
            // logger.info("sent nofity to student " + student.getName());
        }
        
        
        logger.info("All("+ untestedDormNumber +") untested dorms notified.");
    }

}
