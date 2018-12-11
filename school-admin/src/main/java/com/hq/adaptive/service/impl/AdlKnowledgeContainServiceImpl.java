package com.hq.adaptive.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlKnowledgeContainDao;
import com.hq.adaptive.entity.AdlKnowledgeContainEntity;
import com.hq.adaptive.pojo.AdlKnowledgeContainPOJO;
import com.hq.adaptive.service.AdlKnowledgeContainService;

import io.renren.utils.RRException;

/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
@Service("adlKnowledgeContainService")
public class AdlKnowledgeContainServiceImpl implements AdlKnowledgeContainService {
	@Autowired
	private AdlKnowledgeContainDao adlKnowledgeContainDao;

	@Override
	public List<AdlKnowledgeContainPOJO> queryListBySelfId(Long selfId) {
		return this.adlKnowledgeContainDao.queryListBySelfId(selfId);
	}

	@Override
	public void saveList(Long knowledgeId, List<AdlKnowledgeContainPOJO> childList) {
		if(null == knowledgeId) {
			throw new RRException("保存知识点包含题型时，知识点ID不能为空！");
		}
		this.delete(knowledgeId);
		//新增
		if(null != childList && !childList.isEmpty()) {
			for (AdlKnowledgeContainPOJO adlKnowledgeContainPOJO : childList) {
				AdlKnowledgeContainEntity adlKnowledgeContainEntity = pojoToEntity(knowledgeId , adlKnowledgeContainPOJO);
				this.adlKnowledgeContainDao.save(adlKnowledgeContainEntity);
			}
		}
	}

	/**
	 * 本级知识点下包含子集知识点数量
	 *
	 * @param selfId 本级知识点PK
	 * @return
	 */
	@Override
	public int queryTotalBySelfId(Long selfId) {
		return this.adlKnowledgeContainDao.queryTotalBySelfId(selfId);
	}

	/**
	 * 知识点被包含的数量
	 *
	 * @param childId 知识点PK
	 * @return
	 */
	@Override
	public int queryTotalByChildId(Long childId) {
		return this.adlKnowledgeContainDao.queryTotalByChildId(childId);
	}

	private AdlKnowledgeContainEntity pojoToEntity(Long knowledgeId , AdlKnowledgeContainPOJO adlKnowledgeContainPOJO) {
		AdlKnowledgeContainEntity adlKnowledgeContainEntity = new AdlKnowledgeContainEntity();
		adlKnowledgeContainEntity.setSelfId(knowledgeId);
		adlKnowledgeContainEntity.setChildId(adlKnowledgeContainPOJO.getChildId());
		return adlKnowledgeContainEntity;
	}
	/**
	 * 删除
	 * @param knowledgeId
	 * @return
	 */
	private int delete(Long knowledgeId) {
		if(null != knowledgeId) {
			return this.adlKnowledgeContainDao.delete(knowledgeId);
		}
		return 0;
	}

	@Override
	public List<AdlKnowledgeContainPOJO> queryListByChildId(Long childId) {
		return this.adlKnowledgeContainDao.queryListByChildId(childId);
	}
	
}
