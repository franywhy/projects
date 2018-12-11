package io.renren.controller;


import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.stereotype.Controller;
import io.renren.common.doc.SysLog;
import io.renren.dao.CourseAbnormalRegistrationDao;
import io.renren.entity.CourseAbnormalAbandonExamEntity;
import io.renren.entity.CourseAbnormalRegistrationEntity;
import io.renren.entity.ExamEntityTemp;
import io.renren.enums.AuditStatusEnum;
import io.renren.pojo.CourseAbnormalAbandonExamPOJO;
import io.renren.pojo.CourseAbnormalRegistrationPOJO;
import io.renren.pojo.CourseAbnormalRegistrationPOJO1;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseAbnormalAbandonExamService;
import io.renren.service.CourseAbnormalRegistrationService;
import io.renren.utils.DateUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.RRException;



/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
@RestController
@RequestMapping("courseabnormalabandonexam")
public class CourseAbnormalAbandonExamController extends AbstractController {
	@Autowired
	private CourseAbnormalAbandonExamService courseAbnormalAbandonExamService;
	
	@Resource
	KGS invoicesNumberKGS;	
	private static final String INVOICESNUMBER_HEAD = "HQQK";
	
	@RequestMapping("/courseabnormalabandonexam.html")
	public String list(){
		return "courseabnormalabandonexam/courseabnormalabandonexam.html";
	}
	
	/**
	 * 列表
	 */	
	@RequestMapping("/list")
	@RequiresPermissions("courseabnormalabandonexam:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//查询条件		
		//班型名称
		stringQueryDecodeUTF8(map, request, "classTypeName");		
		//班级名称
		stringQueryDecodeUTF8(map, request, "className");
		//班主任名称
		stringQueryDecodeUTF8(map, request, "teacherName");
		//课程名称
		stringQueryDecodeUTF8(map, request, "courseName");
		//专业名称
		stringQueryDecodeUTF8(map, request, "professionName");
		//报读省份
		stringQueryDecodeUTF8(map, request, "applyProvince");
		//报考省份
		stringQueryDecodeUTF8(map, request, "registerProvince");
		//考试时间段
		stringQueryDecodeUTF8(map, request, "scheduleName");
		//学员姓名
		stringQueryDecodeUTF8(map, request, "studentName");
		//手机号
		stringQueryDecodeUTF8(map, request, "mobile");
		//状态
		intQuery(map, request, "status");
		//层次
		stringQueryDecodeUTF8(map, request, "levelName");
		//查询列表数据				
		List<CourseAbnormalAbandonExamPOJO> queryPOJOList = courseAbnormalAbandonExamService.queryPOJOList(map);
		int total = courseAbnormalAbandonExamService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(queryPOJOList, total , request);	
		return R.ok().put("data",pageUtil);
	}
	
	
	/**
	 * 信息
	 */	
	@RequestMapping("/info/{id}")
	@RequiresPermissions("courseabnormalabandonexam:info")
	public R info(@PathVariable("id") Long id){		
		CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam = courseAbnormalAbandonExamService.queryObject(id);
		return R.ok().put("courseAbnormalAbandonExam",courseAbnormalAbandonExam);
	}
		
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")	
	@RequiresPermissions("courseabnormalabandonexam:save")
	public R save(@RequestBody ExamEntityTemp eet,HttpServletRequest request){
		//需要保存的字段:单据号，产品线，报考id,原因，备注			
		if (StringUtils.length(eet.getReason()) > 100) {
			return R.error("原因的长度要小于100！！！");
		}
		if (StringUtils.length(eet.getRemark()) > 100) {
			return R.error("备注的长度要小于100！！！");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		String registrationNo = eet.getRegistrationNo();
		map.put("registrationNo", registrationNo);
		CourseAbnormalRegistrationPOJO1 queryByRegistrationNo = courseAbnormalAbandonExamService.queryByRegistrationNo(map);
		CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam = new CourseAbnormalAbandonExamEntity();								
		courseAbnormalAbandonExam.setRegistrationId(queryByRegistrationNo.getId());			
		courseAbnormalAbandonExam.setProductId(queryByRegistrationNo.getProductId());		
		//弃考单据号			
		courseAbnormalAbandonExam.setInvoicesNumber(INVOICESNUMBER_HEAD+invoicesNumberKGS.nextId());
		//默认状态 新增的状态是待审核
	    courseAbnormalAbandonExam.setStatus(AuditStatusEnum.tongguo.getValue());
	    //创建用户
		courseAbnormalAbandonExam.setCreater(getUserId());
		courseAbnormalAbandonExam.setUpdatePerson(getUserId());
	    //创建时间
		courseAbnormalAbandonExam.setCreateTime(new Date());
		courseAbnormalAbandonExam.setUpdateTime(new Date());
		courseAbnormalAbandonExam.setReason(eet.getReason());
		courseAbnormalAbandonExam.setRemark(eet.getRemark());
		//保存
		String saveExamEntity = courseAbnormalAbandonExamService.saveExamEntity(courseAbnormalAbandonExam);	
		return R.ok().put(saveExamEntity);
	}
	
	
	
	/**
	 * 作废状态
	 */	
	@RequestMapping("/cancel")
	public R cancel(@RequestBody Long[] ids){		
		for (int i = 0; i < ids.length; i++) {
			CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam = courseAbnormalAbandonExamService.queryObject(ids[i]);
			if(StringUtils.isNotBlank(courseAbnormalAbandonExam.getInvoicesNumber())){
				if(courseAbnormalAbandonExam.getStatus() == 1){
					return R.error("该弃考订单已经作废，不能进行任何操作！");
				}
			}
		}
		courseAbnormalAbandonExamService.cancel(AuditStatusEnum.quxiao.getValue(), ids, getUserId(), new Date());	
		return R.ok();
	}
	/**
	 * 反审核
	 * @param ids
	 * @return
	 */	
	@RequestMapping("/antiAudit")
	public R antiAudit(@RequestBody Long[] ids){		
		courseAbnormalAbandonExamService.cancel(AuditStatusEnum.daishenhe.getValue(), ids, getUserId(), new Date());	
		return R.ok();
	}
	
	
	/**
	 *  审核状态
	 */	
	@RequestMapping("/pass")
	public R pass(@RequestBody Long[] ids){		
		for (int i = 0; i < ids.length; i++) {
			CourseAbnormalAbandonExamEntity courseAbnormalAbandonExam = courseAbnormalAbandonExamService.queryObject(ids[i]);
			if(StringUtils.isNotBlank(courseAbnormalAbandonExam.getInvoicesNumber())){
				if(courseAbnormalAbandonExam.getStatus() == 2){
					return R.error("该弃考订单已经审核，无需重新审核！");
				}
			}
		}
		//状态 0未审核 1 作废 2审核通过					
		courseAbnormalAbandonExamService.pass(AuditStatusEnum.tongguo.getValue(), ids, getUserId(), new Date());
		return R.ok();			
	}
	
		
	/**
	 * 下载Excel模板
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/downTemplate")
	public void downExcelTemplate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String downExcel = courseAbnormalAbandonExamService.downExcel();
		String cellsStr = new String(downExcel.getBytes("GBK"), "GBK");
		String[] cells = cellsStr.split("&");
		String filename = "批量导入弃考信息模板-" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ".xls";
		filename = new String(filename.getBytes("GBK"), "iso-8859-1");
		response.setContentType("text/html; charset=UTF-8");
		response.addHeader("Content-Disposition",(new StringBuilder()).append("attachment;filename=").append(filename + ";").toString());
		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
		ExcelReaderJXL.exportToJxlExcel(filename, "批量导入弃考信息模板", cells, toClient);
		toClient.flush();
		toClient.close();
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SysLog("批量导入弃考信息")	
	@RequestMapping("/importExcel")
	public R importExcel(HttpServletRequest request,HttpServletResponse response) {
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		String importExcel = this.courseAbnormalAbandonExamService.importExcel(getUserId(),file);
		return R.ok().put(importExcel);
	}
	
	
	
	
	
	
}
