package cn.edu.njfu.zyf.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.njfu.zyf.service.GeneralAttenderService;
import io.swagger.annotations.ApiOperation;

@RestController
public class GeneralAttenderController {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(GeneralAttenderController.class);
	@Autowired
	private GeneralAttenderService service;	
	
	private volatile static String indexTemplate = null;
	
    private final Path imageStoragePath = Paths.get(".\\static\\");

    @ApiOperation(value = "展示通用签到二维码")
    @RequestMapping(value = "/generalQRCode", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> qrImage(HttpServletResponse res) {
    	try {
    		res.setContentType("image/apng");
            Path file = imageStoragePath.resolve("GeneralConferenceCheckinQRCode.png");
            Resource resource = new UrlResource(file.toUri());
 
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
 
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    	
    }
    
    @ApiOperation(value = "查询签到人数")
    @RequestMapping(value = "/generalNumOfSignIns", method = RequestMethod.GET)
    public int getCheckedNum(HttpServletRequest req, String conferenceID) {
    	if(conferenceID == null || conferenceID.equals("")) {
			return -1;
		}
        int checkins = service.getSignedCount(conferenceID);
        logger.info(req.getLocalAddr() + "会议" + conferenceID + "已签到人数：" + checkins);
        return checkins;
    }
    
    @ApiOperation(value = "展示大屏页面")
    @RequestMapping(value = "/generalIndex", method = RequestMethod.GET)
    public String getIndex(String conferenceID) {
    	if(conferenceID == null || conferenceID.equals("")) {
			return "must include conferenceID in your url";
		}
        return getIndexPage2(conferenceID);
    }
    
	@ApiOperation(value = "下载该会议人员签到情况")
	@RequestMapping(value = "/download", method = RequestMethod.GET)
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
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
        response.setContentType("multipart/form-data");
	    OutputStream os = response.getOutputStream();
	    wb.write(os);
	    os.flush();
	    os.close();
	    return "hi";
	}
    

    private String getIndexPage2(String conferenceID) {
    	return getIndexPageFromResource().replace("{conferenceID}", conferenceID);
    }
    
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
