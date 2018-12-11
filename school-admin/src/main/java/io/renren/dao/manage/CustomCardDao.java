package io.renren.dao.manage;

import io.renren.entity.manage.CustomCard;
import io.renren.pojo.manage.CustomCardPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomCardDao {
    /**
     * 批量更新状态
     */
    int updateBatch(Map<String, Object> map);

    int insert(CustomCard record);

    List<CustomCardPOJO> queryPojoList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    CustomCardPOJO queryPojoObject(Map<String, Object> map);
    
    int updateByPrimaryKey(CustomCard record);

    int updatePushStatus(Map<String, Object> map);

    int deleteBatch(Map<String, Object> map);

    Map<String, Object> queryMsgMap(Long cardId);

    String queryCountByPushTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}