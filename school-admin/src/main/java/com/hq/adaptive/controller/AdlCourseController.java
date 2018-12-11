package com.hq.adaptive.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hq.adaptive.entity.AdlCourseEntity;
import com.hq.adaptive.pojo.query.AdlCourseQuery;
import com.hq.adaptive.service.AdlCourseService;

import io.renren.utils.PMapUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.R;




/**
 * 课程
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-08 10:12:52
 */
@RestController
@RequestMapping("/adaptive/adlcourse")
public class AdlCourseController {
	@Autowired
	private AdlCourseService adlCourseService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("adaptive:adlcourse:list")
	public R list(@RequestParam Map<String, Object> params , HttpServletRequest request){
		//查询列表数据
		AdlCourseQuery query = new AdlCourseQuery();
		query.initPage(request);
		query.setCourseId(PMapUtils.getLong(params, "courseId"));
		query.setCourseName(PMapUtils.getString(params, "courseName"));
		query.setCourseNo(PMapUtils.getString(params, "courseNo"));
		query.setSourceId(PMapUtils.getInteger(params, "courseNo"));
		List<AdlCourseEntity> adlCourseList = adlCourseService.queryList(query);
		int total = adlCourseService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(adlCourseList, total, request);
		
		return R.ok().put("data", pageUtil);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{courseId}")
	@RequiresPermissions("adaptive:adlcourse:info")
	public R info(@PathVariable("courseId") Long courseId){
		AdlCourseEntity adlCourse = adlCourseService.queryObject(courseId);
		
		return R.ok().put("adlCourse", adlCourse);
	}

}
