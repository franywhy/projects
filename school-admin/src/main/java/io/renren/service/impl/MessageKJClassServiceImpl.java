package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import io.renren.entity.ClassToTkLogEntity;
import io.renren.service.ClassToTkLogService;
import io.renren.utils.ThreadPoolExecutorUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.renren.service.CourseUserplanService;
import io.renren.service.MessageKJClassService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.DateUtils;
import io.renren.utils.SyncDateConstant;

/**
 * 会计班级消息推送到题库
 * @class io.renren.service.MessageKJClassService.java
 * @Description:
 * @author shihongjie
 * @dete 2017年10月13日
 */
@Service("messageKJClassService")
public class MessageKJClassServiceImpl implements MessageKJClassService {

    //ThreadPoolExecutor poolExecutor = ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor();
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	@Autowired
	private CourseUserplanService courseUserplanService;
	@Autowired
    private ClassToTkLogService classToTkLogService;

	/** 推送会计班级消息队列名 */
	private static String KJ_CLASS_MESSAGE = "";
	@Value("#{rabbitmq['kj.class.sync.tk']}")
	private void setKJ_CLASS_MESSAGE(String str){
		KJ_CLASS_MESSAGE = str;
		logger.info("MessageKJClassServiceImpl setKJ_CLASS_MESSAGE KJ_CLASS_MESSAGE={}",KJ_CLASS_MESSAGE);
	}
	/** 推送开通题库权限消息队列名 */
	private static String OPEN_PERMISSION_MESSAGE = "";
	@Value("#{rabbitmq['open.permission.sync.tk']}")
	private void setALL_CLASS_MESSAGE(String str){
        OPEN_PERMISSION_MESSAGE = str;
		logger.info("MessageVideoCourseServiceImpl setVIDEO_COURSE_MESSAGE VIDEO_COURSE_MESSAGE={}",OPEN_PERMISSION_MESSAGE);
	}
	@Override
	public void pushToMessageQueueClass() {
		List<Map<String , Object>> list=queryKJClassMessage();
		Gson gson=new Gson();
		for (Map<String, Object> map : list) {
		    map.put("open",1);
		    map.put("isNewClass",1);
			mapDateFormatter(map, "ts");
            String json = gson.toJson(map).toString();
			logger.info("MessageKJClassServiceImpl pushToMessageQueueClass json:{}",json);
            ClassToTkLogEntity entity = new ClassToTkLogEntity();
            entity.setCreatetime(new Date());
            entity.setPushJson(json);
            entity.setUserId((Long) map.get("userId"));
            entity.setGoodId((Long) map.get("goodId"));
            entity.setUserMobile((String) map.get("mobile"));
            entity.setRemark("分班推送:MessageKJClassServiceImpl");
			//推送前先保存日记
           /* poolExecutor.execute(new Runnable() {
                @Override
                public void run() {*/
                   classToTkLogService.save(entity);
              /*   }
            });*/
			amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);
			//amqpTemplate.convertAndSend(OPEN_PERMISSION_MESSAGE, json);
		}
		sysCheckQuoteService.updateSyncTime(new HashMap<String , Object>(), SyncDateConstant.course_userplan);
	}

    @Override
    public void pushToMessageQueueClass(String startDate, String endDate, String commodityIds) {
        List<Map<String , Object>> list=queryKJClassMessage(startDate,endDate,commodityIds);
        Gson gson=new Gson();
        for (Map<String, Object> map : list) {
            map.put("open",1);
            map.put("isNewClass",1);
            mapDateFormatter(map, "ts");
            String json = gson.toJson(map).toString();
            logger.info("MessageKJClassServiceImpl pushToMessageQueueClass json:{}",json);
            ClassToTkLogEntity entity = new ClassToTkLogEntity();
            entity.setCreatetime(new Date());
            entity.setPushJson(json);
            entity.setUserId((Long) map.get("userId"));
            entity.setGoodId((Long) map.get("goodId"));
            entity.setUserMobile((String) map.get("mobile"));
            entity.setRemark("补推:MessageKJClassServiceImpl");
            /*poolExecutor.execute(new Runnable() {
                @Override
                public void run() {*/
                    classToTkLogService.save(entity);
             /*   }
            });*/
            amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);
        }
    }

    private List<Map<String,Object>> queryKJClassMessage(String startDate, String endDate, String commodityIds) {
        Map<String,Object> mapQuery = new HashMap<>();
        List<Long> commodityIdList = new ArrayList<Long>();
        if(StringUtils.isNotBlank(commodityIds)){
            String[] splitstr = commodityIds.split(",");
            for (String string : splitstr) {
                commodityIdList.add(Long.parseLong(string.trim()));
            }
        }
        if(null != startDate) mapQuery.put("startDate", startDate);
        if(null != endDate) mapQuery.put("endDate", endDate);
        if(null != commodityIdList && commodityIdList.size() > 0) mapQuery.put("commodityIdList", commodityIdList);
        List<Map<String, Object>> list = this.courseUserplanService.queryKJClassMessage(mapQuery);
        if(null != list && !list.isEmpty()){
            for (Map<String, Object> map : list) {
                List<String> codeList = this.courseUserplanService.queryCodeListByCommodityId(map.get("goodId"));
                map.put("courseTkCode", tkCourseCode(codeList));
            }
        }
        return list;
    }

    private List<Map<String , Object>> queryKJClassMessage(){
		String millisecond=sysCheckQuoteService.syncDate(new HashMap<String , Object>(), SyncDateConstant.course_userplan); 
		List<Map<String, Object>> list = this.courseUserplanService.queryKJClassMessage(millisecond);
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
