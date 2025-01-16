package cn.edu.njfu.zyf.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.config.Constants;
import cn.edu.njfu.zyf.service.StudentAttenderService;
import io.swagger.annotations.ApiOperation;

@RestController
public class StudentAttenderController {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(GeneralAttenderController.class);
	@Autowired
	private StudentAttenderService service;	

    @ApiOperation(value = "查询签到人数")
    @RequestMapping(value = Constants.STUDENT_NUM_OF_SIGIN_IN, method = RequestMethod.GET)
    public int getCheckedNum(HttpServletRequest req, String conferenceID) {
    	if(conferenceID == null || conferenceID.equals("")) {
			return -1;
		}
        int checkins = service.getSignedCount(conferenceID);
        logger.info(req.getLocalAddr() + "会议" + conferenceID + "已签到人数：" + checkins);
        return checkins;
    }
    
	@ApiOperation(value = "下载该会议人员签到情况")
	@RequestMapping(value = Constants.STUDENT_DOWNLOAD, method = RequestMethod.GET)
	public String findUntestedDorm(HttpServletRequest request, HttpServletResponse response, String conferenceID) throws IOException {
		if(conferenceID == null || conferenceID.equals("")) {
			return "must include conferenceID in your url";
		}
		
	    Workbook wb = service.createAttenderWorkbookByConferenceID(conferenceID);
	    String fileName = conferenceID + "签到情况";
	    if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
        }
	    
	    response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        response.setContentType("multipart/form-data");
	    OutputStream os = response.getOutputStream();
	    wb.write(os);
	    os.flush();
	    os.close();
	    return "hi";
	}
}
