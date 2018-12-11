package io.renren.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

import io.renren.service.CourseUserplanClassDetailService;
import io.renren.service.MessageProductorService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.DateUtils;
import io.renren.utils.SyncDateConstant;
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:rabbitmq2.xml" })*/
@Service("messageProductorCourseUserplanClassServiceImpl")
public class MessageProductorCourseUserplanClassServiceImpl implements MessageProductorService{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AmqpAdmin admin;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private CourseUserplanClassDetailService courseUserplanClassDetailService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	private static String USERPLANCLASSMQ = "";
	@Value("#{rabbitmq['zk.userplanclass.sync.tk']}")
	private void setUSERPLANCLASSMQ(String str){
		USERPLANCLASSMQ = str;
		logger.info("MessageProductorCourseUserplanClassServiceImpl setUSERPLANCLASSMQ USERPLANCLASSMQ={}",USERPLANCLASSMQ);
	}
	@Override
	@Transactional
	public void pushToMessageQueue() {
		List<Map<String , Object>> list=queryUserplanClassDetailForQueue();
		Gson gson=new Gson();
		for (Map<String, Object> map : list) {
			mapDateFormatter(map, "createdTime");
			mapDateFormatter(map, "ts");
			amqpTemplate.convertAndSend(USERPLANCLASSMQ, gson.toJson(map).toString());
		}
		Map<String , Object> map = new HashMap<>();
		map.put("schoolId", "test");
		sysCheckQuoteService.updateSyncTime(map, SyncDateConstant.course_userplan_class_detail);
	}
//	为排课计划准备推送数据  
	public List<Map<String , Object>> queryUserplanClassDetailForQueue() {
		Map<String , Object> map = new HashMap<>();
		map.put("schoolId", "test");
		String millisecond=sysCheckQuoteService.syncDate(map, SyncDateConstant.course_userplan_class_detail); 
	    map.put("millisecond", millisecond);
		List<Map<String , Object>> list=this.courseUserplanClassDetailService.queryUserplanClassDetailForQueue(map);
		return list;
	}
	
	/**
	 * 日期格式化
	 * @param map
	 * @param dateKey
	 */
	private static void mapDateFormatter(Map<String , Object> map , String dateKey){
		if(null != map && StringUtils.isNotBlank(dateKey)){
			Object object = map.get(dateKey);
			if(null != object){
				try {
					Date date = (Date) object;
					String format = DateUtils.format(date, DateUtils.DATE_TIME_PATTERN);
					map.put(dateKey, format);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

     
}
