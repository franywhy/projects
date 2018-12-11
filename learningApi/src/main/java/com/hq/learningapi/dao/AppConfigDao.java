package com.hq.learningapi.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hq.learningapi.entity.AppConfigEntity;

/**
 * app常量记录表
 * @author zhaowenwei
 * @date 2018-02-26 14:58:44
 */
@Repository
public interface AppConfigDao {

	String queryValueByKey(Long key);

	AppConfigEntity queryObject(Long id);

	List<AppConfigEntity> queryList(Map<String, Object> map);
}
