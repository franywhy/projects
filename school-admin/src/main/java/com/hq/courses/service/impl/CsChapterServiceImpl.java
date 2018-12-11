package com.hq.courses.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.util.CodeSplitUtils;
import com.hq.courses.util.CodeValidateUtils;
import io.renren.common.doc.ParamKey;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.courses.dao.CsChapterDao;
import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.pojo.CsCoursePOJO;
import com.hq.courses.pojo.query.CsChapterQuery;
import com.hq.courses.service.CsChapterService;
import com.hq.courses.service.CsCourseService;
import com.hq.courses.service.CsSectionService;
import com.hq.courses.util.CsCodeUtils;

import io.renren.rest.persistent.KGS;
import io.renren.utils.RRException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
@Service("csChapterService")
public class CsChapterServiceImpl implements CsChapterService {
	@Autowired
	private CsChapterDao csChapterDao;
	@Autowired
	private CsCourseService csCourseService;
	@Autowired
	private CsSectionService csSectionService;
	
	private static final int MAX_CHAPTERNO_LENGTH = 5;
	
	@Resource
	private KGS csChapterNoKGS;

	/**
	 * 根据ID查询查询章
	 * @param chapterId	自增id
	 * @return	章
	 */
	@Override
	public CsChapterEntity queryObject(Long chapterId){
		return csChapterDao.queryObject(chapterId);
	}
	
	/**
	 * 查询章列表
	 * @param csChapterQuery
	 * @return	章列表
	 */
	@Override
	public List<CsChapterEntity> queryList(CsChapterQuery csChapterQuery){
		return csChapterDao.queryList(csChapterQuery);
	}
	
	/**
	 * 查询章数量
	 * @return	章数量
	 */
	@Override
	public int queryTotal(Long courseId){
		return csChapterDao.queryTotal(courseId);
	}
	
	/**
	 * 新增章
	 */
	@Override
	public int save(CsChapterEntity csChapter){
		//TODO 重名校验
		if(null == csChapter) {
			throw new RRException("csChapter不能为空!");
		}
		if(StringUtils.isBlank(csChapter.getChapterName())) {
			throw new RRException("章名称不能为空!");
		}
		if(null == csChapter.getCourseId()) {
			throw new RRException("章所属课程不能为空!");
		}
		checkChapterNo(null,csChapter.getChapterNo());
		csChapter.setDr(0);
		return csChapterDao.save(csChapter);
	}
	
	/**
	 * 更新章
	 */
    @Transactional(value = ParamKey.Transactional.hq_courses , propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public int update(CsChapterEntity csChapter){
		//TODO 重名校验
		if(null == csChapter) {
			throw new RRException("csChapter不能为空!");
		}
		if(null ==csChapter.getChapterId()) {
			throw new RRException("章ID不能为空!");
		}
		if(StringUtils.isBlank(csChapter.getChapterName())) {
			throw new RRException("章名称不能为空!");
		}
		if(null == csChapter.getCourseId()) {
			throw new RRException("章所属课程不能为空!");
		}
//		if(StringUtils.isBlank(csChapter.getChapterNo())) {//编号
//			throw new RRException("章编码不能为空!");
//		}
		checkChapterNo(csChapter.getChapterId(),csChapter.getChapterNo());
		int update = csChapterDao.update(csChapter);
		//更新子集code
		this.csSectionService.updateCodeByParentId(csChapter);
		return update;
	}
	
	/**
	 * 按照ID删除
	 * @param chapterId	自增id
	 */
	@Override
	public int delete(Long chapterId) {
		int sectionCount = this.csSectionService.queryCountByParentId(chapterId);
		if(0 < sectionCount) {
			throw new RRException("章下还有节，删除失败!");
		}
		return csChapterDao.delete(chapterId);
	}

	@Override
	public String getChapterNo(Long courseId){
		if(null == courseId || courseId <= 0) {
			throw new RRException("课程ID获取错误!");
		}
		CsCoursePOJO csCoursePOJO = this.csCourseService.queryObject(courseId);
		if(null == csCoursePOJO) {
			throw new RRException("课程获取错误!");
		}
		String code = CsCodeUtils.getChapterCode(csCoursePOJO.getCourseNo(), this.csChapterNoKGS.nextId()+"");
		return code;
	}

	@Override
	public CsChapterEntity saveOrQueryByChapterName(CsChapterEntity csChapter) {
		if(csChapter != null && csChapter.getCourseId() != null && StringUtils.isNotBlank(csChapter.getChapterName())) {
			CsChapterEntity entity = this.csChapterDao.queryObjectByName(csChapter.getChapterName(), csChapter.getCourseId());
			if(null != entity) {
				csChapter = entity;
			}else {
				this.save(csChapter);
			}
		}
		return csChapter;
	}

	@Override
	public void checkChapterNo(Long chapterId,String chapterNo) {
		if(StringUtils.isBlank(chapterNo)) {
			throw new RRException("章编号不能为空！");
		}
		CodeValidateUtils.chapterCodeValidate(chapterNo);
		CsChapterEntity entity = csChapterDao.queryObjectByChapterNo(chapterId,chapterNo);
		if(entity != null) {
			throw new RRException("章编号已经存在!");
		}
	}

	@Override
	public CsChapterEntity queryObjectByChapterNo(String chapterNo) {
		CsChapterEntity entity = csChapterDao.queryObjectByChapterNo(null,chapterNo);
		return entity;
	}

	/**
	 * 更新章、节、知识点code，根据上一级code编码
	 *
	 * @param csCourse 课程PK
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses ,readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public void updateCodeByParentId(CsCourseEntity csCourse) {
		if(csCourse != null){
			//课程对象
			if(null != csCourse && StringUtils.isNotBlank(csCourse.getCourseNo())){
				//章查询条件
				CsChapterQuery csChapterQuery = new CsChapterQuery();
				//按照课程PK查询
				csChapterQuery.setCourseId(csCourse.getCourseId());
				//课程包含章列表
				List<CsChapterEntity> csChapterEntities = this.queryList(csChapterQuery);
				//课程编码
				String courseNo = csCourse.getCourseNo();
				//迭代章列表
				if(CollectionUtils.isNotEmpty(csChapterEntities)){
					for (int i = 0 ; i < csChapterEntities.size(); i++) {
						//章对象
						CsChapterEntity csChapterEntity = csChapterEntities.get(i);
						//原code
						String oldChapterNo = csChapterEntity.getChapterNo();
						//新code
						String newChapterNo = CodeSplitUtils.chapterFullCode(oldChapterNo, courseNo);
						if(StringUtils.isNotBlank(newChapterNo) && !newChapterNo.equals(oldChapterNo)){
							csChapterEntity.setChapterNo(newChapterNo);
							this.update(csChapterEntity);
//							this.csSectionService.updateCodeByParentId(csChapterEntity.getChapterId());
						}
					}
				}
			}
		}
	}
}
