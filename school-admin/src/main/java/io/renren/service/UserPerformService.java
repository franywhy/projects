package io.renren.service;


import io.renren.entity.UserPerformEntity;
import io.renren.pojo.UserPerformPOJO;

import java.util.List;
import java.util.Map;

public interface UserPerformService {
    void save(UserPerformEntity userPerform);

    void deleteBatch(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    List<UserPerformEntity> queryList(Map<String, Object> map);

    Long queryOrderId(Long userId, String commodityName);

    void saveBatch(List<UserPerformEntity> detailList);

    Map<String, Object> queryOrderAndUser(String nickName, String commodityName);

    void update(UserPerformEntity userPerform);

    UserPerformPOJO queryObject1(Long id);

}
