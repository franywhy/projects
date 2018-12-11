package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.renren.service.CourseUserplanService;
import io.renren.service.MessageXLXWClassService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.DateUtils;
import io.renren.utils.SyncDateConstant;

/**
 * 学来学往班级消息推送到题库
 * @class io.renren.service.MessageXLXWClassService.java
 * @Description:
 * @author shihongjie
 * @dete 2017年10月19日
 */
@Service("messageXLXWClassService")
public class MessageXLXWClassServiceImpl implements MessageXLXWClassService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	/** 推送学来学往班级消息队列名 */
	private static String XLXW_CLASS_MESSAGE = "";
	@Value("#{rabbitmq['xlxw.class.sync.tk']}")
	private void setXLXW_CLASS_MESSAGE(String str){
		XLXW_CLASS_MESSAGE = str;
		logger.info("MessageXLXWClassServiceImpl setXLXW_CLASS_MESSAGE XLXW_CLASS_MESSAGE={}",XLXW_CLASS_MESSAGE);
	}
	/** 推送录播课消息队列名 */
	private static String ALL_COURSE_MESSAGE = "";
	@Value("#{rabbitmq['all.course.sync.tk']}")
	private void setALL_CLASS_MESSAGE(String str){
		ALL_COURSE_MESSAGE = str;
		logger.info("MessageVideoCourseServiceImpl setVIDEO_COURSE_MESSAGE VIDEO_COURSE_MESSAGE={}",ALL_COURSE_MESSAGE);
	}
	@Override
	public void pushToMessageQueueClass() {
		List<Map<String , Object>> list=queryXLXWClassMessage();
		Gson gson=new Gson();
		for (Map<String, Object> map : list) {
			mapDateFormatter(map, "ts");
			String json = gson.toJson(map).toString();
			logger.info("MessageXLXWClassServiceImpl pushToMessageQueueClass json:{}",json);
			amqpTemplate.convertAndSend(XLXW_CLASS_MESSAGE, json);
			amqpTemplate.convertAndSend(ALL_COURSE_MESSAGE, json);
		}
		sysCheckQuoteService.updateSyncTime(new HashMap<String , Object>(), SyncDateConstant.course_userplan_xlxw);
	}
	
	private List<Map<String , Object>> queryXLXWClassMessage(){
		String millisecond=sysCheckQuoteService.syncDate(new HashMap<String , Object>(), SyncDateConstant.course_userplan_xlxw); 
		List<Map<String, Object>> list = this.courseUserplanService.queryXLXWClassMessage(millisecond);
		if(null != list && !list.isEmpty()){
			for (Map<String, Object> map : list) {
				List<String> codeList = this.courseUserplanService.queryCodeListByCommodityId(map.get("goodId"));
				map.put("courseTkCode", tkCourseCode(codeList));
			}
		}
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
	
	private static String tkCourseCode(List<String> codeList){
		String result = null;
		if(null != codeList && !codeList.isEmpty()){
			StringBuffer sbf = new StringBuffer();
			for (String string : codeList) {
				sbf.append(string + ",");
			}
			result = sbf.substring(0, sbf.length() - 1);
		}
		return result;
	}

	
//	public static void main(String[] args) {
//		String s= "1,2,3,";
//		List<String> codeList = new ArrayList<>();
//		codeList.add("1");
//		codeList.add("2");
//		codeList.add("3");
//		codeList.add("4");
//		System.out.println(tkCourseCode(codeList));
//	}
}
