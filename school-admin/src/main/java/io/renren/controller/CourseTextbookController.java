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
import io.renren.entity.CourseTextbookDetailEntity;
import io.renren.entity.CourseTextbookEntity;
import io.renren.service.CourseTextbookService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 教材档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 16:09:29
 */
@Controller
@RequestMapping("coursetextbook")
public class CourseTextbookController extends AbstractController {
	@Autowired
	private CourseTextbookService courseTextbookService;
	
	@RequestMapping("/coursetextbook.html")
	public String list(){
		return "course/coursetextbook.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("coursetextbook:list")
	public R list(HttpServletRequest request,String textbookName,String courseName){
		Map<String, Object> map = getMapPage(request);
	    map.put("textbookName",textbookName);
	    map.put("courseName",courseName);
		//查询列表数据
		List<CourseTextbookDetailEntity> courseTextbookList = courseTextbookService.queryList(map);
		int total = courseTextbookService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseTextbookList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{textbookId}")
	@RequiresPermissions("coursetextbook:info")
	public R info(@PathVariable("textbookId") Long textbookId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("textbookId", textbookId);
		longQuery(map, request, "textbookId");
		//map.put("schoolId",SchoolIdUtils.getSchoolId(request));
		CourseTextbookDetailEntity courseTextbook = courseTextbookService.queryObject(map);
		return R.ok().put(courseTextbook);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存教材档案")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("coursetextbook:save")
	public R save(@RequestBody CourseTextbookEntity courseTextbook , HttpServletRequest request){
		//默认状态
	    courseTextbook.setStatus(1);
	    //教材名称
	    if(courseTextbook.getTextbookName()==null){
	    	return R.error("教材名称不能为空");
	    }
	    courseTextbook.setTextbookName(courseTextbook.getTextbookName());
	    //关联课程编号
	    if(courseTextbook.getCourseId()==null){
	    	return R.error("课程编号不能为空");
	    }
	    courseTextbook.setCourseId(courseTextbook.getCourseId());
	    //备注
	    if(courseTextbook.getRemark()==null){
	    	return R.error("备注不能为空");
	    }
	    courseTextbook.setRemark(courseTextbook.getRemark());
	    //平台ID
	    courseTextbook.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseTextbook.setCreatePerson(getUserId());
		//修改用户
		courseTextbook.setModifyPerson(getUserId());
	    //创建时间
		courseTextbook.setCreationTime(new Date());
		//修改时间
		courseTextbook.setModifiedTime(new Date());
		//保存
		try{
			courseTextbookService.save(courseTextbook);	
		}catch(Exception e){
			return R.error("教材名称已存在");
		}
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改教材档案")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("coursetextbook:update")
	public R update(@RequestBody CourseTextbookEntity courseTextbook , HttpServletRequest request){
		//默认状态
	    courseTextbook.setStatus(1);
	    //教材名称
	    if(courseTextbook.getTextbookName()==null){
	    	return R.error("教材名称不能为空");
	    }
	    courseTextbook.setTextbookName(courseTextbook.getTextbookName());
	    //关联课程编号
	    if(courseTextbook.getCourseId()==null){
	    	return R.error("课程编号不能为空");
	    }
	    courseTextbook.setCourseId(courseTextbook.getCourseId());
	    //备注
	    if(courseTextbook.getRemark()==null){
	    	return R.error("备注不能为空");
	    }
	    courseTextbook.setRemark(courseTextbook.getRemark());
	    //平台ID
	    courseTextbook.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseTextbook.setCreatePerson(getUserId());
		//修改用户
		courseTextbook.setModifyPerson(getUserId());
	    //创建时间
		//courseTextbook.setCreationTime(new Date());
		//修改时间
		courseTextbook.setModifiedTime(new Date());
		//修改
		courseTextbookService.update(courseTextbook);
		return R.ok();
	}
	/**
	 * 禁用
	 */
	@SysLog("禁用教材档案")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("webbanner:pause")
	public R pause(@RequestBody Long[] ids){
		courseTextbookService.pause(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用教材档案")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("webbanner:resume")
	public R resume(@RequestBody Long[] ids){
		courseTextbookService.resume(ids);
		return R.ok();
	}
	/**
	 * 删除
	 */
	@SysLog("删除教材档案")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("coursetextbook:delete")
	public R delete(@RequestBody Long[] textbookIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",textbookIds);
		courseTextbookService.deleteBatch(map);	
		return R.ok();
	}
	
}
