package com.izhubo.admin.answer



import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert
import static com.izhubo.rest.common.doc.MongoKey._id
import static com.izhubo.rest.common.doc.MongoKey.timestamp
import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.text.SimpleDateFormat

import javax.servlet.http.HttpServletRequest

import com.izhubo.admin.BaseController
import com.izhubo.model.NCUserState
import com.izhubo.model.UserType
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.MongoKey
import com.izhubo.rest.web.StaticSpring
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject



/**
 * 校区报表
 * @author 
 *
 */
@RestWithSession
class SchoolUserReportController extends BaseController {

	public DBCollection users(){
		return mainMongo.getCollection("users");
	}

	public DBCollection logs(){
		return logMongo.getCollection("day_login");
	}

	public DBCollection schoolreportlogs(){
		return logMongo.getCollection("reportdata_user_reg_groupbyschool");
	}

	public DBCollection provreportlogs(){
		return logMongo.getCollection("reportdata_user_reg_groupbyprov");
	}


	public DBCollection areareportlogs(){
		return logMongo.getCollection("reportdata_user_reg_groupbyarea");
	}
	
	public DBCollection day_login(){
		return logMongo.getCollection("day_login");
	}
	


	/**
	 * 问题列表
	 * @param req
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def readdatalist(HttpServletRequest req){




        if(req["type"]!=null)
		{
			if(req["type"].toString() == "1")
			{
				asyncGetReportdata_school();
			}
			
			
			if(req["type"].toString() == "2")
			{
				asyncGetReportdata_province();
			}
			
			if(req["type"].toString() == "3")
			{
				asyncGetReportdata_area();
			}
			if(req["type"].toString() == "4")
			{
				refilldata();
			}
			if(req["type"].toString() == "5")
			{
				
			}
		  
		}


		return getResultOKS();
	}

	/**
	 * 问题列表
	 * @param req
	 * @return
	 */
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){

		List maxtime =schoolreportlogs().find().sort(new BasicDBObject("update_time" , -1)).limit(1).toArray();

		BasicDBObject maxobj = maxtime.get(0);

		String updatetime = maxobj.get("update_time");

		def result = schoolreportlogs().find(new BasicDBObject("update_time" ,updatetime)).toArray();
		
		if(result){
			result.each {BasicDBObject row->
				
				//潜在用户注册会答人数
				//注册会答校区编号
				def qz_user_query = $$("school_code" : row["_id"]["school_code"]);
				//学员用户注册会答
				qz_user_query.put("priv", UserType.普通用户.ordinal());
				//学员用户注册会答
				qz_user_query.put("nc_user_state", NCUserState.潜在.ordinal());
				//潜在用户注册会答id
				List qz_uids = users().find(qz_user_query , $$(_id: 1)).limit(1000).toArray()*._id;
				//潜在用户注册会答人数
				Long qz_kd_count = 0
				if(qz_uids != null && qz_uids.size() > 0){
					qz_kd_count = day_login().count($$("user_id" , $$('$in', qz_uids)));
				}
				
				row["qz_kd_count"] = qz_kd_count;
				
				
				//成交注册会答人数
				//成交注册会答校区编号
				def cj_user_query = $$("school_code" : row["_id"]["school_code"]);
				//成交用户注册会答
				cj_user_query.put("priv", UserType.普通用户.ordinal());
				//成交用户注册会答
				cj_user_query.put("nc_user_state", NCUserState.成交.ordinal());
				//成交用户注册会答id
				List cj_uids = users().find(cj_user_query , $$(_id: 1)).limit(1000).toArray()*._id;
				//成交用户注册会答人数
				Long cj_kd_count = 0;
				if(cj_uids != null && cj_uids.size() > 0){
					cj_kd_count = day_login().count($$("user_id" , $$('$in', cj_uids)));
				}
				row["cj_kd_count"] = cj_kd_count;
				
			}
		}
		
		return getResultOKS(result);
	}
	@TypeChecked(TypeCheckingMode.SKIP)
	private def getshoolreport(){

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat updatedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long currenttimeset = System.currentTimeMillis();


		String create_date =  df.format(currenttimeset);
		String update_time =  updatedf.format(currenttimeset);



		List<com.mongodb.DBObject> activeList = logs().distinct("user_id");

		//def list=[]
		def iter = users().aggregate(
				$$('$match', [nc_user_state:[$gt:0]]),
				new BasicDBObject('$project', [school_code: '$school_code',school_name: '$school_name',area_name:'$area_name',province:'$province',city:'$city']),
				$$('$group', [_id: [school_code: '$school_code',school_name: '$school_name',area_name:'$area_name',province:'$province',city:'$city'],count:[$sum: 1]]),
				new BasicDBObject('$sort', ["_id.area_name":-1,"_id.province":-1,"_id.city":-1])
				).results().iterator();

		while(iter.hasNext()){
			def finaObj = iter.next();

			def school_code = finaObj.get("_id").get("school_code");


			finaObj.put("qz_count",users().count($$("nc_user_state":NCUserState.潜在.ordinal(),"school_code":school_code)));    //潜在学员判断
			finaObj.put("cj_count", users().count($$("nc_user_state":NCUserState.成交.ordinal(),"school_code":school_code)));    //成交学员判断
			finaObj.put("zt_count",users().count( $$("nc_user_state":NCUserState.暂停.ordinal(),"school_code":school_code)));    //暂停学员判断

			List<com.mongodb.DBObject> ncuserlist = users().find($$(["school_code":school_code,nc_user_state:[$gt:0]]), $$("user_id":1)).toArray();


			finaObj.put("hy_count", Intersection(ncuserlist,activeList));    //活跃学员判断



			finaObj.put("create_date", create_date);
			finaObj.put("update_time", update_time);

			schoolreportlogs().save(finaObj);

			//list.add(finaObj);

		}
	}


	@TypeChecked(TypeCheckingMode.SKIP)
	private def getprovincereport(){

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat updatedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long currenttimeset = System.currentTimeMillis();


		String create_date =  df.format(currenttimeset);
		String update_time =  updatedf.format(currenttimeset);



		List<com.mongodb.DBObject> activeList = logs().distinct("user_id");



		def list=[]
		//先分组
		def iter = users().aggregate(
				$$('$match', [nc_user_state:[$gt:0]]),
				new BasicDBObject('$project', [area_name:'$area_name',province:'$province']),
				$$('$group', [_id: [area_name:'$area_name',province:'$province'],count:[$sum: 1]]),
				new BasicDBObject('$sort', ["_id.area_name":-1,"_id.province":-1,"_id.city":-1])
				).results().iterator();

		while(iter.hasNext()){
			def finaObj = iter.next();

			def province = finaObj.get("_id").get("province");


			finaObj.put("qz_count",users().count($$("nc_user_state":NCUserState.潜在.ordinal(),"province":province)));    //潜在学员判断
			finaObj.put("cj_count", users().count($$("nc_user_state":NCUserState.成交.ordinal(),"province":province)));    //成交学员判断
			finaObj.put("zt_count",users().count( $$("nc_user_state":NCUserState.暂停.ordinal(),"province":province)));    //暂停学员判断

			List<com.mongodb.DBObject> ncuserlist = users().find($$(["province":province,nc_user_state:[$gt:0]]), $$("user_id":1)).toArray();


			finaObj.put("hy_count", Intersection(ncuserlist,activeList));    //活跃学员判断

			list.add(finaObj);


			finaObj.put("create_date", create_date);
			finaObj.put("update_time", update_time);

			provreportlogs().save(finaObj);

		}





		//list.add(finaObj);


	}


	@TypeChecked(TypeCheckingMode.SKIP)
	private def getareareport(){


		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat updatedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long currenttimeset = System.currentTimeMillis();

		
		String create_date =  df.format(currenttimeset);
		String update_time =  updatedf.format(currenttimeset);
		List<com.mongodb.DBObject> activeList = logs().distinct("user_id");

		def iter = users().aggregate(
				$$('$match', [nc_user_state:[$gt:0]]),
				new BasicDBObject('$project', [area_name:'$area_name']),
				$$('$group', [_id: [area_name:'$area_name'],count:[$sum: 1]]),
				new BasicDBObject('$sort', ["_id.area_name":-1,"_id.province":-1,"_id.city":-1])
				).results().iterator();

		while(iter.hasNext()){
			def finaObj = iter.next();

			def area_name = finaObj.get("_id").get("area_name");


			finaObj.put("qz_count",users().count($$("nc_user_state":NCUserState.潜在.ordinal(),"area_name":area_name)));    //潜在学员判断
			finaObj.put("cj_count", users().count($$("nc_user_state":NCUserState.成交.ordinal(),"area_name":area_name)));    //成交学员判断
			finaObj.put("zt_count",users().count( $$("nc_user_state":NCUserState.暂停.ordinal(),"area_name":area_name)));    //暂停学员判断

			List<com.mongodb.DBObject> ncuserlist = users().find($$(["area_name":area_name,nc_user_state:[$gt:0]]), $$("user_id":1)).toArray();


			finaObj.put("hy_count", Intersection(ncuserlist,activeList));    //活跃学员判断

			finaObj.put("create_date", create_date);
			finaObj.put("update_time", update_time);

			areareportlogs().save(finaObj);
			
		
			

		}
	}



	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncGetReportdata_school(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {

						getshoolreport();

					}
				}
				);
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncGetReportdata_province(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {


						getprovincereport();

					}
				}
				);
	}
	
	@TypeChecked(TypeCheckingMode.SKIP)
	private def asyncGetReportdata_area(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {

						getareareport();
					}
				}
				);
	}
	
	
	@TypeChecked(TypeCheckingMode.SKIP)
	private def refilldata(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						//UserAccessTimeKey
						
						List<com.mongodb.DBObject> userlist =    users().distinct("_id");
						List<com.mongodb.DBObject> activeList = logs().distinct("user_id");
						
						
						for(int i=0;i<userlist.size();i++) {
							//DBObject ncuser = new BasicDBObject();
				
							def userid = userlist.get(i);
							if(mainRedis.opsForHash().hasKey("UserAccessTimeKey", userlist.get(i).toString())){
							
								if(!activeList.contains(userid))
								{
								   day_login(userid);
								}
							}
						}
						 
						
						 
					
					}
				}
				);
	}
	
	
	public  void day_login(Integer uid){
		Date time = new Date();
		Long tmp = time.getTime();
		
	     String id = "20150501_" + uid;
		Map<String,Object> setOnInsert = new HashMap<>();
		setOnInsert.put("user_id",uid);
		setOnInsert.put(timestamp,tmp);
	
		String ip = "127.0.0.1";

		setOnInsert.put("ip",ip);
	   //添加qd
	
		String qd = "fill" ;

		setOnInsert.put("qd",qd);
	 
		
		logMongo.getCollection("day_login").findAndModify(new BasicDBObject(_id,id),null,
				null,false,
				new BasicDBObject($setOnInsert,setOnInsert),true,true); //upsert
		

	   
	}
	
	
	@TypeChecked(TypeCheckingMode.SKIP)
	public  List[] GetSchoolReportResult(){

		List<com.mongodb.DBObject> activeList = logs().distinct("user_id");

		def list=[]
		def iter = users().aggregate(
				$$('$match', [nc_user_state:[$gt:-1]]),
				new BasicDBObject('$project', [school_code: '$school_code',school_name: '$school_name',area_name:'$area_name',province:'$province',city:'$city']),
				$$('$group', [_id: [school_code: '$school_code',school_name: '$school_name',area_name:'$area_name',province:'$province',city:'$city'],count:[$sum: 1]]),
				new BasicDBObject('$sort', ["_id.area_name":-1,"_id.province":-1,"_id.city":-1])
				).results().iterator();

		while(iter.hasNext()){
			def finaObj = iter.next();

			def school_code = finaObj.get("_id").get("school_code");


			finaObj.put("qz_count",users().count($$("nc_user_state":NCUserState.潜在.ordinal(),"school_code":school_code)));    //潜在学员判断
			finaObj.put("cj_count", users().count($$("nc_user_state":NCUserState.成交.ordinal(),"school_code":school_code)));    //成交学员判断
			finaObj.put("zt_count",users().count( $$("nc_user_state":NCUserState.暂停.ordinal(),"school_code":school_code)));    //暂停学员判断

			List<com.mongodb.DBObject> ncuserlist = users().find($$("school_code":school_code), $$("user_id":1)).toArray();


			finaObj.put("hy_count", Intersection(ncuserlist,activeList));    //活跃学员判断

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

			SimpleDateFormat updatedf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			long currenttimeset = System.currentTimeMillis();


			String create_date =  df.format(currenttimeset);
			String update_time =  updatedf.format(currenttimeset);


			finaObj.put("create_date", create_date);
			finaObj.put("update_time", update_time);

			schoolreportlogs.save(finaObj);

			list.add(finaObj);

		}

		return list;
	}



	Integer Intersection(List<com.mongodb.DBObject> ncuserlist,List<com.mongodb.DBObject> activelist){

		def usercount = 0;


		int pointerA = 0;
		int pointerB = 0;

		for(int i=0;i<ncuserlist.size();i++) {
			//DBObject ncuser = new BasicDBObject();

			def userid = ncuserlist.get(i).get("_id");
			if(activelist.contains(userid)) {
				usercount++;
			}
		}

		return usercount;
	}
}

