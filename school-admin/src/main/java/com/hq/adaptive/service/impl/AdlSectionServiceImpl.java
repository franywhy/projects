package com.hq.adaptive.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.hq.adaptive.service.AdlSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlSectionDao;
import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.entity.AdlSectionEntity;
import com.hq.adaptive.pojo.AdlSectionPOJO;
import com.hq.adaptive.pojo.query.AdlSectionQuery;
import com.hq.adaptive.service.AdlChapterService;
import com.hq.adaptive.service.AdlKnowledgeService;
import com.hq.adaptive.util.AdlCodeUtils;

import io.renren.rest.persistent.KGS;
import io.renren.utils.RRException;


/**
 * 节档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 16:45:53
 */
@Service("adlSectionService")
public class AdlSectionServiceImpl implements AdlSectionService {
	@Autowired
	private AdlChapterService adlChapterService;
	@Autowired
	private AdlKnowledgeService adlKnowledgeService;
	@Autowired
	private AdlSectionDao adlSectionDao;
	
	@Resource
	private KGS adlSectionNoKGS;
	
	
	/**
	 * 根据ID查询查询节档案
	 * @param sectionId	ID
	 * @return	节档案
	 */
	@Override
	public AdlSectionPOJO queryObject(Long sectionId){
		return adlSectionDao.queryObject(sectionId);
	}
	
	/**
	 * 查询节档案列表
	 * @param limit		返回记录行的最大数目
	 * @return	节档案列表
	 */
	@Override
	public List<AdlSectionEntity> queryList(AdlSectionQuery adlSectionQuery){
		return adlSectionDao.queryList(adlSectionQuery);
	}
	
	/**
	 * 查询节档案数量
	 * @return	节档案数量
	 */
	@Override
	public int queryTotal(AdlSectionQuery adlSectionQuery){
		return adlSectionDao.queryTotal(adlSectionQuery);
	}
	

	/**
	 * 查询节书数量
	 * @param chapterId	章ID
	 * @return			节数量
	 */
	@Override
	public int queryCountByParentId(Long chapterId) {
		return this.adlSectionDao.queryCountByParentId(chapterId);
	}

	/**
	 * 获取节编码
	 * @param chapterId	章ID
	 * @return			节编码
	 */
	@Override
	public String getSectionCode(Long chapterId){
		if(null == chapterId || chapterId <= 0) {
			throw new RRException("章ID获取错误!");
		}
		AdlChapterEntity adlChapterEntity = this.adlChapterService.queryObject(chapterId);
		if(null == adlChapterEntity) {
			throw new RRException("章获取错误!");
		}
		String code = AdlCodeUtils.getSectionCode(adlChapterEntity.getChapterNo(), this.adlSectionNoKGS.nextId()+"");
		return code;
	}


	@Override
	public AdlSectionEntity saveOrQueryBySectionName(AdlSectionEntity adlSection) {
		//TODO 重名校验
		if(null != adlSection) {
			AdlSectionEntity adlSectionEntity = this.adlSectionDao.queryObjectByName(adlSection.getCourseId(), adlSection.getSectionName());
			if(null != adlSectionEntity) {
				adlSection = adlSectionEntity;
			}else {
//				this.save(adlSection);
			}
		}
		return adlSection;
	}
}
