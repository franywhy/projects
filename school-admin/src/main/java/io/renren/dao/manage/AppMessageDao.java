package io.renren.dao.manage;

import io.renren.entity.manage.AppMessage;
import io.renren.pojo.manage.AppMessagePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AppMessageDao {

    int insert(AppMessage record);

    int updateByPrimaryKey(AppMessage record);

    List<AppMessagePOJO> queryPojoList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    AppMessagePOJO queryPojoObject(Map<String, Object> map);

    int updatePushStatus(Map<String, Object> map);

    int deleteBatch(Map<String, Object> map);

    Map<String, Object> queryMsgMap(@Param("messageId") Long messageId);

    String queryCountByPushTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}