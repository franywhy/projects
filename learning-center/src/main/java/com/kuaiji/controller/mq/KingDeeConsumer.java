package com.kuaiji.controller.mq;

import com.alibaba.fastjson.JSONObject;
import com.kuaiji.entity.App;
import com.kuaiji.entity.UserApp;
import com.kuaiji.entity.Users;
import com.kuaiji.service.AppService;
import com.kuaiji.service.UserAppService;
import com.kuaiji.service.UsersService;
import com.kuaiji.utils.CommonUtil;
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

public class KingDeeConsumer implements ChannelAwareMessageListener {

	private static final Logger LOG = LoggerFactory.getLogger(KingDeeConsumer.class);

	@Autowired
	private AppService appService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private UserAppService userAppService;

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicQos(0,1,false);
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) +
				"	收到队列信息" + " [x] Received '" + message + "'");
		
		try {
			String body=new String(message.getBody(),"UTF-8");
			JSONObject JO = JSONObject.parseObject(body);
			/**
			 * {"company_type":"kuaiji","code":"HQ052015101600397508","school_code":"JH105010101","user_name":"吕申诺","nc_school_pk":"0001A510000000000L6C","class_id":"1001A5100000000WVYZJ",
			 * "classtype":"财务会计学段系列","predict_open_time":1445838780000,"dr":0,"class_type_id":"1001A5100000002GMBQ2","nc_user_id":"1001A51000000017S6IG","province_pk":"0001AAAAAAAAAAAAAA",
			 * "sign_status":1,"spec_status":1,"nc_id":"1001A51000000017W7GP","class_name":"财务会计学段Ⅰ（面授无证）","create_time":1444975165000,"ks_name":"1.3.5白天","sex":2,
			 * "school_name":"阜阳校区","province_name":"安徽省",
			 * "product_type":7,"phone":"18325835126","syn_time":1508895182282,"nc_commodity_id":"1001A5100000000WVYZJ","zk_province_pk":"","push_time":1508895182282,"classtime":"9","status":1,"ts":1508895237000}
			 */
			//判断精英级、卓越级、猎才计划
			if (JO != null && JO.getInteger("spec_status") == 1 && CommonUtil.getList().contains(JO.getString("nc_commodity_id"))) {
				Users users = usersService.findByMobile(JO.getString("phone"));
				if(null != users) {
					App app = appService.findByCode("B001");
					if(null != app) {
						int userAppCount = userAppService.findByUseridAppid(users.getUserId(), app.getAppid());
						if(0 == userAppCount) { //不存在记录
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
							userApp.setClasstype(JO.getString("class_name"));
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
			LOG.error("KingDeeConsumer error",e);
		}
	}
}
