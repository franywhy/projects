package com.hq.courses.controller;

import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.pojo.CsCoursePOJO;
import com.hq.courses.pojo.query.CsCourseQuery;
import com.hq.courses.service.CsCourseService;
import io.renren.common.doc.SysLog;
import io.renren.common.validator.Assert;
import io.renren.utils.PMapUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-08 10:12:52
 */
@RestController
@RequestMapping("/courses/cscourse")
public class CsCourseController {
	@Autowired
	private CsCourseService csCourseService;
	
	/**
	 * 课程信息
	 * @param courseId	课程ID
	 * @return
	 */
	@RequestMapping("/info/{courseId}")
	public R info(@PathVariable("courseId")Long courseId , HttpServletRequest request) {
		Assert.isNull(courseId, "课程ID不能为空!");
		CsCoursePOJO csCoursePOJO = this.csCourseService.queryObject(courseId);
		return R.ok().put(csCoursePOJO);
	}
	/**
	 * 校验课程编号
	 * @return
	 */
	@RequestMapping("/checkCourseNo")
	public R checkCourseNo(HttpServletRequest request) {
		String courseNo = ServletRequestUtils.getStringParameter(request, "courseNo" ,null);
		Assert.isBlank(courseNo, "请输入课程编号!");
		Long courseId = ServletRequestUtils.getLongParameter(request, "courseId" , 0);
		this.csCourseService.checkCourseNo(courseId, courseNo);
		return R.ok();
	}
	@SysLog("课程服务_新增课程")
	@RequestMapping("/save")
	public R save(@RequestBody CsCourseEntity csCourse , HttpServletRequest request) {
		this.verifyForm(csCourse);
		this.csCourseService.save(csCourse);
		return R.ok();
	}
	@SysLog("课程服务_修改课程")
	@RequestMapping("/edit")
	public R edit(@RequestBody CsCourseEntity csCourse , HttpServletRequest request) {
		this.verifyForm(csCourse);
		this.csCourseService.update(csCourse);
		return R.ok();
	}
	@SysLog("课程服务_删除课程")
	@RequestMapping("/delete/{courseId}")
	public R delete(@PathVariable("courseId")Long courseId, HttpServletRequest request) {
		Assert.isNull(courseId, "ID不能为空!");
		this.csCourseService.delete(courseId);
		return R.ok();
	}
	/**
	 * 启用
	 */
	@SysLog("课程服务_启用课程")
	@RequestMapping("/enable")
	public R enable(@RequestBody Long courseId){
		Assert.isNull(courseId, "ID不能为空!");
		this.csCourseService.enable(courseId);
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("课程服务_禁用课程")
	@RequestMapping("/unenable")
	public R unenable(@RequestBody Long courseId){
		Assert.isNull(courseId, "ID不能为空!");
		this.csCourseService.unenable(courseId);
		return R.ok();
	}
	
	private void verifyForm(CsCourseEntity csCourse) {
		Assert.isNull(csCourse, "参数错误!");
		Assert.isBlank(csCourse.getCourseName(), "名称不能为空!");
		Assert.isBlank(csCourse.getCourseNo(), "编号不能为空!");
		Assert.isNull(csCourse.getProductId(), "请选择产品线!");
		Assert.isNull(csCourse.getDeptId(), "请选择公司!");
	}
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("courses:course:list")
	public R list(@RequestParam Map<String, Object> params , HttpServletRequest request){
		//查询列表数据
		CsCourseQuery query = new CsCourseQuery();
		query.initPage(request);
		query.setCourseId(PMapUtils.getLong(params, "courseId"));
		query.setCourseName(PMapUtils.getString(params, "courseName"));
		query.setCourseNo(PMapUtils.getString(params, "courseNo"));
		query.setDeptId(PMapUtils.getLong(params, "deptId"));
		query.setProductId(PMapUtils.getLong(params, "productId"));
		query.setStatus(PMapUtils.getInteger(params, "status"));
		List<CsCoursePOJO> coursePOJOList = csCourseService.queryList(query);
		int total = csCourseService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(coursePOJOList, total, request);
		
		return R.ok().put("data", pageUtil);
	}
	
	
	/**
	 * 批量导入
	 * @return
	 */
	@SysLog("课程服务_批量导入课程")
	@RequestMapping("/importCourseExcelInitialization")
	public R importExcel(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = csCourseService.importCourseExcelInitialization( file);
		return R.ok().put(importExcel);
	}
	@SysLog("课程服务_删除课程")
	@RequestMapping("/del")
	public R del(@RequestBody Long courseId){
		this.csCourseService.delete(courseId);
		return R.ok();
	}
}
