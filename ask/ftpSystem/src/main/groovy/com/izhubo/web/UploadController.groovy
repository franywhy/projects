package com.izhubo.web

import java.awt.image.BufferedImage
import java.io.File;

import javax.servlet.http.HttpServletRequest


import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.ServletRequestUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver

import javax.annotation.Resource
import javax.imageio.ImageIO
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.izhubo.model.Code
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.utils.MD5Util
import com.izhubo.web.api.Web
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.mongodb.util.Base64Codec
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.ParamKey
import com.izhubo.rest.web.Crud
import com.izhubo.common.constant.Constant
import com.izhubo.common.doc.Param
import com.izhubo.rest.common.util.AuthCode
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.*
import com.izhubo.web.BaseController
import com.izhubo.web.interceptor.OAuth2SimpleInterceptor

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
/**
 * 文件上传
 * @ClassName: UploadController 
 * @Description: 文件上传
 * @author shihongjie
 * @date 2015年8月29日 下午3:50:09 
 *
 */
@RestWithSession
class UploadController extends BaseController {

	private static final String FILE_HEADER = "A_";
	private static final int USER_ID_TO_DIR_NUM = 5;

	@Value("#{application['ftp.domain']}")
	private String ftp_domain = "http://www.kjkuaida.twsapp.com/";


	//初始化文件保存位置
	private File file_folder;
	@Value("#{application['file.folder']}")
	void setVideoFolder(String folder){
		file_folder = new File(folder);
		file_folder.mkdirs();
		println "初始化-提问-图片存放路径: ${folder}";
	}

	/**
	 * 文件上传
	 * @date 2015年8月29日 下午3:52:13 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def upload(HttpServletRequest request){
		List list = uploadFile(request);
		return ["code" : 1 , "data" : JSONUtil.beanToJson(list?.get(0))];
	}

	/**
	 * 文件上传 加入MD5值校验
	 * @Description: 文件上传 加入MD5值校验
	 * @date 2015年10月7日 下午5:43:36 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def upload_v150(HttpServletRequest request){
		try {
			List list = uploadFile_v150(request);
			return ["code" : 1 , "data" : JSONUtil.beanToJson(list?.get(0))];
		} catch (FileMD5Exception e) {
			//			e.printStackTrace()
			return ["code" : Code.文件MD5值校验失败 , "data" : Code.文件MD5值校验失败_S , "msg" : Code.文件MD5值校验失败_S];
		}
	}

	/**
	 * 文件上传
	 * @date 2015年8月29日 下午3:52:25 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def uploads(HttpServletRequest request){
		List list = uploadFile(request);
		return ["code" : 1 , "data" : JSONUtil.beanToJson(list)];
	}

	/**
	 * 文件上传 加入MD5值校验
	 * @Description: 文件上传 加入MD5值校验
	 * @date 2015年10月7日 下午5:39:26 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	def uploads_v150(HttpServletRequest request){
		try {
			List list = uploadFile_v150(request);
			return ["code" : 1 , "data" : JSONUtil.beanToJson(list)];
		} catch (FileMD5Exception e) {
			//			e.printStackTrace();
			return ["code" : Code.文件MD5值校验失败 , "data" : Code.文件MD5值校验失败_S , "msg" : Code.文件MD5值校验失败_S];
		}
	}

	/**
	 * 上传文件
	 * @date 2015年8月29日 下午3:46:57 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	private List uploadFile(HttpServletRequest request){
		def parse = new CommonsMultipartResolver();
		def req = parse.resolveMultipart(request);
		String sbFileMD5s = req["sbFileMD5s"];
		String fileTypes = req["fileTypes"];
		String method = req["method"];
		String json = req["json"];
		
		//用户id前USER_ID_TO_DIR_NUM位/课程id
		String path = userIdToString(Web.getCurrentUserId()) + "/" + dateFormate();
		//		String path = "1036/uuid";
		List list = new ArrayList();
		for(Map.Entry<String, MultipartFile> entry : req.getFileMap().entrySet()){

			String file_id = UUID.randomUUID().toString();

			MultipartFile file = entry.getValue();
			//文件原名
			String original_file_name = file.getOriginalFilename();
			//文件格式
			String ext = getEXT(original_file_name);
			//文件大小
			Long file_size = file.size;
			//文件保存的名字
			String filePath =  path + "/" + file_id + "." + ext;
			def target = new File(file_folder ,filePath);
			target.getParentFile().mkdirs();
			file.transferTo(target);

			Map fileMap = new HashMap();
			//文件原名称
			fileMap["original_file_name"] = original_file_name;
			//文件大小
			fileMap["file_size"] = file_size;
			//文件地址
			fileMap["url"] = filePath;
			//文件地址
			fileMap["absolute_url"] = ftp_domain + filePath;
			//文件id
			fileMap["_id"] = file_id;
			list.add(fileMap);
		}
		return list;
	}

	//TODO 版本号
	/**
	 * 上传文件
	 * @Description: 上传文件 加入MD5值
	 * @date 2015年10月7日 下午5:35:27 
	 * @param @param request
	 * @param @return 
	 * @throws
	 */
	private List uploadFile_v150(HttpServletRequest request) throws FileMD5Exception{
		def parse = new CommonsMultipartResolver();
		def req = parse.resolveMultipart(request);
		String sbFileMD5s = req["sbFileMD5s"];
		String fileTypes = req["fileTypes"];
		String method = req["method"];
		String json = req["json"];
		String[] fileMD5Array = null;
		if(StringUtils.isBlank(sbFileMD5s)){
			//TODO MD5校验值未上传
			throw new FileMD5Exception();
		}
		fileMD5Array = sbFileMD5s.split(",");
		//用户id前USER_ID_TO_DIR_NUM位/课程id
		String path = userIdToString(Web.getCurrentUserId()) + "/" + dateFormate();
		//		String path = "1036/uuid";
		List list = new ArrayList();
		int fileMD5Array_i = 0;
		for(Map.Entry<String, MultipartFile> entry : req.getFileMap().entrySet()){

			String file_id = UUID.randomUUID().toString();

			MultipartFile file = entry.getValue();
			//文件原名
			String original_file_name = file.getOriginalFilename();
			//文件格式
			String ext = getEXT(original_file_name);
			//文件大小
			Long file_size = file.size;
			//文件保存的名字
			String filePath =  path + "/" + file_id + "." + ext;
			File target = new File(file_folder ,filePath);
			target.getParentFile().mkdirs();
			file.transferTo(target);

			String XCMD5 =  MD5Util.getFileMD5String(target)
			if(!MD5Util.checkPassword(fileMD5Array[fileMD5Array_i++], MD5Util.getFileMD5String(target))){
				//TODO MD5校验值未上传
				throw new FileMD5Exception();
			}

			Map fileMap = new HashMap();
			//文件原名称
			fileMap["original_file_name"] = original_file_name;
			//文件大小
			fileMap["file_size"] = file_size;
			//文件相对地址
			fileMap["url"] = filePath;
			//文件绝对地址
			fileMap["absolute_url"] = ftp_domain + filePath;
			//文件id(文件名不含文件后缀)
			fileMap["_id"] = file_id;
			list.add(fileMap);
		}
		return list;
	}


	//用户id变成string 截取用户前5位id
	def userIdToString(int _id){
		String id = _id + "";
		if(id.length() >= USER_ID_TO_DIR_NUM){
			id = id.substring(USER_ID_TO_DIR_NUM);
		}
		return FILE_HEADER + id;
	}

	// 对扩展名进行小写转换  aa.txt
	public String getEXT(String _file_name) {
		if(StringUtils.isNotBlank(_file_name)){
			return _file_name.substring(_file_name.lastIndexOf(".") + 1, _file_name.length()).toLowerCase();
		}
		return ".png";
	}

	public String dateFormate(){
		java.text.DateFormat format = new java.text.SimpleDateFormat("yyyyMM");
		return format.format(new Date());
	}




	@TypeChecked(TypeCheckingMode.SKIP)
	def cut_pic(HttpServletRequest request){
		String allow_url = request.getParameter('url')
		//是否生成新图 1为生成新图
		String isNew = request.getParameter('new')
		if( ! allow_url.startsWith(ftp_domain)){
			return [code:0,msg: "${allow_url} is NOT allowed."]
		}
		int x = Integer.valueOf(request.getParameter('x'))
		int y = Integer.valueOf(request.getParameter('y'))
		int w = Integer.valueOf(request.getParameter('w'))
		int h = Integer.valueOf(request.getParameter('h'))
		int rw = ServletRequestUtils.getIntParameter(request, 'rw', 400)
		int rh = ServletRequestUtils.getIntParameter(request, 'rh', 300)
		def url = new URL(allow_url)

		String ext ="."+getEXT(allow_url);
		BufferedImage img = ImageIO.read(url)
		String fpath = url.getPath().replace(ext,'').replace(ftp_domain, "").substring(1)+"_${rw}${rh}"+ext;

		File file = new File(file_folder,fpath)
		def cutImg = AuthCode.cutImage(img,x,y,w,h)
		file.getParentFile().mkdirs()
		AuthCode.writeJpeg(AuthCode.compressImage(cutImg,rw,rh),new FileOutputStream(file))
		[code: 1,data:
			[pic_url:"${ftp_domain}${fpath}?v=${w}_${h}_${System.currentTimeMillis()}".toString(),
				path:fpath ]]

	}


}

class FileMD5Exception extends Exception{}
