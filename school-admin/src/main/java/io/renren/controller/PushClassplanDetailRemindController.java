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
import io.renren.entity.PushClassplanDetailRemindEntity;
import io.renren.service.PushClassplanDetailRemindService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 课次通知提醒详情表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:21:14
 */
@Controller
@RequestMapping("pushclassplandetailremind")
public class PushClassplanDetailRemindController extends AbstractController {
	@Autowired
	private PushClassplanDetailRemindService pushClassplanDetailRemindService;
	
	@RequestMapping("/pushclassplandetailremind.html")
	public String list(){
		return "pushclassplandetailremind/pushclassplandetailremind.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("pushclassplandetailremind:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		intQuery(map, request, "pushClassplanRemindId");
		stringQuery(map, request, "liveName");
		//查询列表数据
		List<PushClassplanDetailRemindEntity> pushClassplanDetailRemindList = pushClassplanDetailRemindService.queryList(map);
		int total = pushClassplanDetailRemindService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(pushClassplanDetailRemindList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
//	@ResponseBody
//	@RequestMapping("/info/{id}")
//	//@RequiresPermissions("pushclassplandetailremind:info")
//	public R info(@PathVariable("id") Long id , HttpServletRequest request){
//		Map<String, Object> map = getMap(request);
//		longQuery(map, request, "id");
//		PushClassplanDetailRemindEntity pushClassplanDetailRemind = pushClassplanDetailRemindService.queryObject(map);
//		return R.ok().put(pushClassplanDetailRemind);
//	}
	
	/**
	 * 修改
	 */
	@SysLog("修改推送课次详情")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("pushclassplandetailremind:update")
	public R update(@RequestBody PushClassplanDetailRemindEntity pushClassplanDetailRemind , HttpServletRequest request){
		//修改
		pushClassplanDetailRemind.setAuditor(getUserId());
		pushClassplanDetailRemind.setAuditTime(new Date());
		pushClassplanDetailRemindService.update(pushClassplanDetailRemind);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除推送课次详情")
	@ResponseBody
	@RequestMapping("/delDetail")
	@RequiresPermissions("pushclassplandetailremind:delete")
	public R delete(@RequestBody Integer[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		map.put("updater", getUserId());
		map.put("updateTime", new Date());
		pushClassplanDetailRemindService.deleteBatch(map);	
		return R.ok();
	}
	
	/**
	 * 反审核
	 */
	@SysLog("反审核推送课次详情")
	@ResponseBody
	@RequestMapping("/theAudit")
	@RequiresPermissions("pushclassplandetailremind:theAudit")
	public R theAudit(@RequestBody Integer id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id",id);
		map.put("audit", 2);
		map.put("auditor", getUserId());
		map.put("auditTime", new Date());
		pushClassplanDetailRemindService.theAudit(map);
		return R.ok();
	}
	
	/**
	 * 审核通过
	 */
	@SysLog("审核通过推送课次详情")
	@ResponseBody
	@RequestMapping("/auditPass")
	@RequiresPermissions("pushclassplandetailremind:audit")
	public R auditPass(@RequestBody Integer id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id",id);
		map.put("audit", 1);
		map.put("auditor", getUserId());
		map.put("auditTime", new Date());
		pushClassplanDetailRemindService.theAudit(map);
		return R.ok();
	}
	
	/**
	 * 审核不通过
	 */
	@SysLog("审核不通过推送课次详情")
	@ResponseBody
	@RequestMapping("/auditFail")
	@RequiresPermissions("pushclassplandetailremind:audit")
	public R auditFail(@RequestBody Integer id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("id",id);
		map.put("audit", 0);
		map.put("auditor", getUserId());
		map.put("auditTime", new Date());
		pushClassplanDetailRemindService.theAudit(map);
		return R.ok();
	}
	
	/**
	 * 刷新
	 */
	@SysLog("刷新推送课次详情")
	@ResponseBody
	@RequestMapping("/refresh")
	@RequiresPermissions("pushclassplandetailremind:refresh")
	public R refresh(Integer pushClassplanRemindId, String classplanId){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pushClassplanRemindId", pushClassplanRemindId);
		map.put("classplanId", classplanId);
		map.put("updater", getUserId());
		map.put("updateTime", new Date());
		
		pushClassplanDetailRemindService.refresh(map);
		
		return R.ok();
	}
}
