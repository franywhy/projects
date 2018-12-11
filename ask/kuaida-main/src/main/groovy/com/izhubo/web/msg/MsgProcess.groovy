package com.izhubo.web.msg

import com.izhubo.rest.pushmsg.model.PushMsgBase
import javax.annotation.Resource;

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import com.hqonline.model.Privs;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.rest.pushmsg.IPushService;
import com.izhubo.rest.pushmsg.PushEnvironment;
import com.izhubo.rest.pushmsg.XinggePushService;
import com.izhubo.rest.pushmsg.model.PushMsgBase
import com.izhubo.rest.pushmsg.model.PushMsgTypeEnum;
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum;
import com.izhubo.rest.web.StaticSpring

class MsgProcess {



	private MongoTemplate mainMongobyQueues;

	KGS  _msgKGS

	private int PUSH_ENV = 2;

	def IPushService pushService =GetPushService();

	public IPushService GetPushService() {
		XinggePushService xingePush = new XinggePushService();

		return xingePush;
	}

	public MsgProcess(MongoTemplate mainMongoc,KGS msgKGS) {
		PUSH_ENV =Integer.valueOf(com.izhubo.rest.AppProperties.get("push.env").toString())
		mainMongobyQueues = mainMongoc;
		_msgKGS = msgKGS;
	}

	def messages(){
		mainMongobyQueues.getCollection("message_main")
	}
	//
	//处理老师发消息逻辑，type=0 群发  type=1 单独发送给老师tip_kd


	def processTeacherMsg(String msgContent,String school_pk,String sm_user_id,int type,MongoTemplate mongo)
	{
		String zhaos = Privs.招生.key;
		if(type == 0)
		{
			//如果是单独跟进的老师，只发给单独跟进的那位老师
			if(!("~".equals(sm_user_id)))
			{
				def user =mongo.getCollection("users").findOne($$("sm_user_id",sm_user_id));
				if(user)
				{
					pushBusinessRemindMsg(Integer.valueOf(user["_id"].toString()),msgContent,mongo );
				}
			}
			else
			{

				//如果是尚未有跟进老师的，则发给校区所有老师
				String school_code = mongo.getCollection("area").findOne($$("nc_id":school_pk)).get("code");
				//找出对应school_code的老师，进行群发
				def user_list =mongo.getCollection("users").find($$("priv2":1,"status":true,"school_code":school_code)).toArray();
				user_list.each {item->
					pushBusinessRemindMsg(Integer.valueOf(item["_id"].toString()),msgContent,mongo );
				}
			}

		}
	}

	//发送商机提醒给单个教师
	def pushBusinessRemindMsg(Integer user_id,String content,MongoTemplate mongo)
	{

		Map map = new HashMap();
		Integer _id = _msgKGS.nextId();

		println _id+"";
		map.put("_id",_id);
		map.put("define_info","url");
		map.put("user_type",2);
		map.put("type",com.izhubo.model.MsgType.个人消息.ordinal());
		map.put("title",content);
		map.put("user_id",user_id);
		map.put("content",content);
		map.put("msg_action",PushMsgActionEnum.tourl.name());
		map.put("timestamp",System.currentTimeMillis());
		map.put("status",0);
		map.put("msg_button_text","去跟进");

		if(mongo.getCollection("message_main").save(new BasicDBObject(map)).getN() == 1){

			PUSH_ENV = Integer.valueOf(com.izhubo.rest.AppProperties.get("push.env").toString());

			//是生产环境再发消息
			if(PUSH_ENV == 1)
			{
				//sendMsgById(_id);
			}
			


		}

	}


	def sendMsgsbyObject(final user_id,final userobj,  final title,  final PushMsgBase pushmsg,  final type){
		StaticSpring.execute(
				new Runnable() {
					public void run() {
						Long l = System.currentTimeMillis()
						List<DBObject> msgList = new ArrayList<DBObject>();


						Long tmp = System.currentTimeMillis()
						Integer id = user_id as Integer


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
			{
				def userobj =  mainMongobyQueues.getCollection("users").findOne(new BasicDBObject('_id',user_id));
				sendMsgsbyObject(user_id,userobj, msg["title"], pushmsg, msg["type"]);
			}
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
}
