package com.hq.courses.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hq.adaptive.pojo.KnowledgeKeyPointEnum;
import com.hq.courses.httpService.CsKnowledgeCheckHttp;
import com.hq.courses.pojo.CsKnowledgePOJO;
import com.hq.courses.pojo.query.CsKnowledgeQuery;
import com.hq.courses.service.CsKnowledgeService;

import io.renren.common.doc.SysLog;
import io.renren.common.validator.Assert;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;

/**  
 * 类说明
 * 知识点
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月29日
 */
@RestController
@RequestMapping("/courses/knowledge")
public class CsKnowledgeController {
	
	@Autowired
	private CsKnowledgeService csKnowledgeService;
	
	/**
	 * 知识点列表
	 * @return					知识点列表信息
	 * @throws ServletRequestBindingException 
	 */
	@RequiresPermissions("courses:knowledge:list")
	@RequestMapping("/list")
	public R List(HttpServletRequest request) throws ServletRequestBindingException{
		CsKnowledgeQuery csKnowledgeQuery = new CsKnowledgeQuery();
		csKnowledgeQuery.initPage(request);
		//课程ID
		Long courseId = ServletRequestUtils.getLongParameter(request, "courseId" , 0);
		//章ID
		Long chapterId = ServletRequestUtils.getLongParameter(request, "chapterId" , 0);
		//节ID
		Long sectionId = ServletRequestUtils.getLongParameter(request, "sectionId" , 0);

		Long knowledgeId = ServletRequestUtils.getLongParameter(request, "knowledgeId" , 0);
		
		if(courseId == 0 && chapterId == 0 && sectionId == 0) {
			throw new RRException("请选择课程或者章节！");
		}
		csKnowledgeQuery.setCourseId(courseId);
		csKnowledgeQuery.setChapterId(chapterId);
		csKnowledgeQuery.setSectionId(sectionId);
		if(knowledgeId != 0){
			csKnowledgeQuery.setKnowledgeId(knowledgeId);
		}
		//知识点名称
		String knowledgeName = ServletRequestUtils.getStringParameter(request, "knowledgeName");
		if(StringUtils.isNotBlank(knowledgeName)) {
			csKnowledgeQuery.setKnowledgeName(knowledgeName);
		}
		//知识点编号
		String knowledgeNo = ServletRequestUtils.getStringParameter(request, "knowledgeNo");
		if(StringUtils.isNotBlank(knowledgeNo)) {
			csKnowledgeQuery.setKnowledgeNo(knowledgeNo);
		}
		//考点
		String keyPointString = ServletRequestUtils.getStringParameter(request, "keyPoint");
		if(StringUtils.isNotBlank(keyPointString)) {
			csKnowledgeQuery.setKeyPoint(Integer.valueOf(keyPointString));
		}
		//查询结果
		java.util.List<CsKnowledgePOJO> csKnowledgePOJOs = this.csKnowledgeService.queryList(csKnowledgeQuery);
		int total = this.csKnowledgeService.queryTotal(csKnowledgeQuery);
		PageUtils pageUtil = new PageUtils(csKnowledgePOJOs, total , request);
		return R.ok().put(pageUtil);
	}
	
	@RequiresPermissions("courses:knowledge:edit")
	@SysLog("新增知识点")
	@RequestMapping("/save")
	public R save(@RequestBody CsKnowledgePOJO csKnowledgePOJO , HttpServletRequest request) {
		this.verifyForm(csKnowledgePOJO);
		this.csKnowledgeService.save(csKnowledgePOJO);
		return R.ok();
	}
	
	@RequiresPermissions("courses:knowledge:edit")
	@SysLog("修改知识点")
	@RequestMapping("/update")
	public R update(@RequestBody CsKnowledgePOJO csKnowledgePOJO , HttpServletRequest request) {
		this.verifyForm(csKnowledgePOJO);
		this.csKnowledgeService.update(csKnowledgePOJO);
		return R.ok();
	}
	
	
	@RequestMapping("/info/{knowledgeId}")
	public R info(@PathVariable("knowledgeId")Long knowledgeId , HttpServletRequest request) {
		if(null == knowledgeId) {
			throw new RRException("知识点ID不能为空！");
		}
		CsKnowledgePOJO csKnowledgePOJO = this.csKnowledgeService.queryObject(knowledgeId);
		return R.ok().put(csKnowledgePOJO);
	}
	
	/**
	 * 删除
	 */
	@RequiresPermissions("courses:knowledge:edit")
	@SysLog("删除知识点")
	@RequestMapping("/delete")
	public R delete(@RequestBody Long knowledgeId){
		this.csKnowledgeService.updateDr(knowledgeId);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@RequiresPermissions("courses:knowledge:edit")
	@SysLog("启用知识点")
	@RequestMapping("/enable")
	public R enable(@RequestBody Long knowledgeId){
		this.verifyKnowledgeId(knowledgeId);
		this.csKnowledgeService.enable(knowledgeId);
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@RequiresPermissions("courses:knowledge:edit")
	@SysLog("禁用知识点")
	@RequestMapping("/unenable")
	public R unenable(@RequestBody Long knowledgeId){
		this.verifyKnowledgeId(knowledgeId);
		this.csKnowledgeService.unenable(knowledgeId);
		return R.ok();
	}

	/**
	 * @param knowledgeId
	 */
	private void verifyKnowledgeId(Long knowledgeId) {
		Assert.isNull(knowledgeId, "知识点ID不能为空!");
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(CsKnowledgePOJO csChapterPOJO){
		Assert.isNull(csChapterPOJO, "知识点对象不能为空！");
		Assert.isBlank(csChapterPOJO.getKnowledgeName(), "知识点名称不能为空！");
		if(null == csChapterPOJO.getKeyPoint() || 
				(KnowledgeKeyPointEnum.kp.getValue() != csChapterPOJO.getKeyPoint() && 
				KnowledgeKeyPointEnum.normal.getValue() != csChapterPOJO.getKeyPoint())) {
			throw new RRException("知识点考点不能为空！");
		}
		Assert.isNull(csChapterPOJO.getLevelId(), "知识点难度不能为空！");
		Assert.isNull(csChapterPOJO.getSectionId(), "知识点所属节！");
	}
	
	/**
	 * 批量导入
	 * @return
	 */
	@RequiresPermissions("courses:knowledge:edit")
	@SysLog("批量导入知识点")
	@RequestMapping("/importExcel")
	public R importExcel(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		checkExcelExt(file);
		Long courseId = ServletRequestUtils.getLongParameter(request, "courseId" , 0);
		String importExcel = this.csKnowledgeService.importExcel(courseId, file);
		return R.ok().put(importExcel);
	}
	private void checkExcelExt(MultipartFile file){
		if(null == file){
			throw new RRException("文件不能为空!");
		}
		String filename = file.getOriginalFilename();
		if(StringUtils.isNotBlank(filename) && filename.lastIndexOf(".") > 0){
			System.out.println(filename);
			String ext = filename.substring(filename.lastIndexOf(".") , filename.length());
			if(StringUtils.isBlank(ext) || !".xls".equals(ext)){
				throw new RRException("文件格式不支持请上传 xls文件!");
			}
		}else{
			throw new RRException("文件格式不支持请上传 xls文件!");
		}
	}

	/**
	 * 下载Excel模板
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/downExcelTemplate")
	public void downExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String downExcel = this.csKnowledgeService.downExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "批量导入知识点模板-" + DateUtils.format(new java.util.Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "知识点模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}
	
//	/**
//	 * 校验知识点是否出现闭环
//	 * @return
//	 */
//	@RequestMapping("/checkKnowledgeClose/{courseId}")
//	public R checkKnowledgeClose(@PathVariable("courseId")Long courseId , HttpServletRequest request) {
//		Assert.isNull(courseId, "请选择课程！");
//		boolean checkKnowledge = CsKnowledgeCheckHttp.checkKnowledge();
//		if(checkKnowledge) {
//			return R.ok().put(checkKnowledge);
//		}
//
//		return R.error("知识点出现闭环!");
//	}
	/**
	 * 校验知识点编号
	 * @return
	 */
	@RequestMapping("/checkKnowledgeNo")
	public R checkSectionNo(HttpServletRequest request) {
		String knowledgeNo = ServletRequestUtils.getStringParameter(request, "knowledgeNo" ,null);
		Long knowledgeId = ServletRequestUtils.getLongParameter(request, "knowledgeId" , 0);
		Long courseId = ServletRequestUtils.getLongParameter(request, "courseId" , 0);
		Assert.isBlank(knowledgeNo, "请输入知识点编号!");
		this.csKnowledgeService.checkKnowledgeNo(courseId, knowledgeId, knowledgeNo);
		return R.ok();
	}

	@RequestMapping("/checkKnowledge/{courseId}")
	public R checkKnowledge(@PathVariable("courseId")Long courseId, HttpServletRequest request){
		Assert.isNull(courseId , "课程PK不能为空!");
		return this.csKnowledgeService.checkKnowledge(courseId);
	}
	
	/**
	 * 批量导入
	 * @return
	 */
	@SysLog("初始化导入知识点")
	@RequestMapping("/importKnowledgeExcelInitialization")
	public R importKnowledgeExcelInitialization(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		Long courseId = ServletRequestUtils.getLongParameter(request, "courseId" , 0);
		String importExcel = this.csKnowledgeService.importExcelInitialization(file);
		return R.ok().put(importExcel);
	}
}
