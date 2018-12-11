package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import io.renren.entity.MallAreaEntity;
import io.renren.pojo.area.AreaCreatePOJO;
import io.renren.pojo.area.AreaDisplayPOJO;
import io.renren.pojo.area.AreaUpdatePOJO;
import io.renren.service.MallAreaService;
import io.renren.service.SysUserService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.ShiroUtils;


/**
 * 省份档案业务表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 16:58:35
 */
@Controller
@RequestMapping("/mall/mallarea")
public class MallAreaController extends AbstractController {
	@Autowired
	private MallAreaService mallAreaService;
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallarea:list")
	public R list(Integer page, Integer limit,HttpServletRequest request,MallAreaEntity area){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("areaName",area.getAreaName());
		map.put("ncId",area.getNcId());
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		List<AreaDisplayPOJO> pojoList = new LinkedList<AreaDisplayPOJO>();
		//查询列表数据
		List<MallAreaEntity> mallAreaList = mallAreaService.queryList(map);
		for (MallAreaEntity entity : mallAreaList) {
			AreaDisplayPOJO pojo = new AreaDisplayPOJO();
			pojo.setAreaId(entity.getAreaId());
			pojo.setAreaName(entity.getAreaName());
			pojo.setCreatePerson(sysUserService.queryObject(entity.getCreatePerson()).getUsername());
			pojo.setModifyPerson(sysUserService.queryObject(entity.getModifyPerson()).getUsername());
			pojo.setModifyTime(entity.getModifyTime());
			pojo.setCreateTime(entity.getCreateTime());
			pojo.setStatus(entity.getStatus());
			pojo.setNcId(entity.getNcId());
			pojo.setNcCode(entity.getNcCode());
			pojoList.add(pojo);
		}
		int total = mallAreaService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(pojoList, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{areaId}")
	@RequiresPermissions("mallarea:info")
	public R info(@PathVariable("areaId") Long areaId){
		MallAreaEntity mallArea = mallAreaService.queryObject(areaId);
		AreaDisplayPOJO pojo = new AreaDisplayPOJO();
		pojo.setAreaId(mallArea.getAreaId());
        pojo.setAreaName(mallArea.getAreaName());
        pojo.setCreatePerson(sysUserService.queryObject(mallArea.getCreatePerson()).getUsername());
        pojo.setCreateTime(mallArea.getCreateTime());
        pojo.setModifyPerson(sysUserService.queryObject(mallArea.getModifyPerson()).getUsername());
        pojo.setModifyTime(mallArea.getModifyTime());
        pojo.setNcId(mallArea.getNcId());
		pojo.setNcCode(mallArea.getNcCode());
		return R.ok().put("mallArea", pojo);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存省份")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallarea:save")
	public R save(@RequestBody AreaCreatePOJO pojo,HttpServletRequest request){
		MallAreaEntity mallArea = new MallAreaEntity();
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
	    mallArea.setStatus(1);
	    mallArea.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    mallArea.setAreaName(pojo.getAreaName());
	    mallArea.setCreatePerson(ShiroUtils.getUserId());
	    mallArea.setModifyPerson(ShiroUtils.getUserId());
	    mallArea.setModifyTime(date);
	    mallArea.setCreateTime(date);
	    mallArea.setStatus(1);
	    mallArea.setNcId(pojo.getNcId());
	    mallArea.setNcCode(pojo.getNcCode());
		mallAreaService.save(mallArea);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改省份")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallarea:update")
	public R update(@RequestBody AreaDisplayPOJO pojo){
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		MallAreaEntity mallArea = new MallAreaEntity();
		mallArea.setAreaId(pojo.getAreaId());
		mallArea.setAreaName(pojo.getAreaName());
		mallArea.setModifyPerson(ShiroUtils.getUserId());
		mallArea.setModifyTime(date);
		mallArea.setNcId(pojo.getNcId());
		mallArea.setNcCode(pojo.getNcCode());
		mallAreaService.update(mallArea);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除省份")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallarea:delete")
	public R delete(@RequestBody Long[] areaIds){
		mallAreaService.deleteBatch(areaIds);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用省份")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mallarea:pause")
	public R pause(@RequestBody Long[] areaIds){
		mallAreaService.pause(areaIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用省份")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mallarea:resume")
	public R resume(@RequestBody Long[] areaIds){
		mallAreaService.resume(areaIds);
		return R.ok();
	}
	
	/**
	 * 获取选择列表
	 * */
	@ResponseBody
	@RequestMapping("/getSelectionList")
	public R getSelectionList(HttpServletRequest request){
//		Map<String, Object> map = new HashMap<>();
//		map.put("offset", (page - 1) * limit);
//		map.put("limit", limit);
//		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		Map<String, Object> map = getMapPage(request);
		return R.ok().put("areaSelections", mallAreaService.querySelectionList(map));
	}
}
