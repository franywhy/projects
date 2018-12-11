package io.renren.controller;

import java.util.HashMap;
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
import io.renren.entity.CourseExamScheduleEntity;
import io.renren.pojo.CourseExamSchedulePOJO;
import io.renren.service.CourseExamScheduleService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 课程考试时段档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 18:55:01
 */
@Controller
@RequestMapping("courseexamschedule")
public class CourseExamScheduleController extends AbstractController {
	@Autowired
	private CourseExamScheduleService courseExamScheduleService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseexamschedule:list")
	public R list(String courseName,HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("courseName", courseName);
		//查询列表数据
		List<CourseExamSchedulePOJO> courseExamScheduleList = courseExamScheduleService.queryPojoList(map);
		int total = courseExamScheduleService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseExamScheduleList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{courseExamId}")
	@RequiresPermissions("courseexamschedule:info")
	public R info(@PathVariable("courseExamId") Long courseExamId,HttpServletRequest request){
		Map<String, Object> map = getMap(request);
//		longQuery(map, request, "courseExamId");
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		map.put("courseExamId", courseExamId);
		CourseExamSchedulePOJO courseExamSchedule = courseExamScheduleService.queryPojoObject(map);
		return R.ok().put("courseExamSchedule",courseExamSchedule);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存课程考试时段")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseexamschedule:save")
	public R save(@RequestBody CourseExamScheduleEntity courseExamSchedule , HttpServletRequest request){
		if(StringUtils.isBlank(courseExamSchedule.getCourseId()+"")){
			return R.error("[课程]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getAreaId()+"")){
			return R.error("[省份]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getExamScheduleId()+"")){
			return R.error("[考试时间段]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getExamDate()+"")){
			return R.error("[考试日期]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getExamBucket()+"")){
			return R.error("[考试时段]不能为空！！！");
		}
		if(!StringUtils.isBlank(courseExamSchedule.getRemark()) && courseExamSchedule.getRemark().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		//平台ID
	    courseExamSchedule.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseExamSchedule.setCreatePerson(getUserId());
		//修改用户
		courseExamSchedule.setModifyPerson(courseExamSchedule.getCreatePerson());
		//保存
		courseExamScheduleService.save(courseExamSchedule);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改课程考试时段")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseexamschedule:update")
	public R update(@RequestBody CourseExamScheduleEntity courseExamSchedule , HttpServletRequest request){
		if(StringUtils.isBlank(courseExamSchedule.getCourseId()+"")){
			return R.error("[课程]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getAreaId()+"")){
			return R.error("[省份]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getExamScheduleId()+"")){
			return R.error("[考试时间段]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getExamDate()+"")){
			return R.error("[考试日期]不能为空！！！");
		}
		if(StringUtils.isBlank(courseExamSchedule.getExamBucket()+"")){
			return R.error("[考试时段]不能为空！！！");
		}
		if(!StringUtils.isBlank(courseExamSchedule.getRemark()) && courseExamSchedule.getRemark().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		//平台ID
	    courseExamSchedule.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseExamSchedule.setModifyPerson(getUserId());
		//修改
		courseExamScheduleService.update(courseExamSchedule);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除课程考试时段")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseexamschedule:delete")
	public R delete(@RequestBody Long[] courseExamIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",courseExamIds);
		courseExamScheduleService.deleteBatch(map);	
		return R.ok();
	}
	
}
