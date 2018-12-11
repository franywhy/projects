package io.renren.service.impl;
import java.text.SimpleDateFormat;
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

import io.renren.service.CourseClassplanService;
import io.renren.service.MessageProductorService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.DateUtils;
import io.renren.utils.SyncDateConstant;
@Service("messageProductorCourseClassplanServiceImpl")
public class MessageProductorCourseClassplanServiceImpl implements MessageProductorService{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AmqpAdmin admin;
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private ConnectionFactory connectionFactory;
	@Autowired
	private CourseClassplanService courseClassplanService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	private static String COURSECLASSPLANMQ = "";
	@Value("#{rabbitmq['zk.plan.sync.tk']}")
	private void setCOURSECLASSPLANMQ(String str){
		COURSECLASSPLANMQ = str;
		logger.info("MessageProductorCourseClassplanServiceImpl setCOURSECLASSPLANMQ COURSECLASSPLANMQ={}",COURSECLASSPLANMQ);
	}
	@Override
	@Transactional
	public void pushToMessageQueue() {
		List<Map<String , Object>> list=queryClassPlanForQueue();
		Gson gson=new Gson();
		for (Map<String, Object> map : list) {
			mapDateFormatter(map, "creationTime");
			mapDateFormatter(map, "endTime");
			mapDateFormatter(map, "ts");
			amqpTemplate.convertAndSend(COURSECLASSPLANMQ, gson.toJson(map).toString());
		}
		Map<String , Object> map = new HashMap<>();
		map.put("schoolId", "test");
		sysCheckQuoteService.updateSyncTime(map, SyncDateConstant.course_classplan);
	}
//	为排课计划准备推送数据  
	public List<Map<String , Object>> queryClassPlanForQueue() {
//		Map<String , Object> map = getMap(request);
		Map<String , Object> map = new HashMap<>();
		map.put("schoolId", "test");
//		SysCheckQuoteService sysCheckQuote=new SysCheckQuoteServiceImpl();
		String millisecond=sysCheckQuoteService.syncDate(map, SyncDateConstant.course_userplan_class_detail); 
	    SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//	    long millisecond1= format.parse(millisecond).getTime();
	    map.put("millisecond", millisecond);
		List<Map<String , Object>> list=this.courseClassplanService.queryClassPlanForQueue(map);
		Map<String, Object> syncMap=new HashMap<>();
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
