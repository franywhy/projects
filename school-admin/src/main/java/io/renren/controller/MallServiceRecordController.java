package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import io.renren.entity.MallServiceRecordEntity;
import io.renren.pojo.servicerecord.ServiceRecordCreatePOJO;
import io.renren.pojo.servicerecord.ServiceRecordDisplayPOJO;
import io.renren.pojo.servicerecord.ServiceRecordUpdatePOJO;
import io.renren.service.MallServiceRecordService;
import io.renren.service.SysUserService;
import io.renren.utils.Constant;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.ShiroUtils;


/**
 * 服务类型档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-24 16:39:34
 */
@Controller
@RequestMapping("/mall/mallservicerecord")
public class MallServiceRecordController {
	@Autowired
	private MallServiceRecordService mallServiceRecordService;
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("/mallservicerecord.html")
	public String list(){
		return "mallservicerecord/mallservicerecord.html";
	}
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallservicerecord:list")
	public R list(String serviceType, Integer page, Integer limit,HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("serviceType", serviceType);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		//查询列表数据
		List<MallServiceRecordEntity> mallServiceRecordList = mallServiceRecordService.queryList(map);
		List<ServiceRecordDisplayPOJO> pojoList = new LinkedList<ServiceRecordDisplayPOJO>();
		for(MallServiceRecordEntity entity:mallServiceRecordList){
			ServiceRecordDisplayPOJO pojo = new ServiceRecordDisplayPOJO();
			pojo.setId(entity.getId());
			pojo.setServiceType(entity.getServiceType());
			pojo.setServiceContent(entity.getServiceContent());
			pojo.setCreatePerson(sysUserService.queryObject(entity.getCreatePerson()).getUsername());
			pojo.setModifyPerson(sysUserService.queryObject(entity.getModifyPerson()).getUsername());
			pojo.setCreateTime(entity.getCreateTime());
			pojo.setModifyTime(entity.getModifyTime());
			pojo.setStatus(entity.getStatus());
			pojoList.add(pojo);
		}
		int total = mallServiceRecordService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(pojoList, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{id}")
	@RequiresPermissions("mallservicerecord:info")
	public R info(@PathVariable("id") Long id){
		MallServiceRecordEntity serviceRecord = mallServiceRecordService.queryObject(id);
		ServiceRecordUpdatePOJO pojo = new ServiceRecordUpdatePOJO();
		pojo.setId(serviceRecord.getId());
		pojo.setServiceType(serviceRecord.getServiceType());
		pojo.setServiceContent(serviceRecord.getServiceContent());
		pojo.setCreatePerson(sysUserService.queryObject(serviceRecord.getCreatePerson()).getUsername());
		pojo.setModifyPerson(sysUserService.queryObject(serviceRecord.getModifyPerson()).getUsername());
		pojo.setCreateTime(serviceRecord.getCreateTime());
		pojo.setModifyTime(serviceRecord.getModifyTime());
		return R.ok().put("mallServiceRecord", pojo);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存服务类型")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mallservicerecord:save")
	public R save(@RequestBody ServiceRecordCreatePOJO pojo,HttpServletRequest request){
		if(StringUtils.isBlank(pojo.getServiceType())){
			return R.error("[服务类型]不能为空");
		}
		if(StringUtils.isBlank(pojo.getServiceContent())){
			return R.error("[服务内容]不能为空");
		}
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		MallServiceRecordEntity serviceRecord = new MallServiceRecordEntity();
		serviceRecord.setServiceContent(pojo.getServiceContent());
		serviceRecord.setServiceType(pojo.getServiceType());
		serviceRecord.setSchoolId(SchoolIdUtils.getSchoolId(request));
		serviceRecord.setCreateTime(date);
		serviceRecord.setModifyTime(date);
		serviceRecord.setModifyPerson(ShiroUtils.getUserId());
		serviceRecord.setCreatePerson(ShiroUtils.getUserId());
		serviceRecord.setStatus((int)Constant.Status.RESUME.getValue());
		mallServiceRecordService.save(serviceRecord);	
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改服务类型")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mallservicerecord:update")
	public R update(@RequestBody ServiceRecordUpdatePOJO pojo){
		if(StringUtils.isBlank(pojo.getServiceType())){
			return R.error("[服务类型]不能为空");
		}
		if(StringUtils.isBlank(pojo.getServiceContent())){
			return R.error("[服务内容]不能为空");
		}
		Date date = new Date();
		date.setTime(System.currentTimeMillis());
		MallServiceRecordEntity serviceRecord = new MallServiceRecordEntity();
		serviceRecord.setId(pojo.getId());
		serviceRecord.setServiceContent(pojo.getServiceContent());
		serviceRecord.setServiceType(pojo.getServiceType());
		serviceRecord.setModifyTime(date);
		serviceRecord.setModifyPerson(ShiroUtils.getUserId());
		mallServiceRecordService.update(serviceRecord);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除服务类型")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mallservicerecord:delete")
	public R delete(@RequestBody Long[] ids){
		mallServiceRecordService.deleteBatch(ids);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用服务类型")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mallservicerecord:pause")
	public R pause(@RequestBody Long[] ids){
		mallServiceRecordService.pause(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用服务类型")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mallservicerecord:resume")
	public R resume(@RequestBody Long[] ids){
		mallServiceRecordService.resume(ids);
		return R.ok();
	}
	
}
