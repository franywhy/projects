package io.renren.controller.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import io.renren.entity.manage.*;
import io.renren.service.UsersService;
import io.renren.service.manage.AppAccountService;
import io.renren.service.manage.AppService;
import io.renren.service.manage.NcCourseService;
import io.renren.service.manage.UserAppService;
import io.renren.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static io.renren.utils.CommonUtil.tryProduct;
import static io.renren.utils.CommonUtil.userSync;
import static io.renren.utils.RedisUtil.getJedis;

/**
 * NC排课消息队列消费者
 * @author hq
 */
public class MessageConsumer implements ChannelAwareMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

	private static final String LOCK_KEY = "messageConsumerLockKey";

	@Autowired
	private NcCourseService ncCourseService;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private AppAccountService appAccountService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserAppService userAppService;

	@Autowired
	private JedisPool jedisPool;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicQos(0,1,false);
		LOG.info((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) +
				"	收到队列信息" + " [x] Received '" + message + "'");

		String requestId = message.getMessageProperties().getDeliveryTag()+"";

		try {
			String body=new String(message.getBody(),"UTF-8");
			JSONObject JO = JSONObject.parseObject(body);
			if(JO != null) {
				//{"school_code":"JH208050101","course_code":"kckm01","course_name":"会计基础","phone":"13640029276","spec_status":1,"nc_id":"1001A51000000009K60O","school_name":"韶关浈江校区","nc_commodity_id":"1001A51000000005A3AM","push_time":1535983768755,"nc_sign_id":"1001A51000000004G3R4","dr":0}

				Users users = usersService.findByMobile(JO.getString("phone"));
				if(users != null) {
					//判断精英级、卓越级、猎才计划
					/*if (JO.getInteger("spec_status") == 1 && CommonUtil.getList().contains(JO.getString("nc_commodity_id"))) {
						App app = appService.findByCode("B001");
						if(null != app) {
							this.openKingDee(users, JO, app);
						}
					}*/
					/************************************ 精斗云↑↑********** 会计乐↓↓************************************************/
					List<NcCourse> courses = ncCourseService.findByNcCode(JO.getString("course_code"));
					//赠送实训
					if(courses != null && courses.size() > 0) {
						for (NcCourse course : courses) {
							String province = JO.getString("school_code").substring(0,5);
							App app = appService.findByCodeProvince(course.getCode(), province);
							if(app != null) {
								if("B001".equals(app.getCode())) {
									this.openKingDee(users, JO, app);
								} else {
									this.openAccFun(requestId, app, users, course, JO, message, channel);
								}
							}
						}
					}
				}
			}
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			System.out.println(e.toString());
			//记录报错日志
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			LOG.error("MessageConsumer error",e);
		}
		
	}

	/**
	 * 开通精斗云
	 * @param users
	 * @param JO
	 * @param app
	 */
	private void openKingDee(Users users, JSONObject JO, App app) {
		int userAppCount = userAppService.findByUseridAppid(users.getUserId(), app.getAppid());
		//不存在记录
		if(0 == userAppCount) {
			UserApp userApp = new UserApp();
			userApp.setUserid(users.getUserId());
			userApp.setAppid(app.getAppid());
			userApp.setCode("B001");
			userApp.setUsername(JO.getString("phone"));
			userApp.setUserpass("888888");
			userApp.setCourseid(app.getCourseid());
			userApp.setSchoolCode(JO.getString("school_code"));
			userApp.setSchoolName(JO.getString("school_name"));
			userApp.setNcCommodityId(JO.getString("nc_commodity_id"));
			userApp.setCreatetime(new Date());
			userApp.setDr(0);
			userAppService.addUserApp(userApp);

			//用户同步接口
			String pwd = "JDeO-1612-02D-1107-8711";
			int sex = 0;
			userSync(JO.getString("phone"), sex, JO.getString("phone"), pwd);
			//试用产品接口
			tryProduct(JO.getString("phone"),pwd);
		}
	}

	/**
	 * 开通会计乐
	 * @param requestId
	 * @param app
	 * @param users
	 * @param course
	 * @param JO
	 * @param message
	 * @param channel
	 * @throws Exception
	 */
	private void openAccFun(String requestId, App app, Users users, NcCourse course, JSONObject JO, Message message, Channel channel) throws Exception {
		int lockSum = 0;
		while (true) {
			++lockSum;
			if (RedisUtil.tryGetDistributedLock(getJedis(jedisPool), LOCK_KEY, requestId, 3000)) {
				LOG.info("消息：" + requestId + " 成功获得分布式锁");
				try {
					AppAccount appAccount = appAccountService.findIsStudentByAppId(app.getAppid());
					if(appAccount != null) {
						int userAppCount = userAppService.findByUseridAppid(users.getUserId(),app.getAppid());
						//不存在记录
						if(userAppCount == 0) {
							UserApp userApp = new UserApp();
							userApp.setUserid(users.getUserId());
							userApp.setAppid(app.getAppid());
							userApp.setCode(course.getCode());
							userApp.setUsername(appAccount.getUsername());
							userApp.setUserpass(appAccount.getUserpass());
							userApp.setCourseid(app.getCourseid());
							userApp.setSchoolCode(JO.getString("school_code"));
							userApp.setSchoolName(JO.getString("school_name"));
							userApp.setNcCommodityId(JO.getString("course_code"));
							userApp.setClasstype(JO.getString("course_name"));
							userApp.setCreatetime(new Date());
							userApp.setDr(0);
							userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
						}
					}
				} finally {
					if(RedisUtil.releaseDistributedLock(getJedis(jedisPool), LOCK_KEY, requestId)) {
						LOG.info("消息："+ requestId +" 成功释放分布式锁");
					}
				}
				break;
			} else {
				if(lockSum > 35) {
					LOG.info("消息："+requestId+" 第"+lockSum+"次无法获得分布式锁，放弃抢锁！！！");
					channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
					return;
				}
				LOG.info("消息："+requestId+" 第"+lockSum+"次无法获得分布式锁，休眠100毫秒，继续抢锁！！！");
				Thread.sleep(100);
			}
		}
	}

}
