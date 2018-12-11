package io.renren.dao;


import io.renren.entity.UserPerformEntity;
import io.renren.pojo.UserPerformPOJO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserPerformDao extends BaseDao<UserPerformEntity> {
    void save(UserPerformEntity userPerform);

    Long queryOrderId(@Param("userId") Long userId, @Param("commodityName") String commodityName);

    Map<String, Object> queryOrderAndUser(@Param("nickName") String nickName, @Param("commodityName") String commodityName);

    int update(UserPerformEntity userPerform);

    int deleteBatch(Map<String, Object> map);

    UserPerformPOJO queryObject1(@Param("id") long id);

}
