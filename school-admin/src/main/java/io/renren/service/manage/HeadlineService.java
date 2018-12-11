package io.renren.service.manage;

import io.renren.entity.manage.Headline;
import io.renren.pojo.manage.HeadlinePOJO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28 0028.
 */
public interface HeadlineService {
    
    int queryTotal(Map<String, Object> map);

    List<HeadlinePOJO> queryPojoList(Map<String, Object> map);

    HeadlinePOJO queryPojoObject(Map<String, Object> map);

    void save(Headline headline);

    void update(Headline headline);

    void deleteBatch(Map<String, Object> map);

    void pause(Long[] headlineIds);

    void resume(Long[] headlineIds);

    void updateAppStatus(Long[] headlineIds, int appStatus);

    void updatePushStatus(Long headlineId, int pushStatus, Date pushTime, String pushFindMsgId, int appStatus);

    Map<String, Object> queryMsgMap(Long headlineId);
}
