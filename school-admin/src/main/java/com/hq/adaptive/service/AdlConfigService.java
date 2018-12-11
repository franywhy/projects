package com.hq.adaptive.service;

import com.hq.adaptive.pojo.AdlConfigKeyEnum;
import com.hq.adaptive.pojo.AdlConfigPOJO;
import com.hq.adaptive.pojo.query.AdlConfigQuery;

import io.renren.utils.PageUtils;

/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface AdlConfigService {
	/**
	 * 根据ID查询查询静态变量表
	 * @param ckey	ckey
	 * @param cvalue	cvalue
	 * @return	静态变量表
	 */
	AdlConfigPOJO queryObject(AdlConfigKeyEnum adlConfigKeyEnum , String cvalue);
	String queryNameByValue(String key , String cname);
	
	/**
	 * 知识点难度
	 */
	PageUtils queryListKnowledgeLevel(AdlConfigQuery adlConfigQuery);
	/**
	 * 知识点题型
	 */
	PageUtils queryListKnowledgeQuestionType(AdlConfigQuery adlConfigQuery);
	/**
	 * 知识点考点
	 */
	PageUtils queryListKnowledgeKeyPoin(AdlConfigQuery adlConfigQuery);
}
