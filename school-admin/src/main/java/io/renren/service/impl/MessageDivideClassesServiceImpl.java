package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.renren.dao.MallOrderDao;
import io.renren.entity.ClassToTkLogEntity;
import io.renren.service.ClassToTkLogService;
import io.renren.service.GoodsCoursetkService;
import io.renren.service.MessageDivideClassesService;
import io.renren.utils.DateUtils;
import io.renren.utils.ThreadPoolExecutorUtils;
/**
 * 各业务线分班消息推送到题库
 * @dete 2018/4/21
 * @author Liuhai
 *
 */
@Service("messageDivideClassesService")
public class MessageDivideClassesServiceImpl implements MessageDivideClassesService {
	protected Logger logger = LoggerFactory.getLogger(getClass());

    /*ThreadPoolExecutor poolExecutor = ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor();*/

	@Autowired
	private AmqpTemplate amqpTemplate;
	@Autowired
	private MallOrderDao mallOrderDao;
	@Autowired
    private ClassToTkLogService classToTkLogService;
	@Autowired
	private GoodsCoursetkService goodsCoursetkService;
	
	/** 推送到题库的分班班级消息队列名 */
    private static String DIVIDE_CLASS_MESSAGE = "";
    @Value("#{rabbitmq['divide.class.sync.tk']}")
    private void setDIVIDE_CLASS_MESSAGE(String str){
    	DIVIDE_CLASS_MESSAGE = str;
        logger.info("MessageDivideClassesServiceImpl setDIVIDE_CLASS_MESSAGE DIVIDE_CLASS_MESSAGE={}",DIVIDE_CLASS_MESSAGE);
    }
	@Override
	public void pushToTkDivideClassesMessageQueue(
								String startDate, 
								String endDate, 
								String commodityIds,
								String productIds,
								String watchType) 
	{
		
		// TODO Auto-generated method stub
		List<Map<String , Object>> list = null;
		if(null != startDate || null != endDate){
			if(DateUtils.matchDateString(startDate) && DateUtils.matchDateString(endDate)){
				list = this.queryDivideClassesMessage(startDate+" 00:00:00", endDate+" 23:59:59", commodityIds, productIds, watchType);
			}else{
				logger.error("MessageDivideClassesServiceImpl pushToTkDivideClassesMessageQueue error:params date no match");
			}
		}else{
			list = this.queryDivideClassesMessage(null, null, commodityIds, productIds, watchType);
		}
		
		Gson gson=new Gson();
		if(null!=list && !list.isEmpty()){
			ClassToTkLogEntity entity = new ClassToTkLogEntity();
			for (Map<String, Object> map : list) {
                map.put("open",1);
                map.put("isNewClass",1);
				mapDateFormatter(map, "ts");
				String json = gson.toJson(map).toString();
				logger.info("MessageDivideClassesServiceImpl pushToTkDivideClassesMessageQueue json:{}",json);
                entity.setCreatetime(new Date());
                entity.setPushJson(json);
                entity.setUserId((Long) map.get("userId"));
                entity.setGoodId((Long) map.get("goodId"));
                entity.setUserMobile((String) map.get("mobile"));
                entity.setRemark("分班推送:MessageDivideClassesServiceImpl");
                /*poolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {*/
                		/*classToTkLogService.save(entity);*/
                    /*}
                });*/
                try {
                	classToTkLogService.save(entity);
                	amqpTemplate.convertAndSend(DIVIDE_CLASS_MESSAGE, json);
                } catch (Exception e) {
                	e.printStackTrace();
                }
			}
		}
		
	}
	
	private List<Map<String, Object>> queryDivideClassesMessage(String startDate, String endDate, String commodityIds, String productIds, String watchType){
		Map<String,Object> mapQuery = new HashMap<>();
		List<Long> commodityIdList = new ArrayList<Long>();
		List<Long> productIdList = new ArrayList<Long>();
		if(StringUtils.isNotBlank(commodityIds)){
			String[] splitstr = commodityIds.split(",");
			for (String string : splitstr) {
				
				Long commodityId = Long.parseLong(string.trim());
				int count = this.goodsCoursetkService.queryTotalByCommodityId(commodityId);
				if(count > 0){
					
					commodityIdList.add(commodityId);
				}
				
			}
		}
		if(StringUtils.isNotBlank(productIds)){
			String[] splitstr = productIds.split(",");
			for (String string : splitstr) {
				productIdList.add(Long.parseLong(string.trim()));
			}
		}
		
		if(null != startDate) mapQuery.put("startDate", startDate);
		if(null != endDate) mapQuery.put("endDate", endDate);
		if(null != commodityIdList && commodityIdList.size() > 0) mapQuery.put("commodityIdList", commodityIdList);
		if(null != productIdList && productIdList.size() > 0) mapQuery.put("productIdList", productIdList);
		if(null != watchType) mapQuery.put("watchType", Integer.parseInt(watchType));
		
		List<Map<String, Object>> list = this.mallOrderDao.queryDivideClassesMessage(mapQuery);
		if(null != list && !list.isEmpty()){
			for (Map<String, Object> map : list) {
				List<String> codeList = this.goodsCoursetkService.queryCodeListByCommodityId(map.get("goodId"));
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

}
