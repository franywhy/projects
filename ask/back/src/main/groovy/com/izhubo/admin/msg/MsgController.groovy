package com.izhubo.admin.msg

import static com.izhubo.rest.common.util.WebUtils.$$
import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

import java.util.concurrent.TimeUnit

import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import com.izhubo.admin.BaseController
import com.izhubo.rest.anno.Rest
import com.izhubo.rest.common.util.JSONUtil
import com.mongodb.BasicDBObject
import com.mongodb.DBCollection

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

		//消息内容
		//消息对象
		//消息id


		String json = request["json"];

		println "json: ${json}"

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
	
				System.out.println("delete msg: " +user_id+" msgid:"+token );
				
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



}