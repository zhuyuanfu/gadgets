package cn.edu.njfu.zyf.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class ImageController {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ImageController.class);
	
    private final Path imageStoragePath = Paths.get(".\\src\\main\\resources\\static\\");

    @ApiOperation(value = "根据图片文件名返回一张图片")
    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> qrImage(HttpServletResponse res, String imageFileName) {
    	logger.info("Requesting pic file: " + imageFileName);
    	try {
    		res.setContentType("image/apng");
            Path file = imageStoragePath.resolve(imageFileName);
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
    
}
