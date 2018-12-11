package io.renren.service.impl;

import com.google.gson.Gson;
import io.renren.entity.ClassToTkLogEntity;
import io.renren.service.*;
import io.renren.utils.DateUtils;
import io.renren.utils.SyncDateConstant;
import io.renren.utils.ThreadPoolExecutorUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by DL on 2018/2/26.
 */
@Service("messageKJOldClassService")
public class MessageKJOldClassServiceImpl implements MessageKJOldClassService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //ThreadPoolExecutor poolExecutor = ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor();
    /** 推送会计班级消息队列名 */
    private static String KJ_CLASS_MESSAGE = "";
    @Value("#{rabbitmq['kj.class.sync.tk']}")
    private void setKJ_CLASS_MESSAGE(String str){
        KJ_CLASS_MESSAGE = str;
        logger.info("MessageKJClassServiceImpl setKJ_CLASS_MESSAGE KJ_CLASS_MESSAGE={}",KJ_CLASS_MESSAGE);
    }

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private SysCheckQuoteService sysCheckQuoteService;
    @Autowired
    private CourseUserplanService courseUserplanService;
    @Autowired
    private UserOldclassLogService userOldclassLogService;
    @Autowired
    private ClassToTkLogService classToTkLogService;

    @Override
    public void pushToMessageQueueClass() {
        List<Map<String , Object>> list=queryKJClassMessage();
        Gson gson=new Gson();
        for (Map<String, Object> map : list) {
            mapDateFormatter(map, "ts");
            String json = gson.toJson(map).toString();
            logger.info("MessageKJOldClassServiceImpl pushToMessageQueueClass json:{}",json);
            ClassToTkLogEntity entity = new ClassToTkLogEntity();
            entity.setCreatetime(new Date());
            entity.setPushJson(json);
            entity.setUserId((Long) map.get("userId"));
            entity.setGoodId((Long) map.get("goodId"));
            entity.setUserMobile((String) map.get("mobile"));
            entity.setRemark("转班前:MessageKJOldClassServiceImpl");
            /*poolExecutor.execute(new Runnable() {
                @Override
                public void run() {*/
                    classToTkLogService.save(entity);
            /*    }
            });*/
            amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);
        }
        sysCheckQuoteService.updateSyncTime(new HashMap<String , Object>(), SyncDateConstant.user_oldClass_log);
    }

    private List<Map<String , Object>> queryKJClassMessage(){
        String millisecond=sysCheckQuoteService.syncDate(new HashMap<String , Object>(), SyncDateConstant.user_oldClass_log);
        List<Map<String, Object>> list = this.userOldclassLogService.queryMessage(millisecond);
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

}
