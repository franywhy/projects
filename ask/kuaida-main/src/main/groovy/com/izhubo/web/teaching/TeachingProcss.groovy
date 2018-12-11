package com.izhubo.web.teaching

import java.text.SimpleDateFormat

import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.aop.TrueClassFilter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection;
import com.rabbitmq.client.AMQP.Basic.Return;

import static com.izhubo.rest.common.doc.MongoKey.$set
import static com.izhubo.rest.common.doc.MongoKey.$ne
import static com.izhubo.rest.common.util.WebUtils.$$

class TeachingProcss {

	    private MongoTemplate mainMongobyQueues;
		
		
		public TeachingProcss(MongoTemplate mainMongoc) {mainMongobyQueues = mainMongoc;}
			
	
		private static Logger logger = LoggerFactory.getLogger(TeachingProcss.class);
	
		private DBCollection leave(){
			return mainMongobyQueues.getCollection("leave");
		}
		private DBCollection leave_detail(){
			return mainMongobyQueues.getCollection("leave_detail");
		}
		
		private DBCollection subspend_detail(){
			return mainMongobyQueues.getCollection("subspend_detail");
		}
		
	
		private DBCollection school_notice(){
			return mainMongobyQueues.getCollection("school_notice");
		}
		private DBCollection plan_courses(){
			return mainMongobyQueues.getCollection("plan_courses");
		}
		private DBCollection school_test(){
			return mainMongobyQueues.getCollection("school_test");
		}
		/** 学员信息 */
		public DBCollection plan_student() {
			return mainMongobyQueues.getCollection("plan_student");
		}
		/** 报名表 */
		public DBCollection signs() {
			return mainMongobyQueues.getCollection("signs");
		}
	
		/** 报名表课程科目 */
		public DBCollection sign_course() {
			return mainMongobyQueues.getCollection("sign_course");
		}
		/** 校区 */
		public DBCollection area() {
			return mainMongobyQueues.getCollection("area");
		}
		/** 课程科目 */
		public DBCollection courses() {
			return mainMongobyQueues.getCollection("courses");
		}
		/** 课程 节 */
		public DBCollection courses_chapter_section() {
			return mainMongobyQueues.getCollection("courses_chapter_section");
		}
		/** 课程 课时 */
		public DBCollection courses_content() {
			return mainMongobyQueues.getCollection("courses_content");
		}
	
		/** 课程列表 */
		public DBCollection bannerCourse() {
			return mainMongobyQueues.getCollection("banner_course");
		}
		
		/** 复课详情 */
		public DBCollection reschool_detail() {
			return mainMongobyQueues.getCollection("reschool_detail");
		}
		
	
		
		/** 休学单列表*/
		public DBCollection subspend() {
			return mainMongobyQueues.getCollection("subspend");
		}
		
		
		/////
		//审核公共方法
		//
		private void PublicApprove(String cname,String idname,JSONObject data)
		{
			BasicDBObject set = new BasicDBObject();
			set.put("approve_date", data.get("APPROVE_DATE"));
			set.put("status", data.get("VBILLSTATUS"));
			BasicDBObject setheader = new BasicDBObject();
			setheader.put($set, set);
			mainMongobyQueues.getCollection(cname).update($$("_id",GetIdByNcId(data.get(idname).toString())), setheader);
		}
		
		
		def  processDatafromNc(String json,MongoTemplate mongo)
		{
			mainMongobyQueues = mongo;
			JSONObject jo = new JSONObject(json);
			String type = jo.getString("BILLTYPE");
	
			switch(type){// C
				//请假
				case "HJ08" : SaveOrUpdateLeave(jo); break;
				//休学
				case "HJ10" : SaveOrUpdateSubSpend(jo); break;// D
	
				case "HJ11" : SaveOrUpdateReSchool(jo); break;
	
				case "HJ14" : SaveOrUpdateTransSchool(jo); break;
	
			}
			
			
		}
	
	
		private void SaveOrUpdateLeave(JSONObject data)
		{
			if(data.get("BUSINESS_TYPE").equals("1"))
			{
				PublicApprove("leave","LEAVE_ID",data);
			}
			else
			{
	
				if(data.get("RET_CODE").toString().equals("0")&&CheckTS(data.get("LEAVE_ID").toString(),data.get("TS").toString(),"leave"))
				{
					
					//存入数据库，存入基本字段
					BasicDBObject upset = new BasicDBObject();
					upset.put("_id", GetIdByNcId(data.get("LEAVE_ID").toString()));
					upset.put("nc_user_id", data.get("STUDENTRECORDSID"));
					upset.put("nc_id", data.get("LEAVE_ID"));
					upset.put("status", -1);
					
					//upset
					mainMongobyQueues.getCollection("leave").update($$("_id",GetIdByNcId(data.get("LEAVE_ID").toString())), upset, true, false);
					
				
					BasicDBObject upsetbody = new BasicDBObject();
					JSONObject body = data.getJSONArray("body").getJSONObject(0);
					upsetbody.put("_id", GetIdByNcId(body.get("LEAVE_BID").toString()));
					upsetbody.put("nc_parent_id", data.get("LEAVE_ID"));
					//upsetbody.put("timed", body.get("SCHOOL_TIME").toString());
					//upsetbody.put("sktime", body.get("SCHOOL_DATE").toString());
					upsetbody.put("course_id", body.get("SESEH_ID").toString());
					upsetbody.put("planid", body.get("ARRANGEDPLANB_ID").toString());
					//upset
					mainMongobyQueues.getCollection("leave_detail").update($$("_id",GetIdByNcId(body.get("LEAVE_BID").toString())), upsetbody, true, false);
					
					
	
	
				}
				else
				{
					logger.info("leaveNotUpdate"+data.toString());
				}
			}
	
		}
	
	
		private void SaveOrUpdateSubSpend(JSONObject data)
		{
			if(data.get("BUSINESS_TYPE").equals("1"))
			{
				PublicApprove("subspend","CUNSCHOOL_ID",data);
	
			}
			else
			{
	
				if(data.get("RET_CODE").toString().equals("0")&&CheckTS(data.get("CUNSCHOOL_ID").toString(),data.get("TS").toString(),"subspend"))
				{
	
					//存入数据库，存入基本字段
					BasicDBObject upset = new BasicDBObject();
					upset.put("_id", GetIdByNcId(data.get("CUNSCHOOL_ID").toString()));
					upset.put("nc_user_id", data.get("STUDENTRECORDSID"));
					upset.put("nc_id", data.get("CUNSCHOOL_ID"));
					upset.put("status", -1);
					upset.put("reason", data.get("VREASON"));
					upset.put("nc_code", data.get("VBILLCODE"));
					//upset
					mainMongobyQueues.getCollection("subspend").update($$("_id",GetIdByNcId(data.get("CUNSCHOOL_ID").toString())), upset, true, false);
				}
				else
				{
					logger.info("subspendNotUpdate"+data.toString());
				}
			}
	
		}
		private void SaveOrUpdateReSchool(JSONObject data)
		{
			if(data.get("BUSINESS_TYPE").equals("1"))
			{
				PublicApprove("reschool","CRESCHOOL_ID",data);
	
			}
			else
			{
	
				if(data.get("RET_CODE").toString().equals("0")&&CheckTS(data.get("CRESCHOOL_ID").toString(),data.get("TS").toString(),"reschool"))
				{
	
					//存入数据库，存入基本字段
					BasicDBObject upset = new BasicDBObject();
					upset.put("_id", GetIdByNcId(data.get("CRESCHOOL_ID").toString()));
					upset.put("nc_user_id", data.get("STUDENTRECORDSID"));
					upset.put("nc_id", data.get("CRESCHOOL_ID"));
					upset.put("status", -1);
					upset.put("nc_code", data.get("VBILLCODE"));
					upset.put("start_date", data.get("DRESCHOOL_DATE"));
	
	
					//upset
					mainMongobyQueues.getCollection("reschool").update($$("_id",GetIdByNcId(data.get("CRESCHOOL_ID").toString())), upset, true, false);
				}
				else
				{
					logger.info("subspendNotUpdate"+data.toString());
				}
			}
	
		}
	
	
		private void SaveOrUpdateTransSchool(JSONObject data)
		{
			if(data.get("BUSINESS_TYPE").equals("1"))
			{
				PublicApprove("trans_school","TRANSFER_ID",data);
	
			}
			else
			{
	
				if(data.get("RET_CODE").toString().equals("0")&&CheckTS(data.get("TRANSFER_ID").toString(),data.get("TS").toString(),"trans_school"))
				{
	
					//存入数据库，存入基本字段
					BasicDBObject upset = new BasicDBObject();
					upset.put("_id", GetIdByNcId(data.get("TRANSFER_ID").toString()));
					upset.put("nc_user_id", data.get("STUDENTRECORDSID"));
					upset.put("nc_id", data.get("TRANSFER_ID"));
					upset.put("status", -1);
					upset.put("to_school_code", data.get("TSCHOOL"));
					upset.put("nc_code", data.get("VBILLCODE"));
					upset.put("reason", data.get("TRANSFER_REASON"));
					upset.put("trans_date", data.get("BILL_DATE"));
	
					//upset
					mainMongobyQueues.getCollection("trans_school").update($$("_id",GetIdByNcId(data.get("TRANSFER_ID").toString())), upset, true, false);
				}
				else
				{
					logger.info("transNotUpdate"+data.toString());
				}
			}
	
		}
	
		private String GetIdByNcId(String ncid)
		{
			return "N_"+ncid;
		}
		
		private boolean CheckTS(String _ID,String TS,String cname)
		{
			def Obj = mainMongobyQueues.getCollection(cname).findOne($$("_id",GetIdByNcId(_ID)));
			
			if(Obj&&Obj.get("ts"))
			{
				
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date newtdate = formatter.parse(TS);
				
				Date oldtdate = formatter.parse(Obj.get("ts").toString());
				
				if(newtdate>oldtdate)
				{
					return true;
				}

				else
				{
					return true;
				}
				
			}
			else
			{
				return true;
			}
		  
		}
	
		
}
