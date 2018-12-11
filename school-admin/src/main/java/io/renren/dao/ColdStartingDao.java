package io.renren.dao;


import io.renren.entity.ColdStartingEntity;

import java.util.Map;

/**
 * 冷启动数据
 * 
 * @author linyuebin
 * @email trust_100@163.com
 * @date 2017-12-30 11:30:54
 */
public interface ColdStartingDao extends BaseDao<ColdStartingEntity> {

    void resume(Map<String, Object> map);

    void pause(Map<String, Object> map);
}
