package com.hq.adaptive.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.hq.adaptive.service.AdlSectionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.adaptive.dao.AdlChapterDao;
import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.entity.AdlCourseEntity;
import com.hq.adaptive.pojo.query.AdlChapterQuery;
import com.hq.adaptive.service.AdlChapterService;
import com.hq.adaptive.service.AdlCourseService;
import com.hq.adaptive.util.AdlCodeUtils;

import io.renren.rest.persistent.KGS;
import io.renren.utils.RRException;


/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
@Service("adlChapterService")
public class AdlChapterServiceImpl implements AdlChapterService {
	@Autowired
	private AdlChapterDao adlChapterDao;
	@Autowired
	private AdlCourseService adlCourseService;
	@Autowired
	private AdlSectionService adlSectionService;
	
	
	
	@Resource
	private KGS adlChapterNoKGS;

//	@Resource
//	private static KGS adlKnowledgeNoKGS;
	/**
	 * 根据ID查询查询章
	 * @param chapterId	自增id
	 * @return	章
	 */
	@Override
	public AdlChapterEntity queryObject(Long chapterId){
		return adlChapterDao.queryObject(chapterId);
	}
	
	/**
	 * 查询章列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	章列表
	 */
	@Override
	public List<AdlChapterEntity> queryList(AdlChapterQuery adlChapterQuery){
		return adlChapterDao.queryList(adlChapterQuery);
	}
	
	/**
	 * 查询章数量
	 * @return	章数量
	 */
	@Override
	public int queryTotal(Long courseId){
		return adlChapterDao.queryTotal(courseId);
	}
	
	/**
	 * 新增章
	 */
	@Override
	public int save(AdlChapterEntity adlChapter){
		//TODO 重名校验
		if(null == adlChapter) {
			throw new RRException("adlChapter不能为空!");
		}
		if(StringUtils.isBlank(adlChapter.getChapterName())) {
			throw new RRException("章名称不能为空!");
		}
		if(null == adlChapter.getCourseId()) {
			throw new RRException("章所属课程不能为空!");
		}
		if(StringUtils.isBlank(adlChapter.getChapterNo())) {//编号
			String chapterNo = getChapterNo(adlChapter.getCourseId());
			adlChapter.setChapterNo(chapterNo);
		}
		adlChapter.setDr(0);
		return adlChapterDao.save(adlChapter);
	}
	
	/**
	 * 更新章
	 */
	@Override
	public int update(AdlChapterEntity adlChapter){
		//TODO 重名校验
		if(null == adlChapter) {
			throw new RRException("adlChapter不能为空!");
		}
		if(null ==adlChapter.getChapterId()) {
			throw new RRException("章ID不能为空!");
		}
		if(StringUtils.isBlank(adlChapter.getChapterName())) {
			throw new RRException("章名称不能为空!");
		}
		if(null == adlChapter.getCourseId()) {
			throw new RRException("章所属课程不能为空!");
		}
		if(StringUtils.isBlank(adlChapter.getChapterNo())) {//编号
			throw new RRException("章编码不能为空!");
		}
		return adlChapterDao.update(adlChapter);
	}
	
	/**
	 * 按照ID删除
	 * @param chapterId	自增id
	 */
	@Override
	public int delete(Long chapterId) {
		int sectionCount = this.adlSectionService.queryCountByParentId(chapterId);
		if(0 < sectionCount) {
			throw new RRException("章下还有节，删除失败!");
		}
		return adlChapterDao.delete(chapterId);
	}

	@Override
	public String getChapterNo(Long courseId){
		if(null == courseId || courseId <= 0) {
			throw new RRException("课程ID获取错误!");
		}
		AdlCourseEntity adlCourseEntity = this.adlCourseService.queryObject(courseId);
		if(null == adlCourseEntity) {
			throw new RRException("课程获取错误!");
		}
		String code = AdlCodeUtils.getChapterCode(adlCourseEntity.getCourseNo(), this.adlChapterNoKGS.nextId()+"");
		return code;
	}

	@Override
	public AdlChapterEntity saveOrQueryByChapterName(AdlChapterEntity adlChapter) {
		if(adlChapter != null && adlChapter.getCourseId() != null && StringUtils.isNotBlank(adlChapter.getChapterName())) {
			AdlChapterEntity entity = this.adlChapterDao.queryObjectByName(adlChapter.getChapterName(), adlChapter.getCourseId());
			if(null != entity) {
				adlChapter = entity;
			}else {
				this.save(adlChapter);
			}
		}
		return adlChapter;
	}
}
