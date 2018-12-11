package io.renren.controller;

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
import io.renren.entity.SysSchoolEntity;
import io.renren.service.SysSchoolService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 校区管理档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-29 10:09:52
 */
@Controller
@RequestMapping("sysschool")
public class SysSchoolController extends AbstractController {
	@Autowired
	private SysSchoolService sysSchoolService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sysschool:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "schoolName");
		//查询列表数据
		List<SysSchoolEntity> sysSchoolList = sysSchoolService.queryList(map);
		int total = sysSchoolService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(sysSchoolList, total, request);
		
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sysschool:info")
	public R info(@PathVariable("id") Long id ,  HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		SysSchoolEntity sysSchool = sysSchoolService.queryObject(map);
		
		return R.ok().put(sysSchool);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
//	@RequiresPermissions("sysschool:save")
	public R save(@RequestBody SysSchoolEntity sysSchool){
		sysSchoolService.save(sysSchool);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改校区管理档案")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sysschool:update")
	public R update(@RequestBody SysSchoolEntity sysSchool ){
		sysSchoolService.update(sysSchool);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除校区管理档案")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("sysschool:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		sysSchoolService.deleteBatch(map);
		
		return R.ok();
	}
	
}
