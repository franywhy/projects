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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import io.renren.entity.RelatedCommodityDetailEntity;
import io.renren.entity.RelatedCommodityEntity;
import io.renren.pojo.relatedCommodity.RelatedCommodityDetailPOJO;
import io.renren.pojo.relatedCommodity.RelatedCommodityPOJO;
import io.renren.service.RelatedCommodityDetailService;
import io.renren.service.RelatedCommodityService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 商品关联档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-28 14:41:33
 */
@Controller
@RequestMapping("/mall/relatedcommodity")
public class RelatedCommodityController extends AbstractController {
	@Autowired
	private RelatedCommodityService relatedCommodityService;
	@Autowired
	private RelatedCommodityDetailService relatedCommdityDateilService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mall:relatedcommodity:list")
	public R list(String relatedName, Integer page, Integer limit){
		Map<String, Object> map = new HashMap<>();
		map.put("relatedName", relatedName);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);	
		
		//查询列表数据
		List<RelatedCommodityEntity> relatedCommodityList = relatedCommodityService.queryList(map);
		
		int total = relatedCommodityService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(relatedCommodityList, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{relatedCommodityId}")
	@RequiresPermissions("mall:relatedcommodity:info")
	public R info(@PathVariable("relatedCommodityId") Long relatedCommodityId){
		RelatedCommodityEntity relatedCommodity = relatedCommodityService.queryObject(relatedCommodityId);
		List<RelatedCommodityDetailEntity> detailList = relatedCommdityDateilService.queryObject(relatedCommodityId);
		return R.ok().put("relatedCommodity", relatedCommodity).put("detailList", detailList);
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mall:relatedcommodity:save")
	public R save(@RequestBody RelatedCommodityPOJO relatedCommodity, HttpServletRequest request ){
		if(StringUtils.isBlank(relatedCommodity.getRelatedName())){
			return R.error("[关联名称]不能为空");
		}
		//获取平台id
		String schoolId = SchoolIdUtils.getSchoolId(request);
		//平台id
		relatedCommodity.setSchoolId(schoolId);
		//创建人
		relatedCommodity.setCreator(getUserId());
		//修改人
		relatedCommodity.setModifier(relatedCommodity.getCreator());
		//保存主表
		relatedCommodityService.save(relatedCommodity);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mall:relatedcommodity:update")
	public R update(@RequestBody RelatedCommodityPOJO relatedCommodity){
		relatedCommodity.setModifier(getUserId());
		relatedCommodityService.update(relatedCommodity);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mall:relatedcommodity:delete")
	public R delete(@RequestBody Long[] relatedCommodityIds){
		relatedCommodityService.deleteBatch(relatedCommodityIds);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mall:relatedcommodity:pause")
	public R pause(@RequestBody Long[] relatedCommodityIds){
		relatedCommodityService.pause(relatedCommodityIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mall:relatedcommodity:resume")
	public R resume(@RequestBody Long[] relatedCommodityIds){
		relatedCommodityService.resume(relatedCommodityIds);
		return R.ok();
	}
	
}
