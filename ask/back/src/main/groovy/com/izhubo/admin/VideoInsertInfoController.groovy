package com.izhubo.admin
import com.izhubo.rest.common.doc.MongoKey
import javax.servlet.http.HttpServletRequest;
import static com.izhubo.rest.common.doc.MongoKey.ALL_FIELD
import static com.izhubo.rest.common.util.WebUtils.$$
import org.apache.commons.lang.StringUtils
import com.izhubo.rest.anno.RestWithSession;
import  com.izhubo.rest.common.util.JSONUtil;
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection;
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.izhubo.rest.web.Crud
@RestWithSession
class VideoInsertInfoController extends BaseController {
	 
	DBCollection courseDB() {
		mainMongo.getCollection('course');
    }
	DBCollection video_inert_infoDB(){
		mainMongo.getCollection('video_inert_info');
	}
   
   //
   def list(HttpServletRequest req) {
	   QueryBuilder query = QueryBuilder.start();
	   Crud.list(req, video_inert_infoDB(), query.get(), ALL_FIELD, MongoKey.SJ_DESC){
		   List<BasicDBObject> data->
		   for(BasicDBObject obj: data){
			   def  only = courseDB().findOne(new BasicDBObject("_id" : obj["course_id"]));		
			   def	course_name = only.get("course_name");
			   def  chapter_name = "";
			   def  courseware_name = "";
			   List<DBObject> chapters = (List<DBObject>)only.get("chapter");
			   for(DBObject chapter:chapters){
				   if(chapter.get("_id").equals(obj["chapter_id"])){
					 chapter_name = chapter.get("chapter_name");
				   }
				   List<DBObject> coursewares  = (List<DBObject>)chapter.get("chapter_courseware");
				   for(DBObject courseware:coursewares){
					   if(courseware.get("courseware_id").equals(obj["courseware_id"])){
						  courseware_name = courseware.get("courseware_name");
					   }
				   }												
			   }
			   if(course_name){
				   obj.put("course_name", course_name);
			   }
			   if(chapter_name){
				   obj.put("chapter_name",chapter_name);
			   }
			   if(courseware_name){
				   obj.put("courseware_name",courseware_name);
			   }
		   }
	   };
   }
   
   //联动查询课程/章节/课件
   def linked_query_course(HttpServletRequest req){
	   List<DBObject> list =  courseDB().find().toArray();
		return ["code":1,"data":list];
   }
   
   
   def add(HttpServletRequest request){
	   def video_inert_info =$$("_id" : UUID.randomUUID().toString());	   
	   def course_id = request.getParameter("course_id");
	   def chapter_id = request.getParameter("chapter_id");
	   def courseware_id = request.getParameter("courseware_id");
	   def insert_time = request.getParameter("insert_time");	   
	   def is_use = request.getParameter("is_use");
	   def json = request.getParameter("json");	   	 
	   def  rationMap = JSONUtil.jsonToMap(json);
	   List vid = (ArrayList)rationMap.get("video_insert_detail");	   
	   video_inert_info.put("course_id", course_id);
	   video_inert_info.put("chapter_id", chapter_id);
	   video_inert_info.put("courseware_id", courseware_id);
	   video_inert_info.put("insert_time", insert_time);
	   video_inert_info.put("is_use", is_use.equals("true")?true:false);
	   video_inert_info.put("video_insert_detail", vid);	   
	   video_inert_info.append("manage_info",getCurrentUserInfo(request));
	   video_inert_infoDB().save(video_inert_info);
	   return OK();
   }
   
   
   def edit(HttpServletRequest request){
	   def _id =request.getParameter("_id");
	   def video_inert_info =$$("_id" : _id);
	   def course_id = request.getParameter("course_id");
	   def chapter_id = request.getParameter("chapter_id");
	   def courseware_id = request.getParameter("courseware_id");
	   def insert_time = request.getParameter("insert_time");
	   def is_use = request.getParameter("is_use");
	   def json = request.getParameter("json");
	   def  rationMap = JSONUtil.jsonToMap(json);
	   List vid = (ArrayList)rationMap.get("video_insert_detail");
	   video_inert_info.put("course_id", course_id);
	   video_inert_info.put("chapter_id", chapter_id);
	   video_inert_info.put("courseware_id", courseware_id);
	   video_inert_info.put("insert_time", insert_time);
	   video_inert_info.put("is_use", is_use.equals("true")?true:false);
	   video_inert_info.put("video_insert_detail", vid);
	   video_inert_info.append("manage_info",getCurrentUserInfo(request));
	   video_inert_infoDB().update(new BasicDBObject("_id":_id),new BasicDBObject('$set':video_inert_info))
	   return OK();
   }
   
   def del(HttpServletRequest req){
	   String id = req[_id]
	   if(StringUtils.isEmpty(id))
		   return [code:0]
	   video_inert_infoDB().remove(new BasicDBObject(_id,id))
	   return OK();
   }
   
   def getCurrentUserInfo(HttpServletRequest req){
			Map user = (Map) req.getSession().getAttribute("user");
			Map manage_info = new HashMap();
			Long now = System.currentTimeMillis();
			//创建人id
			manage_info.put("create_user_id",user.get("_id") as Integer);
			//创建日期
			manage_info.put("timestamp",now);
			//修改人Id
			manage_info.put("update_user_id",user.get("_id") as Integer);
			//修改日期
			manage_info.put("update_date",now);
			return manage_info;
	}
   	//操作表名称
    def property = "video_inert_info"; 
   
	def submit(HttpServletRequest request){
	   submit1(request);
	}
	
	def rollbackSubmit(HttpServletRequest request){
	   rollbackSubmit1(request)
	}
	
	def audit(HttpServletRequest request){
	   audit1(request);
	}
	
	def rollbackAudit(HttpServletRequest request){
	   rollbackAudit1(request);
	}
   
}
