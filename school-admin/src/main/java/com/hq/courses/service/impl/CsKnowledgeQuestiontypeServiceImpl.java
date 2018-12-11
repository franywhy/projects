package com.hq.courses.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.courses.dao.CsKnowledgeQuestiontypeDao;
import com.hq.courses.entity.CsKnowledgeQuestiontypeEntity;
import com.hq.courses.pojo.CsConfigKeyEnum;
import com.hq.courses.pojo.CsKnowledgeQuestiontypePOJO;
import com.hq.courses.service.CsKnowledgeQuestiontypeService;

import io.renren.utils.RRException;

/**
 * 知识点题型
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 14:46:48
 */
@Service("csKnowledgeQuestiontypeService")
public class CsKnowledgeQuestiontypeServiceImpl implements CsKnowledgeQuestiontypeService {
	@Autowired
	private CsKnowledgeQuestiontypeDao csKnowledgeQuestiontypeDao;

	@Override
	public List<CsKnowledgeQuestiontypePOJO> queryList(Long knowledgeId) {
		if(null != knowledgeId && knowledgeId > 0) 
			return this.csKnowledgeQuestiontypeDao.queryList(knowledgeId , CsConfigKeyEnum.ADP_KNOWLEDGE_QUESTION_TYPE.getCkey());
		return null;
	}

	@Override
	public void saveList(Long knowledgeId, List<CsKnowledgeQuestiontypePOJO> questiontypeList) {
		if(null == knowledgeId) {
			throw new RRException("保存知识点包含题型时，知识点ID不能为空！");
		}
		//删除包含题型
		this.delete(knowledgeId);
		//新增
		if(null != questiontypeList && !questiontypeList.isEmpty()) {
			for (CsKnowledgeQuestiontypePOJO csKnowledgeQuestiontypePOJO : questiontypeList) {
				CsKnowledgeQuestiontypeEntity csKnowledgeQuestiontypeEntity = pojoToEntity(knowledgeId , csKnowledgeQuestiontypePOJO);
				//保存
				this.save(csKnowledgeQuestiontypeEntity);
			}
		}
	}

	private CsKnowledgeQuestiontypeEntity pojoToEntity(Long knowledgeId , CsKnowledgeQuestiontypePOJO csKnowledgeQuestiontypePOJO) {
		//对象转换
		CsKnowledgeQuestiontypeEntity csKnowledgeQuestiontypeEntity = new CsKnowledgeQuestiontypeEntity();
		//赋值
		csKnowledgeQuestiontypeEntity.setKnowledgeId(knowledgeId);
		csKnowledgeQuestiontypeEntity.setQuestiontypeId(csKnowledgeQuestiontypePOJO.getQuestiontypeId());
		return csKnowledgeQuestiontypeEntity;
	}
	

	private int delete(Long knowledgeId) {
		if(null != knowledgeId && knowledgeId > 0) 
			return this.csKnowledgeQuestiontypeDao.delete(knowledgeId);
		return 0;
	}
	
	private int save(CsKnowledgeQuestiontypeEntity csKnowledgeQuestiontypeEntity) {
		if(null != csKnowledgeQuestiontypeEntity) {
			return this.csKnowledgeQuestiontypeDao.save(csKnowledgeQuestiontypeEntity);
		}
		return 0;
	}
}
