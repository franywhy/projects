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

import io.renren.dao.MallOrderDao;
import io.renren.service.CourseUserplanService;
import io.renren.service.MessageVideoCourseService;
import io.renren.utils.DateUtils;

/**
 * 录播课消息推送到题库
 * @dete 2017年11月3日
 */
@Service("messageVideoCourseService")
public class MessageVideoCourseServiceImpl implements MessageVideoCourseService {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

    //ThreadPoolExecutor poolExecutor = ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor();

	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private MallOrderDao mallOrderDao;
	@Autowired
	private CourseUserplanService courseUserplanService;
    @Autowired
    private ClassToTkLogService classToTkLogService;
	/*
    *//** 推送开通题库权限消息队列名 *//*
    private static String OPEN_PERMISSION_MESSAGE = "";
    @Value("#{rabbitmq['open.permission.sync.tk']}")
    private void setALL_CLASS_MESSAGE(String str){
        OPEN_PERMISSION_MESSAGE = str;
        logger.info("MessageVideoCourseServiceImpl setVIDEO_COURSE_MESSAGE VIDEO_COURSE_MESSAGE={}",OPEN_PERMISSION_MESSAGE);
    }*/

    /** 推送会计班级消息队列名 */
    private static String KJ_CLASS_MESSAGE = "";
    @Value("#{rabbitmq['kj.class.sync.tk']}")
    private void setKJ_CLASS_MESSAGE(String str){
        KJ_CLASS_MESSAGE = str;
        logger.info("MessageKJClassServiceImpl setKJ_CLASS_MESSAGE KJ_CLASS_MESSAGE={}",KJ_CLASS_MESSAGE);
    }
	@Override
	public void pushToMessageQueueClass(String startDate, String endDate, String orderNo, String commodityIds) {
		List<Map<String , Object>> list = null;
		if(null != startDate || null != endDate){
			if(DateUtils.matchDateString(startDate) && DateUtils.matchDateString(endDate)){
				list=queryVideoClassMessage(startDate+" 00:00:00", endDate+" 23:59:59", orderNo, commodityIds);
			}else{
				logger.error("MessageVideoClassServiceImpl pushToMessageQueueClass error:params date no match");
			}
		}else{
			list=queryVideoClassMessage(null, null, orderNo, commodityIds);
		}
		Gson gson=new Gson();
		if(null!=list && !list.isEmpty()){
			for (Map<String, Object> map : list) {
                map.put("open",1);
                map.put("isNewClass",1);
				mapDateFormatter(map, "ts");
				String json = gson.toJson(map).toString();
				logger.info("MessageVideoClassServiceImpl pushToMessageQueueClass json:{}",json);
                ClassToTkLogEntity entity = new ClassToTkLogEntity();
                entity.setCreatetime(new Date());
                entity.setPushJson(json);
                entity.setUserId((Long) map.get("userId"));
                entity.setGoodId((Long) map.get("goodId"));
                entity.setUserMobile((String) map.get("mobile"));
                entity.setRemark("录播分班:MessageVideoClassServiceImpl");
               /* poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {*/
                        classToTkLogService.save(entity);
                /*    }
                });*/
				amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);
			}
		}
	}
	
	private List<Map<String , Object>> queryVideoClassMessage(String startDate, String endDate, String orderNo, String commodityIds){
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
		if(null != orderNo) mapQuery.put("orderNo", orderNo);
		List<Map<String, Object>> list = mallOrderDao.queryVideoCourse(mapQuery);
		if(null != list && !list.isEmpty()){
			for (Map<String, Object> mapResult : list) {
				List<String> codeList = courseUserplanService.queryCodeListByCommodityId(mapResult.get("goodId"));
				mapResult.put("courseTkCode", tkCourseCode(codeList));
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
}
