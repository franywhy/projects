
package com.izhubo.admin.crud

import groovy.transform.TypeChecked;
import groovy.transform.TypeCheckingMode;

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection;
import com.mongodb.DBObject
import com.mongodb.QueryBuilder
import com.tencent.xinge.XingeApp
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.anno.RestWithSession
import com.izhubo.rest.common.doc.IMessageCode
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.pushmsg.IPushService
import com.izhubo.rest.pushmsg.PushEnvironment;
import com.izhubo.rest.pushmsg.model.PushMsgBase
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;
import com.izhubo.rest.web.Crud
import com.izhubo.rest.web.StaticSpring
import com.izhubo.admin.BaseController
import com.izhubo.common.util.KeyUtils
import com.izhubo.model.MsgType
import com.izhubo.model.OpType
import com.izhubo.model.UserType

import org.json.JSONObject
import org.apache.commons.lang.StringUtils
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback

import com.izhubo.rest.common.doc.MongoKey 

import java.awt.GraphicsConfiguration.DefaultBufferCapabilities;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.util.WebUtils.$$
import static com.izhubo.rest.groovy.CrudClosures.*
import static com.izhubo.rest.common.doc.MongoKey.*

import java.util.concurrent.TimeUnit


/**
 * @author: wubinjie@ak.cc
 * Date: 13-11-12 上午11:54
 */

//@Rest
@RestWithSession
@TypeChecked(TypeCheckingMode.SKIP)
class MessageController extends BaseController{

	def messages(){mainMongo.getCollection("message_main")}

	def message_admin(){mainMongo.getCollection("message_admin")}

	def _topics(){mainMongo.getCollection("topics")}
	
	/** 微课表 */
	public DBCollection daily_recommend() {
		return mainMongo.getCollection("daily_recommend");
	}

	DBCollection _users(){	mainMongo.getCollection('users')}
	
	
	DBCollection qQUser(){	qquserMongo.getCollection('qQUser')}
	
	/** 报名表 */
	public DBCollection signs() {
		return mainMongo.getCollection("signs");
	}
	
	//中级职称相关配置项初始化
	private String[] getmiddle_commodity_ids()
	{
		return constants().findOne().get("middle_commodity_id");
	}

	private String[] getmiddle_course_ids()
	{
		return constants().findOne().get("middle_course_ids");
	}

	private String[] getmiddle_white_list()
	{
		return constants().findOne().get("middle_white_list");
	}


	//	支付宝批量付款异步通知接口
	@Value("#{application['push.env']}")
	private int PUSH_ENV;
	
	private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static final String MSG_TIME_JOB = "msgtimejob:";

	def IPushService pushService =GetPushService();


	@Resource
	KGS   msgKGS

	@Resource
	KGS   msgAdminKGS



	//排课计划显示
	@TypeChecked(TypeCheckingMode.SKIP)
	def list(HttpServletRequest req){
		Map user = (Map) req.getSession().getAttribute("user");

		def query = QueryBuilder.start()
		//获取公司id进行数据权限处理


		Crud.list(req,message_admin(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){


			}

		}
	}
	//
	
	
	public del(HttpServletRequest req) {
		
	}

	public add(HttpServletRequest req) {
		Map map = new HashMap();
		
		Map props = [_id:Int,title:Str,content:Str,type:Int,msg_action:Str,define_info:Str,msg_button_text:Str,url:Str,
			t_users:{def users = it as String; if(users != null && !users.isEmpty()){;users.split(',').collect { it as Integer}}},
			user_type:Int,timestamp:Timestamp,status:{0},send:Int,send_time:Str,video_content:Str];
		for (Map.Entry<String, Closure> entry : props.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue().call(req.getParameter(key));
			if (val != null) {
				map.put(key, val);
			}
		}
		
		//		"msg_action" : "mytopices_topic",
		//		"define_info" : "asdfsdf",
		//		"msg_button_text" : "去评价",
		//		"url" : ""
		def userlist = map.get("t_users");
		map.put("define_info", map.get("content"));
		map.put("msg_button_text","去查看");

		if(PushMsgActionEnum.tourl.name() ==map.get("msg_action").toString()||PushMsgActionEnum.live_detail.name() ==map.get("msg_action"))
		{

			map.put("url", map.get("define_info"));
		}

		//如果是普通文字消息，则没有这方面逻辑
		if(PushMsgActionEnum.donothing.name() ==map.get("msg_action").toString())
		{
			map.remove("define_info");
			map.remove("msg_button_text");
		}
		
		//如果是微课的消息增加3个字段
		if(PushMsgActionEnum.video.name() ==map.get("msg_action").toString())
		{
			map.put("content", map.get("video_content"));
			map.put("video_id", map.get("video_content"));
			def video = daily_recommend().findOne($$("recommend_video_id":map.get("video_content")));
			String video_name=video.get("recommend_title")
			String video_record_id=video.get("_id")
			map.put("video_name", video_name);
			map.put("video_record_id", video_record_id);
		}

		//存入管理后台消息实体
		def id = msgAdminKGS.nextId();
		map.put("_id", id);
		
		
		if(req['send'] && req['send'].equals("2")){
			
			map.put("is_send", 0);
			String setKey = MSG_TIME_JOB+id.toString();
			//计算当前时间到设定时间的间隔毫秒数
			long sendlong = yyyyMMdd.parse(map.get("send_time")).time - System.currentTimeMillis();
			//在这里将消息id存入redis键值过期机制
			chatRedis.opsForValue().set(setKey, "", sendlong , TimeUnit.MILLISECONDS);
			mainRedis.opsForValue().set(setKey, "", sendlong , TimeUnit.MILLISECONDS);
			
	    }
		message_admin().save(new BasicDBObject(map));



		if(req['send'] && req['send'].equals("1")){

			sendMsg(id);
		}
		

		return IMessageCode.OK;
	}



	public edit(HttpServletRequest req) {

		if(Crud.edit(req) == IMessageCode.OK){
			if(req['send'] && req['send'].equals("1")){
				def id = req[_id];
				sendMsg(id);
			}
			return IMessageCode.OK;
		}
		return IMessageCode.CODE0;
	}

	def send(HttpServletRequest req){
		def id = req[_id];
		return sendMsg(id)
	}

	def sendMsg(id)
	{
		def  map = message_admin().findOne($$(_id, id as Integer));
		//发送公共消息
		if(map.get("type") == com.izhubo.model.MsgType.公共消息.ordinal())
		{	def msgid = msgKGS.nextId();
			map.put("_id", msgid);
			map.put("user_id", 0);
			if(null.equals(map.get("send_time")) || map.get("send_time")==""){
				map.put("send_time",LongToDate(map.get("timestamp")));
			}
			if(messages().save(new BasicDBObject(map)).getN() == 1){

					sendMsgById(msgid);
				
			}
		}
		else
		{
			def userlist = map.get("t_users");
			//发的是多个人的话，应该生成多个人的消息
			userlist.each {
				Integer user_id = Integer.valueOf(it.toString());
				def msgid = msgKGS.nextId();
				map.put("_id", msgid);
				map.put("user_id", user_id);
				if(null.equals(map.get("send_time")) || map.get("send_time")==""){
					map.put("send_time",LongToDate(map.get("timestamp")));
				}

				if(messages().save(new BasicDBObject(map)).getN() == 1){
						sendMsgById(msgid);
				}

			}
		}
		
		if(map.get("send").equals("2"))
		{
		
			message_admin().update(new BasicDBObject("_id":id), $$( $set : $$("is_send" : 1 )));
			
			
			
		}
		
	}

	@TypeChecked(TypeCheckingMode.SKIP)
	private def sendMsgById(id){
		if(id == null){
			return [code : 0]
		}
		def msg = messages().findOne($$(_id, id as Integer));
		if(msg == null){
			return [code : 0]
		}
		def user_ids = null

		PushMsgBase pushmsg = new PushMsgBase();
		pushmsg.msg_title = msg["title"];


		if(msg.get("msg_action").toString() == PushMsgActionEnum.tourl.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.adver.ordinal();
			pushmsg.msg_url = msg["define_info"];
			pushmsg.push_action_name = PushMsgActionEnum.tourl.name();
			pushmsg.define_info = msg["define_info"];
		}
		else if(msg.get("msg_action").toString() == PushMsgActionEnum.mytopices_topic.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.mytopices_topic.toString();
			pushmsg.define_info = msg["define_info"];
			//pushmsg.msg_content =  "精彩问题推送";
		}
		else if(msg.get("msg_action").toString() == PushMsgActionEnum.donothing.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.nothing.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.defult.toString();
			pushmsg.define_info = msg["content"];
			//pushmsg.msg_content =  msg["content"];
		}
		if(msg.get("msg_action").toString() == PushMsgActionEnum.mytest.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.mytest.name();

		}
		
		if(PushMsgActionEnum.live_detail.name().equals(msg.get("msg_action").toString()))
		{
			pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.live_detail.name();
			pushmsg.define_info =  msg["define_info"];
			pushmsg.msg_url = msg["define_info"];

		}
		if(msg.get("msg_action").toString() == PushMsgActionEnum.video.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.video.name();
			//新增2个微课参数
			pushmsg.video_id = msg["video_id"]
			pushmsg.video_name = msg["video_name"]
			pushmsg.video_record_id = msg["video_record_id"]
		}
		if(msg.get("type") == com.izhubo.model.MsgType.个人消息.ordinal()){//发送方式 用户ID
			def user_id = msg.get("user_id");	
		    sendMsgsbyObject(user_id as Integer, msg["title"], pushmsg, msg["type"])
		}
		else if(msg.get("type") == com.izhubo.model.MsgType.公共消息.ordinal()){//发送方式 用户群组
			Integer user_type = msg["user_type"] as Integer
			pushService.PushServiceInit(PUSH_ENV);
			if(user_type == 2)
			{
				pushService.PushMsgToAllTeachers(pushmsg);
			}
			else if(user_type == 3)
			{
				pushService.PushMsgToAllStudents(pushmsg);
			}
			else if(user_type == 4)
			{
				pushService.PushMsgToAllStudents_android(pushmsg);
			}
			else if(user_type == 5)
			{
				pushService.PushMsgToAllStudents_ios(pushmsg);
			}
			else if(user_type == 6)
			{
				
				def userList = signs().find(
					$$( "nc_commodity_id" : $$('$in':getmiddle_commodity_ids()), "status" : $$('$in' : [
						1 ,
						2 ,
						3
					]) , "dr" : 0) ,
					$$("nc_id" : 1 , "nc_commodity_id" : 1 , "code" : 1, "status" : 1 , "order_no" : 1,"school_name":1,"nc_user_id":1,"phone":1)//NC_id  商品的NCid code  状态
					).sort($$("status" : 1 , "create_time" : 1))?.toArray();
				
				
				def useridlist = new ArrayList();
				
				userList.each {def atem->
					def phone = atem.get("phone");
					def qquserobj = qQUser().findOne($$("username":phone));
					if(qquserobj)
					{
						Integer user_id = (Integer)users().findOne($$("tuid":qquserobj.get("tuid"))).get("_id");
						
						if(!useridlist.contains(user_id))
						{
						  useridlist.add(user_id);
						  if(1 == PUSH_ENV)
						  {
						     sendMsgsbyObject(user_id as Integer, msg["title"], pushmsg, msg["type"]);
						  }
						  else
						  {
							  println user_id+" "+phone;
						  }
						}
					}	
					
				}
				
	
				
			}
		}

		Crud.opLog(OpType.message_send,  [type: msg["type"],title:msg["title"],content:msg["content"],timestamp:System.currentTimeMillis()])
		//cleanHistoryMsg()

		return [code : 1]
	}


	//学员下拉框列表
	//@TypeChecked(TypeCheckingMode.SKIP)

	def user_list(HttpServletRequest req){ //TODO
		Map user = req.getSession().getAttribute("user") as Map;
		String search=req.getParameter("search");
		def query = QueryBuilder.start()
		//query.and("priv").is(UserType.普通用户.ordinal());



		if (StringUtils.isNotBlank(search)) {
			//启用不区分大小写的匹配。

			String pattern = "^1[0-9]{10}\$";
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(search);
			if (m.matches()) {

				def userobj =  qquserMongo.getCollection("qQUser").findOne(new BasicDBObject('username',search));
				def tuid = userobj.get("tuid");
				query.and("tuid").is(tuid);
			}
			else
			{
				Pattern nicknamepattern = Pattern.compile("^.*" + search + ".*\$", Pattern.CASE_INSENSITIVE)
				query.and("nick_name").regex(nicknamepattern)
			}

		}

		Crud.list(req,_users(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}


	/**
	 * 问题列表
	 * @param req
	 * @return
	 */
	def qc_list(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();

		query.and("evaluation_type").in([2 , 3]);

		//问题内容
		def content = req.getParameter("search");
		if(StringUtils.isNotBlank(content)){
			Pattern pattern = Pattern.compile("^.*" + content + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("content").regex(pattern);
		}

		Crud.list(req,_topics(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC){List<BasicDBObject> data->
			//id-->name
			for(BasicDBObject obj: data){

				//学生id
				Integer author_id = obj['author_id'] as Integer;
				//学生名字
				obj["author_name"] = users().findOne($$("_id" : author_id) , $$("nick_name" : 1))?.get("nick_name");
				//老师id
				Integer teach_id = obj['teach_id'] as Integer;

				if(teach_id){
					//老师名字
					obj["teach_name"] = users().findOne($$("_id" : teach_id ), $$("nick_name" : 1))?.get("nick_name");
				}


			}

		}
	}


	/**
	 * 发送多条消息
	 * @param user_ids
	 * @param title
	 * @param content
	 * @param type
	 */
	def sendMsgs(final user_ids,  final title,  final content,  final type){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						Long l = System.currentTimeMillis()
						List<DBObject> msgList = new ArrayList<DBObject>();

						PushMsgBase pushmsg = new PushMsgBase();
						pushmsg.msg_title = title;


						user_ids.each{
							Long tmp = System.currentTimeMillis()
							Integer id = it as Integer

							//调用API发送消息

							def userobj =  mainMongo.getCollection("users").findOne(new BasicDBObject('_id',id))

							if(type == 0||type==2)
							{
								pushmsg.msg_type = 0;
								pushmsg.msg_content =  content;
							}
							else if(type == 1)
							{
								pushmsg.msg_type =1;
								pushmsg.msg_url =  content;
							}

							pushService.PushServiceInit(PUSH_ENV);
							if(userobj.get("priv") == "3")
							{
								pushService.PushMsgToSingleUser(id,pushmsg);
							}
							else if(userobj.get("priv") == "2")
							{
								pushService.PushMsgToSingleTeacher(id,pushmsg);
							}


							def m = $$(_id: id+"_"+tmp,
							type: type, t:id,
							tdel:0,tread:0,
							title:title,
							content:content,
							timestamp:tmp)
							msgList.add(m);
						}



						println "sendMsgs cost time: ${System.currentTimeMillis() - l}"
					}
				}
				);

	}

	def sendMsgsbyObject(final user_id,  final title,  final PushMsgBase pushmsg,  final type){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						Long l = System.currentTimeMillis()
						List<DBObject> msgList = new ArrayList<DBObject>();
						Long tmp = System.currentTimeMillis()
						Integer id = user_id as Integer

						//def userobj =  mainMongo.getCollection("users").findOne(new BasicDBObject('_id',id))
						pushService.PushServiceInit(PUSH_ENV);
						pushService.PushMsgToSingleUser(id,pushmsg);
						pushService.PushMsgToSingleTeacher(id,pushmsg);

				println "sendMsgs cost time: ${System.currentTimeMillis() - l}"
					}
				}
				);

	}

	/**
	 * 清除7天前未读消息
	 */
	private final static long WEEK = 7 *24 * 3600 * 1000L

	@TypeChecked(TypeCheckingMode.SKIP)
	private def cleanHistoryMsg(){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						// 已经被用户删除的
						//msgs().remove($$(tdel:1))
						def time = System.currentTimeMillis() - WEEK
						//2个月之前，且未读的消息
						int size =1
						while(size > 0){
							List lst = msgs().find($$([timestamp : $$($lt , time), tread : 0]), $$(_id, 1))
							.limit(1000).toArray()*._id
							size = lst.size();
							msgs().remove($$(_id , $$($in, lst)))
							Thread.sleep(500)
						}

					}
				}
				);
	}
	
	
	
	/**
	 * 微课列表
	 * @param req
	 * @return
	 */
	def video_list(HttpServletRequest req){
		QueryBuilder query = QueryBuilder.start();

		query.and("recommend_type").in([3]);

		//问题内容
		def content = req.getParameter("search");
		if(StringUtils.isNotBlank(content)){
			Pattern pattern = Pattern.compile("^.*" + content + ".*\$", Pattern.CASE_INSENSITIVE);
			query.and("recommend_title").regex(pattern);
		}
		Crud.list(req,daily_recommend(),query.get(),MongoKey.ALL_FIELD,MongoKey.SJ_DESC)
	}
	
	/**
	 * 
	 * @param dateFormat
	 * @param time
	 * @return
	 */
	public String LongToDate(Long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return sdf.format(date);
		}
}