package com.hq.courses.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.adaptive.pojo.CourseTreePOJO;
import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.entity.CsSectionEntity;
import com.hq.courses.pojo.CsCoursePOJO;
import com.hq.courses.pojo.query.CsChapterQuery;
import com.hq.courses.pojo.query.CsConfigQuery;
import com.hq.courses.pojo.query.CsCourseQuery;
import com.hq.courses.pojo.query.CsSectionQuery;
import com.hq.courses.service.CsChapterService;
import com.hq.courses.service.CsConfigService;
import com.hq.courses.service.CsCourseService;
import com.hq.courses.service.CsSectionService;

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
@RequestMapping("/courses/layer")
public class CsLayerController {
	
	@Autowired
	private CsCourseService csCourseService;
	@Autowired
	private CsChapterService csChapterService;
	@Autowired
	private CsSectionService csSectionService;
	@Autowired
	private CsConfigService csConfigService;
	/**
	 * 章节列表
	 * @param courseId	课程ID
	 * @return	章节列表
	 */
	@RequestMapping("/chapterList")
	public R chapterList(HttpServletRequest request){
		CsChapterQuery csChapterQuery = new CsChapterQuery();
		csChapterQuery.initPage(request);
		Long courseId = ServletRequestUtils.getLongParameter(request , "courseId" , 0);
		if(null == courseId || courseId == 0) {
			return R.error("参数错误");
		}
		csChapterQuery.setCourseId(courseId);
		List<CsChapterEntity> chapters = this.csChapterService.queryList(csChapterQuery);
		int total = this.csChapterService.queryTotal(courseId);
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
		CsCourseQuery csCourseQuery = new CsCourseQuery();
		csCourseQuery.initPage(request);
		String courseName = ServletRequestUtils.getStringParameter(request, "courseName");
		if(StringUtils.isNotBlank(courseName)) {
			csCourseQuery.setCourseName(courseName);;
		}
		String courseNo = ServletRequestUtils.getStringParameter(request, "courseNo");
		if(StringUtils.isNotBlank(courseNo)) {
			csCourseQuery.setCourseNo(courseNo);
		}
		List<CsCoursePOJO> list = this.csCourseService.queryList(csCourseQuery);
		int total = this.csCourseService.queryTotal(csCourseQuery);
		PageUtils pageUtil = new PageUtils(list, total, request);
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
		CsCoursePOJO csCoursePOJO = this.csCourseService.queryObject(courseId);
		if(csCoursePOJO == null) {
			throw new RRException("未找到对应课程！");
		}
		List<CourseTreePOJO> courseList = new ArrayList<>();
		//第一级 课程
		CourseTreePOJO coursePOJO = new CourseTreePOJO(csCoursePOJO.getCourseId(),"("+csCoursePOJO.getCourseNo()+")"+csCoursePOJO.getCourseName(),1);
		courseList.add(coursePOJO);
		
		//第二级 章
		CsChapterQuery csChapterQuery = new CsChapterQuery();
		csChapterQuery.setCourseId(courseId);
		List<CsChapterEntity> csChapterEntities = this.csChapterService.queryList(csChapterQuery);
		if(null != csChapterEntities && !csChapterEntities.isEmpty()) {
			List<CourseTreePOJO> chapterPOJO = new ArrayList<>();
			coursePOJO.setChildren(chapterPOJO);
			for (CsChapterEntity csChapterEntity : csChapterEntities) {
				CourseTreePOJO chapterTree = new CourseTreePOJO(csChapterEntity.getChapterId() , "("+csChapterEntity.getChapterNo()+")"+csChapterEntity.getChapterName() ,2);
				
				chapterPOJO.add(chapterTree);
				
				//第三级 节
				CsSectionQuery csSectionQuery = new CsSectionQuery();
				csSectionQuery.setChapterId(csChapterEntity.getChapterId());
				List<CsSectionEntity> csSectionEntities = csSectionService.queryList(csSectionQuery);
				if(null != csSectionEntities && !csSectionEntities.isEmpty()) {
					List<CourseTreePOJO> sectionPOJO = new ArrayList<>();
					chapterTree.setChildren(sectionPOJO);
					for (CsSectionEntity csSectionEntity : csSectionEntities) {
						CourseTreePOJO sectionTree = new CourseTreePOJO(csSectionEntity.getSectionId() , "("+csSectionEntity.getSectionNo()+")"+csSectionEntity.getSectionName() , 3);
						sectionPOJO.add(sectionTree);
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
	public R csConfingLevel( HttpServletRequest request) {
		CsConfigQuery csConfigQuery = new CsConfigQuery();
		csConfigQuery.initPage(request);
		PageUtils listKnowledgeLevel = this.csConfigService.queryListKnowledgeLevel(csConfigQuery);
		return R.ok().put(listKnowledgeLevel);
	}
	
	/**
	 * 知识点题型列表
	 * @param request
	 * @return		知识点题型列表
	 */
	@RequestMapping("/knowledgeQuestionType")
	public R csConfingQuestionType( HttpServletRequest request) {
		CsConfigQuery csConfigQuery = new CsConfigQuery();
		csConfigQuery.initPage(request);
		PageUtils pageUtils = this.csConfigService.queryListKnowledgeQuestionType(csConfigQuery);
		return R.ok().put(pageUtils);
	}
	
	/**
	 * 知识点考点列表
	 * @param request
	 * @return		知识点题型列表
	 */
	@RequestMapping("/knowledgeKeyPoint")
	public R knowledgeKeyPoint( HttpServletRequest request) {
		CsConfigQuery csConfigQuery = new CsConfigQuery();
		csConfigQuery.initPage(request);
		PageUtils pageUtils = this.csConfigService.queryListKnowledgeKeyPoin(csConfigQuery);
		return R.ok().put(pageUtils);
	}
	
}