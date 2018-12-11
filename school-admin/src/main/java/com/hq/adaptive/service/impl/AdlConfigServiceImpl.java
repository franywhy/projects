package com.hq.adaptive.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlConfigDao;
import com.hq.adaptive.pojo.AdlConfigKeyEnum;
import com.hq.adaptive.pojo.AdlConfigPOJO;
import com.hq.adaptive.pojo.query.AdlConfigQuery;
import com.hq.adaptive.service.AdlConfigService;

import io.renren.utils.PageUtils;

/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
@Service("adlConfigService")
public class AdlConfigServiceImpl implements AdlConfigService {
	@Autowired
	private AdlConfigDao adlConfigDao;
	
	/**
	 * 根据ID查询查询静态变量表
	 * @param ckey	ckey
	 * @param cvalue	cvalue
	 * @return	静态变量表
	 */
	public AdlConfigPOJO queryObject(AdlConfigKeyEnum adlConfigKeyEnum , String cvalue) {
		return this.adlConfigDao.queryObject(adlConfigKeyEnum.getCkey(), cvalue);
	}

	@Override
	public PageUtils queryListKnowledgeLevel(AdlConfigQuery adlConfigQuery) {
		if(null != adlConfigQuery) {
			adlConfigQuery.setCkey(AdlConfigKeyEnum.KNOWLEDGE_LEVEL.getCkey());
			return this.queryPage(adlConfigQuery);
		}
		return null;
	}

	@Override
	public PageUtils queryListKnowledgeQuestionType(AdlConfigQuery adlConfigQuery) {
		if(null != adlConfigQuery) {
			adlConfigQuery.setCkey(AdlConfigKeyEnum.ADP_KNOWLEDGE_QUESTION_TYPE.getCkey());
			return this.queryPage(adlConfigQuery);
		}
		return null;
	}

	private List<AdlConfigPOJO> queryList(AdlConfigQuery adlConfigQuery){
		return this.adlConfigDao.queryList(adlConfigQuery);
	}
	
	private int queryCount(AdlConfigQuery adlConfigQuery) {
		return this.adlConfigDao.queryTotal(adlConfigQuery);
	}
	
	private PageUtils queryPage(AdlConfigQuery adlConfigQuery){
		PageUtils page = null;
		if(null != adlConfigQuery && StringUtils.isNotBlank(adlConfigQuery.getCkey())) {
			List<AdlConfigPOJO> list = this.queryList(adlConfigQuery);
			int count = this.queryCount(adlConfigQuery);
			page = new PageUtils(list, count, adlConfigQuery.getLimit(), adlConfigQuery.get_page());
		}
		return page;
	}

	@Override
	public PageUtils queryListKnowledgeKeyPoin(AdlConfigQuery adlConfigQuery) {
		if(null != adlConfigQuery) {
			adlConfigQuery.setCkey(AdlConfigKeyEnum.ADP_KNOWLEDGE_KEY_POINT.getCkey());
			return this.queryPage(adlConfigQuery);
		}
		return null;
	}

	@Override
	public String queryNameByValue(String key, String cname) {
		return this.adlConfigDao.queryNameByValue(key, cname);
	}
	
	
}
