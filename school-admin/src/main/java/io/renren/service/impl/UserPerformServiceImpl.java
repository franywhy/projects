package io.renren.service.impl;

import io.renren.dao.UserPerformDao;
import io.renren.entity.UserPerformEntity;
import io.renren.pojo.UserPerformPOJO;
import io.renren.service.UserPerformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service("userPerformService")
public class UserPerformServiceImpl implements UserPerformService {

    @Autowired
    private UserPerformDao userPerformDao;

    public void save(UserPerformEntity userPerform) {
        userPerformDao.save(userPerform);
    }


    public void deleteBatch(Map<String, Object> map) {
        userPerformDao.deleteBatch(map);
    }


    public int queryTotal(Map<String, Object> map) {
        return userPerformDao.queryTotal(map);
    }

    public List<UserPerformEntity> queryList(Map<String, Object> map) {
        return userPerformDao.queryList(map);
    }

    public Long queryOrderId(Long userId, String commodityName) {
        return userPerformDao.queryOrderId(userId, commodityName);
    }


    public void saveBatch(List<UserPerformEntity> list) {
        userPerformDao.saveBatch(list);
    }

    public Map<String, Object> queryOrderAndUser(String nickName, String commodityName) {
        return userPerformDao.queryOrderAndUser(nickName, commodityName);
    }

    public void update(UserPerformEntity userPerform) {
        userPerformDao.update(userPerform);
    }

    public UserPerformPOJO queryObject1(Long id) {
        return userPerformDao.queryObject1(id);
    }
}
