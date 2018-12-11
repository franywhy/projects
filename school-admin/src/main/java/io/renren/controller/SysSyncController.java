package io.renren.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.service.SyncService;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;

/**
 * 同步数据接口
 * @class io.renren.controller.SysSync.java
 * @Description:
 * @author shihongjie
 * @dete 2017年6月7日
 */
@Controller
@RequestMapping("/sys/sync")
public class SysSyncController extends AbstractController {
	@Autowired
	private SyncService syncService;
	
	/**
	 * 同步课程
	 */
	@ResponseBody
	@RequestMapping("/course")
	public R syncCourse(HttpServletRequest request){
		syncService.syncCourse(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	/**
	 * 同步商品
	 */
	@ResponseBody
	@RequestMapping("/commodity")
	public R syncCommodity(HttpServletRequest request){
		syncService.syncCommodity(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	@ResponseBody
	@RequestMapping("/teacher")
	public R syncTeacher(HttpServletRequest request){
		syncService.syncTeacher(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	@ResponseBody
	@RequestMapping("/liveRoom")
	public R sysncliveRoom(HttpServletRequest request){
		syncService.syncliveRoom(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	@ResponseBody
	@RequestMapping("/courseClassPlan")
	public R syncCourseClassPlan(HttpServletRequest request){
		syncService.syncClassplan(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	
	/**
	 * 批量生成学员规划-订单未生成学员规划的
	 */
	@ResponseBody
	@RequestMapping("/saveUserplanBatch")
	public R saveUserplanBatch(HttpServletRequest request){
		syncService.saveUserplanBatch(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	@ResponseBody
	@RequestMapping("/courseUserPlanClass")
	public R syncCourseUserPlanClass(HttpServletRequest request){
		syncService.syncCourseUserPlanClass(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	/**
	 * 同步学员档案
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/users")
	public R syncUsers(HttpServletRequest request){
		syncService.syncUsers(SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	/**
	 * 更新排课信息
	 * @param request
	 * @return
	 */
	@SysLog("更新排课信息")
	@ResponseBody
	@RequestMapping("/updateCourseClassPlan")
	public R updateCourseClassPlan(HttpServletRequest request){
		syncService.updateCourseClassPlanLives();
		return R.ok();
	}
	/**
	 * 同步直播和录播
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/syncLiveLog")
	public R syncLiveLog(HttpServletRequest request){
		syncService.syncLiveLog(0,SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}
	/**
	 * 同步直播
	 * @param request
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping("/syncVideoLog")
	public R syncVideoLog(HttpServletRequest request){
		syncService.syncLiveLog(1,SchoolIdUtils.getSchoolId(request));
		return R.ok();
	}*/
	/**
	 * 更新排课明细回放
	 * @param request
	 * @return
	 */
	@SysLog("更新排课明细回放")
	@ResponseBody
	@RequestMapping("/UpdatecourseClassplanLives")
	public R UpdatecourseClassplanLives(HttpServletRequest request){
		syncService.UpdatecourseClassplanLives();
		return R.ok();
	}
	
	/**
	 * 同步自考1.0出勤数据
	 * @param request
	 * @return
	 */
//	@SysLog("同步自考1.0出勤数据")
	@ResponseBody
	@RequestMapping("/syncKSLogMongodb")
	public R syncKSLogMongodb(){
		this.syncService.syncKSLogMongodb();
		return R.ok();
	}
	
	/*
	 * 同步客服
	 */
	@ResponseBody
	@RequestMapping("/syncAgent")
	public R syncAgent(){
		this.syncService.syncAgent();
		return R.ok();
	}
	
	/*
	 * 同步客户
	 */
	@ResponseBody
	@RequestMapping("/syncCustomers")
	public R syncCustomers(Integer startOrderId, Integer endOrderId, String teacherMobile, String orderNos){
		this.syncService.syncCustomers(startOrderId, endOrderId,teacherMobile,orderNos);
		return R.ok();
	}
}
