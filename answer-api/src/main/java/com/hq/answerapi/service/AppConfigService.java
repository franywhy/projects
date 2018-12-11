package com.hq.answerapi.service;



import com.hq.answerapi.entity.AppConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * app常量记录表
 * 
 * @author zhaowenwei
 * @date 2018-02-26 14:58:44
 */
public interface AppConfigService {

	String queryValueByKey(Long key);

	AppConfigEntity queryObject(Long id);

	List<AppConfigEntity> queryList(Map<String, Object> map);

	AppConfigEntity queryObjectByKey(Long id);
}
