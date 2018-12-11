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
import io.renren.entity.AppFeedbackEntity;
import io.renren.service.AppFeedbackService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * APP意见反馈表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 14:27:17
 */
@Controller
@RequestMapping("appfeedback")
public class AppFeedbackController extends AbstractController {
	@Autowired
	private AppFeedbackService appFeedbackService;
	
	@RequestMapping("/appfeedback.html")
	public String list(){
		return "appfeedback/appfeedback.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("appfeedback:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "content");
		stringQuery(map, request, "nickName");
		stringQuery(map, request, "mobile");
		//查询列表数据
		List<AppFeedbackEntity> appFeedbackList = appFeedbackService.queryList(map);
		int total = appFeedbackService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(appFeedbackList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{feedbackId}")
	@RequiresPermissions("appfeedback:info")
	public R info(@PathVariable("feedbackId") Long feedbackId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("feedbackId", feedbackId);
		AppFeedbackEntity appFeedback = appFeedbackService.queryObject(map);
		return R.ok().put(appFeedback);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存APP意见反馈表")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("appfeedback:save")
	public R save(@RequestBody AppFeedbackEntity appFeedback , HttpServletRequest request){
	    //平台ID
	    appFeedback.setSchoolId(SchoolIdUtils.getSchoolId(request));
		//保存
		appFeedbackService.save(appFeedback);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改APP意见反馈表")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("appfeedback:update")
	public R update(@RequestBody AppFeedbackEntity appFeedback , HttpServletRequest request){
		 //平台ID
	    appFeedback.setSchoolId(SchoolIdUtils.getSchoolId(request));
		//修改
		appFeedbackService.update(appFeedback);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除APP意见反馈表")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("appfeedback:delete")
	public R delete(@RequestBody Long[] feedbackIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",feedbackIds);
		appFeedbackService.deleteBatch(map);	
		return R.ok();
	}
	
}
