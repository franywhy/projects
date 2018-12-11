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
import io.renren.entity.AppCourseBannerEntity;
import io.renren.pojo.AppCourseBannerPOJO;
import io.renren.service.AppCourseBannerService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * app课程banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-18 14:23:20
 */
@Controller
@RequestMapping("appcoursebanner")
public class AppCourseBannerController extends AbstractController {
	@Autowired
	private AppCourseBannerService appCourseBannerService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("appcoursebanner:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "titleName");
		stringQuery(map, request, "productName");
		//查询列表数据
		List<AppCourseBannerPOJO> appCourseBannerList = appCourseBannerService.queryListPOJO(map);
		int total = appCourseBannerService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(appCourseBannerList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("appcoursebanner:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id", id);
		AppCourseBannerPOJO appCourseBanner = appCourseBannerService.queryObjectPOJO(map);
		return R.ok().put(appCourseBanner);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存课程banner档案")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("appcoursebanner:save")
	public R save(@RequestBody AppCourseBannerEntity appCourseBanner , HttpServletRequest request){
	    //创建用户
		appCourseBanner.setCreatePerson(getUserId());
		//创建时间
		appCourseBanner.setCreateTime(new Date());
		//保存
		appCourseBannerService.save(appCourseBanner);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改课程banner档案")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("appcoursebanner:update")
	public R update(@RequestBody AppCourseBannerEntity appCourseBanner , HttpServletRequest request){
	    //修改用户
		appCourseBanner.setModifyPerson(getUserId());
		//修改时间
		appCourseBanner.setModifyTime(new Date());;
		//修改
		appCourseBannerService.update(appCourseBanner);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除课程banner档案")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("appcoursebanner:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		appCourseBannerService.deleteBatch(map);	
		return R.ok();
	}
	
}
