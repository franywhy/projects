package com.hq.adaptive.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hq.adaptive.entity.AdlKnowledgeEntity;
import com.hq.adaptive.pojo.AdlKnowledgePOJO;
import com.hq.adaptive.pojo.query.*;
import com.hq.adaptive.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.entity.AdlCourseEntity;
import com.hq.adaptive.entity.AdlSectionEntity;
import com.hq.adaptive.pojo.CourseTreePOJO;

import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

/**  
 * 类说明   章节
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月27日
 */
@RestController
@RequestMapping("/adaptive/layer")
public class AdlLayerController {
	
	@Autowired
	private AdlCourseService adlCourseService;
	@Autowired
	private AdlChapterService adlChapterService;
	@Autowired
	private AdlSectionService adlSectionService;
	@Autowired
	private AdlConfigService adlConfigService;

	@Autowired
	private AdlKnowledgeService adlKnowledgeService;
	/**
	 * 章节列表
	 * @return	章节列表
	 */
	@RequestMapping("/chapterList")
	public R chapterList(HttpServletRequest request){
		AdlChapterQuery adlChapterQuery = new AdlChapterQuery();
		adlChapterQuery.initPage(request);
		Long courseId = ServletRequestUtils.getLongParameter(request , "courseId" , 0);
		if(null == courseId || courseId == 0) {
			return R.error("参数错误");
		}
		adlChapterQuery.setCourseId(courseId);
		List<AdlChapterEntity> chapters = this.adlChapterService.queryList(adlChapterQuery);
		int total = this.adlChapterService.queryTotal(courseId);
		PageUtils pageUtil = new PageUtils(chapters, total, request);
		return R.ok().put(pageUtil);
	}
	/**
	 * 课程列表
	 * @param 	
	 * @return	课程列表
	 * @throws ServletRequestBindingException 
	 */
	@RequestMapping("/courseList")
	public R courseList( HttpServletRequest request) throws ServletRequestBindingException {
		AdlCourseQuery adlCourseQuery = new AdlCourseQuery();
		adlCourseQuery.initPage(request);
		String courseName = ServletRequestUtils.getStringParameter(request, "courseName");
		if(StringUtils.isNotBlank(courseName)) {
			adlCourseQuery.setCourseName(courseName);;
		}
		String courseNo = ServletRequestUtils.getStringParameter(request, "courseNo");
		if(StringUtils.isNotBlank(courseNo)) {
			adlCourseQuery.setCourseNo(courseNo);
		}
		List<AdlCourseEntity> list = this.adlCourseService.queryList(adlCourseQuery);
		int total = this.adlCourseService.queryTotal(adlCourseQuery);
		PageUtils pageUtil = new PageUtils(list, total, request);
		return R.ok().put(pageUtil);
	}
	/**
	 * 节列表
	 * @param
	 * @return	节列表
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping("/sectionList")
	public R sectionList( HttpServletRequest request) throws ServletRequestBindingException {
		AdlSectionQuery adlSectionQuery = new AdlSectionQuery();
		adlSectionQuery.initPage(request);

		String courseIdString = ServletRequestUtils.getStringParameter(request, "courseId");
		if(StringUtils.isBlank(courseIdString)){
			throw new RRException("请选择课程!");
		}else{
			adlSectionQuery.setCourseId(Long.valueOf(courseIdString));
		}

		String sectionName = ServletRequestUtils.getStringParameter(request, "sectionName");
		if(StringUtils.isNotBlank(sectionName)) {
			adlSectionQuery.setSectionName(sectionName);;
		}
		String sectionNo = ServletRequestUtils.getStringParameter(request, "sectionNo");
		if(StringUtils.isNotBlank(sectionNo)) {
			adlSectionQuery.setSectionNo(sectionNo);
		}

		List<AdlSectionEntity> adlSectionEntityList = this.adlSectionService.queryList(adlSectionQuery);
		int total = this.adlSectionService.queryTotal(adlSectionQuery);
		PageUtils pageUtil = new PageUtils(adlSectionEntityList, total, request);
		return R.ok().put(pageUtil);
	}

	/**
	 * 课程树-章-节
	 * @param courseId 课程ID
	 * @param request
	 * @return		课程树形结构 课程、章、节
	 */
	@RequestMapping("/courseTree/{courseId}")
	public R courseTree(@PathVariable("courseId")Long courseId , HttpServletRequest request) {
		if(null == courseId || courseId == 0) {
			throw new RRException("请选择课程！");
		}
		AdlCourseEntity adlCourseEntity = this.adlCourseService.queryObject(courseId);
		if(adlCourseEntity == null) {
			throw new RRException("未找到对应课程！");
		}
		List<CourseTreePOJO> courseList = new ArrayList<>();
		//第一级 课程
		CourseTreePOJO coursePOJO = new CourseTreePOJO(adlCourseEntity.getCourseId(),"("+adlCourseEntity.getCourseNo()+")"+adlCourseEntity.getCourseName(),1);
		courseList.add(coursePOJO);
		
		//第二级 章
		AdlChapterQuery adlChapterQuery = new AdlChapterQuery();
		adlChapterQuery.setCourseId(courseId);
		List<AdlChapterEntity> adlChapterEntities = this.adlChapterService.queryList(adlChapterQuery);
		if(null != adlChapterEntities && !adlChapterEntities.isEmpty()) {
			List<CourseTreePOJO> chapterPOJO = new ArrayList<>();
			coursePOJO.setChildren(chapterPOJO);
			for (AdlChapterEntity adlChapterEntity : adlChapterEntities) {
				CourseTreePOJO chapterTree = new CourseTreePOJO(adlChapterEntity.getChapterId() , "("+adlChapterEntity.getChapterNo()+")"+adlChapterEntity.getChapterName() ,2);
				
				chapterPOJO.add(chapterTree);
				
				//第三级 节
				AdlSectionQuery adlSectionQuery = new AdlSectionQuery();
				adlSectionQuery.setChapterId(adlChapterEntity.getChapterId());
				List<AdlSectionEntity> adlSectionEntities = adlSectionService.queryList(adlSectionQuery);
				if(null != adlSectionEntities && !adlSectionEntities.isEmpty()) {
					List<CourseTreePOJO> sectionPOJO = new ArrayList<>();
					chapterTree.setChildren(sectionPOJO);
					for (AdlSectionEntity adlSectionEntity : adlSectionEntities) {
						CourseTreePOJO sectionTree = new CourseTreePOJO(adlSectionEntity.getSectionId() , "("+adlSectionEntity.getSectionNo()+")"+adlSectionEntity.getSectionName() , 3);
						sectionPOJO.add(sectionTree);
					}
				}
				
				
			}
		}
		return R.ok().put(courseList);
	}

	/**
	 * 课程树形结构 章、节、知识点
	 * @param courseId 课程ID
	 * @param request
	 * @return		课程树形结构 章、节、知识点
	 */
	@RequestMapping("/courseTreeAll/{courseId}")
	public R courseTreeAll(@PathVariable("courseId")Long courseId , HttpServletRequest request) {
		if(null == courseId || courseId == 0) {
			throw new RRException("请选择课程！");
		}
		AdlCourseEntity adlCourseEntity = this.adlCourseService.queryObject(courseId);
		if(adlCourseEntity == null) {
			throw new RRException("未找到对应课程！");
		}
		List<CourseTreePOJO> courseList = new ArrayList<>();
		//第一级 课程
		CourseTreePOJO coursePOJO = new CourseTreePOJO(adlCourseEntity.getCourseId(),"("+adlCourseEntity.getCourseNo()+")"+adlCourseEntity.getCourseName(),1);
		courseList.add(coursePOJO);

		//第二级 章
		AdlChapterQuery adlChapterQuery = new AdlChapterQuery();
		adlChapterQuery.setCourseId(courseId);
		List<AdlChapterEntity> adlChapterEntities = this.adlChapterService.queryList(adlChapterQuery);
		if(null != adlChapterEntities && !adlChapterEntities.isEmpty()) {
			List<CourseTreePOJO> chapterPOJO = new ArrayList<>();
			coursePOJO.setChildren(chapterPOJO);
			for (AdlChapterEntity adlChapterEntity : adlChapterEntities) {
				CourseTreePOJO chapterTree = new CourseTreePOJO(adlChapterEntity.getChapterId() , "("+adlChapterEntity.getChapterNo()+")"+adlChapterEntity.getChapterName() ,2);

				chapterPOJO.add(chapterTree);

				//第三级 节
				AdlSectionQuery adlSectionQuery = new AdlSectionQuery();
				adlSectionQuery.setChapterId(adlChapterEntity.getChapterId());
				List<AdlSectionEntity> adlSectionEntities = adlSectionService.queryList(adlSectionQuery);
				if(null != adlSectionEntities && !adlSectionEntities.isEmpty()) {
					List<CourseTreePOJO> sectionPOJO = new ArrayList<>();
					chapterTree.setChildren(sectionPOJO);
					for (AdlSectionEntity adlSectionEntity : adlSectionEntities) {
						CourseTreePOJO sectionTree = new CourseTreePOJO(adlSectionEntity.getSectionId() , "("+adlSectionEntity.getSectionNo()+")"+adlSectionEntity.getSectionName() , 3);
						sectionPOJO.add(sectionTree);

						//第四级 知识点
						AdlKnowledgeQuery adlKnowledgeQuery = new AdlKnowledgeQuery();
						adlKnowledgeQuery.setSectionId(adlSectionEntity.getSectionId());

//						List<AdlKnowledgePOJO> adlKnowledgePOJOList = adlKnowledgeService.queryList(adlKnowledgeQuery);
//						if(null != adlKnowledgePOJOList && !adlKnowledgePOJOList.isEmpty()){
//							List<CourseTreePOJO> knowledgePOJO = new ArrayList<>();
//							sectionTree.setChildren(knowledgePOJO);
//							for(AdlKnowledgePOJO adlKnowledgePOJO : adlKnowledgePOJOList){
//								CourseTreePOJO knowledgeTree = new CourseTreePOJO(adlKnowledgePOJO.getKnowledgeId(), "("+adlKnowledgePOJO.getKnowledgeNo()+")"+adlKnowledgePOJO.getKnowledgeName() , 4);
//								knowledgePOJO.add(knowledgeTree);
//							}
//						}
						List<AdlKnowledgeEntity> adlKnowledgeEntityList = adlKnowledgeService.queryEntityList(adlKnowledgeQuery);
						if(null != adlKnowledgeEntityList && !adlKnowledgeEntityList.isEmpty()){
							List<CourseTreePOJO> knowledgeList = new ArrayList<>();
							sectionTree.setChildren(knowledgeList);
							for(AdlKnowledgeEntity adlKnowledgeEntity : adlKnowledgeEntityList){
								CourseTreePOJO knowledgeTree = new CourseTreePOJO(adlKnowledgeEntity.getKnowledgeId(), "("+adlKnowledgeEntity.getKnowledgeNo()+")"+adlKnowledgeEntity.getKnowledgeName() , 4);
                                knowledgeTree.setKnowledgeId(adlKnowledgeEntity.getKnowledgeId());
								knowledgeList.add(knowledgeTree);
							}
						}
					}
				}


			}
		}
		return R.ok().put(courseList);
	}

	/**
	 * 知识点难度列表
	 * @param request
	 * @return		知识点难度列表
	 */
	@RequestMapping("/knowledgeLevel")
	public R adlConfingLevel( HttpServletRequest request) {
		AdlConfigQuery adlConfigQuery = new AdlConfigQuery();
		adlConfigQuery.initPage(request);
		PageUtils listKnowledgeLevel = this.adlConfigService.queryListKnowledgeLevel(adlConfigQuery);
		return R.ok().put(listKnowledgeLevel);
	}
	
	/**
	 * 知识点题型列表
	 * @param request
	 * @return		知识点题型列表
	 */
	@RequestMapping("/knowledgeQuestionType")
	public R adlConfingQuestionType( HttpServletRequest request) {
		AdlConfigQuery adlConfigQuery = new AdlConfigQuery();
		adlConfigQuery.initPage(request);
		PageUtils pageUtils = this.adlConfigService.queryListKnowledgeQuestionType(adlConfigQuery);
		return R.ok().put(pageUtils);
	}
	
	/**
	 * 知识点考点列表
	 * @param request
	 * @return		知识点题型列表
	 */
	@RequestMapping("/knowledgeKeyPoint")
	public R knowledgeKeyPoint( HttpServletRequest request) {
		AdlConfigQuery adlConfigQuery = new AdlConfigQuery();
		adlConfigQuery.initPage(request);
		PageUtils pageUtils = this.adlConfigService.queryListKnowledgeKeyPoin(adlConfigQuery);
		return R.ok().put(pageUtils);
	}
	
}