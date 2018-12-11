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
import io.renren.entity.SysDeptEntity;
import io.renren.service.SysDeptService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 部门管理
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-25 14:21:58
 */
@Controller
@RequestMapping("sysdept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sysdept:list")
	public List<SysDeptEntity> list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		
		//TODO 如果不是超级管理员，则只能查询本部门及子部门数据
		
		//查询列表数据
		List<SysDeptEntity> deptList = sysDeptService.queryList(map);
		return deptList;
	}
	/**
	 * 选择部门(添加、修改菜单)
	 */
	@ResponseBody
	@RequestMapping("/select")
	@RequiresPermissions("sysdept:select")
	public R select(){
		Map<String, Object> map = new HashMap<>();
		
		//TODO 如果不是超级管理员，则只能查询本部门及子部门数据
		
		List<SysDeptEntity> deptList = sysDeptService.queryList(map);
		
		//添加一级部门
		if(getUserId() != null){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("一级部门");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}
		return R.ok().put("deptList", deptList);
	}
	/**
	 * 上级部门id（若是超级管理员上级部门则为0）
	 */
	@ResponseBody
	@RequestMapping("/info")
	@RequiresPermissions("sysdept:list")
	public R info(){
		long deptId = 0;
		
		//TODO 如果不是超级管理员，则只能查询本部门及子部门信息
		
		return R.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sysdept:info")
	public R info(@PathVariable("deptId") Long deptId){
		SysDeptEntity sysDept = sysDeptService.queryObject(deptId);
		return R.ok().put("sysDept", sysDept);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存部门")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("sysdept:save")
	public R save(@RequestBody SysDeptEntity sysDept , HttpServletRequest request){
		//保存
		sysDeptService.save(sysDept);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改部门")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sysdept:update")
	public R update(@RequestBody SysDeptEntity sysDept , HttpServletRequest request){
		//修改
		sysDeptService.update(sysDept);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除部门")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("sysdept:delete")
	public R delete(@RequestBody Long deptId){
		//判断是否有子部门
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return R.error("请先删除子部门");  
		}
		sysDeptService.delete(deptId);	
		return R.ok();
	}
	
}
