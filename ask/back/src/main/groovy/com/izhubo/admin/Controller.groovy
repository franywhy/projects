package com.izhubo.admin

import java.text.DateFormat
import java.text.SimpleDateFormat

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver

import com.izhubo.common.util.AuthCode
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.MsgDigestUtil
import com.mongodb.BasicDBObject
import com.mysql.jdbc.integration.jboss.ExtendedMysqlExceptionSorter;

/**
 * date: 13-2-21 下午2:10
 * @author: wubinjie@ak.cc
 */
@Rest
class Controller extends BaseController{

    @Resource
    SysController sysController

    @Value("#{application['pic.domain']}")
    String pic_domain = "http://img.show.izhubo.com/"

    File pic_folder

    @Value("#{application['pic.folder']}")
    void setPicFolder(String folder){
        pic_folder = new File(folder)
        pic_folder.mkdirs()
        println "初始化图片上传目录 : ${folder}"
    }
	

	
    def upload(HttpServletRequest request,HttpServletResponse response){
        def parse = new CommonsMultipartResolver()
//		parse.setMaxUploadSize(10 * 1024 * 1024)  // 10MB
        def req = null

        try{
			req = parse.resolveMultipart(request)
			
            Long id = System.currentTimeMillis()
			
		
			String ext = "";
            String filePath = "${id&63}/${id&7}/${id}";
            for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
                MultipartFile file = entry.getValue()
				String file_name = file.getOriginalFilename()
				 ext = getEXT(file_name);
	
                def target = new File(pic_folder ,filePath+"."+ext)
                target.getParentFile().mkdirs()
                file.transferTo(target)
                break
            }

            String iframeCallBack = req.getParameter("icallback")
            if (StringUtils.isNotBlank(iframeCallBack)){
                def out = response.getWriter()
                out.println("<script>top.${iframeCallBack}({\"code\":1,\"data\":{\"pic_url\":\"${pic_domain}${filePath}.${ext}\"}});</script>")
                out.close()
                return
            }

            [code: 1,url:"${pic_domain}${filePath}.".toString()+ext,error:0]
        }finally {
			if(req != null){
				parse.cleanupMultipart(req)
			}
        }
    }

    def login(HttpServletRequest request){

        String input = request[auth_code]
        if (codeVerifError(request,input)){
            return [code: 30419,msg:'验证码错误']
        }

        String name = request["name"]
		println "name: " + name
		println "password: " + request["password"].toString()
        String password = MsgDigestUtil.SHA.digest2HEX(request["password"].toString())
		println "sha-1 password: " + password
        def user = adminMongo.getCollection("admins").findOne(new BasicDBObject(name:name,password:password))
        if (null == user){
            return [code: 0,msg:'密码错误']
        }

        Map menus = user.get("menus") as Map
        if (menus == null || menus.isEmpty()){
            return [code: 0,msg:'权限不足']
        }

        Map<String,String> sMap = new HashMap()
        sMap.put(_id,user.get(_id) as String)
        sMap.put("nick_name",user.get("nick_name") as String)
        sMap.put("name",user.get("name") as String)
		//公司ID add by shj 2015-01-08  start
        sMap.put("company_id",user.get("company_id") as String);
		//公司ID add by shj 2015-01-08  end
        request.getSession().setAttribute("user",sMap)
		request.getSession().setAttribute("menus",new HashMap(menus))
        Map modules = user.get("modules") as Map
        if (modules != null){
            request.getSession().setAttribute("modules",new HashMap(modules))
        }

        def data = sMap.clone() as Map
        data.put("menus",menus)
       // data.put("modules",menus)
        return [code: 1,data: data]
    }
	
//	def loginByGuid(String ssoguid , HttpServletRequest request){
//
//		if(StringUtils.isBlank(ssoguid)){
//			return false;
//		}
//		
//		def user = adminMongo.getCollection("admins").findOne(new BasicDBObject("ssoguid" : ssoguid))
//		if (null == user){
//			return false;
//		}
//
//		Map menus = user.get("menus") as Map;
//		if (menus == null || menus.isEmpty()){
//			return false;
//		}
//
//		Map<String,String> sMap = new HashMap()
//		sMap.put(_id,user.get(_id) as String)
//		sMap.put("nick_name",user.get("nick_name") as String)
//		sMap.put("name",user.get("name") as String)
//		sMap.put("company_id",user.get("company_id") as String);
//		request.getSession().setAttribute("user",sMap)
//		request.getSession().setAttribute("menus",new HashMap(menus))
//		Map modules = user.get("modules") as Map
//		if (modules != null){
//			request.getSession().setAttribute("modules",new HashMap(modules))
//		}
//
//		return true;
//	}


    def logout(HttpServletRequest request){
        request.getSession().invalidate()
        return [code: 1]
    }


    def modif_pwd(HttpServletRequest req){
        Map user = req.getSession().getAttribute("user") as Map
        if (null == user){
            return [code: 0]
        }
        String pwd = MsgDigestUtil.SHA.digest2HEX(req['password'].toString())
        adminMongo.getCollection('admins')
                .update(new BasicDBObject(_id,user.get(_id) as Integer),new BasicDBObject('$set',[password:pwd]))
        [code: 1]
    }


  /*  def authcode(HttpServletRequest request,HttpServletResponse response){
        String code =  AuthCode.random(4 +  ((int)System.currentTimeMillis()&1) )
        request.getSession().setAttribute(auth_code,code)
        response.addHeader('Content-Type',"image/png")
        Captcha.draw(code,160,48,response.getOutputStream())
    }*/

    def authcode(HttpServletRequest request,HttpServletResponse response){
        String code =  AuthCode.random(4  + ( (int)System.currentTimeMillis()&1))
       // mainRedis.opsForValue().set(KeyUtils.USER.authCode(Web.currentUserId()),code,60L,TimeUnit.SECONDS)
        request.getSession().setAttribute(auth_code,code)
        response.addHeader('Content-Type',"image/png")
        AuthCode.draw(code,160,48,response.getOutputStream())
    }

    def session(HttpServletRequest req){
        def user = req.getSession().getAttribute("user")
        if (null == user){
            return [code: 0]
        }
        def data = new HashMap(user as Map)
        data.put("menus",req.getSession().getAttribute("menus"))
        data.put("modules",req.getSession().getAttribute("modules"))
        [code: 1,data: data]
    }

	
	//add by shihongjie 2015-01-14

	File video_folder

	@Value("#{application['video.folder']}")
	void setVideoFolder(String folder){
		video_folder = new File(folder)
		video_folder.mkdirs()
		println "初始化视频上传目录 : ${folder}"
	}
	
	def upload_video(HttpServletRequest request,HttpServletResponse response){
		def parse = new CommonsMultipartResolver()
//		parse.setMaxUploadSize(10 * 1024 * 1024)  // 10MB
		def req = null

		try{
			req = parse.resolveMultipart(request);
			Map user = req.getSession().getAttribute("user") as Map;
			if(user){
				String company_id = user.get("company_id");
				String company_name = mainMongo.getCollection("company").findOne(new BasicDBObject("_id": company_id))?.get("company_code");
				if(company_name){
					String path = company_name +dir_data();
					
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
						def target = new File(video_folder ,filePath);
						target.getParentFile().mkdirs();
						file.transferTo(target);
						break
					}
		
					String iframeCallBack = req.getParameter("icallback");
					if (StringUtils.isNotBlank(iframeCallBack)){
						def out = response.getWriter();
						//返回文件位置
						//文件格式
						//文件大小
						out.println("<script>top.${iframeCallBack}({\"code\":1,\"data\":{\"pic_url\":\"${filePath}\" , \"ext\":\"${ext}\", \"file_size\":\"${file_size}\", \"file_name\":\"${file_name}\", \"type\":\"courseware\"}});</script>");
						out.close();
						return
					}
					return [code: 1,url:"${filePath}".toString(),error:0];
					
				}
			}
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
	
//	公司名/年-月  格式是否可以  
	private static final DateFormat FORMAT_DIR = new SimpleDateFormat("/yyyy-MM");
	private String dir_data(){
		return FORMAT_DIR.format(new Date());
	}
	
	
//	public static void main(String[] args) {
//		File source = new File("E:\\FDownload\\0001.土豆网-Muki：由咖啡驱动的智能马克杯[高清版].flv");
//		Encoder encoder = new Encoder();
//		long beginTime = System.currentTimeMillis();
//		try {
//			// 获取时长
//			MultimediaInfo m = encoder.getInfo(source);
//			long sTime = m.getDuration();
//			long minute = sTime / 60000;
//			long second = (sTime % 60000) / 1000;
//			System.out.println("视频时长：" + sTime);
//			System.out.println("视频时长：" + minute + ":" + (second < 10 ? "0" + second : second+""));
//			System.out.println("获取时长花费时间是：" + (System.currentTimeMillis() - beginTime));
////			beginTime = System.currentTimeMillis();
////			encoder.encode(source, target, attrs);
////			System.out.println("视频转码花费时间是：" + (System.currentTimeMillis() - beginTime));
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (InputFormatException e) {
//			e.printStackTrace();
//		} catch (EncoderException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	
	//新增上传文档方法
	@Value("#{application['pic.domain']}")
	String doc_domain = "http://img.show.izhubo.com/"

	File doc_folder
	@Value("#{application['pic.folder']}")
	void setDocFolder(String folder){
		doc_folder = new File(folder)
		doc_folder.mkdirs()
		println "初始化文档上传目录 : ${folder}"
	}
	//不判断文件格式后期需优化
	def upload_doc(HttpServletRequest request,HttpServletResponse response){
		def parse = new CommonsMultipartResolver()
//		parse.setMaxUploadSize(10 * 1024 * 1024)  // 10MB
		def req = null

		try{
			req = parse.resolveMultipart(request);
			Map user = req.getSession().getAttribute("user") as Map;
			if(user){
				String company_id = user.get("company_id");
				String company_name = mainMongo.getCollection("company").findOne(new BasicDBObject("_id": company_id))?.get("company_name");
				if(company_name){
					String path = company_name +dir_data();
					
					Long id = System.currentTimeMillis();
					//文件名
					String file_name = null;
					//地址
					String filePath = null;
					String extName= getEXT(file_name);
					for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
						MultipartFile file = entry.getValue();
						file_name = file.getOriginalFilename()
						
						filePath =  path + "/" + UUID.randomUUID().toString() + "." + getEXT(file_name);
						def target = new File(doc_folder ,filePath);
						target.getParentFile().mkdirs();
						file.transferTo(target);
						break
					}
		
					String iframeCallBack = req.getParameter("icallback");
					if (StringUtils.isNotBlank(iframeCallBack)){
						def out = response.getWriter();
						out.println("<script>top.${iframeCallBack}({\"code\":1,\"data\":{\"pic_url\":\"${doc_domain}${filePath}\"}});</script>");
						out.close();
						return
					}
					return [code: 1,url:"${doc_domain}${filePath}".toString(),error:0];
					
				}
			}
		}finally {
			if(req != null){
				parse.cleanupMultipart(req);
			}
		}
	}
	
}
