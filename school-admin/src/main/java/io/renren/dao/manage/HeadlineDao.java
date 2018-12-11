package io.renren.dao.manage;

import io.renren.entity.manage.Headline;
import io.renren.pojo.manage.HeadlinePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HeadlineDao {

    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);

    int insert(Headline record);

    int updateByPrimaryKey(Headline record);

    int updatePushStatus(Map<String, Object> map);

    List<HeadlinePOJO> queryPojoList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    HeadlinePOJO queryPojoObject(Map<String, Object> map);

    int deleteBatch(Map<String, Object> map);

    Map<String, Object> queryMsgMap(Long headlineId);

    String queryCountByPushTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}