package cn.edu.njfu.zyf.service.impl;

import org.springframework.stereotype.Service;
import cn.edu.njfu.zyf.service.GadgetService;

@Service
public class GadgetServiceImpl implements GadgetService{

	public String hello() {
		return "Hello Springboot! 你好世界！";
	}
}
