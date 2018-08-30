package com.zhiyou100.rmlb.emall.sbt.manager.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhiyou100.rmlb.sbt.emall.common.utils.FastDFSClient;
import com.zhiyou100.rmlb.sbt.emall.common.utils.JsonUtils;



@RestController
public class PicController {
	@Value("${IMG_URL}")
	private String IMG_URL;
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String picUpload(MultipartFile uploadFile) {
		//把图片上传到服务器 初始化加载配置文件
		try {
			//把图片上传到服务器 初始化加载配置文件
			FastDFSClient dfsClient=new FastDFSClient("classpath:conf/fastDFS.conf");
			//取得文件的扩展名
			String fileName=uploadFile.getOriginalFilename();
			String extName=fileName.substring(fileName.lastIndexOf(".")+1);
			//得到一个图片的地址和文件名
			String url=dfsClient.uploadFile(uploadFile.getBytes(), extName);
			//补充完成的url
			url=IMG_URL+url;
			Map map=new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			Map map=new HashMap<>();
			map.put("error", 1);
			map.put("message", "图片上传失败");
			return JsonUtils.objectToJson(map);
		}
	}
}
