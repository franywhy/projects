package com.hq.adaptive.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlKnowledgeVideoDao;
import com.hq.adaptive.entity.AdlKnowledgeVideoEntity;
import com.hq.adaptive.pojo.AdlKnowledgeVideoPOJO;
import com.hq.adaptive.service.AdlKnowledgeVideoService;

import io.renren.utils.DateUtils;
import io.renren.utils.RRException;

/**
 * 知识点视频档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
@Service("adlKnowledgeVideoService")
public class AdlKnowledgeVideoServiceImpl implements AdlKnowledgeVideoService {
	@Autowired
	private AdlKnowledgeVideoDao adlKnowledgeVideoDao;

	@Override
	public AdlKnowledgeVideoPOJO queryObject(Long KnowledgeId) {
		return this.adlKnowledgeVideoDao.queryObject(KnowledgeId);
	}

	@Override
	public int save(AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO) {
		if(null != adlKnowledgeVideoPOJO && StringUtils.isNotBlank(adlKnowledgeVideoPOJO.getPolyvVid())) {
			//校验
			verifyForm(adlKnowledgeVideoPOJO);
			//POJO==>Entity
			AdlKnowledgeVideoEntity adlKnowledgeVideoEntity = pojoToEntity(adlKnowledgeVideoPOJO);
			return this.adlKnowledgeVideoDao.save(adlKnowledgeVideoEntity);
		}
		return 0;
	}
	
	@Override
	public int saveOrUpdate(Long KnowledgeId, AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO) {
		if(null != adlKnowledgeVideoPOJO && StringUtils.isNotBlank(adlKnowledgeVideoPOJO.getPolyvVid())) {
			if(null != adlKnowledgeVideoPOJO.getVideoId()) {
				this.update(KnowledgeId, adlKnowledgeVideoPOJO);
			}else {
				adlKnowledgeVideoPOJO.setKnowledgeId(KnowledgeId);
				this.save(adlKnowledgeVideoPOJO);
			}
		}else {
			this.adlKnowledgeVideoDao.delete(KnowledgeId);
		}
		return 0;
	}
	
	private int update(Long KnowledgeId, AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO) {
		if(null != adlKnowledgeVideoPOJO) {
			//校验
			verifyForm(adlKnowledgeVideoPOJO);
			//POJO==>Entity
			AdlKnowledgeVideoEntity adlKnowledgeVideoEntity = pojoToEntity(adlKnowledgeVideoPOJO);
			return this.adlKnowledgeVideoDao.update(adlKnowledgeVideoEntity);
		}
		return 0;
	}
	/**
	 * POJO==>Entity
	 * @param adlKnowledgeVideoPOJO
	 * @return
	 */
	private AdlKnowledgeVideoEntity pojoToEntity(AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO) {
		AdlKnowledgeVideoEntity adlKnowledgeVideoEntity = new AdlKnowledgeVideoEntity();
		adlKnowledgeVideoEntity.setVideoId(adlKnowledgeVideoPOJO.getVideoId());
		adlKnowledgeVideoEntity.setKnowledgeId(adlKnowledgeVideoPOJO.getKnowledgeId());
		adlKnowledgeVideoEntity.setPolyvDuration(adlKnowledgeVideoPOJO.getPolyvDuration());
		adlKnowledgeVideoEntity.setPolyvDurationS(DateUtils.videDuration(adlKnowledgeVideoPOJO.getPolyvDuration()));
		adlKnowledgeVideoEntity.setPolyvName(adlKnowledgeVideoPOJO.getPolyvName());
		adlKnowledgeVideoEntity.setPolyvVid(adlKnowledgeVideoPOJO.getPolyvVid());
		adlKnowledgeVideoEntity.setScreenShot(adlKnowledgeVideoPOJO.getScreenShot());
		return adlKnowledgeVideoEntity;
	}

	/**
	 * 非空校验
	 * @param adlKnowledgeVideoPOJO
	 */
	private void verifyForm(AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO) {
		if(adlKnowledgeVideoPOJO.getKnowledgeId() == null) {
			throw new RRException("视频所属知识点(KnowledgeId)不能为空！");
		}
		if(StringUtils.isBlank(adlKnowledgeVideoPOJO.getPolyvVid())) {
			throw new RRException("视频ID不能为空！");
		}
	}

	/**
	 * 查询知识点视频表是否存在该记录
	 * @param knowledgeId	知识点id
	 * @return	
	 */
	@Override
	public boolean queryKnowledgeIdIsNotExist(Long knowledgeId) {
		if(adlKnowledgeVideoDao.queryKnowledgeId(knowledgeId) == null) {
			return true;
		}
		return false;
	}

}
