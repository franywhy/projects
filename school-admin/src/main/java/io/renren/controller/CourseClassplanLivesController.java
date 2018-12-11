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
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.service.CourseClassplanLivesService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 排课计划直播明细子表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-26 09:22:46
 */
@Controller
@RequestMapping("courseclassplanlives")
public class CourseClassplanLivesController extends AbstractController {
	@Autowired
	private CourseClassplanLivesService courseClassplanLivesService;
	
	@RequestMapping("/courseclassplanlives.html")
	public String list(){
		return "courseclassplanlives/courseclassplanlives.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseclassplanlives:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//开始时间
		stringQuery(map, request, "startTime");
		//结束时间
		stringQuery(map, request, "endTime");
		//直播课次名称
		stringQuery(map, request, "classplanLiveName");
		//排课计划名称
		stringQuery(map, request, "classplanName");
		//上课时段
		intQuery(map, request, "timeBucket");
		//查询列表数据
		List<ClassplanLivePOJO> courseClassplanLivesList = courseClassplanLivesService.queryPojoList1(map);
		int total = courseClassplanLivesService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseClassplanLivesList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{classplanLiveId}")
	@RequiresPermissions("courseclassplanlives:info")
	public R info(@PathVariable("classplanLiveId") String classplanLiveId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("classplanLiveId", classplanLiveId);
		ClassplanLivePOJO courseClassplanLives = courseClassplanLivesService.queryObject1(map);
		return R.ok().put(courseClassplanLives);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存排课计划直播明细")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseclassplanlives:save")
	public R save(@RequestBody CourseClassplanLivesEntity courseClassplanLives , HttpServletRequest request){
	    //平台ID
	    courseClassplanLives.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseClassplanLives.setCreatePerson(getUserId());
	    //创建时间
		courseClassplanLives.setCreationTime(new Date());
		//保存
		courseClassplanLivesService.save(courseClassplanLives);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改排课计划直播明细")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseclassplanlives:update")
	public R update(@RequestBody CourseClassplanLivesEntity courseClassplanLives , HttpServletRequest request){
		 //平台ID
	    courseClassplanLives.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseClassplanLives.setModifyPerson(getUserId());
		//修改时间
		courseClassplanLives.setModifiedTime(new Date());
		//修改
		courseClassplanLivesService.update(courseClassplanLives);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除排课计划直播明细")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseclassplanlives:delete")
	public R delete(@RequestBody String[] classplanLiveIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",classplanLiveIds);
		courseClassplanLivesService.deleteBatch(map);	
		return R.ok();
	}
	
}
