package io.renren.controller;

import java.util.ArrayList;
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
import io.renren.entity.SysVersionEntity;
import io.renren.service.SysVersionService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
/**
 * 系统版本
 * @author xiechaojun
 *2017年5月27日
 */
@Controller
@RequestMapping("sysversion")
public class SysVersionController extends AbstractController {
	@Autowired
	private SysVersionService sysVersionService;
	
	@RequestMapping("/sysversion.html")
	public String list(){
		return "sysversion/sysversion.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sysversion:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "clientType");
		stringQuery(map, request, "appType");
		//查询列表数据
		List<SysVersionEntity> sysVersionList = sysVersionService.queryList(map);
		int total = sysVersionService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(sysVersionList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sysversion:info")
	public R info(@PathVariable("id") Long id , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		/*longQuery(map, request, "id");*/
		map.put("id", id);
		SysVersionEntity sysVersion = sysVersionService.queryObject(map);
		return R.ok().put(sysVersion);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存系统版本")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("sysversion:save")
	public R save(@RequestBody SysVersionEntity sysVersion , HttpServletRequest request){
	/*	//默认状态
	    sysVersion.setStatus(1);*/
	   /* //平台ID
	    sysVersion.setSchoolId(SchoolIdUtils.getSchoolId(request));*/
        //添加业务线ID
        sysVersion.setSchoolId(sysVersion.getSchoolId());
        //创建用户
        sysVersion.setCreatePerson(getUserId());
        //更新用户
        sysVersion.setModifyPerson(getUserId());
        //创建时间
        sysVersion.setCreateTime(new Date());
        //更新时间
        sysVersion.setModifyTime(new Date());
        //版本类型	
		String client_type=sysVersion.getClientType();
       //版本状态
		String status=sysVersion.getStatus();
		String[]  str=status.split(",");
		Integer isActive=Integer.valueOf(str[0]);
		Integer isGreyUpdate=Integer.valueOf(str[1]);
		sysVersion.setIsActive(isActive);
		sysVersion.setIsGreyUpdate(isGreyUpdate);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "添加失败的具体原因如下:";
		String errMsg="";
		/*if(("1,0").equals(status)){
			Map<String, Object> map = getMap(request);
			map.put("isActive", isActive);
			map.put("isGreyUpdate", isGreyUpdate);
			map.put("client_type", client_type);
			if(sysVersionService.checkSysVersion(map)){
				exceptMsg+="只存在唯一的正式线状态，请将其他灰度升级状态改成下线状态";
				exceptList.add(exceptMsg);
			}
		}
		else if(("1,1").equals(status)){
			Map<String, Object> map = getMap(request);
			map.put("isActive", isActive);
			map.put("isGreyUpdate", isGreyUpdate);
			map.put("client_type", client_type);
			if(sysVersionService.checkSysVersion(map)){
				exceptMsg+="只存在唯一的灰度升级状态，请将其他灰度升级状态改成下线状态";
				exceptList.add(exceptMsg);
			}
		}*/
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			//保存
			sysVersionService.save(sysVersion);	
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
		
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改系统版本")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("sysversion:update")
	public R update(@RequestBody SysVersionEntity sysVersion , HttpServletRequest request){
		/* //平台ID
	    sysVersion.setSchoolId(SchoolIdUtils.getSchoolId(request));*/
        //业务线ID
        sysVersion.setSchoolId(sysVersion.getSchoolId());
	    //修改用户
		sysVersion.setModifyPerson(getUserId());
		//修改时间
		sysVersion.setModifyTime(new Date());
		Long id=sysVersion.getId();
		//版本类型	
				String client_type=sysVersion.getClientType();
		       //版本状态
				String status=sysVersion.getStatus();
				String[]  str=status.split(",");
				Integer isActive=Integer.valueOf(str[0]);
				Integer isGreyUpdate=Integer.valueOf(str[1]);
				sysVersion.setIsActive(isActive);
				sysVersion.setIsGreyUpdate(isGreyUpdate);
				ArrayList<String> exceptList = new ArrayList<String>();
				String exceptMsg = "修改失败的具体原因如下:";
				String errMsg="";
				/*if(("1,0").equals(status)){
					Map<String, Object> map = getMap(request);
					map.put("isActive", isActive);
					map.put("isGreyUpdate", isGreyUpdate);
					map.put("client_type", client_type);
					map.put("id", id);
					if(sysVersionService.checkSysVersion(map)){
						exceptMsg="只存在唯一的正式线状态，请将其他正式线状态改成下线状态";
						exceptList.add(exceptMsg);
					}
				}
				else if(("1,1").equals(status)){
					Map<String, Object> map = getMap(request);
					map.put("isActive", isActive);
					map.put("isGreyUpdate", isGreyUpdate);
					map.put("client_type", client_type);
					map.put("id", id);
					if(sysVersionService.checkSysVersion(map)){
						exceptMsg="只存在唯一的灰度升级状态，请将其他灰度升级状态改成下线状态";
						exceptList.add(exceptMsg);
					}
				}*/
				for (int i = 0; i < exceptList.size(); i++)
				{
					errMsg += exceptList.get(i) + "<br>";
				}
				if(errMsg.equals("")){
					//修改
					sysVersionService.update(sysVersion);
					return R.ok();
				}
				else{
					return R.error(errMsg);
				}
		
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除系统版本")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("sysversion:delete")
	public R delete(@RequestBody Long[] ids , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",ids);
		sysVersionService.deleteBatch(map);	
		return R.ok();
	}
	
}
