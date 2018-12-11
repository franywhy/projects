package com.hq.courses.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.courses.dao.CsConfigDao;
import com.hq.courses.pojo.CsConfigKeyEnum;
import com.hq.courses.pojo.CsConfigPOJO;
import com.hq.courses.pojo.query.CsConfigQuery;
import com.hq.courses.service.CsConfigService;

import io.renren.utils.PageUtils;

/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
@Service("csConfigService")
public class CsConfigServiceImpl implements CsConfigService {
	@Autowired
	private CsConfigDao csConfigDao;
	
	/**
	 * 根据ID查询查询静态变量表
	 * @param ckey	ckey
	 * @param cvalue	cvalue
	 * @return	静态变量表
	 */
	public CsConfigPOJO queryObject(CsConfigKeyEnum csConfigKeyEnum , String cvalue) {
		return this.csConfigDao.queryObject(csConfigKeyEnum.getCkey(), cvalue);
	}

	@Override
	public PageUtils queryListKnowledgeLevel(CsConfigQuery csConfigQuery) {
		if(null != csConfigQuery) {
			csConfigQuery.setCkey(CsConfigKeyEnum.KNOWLEDGE_LEVEL.getCkey());
			return this.queryPage(csConfigQuery);
		}
		return null;
	}

	@Override
	public PageUtils queryListKnowledgeQuestionType(CsConfigQuery csConfigQuery) {
		if(null != csConfigQuery) {
			csConfigQuery.setCkey(CsConfigKeyEnum.ADP_KNOWLEDGE_QUESTION_TYPE.getCkey());
			return this.queryPage(csConfigQuery);
		}
		return null;
	}

	private List<CsConfigPOJO> queryList(CsConfigQuery csConfigQuery){
		return this.csConfigDao.queryList(csConfigQuery);
	}
	
	private int queryCount(CsConfigQuery csConfigQuery) {
		return this.csConfigDao.queryTotal(csConfigQuery);
	}
	
	private PageUtils queryPage(CsConfigQuery csConfigQuery){
		PageUtils page = null;
		if(null != csConfigQuery && StringUtils.isNotBlank(csConfigQuery.getCkey())) {
			List<CsConfigPOJO> list = this.queryList(csConfigQuery);
			int count = this.queryCount(csConfigQuery);
			page = new PageUtils(list, count, csConfigQuery.getLimit(), csConfigQuery.get_page());
		}
		return page;
	}

	@Override
	public PageUtils queryListKnowledgeKeyPoin(CsConfigQuery csConfigQuery) {
		if(null != csConfigQuery) {
			csConfigQuery.setCkey(CsConfigKeyEnum.ADP_KNOWLEDGE_KEY_POINT.getCkey());
			return this.queryPage(csConfigQuery);
		}
		return null;
	}

	@Override
	public String queryNameByValue(String key, String cname) {
		return this.csConfigDao.queryNameByValue(key, cname);
	}
	
	
}
