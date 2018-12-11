package com.izhubo.web

import com.izhubo.common.util.KeyUtils
import com.izhubo.model.UserType
import com.izhubo.rest.AppProperties
import com.izhubo.rest.pushmsg.model.PushMsgActionEnum
import com.mongodb.DBCollection
import net.sf.json.JSONObject

import java.text.SimpleDateFormat

import static com.izhubo.rest.common.doc.MongoKey.$set;
import static com.izhubo.rest.common.doc.MongoKey._id;
import static com.izhubo.rest.common.util.WebUtils.$$;

import java.sql.Timestamp
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Value
import org.apache.commons.lang.StringUtils

import com.izhubo.utils.SuperHashMap;
import com.mongodb.BasicDBObject
import com.mongodb.DBObject;
import com.izhubo.rest.common.util.http.HttpClientUtil;
import com.izhubo.rest.persistent.KGS
import com.izhubo.rest.web.StaticSpring;



/**
 * 公开发送站内信的接口，便于后台脚本调用
 * @author wubinjie
 *
 */
@Controller
class MessageController extends BaseController {


	@Resource
	protected KGS msgKGS;

	public DBCollection app_live() {
		return mainMongo.getCollection("app_live");
	}

	public DBCollection app_live_reservation() {
		return mainMongo.getCollection("app_live_reservation");
	}

	def messages(){
		mainMongo.getCollection("message_admin")
	}


	
	@Value("#{application['admin.domain']}")
	private String admin_domain = "http://ttadmin.app1101168695.twsapp.com/";
	
	private String send_url = "/message/send.json";
	
	
	/**
	 * 不支持多用户发送
	 *
	 * @param id
	 */
	private void sendMsgJobById(Integer id) {
		if (id == null) {
			return;
		}
		HttpClientUtil.get(admin_domain + send_url + "?_id="
			+ id, null);
		
	}

	//s	获取老师信息
	def test(HttpServletRequest request){

		LiveRemindTimeoutProcess(KeyUtils.LIVE.APP_LIVE_REMIND+"1");
		return getResultOK();
	}
	
	public void MsgJobTimeoutProcess(String key) {
		println "==============================MsgJobTimeoutProcess topickey:" + key
		def msg_id = key.split(':')[1];
		if(StringUtils.isNotBlank(msg_id)){
			msg_id = Integer.parseInt(msg_id);
			sendMsgJobById(msg_id);
		}
		
	}
	private String hqjyh5domain = AppProperties.get("hqjyh5.domain").toString();
	def  buildliveurl(String live_id)
	{
		return hqjyh5domain +"/kj-live-detail.html?live_id="+live_id;
	}


	public void LiveRemindTimeoutProcess(String key) {
		println "==============================MsgJobTimeoutProcess topickey:" + key

		if(key.startsWith(KeyUtils.LIVE.APP_LIVE_REMIND)) {
			def live_id = key.split(':')[2];
			def live_obj = app_live().findOne($$("_id": live_id));
			if (live_obj) {
			//拿到预约的所有学员，然后做推送
            def resuserlist =app_live_reservation().find($$("live_id":live_id)).toArray();
			if(resuserlist)
			{
				def userints = new ArrayList();
				resuserlist.each {def x ->

                   def user_id = x.get("user_id");


					userints.add(user_id);


				}

				Map map = new HashMap();
				Integer _id = msgKGS.nextId();
				map.put("_id", _id);
				map.put("define_info", buildliveurl(live_id));
				map.put("user_type", UserType.普通用户.ordinal());
				map.put("type", com.izhubo.model.MsgType.个人消息.ordinal());
				map.put("title", "您有直播即将开始:"+live_obj.get("live_title")+"时间："+liveOpenTimeFormate((Long)live_obj.get("live_start_time"),(Long)live_obj.get("live_end_time")));
				//map.put("title", "直播提醒");
				map.put("t_users", userints);
				map.put("msg_action", PushMsgActionEnum.live_detail.name());
				map.put("timestamp", System.currentTimeMillis());
				map.put("status", 0);
				map.put("msg_button_text", "看直播");

				if (messages().save(new BasicDBObject(map)).getN() == 1) {

					sendMsgJobById(_id);


				}
			}


		}
		}
		else if(key.startsWith(KeyUtils.LIVE.MID_LIVE_REMIND))
		{

		}



	}


	private  String liveOpenTimeFormate(Long start,Long end){
		if(start > 0&&end>0){
			SimpleDateFormat Startformatter = new SimpleDateFormat ("yyyy/MM/dd HH:mm");
			SimpleDateFormat Endformatter = new SimpleDateFormat ("HH:mm");
			return Startformatter.format(new Date(start))+"~"+Endformatter.format(new Date(end));
		}
		return "-";
	}
	
	


}
