package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.renren.common.doc.SysLog;
import io.renren.dao.MallOrderDao;
import io.renren.entity.MallOrderEntity;
import io.renren.enums.AuditStatusEnum;
import io.renren.pojo.CourseAbnormalOrderPOJO;
import io.renren.service.MallOrderService;
import io.renren.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import io.renren.entity.CourseAbnormalOrderEntity;
import io.renren.service.CourseAbnormalOrderService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 休学失联记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
@RestController
@RequestMapping("courseabnormalorder")
public class CourseAbnormalOrderController extends AbstractController {
	@Autowired
	private CourseAbnormalOrderService courseAbnormalOrderService;

	@Autowired
	private MallOrderService mallOrderService;
	
	@RequestMapping("/courseabnormalorder.html")
	public String list(){
		return "courseabnormalorder/courseabnormalorder.html";
	}
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("courseabnormalorder:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "orderNo");
		stringQuery(map, request, "mallOrderNo");
		stringQuery(map, request, "nickName");
		intQuery(map, request, "studentId");
		stringQuery(map, request, "mobile");
		intQuery(map, request, "classTeacherId");
		intQuery(map, request, "classId");
        intQuery(map, request, "auditStatus");
        intQuery(map, request, "abnormalType");
		stringQuery(map, request, "startTime");
		stringQuery(map, request, "endTime");
		intQuery(map, request, "areaId");
		intQuery(map, request, "professionId");
		intQuery(map, request, "levelId");
		intQuery(map, request, "classTypeId");
		//查询列表数据
		List<CourseAbnormalOrderPOJO> courseAbnormalOrderList = courseAbnormalOrderService.queryList(map);
		int total = courseAbnormalOrderService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseAbnormalOrderList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("courseabnormalorder:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		CourseAbnormalOrderPOJO courseAbnormalOrder = courseAbnormalOrderService.queryObject(id);
		return R.ok().put(courseAbnormalOrder);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("courseabnormalorder:save")
	public R save(@RequestBody CourseAbnormalOrderEntity courseAbnormalOrder , HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("orderId",courseAbnormalOrder.getOrderId());
		//保存
		courseAbnormalOrder.setCreatePerson(getUserId());
		MallOrderEntity mallOrderEntity  = mallOrderService.queryObject(map);
		courseAbnormalOrder.setProductId(mallOrderEntity.getProductId());
		courseAbnormalOrderService.save(courseAbnormalOrder);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("courseabnormalorder:update")
	public R update(@RequestBody CourseAbnormalOrderEntity courseAbnormalOrder , HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("orderId",courseAbnormalOrder.getOrderId());
		//保存
		courseAbnormalOrder.setCreatePerson(getUserId());
		MallOrderEntity mallOrderEntity  = mallOrderService.queryObject(map);
		courseAbnormalOrder.setProductId(mallOrderEntity.getProductId());
		//修改用户
		courseAbnormalOrder.setUpdatePerson(getUserId());
		//修改时间
		courseAbnormalOrder.setUpdateTime(new Date());
		courseAbnormalOrderService.update(courseAbnormalOrder);
		return R.ok();
	}

	/**
	 * 信息
	 */
	@RequestMapping("/auditPass")
	@RequiresPermissions("courseabnormalorder:auditPass")
	public R auditPass(@RequestBody Long[] ids){
		StringBuffer sb = new StringBuffer();
		for (Long id:ids) {
			CourseAbnormalOrderPOJO courseAbnormalOrderPOJO = courseAbnormalOrderService.queryObject(id);
			CourseAbnormalOrderEntity courseAbnormalOrderEntity = new CourseAbnormalOrderEntity();
			courseAbnormalOrderEntity.setId(id);
			courseAbnormalOrderEntity.setModifyPerson(getUserId());
			courseAbnormalOrderEntity.setModifiedTime(new Date());
			courseAbnormalOrderEntity.setAuditStatus(AuditStatusEnum.tongguo.getValue());
			courseAbnormalOrderEntity.setStartTime(courseAbnormalOrderPOJO.getStartTime());
			courseAbnormalOrderEntity.setOrderId(courseAbnormalOrderPOJO.getOrderId());
			try{
				courseAbnormalOrderService.update(courseAbnormalOrderEntity);
			}catch (Exception e){
				sb.append("单号"+courseAbnormalOrderPOJO.getOrderno()+e.getMessage()+";");
			}
		}
		return R.ok().put(sb.toString());
	}

	/**
	 * 复课
	 */
	@RequestMapping("/saveResumeClasses")
	@RequiresPermissions("courseabnormalorder:saveResumeClasses")
	public R saveResumeClasses(@RequestBody CourseAbnormalOrderEntity courseAbnormalOrder , HttpServletRequest request){
		courseAbnormalOrderService.saveResumeClasses(courseAbnormalOrder);
		return R.ok();
	}

	/**
	 * 反审核
	 */
	@RequestMapping("/updateAuditCancel")
	@RequiresPermissions("courseabnormalorder:updateAuditCancel")
	public R updateAuditCancel(@RequestBody Long[] ids){
		courseAbnormalOrderService.updateCancelBatch(AuditStatusEnum.daishenhe.getValue(),ids,getUserId(),new Date());
		return R.ok();
	}

	/**
	 * 作废
	 */
	@RequestMapping("/updateCancel")
	@RequiresPermissions("courseabnormalorder:updateCancel")
	public R updateCancel(@RequestBody Long[] ids){
		courseAbnormalOrderService.updateCancelBatch(AuditStatusEnum.quxiao.getValue(),ids,getUserId(),new Date());
		return R.ok();
	}


	/**
	 * 下载Excel模板
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downExcelTemplate")
	@RequiresPermissions("courseabnormalorder:downExcelTemplate")
	public void downExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String downExcel = courseAbnormalOrderService.downExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "批量导入休学失联信息模板-" + DateUtils.format(new java.util.Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "休学失联信息模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}

	/**
	 * 批量导入
	 * @return
	 */
	@RequiresPermissions("courseabnormalorder:importExcel")
	@SysLog("批量导入休学失联信息")
	@RequestMapping("/importExcel")
	public R importExcel(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.courseAbnormalOrderService.importExcel(getUserId(),file);
		return R.ok().put(importExcel);
	}
}
