package io.renren.controller;

import java.util.HashMap;
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
import io.renren.entity.MallStudioEntity;
import io.renren.service.MallStudioService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 直播室档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 11:10:34
 */
@Controller
@RequestMapping("/mall/studio")
public class MallStudioController extends AbstractController {
	@Autowired
	private MallStudioService mallStudioService;
	
	
	/**
	 * 直播间列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mall:studio:list")
	public R list(String studioName, String productName, Integer page, Integer limit, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("studioName", studioName);
		map.put("productName", productName);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);	
		
		//查询列表数据（实现检索）
		List<MallStudioEntity> mallStudioList = mallStudioService.queryList(map);
		int total = mallStudioService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(mallStudioList, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 直播间列表信息
	 */
	@ResponseBody
	@RequestMapping("/info/{studioId}")
	@RequiresPermissions("mall:studio:info")
	public R info(@PathVariable("studioId") Long studioId, HttpServletRequest requset){
		String schoolId = SchoolIdUtils.getSchoolId(requset);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolId", schoolId);
		map.put("studioId", studioId);
		MallStudioEntity mallStudio = mallStudioService.queryObject(map);
		return R.ok().put("mallStudio", mallStudio);
	}
	
	/**
	 * 新增直播间
	 */
	@SysLog("保存直播间")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mall:studio:save")
	public R save(@RequestBody MallStudioEntity mallStudio,HttpServletRequest request){
		if(StringUtils.isBlank(mallStudio.getStudioName())){
			return R.error("[直播间名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+mallStudio.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isNotBlank(mallStudio.getRemake()) && mallStudio.getRemake().length() > 50){
			return R.error("[直播间描述]不能超过50个字！！！");
		}
		mallStudio.setSchoolId(SchoolIdUtils.getSchoolId(request));
		mallStudio.setCreator(getUserId());
		mallStudio.setModifier(mallStudio.getCreator());
		mallStudioService.save(mallStudio);	
		return R.ok();
	}
	
	/**
	 * 修改直播间信息
	 */
	@SysLog("修改直播间")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mall:studio:update")
	public R update(@RequestBody MallStudioEntity mallStudio){
		if(StringUtils.isBlank(mallStudio.getStudioName())){
			return R.error("[直播间名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+mallStudio.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		if(StringUtils.isNotBlank(mallStudio.getRemake()) && mallStudio.getRemake().length() > 50){
			return R.error("[直播间描述]不能超过50个字！！！");
		}
		mallStudio.setModifier(getUserId());
		mallStudioService.update(mallStudio);
		return R.ok();
	}
	
	/**
	 * 删除直播间信息
	 */
	@SysLog("删除直播间")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mall:studio:delete")
	public R delete(@RequestBody Long[] studioIds){
		mallStudioService.deleteBatch(studioIds);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用直播间")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mall:studio:pause")
	public R pause(@RequestBody Long[] studioIds){
		mallStudioService.pause(studioIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用直播间")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mall:studio:resume")
	public R resume(@RequestBody Long[] studioIds){
		mallStudioService.resume(studioIds);
		return R.ok();
	}
	
}
