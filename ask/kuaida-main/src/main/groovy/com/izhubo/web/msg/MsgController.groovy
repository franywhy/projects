package com.izhubo.web.msg

import static com.izhubo.rest.common.util.WebUtils.$$

import java.util.concurrent.TimeUnit;

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import com.alipay.api.DefaultAlipayClient;
import com.izhubo.model.UserType;
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.JSONUtil
import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.model.PushMsgBase;
import com.izhubo.web.BaseController
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.izhubo.rest.web.StaticSpring
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.pushmsg.PushEnvironment;
import com.izhubo.rest.pushmsg.model.PushMsgBase
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;

import org.omg.CORBA._IDLTypeStub;
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.MongoTemplate;
/**
 * 
 * @ClassName: MainController 
 * @Description: api接口
 * @author shihongjie
 * @date 2015年5月21日 下午2:26:25 
 *
 */
@Rest
@TypeChecked(TypeCheckingMode.SKIP)
class MsgController extends BaseController{

	private long MSG_TIMEOUT = 300;

	private String MSG_TAG = "msg:";

	private String OFFLINEMSG_TAG = "offlinemsg:";
	private String OFFLINEMSGDETAIL_TAG = "offlinemsg_content:";

	public DBCollection topics() {
		return mainMongo.getCollection("topics");
	}
	public DBCollection users() {
		return mainMongo.getCollection("users");
	}
	public DBCollection msgs() {
		return mainMongo.getCollection("msg");
	}
	
	def messages(){mainMongo.getCollection("message_main")}
	
	
	@Value("#{application['push.env']}")
	private int PUSH_ENV;
	
	@Resource
	KGS   msgKGS


	public void SendMsg(List<String> to_user, Object o) {
		Map<String,Object> map = JSONUtil.beanToMap(o);
		String tuid = PostMsg(JSONUtil.beanToJson(o));
		map.put("msg_id", tuid);

		JSONObject jsonObject = JSONObject.fromObject(map);

		for(int i=0;i<to_user.size();i++) {
			String channel = to_user.get(i);
			publish(channel,jsonObject.toString());
		}
	}




	//s	获取老师信息
	def PostMsg(HttpServletRequest request){

		//消息内容$set
		//消息对象
		//消息id


		String json = request["json"];

		//println "json: ${json}"

		String uuid = PostMsg(json);

		//返回消息id

		return getResultOK(uuid);
	}

	public String PostMsg(String json)
	{
		Map jmap = JSONUtil.jsonToMap(json);
		UUID uuid = UUID.randomUUID();
		jmap.put("msg_id", uuid.toString());
		jmap.put("is_read",false);
		jmap.put("timestamp",System.currentTimeMillis());

		String msgJson = JSONObject.fromObject(jmap).toString();
		mainRedis.opsForValue().set(MSG_TAG+uuid.toString(), msgJson, MSG_TIMEOUT,TimeUnit.SECONDS);

		//	msgs().save(new BasicDBObject(jmap));

		return uuid;
	}




	//s	获取老师信息
	def GetMsg(HttpServletRequest request){

		//消息内容
		//消息对象
		//消息id
		String token = request["token"];

		def msgobj = mainRedis.opsForValue().get(MSG_TAG+token);

		if(msgobj!=null)
		{
			try{
				
				JSONObject msgjson = JSONObject.fromObject(msgobj);
				String user_id = msgjson.getJSONArray("to_user").getString(0);
				mainRedis.delete(OFFLINEMSGDETAIL_TAG+token+"_"+user_id);
				chatRedis.delete(OFFLINEMSGDETAIL_TAG+token+"_"+user_id);
				mainRedis.delete(OFFLINEMSG_TAG+token+"_"+user_id);
				chatRedis.delete(OFFLINEMSG_TAG+token+"_"+user_id);
	
				//System.out.println("delete msg: " +user_id+" msgid:"+token );
				
			}catch (Exception e){
		     	System.out.println("delete msg erro: " +msgobj+" msgid:"+token );
				e.printStackTrace()
			}

		}





		return getResultOK(msgobj);
	}

	def GetUnReadMsg(HttpServletRequest request)
	{

		String msgType = request["msg_type"];
		String userid = request["user_id"];


		long timestamp =Long.valueOf(request["timestamp"]).longValue();
		def collectionList = msgs().find(
				$$("is_read" : false,
				"type" : msgType,
				"timestamp":$$($gt : timestamp),
				"to_user":new BasicDBObject('$in', [userid.toString()])
				))
				.sort($$("timestamp" : -1))
				.toArray();

		for(int i=0;i<collectionList.size();i++)
		{
			msgs().update(
					$$("_id" : collectionList.get(i).get("_id")),
					$$( $set : $$("is_read" : true ))
					);
		}

		//,"to_user":$$($in : {userid})

		return getResultOK(collectionList);
	}
	
	
	
	def IPushService pushService =GetPushService();
	
	
	//发送未评价超时提醒。
	def pushRemindMsg(Integer user_id,String topic_id)
	{

		
		Map map = new HashMap();
		Integer _id = msgKGS.nextId();
		map.put("_id",_id);
		map.put("define_info",topic_id);
		map.put("user_type",UserType.普通用户.ordinal());
		map.put("type",com.izhubo.model.MsgType.个人消息.ordinal());
		map.put("title","您有问题未评价");
		map.put("user_id",user_id);
		map.put("content","您有问题未评价");
		map.put("msg_action",PushMsgActionEnum.mytopices_topic.name());
		map.put("timestamp",System.currentTimeMillis());
		map.put("status",0);
		map.put("msg_button_text","去评价");
		
		if(messages().save(new BasicDBObject(map)).getN() == 1){
			
			sendMsgById(_id);
								
		}
		
	}
	
	
	//发送商机提醒给单个教师
	def pushBusinessRemindMsg(Integer user_id,String content,MongoTemplate mongo)
	{
		
		Map map = new HashMap();
		Integer _id = msgKGS.nextId();
		map.put("_id",_id);
		map.put("define_info","url");
		map.put("user_type",UserType.招生老师.ordinal());
		map.put("type",com.izhubo.model.MsgType.个人消息.ordinal());
		map.put("title",content);
		map.put("user_id",user_id);
		map.put("content",content);
		map.put("msg_action",PushMsgActionEnum.tourl.name());
		map.put("timestamp",System.currentTimeMillis());
		map.put("status",0);
		map.put("msg_button_text","去跟进");
		
		if(mongo.getCollection("messages").save(new BasicDBObject(map)).getN() == 1){
			
			PUSH_ENV = Integer.valueOf(com.izhubo.rest.AppProperties.get("push.env").toString());
	
			sendMsgById(_id);
			
								
		}
		
	}
	
	//
	//处理老师发消息逻辑，type=0 群发  type=1 单独发送给老师
	def processTeacherMsg(String msgContent,String school_pk,String nc_user_id,int type,MongoTemplate mongo)
	{
		
		if(type == 0)
		{
			String school_code = mongo.getCollection("area").findOne($$("nc_id":school_pk)).get("code");
			//找出对应school_code的老师，进行群发
			def user_list =mongo.getCollection("users").find($$("priv":UserType.招生老师.ordinal(),"status":true,"school_code":school_code)).toArray();
			user_list.each {item->
				pushBusinessRemindMsg(Integer.valueOf(item["_id"].toString()),msgContent,mongo );
			}
			
		}
	}

    
	
	
	@TypeChecked(TypeCheckingMode.SKIP)
	private def sendMsgById(id){
		if(id == null){
			return [code : 0]
		}
		def msg = messages().findOne($$("_id", id as Integer));
		if(msg == null){
			return [code : 0]
		}
		def user_ids = null

		PushMsgBase pushmsg = new PushMsgBase();
		pushmsg.msg_title = msg["title"];


		if(msg.get("msg_action").toString() == PushMsgActionEnum.tourl.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.adver.ordinal();
			pushmsg.msg_url = msg["content"];
		}
		else if(msg.get("msg_action").toString() == PushMsgActionEnum.mytopices_topic.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.mytopices_topic.toString();
			pushmsg.define_info = msg["content"];
			//pushmsg.msg_content =  "精彩问题推送";
		}
		else if(msg.get("msg_action").toString() == PushMsgActionEnum.defult.name())
		{
			pushmsg.msg_type = PushMsgTypeEnum.text.ordinal();
			pushmsg.push_action_name = PushMsgActionEnum.defult.toString();
			pushmsg.define_info = msg["content"];
			//pushmsg.msg_content =  msg["content"];
		}
		if(msg.get("type") == com.izhubo.model.MsgType.个人消息.ordinal()){//发送方式 用户ID
			def user_id = msg.get("user_id");
			if(user_id != null)
				sendMsgsbyObject(user_id, msg["title"], pushmsg, msg["type"]);
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
			
		}

	


		return [code : 1]
	}
	
	
	def sendMsgsbyObject(final user_id,  final title,  final PushMsgBase pushmsg,  final type){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						Long l = System.currentTimeMillis()
						List<DBObject> msgList = new ArrayList<DBObject>();


						Long tmp = System.currentTimeMillis()
						Integer id = user_id as Integer

						def userobj =  mainMongo.getCollection("users").findOne(new BasicDBObject('_id',id));

						pushService.PushServiceInit(PUSH_ENV);

						if(userobj["priv"] == 3)
						{
							pushService.PushMsgToSingleUser(id,pushmsg);
						}
						else if(userobj["priv"] == 2)
						{
							pushService.PushMsgToSingleTeacher(id,pushmsg);
						}

						//调用API发消息



						println "sendMsgs cost time: ${System.currentTimeMillis() - l}"
					}
				}
				);

	}



}