package io.renren.modules.job.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;

import io.renren.modules.job.service.CourseClassplanLivesService;
import io.renren.modules.job.service.CourseClassplanService;
import io.renren.modules.job.service.MessageCourseClassplanService;
import io.renren.modules.job.service.SysCheckQuoteService;
import io.renren.modules.job.utils.SyncDateConstant;
import org.springframework.stereotype.Service;

@Service("messageCourseClassplanService")
public class MessageCourseClassplanServiceImpl implements MessageCourseClassplanService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	@Autowired
	private CourseClassplanService courseClassplanService;
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	
	/** 推送双师排课计划到NCDE 消息队列名 */
	private static String SS_COURSE_CLASSPLAN = "";
	@Value("${ss.classplan.sync.nc}")
	private void setSS_COURSE_CLASSPLAN(String str){
		SS_COURSE_CLASSPLAN = str;
		logger.info("MessageCourseClassplanServiceImpl setSS_COURSE_CLASSPLAN SS_COURSE_CLASSPLAN={}",SS_COURSE_CLASSPLAN);
	}
	
	@Override
	public void pushToNCCourseClassplanMessageQueue() {
		List<Map<String , Object>> list=querySsCourseClassplanMessage();
		for (Map<String, Object> map : list) {
			String json = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
			System.out.println(json);
			logger.info("MessageCourseClassplanServiceImpl PushMsgCourseClassplantoNC json:{}",json);
			amqpTemplate.convertAndSend(SS_COURSE_CLASSPLAN, json);
		}

		sysCheckQuoteService.updateSyncTime(new HashMap<String , Object>(), SyncDateConstant.course_classplan);
	}
	
	private List<Map<String , Object>> querySsCourseClassplanMessage(){
		String millisecond=sysCheckQuoteService.syncDate(new HashMap<String , Object>(), SyncDateConstant.course_classplan);
		List<Map<String, Object>> list = this.courseClassplanService.querySsCourseClassplanMessage(millisecond);
		if(null != list && !list.isEmpty()){
			for (Map<String, Object> map : list) {
				Integer dr = (Integer) map.get("dr");
				if(dr == 0){
					
					List<Map<String, Object>> listDetails = this.courseClassplanLivesService.queryListByClassplanId(map.get("arrangeid"));
					int i = 1;//直播课次排序
					if(null != listDetails && !listDetails.isEmpty()){
						
						for (Map<String, Object> classplanLivesMap : listDetails) {
							classplanLivesMap.put("classhour", i++);
						}
					}

					map.put("arrangedetail", listDetails);
				}
			}
		}
		return list;
	}

}
