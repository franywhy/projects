package com.izhubo.web.msg

import static com.izhubo.rest.common.doc.MongoKey.$set;
import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.util.WebUtils.$$;

import java.sql.Timestamp
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode
import org.apache.commons.lang3.StringUtils









import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.izhubo.utils.SuperHashMap;
import com.izhubo.web.BaseController
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.izhubo.model.MsgType;
import com.izhubo.rest.AppProperties;
import com.izhubo.rest.anno.RestWithSession;
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.StaticSpring;
import com.izhubo.rest.web.spring.InterceptorAnnotationAwareClassNameHandlerMapping;
import com.izhubo.web.api.Web;
import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.annotations.ApiParam
import com.wordnik.swagger.annotations.ApiImplicitParam
import com.wordnik.swagger.annotations.ApiImplicitParams
import com.wordnik.swagger.annotations.ApiOperation
import com.izhubo.web.vo.MyMessageListVO;
import com.izhubo.web.vo.BaseResultVO;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;







import javax.servlet.http.HttpServletRequest

import static com.izhubo.rest.common.doc.MongoKey.*

import org.springframework.web.bind.ServletRequestUtils
import com.izhubo.utils.DataUtils;

/**
 * 公开发送站内信的接口，便于后台脚本调用
 * @author wubinjie
 *
 */
@RestWithSession
@RequestMapping("/messagecenter")
@TypeChecked(TypeCheckingMode.SKIP)
class MessageCenterController extends BaseController {


	@Resource
	protected KGS msgKGS;

	public DBCollection message_main() {
		return mainMongo.getCollection("message_main");
	}
	
	public DBCollection message_user() {
		return mainMongo.getCollection("message_user");
	}
	
	public BasicDBObject msgQuery(){
		return new BasicDBObject();
	}
	
	/**
	 * 个人消息列表
	 * @date 2016年3月28日
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_msglist_pc/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "PC端个人消息列表", httpMethod = "GET", response = MyMessageListVO.class, notes = "个人消息列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "第page页", required = false, dataType = "int", paramType = "query" , defaultValue="1"),
		@ApiImplicitParam(name = "size", value = "每页size数量", required = false, dataType = "int", paramType = "query" , defaultValue="15"),
		@ApiImplicitParam(name = "type", value = "0 公共消息 1个人消息", required = false, dataType = "int", paramType = "query" , defaultValue="1")
	])
	def get_msglist_pc(HttpServletRequest request){
		
		Integer user_id = Web.getCurrentUserId();
		int size = ServletRequestUtils.getIntParameter(request, "size", 15);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		int type = ServletRequestUtils.getIntParameter(request, "type", 0);
		
		def	  query;
		if(type == MsgType.公共消息.ordinal())
		{
			//查询条件
					  query = $$(
				   $and : [
						  $$( "user_type" ,   3)
						  ]
				   );
		}
		else
		{
				  query = $$(
				$and : [
					   $$( "user_id" ,   user_id),
					   $$( "type" ,   type)
					   ]
				);
		}
		
		
		

			//故意限制1000条，避免后期数量过多问题
			int allmsg = message_main().count(query);
			int readmsg = message_user().count($$("user_id",user_id));
			int result = allmsg - readmsg;
			
			
			int count = message_main().count(query);
			int allpage = count / size + ((count% size) >0 ? 1 : 0);
			//查询结果
			def mslist = null;
			if(count > 0){
				//需要查询的字段
				mslist = message_main().find(query ).sort($$( "timestamp" ,   -1)).skip((page - 1) * size).limit(size).toArray();
				
				if(mslist){
					
					mslist.each {BasicDBObject row ->
						String _id = row["_id"];
						//商品所属校区和价格
					
						String msgid = getOpenMsgId(_id,user_id);
						long isopen =   message_user().count($$("_id", msgid));
						if(isopen>0)
						{
							row["is_open"] = 1;
						}
						else
						{
							row["is_open"] = 0;
						}
										
					}
						
				 }
			}
		
			
			return getResultOK(mslist, allpage, count , page , size);
		
	

		
	}
	
	
	/**
	 * 个人消息列表
	 * @date 2016年3月28日
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_msglist/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "个人消息列表", httpMethod = "GET", response = MyMessageListVO.class, notes = "个人消息列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "第page页", required = false, dataType = "int", paramType = "query" , defaultValue="1"),
		@ApiImplicitParam(name = "size", value = "每页size数量", required = false, dataType = "int", paramType = "query" , defaultValue="15")
	])
	def get_msglist(HttpServletRequest request){
		
		Integer user_id = Web.getCurrentUserId();
		int size = ServletRequestUtils.getIntParameter(request, "size", 15);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		
			//查询条件
				def	  query = $$(
				   $or : [
						  $$( "user_id" ,   user_id),
						  $$( "user_type" ,   3).append("type",0)
						  ]
				   );
			
			def count = message_main().count(query);
			int allpage = count / size + ((count% size) >0 ? 1 : 0);
			//查询结果
			def mslist = null;
			if(count > 0){
				//需要查询的字段
				mslist = message_main().find(query ).sort($$("send_time":-1,"timestamp" :-1)).skip((page - 1) * size).limit(size).toArray();
				
				if(mslist){
					
					mslist.each {BasicDBObject row ->
						String _id = row["_id"];
						//商品所属校区和价格
						String msgid = getOpenMsgId(_id,user_id);
						long isopen =   message_user().count($$("_id", msgid));
						if(isopen>0)
						{
							row["is_open"] = 1;
						}
						else
						{
							row["is_open"] = 0;
						}
						//增加时间的处理
						if(StringUtils.isNotBlank(row["send_time"])){
							String formatType = "yyyy-MM-dd HH:mm:ss"
							long Ltime = stringToLong(row["send_time"],formatType)
							row["timestamp"] = Ltime
						}

					}
						
				 }
			}
			
			return getResultOK(mslist, allpage, count , page , size);
		
	}
	
	// date类型转换为long类型
	public static long dateToLong(Date date) {
		return date.getTime();
	}
	// string类型转换为date类型
	public static java.util.Date stringToDate(String strTime, String formatType)
	throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}
	// string类型转换为long类型
	public static long stringToLong(String strTime, String formatType)
	throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}
	
	
	/**
	 * 个人消息列表
	 * @date 2016年3月28日
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "get_msglist_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "教师或内部员工消息列表", httpMethod = "GET", response = MyMessageListVO.class, notes = "个人消息列表")
	@ApiImplicitParams([
		@ApiImplicitParam(name = "page", value = "第page页", required = false, dataType = "int", paramType = "query" , defaultValue="1"),
		@ApiImplicitParam(name = "size", value = "每页size数量", required = false, dataType = "int", paramType = "query" , defaultValue="15")
	])
	def get_msglist_teacher(HttpServletRequest request){
		
		Integer user_id = Web.getCurrentUserId();
		int size = ServletRequestUtils.getIntParameter(request, "size", 15);
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);
		
		 
		
			//查询条件
				def	  query = $$(
				   $or : [
						  $$( "user_id" ,   user_id),
						  $$( "user_type" ,   Web.currentUserPriv()).append("type",0)
						  ]
				   );
			
			def count = message_main().count(query);
			int allpage = count / size + ((count% size) >0 ? 1 : 0);
			//查询结果
			def mslist = null;
			if(count > 0){
				//需要查询的字段
				mslist = message_main().find(query ).sort($$( "timestamp" ,   -1)).skip((page - 1) * size).limit(size).toArray();
				
				if(mslist){
					
					mslist.each {BasicDBObject row ->
						String _id = row["_id"];
						//特殊处理，如果是商机跟进记录，则进入商机跟进记录H5
						if(row["msg_button_text"].toString().equals("去跟进"))
						{
							//http://h5.hqjy.com/t-business.html?token=q2-51b2f73b4b6efd6b5b060e761bdc5e7e
							String hqjyh5domain = AppProperties.get("hqjyh5.domain").toString(); 
							String TOKEN   = request.getParameter("access_token");
							row.put("define_info", hqjyh5domain+"/t-business.html?token="+TOKEN)
						}
						//商品所属校区和价格
						String msgid = getOpenMsgId(_id,user_id);
						long isopen =   message_user().count($$("_id", msgid));
						if(isopen>0)
						{
							row["is_open"] = 1;
						}
						else
						{
							row["is_open"] = 0;
						}
										
					}
						
				 }
			}
			
			
			return getResultOK(mslist, allpage, count , page , size);
		
	

		
	}
	
	
	
	def getOpenMsgId(Object msgid,Integer user_id)
	{
		return msgid.toString()+"_"+user_id.toString();
	}

	
	/**
	 * 标记消息为已读
	 * @date 2016年3月9日 下午5:42:49
	 * @param @param request
	 */
	@ResponseBody
	@RequestMapping(value = "read_msglist/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ApiOperation(value = "一次性将未读消息变成已读", httpMethod = "POST",  notes = "一次性将未读消息变成已读", response = BaseResultVO.class)
	def read_msglist(
		HttpServletRequest request
		){
		//TODO 待测试 消息返回结构VO
	
		Integer user_id = Web.getCurrentUserId();
		
		

		
		def	  query = $$(
				   $or : [
						  $$( "user_id" ,   user_id),
						  $$( "user_type" ,   3).append("type",0)
						  ]
				   );
	
		
		//故意限制1000条，避免后期数量过多问题
		def mslist = message_main().find(query).limit(1000).toArray();
		
		if(mslist){
			
			mslist.each {BasicDBObject row ->
				
				BasicDBObject insert = new BasicDBObject();
				
				insert.put("_id", getOpenMsgId(row["_id"],user_id));
				insert.put("msg_id", row["_id"]);
				insert.put("opentime", System.currentTimeMillis());
				insert.put("user_id", user_id);
				message_user().save(insert);
				
			}
				
		 }
		
		
	
			
		return getResultOK();
		
		
		
		
		
		
	}
		
		
		/**
		 * 标记消息为已读
		 * @date 2016年3月9日 下午5:42:49
		 * @param @param request
		 */
		@ResponseBody
		@RequestMapping(value = "read_msglist_teacher/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "一次性将未读消息变成已读", httpMethod = "POST",  notes = "一次性将未读消息变成已读", response = BaseResultVO.class)
		def read_msglist_teacher(
			HttpServletRequest request
			){
			//TODO 待测试 消息返回结构VO
		
			Integer user_id = Web.getCurrentUserId();
			
			
	
			
			def	  query = $$(
					   $or : [
							  $$( "user_id" ,   user_id),
							  $$( "user_type" ,   Web.currentUserPriv()).append("type",0)
							  ]
					   );
		
			
			//故意限制1000条，避免后期数量过多问题
			def mslist = message_main().find(query).limit(1000).toArray();
			
			if(mslist){
				
				mslist.each {BasicDBObject row ->
					
					BasicDBObject insert = new BasicDBObject();
					
					insert.put("_id", getOpenMsgId(row["_id"],user_id));
					insert.put("msg_id", row["_id"]);
					insert.put("opentime", System.currentTimeMillis());
					insert.put("user_id", user_id);
					message_user().save(insert);
					
				}
					
			 }
			
			
		
				
			return getResultOK();
			
			
			
			
			
			
		}
		
		
		@ResponseBody
		@RequestMapping(value = "read_msglist_byid/*-*", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "根据消息id标记单条消息为已读", httpMethod = "POST",  notes = "根据消息id标记单条消息为已读", response = BaseResultVO.class)
		@ApiImplicitParams([
			@ApiImplicitParam(name = "msg_id", value = "消息id", required = true, dataType = "String", paramType = "query" , defaultValue="1")
		])
		def read_msglist_byid(
			HttpServletRequest request
			){
			//TODO 待测试 消息返回结构VO
		
			Integer user_id = Web.getCurrentUserId();
			
			
	         String msg_id = ServletRequestUtils.getStringParameter(request, "msg_id", "");
			
			def	  query = $$( "msg_id" ,   msg_id);
					
		
			
			//故意限制1000条，避免后期数量过多问题
			def msgobj = message_main().findOne(query);
			
			if(msgobj){
				
			
					
					BasicDBObject insert = new BasicDBObject();
					
					insert.put("_id", getOpenMsgId(msgobj["_id"],user_id));
					insert.put("msg_id", msgobj["_id"]);
					insert.put("opentime", System.currentTimeMillis());
					insert.put("user_id", user_id);
					message_user().save(insert);
					
				
					
			 }
			
			
		
				
			return getResultOK();
			
			
			
			
			
			
		}
		
		/**
		 * 获取未读消息数目
		 * @date 2016年3月9日 下午5:42:49
		 * @param @param request
		 */
		@ResponseBody
		@RequestMapping(value = "get_unread_count/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
		@ApiOperation(value = "获取未读消息数目", httpMethod = "GET",  notes = "获取未读消息数目", response = BaseResultVO.class)
		def get_unread_count(
			HttpServletRequest request
			){
			//TODO 待测试 消息返回结构VO	
			Integer user_id = Web.getCurrentUserId();	
		
		def	  query = $$(
				   $or : [
						  $$( "user_id" ,   user_id),
						  $$( "user_type" ,   3).append("type",0)
						  ]
				   );
			//故意限制1000条，避免后期数量过多问题
			int allmsg = message_main().count(query);	
			int readmsg = message_user().count($$("user_id",user_id));	
			int result = allmsg - readmsg;
			
			if(result<0)
			{
				result = 0;
			}
			return getResultOK(result);
	
			
		}
			
			
			/**
			 * 获取未读消息数目
			 * @date 2016年3月9日 下午5:42:49
			 * @param @param request
			 */
			@ResponseBody
			@RequestMapping(value = "get_unread_count_teacher/*-*", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
			@ApiOperation(value = "获取未读消息数目", httpMethod = "GET",  notes = "获取未读消息数目", response = BaseResultVO.class)
			def get_unread_count_teacher(
				HttpServletRequest request
				){
				//TODO 待测试 消息返回结构VO
				Integer user_id = Web.getCurrentUserId();
			
			def	  query = $$(
					   $or : [
							  $$( "user_id" ,   user_id),
							  $$( "user_type" ,   Web.currentUserPriv()).append("type",0)
							  ]
					   );
				//故意限制1000条，避免后期数量过多问题
				int allmsg = message_main().count(query);
				int readmsg = message_user().count($$("user_id",user_id));
				int result = allmsg - readmsg;
				
				if(result<0)
				{
					result = 0;
				}
				
		
				return getResultOK(result);
		
				
			}
	
	
	
	/**
	 * 发送站内信息给用户
	 * @param title
	 * @param content
	 * @param type
	 * 消息类型， 系统消息1，活动消息2
	 * @param s_type
	 * 0单个用户，1用户组
	 * @param users
	 * 用户id
	 * @param user_type
	 * 用户类型，当用户组时有效
	 * @param timestamp
	 */
	public void addMessage(String title, String content, Integer type,
			Integer s_type, Collection<Integer> users, Integer user_type, Timestamp timestamp) {

		Map<Object, Object> map = new HashMap<>();
		int id = msgKGS.nextId();
		map.put(_id, id);
		map.put("title", title);
		map.put("content", content);
		map.put("type", type);
		map.put("s_type", s_type);
		map.put("t_users", users);
		map.put("user_type", user_type);
		map.put("timestamp", timestamp);
		map.put("status", 0);
		map.put("", 0);

		if (adminMongo.getCollection("messages").save($$(map))
		.getN() == 1) {
			sendMsgById(id);
		}
	}

	/**
	 * 不支持多用户发送
	 *
	 * @param id
	 */
	private void sendMsgById(Integer id) {
		if (id == null) {
			return;
		}
		DBObject msg = adminMongo.getCollection("messages").findAndModify(
				$$(_id, id).append("status", 0), $$($set, $$("status", 1)));
		if (msg == null) {
			return;
		}
		Collection<Integer> user_ids = null;
		if ((Integer) msg.get("s_type") == 0) {
			user_ids = (Collection<Integer>) msg.get("t_users");
			if (user_ids != null)
				sendMsgs(user_ids, (String) msg.get("title"),
						(String) msg.get("content"), (Integer) msg.get("type"));
		}
	}

	private void sendMsgs(final Collection<Integer> user_ids,
			final String title, final String content, final Integer type) {
		StaticSpring.execute(new Runnable() {
					public void run() {
						Long l = System.currentTimeMillis();
						List<DBObject> msgList = new ArrayList<DBObject>();
						for (Integer user_id : user_ids) {
							Long tmp = System.currentTimeMillis();
							Integer id = user_id;
							Map m = SuperHashMap.toSuperMap(_id, (Object)(id+"_"+tmp))
									.putVal("type", type)
									.putVal("t", id)
									.putVal("tdel", 0)
									.putVal("tread", 0)
									.putVal("title", title)
									.putVal("content", content)
									.putVal("timestamp", tmp);

							msgList.add($$(m));
						}
						mainMongo.getCollection("msgs").insert(msgList);
					}
				});
	}

	public void setMsgKGS(KGS msgKGS) {
		this.msgKGS = msgKGS;
	}
}
