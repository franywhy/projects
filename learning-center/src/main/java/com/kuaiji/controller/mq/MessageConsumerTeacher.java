package com.kuaiji.controller.mq;

import com.alibaba.fastjson.JSONObject;
import com.kuaiji.entity.App;
import com.kuaiji.entity.AppAccount;
import com.kuaiji.entity.UserApp;
import com.kuaiji.entity.Users;
import com.kuaiji.service.AppAccountService;
import com.kuaiji.service.AppService;
import com.kuaiji.service.UserAppService;
import com.kuaiji.service.UsersService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kuaiji.utils.CommonUtil.tryProduct;
import static com.kuaiji.utils.CommonUtil.userSync;

public class MessageConsumerTeacher implements ChannelAwareMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(MessageConsumerTeacher.class);

	@Autowired
	private AppService appService;
	
	@Autowired
	private AppAccountService appAccountService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserAppService userAppService;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicQos(0,1,false);
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) +
				"	收到队列信息" + " [x] Received '" + message + "'");
		String[] courseCode = new String[]{"A003","A001"};

		try {
			String body=new String(message.getBody(),"UTF-8");
			JSONObject JO = JSONObject.parseObject(body);
			if(JO != null) {
				/**
				 * {"school_code":"JH101080301","course_code":"kckm01","course_name":"会计基础","phone":"15874790563","spec_status":0,"nc_id":"1001A51000000020S2UE","school_name":"衡阳雁峰校区","push_time":1503021094370,
				 * "nc_sign_id":"1001A51000000020S2UC","dr":0,"status":3}
				 *
				 * {"sm_user_id":"0001A510000000000XXV","enablestate":"2","teacher_name":"李景","school_nc_code":"JH211020201","post_name":"全职老师","school_nc_id":"0001A510000000066YN2","teacher_mobile":"14752266902",
				 * "usertype":1,"push_time":1505667602681,"teacher_nc_id":"1001A51000000009OB2Z","ts":"2017-09-18 01:00:03","cardend":"106820"}
				 */
				Users users = usersService.findByMobile(JO.getString("teacher_mobile"));
				if(users != null) {
					for (String code : courseCode) { //赠送实训
						String province = JO.getString("school_nc_code").substring(0,5);
						App app = appService.findByCodeProvince(code, province);
						if(app != null) {
							AppAccount appAccount = appAccountService.findIsTeacherByAppId(app.getAppid());
							if(appAccount != null) {
								int userAppCount = userAppService.findByUseridAppid(users.getUserId(),app.getAppid());
								if(userAppCount == 0) { //不存在记录
									UserApp userApp = new UserApp();
									userApp.setUserid(users.getUserId());
									userApp.setAppid(app.getAppid());
									userApp.setCode(code);
									userApp.setUsername(appAccount.getUsername());
									userApp.setUserpass(appAccount.getUserpass());
									userApp.setCourseid(app.getCourseid());
									userApp.setSchoolCode(JO.getString("school_nc_code"));
									/*userApp.setSchoolName(JO.getString("school_name"));
									userApp.setNcCommodityId(JO.getString("course_code"));
									userApp.setClasstype(JO.getString("course_name"));*/
									userApp.setCreatetime(new Date());
									userApp.setDr(0);
									userAppService.addUserAppUpdateAccountDr(userApp, appAccount.getAccountid());
								}
							}
						} else { //金蝶友商
							app = appService.findByCode(code);
							if(app != null) {
								int userAppCount = userAppService.findByUseridAppid(users.getUserId(), app.getAppid());
								if(userAppCount == 0) { //不存在记录
									UserApp userApp = new UserApp();
									userApp.setUserid(users.getUserId());
									userApp.setAppid(app.getAppid());
									userApp.setCode(code);
									userApp.setUsername(JO.getString("teacher_mobile"));
									userApp.setUserpass("888888");
									userApp.setCourseid(app.getCourseid());
									userApp.setSchoolCode(JO.getString("school_nc_code"));
									/*userApp.setSchoolName(JO.getString("school_name"));
									userApp.setNcCommodityId(JO.getString("course_code"));
									userApp.setClasstype(JO.getString("course_name"));*/
									userApp.setCreatetime(new Date());
									userApp.setDr(0);
									userAppService.addUserApp(userApp);

									//用户同步接口
									String pwd = "JDeO-1612-02D-1107-8711";
									int sex = 0;
									userSync(JO.getString("teacher_mobile"), sex, JO.getString("teacher_mobile"), pwd);
									//试用产品接口
									tryProduct(JO.getString("teacher_mobile"),pwd);
								}
							}
						}
					}
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				} else {
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				}
			} else {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			//记录报错日志
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			LOG.error("MessageConsumerTeacher error",e);
		}
		
	}

}
