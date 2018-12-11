package com.hq.courses.service;

import com.hq.courses.pojo.CsConfigKeyEnum;
import com.hq.courses.pojo.CsConfigPOJO;
import com.hq.courses.pojo.query.CsConfigQuery;

import io.renren.utils.PageUtils;

/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface CsConfigService {
	/**
	 * 根据ID查询查询静态变量表
	 * @param ckey	ckey
	 * @param cvalue	cvalue
	 * @return	静态变量表
	 */
	CsConfigPOJO queryObject(CsConfigKeyEnum csConfigKeyEnum , String cvalue);
	String queryNameByValue(String key , String cname);
	
	/**
	 * 知识点难度
	 */
	PageUtils queryListKnowledgeLevel(CsConfigQuery csConfigQuery);
	/**
	 * 知识点题型
	 */
	PageUtils queryListKnowledgeQuestionType(CsConfigQuery csConfigQuery);
	/**
	 * 知识点考点
	 */
	PageUtils queryListKnowledgeKeyPoin(CsConfigQuery csConfigQuery);
}
