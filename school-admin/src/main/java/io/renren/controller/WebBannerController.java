package io.renren.controller;

import java.util.Date;
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
import io.renren.entity.WebBannerEntity;
import io.renren.service.WebBannerService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 15:47:07
 */
@Controller
@RequestMapping("mall/webbanner")
public class WebBannerController extends AbstractController {
	@Autowired
	private WebBannerService webBannerService;
	@RequestMapping("/webbanner.html")
	public String list(){
		return "webbanner/webbanner.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("webbanner:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "name");
		//查询列表数据
		List<WebBannerEntity> webBannerList = webBannerService.queryList(map);
		int total = webBannerService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(webBannerList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("webbanner:info")
	public R info(@PathVariable("id")Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
//		longQuery(map, request, "id");
		map.put("id", id);
		WebBannerEntity webBanner = webBannerService.queryObject(map);
		return R.ok().put(webBanner);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存webBanner档案")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("webbanner:save")
	public R save(@RequestBody WebBannerEntity webBanner , HttpServletRequest request){
		if(StringUtils.isBlank(webBanner.getName())){
			return R.error("[名称]不能为空！！！");
		}
		if(StringUtils.isBlank(webBanner.getPic())){
			return R.error("[图片]不能为空！！！");
		}
		/*if(StringUtils.isBlank(webBanner.getUrl())){
			return R.error("[跳转地址]不能为空！！！");
		}*/
		if(StringUtils.isBlank(webBanner.getColourText())){
			return R.error("[颜色]不能为空！！！");
		}
		if(StringUtils.isBlank(webBanner.getOrderNum()+"")){
			return R.error("[序号]不能为空！！！");
		}
		//默认状态
	    webBanner.setStatus(1);
	    //平台ID
	    webBanner.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		webBanner.setCreatePerson(getUserId());
	    //创建时间
		webBanner.setCreateTime(new Date());
		//banner颜色
		webBanner.setColourText(webBanner.getColourText());
		//保存
		webBannerService.save(webBanner);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改webBanner档案")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("webbanner:update")
	public R update(@RequestBody WebBannerEntity webBanner , HttpServletRequest request){
		if(StringUtils.isBlank(webBanner.getName())){
			return R.error("[名称]不能为空！！！");
		}
		if(StringUtils.isBlank(webBanner.getPic())){
			return R.error("[图片]不能为空！！！");
		}
		/*if(StringUtils.isBlank(webBanner.getUrl())){
			return R.error("[跳转地址]不能为空！！！");
		}*/
		if(StringUtils.isBlank(webBanner.getColourText())){
			return R.error("[颜色]不能为空！！！");
		}
		if(StringUtils.isBlank(webBanner.getOrderNum()+"")){
			return R.error("[序号]不能为空！！！");
		}
		//平台ID
	    webBanner.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //修改用户
		webBanner.setModifyPerson(getUserId());
		//修改时间
		webBanner.setModifyTime(new Date());
		//修改banner颜色
		webBanner.setColourText(webBanner.getColourText());
		//修改
		webBannerService.update(webBanner);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除webBanner档案")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("webbanner:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		webBannerService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用webBanner档案")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("webbanner:pause")
	public R pause(@RequestBody Long[] ids){
		webBannerService.pause(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用webBanner档案")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("webbanner:resume")
	public R resume(@RequestBody Long[] ids){
		webBannerService.resume(ids);
		return R.ok();
	}
}
