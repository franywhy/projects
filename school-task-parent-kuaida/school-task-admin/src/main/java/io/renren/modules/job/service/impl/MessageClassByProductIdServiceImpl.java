package io.renren.modules.job.service.impl;

import com.google.gson.Gson;
import io.renren.modules.job.entity.ClassToTkLogEntity;
import io.renren.modules.job.service.ClassToTkLogService;
import io.renren.modules.job.service.CourseUserplanService;
import io.renren.modules.job.service.MessageClassByProductIdService;
import io.renren.modules.job.service.SysCheckQuoteService;
import io.renren.modules.job.utils.DateUtils;
import io.renren.modules.job.utils.SyncDateConstant;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DL on 2018/6/20.
 */
@Service("messageClassServiceByProductId")
public class MessageClassByProductIdServiceImpl implements MessageClassByProductIdService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private SysCheckQuoteService sysCheckQuoteService;
    @Autowired
    private CourseUserplanService courseUserplanService;
    @Autowired
    private ClassToTkLogService classToTkLogService;

    /** 推送会计/学来学往班级消息队列名 */
    private static String KJ_CLASS_MESSAGE = "";
    @Value("${kj.class.sync.tk}")
    private void setKJ_CLASS_MESSAGE(String str){
        KJ_CLASS_MESSAGE = str;
        logger.error("MessageClassByProductIdServiceImpl setKJ_CLASS_MESSAGE KJ_CLASS_MESSAGE={}",KJ_CLASS_MESSAGE);
    }


    /** 推送自考班级消息队列名 */
    private static String ZK_CLASS_MESSAGE = "";
    @Value("${zk.class.sync.tk}")
    private  void setZK_CLASS_MESSAGE(String str) {
        ZK_CLASS_MESSAGE = str;
        logger.error("MessageClassByProductIdServiceImpl setZK_CLASS_MESSAGE ZK_CLASS_MESSAGE={}",ZK_CLASS_MESSAGE);
    }





    @Override
    public void pushToMessageQueueClass() {
        List<Map<String , Object>> list=queryKJClassMessage();
        logger.error("MessageClassByProductIdServiceImpl  pushToMessageQueueClass  classMessageList={}",list);
        for (Map<String, Object> map : list) {
            map.put("isNewClass",1);
            mapDateFormatter(map, "ts");
            Long productId = (Long) map.get("productId");
            if (1L == productId ){
                String json = getMessageJson(map);
                amqpTemplate.convertAndSend(ZK_CLASS_MESSAGE, json);
            }else {
                String json = getMessageJson(map);
                amqpTemplate.convertAndSend(KJ_CLASS_MESSAGE, json);
            }

        }
        sysCheckQuoteService.updateSyncTime(new HashMap<String , Object>(), SyncDateConstant.course_userplan);
    }

    private String getMessageJson(Map<String, Object> map) {
        Gson gson=new Gson();
        map.remove("productId");
        String json = gson.toJson(map).toString();
        logger.error("MessageClassByProductIdServiceImpl pushToMessageQueueClass classMessageJson:{}",json);
        ClassToTkLogEntity entity = new ClassToTkLogEntity();
        entity.setCreatetime(new Date());
        entity.setPushJson(json);
        entity.setUserId((Long) map.get("userId"));
        entity.setGoodId((Long) map.get("goodId"));
        entity.setUserMobile((String) map.get("mobile"));
        entity.setRemark("分班推送:MessageClassByProductIdServiceImpl");
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                classToTkLogService.save(entity);
            }
        });
        return json;
    }

    private List<Map<String , Object>> queryKJClassMessage(){
        String millisecond=sysCheckQuoteService.syncDate(new HashMap<String , Object>(), SyncDateConstant.course_userplan);
//        String millisecond = "2018-06-01";
        List<Map<String, Object>> list = this.courseUserplanService.queryClassMessageByProductId(millisecond);
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
