package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseAbnormalFreeAssessmentEntity;
import io.renren.entity.CourseUserplanEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.pojo.CourseAbnormalFreeAssessmentPOJO;
import io.renren.service.CourseAbnormalFreeAssessmentService;
import io.renren.service.impl.CourseUserplanDetailServiceImpl;
import io.renren.service.impl.CourseUserplanServiceImpl;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 休学失联记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
@RestController
@RequestMapping("courseAbnormalFreeAssessment")
public class CourseAbnormalFreeAssessmentController extends AbstractController {
	@Autowired
	private CourseAbnormalFreeAssessmentService courseAbnormalFreeAssessmentService;
	@Autowired
	private CourseUserplanServiceImpl courseUserplanService;

	@Autowired
	private CourseUserplanDetailServiceImpl courseUserplanDetailService;
	
	/**
	 * 列表 
	 */
	@RequestMapping("/list")
	@RequiresPermissions("courseAbnormalFreeAssessment:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "orderNo");
		stringQuery(map, request, "mallOrderNo");
		stringQuery(map, request, "nickName");
		stringQuery(map, request, "courseName");
		stringQuery(map, request, "mobile");
		intQuery(map, request, "classTeacherId");
		intQuery(map, request, "classId");
        intQuery(map, request, "auditStatus");
		stringQuery(map, request, "startTime");
		stringQuery(map, request, "endTime");
		intQuery(map, request, "areaId");
		intQuery(map, request, "professionId");
		intQuery(map, request, "levelId");
		intQuery(map, request, "classTypeId");
		//查询列表数据
		List<CourseAbnormalFreeAssessmentPOJO> courseAbnormalFreeAssessmentList = courseAbnormalFreeAssessmentService.queryList(map);
		int total = courseAbnormalFreeAssessmentService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseAbnormalFreeAssessmentList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("courseAbnormalFreeAssessment:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		CourseAbnormalFreeAssessmentPOJO courseAbnormalFreeAssessment = courseAbnormalFreeAssessmentService.queryObject(id);
		return R.ok().put(courseAbnormalFreeAssessment);
	}
	

	/**
	 * 批量审核通过
	 */
	@RequestMapping("/auditPass")
	@RequiresPermissions("courseAbnormalFreeAssessment:auditPass")
	public R auditPass(@RequestBody Long[] ids){
		courseAbnormalFreeAssessmentService.updateAuditPassBatch(AuditStatusEnum.tongguo.getValue(),ids,getUserId(),new Date());
		return R.ok();
	}

	/**
	 * 批量反审核
	 */
	@RequestMapping("/auditPassCancel")
	@RequiresPermissions("courseAbnormalFreeAssessment:auditPassCancel")
	public R auditPassCancel(@RequestBody Long[] ids){
		courseAbnormalFreeAssessmentService.updateCancelBatch(AuditStatusEnum.daishenhe.getValue(),ids,getUserId(),new Date());
		return R.ok();
	}

	/**
	 * 作废
	 */
	@RequestMapping("/updateCancel")
	@RequiresPermissions("courseAbnormalFreeAssessment:updateCancel")
	public R updateCancel(@RequestBody Long[] ids , HttpServletRequest request){
		courseAbnormalFreeAssessmentService.updateCancelBatch(AuditStatusEnum.quxiao.getValue(),ids,getUserId(),new Date());
		return R.ok();
	}


	/**
	 * 下载Excel模板
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downExcelTemplate")
	@RequiresPermissions("courseAbnormalFreeAssessment:downExcelTemplate")
	public void downExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String downExcel = courseAbnormalFreeAssessmentService.downExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "批量导入免考信息模板-" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "批量导入免考信息模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}

	/**
	 * 批量导入
	 * @return
	 */
	@RequiresPermissions("courseAbnormalFreeAssessment:importExcel")
	@SysLog("批量导入免考信息")
	@RequestMapping("/importExcel")
	public R importExcel(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.courseAbnormalFreeAssessmentService.importExcel(getUserId(),file);
		return R.ok().put(importExcel);
	}


	@RequestMapping("/courseListByOrderId")
//	@RequiresPermissions("courseAbnormalFreeAssessment:courseListByOrderId")
	public R courseListByOrderId(HttpServletRequest request){
		String orderId = request.getParameter("orderId") ;
		String userPlanId = courseUserplanService.queryUserPlanId(orderId);
		if(userPlanId != null){
			Map<String, Object> map = getMapPage(request);
			map.put("userPlanId",userPlanId);
            map.put("targetGrade",true);
            map.put("examEn2",true);

			List<Map<String , Object>> list = this.courseUserplanDetailService.courseListByUserPlanId(map);
			int total = courseUserplanDetailService.courseTotalByUserPlanId(map);
			PageUtils pageUtil = new PageUtils(list, total, request);
			return R.ok().put("page", pageUtil);
		}
		return R.ok();
	}
}
