package com.hq.courses.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.hq.courses.util.CodeSplitUtils;
import com.hq.courses.util.CodeValidateUtils;
import io.renren.common.doc.ParamKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.courses.dao.CsSectionDao;
import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.entity.CsSectionEntity;
import com.hq.courses.pojo.CsSectionPOJO;
import com.hq.courses.pojo.query.CsKnowledgeQuery;
import com.hq.courses.pojo.query.CsSectionQuery;
import com.hq.courses.service.CsChapterService;
import com.hq.courses.service.CsKnowledgeService;
import com.hq.courses.service.CsSectionService;
import com.hq.courses.util.CsCodeUtils;

import io.renren.rest.persistent.KGS;
import io.renren.utils.RRException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 节档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 16:45:53
 */
@Service("csSectionService")
public class CsSectionServiceImpl implements CsSectionService {
	@Autowired
	private CsChapterService csChapterService;
	@Autowired
	private CsKnowledgeService csKnowledgeService;
	@Autowired
	private CsSectionDao csSectionDao;
	
	@Resource
	private KGS csSectionNoKGS;
	
	private static final int MAX_SECTIONNO_LENGTH = 5;
	
	/**
	 * 根据ID查询查询节档案
	 * @param sectionId	ID
	 * @return	节档案
	 */
	@Override
	public CsSectionPOJO queryObject(Long sectionId){
		return csSectionDao.queryObject(sectionId);
	}
	
	/**
	 * 查询节档案列表
	 * @return	节档案列表
	 */
	@Override
	public List<CsSectionEntity> queryList(CsSectionQuery csSectionQuery){
		return csSectionDao.queryList(csSectionQuery);
	}
	
	/**
	 * 查询节档案数量
	 * @return	节档案数量
	 */
	@Override
	public int queryTotal(CsSectionQuery csSectionQuery){
		return csSectionDao.queryTotal(csSectionQuery);
	}
	
	/**
	 * 新增节档案
	 */
	@Override
	public int save(CsSectionEntity csSection){
		//TODO 重名校验
		if(null == csSection) {
			throw new RRException("csSection不能为空!");
		}
		if(null == csSection.getChapterId() || 0 == csSection.getChapterId()) {
			throw new RRException("章不能为空!");
		}
		//课程ID
		CsChapterEntity csChapterEntity = this.csChapterService.queryObject(csSection.getChapterId());
		if(null == csChapterEntity) {
			throw new RRException("获取章失败！");
		}
		csSection.setCourseId(csChapterEntity.getCourseId());
		//节编号
//		if(StringUtils.isBlank(csSection.getSectionNo())) {
//			String sectionCode = getSectionCode(csSection.getChapterId());
//			csSection.setSectionNo(sectionCode);
//		}
		checkSectionNo(csSection.getSectionId(),csSection.getSectionNo());
		csSection.setDr(0);
		return csSectionDao.save(csSection);
	}
	
	/**
	 * 更新节档案
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses ,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public int update(CsSectionEntity csSection){
		if(null == csSection) {
			throw new RRException("csSection不能为空!");
		}
		if(null == csSection.getChapterId() || 0 == csSection.getChapterId()) {
			throw new RRException("章不能为空!");
		}
		//节编号
//		if(StringUtils.isBlank(csSection.getSectionNo())) {
//			throw new RRException("编码不能为空!");
//		}
		checkSectionNo(csSection.getSectionId(),csSection.getSectionNo());
		int update = csSectionDao.update(csSection);
		//更新子集code
		this.csKnowledgeService.updateCodeByParentId(csSection);
		return update;
	}
	
	/**
	 * 按照ID删除
	 * @param sectionId	ID
	 */
	@Override
	public int delete(Long sectionId) {
		CsKnowledgeQuery csKnowledgeQuery = new CsKnowledgeQuery();
		csKnowledgeQuery.setSectionId(sectionId);
		int total = this.csKnowledgeService.queryTotal(csKnowledgeQuery);
		if(total > 0) {
			throw new RRException("删除失败，节下还有知识点!");
		}
		return csSectionDao.delete(sectionId);
	}

	/**
	 * 查询节书数量
	 * @param chapterId	章ID
	 * @return			节数量
	 */
	@Override
	public int queryCountByParentId(Long chapterId) {
		return this.csSectionDao.queryCountByParentId(chapterId);
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
		CsChapterEntity csChapterEntity = this.csChapterService.queryObject(chapterId);
		if(null == csChapterEntity) {
			throw new RRException("章获取错误!");
		}
		String code = CsCodeUtils.getSectionCode(csChapterEntity.getChapterNo(), this.csSectionNoKGS.nextId()+"");
		return code;
	}

	@Override
	public int updateSectionParent(CsSectionEntity csSection) {
		if(null != csSection) {
			if(csSection.getChapterId() != null && csSection.getSectionId() != null) {
				CsSectionPOJO csSectionPOJO = csSectionDao.queryObject(csSection.getSectionId());
				String sectionNo = CodeSplitUtils.sectionFullCode(csSectionPOJO.getSectionNo(), this.csChapterService.queryObject(csSection.getChapterId()).getChapterNo());
				if(StringUtils.isNotBlank(sectionNo)){
					csSection.setSectionNo(sectionNo);
				}
				return update(csSection);
			}
		}
		return 0;
	}

	@Override
	public CsSectionEntity saveOrQueryBySectionName(CsSectionEntity csSection) {
		//TODO 重名校验
		if(null != csSection) {
			CsSectionEntity csSectionEntity = this.csSectionDao.queryObjectByName(csSection.getCourseId(), csSection.getSectionName());
			if(null != csSectionEntity) {
				csSection = csSectionEntity;
			}else {
				this.save(csSection);
			}
		}
		return csSection;
	}

	@Override
	public void checkSectionNo(Long sectionId, String sectionNo) {
		if(StringUtils.isBlank(sectionNo)) {
			throw new RRException("节编号不能为空！");
		}
		CodeValidateUtils.sectionCodeValidate(sectionNo);
		CsSectionEntity csSectionEntity = csSectionDao.queryObjectBySectionNo(sectionId, sectionNo);
		if(csSectionEntity != null) {
			throw new RRException("节编号已经存在!");
		}
	}

	/**
	 * 更新code，根据上一级code编码
	 *
	 * @param csChapterEntity
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses ,readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public void updateCodeByParentId(CsChapterEntity csChapterEntity) {
		if(csChapterEntity != null){
			if(null != csChapterEntity){
				CsSectionQuery csSectionQuery = new CsSectionQuery();
				csSectionQuery.setChapterId(csChapterEntity.getChapterId());
				//节列表
				List<CsSectionEntity> csSectionEntities = this.queryList(csSectionQuery);
				if(CollectionUtils.isNotEmpty(csSectionEntities)){
					String chapterNo = csChapterEntity.getChapterNo();
					for (int i = 0; i < csSectionEntities.size(); i++) {
						CsSectionEntity csSectionEntity = csSectionEntities.get(i);
						//原code
						String oldSectionNo = csSectionEntity.getSectionNo();
						//新code
						String newSectionNo = CodeSplitUtils.sectionFullCode(oldSectionNo, chapterNo);
						if(StringUtils.isNotBlank(newSectionNo) && !newSectionNo.equals(oldSectionNo)){
							csSectionEntity.setSectionNo(newSectionNo);
							this.update(csSectionEntity);
						}
					}
				}
			}
		}
	}

	@Override
	public CsSectionEntity queryObjectBySectionNo(String sectionNo) {
		return csSectionDao.queryObjectBySectionNo(null, sectionNo);
	}
}
