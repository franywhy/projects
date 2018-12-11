package com.hq.adaptive.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlKnowledgeFileDao;
import com.hq.adaptive.entity.AdlKnowledgeFileEntity;
import com.hq.adaptive.pojo.AdlKnowledgeFilePOJO;
import com.hq.adaptive.service.AdlKnowledgeFileService;

import io.renren.utils.RRException;

/**
 * 知识点资料档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
@Service("adlKnowledgeFileService")
public class AdlKnowledgeFileServiceImpl implements AdlKnowledgeFileService {
	@Autowired
	private AdlKnowledgeFileDao adlKnowledgeFileDao;

	@Override
	public AdlKnowledgeFilePOJO queryObject(Long KnowledgeId) {
		if(null != KnowledgeId) {
			return this.adlKnowledgeFileDao.queryObject(KnowledgeId);
		}
		return null;
	}

	@Override
	public int save(AdlKnowledgeFilePOJO adlKnowledgeFilePOJO) {
		if(null != adlKnowledgeFilePOJO && StringUtils.isNotBlank(adlKnowledgeFilePOJO.getFileUrl())) {
			verifyForm(adlKnowledgeFilePOJO);
			AdlKnowledgeFileEntity adlKnowledgeFileEntity = pojoToEntity(adlKnowledgeFilePOJO);
			return this.adlKnowledgeFileDao.save(adlKnowledgeFileEntity);
		}
		return 0;
	}

	/**
	 * @param adlKnowledgeFilePOJO
	 * @return
	 */
	private AdlKnowledgeFileEntity pojoToEntity(AdlKnowledgeFilePOJO adlKnowledgeFilePOJO) {
		AdlKnowledgeFileEntity adlKnowledgeFileEntity = new AdlKnowledgeFileEntity();
		adlKnowledgeFileEntity.setFileName(adlKnowledgeFilePOJO.getFileName());
		adlKnowledgeFileEntity.setFileUrl(adlKnowledgeFilePOJO.getFileUrl());
		adlKnowledgeFileEntity.setKnowledgeId(adlKnowledgeFilePOJO.getKnowledgeId());
		adlKnowledgeFileEntity.setFileId(adlKnowledgeFilePOJO.getFileId());
		return adlKnowledgeFileEntity;
	}
	
	/**
	 * 非空校验
	 * @param adlKnowledgeFilePOJO
	 */
	private void verifyForm(AdlKnowledgeFilePOJO adlKnowledgeFilePOJO) {
		if(StringUtils.isBlank(adlKnowledgeFilePOJO.getFileUrl())) {
			throw new RRException("知识点资料下载地址不能为空！");
		}
		if(null== adlKnowledgeFilePOJO.getKnowledgeId()) {
			throw new RRException("知识点资料知识点ID不能为空！");
		}
	}

	@Override
	public int saveOrUpdate(Long KnowledgeId , AdlKnowledgeFilePOJO adlKnowledgeFilePOJO) {
		if(null != adlKnowledgeFilePOJO && StringUtils.isNotBlank(adlKnowledgeFilePOJO.getFileUrl())) {
			if(null == adlKnowledgeFilePOJO.getFileId()) {
				adlKnowledgeFilePOJO.setKnowledgeId(KnowledgeId);
				this.save(adlKnowledgeFilePOJO);
			}else {
				this.update(adlKnowledgeFilePOJO);
			}
		}else {
			return this.delete(KnowledgeId);
		}
		return 0;
	}

	private int update(AdlKnowledgeFilePOJO adlKnowledgeFilePOJO) {
		if(null != adlKnowledgeFilePOJO) {
			this.verifyForm(adlKnowledgeFilePOJO);
			AdlKnowledgeFileEntity adlKnowledgeFileEntity = pojoToEntity(adlKnowledgeFilePOJO);
			return this.adlKnowledgeFileDao.update(adlKnowledgeFileEntity);
		}
		return 0;
	}
	@Override
	public int delete(Long KnowledgeId) {
		if(null != KnowledgeId) {
			return this.adlKnowledgeFileDao.delete(KnowledgeId);
		}
		return 0;
	}
}
