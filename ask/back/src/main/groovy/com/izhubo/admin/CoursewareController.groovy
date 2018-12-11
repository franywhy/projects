package com.izhubo.admin

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.DateFormat
import java.text.SimpleDateFormat

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartResolver

import com.izhubo.model.CoursewareStates
import com.izhubo.model.OpType
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.Crud
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.QueryBuilder



/**
 * 课程管理
 * date: 2015-01-08
 * @author: shj
 */
@RestWithSession
class CoursewareController extends BaseController{

	static final  Logger logger = LoggerFactory.getLogger(CoursewareController.class);
	private static final String USYSTEM_UP = 'usystem:upload:';
	@Resource
	KGS coursewareKGS;
	@Resource
	KGS coursewareIValueKGS;
	DBCollection _courseware(){
		return mainMongo.getCollection('courseware');
	}
	DBCollection _admins(){
		return adminMongo.getCollection('admins');
	}
	DBCollection _users(){
		return mainMongo.getCollection('users');
	}

	
	
	
	def list(HttpServletRequest req){
		//登录用户信息
		Map<String,String> sMap = (Map<String,String>)req.getSession().getAttribute("user");
		//默认是公司人员的管理人员操作
		String company_id = sMap.get("company_id");
		
		//查询条件初始化
		QueryBuilder query = QueryBuilder.start();
		query = Web.fillTimeBetween(query, "update_time", req);
		query.and("company_id").is(company_id);
		
		
        intQuery(query,req,"courseware_code");
        stringQuery(query,req,"courseware_name");
        intQuery(query,req,"teacher_id");
		
		Integer user_id = req["user_id"] as Integer;
		if(user_id){
			query.and("manage_info.create_user_id").is(user_id);
		}
		
		Integer update_id = req["update_id"] as Integer;
		if(user_id){
			query.and("manage_info.update_id").is(user_id);
		}
	
		
	    String review_upload_flag =req.getParameter("review_upload_flag");
		if(StringUtils.isNotBlank(review_upload_flag)){
			query.and("manage_info.upload_flag").is(Boolean.valueOf(review_upload_flag) );
		}
		String review_audit_flag =req.getParameter("review_audit_flag");
		if(StringUtils.isNotBlank(review_audit_flag)){
			query.and("manage_info.audit_flag").is(Boolean.valueOf(review_audit_flag));
		}
		
		
		Crud.list(req,_courseware(),query.get(),null,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){ 
				
				Integer teacher_id = obj['teacher_id'] as Integer;
				
				Map manage_info = (Map) obj['manage_info'];
				Integer create_user_id = manage_info['create_user_id'] as Integer;
				Integer update_user_id = manage_info['update_user_id'] as Integer;
				Integer states = obj['states'] as Integer;
//				println obj['states'];
//				def dd =  obj['states'];
				if(teacher_id){
					obj.put(
						"teacher_name", 
						_users().findOne(new BasicDBObject("_id" : teacher_id) , new BasicDBObject("nick_name":1))?.get("nick_name")
						);
				}
				if(create_user_id){
					manage_info.put("create_user_name", _admins().findOne(new BasicDBObject("_id" : create_user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(update_user_id){
					manage_info.put("update_user_name", _admins().findOne(new BasicDBObject("_id" : update_user_id as Integer) , new BasicDBObject("nick_name":1))?.get("nick_name"));
				}
				if(states!=null){
					if(states == CoursewareStates.上传成功.ordinal()){
						obj['states_name'] = "同步成功";
					}else if(states == CoursewareStates.未通知.ordinal()){
						obj['states_name'] = "未同步";
					}
				}
			}
			
		}
	}
	//教师下拉框列表
	def teacher_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user && user["company_id"]){
			List<DBObject> list = mainMongo.getCollection('users').find(
				$$("priv" : UserType.主播.ordinal(), "company_id" : user.get("company_id").toString()),
				$$("_id" : 1 , "nick_name" : 1)
				).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	
	def user_list(HttpServletRequest req){
		Map user = req.getSession().getAttribute("user") as Map;
		if(user && user["company_id"]){
			List<DBObject> list = adminMongo.getCollection('admins').find(
					$$("company_id" : user.get("company_id").toString()),
					$$("_id" : 1 , "nick_name" : 1)
					).sort($$(_id : 1)).limit(1000).toArray();
			return ["code" : 1, "data" : list];
		}
		return null;
	}
	
	def find_sid(HttpServletRequest req){
		String sid = req.getSession().getId();
		return [code : 1 , data :[ "sid" : sid]];
	}
	
	def del(HttpServletRequest req){
		String id = req[_id]
		if(StringUtils.isEmpty(id))
			return [code:0]
		mainMongo.getCollection('courseware').remove(new BasicDBObject(_id,id))
		Crud.opLog(OpType.courseware,[del:id]);
		return OK();
	}
	
	def add(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		String company_id = user.get("company_id");
		//课件信息
		def courseware = $$("_id" : UUID.randomUUID().toString() , 'company_id' : company_id);
		//课件编号
		courseware.append("courseware_code", coursewareKGS.nextId());
		//上传编号
		courseware.append("courseware_ivalue", coursewareIValueKGS.nextId());
		//教师ID
		String teacher_id = request.getParameter("teacher_id");
		if(StringUtils.isNotBlank(teacher_id)){
			courseware.append("teacher_id", teacher_id as Integer);
		}
		//课件名称
		String courseware_name = request.getParameter("courseware_name");
		courseware.append("courseware_name", courseware_name);
		//课件简介
//		String courseware_introduction = request.getParameter("courseware_introduction");
//		courseware.append("courseware_introduction", courseware_introduction);
		//课件地址
		courseware.append("courseware_url", request.getParameter("courseware_url"));
		//格式
		courseware.append("ext", request.getParameter("ext"));
		//大小
		courseware.append("file_size", request.getParameter("file_size"));
		//时长
		courseware.append("video_time", request.getParameter("video_time"));
		//图片地址
//		String pic_url = request.getParameter("pic_url");
//		courseware.append("pic_url", pic_url);
		//0.未上传 1.处理中 2.上传成功 3.上传失败
		courseware.put("states" , 0);
		//时间
		Long now = System.currentTimeMillis();
		//通用信息
		Map manage_info = new HashMap();
//		创建日期
		manage_info.put("timestamp", now);
		//创建人id
		manage_info.put("create_user_id", user.get("_id") as Integer);
		//修改时间
		manage_info.put("update_date", now);
		//修改人ID
		manage_info.put("update_user_id", user.get("_id") as Integer);
		//提交标记
		Boolean upload_flag = Boolean.valueOf(request.getParameter("upload_flag"));
		//提交标记
		manage_info.put("upload_flag" , upload_flag);
		//审核标记
		manage_info.put("audit_flag" , false);
		if(upload_flag){
			//提交人
			manage_info.put("upload_user_id" , user.get("_id") as Integer);
			//提交时间
			manage_info.put("upload_date" , now);
		}
		courseware.append("manage_info", manage_info);
		_courseware().save(courseware);
		Crud.opLog(OpType.courseware,[save:request["_id"]]);
		return OK();
	}
	
	/**
	 * 保存
	 * teacher_id
	 * user_id
	 * company_id
	 * courseware_name
	 * courseware_url
	 * upload_flag
	 * @param smap
	 * @return
	 */
	private boolean save(Map smap){
		//课件信息
		def courseware = $$("_id" : UUID.randomUUID().toString() , 'company_id' : smap["company_id"]);
		//课件编号
		courseware.append("courseware_code", coursewareKGS.nextId());
		//上传编号
		courseware.append("courseware_ivalue", coursewareIValueKGS.nextId());
		//教师ID
		courseware.append("teacher_id",  smap["teacher_id"] as Integer);
		//课件名称
		courseware.append("courseware_name",  smap["courseware_name"]);
		//课件地址
		courseware.append("courseware_url", smap["courseware_url"]);
		//0.未上传 1.处理中 2.上传成功 3.上传失败
		courseware.put("states" , 0);
		
		//时长
		courseware.put("video_time" , smap["video_time"]);
		//类型
		courseware.put("ext" , smap["ext"]);
		//文件大小
		courseware.put("file_size" , smap["file_size"] as Long);
		
		
		//时间
		Long now = System.currentTimeMillis();
		//通用信息
		Map manage_info = new HashMap();
//		创建日期
		manage_info.put("timestamp", now);
		//创建人id
		manage_info.put("create_user_id", smap["user_id"] as Integer);
		//修改时间
		manage_info.put("update_date", now);
		//修改人ID
		manage_info.put("update_user_id", smap["user_id"] as Integer);
		//提交标记
		Boolean upload_flag = Boolean.valueOf(smap["upload_flag"].toString());
		//提交标记
		manage_info.put("upload_flag" , upload_flag);
		//审核标记
		manage_info.put("audit_flag" , false);
		
		
		if(upload_flag){
			//提交人
			manage_info.put("upload_user_id" , smap["user_id"] as Integer);
			//提交时间
			manage_info.put("upload_date" , now);
		}
		courseware.append("manage_info", manage_info);
		_courseware().save(courseware);
		Crud.opLog(OpType.courseware,[save:smap["user_id"]]);
		
		return true;
	}
	
	def edit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		BasicDBObject courseware = new BasicDBObject();
		//教师ID
		String teacher_id = request.getParameter("teacher_id");
		if(StringUtils.isNotBlank(teacher_id)){
			courseware.append("teacher_id", teacher_id as Integer);
		}
		//课件名称
		String courseware_name = request.getParameter("courseware_name");
		if(StringUtils.isNotBlank(courseware_name)){
			courseware.append("courseware_name", courseware_name);
		}
//		//课件简介
//		String courseware_introduction = request.getParameter("courseware_introduction");
//		if(StringUtils.isNotBlank(courseware_introduction)){
//			courseware.append("courseware_introduction", courseware_introduction);
//		}
		courseware.append("courseware_url", request.getParameter("courseware_url"));
//		//图片地址
//		String pic_url = request.getParameter("pic_url");
//		if(StringUtils.isNotBlank(pic_url)){
//			courseware.append("pic_url", pic_url);
//		}
		Long now = System.currentTimeMillis();
		//时长
		courseware.append("video_time", request.getParameter("video_time"));
		//类型
		if(request["ext"]){
			courseware.append("ext" , request["ext"]);
		}
		//文件大小
		if(request["file_size"] && StringUtils.isNotBlank(request["file_size"].toString())){
			courseware.append("file_size" , request["file_size"] as Long);
		}
		
		//修改时间
		courseware.append("manage_info.update_time", now);
		//修改人ID
		courseware.append("manage_info.update_id", user.get("_id") as Integer);
		//提交标记
		Boolean upload_flag = Boolean.valueOf(request.getParameter("upload_flag"));
		courseware.append("manage_info.upload_flag", upload_flag);
		if(upload_flag){
			//提交人
			courseware.append("manage_info.upload_user_id", user.get("_id") as Integer);
			//提交时间
			courseware.append("manage_info.upload_date", now);
		}
		String _id = request.getParameter("_id"); 
		_courseware().update(new BasicDBObject("_id":request.getParameter("_id")),new BasicDBObject('$set':courseware));
		Crud.opLog(OpType.courseware,[update:request["_id"]]);
		return OK();
	}

	/**
	 * 提交
	 */
	def submit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		_courseware().update(
			new BasicDBObject("_id":request.getParameter("_id") , "manage_info.upload_flag" : false),
			new BasicDBObject('$set':
				new BasicDBObject(
					"manage_info.upload_flag" : true,
					//提交人
					"manage_info.upload_user_id": user.get("_id") as Integer,
					"manage_info.upload_date" : System.currentTimeMillis()
					)
				));
		return OK();
	}
	
	
	/**
	 * 审核
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def audit(HttpServletRequest request) {
		String _id = request['_id'];
		//CDN处理完成校验
		if(!isReadyPublish(_id)){
			return [code : 0 , msg : '课件正在维护！请稍后再试！'];
		}
		def map = Web.upload("publish?_id=${_id}");
//			def map = Web.upload("publish?file_name=${courseware_url}&file_code=${courseware_code}");
		if(map){
			if(map['code'] as Integer == 1 ){
				Map user = (Map) request.getSession().getAttribute("user");
				BasicDBObject courseware = new BasicDBObject();
				Long now = System.currentTimeMillis();
				courseware.put("manage_info.audit_flag",true);
				//提交人
				courseware.put("manage_info.upload_user_id" , user.get("_id") as Integer);
				//提交时间
				courseware.put("manage_info.upload_date" , now);
				
				_courseware().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':courseware));
				Crud.opLog(OpType.courseware , [ update : request["_id"] ]);
			}else{
				return [code : 0  , msg : map['data']];
			}
		}else{
			return [code : 0  , msg : "视频服务器反馈异常，请联系管理员!"];
		}
		return OK();
	}
	
	/**
	 * 收回
	 */
	def recovery(HttpServletRequest request){
		
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		String _id = request.getParameter("_id");
		//CDN处理完成校验
		if(!isReadyPublish(_id)){
			return [code : 0 , msg : '课件正在维护！请稍后再试！'];
		}
		
		_courseware().update(
			new BasicDBObject("_id": _id , "manage_info.upload_flag" : true),
			new BasicDBObject('$set':new BasicDBObject("manage_info.upload_flag" : false,)
				));
		return OK();
	}
	
	/**
	 * 反审核
	 */
	def reaudit(HttpServletRequest request){
		Map user = (Map) request.getSession().getAttribute("user");
		if(user == null){
			return OK();
		}
		def _id =request.getParameter("_id");
		//查询课件
		def cw = _courseware().findOne(
			new BasicDBObject("_id" : _id),
			new BasicDBObject("courseware_url":1 , "courseware_code":1)
				);
		if(cw){
			//引用校验
			if(isUse(_id)){
				return [code : 0 , msg : '课件已被引用，无法反审核！取消引用才可操作！'];
			}
			//CDN处理完成校验
			if(!isReadDelete(_id)){
				return [code : 0 , msg : '课件正处于上传或上传列队中!请稍后在试!'];
			}
			//课件地址
			String courseware_url = cw.get('courseware_url');
			//课件编码
			Integer courseware_code = cw.get('courseware_code') as Integer;
			//通知视频服务器 删除CDN位置
			def map = Web.upload("delete?_id=${_id}");
			if(map){
				//删除成功
				if(map['code'] as Integer == 1 ){
					//更新数据库
					_courseware().update(
						new BasicDBObject("_id":_id, "manage_info.audit_flag" : true),
						new BasicDBObject('$set':
							new BasicDBObject(
								"manage_info.audit_flag" : false,
								)
							));
				}else{
					return [code : 0  , msg : map['data']];
				}
			}else{
				return [code : 0  , msg : "视频服务器反馈异常，请联系管理员!"];
			}
		}
		
		return OK();
	}
	
	//课件是否被引用
	private Boolean isUse(String courseware_id){
		Boolean isUse = false;
		DBObject course = mainMongo.getCollection('course').findOne(new BasicDBObject("courseware_id" : courseware_id));
		if(course == null){
			course = mainMongo.getCollection('course').findOne(new BasicDBObject("chapter.chapter_courseware.courseware_id" : courseware_id));
		}
		if(course){
			isUse = true;
		}
		return isUse;
	}
	
	//是否上传成功
	private Boolean isReadDelete(String courseware_id){
		DBObject course = mainMongo.getCollection('courseware').findOne(new BasicDBObject("_id" : courseware_id , 'states': CoursewareStates.上传成功.ordinal()));
		if(course){
			return true;
		}else{
			return false;
		}
	}
	
	
	//是否可以上传
	private Boolean isReadyPublish(String courseware_id){
		DBObject course = mainMongo.getCollection('courseware').findOne(new BasicDBObject("_id" : courseware_id , 'states': CoursewareStates.未通知.ordinal()));
		if(course){
			return true;
		}else{
			return false;
		}
	}
	
	
	File video_folder
	
	@Value("#{application['video.folder']}")
	void setVideoFolder(String folder){
		video_folder = new File(folder)
		video_folder.mkdirs()
		println "初始化视频上传目录 : ${folder}"
	}
	
	//批量上传文件
	def addBatch(HttpServletRequest request , HttpServletResponse response){
		def parse = new CommonsMultipartResolver();
		def req = null;
		try{
			req = parse.resolveMultipart(request);
			//基础信息
			Integer user_id = req.getParameter("user_id") as Integer;
//			Integer user_id = req["user_id"] as Integer;
			String teacher_id = req.getParameter("teacher_id") as String;
			Boolean upload_flag = Boolean.valueOf(req.getParameter("upload_flag"));
			//文件名称
			String fileName = req.getParameter("fileName") as String;
			//文件时长
			String video_time = req.getParameter("video_time") as String;
			
//			println "fileName:${fileName}";
//			println "video_time:${video_time}";
			String company_id = _admins().findOne(new BasicDBObject("_id" : user_id) , new BasicDBObject("company_id":1))?.get("company_id");
			//用户信息错误
			if(company_id == null){
				return [code : -3000];
			}
			String company_code = mainMongo.getCollection('company').findOne(new BasicDBObject("_id" : company_id) , new BasicDBObject("company_code":1))?.get("company_code");
			//公司信息错误
			if(company_code == null){
				return [code : -3001];
			}
			
//			return [code : -3001];
			String path = company_code + dir_data();
			//文件名
			String file_name = null;
			//地址
			String filePath = null;
			Long file_size = 0;
			for(Map.Entry<String, MultipartFile> entry  : req.getFileMap().entrySet()){
				MultipartFile file = entry.getValue();
				file_name = file.getOriginalFilename();
				file_size = file.size;
				filePath =  path + "/" + UUID.randomUUID().toString() + "." + getEXT(file_name);
				def target = new File(video_folder ,filePath);
				target.getParentFile().mkdirs();
				file.transferTo(target);
				break
			}
			//数据准备
			Map smap = new HashMap();
			smap["teacher_id"] = teacher_id;
			smap["user_id"] = user_id;
			smap["company_id"] = company_id;
			//文件名
			smap["courseware_name"] = fileName;
//			smap["courseware_name"] = file_name;
			smap["courseware_url"] = filePath;
			smap["upload_flag"] = upload_flag;
			//时长
			smap["video_time"] = video_time;
			//格式
			smap["ext"] = getEXT(file_name);
			//大小
			smap["file_size"] = file_size;
//			file.size
			//保存到数据库
			save(smap);
			return [code : 1];
		}catch(Exception e){
			logger.error("courseware method addBatch error! msg : " + e.getMessage());
		}
		finally {
			if(req != null){
				parse.cleanupMultipart(req);
			}
		}
	}

	
	//	公司名/年-月  格式是否可以
	private static final DateFormat FORMAT_DIR = new SimpleDateFormat("/yyyy-MM");
	private String dir_data(){
		return FORMAT_DIR.format(new Date());
	}
	
	// 对扩展名进行小写转换  aa.txt
	private String getEXT(String _file_name) {
		if(StringUtils.isNotBlank(_file_name)){
			return _file_name.substring(_file_name.lastIndexOf(".") + 1, _file_name.length()).toLowerCase();
		}
		return null;
	}
	

}
