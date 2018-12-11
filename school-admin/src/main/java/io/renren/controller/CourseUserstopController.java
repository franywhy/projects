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

import io.renren.entity.CourseUserstopEntity;
import io.renren.service.CourseUserstopService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 休学档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-12 10:38:36
 */
@Controller
@RequestMapping("courseuserstop")
public class CourseUserstopController extends AbstractController {
	@Autowired
	private CourseUserstopService courseUserstopService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseuserstop:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "userName");
		stringQuery(map, request, "mobile");
		//查询列表数据
		List<CourseUserstopEntity> courseUserstopList = courseUserstopService.queryList(map);
		int total = courseUserstopService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseUserstopList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("courseuserstop:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		CourseUserstopEntity courseUserstop = courseUserstopService.queryObject(map);
		return R.ok().put(courseUserstop);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
//	@RequiresPermissions("courseuserstop:save")
	public R save(@RequestBody CourseUserstopEntity courseUserstop , HttpServletRequest request){
		//默认状态
	    courseUserstop.setStatus(0);
	    //创建用户
		courseUserstop.setCreatePerson(getUserId());
	    //创建时间
		courseUserstop.setCreationTime(new Date());
		//保存
		courseUserstopService.save(courseUserstop);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
//	@RequiresPermissions("courseuserstop:update")
	public R update(@RequestBody CourseUserstopEntity courseUserstop , HttpServletRequest request){
	    //修改用户
		courseUserstop.setModifyPerson(getUserId());
		//修改时间
		courseUserstop.setModifiedTime(new Date());
		//修改
		courseUserstopService.update(courseUserstop);
		return R.ok();
	}
	
	/**
	 * 审核通过
	 */
	@ResponseBody
	@RequestMapping("/accept")
//	@RequiresPermissions("courseuserstop:accept")
	public R accept(@RequestBody CourseUserstopEntity courseUserstop){
		//审核用户
		courseUserstop.setModifyPerson(getUserId());
		//审核时间
		courseUserstop.setModifiedTime(new Date());
		//通过
		courseUserstop.setStatus(3);
		//审核
		courseUserstopService.update(courseUserstop);
		return R.ok();
	}
	
	/**
	 * 审核未过
	 */
	@ResponseBody
	@RequestMapping("/reject")
//	@RequiresPermissions("courseuserstop:reject")
	public R reject(@RequestBody CourseUserstopEntity courseUserstop){
		//审核用户
		courseUserstop.setModifyPerson(getUserId());
		//审核时间
		courseUserstop.setModifiedTime(new Date());
		//未通过
		courseUserstop.setStatus(2);
		//审核
		courseUserstopService.update(courseUserstop);
		return R.ok();
	}
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
//	@RequiresPermissions("courseuserstop:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		courseUserstopService.deleteBatch(map);	
		return R.ok();
	}
	
}
