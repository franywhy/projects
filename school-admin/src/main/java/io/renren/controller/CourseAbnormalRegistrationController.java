package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseAbnormalRegistrationEntity;
import io.renren.entity.SysUserEntity;
import io.renren.pojo.CourseAbnormalRegistrationPOJO;
import io.renren.service.CourseAbnormalRegistrationService;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.ShiroUtils;




/**
 * 报考档案表
 * 
 * @author vince
 * @date 2018-03-28 09:21:52
 */
@RestController
@RequestMapping("courseabnormalregistration")
public class CourseAbnormalRegistrationController extends AbstractController{
	
	@Autowired
	private CourseAbnormalRegistrationService courseAbnormalRegistrationService;
	
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}
	/**
	 * 列表
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("generator:courseabnormalregistration:list")
	public R list(HttpServletRequest request){
		//查询列表数据
//        Query query = new Query(params);
		Map<String, Object> query = getMapPage(request);
		List<CourseAbnormalRegistrationEntity> courseAbnormalRegistrationList = courseAbnormalRegistrationService.queryList(query);
		int total = courseAbnormalRegistrationService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(courseAbnormalRegistrationList, total, request);
		
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("generator:courseabnormalregistration:info")
	public R info(@PathVariable("id") Long id){
		CourseAbnormalRegistrationEntity courseAbnormalRegistration = courseAbnormalRegistrationService.queryObject(id);
		
		return R.ok().put("courseAbnormalRegistration", courseAbnormalRegistration);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("generator:courseabnormalregistration:save")
	public R save(@RequestBody CourseAbnormalRegistrationEntity courseAbnormalRegistration){
		if(StringUtils.isNotBlank(courseAbnormalRegistration.getRegistrationNumber())){
			if(courseAbnormalRegistration.getRegistrationNumber().length()>50){
				return R.ok().put("报考登记号长度超过限制");
			}
		}
		SysUserEntity sysUserEntity = getUser();
		courseAbnormalRegistration.setCreater(sysUserEntity.getUserId());
		courseAbnormalRegistration.setUpdatePerson(sysUserEntity.getUserId());
		courseAbnormalRegistration.setStatus(2);
		String message = courseAbnormalRegistrationService.saveEntity(courseAbnormalRegistration);
		return R.ok().put(message);
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("generator:courseabnormalregistration:update")
	public R update(@RequestBody CourseAbnormalRegistrationEntity courseAbnormalRegistration){
//		courseAbnormalRegistrationService.update(courseAbnormalRegistration);
		return R.ok();
	}
	
	
	
	/**
	 * 查询报考列表数据
	 */
	@RequestMapping("/queryRegistrationList")
//	@RequiresPermissions("generator:courseabnormalregistration:list")
	public R queryRegistrationList(HttpServletRequest request){
		//查询列表数据
		Map<String, Object> query = getMapPage(request);
		longQuery(query, request, "classtypeId");
		longQuery(query, request, "scheduleDateId");
		longQuery(query, request, "sysUserId");
		stringQuery(query, request, "nickName");
		longQuery(query, request, "classId");
		longQuery(query, request, "mobile");
		longQuery(query, request, "courseId");
		longQuery(query, request, "status");
		longQuery(query, request, "professionId");
		longQuery(query, request, "levelId");
		longQuery(query, request, "bdAreaId");
		longQuery(query, request, "bkAreaId");
		query.put("dr", 0);
		List<CourseAbnormalRegistrationPOJO> registrationList = courseAbnormalRegistrationService.queryRegistrationList(query);
		
		int total = courseAbnormalRegistrationService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(registrationList, total, request);
		
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 作废
	 * @param registrationIds
	 * @return
	 */
	@RequestMapping("/cancellation")
//	@RequiresPermissions("generator:courseabnormalregistration:update")
	public R cancellation(@RequestBody Long[] registrationIds){
		courseAbnormalRegistrationService.cancellation(registrationIds);
		return R.ok();
	}
	
	/**
	 * 审核通过
	 * @param registrationIds
	 * @return
	 */
	@RequestMapping("/audit")
//	@RequiresPermissions("generator:courseabnormalregistration:update")
	public R audit(@RequestBody Long[] registrationIds){
		String message = courseAbnormalRegistrationService.audit(registrationIds);
		return R.ok().put(message);
	}
	
	/**
	 * 反审核
	 * @param registrationIds
	 * @return
	 */
	@RequestMapping("/opposeAudit")
//	@RequiresPermissions("generator:courseabnormalregistration:update")
	public R opposeAudit(@RequestBody Long[] registrationIds){
		String message = courseAbnormalRegistrationService.opposeAudit(registrationIds);
		return R.ok().put(message);
	}
	
	
	/**
	 * 弹窗列表
	 */
	@RequestMapping("/registrationLayList")
//	@RequiresPermissions("generator:courseabnormalregistration:registrationLayList")
	public R registrationLayList(HttpServletRequest request){
		//查询列表数据
//        Query query = new Query(params);
		Map<String, Object> query = getMapPage(request);
		query.put("status", 2);
		List<CourseAbnormalRegistrationPOJO> registrationList = courseAbnormalRegistrationService.queryRegistrationLayList(query);
		int total = courseAbnormalRegistrationService.queryLayTotal(query);
		PageUtils pageUtil = new PageUtils(registrationList, total, request);
		return R.ok().put(pageUtil);
	}
	
	/**
	 * 下载Excel模板
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downExcelTemplate")
	public void downExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String downExcel = courseAbnormalRegistrationService.downExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "批量导入报考信息模板-" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "批量导入报考信息模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}
	
	/**
	 * 批量导入
	 * @return
	 */
	/*@RequiresPermissions("courseAbnormalFreeAssessment:importExcel")*/
	@SysLog("批量导入免考信息")
	@RequestMapping("/importExcel")
	public R importExcel(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.courseAbnormalRegistrationService.importExcel(getUserId(),file);
		return R.ok().put(importExcel);
	}
}
