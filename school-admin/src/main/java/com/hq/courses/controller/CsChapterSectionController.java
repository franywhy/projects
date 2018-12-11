package com.hq.courses.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.adaptive.pojo.ZJEnum;
import com.hq.adaptive.pojo.ZJPOJO;
import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.entity.CsSectionEntity;
import com.hq.courses.pojo.CsSectionPOJO;
import com.hq.courses.pojo.query.CsChapterQuery;
import com.hq.courses.pojo.query.CsKnowledgeQuery;
import com.hq.courses.pojo.query.CsSectionQuery;
import com.hq.courses.service.CsChapterService;
import com.hq.courses.service.CsKnowledgeService;
import com.hq.courses.service.CsSectionService;

import io.renren.common.doc.SysLog;
import io.renren.common.validator.Assert;
import io.renren.utils.R;

/**  
 * 类说明   章节
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月27日
 */
@RestController
@RequestMapping("/courses/zj")
public class CsChapterSectionController {
	
	@Autowired
	private CsChapterService csChapterService;
	@Autowired
	private CsSectionService csSectionService;
	@Autowired
	private CsKnowledgeService csKnowledgeService;
	/**
	 * 章节列表
	 * @param courseId	课程ID
	 * @return	章节列表
	 */
	@RequestMapping("/zjList/{courseId}")
	public List<ZJPOJO> zjList(@PathVariable("courseId") Long courseId) {
		List<ZJPOJO> zjs = new ArrayList<>();
		//章
		CsChapterQuery csChapterQuery = new CsChapterQuery();
		csChapterQuery.setOffset(0);
		csChapterQuery.setLimit(1000);
		csChapterQuery.setCourseId(courseId);
		List<CsChapterEntity> chapters = this.csChapterService.queryList(csChapterQuery);
		if(CollectionUtils.isNotEmpty(chapters)) {
			CsKnowledgeQuery csKnowledgeQuery = null;
			for (CsChapterEntity csChapterEntity : chapters) {
				ZJPOJO zj = new ZJPOJO();
				zj.initZId(csChapterEntity.getChapterId());
				zj.setCode(csChapterEntity.getChapterNo());
//				zj.setKnowledgeNum(0);
				zj.setName(csChapterEntity.getChapterName());
				zj.setOrderNum(csChapterEntity.getOrderNum());
				//知识点数量
				csKnowledgeQuery = new CsKnowledgeQuery();
				csKnowledgeQuery.setChapterId(csChapterEntity.getChapterId());
				zj.setKnowledgeNum(csKnowledgeService.queryTotal(csKnowledgeQuery));
				
				zjs.add(zj);
			}
		}
		//节
		CsSectionQuery csSectionQuery = new CsSectionQuery();
		csSectionQuery.setOffset(0);
		csSectionQuery.setLimit(1000);
		csSectionQuery.setCourseId(courseId);
		List<CsSectionEntity> sections = this.csSectionService.queryList(csSectionQuery);
		if(CollectionUtils.isNotEmpty(sections)) {
			CsKnowledgeQuery csKnowledgeQuery = null;
			for (CsSectionEntity csSectionEntity : sections) {
				ZJPOJO zj = new ZJPOJO();
				zj.initJId(csSectionEntity.getSectionId(), csSectionEntity.getChapterId());
				zj.setCode(csSectionEntity.getSectionNo());
//				zj.setKnowledgeNum(0);
				zj.setName(csSectionEntity.getSectionName());
				zj.setOrderNum(csSectionEntity.getOrderNum());
				//知识点数量
				csKnowledgeQuery = new CsKnowledgeQuery();
				csKnowledgeQuery.setSectionId(csSectionEntity.getSectionId());
				zj.setKnowledgeNum(csKnowledgeService.queryTotal(csKnowledgeQuery));
				zjs.add(zj);
			}
		}
		return zjs;
	}
	
	/**
	 * 章节信息
	 * @param zjId	章节ID
	 * @return		章节信息
	 */
	@RequestMapping("/zjInfo/{zjId}")
	public R getZJInfo(@PathVariable("zjId") String zjId){
		if(StringUtils.isNotBlank(zjId)) {
			if(zjId.indexOf(ZJEnum.Z.getHead()) == 0) {//章
				String zidStr = zjId.substring(ZJEnum.Z.getHead().length(), zjId.length());
				Long chapterId = Long.valueOf(zidStr);
				CsChapterEntity chapterEntity = this.csChapterService.queryObject(chapterId);
				return R.ok().put(chapterEntity).put("zjType", ZJEnum.Z.getValue());
			}else if(zjId.indexOf(ZJEnum.J.getHead()) == 0) {//节
				String jidStr = zjId.substring(ZJEnum.J.getHead().length(), zjId.length());
				Long sectionId = Long.valueOf(jidStr);
				CsSectionPOJO csSectionPOJO = this.csSectionService.queryObject(sectionId);
				return R.ok().put(csSectionPOJO).put("zjType", ZJEnum.J.getValue());
			}
		}
		return R.error("参数异常!");
	}
	
	@SysLog("ADL新增章")
	@RequestMapping("/cschapter/save")
	public R saveChapter(@RequestBody CsChapterEntity csChapterEntity , HttpServletRequest request) {
		this.csChapterService.save(csChapterEntity);
		return R.ok();
	}
	@SysLog("ADL修改章")
	@RequestMapping("/cschapter/update")
	public R updateChapter(@RequestBody CsChapterEntity csChapterEntity , HttpServletRequest request) {
		this.csChapterService.update(csChapterEntity);
		return R.ok();
	}
	
//	@SysLog("ADL删除章节")
	@RequestMapping("/zjRemove")
	public R zjRemove(@RequestBody String zjId , HttpServletRequest request){
		if(StringUtils.isNotBlank(zjId)) {
			if(zjId.indexOf(ZJEnum.Z.getHead()) == 0) {//章
				// 删除章
				String zidStr = zjId.substring(ZJEnum.Z.getHead().length(), zjId.length());
				Long chapterId = Long.valueOf(zidStr);
				this.csChapterService.delete(chapterId);
				return R.ok();
			}else if(zjId.indexOf(ZJEnum.J.getHead()) == 0) {//节
				// 删除节
				String jidStr = zjId.substring(ZJEnum.J.getHead().length(), zjId.length());
				Long sectionId = Long.valueOf(jidStr);
				this.csSectionService.delete(sectionId);
				return R.ok();
			}
		}
		return R.error("参数异常!");
	}
	
	/**
	 * 获取章编码
	 * @param courseId	课程ID
	 * @return			章编码
	 */
	@RequestMapping("/getChapterNo/{courseId}")
	public R getChapterNo(@PathVariable("courseId") Long courseId) {
		String chapterNo = null;
		chapterNo = this.csChapterService.getChapterNo(courseId);
		return R.ok().put(chapterNo);
	}
	
	/**
	 * 校验章编号
	 * @return
	 */
	@RequestMapping("/checkChapterNo")
	public R checkCourseNo(HttpServletRequest request) {
		String chapterNo = ServletRequestUtils.getStringParameter(request, "chapterNo" ,null);
		Long chapterId = ServletRequestUtils.getLongParameter(request, "chapterId" , 0);
		Assert.isBlank(chapterNo, "请输入章编号!");
		this.csChapterService.checkChapterNo(chapterId,chapterNo);
		return R.ok();
	}
	
	/**
	 * 新增节时-获取章信息
	 * @param zjId	章节ID
	 * @return		章ID、章编码、章名称、节编码
	 */
	@RequestMapping("/jAddBeforeGetChapterInfo")
	public R jAddBeforeGetInfo(@RequestBody String zjId , HttpServletRequest request) {
		if(StringUtils.isNotBlank(zjId)) {
			Map<String , Object> data = null;
			if(zjId.indexOf(ZJEnum.Z.getHead()) == 0) {//章
				// 删除章
				String zidStr = zjId.substring(ZJEnum.Z.getHead().length(), zjId.length());
				Long chapterId = Long.valueOf(zidStr);
				CsChapterEntity csChapterEntity = this.csChapterService.queryObject(chapterId);
				if(null != csChapterEntity) {
					data = new HashMap<>();
					data.put("chapterId", csChapterEntity.getChapterId());
					data.put("chapterNo", csChapterEntity.getChapterNo());
					data.put("chapterName", csChapterEntity.getChapterName());
				}
			}else if(zjId.indexOf(ZJEnum.J.getHead()) == 0) {//节
				// 删除节
				String jidStr = zjId.substring(ZJEnum.J.getHead().length(), zjId.length());
				Long sectionId = Long.valueOf(jidStr);
				CsSectionPOJO csSectionPOJO = this.csSectionService.queryObject(sectionId);
				if(null != csSectionPOJO) {
					data = new HashMap<>();
					data.put("chapterId", csSectionPOJO.getChapterId());
					data.put("chapterNo", csSectionPOJO.getChapterNo());
					data.put("chapterName", csSectionPOJO.getChapterName());
				}
			}
			if(null != data) {
				//节编码
				String SectionNo;
				SectionNo = this.csSectionService.getSectionCode((Long) data.get("chapterId"));
				data.put("sectionNo", SectionNo);
				return R.ok().put(data);
			}
		}
		return R.error("参数异常!");
	}
	
	@SysLog("ADL新增节")
	@RequestMapping("/cssection/save")
	public R saveSection(@RequestBody CsSectionEntity csSectionEntity , HttpServletRequest request) {
		this.csSectionService.save(csSectionEntity);
		return R.ok();
	}
	
	@SysLog("ADL修改节")
	@RequestMapping("/cssection/update")
	public R updateSection(@RequestBody CsSectionEntity csSectionEntity , HttpServletRequest request) {
		this.csSectionService.update(csSectionEntity);
		return R.ok();
	}
	
	@SysLog("ADL修改节上级")
	@RequestMapping("/cssection/updateSectionParent")
	public R updateSectionParent(@RequestBody CsSectionEntity csSectionEntity , HttpServletRequest request) {
		this.csSectionService.updateSectionParent(csSectionEntity);
		return R.ok();
	}
	/**
	 * 校验节编号
	 * @return
	 */
	@RequestMapping("/checkSectionNo")
	public R checkSectionNo(HttpServletRequest request) {
		String sectionNo = ServletRequestUtils.getStringParameter(request, "sectionNo" ,null);
		Long sectionId = ServletRequestUtils.getLongParameter(request, "section" , 0);
		Assert.isBlank(sectionNo, "请输入节编号!");
		this.csSectionService.checkSectionNo(sectionId,sectionNo);
		return R.ok();
	}
}
