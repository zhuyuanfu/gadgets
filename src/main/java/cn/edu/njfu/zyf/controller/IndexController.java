package cn.edu.njfu.zyf.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.config.Constants;
import cn.edu.njfu.zyf.model.LowCodeApplicationToUrlMapper;
import io.swagger.annotations.ApiOperation;

@RestController
public class IndexController {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	private volatile static String indexTemplate = null;
	
	private static final HashMap<String, LowCodeApplicationToUrlMapper> app2UrlMap = new HashMap<>();
	
	static {
		app2UrlMap.put(Constants.GENERAL_APPLICATION_NAME, 
				new LowCodeApplicationToUrlMapper(
						Constants.GENERAL_APPLICATION_NAME, 
						Constants.GENERAL_NUM_OF_SIGIN_IN, 
						Constants.GENERAL_DOWNLOAD,
						Constants.GENERAL_QR_CODE_FILE_NAME
						)
				);
		app2UrlMap.put(Constants.STUDENT_APPLICATION_NAME, 
				new LowCodeApplicationToUrlMapper(
						Constants.STUDENT_APPLICATION_NAME, 
						Constants.STUDENT_NUM_OF_SIGIN_IN, 
						Constants.STUDENT_DOWNLOAD,
						Constants.STUDENT_QR_CODE_FILE_NAME
						)
				);
		app2UrlMap.put(Constants.ART_COLLEGE_APPLICATION_NAME, 
				new LowCodeApplicationToUrlMapper(
						Constants.ART_COLLEGE_APPLICATION_NAME, 
						Constants.ART_COLLEGE_NUM_OF_SIGN_IN, 
						Constants.ART_COLLEGE_DOWNLOAD,
						Constants.ART_COLLEGE_QR_CODE_FILE_NAME
						)
				);
	}
	
    @ApiOperation(value = "展示签到大屏页面")
    @RequestMapping(value = "/conferenceCheckingInIndex", method = RequestMethod.GET)
    /**
     * @param applicationName 可能有两个值，对应“通用会议签到”和“艺术院会议签到”：
     * @Param applicationName general和artCollege
     * @param conferenceID 例：测试会议（2025年1月12日）
     * @return
     */
    public String getIndex(String lowCodePlatformApplicationName, String conferenceID) {
    	if (lowCodePlatformApplicationName == null || lowCodePlatformApplicationName.equals("")) {
			return "You MUST include low-code-platform application name in your url.";
    	}
    	if(conferenceID == null || conferenceID.equals("")) {
			return "You MUST include conferenceID in your url.";
		}
        return getIndexPage(lowCodePlatformApplicationName, conferenceID);
    }
    

    private String getIndexPage(String lowCodePlatformApplicationName, String conferenceID) {
    	LowCodeApplicationToUrlMapper mapper = app2UrlMap.get(lowCodePlatformApplicationName);
    	return getIndexPageFromResource()
    			.replace("{conferenceID}", conferenceID)
    			.replace("{queryNumberOfSignInsUrl}", mapper.getQueryNumberOfSignInsUrl())
    			.replace("{downloadUrl}", mapper.getDownloadUrl())
    			.replace("{imageFileName}", mapper.getPictureFileName());
    }
    
    
    /**
     * 从配置文件读取原始模板
     * @return
     */
    private static String getIndexPageFromResource() {
    	if (indexTemplate != null) return indexTemplate;
    	
    	ResourceLoader resourceLoader = new DefaultResourceLoader();
    	Resource resource = resourceLoader.getResource("classpath:static/indexTemplate.html");
    	InputStream is = null;
    	InputStreamReader isr = null;
    	BufferedReader br = null;
    	try {
    		is = resource.getInputStream();
    		isr = new InputStreamReader(is, "UTF-8");
    		br = new BufferedReader(isr);
    		String line = null;
    		StringBuilder indexTemplateBuilder = new StringBuilder();
    		while((line = br.readLine()) != null) {
    			indexTemplateBuilder.append(line).append('\n');
    		}
    		indexTemplate = indexTemplateBuilder.toString();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
    		try {
				if (br != null) br.close();
				if (isr != null) isr.close();
				if (is != null) is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return indexTemplate;
    }
    
}
