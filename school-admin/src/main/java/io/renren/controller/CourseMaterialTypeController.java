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
import io.renren.entity.CourseMaterialTypeEntity;
import io.renren.service.CourseMaterialTypeService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 资料库类型
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 17:42:53
 */
@Controller
@RequestMapping("coursematerialtype")
public class CourseMaterialTypeController extends AbstractController {
	@Autowired
	private CourseMaterialTypeService courseMaterialTypeService;
	
	@RequestMapping("/coursematerialtype.html")
	public String list(){
		return "coursematerialtype/coursematerialtype.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("coursematerialtype:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		//查询列表数据
		List<CourseMaterialTypeEntity> courseMaterialTypeList = courseMaterialTypeService.queryList(map);
		int total = courseMaterialTypeService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseMaterialTypeList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{materialTypeId}")
	@RequiresPermissions("coursematerialtype:info")
	public R info(@PathVariable("materialTypeId") Long materialTypeId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		longQuery(map, request, "materialTypeId");
		CourseMaterialTypeEntity courseMaterialType = courseMaterialTypeService.queryObject(map);
		return R.ok().put(courseMaterialType);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存资料库类型")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("coursematerialtype:save")
	public R save(@RequestBody CourseMaterialTypeEntity courseMaterialType , HttpServletRequest request){
		//默认状态
	  /*  courseMaterialType.setStatus(1);*/
	    //平台ID
	    courseMaterialType.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseMaterialType.setCreatePerson(getUserId());
	    //创建时间
		courseMaterialType.setCreationTime(new Date());
		//保存
		courseMaterialTypeService.save(courseMaterialType);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改资料库类型")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("coursematerialtype:update")
	public R update(@RequestBody CourseMaterialTypeEntity courseMaterialType , HttpServletRequest request){
		 //平台ID
	    courseMaterialType.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		courseMaterialType.setModifyPerson(getUserId());
		//修改时间
		courseMaterialType.setModifiedTime(new Date());
		//修改
		courseMaterialTypeService.update(courseMaterialType);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除资料库类型")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("coursematerialtype:delete")
	public R delete(@RequestBody Long[] materialTypeIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",materialTypeIds);
		courseMaterialTypeService.deleteBatch(map);	
		return R.ok();
	}
	
}
