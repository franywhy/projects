package com.izhubo.web.mq;

import com.alibaba.fastjson.JSON;
import com.izhubo.rest.persistent.KGS;
import com.izhubo.userSystem.mongo.qquser.QQUser;
import com.izhubo.userSystem.mongo.qquser.QQUserRepositery;
import com.izhubo.utils.AES;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.izhubo.rest.common.doc.MongoKey.$setOnInsert;
import static com.izhubo.rest.common.doc.MongoKey._id;

/**
 * @author hq
 */
@Controller
public class UserMessageConsumer implements ChannelAwareMessageListener {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private QQUserRepositery qqUserRepositery;
	@Resource
    private MongoTemplate mainMongo;
	@Resource
    private KGS userKGS;
	//默认密码123456
	private static String PSW = "afa651c0a832371d479f8131271e20cc";
	private static String pd = "kuaida";
	private static String pic = "http://answerimg.kjcity.com/default_student.png";
	private static final String ACAESKEY = "71bcbe39400d8328";

	private DBCollection users(){
		return mainMongo.getCollection("users");
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		logger.info("开通员工会答账号任务收到队列信息" + " [x] Received '" + new String(message.getBody(), "UTF-8") + "'");
		QQUser qqUser = new QQUser();
		String body = new String(message.getBody(), "UTF-8");//获取消息体
		try {
			Map<String, Object> mapBody = JSON.parseObject(body,HashMap.class);//将消息体转成map
			String STATUS = (String) mapBody.get("status");
			String nick_name = (String) mapBody.get("user_name");
			String mobile = ((String) mapBody.get("mobile")).trim();
			String school_code = (String) mapBody.get("xq_code");
			String school_name = (String) mapBody.get("xq_name");
			String pk = (String) mapBody.get("pk_psndoc");
			//在职
			if("0".equals(STATUS)){
				List<QQUser> qqUserList = this.qqUserRepositery.findByPk(pk);
				//新增
				if(qqUserList.size() < 1){
					qqUserList = this.qqUserRepositery.findByUsername(mobile);
					if(qqUserList.size() == 0) {
						String tuid = com.izhubo.rest.common.util.RandomUtil.getTuid();
						qqUser.setNickName(nick_name);
						qqUser.setPassword(PSW);
						qqUser.setQd(pd);
						qqUser.setTuid(tuid);
						qqUser.setUsername(mobile);
						qqUser.setPk(pk);
						this.qqUserRepositery.save(qqUser);
						this.addUser(tuid, nick_name, PSW, mobile, school_code, school_name, pic);
					} else {
						qqUser = qqUserList.get(0);
						qqUser.setNickName(nick_name);
						qqUser.setPk(pk);
						this.qqUserRepositery.save(qqUser);
						this.updateUser(qqUser.getTuid(), nick_name, mobile, school_code, school_name);
					}
				}else{//修改
					qqUser =  qqUserList.get(0);
					qqUser.setUsername(mobile);
					qqUser.setNickName(nick_name);
					this.qqUserRepositery.save(qqUser);
					this.updateUser(qqUser.getTuid(), nick_name, mobile, school_code, school_name);
				}
				
			} else{//离职
				this.updateUser(mobile);
			}
			
		} catch (Exception e) {
			try {
                e.printStackTrace();
                logger.error(" UserMessageConsumer onMessage error Cause by:{}, message body=[{}]", e.getMessage(),body);
			} catch (Exception e2) {
				e2.printStackTrace();
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		} finally{
			try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			} catch (Exception e) {
				logger.error(" Nc数据提交出错! exception message:{}, message body=[{}]", e.getMessage(),body);
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		}
	}
	
	private DBObject addUser(String tuid, String nick_name, String password, String mobile, 
			                 String school_code, String school_name, String pic) throws Exception {
		
		BasicDBObject query_tuid = new BasicDBObject("tuid", tuid);
        DBObject user = users().findOne(query_tuid);
        if (null == user) {
        	user = new BasicDBObject(_id, userKGS.nextId());
        	user.put("tuid", tuid);
        	user.put("nick_name", nick_name);
        	user.put("real_name_from_nc", nick_name);//真实姓名
			user.put("is_hq",1);
        	user.put("priv", 2);//2：老师 3：员工
        	user.put("status", Boolean.TRUE);
        	user.put("timestamp", System.currentTimeMillis());
        	user.put("pic", pic);
        	user.put("topic_count", 0);
            user.put("topic_evaluation_count", 0);
            user.put("ackey", AES.aesEncrypt(password, ACAESKEY));
            user.put("vlevel", 0);
            user.put("is_show_update", 0);
            user.put("mobile", mobile);
            user.put("qd", "kuaida");
            
            user.put("priv0", 1);// 学生权限
            user.put("priv1", 1);// 抢答权限
            user.put("priv2", 0);// 招生权限
            user.put("priv3", 0);
            user.put("priv4", 0);
            user.put("priv5", 0);
            user.put("priv6", 0);
            user.put("priv7", 0);
            user.put("priv8", 0);
            user.put("priv9", 0);
            user.put("school_code", school_code);
            user.put("school_name", school_name);
            try {
                return users().findAndModify(
                        query_tuid.append(_id, user.removeField(_id)), null,
                        null, false, new BasicDBObject($setOnInsert, user),
                        true, true); // upsert
            } catch (MongoException e) {
                e.printStackTrace();
                query_tuid.remove(_id);
                return users().findOne(query_tuid);
            }
        } else{
        	return user;
        }
	}
	
	private void updateUser(String mobile){
		List<QQUser> list = this.qqUserRepositery.findByUsername(mobile);
		if (list.size() > 0) {
			QQUser qqUser = list.get(0);
			try {
                users().update(new BasicDBObject("tuid", qqUser.getTuid()).append("is_hq",1),
						new BasicDBObject("$set",new BasicDBObject("priv",3).append("priv1",0)));
            } catch (MongoException e) {
                e.printStackTrace();
                logger.error("UserMessageConsumer updateUser Exception："+e.toString());
            }
		} else {
			logger.info("UserMessageConsumer updateUser 找不到这个员工！！！");
		}
	}
	
	private void updateUser(String tuid, String nick_name, String mobile, String school_code, String school_name){
		try {
            users().update(
            		new BasicDBObject("tuid", tuid),
					new BasicDBObject("$set",
					new BasicDBObject("nick_name",nick_name).append("real_name_from_nc",nick_name).append("mobile",mobile).append("school_code",school_code).append("school_name",school_name)));
        } catch (MongoException e) {
            e.printStackTrace();
            logger.error("UserMessageConsumer updateUser Exception："+e.toString());
        }
	}
}
