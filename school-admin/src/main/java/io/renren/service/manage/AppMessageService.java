package io.renren.service.manage;

import io.renren.entity.manage.AppMessage;
import io.renren.pojo.manage.AppMessagePOJO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/29 0029.
 */
public interface AppMessageService {

    int queryTotal(Map<String, Object> map);

    List<AppMessagePOJO> queryPojoList(Map<String, Object> map);

    AppMessagePOJO queryPojoObject(Map<String, Object> map);

    void save(AppMessage appMessage);

    void update(AppMessage appMessage);

    void deleteBatch(Map<String, Object> map);

    void updatePushStatus(Long messageId, int pushStatus, Date pushTime, String pushMsgId);

    Map<String, Object> queryMsgMap(Long messageId);

    String isExistSavePushTime(Date pushTime);
}
