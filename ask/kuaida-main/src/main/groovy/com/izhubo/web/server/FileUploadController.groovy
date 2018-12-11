package com.izhubo.web.server

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver

import com.izhubo.common.doc.Param
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.web.BaseController
import com.izhubo.web.api.Web

/**
 * 
* @ClassName: MainController 

* @Description: api接口
* @author shihongjie
* @date 2015年5月21日 下午2:26:25 
*
 */
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class FileUploadController extends BaseController{
	
	File pic_folder
	
	@Value("#{application['pic.domain']}")
	String pic_domain = "http://img.show.izhubo.com/";
	
	@Value("#{application['pic.folder']}")
	void setPicFolder(String folder){
		pic_folder = new File(folder)
		pic_folder.mkdirs()
		println "初始化图片上传--头像 : ${folder}"
	}
	
	def con(){
		return getResultOK();
	}

	def upload_pic(HttpServletRequest request){
		println "11111111111111111111111111"
		def parse = new CommonsMultipartResolver()
		def req = parse.resolveMultipart(request)

		println "222222222222222222222"
		try{
			Integer id = Web.getCurrentUserId()
			String type = req.getParameter(Param.first)

			String filePath = "${id&63}/${id&7}/${id}"
//			String filePath = "${id&63}/${id&7}/${id}_${type}.jpg"
			for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
				MultipartFile file = entry.getValue()
				def target = new File(pic_folder ,filePath+file.getOriginalFilename())
//				def target = new File(pic_folder ,filePath)
				target.getParentFile().mkdirs()
				file.transferTo(target)
//				break
			}
			[code: 1,data: [pic_url:"${pic_domain}${filePath}?v=${System.currentTimeMillis()}".toString()]]
		}finally {
			parse.cleanupMultipart(req)
		}
	}
	
	
	File file_folder
	
	@Value("#{application['file.folder']}")
	void setVideoFolder(String folder){
		file_folder = new File(folder)
		file_folder.mkdirs()
		println "初始化文件上传: ${folder}"
	}
	
	def upload_file(HttpServletRequest request,HttpServletResponse response){
		def parse = new CommonsMultipartResolver()
//		parse.setMaxUploadSize(10 * 1024 * 1024)  // 10MB
		def req = null

		try{
		
				String user_id = Web.getCurrentUserId();
				String path = user_id.substring(0 , 3) + "/" + user_id;
				
				Long id = System.currentTimeMillis();
				//文件名
				String file_name = null;
				//地址
				String filePath = null;
//					格式
				String ext = null;
				//大小
				Long file_size = 0;
				for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
					MultipartFile file = entry.getValue();
					file_name = file.getOriginalFilename()
					ext = getEXT(file_name);
					file_size = file.size;
					filePath =  path + "/" + UUID.randomUUID().toString() + "." + ext;
					def target = new File(file_folder ,filePath);
					target.getParentFile().mkdirs();
					file.transferTo(target);
					break
				}
	
				return [code: 1,url:"${filePath}".toString(),error:0];
					
			
		}finally {
			if(req != null){
				parse.cleanupMultipart(req);
			}
		}
	}
	
	
	
	
	def upload_single_file(HttpServletRequest request,HttpServletResponse response){
		def parse = new CommonsMultipartResolver()
//		parse.setMaxUploadSize(10 * 1024 * 1024)  // 10MB
		def req = null

		try{
			req = parse.resolveMultipart(request);

			
				String path ="drawboard" ;
				
				Long id = System.currentTimeMillis();
				//文件名
				String file_name = null;
				//地址
				String filePath = null;
//					格式
				String ext = null;
				//大小
				Long file_size = 0;

				for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
					MultipartFile file = entry.getValue();
					file_name = file.getOriginalFilename()
					ext = getEXT(file_name);
					file_size = file.size;
					filePath =  path + "/" + UUID.randomUUID().toString() + "." + ext;
					def target = new File(pic_folder ,filePath);
					target.getParentFile().mkdirs();
					file.transferTo(target);
					break
				}
	
				return [code: 1,url:"${pic_domain}${filePath}".toString(),error:0];
					
			
		}finally {
			if(req != null){
				parse.cleanupMultipart(req);
			}
		}
	}
	
	
	def upload_avatar_file(HttpServletRequest request,HttpServletResponse response){
		def parse = new CommonsMultipartResolver()
//		parse.setMaxUploadSize(10 * 1024 * 1024)  // 10MB
		def req = null

		try{
			req = parse.resolveMultipart(request);

			
				String user_id = Web.getCurrentUserId();
				String path = user_id.substring(0 , 3) + "/" + user_id;
				
				Long id = System.currentTimeMillis();
				//文件名
				String file_name = null;
				//地址
				String filePath = null;
//					格式
				String ext = null;
				//大小
				Long file_size = 0;

				for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
					MultipartFile file = entry.getValue();
					file_name = file.getOriginalFilename()
					ext = getEXT(file_name);
					file_size = file.size;
					filePath =  path + "/" + UUID.randomUUID().toString() + "." + ext;
					def target = new File(pic_folder ,filePath);
					target.getParentFile().mkdirs();
					file.transferTo(target);
					break
				}
	
				return [code: 1,url:"${pic_domain}${filePath}".toString(),error:0];
					
			
		}finally {
			if(req != null){
				parse.cleanupMultipart(req);
			}
		}
	}
	
	// 对扩展名进行小写转换  aa.txt
	public String getEXT(String _file_name) {
		if(StringUtils.isNotBlank(_file_name)){
			return _file_name.substring(_file_name.lastIndexOf(".") + 1, _file_name.length()).toLowerCase();
		}
		return null;
	}
	
	def udemo(){
		
	}
		
}