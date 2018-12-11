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

import io.renren.common.doc.ParamKey;
import io.renren.common.doc.SysLog;
import io.renren.entity.MallMarketCourseEntity;
import io.renren.pojo.MallMarketCoursePOJO;
import io.renren.service.MallMarketCourseService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-19 11:58:28
 */
@Controller
@RequestMapping("mallmarketcourse")
public class MallMarketCourseController extends AbstractController {
	@Autowired
	private MallMarketCourseService mallMarketCourseService;
	
	/**
	 * 分类下拉列表
	 */
	@ResponseBody
	@RequestMapping("/classifyDownList")
	public R classifyDownList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		return R.ok().put(ParamKey.Out.data, this.mallMarketCourseService.queryClassifyDownList(map));
	}
	
	/**
	 * 分类列表
	 */
	@ResponseBody
	@RequestMapping("/classifyList")
	@RequiresPermissions("mallmarketcourse:classifylist")
	public R classifyList(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "classifyName");
		stringQuery(map, request, "productName");
		//查询列表数据
		List<MallMarketCoursePOJO> mallMarketCourseClassifyList = mallMarketCourseService.queryListClassifyPOJO(map);
		int total = mallMarketCourseService.queryTotalClassify(map);
		PageUtils pageUtil = new PageUtils(mallMarketCourseClassifyList, total , request);	
		return R.ok().put(pageUtil);
	}
	/**
	 * 课程列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallmarketcourse:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "parentName");
		stringQuery(map, request, "titleName");
		stringQuery(map, request, "productName");
		//查询列表数据
		List<MallMarketCoursePOJO> mallMarketCourseList = mallMarketCourseService.queryListPOJO(map);
		int total = mallMarketCourseService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallMarketCourseList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("mallmarketcourse:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		MallMarketCoursePOJO mallMarketCourse = mallMarketCourseService.queryObjectPOJO(map);
		return R.ok().put(mallMarketCourse);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存营销课程分类档案")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallmarketcourse:save")
	public R save(@RequestBody MallMarketCourseEntity mallMarketCourse , HttpServletRequest request){
	    //创建用户
		mallMarketCourse.setCreatePerson(getUserId());
	    //创建时间
		mallMarketCourse.setCreateTime(new Date());
		//保存
		mallMarketCourseService.save(mallMarketCourse);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改营销课程分类档案")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallmarketcourse:update")
	public R update(@RequestBody MallMarketCourseEntity mallMarketCourse , HttpServletRequest request){
	    //修改用户
		mallMarketCourse.setModifyPerson(getUserId());
		//修改时间
		mallMarketCourse.setModifyTime(new Date());
		//修改
		mallMarketCourseService.update(mallMarketCourse);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除营销课程分类档案")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallmarketcourse:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		mallMarketCourseService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用营销课程分类档案")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mallmarketcourse:pause")
	public R pause(@RequestBody Long[] ids , HttpServletRequest request){
		mallMarketCourseService.pause(ids);	
		return R.ok();
	}
	/**
	 * 启用
	 */
	@SysLog("启用营销课程分类档案")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mallmarketcourse:resume")
	public R resume(@RequestBody Long[] ids , HttpServletRequest request){
		mallMarketCourseService.resume(ids);	
		return R.ok();
	}
}
