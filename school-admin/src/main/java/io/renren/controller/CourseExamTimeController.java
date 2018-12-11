package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseExamTimeEntity;
import io.renren.service.CourseExamTimeService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 *  考试倒计时
 * @author xiechaojun
 *2017年5月27日
 */
@Controller
@RequestMapping("courseexamtime")
public class CourseExamTimeController extends AbstractController {
	@Autowired
	private CourseExamTimeService courseExamTimeService;
	
	@RequestMapping("/courseexamtime.html")
	public String list(){
		return "courseexamtime/courseexamtime.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseexamtime:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		List<CourseExamTimeEntity> courseExamTimeList = courseExamTimeService.queryList(map);
		int total = courseExamTimeService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseExamTimeList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("courseexamtime:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		CourseExamTimeEntity courseExamTime = courseExamTimeService.queryObject(map);
		return R.ok().put(courseExamTime);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存考试倒计时")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseexamtime:save")
	public R save(@RequestBody CourseExamTimeEntity courseExamTime , HttpServletRequest request){
	    //平台ID
	    courseExamTime.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseExamTime.setCreatePerson(getUserId());
	    //创建时间
		courseExamTime.setCreationTime(new Date());
		//保存
		courseExamTimeService.save(courseExamTime);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改考试倒计时")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseexamtime:update")
	public R update(@RequestBody CourseExamTimeEntity courseExamTime , HttpServletRequest request){
		 //平台ID
	    courseExamTime.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseExamTime.setModifyPerson(getUserId());
		//修改时间
		courseExamTime.setModifiedTime(new Date());
		//修改
		courseExamTimeService.update(courseExamTime);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除考试倒计时")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseexamtime:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		courseExamTimeService.deleteBatch(map);	
		return R.ok();
	}
	
}
