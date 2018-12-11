package io.renren.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.AppBannerEntity;
import io.renren.pojo.AppBannerPOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.AppBannerService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 移动端banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-20 14:55:22
 */
@Controller
@RequestMapping("appbanner")
public class AppBannerController extends AbstractController {
	@Autowired
	private AppBannerService appBannerService;
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("appbanner:list")
	public R list(String name, Long levelId, Long professionId, HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		map.put("name", name);
		map.put("levelId", levelId);
		map.put("professionId", professionId);
		//查询列表数据
		List<AppBannerPOJO> appBannerList = appBannerService.queryPojoList(map);
		int total = appBannerService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(appBannerList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("appbanner:info")
	public R info(@PathVariable("id") Long id, HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		AppBannerPOJO appBanner = appBannerService.queryPojoObject(map);
		return R.ok().put(appBanner);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存移动端banner档案")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("appbanner:save")
	public R save(@RequestBody AppBannerEntity appBanner , HttpServletRequest request){
		if(StringUtils.isBlank(appBanner.getName())){
			return R.error("[名称]不能为空");
		}
		if(StringUtils.isBlank(appBanner.getPic())){
			return R.error("[图片]不能为空");
		}
		if(StringUtils.isBlank(""+appBanner.getLevelId())){
			return R.error("[学历]不能为空");
		}
		if(StringUtils.isBlank(""+appBanner.getProfessionId())){
			return R.error("[专业]不能为空");
		}
		//排序
		appBanner.setOrderNum(0);
	    //平台ID
	    appBanner.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		appBanner.setCreatePerson(getUserId());
		//修改用户
		appBanner.setModifyPerson(appBanner.getCreatePerson());
		//保存
		appBannerService.save(appBanner);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改移动端banner档案")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("appbanner:update")
	public R update(@RequestBody AppBannerEntity appBanner , HttpServletRequest request){
		if(StringUtils.isBlank(appBanner.getName())){
			return R.error("[名称]不能为空");
		}
		if(StringUtils.isBlank(appBanner.getPic())){
			return R.error("[图片]不能为空");
		}
		if(StringUtils.isBlank(""+appBanner.getLevelId())){
			return R.error("[学历]不能为空");
		}
		if(StringUtils.isBlank(""+appBanner.getProfessionId())){
			return R.error("[专业]不能为空");
		}
		//平台ID
	    appBanner.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		appBanner.setModifyPerson(getUserId());
		//修改
		appBannerService.update(appBanner);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除移动端banner档案")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("appbanner:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		appBannerService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用移动端banner档案")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("appbanner:pause")
	public R pause(@RequestBody Long[] ids){
		appBannerService.pause(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用移动端banner档案")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("appbanner:resume")
	public R resume(@RequestBody Long[] ids){
		appBannerService.resume(ids);
		return R.ok();
	}
}
