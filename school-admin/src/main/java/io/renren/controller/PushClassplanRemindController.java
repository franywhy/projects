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
import io.renren.entity.PushClassplanRemindEntity;
import io.renren.pojo.PushClassplanRemindPojo;
import io.renren.service.PushClassplanRemindService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 课次通知提醒表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:19:51
 */
@Controller
@RequestMapping("pushclassplanremind")
public class PushClassplanRemindController extends AbstractController {
	@Autowired
	private PushClassplanRemindService pushClassplanRemindService;
	
	@RequestMapping("/pushclassplanremind.html")
	public String list(){
		return "pushclassplanremind/pushclassplanremind.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("pushclassplanremind:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "name");
		stringQuery(map, request, "templateName");
		//查询列表数据
		List<PushClassplanRemindPojo> pushClassplanRemindList = pushClassplanRemindService.queryListPojo(map);
		int total = pushClassplanRemindService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(pushClassplanRemindList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
//	@ResponseBody
//	@RequestMapping("/info/{id}")
//	//@RequiresPermissions("pushclassplanremind:info")
//	public R info(@PathVariable("id") Integer id , HttpServletRequest request){
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("id", id);
//		PushClassplanRemindPojo pushClassplanRemind = pushClassplanRemindService.queryObjectPojo(map);
//		return R.ok().put(pushClassplanRemind);
//	}
	
	/**
	 * 保存
	 */
	@SysLog("新增推送课次")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("pushclassplanremind:save")
	public R save(@RequestBody PushClassplanRemindEntity pushClassplanRemind , HttpServletRequest request){
	    //创建用户
		pushClassplanRemind.setCreater(getUserId());
	    //创建时间
		pushClassplanRemind.setCreateTime(new Date());
		
		pushClassplanRemind.setDr(0);
		//保存
		pushClassplanRemindService.save(pushClassplanRemind);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
//	@ResponseBody
//	@RequestMapping("/update")
//	//@RequiresPermissions("pushclassplanremind:update")
//	public R update(@RequestBody PushClassplanRemindEntity pushClassplanRemind , HttpServletRequest request){
//	    //修改用户
//		pushClassplanRemind.setUpdater(getUserId());
//		//修改时间
//		pushClassplanRemind.setUpdateTime(new Date());
//		//修改
//		pushClassplanRemindService.update(pushClassplanRemind);
//		return R.ok();
//	}
	
	/**
	 * 删除
	 */
	@SysLog("删除推送课次")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("pushclassplanremind:delete")
	public R delete(@RequestBody Integer[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		map.put("updater", getUserId());
		map.put("updateTime", new Date());
		pushClassplanRemindService.deleteBatch(map);	
		return R.ok();
	}
	
}
