package com.izhubo.web.mq;

import com.alibaba.fastjson.JSONObject;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hq
 */
public class TeacherPlanConsumer implements ChannelAwareMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(TeacherPlanConsumer.class);

	@Autowired
	private QQUserRepositery qqUserRepositery;

	@Resource
	private MongoTemplate mainMongo;

	private DBCollection users() {
		return mainMongo.getCollection("users");
	}

	private DBCollection tip_content() {
		return mainMongo.getCollection("tip_content");
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicQos(0,1,false);
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) +
				"	收到队列信息" + " [x] Received '" + message + "'");
		
		try {
			String body=new String(message.getBody(),"UTF-8");
			LOG.info("TeacherPlanConsumer message body:	"+body);
			JSONObject JO = JSONObject.parseObject(body);
			/**
			 * 蓝鲸排课：
			 * {"mobile":"15622168302","courseNo":"kckm001","businessId":"kuaiji"}
			 */
			/**
			 * NC排课：
			 * {"ncId":"1001A5100000002OCNAC","teacherCode":"93628","teacherName":"余杰萍","psndocId":"0001A5100000000HQPA4","mobile":"13760001102","spstatus":1,"classPlanCode":"HJ022017061300075196","courseId":"1001A71000000000C9IW","classType":"正常排课","courseName":"电脑账","courseNo":"kckm05","openTime":"2017-06-19 09:37:37","pk_org":null,"status":1,"teacherId":"1001A5100000002L1VZE","dr":0,"ts":1498800009000,"pushTimestamp":null,"businessId":"kuaiji"}
			 */
			if (JO != null) {
				String mobile = JO.getString("mobile");
				String courseNo = JO.getString("courseNo");
				String businessId = JO.getString("businessId");
				if(StringUtils.isBlank(mobile) || StringUtils.isBlank(courseNo) || (!"kuaiji".equals(businessId) && !"zikao".equals(businessId))) {
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
					return;
				}
				List<String> courseNoList = Arrays.asList(courseNo);
				String key = "user_industry";
				int product = 0;
				if("zikao".equals(businessId)) {
					key = "user_industry_zikao";
					product = 1;
				}
				List<QQUser> qqUsers = qqUserRepositery.findByUsername(mobile);
				if(null != qqUsers && qqUsers.size() > 0) {
					QQUser qqUser = qqUsers.get(0);
					DBObject user = users().findOne(new BasicDBObject("tuid", qqUser.getTuid()), new BasicDBObject(key,1));
					if(null != user) {
						List<DBObject> listTip = new ArrayList<>();
						DBObject mapTip = new BasicDBObject();
						Set<Integer> tipSet = new HashSet<>();
						List<DBObject> userIndustry = (List<DBObject>) user.get(key);
						if(null != userIndustry && userIndustry.size() > 0) {
							DBObject object = userIndustry.get(0);
							if(null != object) {
								List<DBObject> users_industry_tip = (List<DBObject>)object.get("users_industry_tip");
								for (DBObject item : users_industry_tip) {
									tipSet.add((Integer) item.get("industry_tip_id"));
								}
							}
						}
						List<DBObject> tipList = tip_content().find(
								new BasicDBObject("product",product).append("dr",0).append("course_no",new BasicDBObject("$in",courseNoList)),
								new BasicDBObject("_id",1)).toArray();
						for (DBObject item : tipList) {
							tipSet.add((Integer) item.get("_id"));
						}
						for (Integer item : tipSet) {
							DBObject idMap = new BasicDBObject();
							idMap.put("industry_tip_id", item);
							listTip.add(idMap);
						}
						mapTip.put("industry_id",1111);
						mapTip.put("users_industry_tip",listTip);
						users().update(new BasicDBObject("tuid", qqUser.getTuid()),
								new BasicDBObject("$set",new BasicDBObject(key,Arrays.asList(mapTip)).append("business_id", Arrays.asList(businessId)).append("priv1",1).append("priv",2)));
					}
				}
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			System.out.println(e.toString());
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			LOG.error("KingDeeConsumer error",e);
		}
	}
}
