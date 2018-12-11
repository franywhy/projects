package com.hq.adaptive.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hq.adaptive.service.AdlSectionService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.entity.AdlSectionEntity;
import com.hq.adaptive.pojo.AdlSectionPOJO;
import com.hq.adaptive.pojo.ZJEnum;
import com.hq.adaptive.pojo.ZJPOJO;
import com.hq.adaptive.pojo.query.AdlChapterQuery;
import com.hq.adaptive.pojo.query.AdlKnowledgeQuery;
import com.hq.adaptive.pojo.query.AdlSectionQuery;
import com.hq.adaptive.service.AdlChapterService;
import com.hq.adaptive.service.AdlKnowledgeService;

import io.renren.common.doc.SysLog;
import io.renren.utils.R;

/**  
 * 类说明   章节
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月27日
 */
@RestController
@RequestMapping("/adaptive/zj")
public class AdlChapterSectionController {
	
	@Autowired
	private AdlChapterService adlChapterService;
	@Autowired
	private AdlSectionService adlSectionService;
	@Autowired
	private AdlKnowledgeService adlKnowledgeService;
	/**
	 * 章节列表
	 * @param courseId	课程ID
	 * @return	章节列表
	 */
	@RequestMapping("/zjList/{courseId}")
	public List<ZJPOJO> zjList(@PathVariable("courseId") Long courseId) {
		List<ZJPOJO> zjs = new ArrayList<>();
		//章
		AdlChapterQuery adlChapterQuery = new AdlChapterQuery();
		adlChapterQuery.setOffset(0);
		adlChapterQuery.setLimit(1000);
		adlChapterQuery.setCourseId(courseId);
		
		List<AdlChapterEntity> chapters = this.adlChapterService.queryList(adlChapterQuery);
		if(CollectionUtils.isNotEmpty(chapters)) {
			AdlKnowledgeQuery adlKnowledgeQuery = null;
			for (AdlChapterEntity adlChapterEntity : chapters) {
				ZJPOJO zj = new ZJPOJO();
				zj.initZId(adlChapterEntity.getChapterId());
				zj.setCode(adlChapterEntity.getChapterNo());
				zj.setKnowledgeNum(0);
				zj.setName(adlChapterEntity.getChapterName());
				zj.setOrderNum(adlChapterEntity.getOrderNum());
				//知识点数量
				adlKnowledgeQuery = new AdlKnowledgeQuery();
				//知识点数量
				adlKnowledgeQuery.setChapterId(adlChapterEntity.getChapterId());
				zj.setKnowledgeNum(adlKnowledgeService.queryTotal(adlKnowledgeQuery));
				
				zjs.add(zj);
			}
		}
		//节
		AdlSectionQuery adlSectionQuery = new AdlSectionQuery();
		adlSectionQuery.setOffset(0);
		adlSectionQuery.setLimit(1000);
		adlSectionQuery.setCourseId(courseId);
		List<AdlSectionEntity> sections = this.adlSectionService.queryList(adlSectionQuery);
		if(CollectionUtils.isNotEmpty(sections)) {
			AdlKnowledgeQuery adlKnowledgeQuery = null;
			for (AdlSectionEntity adlSectionEntity : sections) {
				ZJPOJO zj = new ZJPOJO();
				zj.initJId(adlSectionEntity.getSectionId(), adlSectionEntity.getChapterId());
				zj.setCode(adlSectionEntity.getSectionNo());
				zj.setKnowledgeNum(0);
				zj.setName(adlSectionEntity.getSectionName());
				zj.setOrderNum(adlSectionEntity.getOrderNum());
				//知识点数量
				adlKnowledgeQuery = new AdlKnowledgeQuery();
				adlKnowledgeQuery.setSectionId(adlSectionEntity.getSectionId());
				zj.setKnowledgeNum(adlKnowledgeService.queryTotal(adlKnowledgeQuery));
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
				AdlChapterEntity chapterEntity = this.adlChapterService.queryObject(chapterId);
				return R.ok().put(chapterEntity).put("zjType", ZJEnum.Z.getValue());
			}else if(zjId.indexOf(ZJEnum.J.getHead()) == 0) {//节
				String jidStr = zjId.substring(ZJEnum.J.getHead().length(), zjId.length());
				Long sectionId = Long.valueOf(jidStr);
				AdlSectionPOJO adlSectionPOJO = this.adlSectionService.queryObject(sectionId);
				return R.ok().put(adlSectionPOJO).put("zjType", ZJEnum.J.getValue());
			}
		}
		return R.error("参数异常!");
	}
	
	@SysLog("ADL新增章")
	@RequestMapping("/adlchapter/save")
	public R saveChapter(@RequestBody AdlChapterEntity adlChapterEntity , HttpServletRequest request) {
		this.adlChapterService.save(adlChapterEntity);
		return R.ok();
	}
	@SysLog("ADL修改章")
	@RequestMapping("/adlchapter/update")
	public R updateChapter(@RequestBody AdlChapterEntity adlChapterEntity , HttpServletRequest request) {
		this.adlChapterService.update(adlChapterEntity);
		return R.ok();
	}
	

	/**
	 * 获取章编码
	 * @param courseId	课程ID
	 * @return			章编码
	 */
	@RequestMapping("/getChapterNo/{courseId}")
	public R getChapterNo(@PathVariable("courseId") Long courseId) {
		String chapterNo = null;
		chapterNo = this.adlChapterService.getChapterNo(courseId);
		return R.ok().put(chapterNo);
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
				AdlChapterEntity adlChapterEntity = this.adlChapterService.queryObject(chapterId);
				if(null != adlChapterEntity) {
					data = new HashMap<>();
					data.put("chapterId", adlChapterEntity.getChapterId());
					data.put("chapterNo", adlChapterEntity.getChapterNo());
					data.put("chapterName", adlChapterEntity.getChapterName());
				}
			}else if(zjId.indexOf(ZJEnum.J.getHead()) == 0) {//节
				// 删除节
				String jidStr = zjId.substring(ZJEnum.J.getHead().length(), zjId.length());
				Long sectionId = Long.valueOf(jidStr);
				AdlSectionPOJO adlSectionPOJO = this.adlSectionService.queryObject(sectionId);
				if(null != adlSectionPOJO) {
					data = new HashMap<>();
					data.put("chapterId", adlSectionPOJO.getChapterId());
					data.put("chapterNo", adlSectionPOJO.getChapterNo());
					data.put("chapterName", adlSectionPOJO.getChapterName());
				}
			}
			if(null != data) {
				//节编码
				String SectionNo;
				SectionNo = this.adlSectionService.getSectionCode((Long) data.get("chapterId"));
				data.put("sectionNo", SectionNo);
				return R.ok().put(data);
			}
		}
		return R.error("参数异常!");
	}

}
