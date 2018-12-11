package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.MallExamScheduleEntity;
import io.renren.pojo.examschedule.ExamScheduleCreatePOJO;
import io.renren.pojo.examschedule.ExamScheduleDisplayPOJO;
import io.renren.pojo.examschedule.ExamSchedulePOJO;
import io.renren.pojo.examschedule.ExamScheduleUpdatePOJO;
import io.renren.service.MallExamScheduleService;
import io.renren.service.SysUserService;
import io.renren.utils.Constant;
import io.renren.utils.OccupyException;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.ShiroUtils;


/**
 * 考试时刻表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
@Controller
@RequestMapping("/mall/mallexamschedule")
public class MallExamScheduleController extends AbstractController {
	@Autowired
	private MallExamScheduleService mallExamScheduleService;
	
	//考试世间段弹框
	@ResponseBody
	@RequestMapping("/listGrid")
	public R listGrid(String scheduleName, Integer page, Integer limit,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		map.put("scheduleName", scheduleName);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		//查询列表数据
		List<ExamSchedulePOJO> mallExamScheduleList = mallExamScheduleService.queryPojoList(map);
		int total = mallExamScheduleService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallExamScheduleList, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallexamschedule:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "scheduleName");
		stringQuery(map, request, "productName");
		longQuery(map, request, "productId");
		//查询列表数据
		List<ExamSchedulePOJO> mallExamScheduleList = mallExamScheduleService.queryPojoList(map);
		int total = mallExamScheduleService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallExamScheduleList, total, request);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("mallexamschedule:info")
	public R info(@PathVariable("id") Long id, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		map.put("id", id);
		ExamSchedulePOJO mallExamSchedule = mallExamScheduleService.queryPojoObject(map);
		return R.ok().put("mallExamSchedule", mallExamSchedule);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存考试时刻")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallexamschedule:save")
	public R save(@RequestBody MallExamScheduleEntity mallExamSchedule,HttpServletRequest request){
		if(StringUtils.isBlank(mallExamSchedule.getScheduleName())){
			return R.error("[考试时段名称]不能为空！！！");
		}
		if(StringUtils.isBlank(mallExamSchedule.getScheduleDate())){
			return R.error("[考试年月]不能为空！！！");
		}
		if(StringUtils.isNotBlank(mallExamSchedule.getComments()) && mallExamSchedule.getComments().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		if(null == mallExamSchedule.getProductId() || mallExamSchedule.getProductId() <= 0){
			return R.error("[产品线]必填！");
		}
		mallExamSchedule.setSchoolId(SchoolIdUtils.getSchoolId(request));
		mallExamSchedule.setCreatePerson(getUserId());
		mallExamSchedule.setModifyPerson(mallExamSchedule.getCreatePerson());
		mallExamScheduleService.save(mallExamSchedule);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改考试时刻")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallexamschedule:update")
	public R update(@RequestBody MallExamScheduleEntity mallExamSchedule){
		if(StringUtils.isBlank(mallExamSchedule.getScheduleName())){
			return R.error("[考试时段名称]不能为空！！！");
		}
		if(StringUtils.isBlank(mallExamSchedule.getScheduleDate())){
			return R.error("[考试年月]不能为空！！！");
		}
		if(!StringUtils.isBlank(mallExamSchedule.getComments()) && mallExamSchedule.getComments().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		if(null == mallExamSchedule.getProductId() || mallExamSchedule.getProductId() <= 0){
			return R.error("[产品线]必填！");
		}
		mallExamSchedule.setModifyPerson(getUserId());
		mallExamScheduleService.update(mallExamSchedule);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除考试时刻")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallexamschedule:delete")
	public R delete(@RequestBody Long[] ids){
		try {
			mallExamScheduleService.deleteBatch(ids);
		} catch (OccupyException e) {
			logger.error("e.getMessageis{ }",e);
//			e.printStackTrace();
			return R.error().put(e.getMessage());
		}	
		return R.ok();
	}
	
	
	/**
	 * 禁用
	 */
	@SysLog("禁用考试时刻")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mallexamschedule:pause")
	public R pause(@RequestBody Long[] ids){
		mallExamScheduleService.pause(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用考试时刻")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mallexamschedule:resume")
	public R resume(@RequestBody Long[] ids){
		mallExamScheduleService.resume(ids);
		return R.ok();
	}
	
}
