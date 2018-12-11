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
import io.renren.entity.CourseUserplanServiceEntity;
import io.renren.service.CourseUserplanServiceService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 服务记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-21 09:16:09
 */
@Controller
@RequestMapping("courseuserplanservice")
public class CourseUserplanServiceController extends AbstractController {
	@Autowired
	private CourseUserplanServiceService courseUserplanServiceService;
	
//	@RequestMapping("/courseuserplanservice.html")
//	public String list(){
//		return "courseuserplanservice/courseuserplanservice.html";
//	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("courseuserplanservice:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//用户ID
		longQuery(map, request, "userplanId");
		//查询列表数据
		List<Map<String, Object>> courseUserplanServiceList = courseUserplanServiceService.queryListMap(map);
		int total = courseUserplanServiceService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseUserplanServiceList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{userplanServiceId}")
	@RequiresPermissions("courseuserplanservice:info")
	public R info(@PathVariable("userplanServiceId")Long userplanServiceId ,HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("userplanServiceId", userplanServiceId);
//		longQuery(map, request, "userplanServiceId");
		Map<String, Object> courseUserplanService = courseUserplanServiceService.queryMap(map);
		return R.ok().put(courseUserplanService);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存服务记录")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("courseuserplanservice:save")
	public R save(@RequestBody CourseUserplanServiceEntity courseUserplanServiceEntity , HttpServletRequest request){
		// 校验
		if (null == courseUserplanServiceEntity.getUserplanId()) {
			return R.error("请选择学员规划!");
		}
		if (null == courseUserplanServiceEntity.getServiceTypeId()) {
			return R.error("请选择服务类型");
		}
		if (null == courseUserplanServiceEntity.getUserplanServiceTime()) {
			return R.error("请输入服务时间");
		}
		if (null == courseUserplanServiceEntity.getServiceTypeContent()) {
			return R.error("请输入服务内容");
		}
		if (null == courseUserplanServiceEntity.getServiceFbId()) {
			return R.error("请选择服务反馈类型");
		}
		if (null == courseUserplanServiceEntity.getServiceFbContent()) {
			return R.error("请选择服务反馈内容");
		}
		//类型默认老师
		courseUserplanServiceEntity.setType(0);
		// 平台ID
		courseUserplanServiceEntity.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 创建用户
		courseUserplanServiceEntity.setCreatePerson(getUserId());
		//创建时间
		courseUserplanServiceEntity.setCreateTime(new Date());
		// 保存
		courseUserplanServiceService.save(courseUserplanServiceEntity);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改服务记录")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("courseuserplanservice:update")
	public R update(@RequestBody CourseUserplanServiceEntity courseUserplanServiceEntity , HttpServletRequest request){
		// 校验
		if (null == courseUserplanServiceEntity.getUserplanId()) {
			return R.error("请选择学员规划!");
		}
		if (null == courseUserplanServiceEntity.getServiceTypeId()) {
			return R.error("请选择服务类型");
		}
		if (null == courseUserplanServiceEntity.getServiceTypeContent()) {
			return R.error("请输入服务内容");
		}
		if (null == courseUserplanServiceEntity.getServiceFbId()) {
			return R.error("请选择服务反馈类型");
		}
		if (null == courseUserplanServiceEntity.getServiceFbContent()) {
			return R.error("请选择服务反馈内容");
		}
		// 平台ID
		// 平台ID
		courseUserplanServiceEntity.setSchoolId(SchoolIdUtils.getSchoolId(request));
		//类型默认老师
		courseUserplanServiceEntity.setType(0);
		//创建用户
		courseUserplanServiceEntity.setModifyPerson(getUserId());
		//创建时间
		courseUserplanServiceEntity.setModifyTime(new Date());
		// 修改
		courseUserplanServiceService.update(courseUserplanServiceEntity);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除服务记录")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("courseuserplanservice:delete")
	public R delete(@RequestBody Long[] userplanServiceIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",userplanServiceIds);
		courseUserplanServiceService.deleteBatch(map);	
		return R.ok();
	}
	
}
