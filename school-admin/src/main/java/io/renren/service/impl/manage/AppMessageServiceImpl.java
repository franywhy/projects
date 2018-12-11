package io.renren.service.impl.manage;

import io.renren.dao.CourseOliveDao;
import io.renren.dao.manage.AppMessageDao;
import io.renren.dao.manage.CustomCardDao;
import io.renren.dao.manage.HeadlineDao;
import io.renren.entity.manage.AppMessage;
import io.renren.pojo.manage.AppMessagePOJO;
import io.renren.service.manage.AppMessageService;
import io.renren.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2018/1/29 0029.
 */
@Service
public class AppMessageServiceImpl implements AppMessageService {

    @Autowired
    private AppMessageDao appMessageDao;
    @Autowired
    private CourseOliveDao courseOliveDao;
    @Autowired
    private HeadlineDao headlineDao;
    @Autowired
    private CustomCardDao customCardDao;

    @Value("#{application['pom.message.push.time']}")
    private int MESSAGESPACETIME;

    @Override
    public int queryTotal(Map<String, Object> map) {
        return appMessageDao.queryTotal(map);
    }

    @Override
    public List<AppMessagePOJO> queryPojoList(Map<String, Object> map) {
        return appMessageDao.queryPojoList(map);
    }

    @Override
    public AppMessagePOJO queryPojoObject(Map<String, Object> map) {
        return appMessageDao.queryPojoObject(map);
    }

    @Override
    public void save(AppMessage appMessage) {
        //创建时间
        appMessage.setCreateTime(new Date());
        //修改时间
        appMessage.setModifyTime(appMessage.getCreateTime());
        //初始化推送状态
        appMessage.setPushStatus(Constant.Push.NOT.getValue());
        appMessage.setDr(Constant.DR.NORMAL.getValue());
        appMessageDao.insert(appMessage);
    }

    @Override
    public void update(AppMessage appMessage) {
        appMessageDao.updateByPrimaryKey(appMessage);
    }

    @Override
    public void deleteBatch(Map<String, Object> map) {
        appMessageDao.deleteBatch(map);
    }

    @Override
    public void updatePushStatus(Long messageId, int pushStatus, Date pushTime, String pushMsgId) {
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("pushStatus", pushStatus);
        map.put("pushTime", pushTime);
        map.put("pushMsgId", pushMsgId);
        appMessageDao.updatePushStatus(map);
    }

    @Override
    public Map<String, Object> queryMsgMap(Long messageId) {
        return appMessageDao.queryMsgMap(messageId);
    }

    @Override
    public String isExistSavePushTime(Date pushTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pushTime);
        calendar.add(Calendar.MINUTE,-MESSAGESPACETIME*1);
        Date startTime = calendar.getTime();
        calendar.add(Calendar.MINUTE,+MESSAGESPACETIME*2);
        Date endTime = calendar.getTime();
        String message = " 有消息要推送,建议推送时间在此延后"+MESSAGESPACETIME+"分钟以上";
        //头条
        String headlinePushTime = headlineDao.queryCountByPushTime(startTime,endTime);
        if (headlinePushTime !=  null ){
            return headlinePushTime.substring(0,headlinePushTime.length()-2)+message;
        }
        //自定义卡片
        String customCardPushTime = customCardDao.queryCountByPushTime(startTime,endTime);
        if (customCardPushTime !=  null){
            return customCardPushTime.substring(0,customCardPushTime.length()-2)+message;
        }
        //系统消息
        String appMessagePushTime = appMessageDao.queryCountByPushTime(startTime,endTime);
        if (appMessagePushTime != null){
            return appMessagePushTime.substring(0,appMessagePushTime.length()-2)+message;
        }
        //公开课
        String courseOlivePushTime = courseOliveDao.queryCountByPushTime(startTime,endTime);
        if (courseOlivePushTime != null ){
            return courseOlivePushTime.substring(0,courseOlivePushTime.length()-2)+message;
        }
        return null;
    }
}
