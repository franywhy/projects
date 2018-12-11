package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.common.validator.Assert;
import io.renren.entity.CourseAbnormalClassplanEntity;
import io.renren.pojo.CourseAbnormalClassplanPOJO;
import io.renren.pojo.query.CourseAbnormalClassplanQuery;
import io.renren.service.CourseAbnormalClassplanService;
import io.renren.utils.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * 弃考免考记录单
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2018-03-17 17:58:51
 */
@RestController
@RequestMapping("/courses/courseabnormalclassplan")
public class CourseAbnormalClassplanController extends  AbstractController{
	@Autowired
	private CourseAbnormalClassplanService courseAbnormalClassplanService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("course:courseabnormalclassplan:list")
	public R list(HttpServletRequest request) throws ServletRequestBindingException {
		//查询列表数据
		CourseAbnormalClassplanQuery query = new CourseAbnormalClassplanQuery();
		query.initPage(request);
		//查询条件
        //学员PK
        String studentId = ServletRequestUtils.getStringParameter(request, "studentId");
        //学员手机号
        String studentMobile = ServletRequestUtils.getStringParameter(request, "studentMobile");
        //单号
        String abnormalClassplanNo = ServletRequestUtils.getStringParameter(request, "abnormalClassplanNo");
        //异常类型(3.弃考 4.免考)
        String abnormalType = ServletRequestUtils.getStringParameter(request, "abnormalType");
        //订单号
        String orderNo = ServletRequestUtils.getStringParameter(request, "orderNo");
        //课程PK
        String courseId = ServletRequestUtils.getStringParameter(request, "courseId");
//        排课PK
        String classplanId = ServletRequestUtils.getStringParameter(request, "classplanId");
        //审核状态(0-审核中 1-取消  2-通过)
        String auditStatus = ServletRequestUtils.getStringParameter(request, "auditStatus");

        if(StringUtils.isNotBlank(studentId)){
            query.setStudentId(Long.valueOf(studentId));
        }
        if(StringUtils.isNotBlank(studentMobile)){
            query.setStudentMobile(studentMobile);
        }
        if(StringUtils.isNotBlank(abnormalClassplanNo)){
            query.setAbnormalClassplanNo(abnormalClassplanNo);
        }
        if(StringUtils.isNotBlank(abnormalType)){
            query.setAbnormalType(Integer.valueOf(abnormalType));
        }
        if(StringUtils.isNotBlank(classplanId)){
            query.setClassplanId(classplanId);
        }
        if(StringUtils.isNotBlank(auditStatus)){
            query.setAuditStatus(Integer.valueOf(auditStatus));
        }
        List<CourseAbnormalClassplanPOJO> courseAbnormalClassplanPOJOS = this.courseAbnormalClassplanService.queryList(query);
		int total = this.courseAbnormalClassplanService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(courseAbnormalClassplanPOJOS, total, request);
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("course:courseabnormalclassplan:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request) {
        CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO = courseAbnormalClassplanService.queryObject(id);

        return R.ok().put(courseAbnormalClassplanPOJO);
	}
	
	/**
	 * 保存
	 */
	@SysLog("弃考免考单-新增")
	@RequestMapping("/save")
	@RequiresPermissions("course:courseabnormalclassplan:save")
	public R save(@RequestBody CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO){
		this.verifyForm(courseAbnormalClassplanPOJO);
		courseAbnormalClassplanPOJO.setCreatePerson(getUserId());
		courseAbnormalClassplanPOJO.setUpdatePerson(getUserId());
		courseAbnormalClassplanService.save(courseAbnormalClassplanPOJO);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("弃考免考单-修改")
	@RequestMapping("/update")
	@RequiresPermissions("course:courseabnormalclassplan:edit")
	public R update(@RequestBody CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO){
		this.verifyForm(courseAbnormalClassplanPOJO);
		courseAbnormalClassplanPOJO.setUpdatePerson(getUserId());
		courseAbnormalClassplanService.update(courseAbnormalClassplanPOJO);
		return R.ok();
	}

	/**
	 * 取消
	 */
	@SysLog("弃考免考单-取消")
	@RequestMapping("/updateCancel")
	@RequiresPermissions("course:courseabnormalclassplan:updateCancel")
	public R updateCancel(@RequestBody Long id){
		Assert.isNull(id , "请选择一条记录");
		courseAbnormalClassplanService.updateCancle(id);
		return R.ok();
	}

	/**
	 * 弃考免考单-审核通过
	 */
	@SysLog("弃考免考单-审核通过")
	@RequestMapping("/updateAdopt")
	@RequiresPermissions("course:courseabnormalclassplan:updateAdopt")
	public R updateAdopt(@RequestBody Long id){
		Assert.isNull(id , "请选择一条记录");
		courseAbnormalClassplanService.updateAdopt(id , getUserId());
		return R.ok();
	}

	/**
	 * 下载Excel课程模板
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downExcelTemplateCourse")
	public void downExcelTemplateCourse(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String cellsStr = new String(Constant.COURSE_ABNORMAL_CLASSPLAN_EXCEL_TEMPAL_COURSE.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "课程模板-" + DateUtils.format(new java.util.Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "课程模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}

//	/**
//	 * 下载Excel排课计划模板
//	 * @return
//	 * @throws IOException
//	 */
//	@RequestMapping("/downExcelTemplateClassplan")
//	public void downExcelTemplateClassplan(HttpServletRequest request,HttpServletResponse response) throws IOException {
//		String cellsStr = new String(Constant.COURSE_ABNORMAL_CLASSPLAN_EXCEL_TEMPAL_CLASSPLAN.getBytes("GBK"), "GBK");
//		String[] cells = cellsStr.split("&");
//		String filename = "排课计划模板-" + DateUtils.format(new java.util.Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
//		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
//		response.setContentType("text/html; charset=UTF-8");
//		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
//		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//		ExcelReaderJXL.exportToJxlExcel(filename, "排课计划模板", cells, toClient);
//		toClient.flush();
//		toClient.close();
//	}
	/**
	 * 弃考免考单-批量上传-课程模板
	 */
	@SysLog("弃考免考单-批量上传-课程模板")
	@RequiresPermissions("course:courseabnormalclassplan:importExcelTemplateCourse")
	@RequestMapping("/importExcelTemplateCourse")
	public R importExcelTemplateCourse(HttpServletRequest request,HttpServletResponse response){
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.courseAbnormalClassplanService.importExcelTemplateCourse(file , getUserId());

		return R.ok().put(importExcel);
	}

	/**
	 * 字段类型校验
	 * @param courseAbnormalClassplanPOJO
	 */
	private void verifyForm(CourseAbnormalClassplanPOJO courseAbnormalClassplanPOJO) {
		Assert.isNull(courseAbnormalClassplanPOJO , "CourseAbnormalClassplanEntity对象错误!");
		Assert.isNull(courseAbnormalClassplanPOJO.getStudentId() , "请选择学员！");
		Assert.isNull(courseAbnormalClassplanPOJO.getAbnormalType() , "请选择异常类型！");
		Assert.isNull(courseAbnormalClassplanPOJO.getClassplanId() , "请选择排课计划！");
	}
}
