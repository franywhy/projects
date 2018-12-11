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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.constant.DateTimeConstant;
import io.renren.entity.TimeTableDetailEntity;
import io.renren.entity.TimetableEntity;
import io.renren.pojo.timetable.TimeTableCreatePOJO;
import io.renren.pojo.timetable.TimeTableDetailPOJO;
import io.renren.pojo.timetable.TimeTableDisplayPOJO;
import io.renren.pojo.timetable.TimeTablePOJO;
import io.renren.pojo.timetable.TimeTableUpdatePOJO;
import io.renren.service.SysUserService;
import io.renren.service.TimeTableDetailService;
import io.renren.service.TimetableService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.ShiroUtils;

/**
 * 上课时点档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-20 16:43:49
 */
@Controller
@RequestMapping("/mall/timetable")
public class TimetableController extends AbstractController {
	@Autowired
	private TimetableService timetableService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private TimeTableDetailService timeTableDetailService;

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("timetable:list")
	public R list(String name, String productName, Integer page, Integer limit,HttpServletRequest request) {
		Map<String, Object> map = getMapPage(request);
		map.put("name", name);
		map.put("productName", productName);
		//平台id
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		// 查询列表数据
		List<TimeTablePOJO> timetableList = timetableService.queryPojoList(map);
		int total = timetableService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(timetableList, total, limit, page);
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{number}")
	@RequiresPermissions("timetable:info")
	public R info(@PathVariable("number") Long number) {
		TimeTablePOJO timetable = timetableService.queryPojoObject(number);
		List<TimeTableDetailEntity> detailList = timeTableDetailService.queryObject(number);
		return R.ok().put("timetable", timetable).put("detailList", detailList);
		
	}

	/**
	 * 保存
	 */
	@SysLog("保存上课时点")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("timetable:save")
	public R save(@RequestBody TimeTablePOJO timetable,HttpServletRequest request) {
		if(StringUtils.isBlank(timetable.getName())){
			return R.error("[上课时点名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+timetable.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isNotBlank(timetable.getComments()) && timetable.getComments().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		//平台id
		timetable.setSchoolId(SchoolIdUtils.getSchoolId(request));
		//创建人
		timetable.setCreatePerson(getUserId());
		//修改人
		timetable.setUpdatePerson(timetable.getCreatePerson());
		//保存主表
		timetableService.save(timetable);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改上课时点")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("timetable:update")
	public R update(@RequestBody TimeTablePOJO timetable) {
		if(StringUtils.isBlank(timetable.getName())){
			return R.error("[上课时点名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+timetable.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isNotBlank(timetable.getComments()) && timetable.getComments().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		timetable.setUpdatePerson(getUserId());
		timetableService.update(timetable);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除上课时点")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("timetable:delete")
	public R delete(@RequestBody Long[] numbers) {
		timetableService.deleteBatch(numbers);
		timeTableDetailService.deleteBatch(numbers);
		return R.ok();
	}

	/**
	 * 禁用
	 */
	@SysLog("禁用上课时点")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("timetable:pause")
	public R pause(@RequestBody Long[] numbers) {
		timetableService.pause(numbers);
		return R.ok();
	}

	/**
	 * 启用
	 */
	@SysLog("启用上课时点")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("timetable:resume")
	public R resume(@RequestBody Long[] numbers) {
		timetableService.resume(numbers);
		return R.ok();
	}

}
