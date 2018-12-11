package com.hq.courses.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.courses.dao.CsKnowledgeContainDao;
import com.hq.courses.entity.CsKnowledgeContainEntity;
import com.hq.courses.pojo.CsKnowledgeContainPOJO;
import com.hq.courses.service.CsKnowledgeContainService;

import io.renren.utils.RRException;

/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
@Service("csKnowledgeContainService")
public class CsKnowledgeContainServiceImpl implements CsKnowledgeContainService {
	@Autowired
	private CsKnowledgeContainDao csKnowledgeContainDao;

	@Override
	public List<CsKnowledgeContainPOJO> queryListBySelfId(Long selfId) {
		return this.csKnowledgeContainDao.queryListBySelfId(selfId);
	}

	@Override
	public void saveList(Long knowledgeId, List<CsKnowledgeContainPOJO> childList) {
		if(null == knowledgeId) {
			throw new RRException("保存知识点包含题型时，知识点ID不能为空！");
		}
		this.delete(knowledgeId);
		//新增
		if(null != childList && !childList.isEmpty()) {
			for (CsKnowledgeContainPOJO csKnowledgeContainPOJO : childList) {
				CsKnowledgeContainEntity csKnowledgeContainEntity = pojoToEntity(knowledgeId , csKnowledgeContainPOJO);
				this.csKnowledgeContainDao.save(csKnowledgeContainEntity);
			}
		}
	}

	private CsKnowledgeContainEntity pojoToEntity(Long knowledgeId , CsKnowledgeContainPOJO csKnowledgeContainPOJO) {
		CsKnowledgeContainEntity csKnowledgeContainEntity = new CsKnowledgeContainEntity();
		csKnowledgeContainEntity.setSelfId(knowledgeId);
		csKnowledgeContainEntity.setChildId(csKnowledgeContainPOJO.getChildId());
		return csKnowledgeContainEntity;
	}
	/**
	 * 删除
	 * @param knowledgeId
	 * @return
	 */
	private int delete(Long knowledgeId) {
		if(null != knowledgeId) {
			return this.csKnowledgeContainDao.delete(knowledgeId);
		}
		return 0;
	}

	@Override
	public List<CsKnowledgeContainPOJO> queryListByChildId(Long childId) {
		return this.csKnowledgeContainDao.queryListByChildId(childId);
	}
	
}
