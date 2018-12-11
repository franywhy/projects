package io.renren.controller.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import io.renren.entity.manage.*;
import io.renren.service.UsersService;
import io.renren.service.manage.AppAccountService;
import io.renren.service.manage.AppCommodityService;
import io.renren.service.manage.AppService;
import io.renren.service.manage.UserAppService;
import io.renren.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.renren.utils.RedisUtil.getJedis;

/**
 * NC报名消息队列消费者
 * @author hq
 */
public class SignConsumer implements ChannelAwareMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(SignConsumer.class);

	private static final String LOCK_KEY = "signConsumerLockKey";

	/** 休眠时间*/
	private static long queueSleepMillis = 0;
	public static void setQueueSleepMillis(long queueSleepMillis) {
		SignConsumer.queueSleepMillis = queueSleepMillis;
	}

	@Autowired
	private AppService appService;
	
	@Autowired
	private AppAccountService appAccountService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserAppService userAppService;

	@Autowired
	private AppCommodityService appCommodityService;

	@Autowired
	private JedisPool jedisPool;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicQos(0,1,false);
		LOG.info((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) +
				"	收到队列信息" + " [x] Received '" + message + "'");
		Thread.sleep(queueSleepMillis);

		String requestId = message.getMessageProperties().getDeliveryTag()+"";

		try {
			String body=new String(message.getBody(),"UTF-8");
			JSONObject JO = JSONObject.parseObject(body);
			/**
			 * {"company_type":"xuelxuew","code":"HQ052018041300110379","school_code":"JM102040101","user_name":"尚双双",
			 * "nc_school_pk":"0001A5100000000LG01B","class_id":"1001A510000000271DST","classtype":"2","predict_open_time":1482409755000,
			 * "dr":0,"class_type_id":"1001A5100000002KI629","nc_user_id":"1001A5100000003SCEOM","province_pk":"0001A510000000000L3I",
			 * "sign_status":1,"spec_status":1,"nc_id":"1001A5100000003T9A7Z","class_name":"会计零风险签约取证班专科","vbill_status":1,
			 * "create_time":1523605892000,"ks_name":"晚19:30至21:30","sex":2,"school_name":"广州增城新塘校区","province_name":"广东省（加盟）",
			 * "product_type":3,"phone":"13460689903","syn_time":1523940002012,"nc_commodity_id":"1001A510000000271DST","zk_province_pk":"",
			 * "push_time":1523940002012,"classtime":"7","status":1,"ts":1523894946000}
			 */
			/**
			 * 其他课程=0,CPA=1,初级职称=2,学历课程=3,从业=4,猎才计划=5,中级职称=6,中央财大=7,卓越级=8,自考课程=9,实务级=10,入门级=11,
			 * 全能级=12,精英级=13,精斗云财务软件=15,初级直播=16,中级直播=17,K自考=18,CMA=19,财务数据分析师=20,设计类课程=21,IT类课程=22
			 */
			//判断初级职称，初级直播，CPA
			if (JO != null && JO.getInteger("spec_status") == 1) {
				AppCommodity appCommodity = appCommodityService.selectByExampleFetchOne(JO.getString("nc_commodity_id"));
				if(null != appCommodity) {
					Users users = usersService.findByMobile(JO.getString("phone"));
					if(null != users) {
						int itemType = appCommodity.getItemType();
						String code = null;
						if(1 == itemType) {
							code = "A006";
						} else if (2 == itemType || 16 == itemType) {
							code = "A005";
						}
						List<App> appList = appService.findListByCode(code);
						if(appList != null) {
							int lockSum = 0;
							while (true) {
								++lockSum;
								if (RedisUtil.tryGetDistributedLock(getJedis(jedisPool), LOCK_KEY, requestId, 3000)) {
									LOG.info("消息：" + requestId + " 成功获得分布式锁");
									try {
										AppAccount appAccount = appAccountService.findIsStudentByCode(code);
										if(appAccount != null) {
											List<UserApp> list = new ArrayList<>();
											for (App app : appList) {
												int userAppCount = userAppService.findByUseridAppid(users.getUserId(),app.getAppid());
												//不存在记录
												if(userAppCount == 0) {
													UserApp userApp = new UserApp();
													userApp.setUserid(users.getUserId());
													userApp.setAppid(app.getAppid());
													userApp.setCode(app.getCode());
													userApp.setUsername(appAccount.getUsername());
													userApp.setUserpass(appAccount.getUserpass());
													userApp.setCourseid(app.getCourseid());
													userApp.setSchoolCode(JO.getString("school_code"));
													userApp.setSchoolName(JO.getString("school_name"));
													userApp.setNcCommodityId(JO.getString("nc_commodity_id"));
													userApp.setClasstype(JO.getString("class_type_id"));
													userApp.setCreatetime(new Date());
													userApp.setDr(0);
													list.add(userApp);
												}
											}
											if(list.size() > 0) {
												userAppService.addUserAppListUpdateAccountDr(list, appAccount.getAccountid());
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

}
